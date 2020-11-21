package com.app.pixstory.ui.dashboard.you.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.core.dialog.page_search.PageSearch;
import com.app.pixstory.data.model.MemberData;
import com.app.pixstory.data.model.StoryData;
import com.app.pixstory.databinding.ActivityResultBinding;
import com.app.pixstory.ui.dashboard.story_detail.StoryDetailPage;
import com.app.pixstory.ui.dashboard.you.HomeYouActivity;
import com.app.pixstory.ui.messages.inbox.InboxActivity;
import com.app.pixstory.ui.messages.message.MessageActivity;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.GlobalMethods;
import com.app.pixstory.utils.util.Bundles;

import java.util.Objects;

import static com.app.pixstory.base.BaseApplication.getContext;
import static com.app.pixstory.utils.Constants.HOME_LOADER;

public class ResultActivity extends BaseActivity<ActivityResultBinding, ResultViewModel> implements ResultNavigator {

    private ResultViewModel viewModel;
    private ActivityResultBinding binding;
    private String type;
    private int pageId;
    private SeeMoreStoriesAdapter seeMoreStoriesAdapter;
    private SeeMoreMemberAdapter seeMoreMemberAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_result;
    }

    @Override
    protected void getBinding(ActivityResultBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(ResultViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected Class<ResultViewModel> setViewModel() {
        return ResultViewModel.class;
    }

    @Override
    protected void init() {
        viewModel.setNavigator(this);
        toolbar(binding.toolbar.toolbar, binding.toolbar.toolbarTitle, "Results");
        type = Bundles.getInstance().getType(getIntent().getExtras());
        pageId = Bundles.getInstance().getPageId(getIntent().getExtras());
        String findInPage = Bundles.getInstance().getFindInPage(getIntent().getExtras());
        String searchText = Bundles.getInstance().getSearchText(getIntent().getExtras());
        String searchType = Bundles.getInstance().getSearchType(getIntent().getExtras());
        setOnClickListers();
        initStoryAdapter();
        initMembersAdapter();

        if (type.equalsIgnoreCase(Constants.MEMBERS)) {

            if (searchType.isEmpty() && searchText.isEmpty()) {
                viewModel.getPageMembers(pageId);
                //viewModel.getPageMembers(1);

            } else {
                viewModel.getSearchedPageMembers(pageId, searchText, searchType, findInPage);
            }

            viewModel.getMembersPagedList().observe(this, new Observer<PagedList<MemberData>>() {
                @Override
                public void onChanged(PagedList<MemberData> data) {
                    if (data != null) {
                        openUsers();
                        binding.recyclerViewMember.setVisibility(View.VISIBLE);
                        seeMoreMemberAdapter.submitList(data);
                    }
                }
            });

        } else if (type.equalsIgnoreCase(Constants.STORIES)) {

            if (searchType.isEmpty() && searchText.isEmpty()) {
                viewModel.getPageStories(pageId);
                //viewModel.getPageStories(1);
            } else {
                viewModel.getSearchedPageStories(pageId, searchText, searchType, findInPage);
            }

            viewModel.getStoriesPagedList().observe(this, new Observer<PagedList<StoryData>>() {
                @Override
                public void onChanged(PagedList<StoryData> data) {
                    if (data != null) {
                        openStories();
                        binding.recyclerViewStory.setVisibility(View.VISIBLE);
                        seeMoreStoriesAdapter.submitList(data);
                    }
                }
            });

        }

        viewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    showSimmerLoader(HOME_LOADER);
                } else {
                    hideSimmerLoader();
                }
            }
        });

        viewModel.getProgressLoadStatus().observe(this, status -> {
            if (Objects.requireNonNull(status).equalsIgnoreCase(Constants.LOADING_FIRST)) {
                firstLoadStart(type);
            } else if (status.equalsIgnoreCase(Constants.LOADED)) {
                stopLoading(type);
            } else {
                secondLoadStart(type);
            }
        });

    }

    private void initMembersAdapter() {
        seeMoreMemberAdapter = new SeeMoreMemberAdapter();
        binding.recyclerViewMember.setLayoutManager(new LinearLayoutManager(this));
        //binding.recyclerViewMember.addItemDecoration(new DividerItemDecoration(binding.recyclerViewMember.getContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerViewMember.setHasFixedSize(true);
        binding.recyclerViewMember.setAdapter(seeMoreMemberAdapter);
        seeMoreMemberAdapter.setListener(new SeeMoreMemberAdapter.ItemClick() {
            @Override
            public void follow(MemberData data, int position) {
                viewModel.doFollow(data.getUserId(), data.getIsFollow() == 0 ? 1 : 0, position);
            }

            @Override
            public void chat(MemberData data,int position) {
                startActivity(MessageActivity.class,Bundles.getInstance().setMessage(type, data.getUserId(), data.getUsername()));
            }

            @Override
            public void itemClicked(Bundle bundle) {
                startUploadActivity(HomeYouActivity.class, bundle);
            }
        });
    }

    private void initStoryAdapter() {
        seeMoreStoriesAdapter = new SeeMoreStoriesAdapter();
        binding.recyclerViewStory.setLayoutManager(new LinearLayoutManager(this));
        //binding.recyclerViewStory.addItemDecoration(new DividerItemDecoration(binding.recyclerViewStory.getContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerViewStory.setHasFixedSize(true);
        binding.recyclerViewStory.setAdapter(seeMoreStoriesAdapter);
        seeMoreStoriesAdapter.setListener(new SeeMoreStoriesAdapter.ItemCallback() {
            @Override
            public void itemClicked(Bundle bundle) {
                startUploadActivity(StoryDetailPage.class, bundle);
            }

            @Override
            public void shareStory(int storyId) {
                viewModel.shareStory(storyId, pageId);
            }
        });

    }

    private void setOnClickListers() {
        binding.resultType.faq.setOnClickListener(this::OnClick);
        binding.resultType.users.setOnClickListener(this::OnClick);
        binding.resultType.photos.setOnClickListener(this::OnClick);
        binding.resultType.pages.setOnClickListener(this::OnClick);
        binding.resultType.stories.setOnClickListener(this::OnClick);
        binding.toolbar.search.setOnClickListener(this::OnClick);
    }

    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.faq:
                type = "faq";
                openFAQ();
                break;
            case R.id.users:
                if (!type.equalsIgnoreCase(Constants.MEMBERS)) {
                    type = Constants.MEMBERS;
                    if (viewModel.getMembersPagedList().getValue() == null) {
                        viewModel.getPageMembers(pageId);
                        //viewModel.getPageMembers(1);
                        viewModel.getMembersPagedList().observe(this, new Observer<PagedList<MemberData>>() {
                            @Override
                            public void onChanged(PagedList<MemberData> data) {
                                if (data != null) {
                                    openUsers();
                                    seeMoreMemberAdapter.submitList(data);
                                    binding.recyclerViewMember.setVisibility(View.VISIBLE);
                                    binding.recyclerViewStory.setVisibility(View.GONE);
                                }
                            }
                        });
                    } else {
                        openUsers();
                        seeMoreMemberAdapter.submitList(viewModel.getMembersPagedList().getValue());
                        binding.recyclerViewMember.setVisibility(View.VISIBLE);
                        binding.recyclerViewStory.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.photos:
                type = "photos";
                openPhotos();
                break;
            case R.id.pages:
                type = "pages";
                openPages();
                break;
            case R.id.stories:
                if (!type.equalsIgnoreCase(Constants.STORIES)) {
                    type = Constants.STORIES;
                    if (viewModel.getStoriesPagedList().getValue() == null) {
                        viewModel.getPageStories(pageId);
                        //viewModel.getPageStories(1);
                        viewModel.getStoriesPagedList().observe(this, new Observer<PagedList<StoryData>>() {
                            @Override
                            public void onChanged(PagedList<StoryData> data) {
                                if (data != null) {
                                    openStories();
                                    seeMoreStoriesAdapter.submitList(data);
                                    binding.recyclerViewMember.setVisibility(View.GONE);
                                    binding.recyclerViewStory.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    } else {
                        openStories();
                        seeMoreStoriesAdapter.submitList(viewModel.getStoriesPagedList().getValue());
                        binding.recyclerViewMember.setVisibility(View.GONE);
                        binding.recyclerViewStory.setVisibility(View.VISIBLE);
                    }
                }
                break;

            case R.id.search:

                PageSearch.searchPage(this, type, new PageSearch.SearchCallback() {
                    @Override
                    public void search(String searchText, String filter, String findInPage) {

                        if (type.equalsIgnoreCase(Constants.STORIES)) {

                            viewModel.getSearchedPageStories(pageId, searchText, filter, findInPage);
                            viewModel.getStoriesPagedList().observe(ResultActivity.this, new Observer<PagedList<StoryData>>() {
                                @Override
                                public void onChanged(PagedList<StoryData> data) {
                                    if (data != null) {
                                        openStories();
                                        binding.recyclerViewStory.setVisibility(View.VISIBLE);
                                        seeMoreStoriesAdapter.submitList(data);
                                    }
                                }
                            });

                        } else {
                            viewModel.getSearchedPageMembers(pageId, searchText, filter, findInPage);
                            viewModel.getMembersPagedList().observe(ResultActivity.this, new Observer<PagedList<MemberData>>() {
                                @Override
                                public void onChanged(PagedList<MemberData> data) {
                                    if (data != null) {
                                        openUsers();
                                        binding.recyclerViewMember.setVisibility(View.VISIBLE);
                                        seeMoreMemberAdapter.submitList(data);
                                    }
                                }
                            });
                        }

                    }
                });

                break;

        }
    }

    private void openFAQ() {
        binding.resultType.stories.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.resultType.pages.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.resultType.photos.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.resultType.users.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.resultType.faq.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void openUsers() {
        binding.resultType.stories.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.resultType.pages.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.resultType.photos.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.resultType.users.setBackgroundColor(getResources().getColor(R.color.white));
        binding.resultType.faq.setBackgroundColor(getResources().getColor(R.color.neon));
    }

    private void openPhotos() {
        binding.resultType.stories.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.resultType.pages.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.resultType.photos.setBackgroundColor(getResources().getColor(R.color.white));
        binding.resultType.users.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.resultType.faq.setBackgroundColor(getResources().getColor(R.color.neon));
    }

    private void openPages() {
        binding.resultType.stories.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.resultType.pages.setBackgroundColor(getResources().getColor(R.color.white));
        binding.resultType.photos.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.resultType.users.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.resultType.faq.setBackgroundColor(getResources().getColor(R.color.neon));
    }

    private void openStories() {
        binding.resultType.stories.setBackgroundColor(getResources().getColor(R.color.white));
        binding.resultType.pages.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.resultType.photos.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.resultType.users.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.resultType.faq.setBackgroundColor(getResources().getColor(R.color.neon));
    }

    public void firstLoadStart(String type) {
        if (type.equalsIgnoreCase(Constants.MEMBERS))
            binding.recyclerViewMember.setVisibility(View.GONE);
        else
            binding.recyclerViewStory.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }

    public void secondLoadStart(String type) {
        if (type.equalsIgnoreCase(Constants.MEMBERS)) {
            binding.recyclerViewMember.setVisibility(View.VISIBLE);
            binding.recyclerViewMember.setClipToPadding(true);
            binding.recyclerViewMember.setPadding(0, 0, 0, 108);
        } else {
            binding.recyclerViewStory.setVisibility(View.VISIBLE);
            binding.recyclerViewStory.setClipToPadding(true);
            binding.recyclerViewStory.setPadding(0, 0, 0, 108);
        }
        binding.shimmer.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    public void stopLoading(String type) {

        if (type.equalsIgnoreCase(Constants.MEMBERS)) {
            binding.recyclerViewMember.setVisibility(View.VISIBLE);
            binding.recyclerViewMember.setClipToPadding(false);
            binding.recyclerViewMember.setPadding(0, 0, 0, 108);
        } else {
            binding.recyclerViewStory.setVisibility(View.VISIBLE);
            binding.recyclerViewStory.setClipToPadding(false);
            binding.recyclerViewStory.setPadding(0, 0, 0, 108);
        }
        binding.shimmer.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.GONE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void message(int message) {
        showToast(message);
    }

    @Override
    public void message(String message) {
        showToast(message);
    }

    @Override
    public void followStatusChanged(String message, int position, int follow) {
        Objects.requireNonNull(Objects.requireNonNull(viewModel.getMembersPagedList().getValue()).get(position)).setIsFollow(follow);
        seeMoreMemberAdapter.notifyItemChanged(position);
    }

    @Override
    public void storyShared(String message) {
        showToast(message);
    }
}
