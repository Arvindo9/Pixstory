package com.app.pixstory.ui.dashboard.you.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.base.flex.FlexboxAdapter;
import com.app.pixstory.core.dialog.page_search.PageSearch;
import com.app.pixstory.data.model.PageDetailResponse;
import com.app.pixstory.databinding.ActivityPageDetailBinding;
import com.app.pixstory.ui.dashboard.others.adapter.BadgesAdapter;
import com.app.pixstory.ui.dashboard.story_detail.StoryDetailPage;
import com.app.pixstory.ui.dashboard.upload.activity.page.CreatePageTitleActivity;
import com.app.pixstory.ui.dashboard.you.HomeYouActivity;
import com.app.pixstory.ui.messages.inbox.InboxActivity;
import com.app.pixstory.ui.messages.message.MessageActivity;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.GlobalMethods;
import com.app.pixstory.utils.Utils;
import com.app.pixstory.utils.util.Bundles;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.app.pixstory.base.BaseApplication.getContext;
import static com.app.pixstory.utils.Constants.CHAT;
import static com.app.pixstory.utils.Constants.HOME_LOADER;
import static com.app.pixstory.utils.Constants.INTERESTS;
import static com.app.pixstory.utils.Constants.MEMBERS;
import static com.app.pixstory.utils.Constants.STORIES;

public class PageDetailActivity extends BaseActivity<ActivityPageDetailBinding, PageDetailViewModel> implements PageDetailNavigator {
    private ActivityPageDetailBinding binding;
    private PageDetailViewModel viewModel;
    private String type;
    private PageDetailResponse.Data pageData;
    private int pageId;
    private String title;

    @Override
    public int getLayout() {
        return R.layout.activity_page_detail;
    }

    @Override
    protected void getBinding(ActivityPageDetailBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(PageDetailViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected Class<PageDetailViewModel> setViewModel() {
        return PageDetailViewModel.class;
    }

    @Override
    protected void init() {
        viewModel.setNavigator(this);
        clickListener();
        toolbar(binding.toolbar.toolbar, binding.toolbar.toolbarTitle, "");
        Bundle bundle = getIntent().getBundleExtra(Constants.KEY_DEFAULT_ACTIVITY_BUNDLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.recyclerView.getContext(), linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getBaseContext(), R.drawable.divider_10dp)));
        binding.recyclerView.addItemDecoration(dividerItemDecoration);
        binding.recyclerView.setLayoutManager(linearLayoutManager);

        if (bundle != null) {
            pageId = bundle.getInt("page_id");
            title = bundle.getString("title");
            viewModel.getPageDetail(pageId);
            //viewModel.getPageDetail(1);
            Utils.setImageFromPath(this, binding.banner, bundle.getString("image"), binding.progressBar);
            if (title != null){
                binding.pageTitle.setVisibility(View.VISIBLE);
                binding.pageTitle.setText(title);
            } else {
                binding.pageTitle.setVisibility(View.GONE);
            }

        } else {
            Intent intent = getIntent();
           Bundle pageBundle = intent.getBundleExtra("bundl");
            pageId = pageBundle.getInt("page_id");
            title = pageBundle.getString("title");
            viewModel.getPageDetail(pageId);
            //viewModel.getPageDetail(1);
            Utils.setImageFromPath(this, binding.banner, pageBundle.getString("image"), binding.progressBar);
            if (title != null){
                binding.pageTitle.setVisibility(View.VISIBLE);
                binding.pageTitle.setText(title);
            } else {
                binding.pageTitle.setVisibility(View.GONE);
            }
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

        binding.toolbar.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //viewModel.followPage(pageId,pageData.getPage().getIsFollow() == 0 ? 1 : 0);
                viewModel.followPage(1, pageData.getPage().getIsFollow() == 0 ? 1 : 0);
            }
        });
    }

    private void clickListener() {
        binding.storyType.stories.setOnClickListener(this::onClick);
        binding.storyType.members.setOnClickListener(this::onClick);
        binding.storyType.chat.setOnClickListener(this::onClick);
        binding.storyType.interests.setOnClickListener(this::onClick);
        binding.profileImage.setOnClickListener(this::onClick);
        binding.seeMore.setOnClickListener(this::onClick);
        binding.toolbar.edit.setOnClickListener(this::onClick);
    }

    @Override
    public void pageDetailFetched(PageDetailResponse.Data pageData) {
        this.pageData = pageData;

        if (pageData.getPage().getIsPageAdmin() == 2) {
            binding.toolbar.follow.setVisibility(View.VISIBLE);
            binding.toolbar.report.setVisibility(View.VISIBLE);
            binding.toolbar.edit.setVisibility(View.GONE);
            binding.toolbar.toolbarTitle.setText("");
            binding.title.setText(getResources().getString(R.string.stories_plus));
            binding.title.setOnClickListener(this::onClick);
        } else if (pageData.getPage().getIsPageAdmin() == 1) {
            binding.toolbar.follow.setVisibility(View.GONE);
            binding.toolbar.report.setVisibility(View.GONE);
            binding.toolbar.edit.setVisibility(View.VISIBLE);
            binding.toolbar.toolbarTitle.setText(pageData.getPage().getPageView() + " Views");
            binding.title.setText(getResources().getString(R.string.stories_plus));
            binding.title.setOnClickListener(this::onClick);
        } else {
            binding.toolbar.follow.setVisibility(View.VISIBLE);
            binding.toolbar.report.setVisibility(View.VISIBLE);
            binding.toolbar.edit.setVisibility(View.GONE);
            binding.toolbar.toolbarTitle.setText("");
            binding.title.setText(getResources().getString(R.string.page_stories));
        }

        if (pageData.getPage().getPageStory() != null) {
            binding.listLayout.setVisibility(View.VISIBLE);
            setStoriesItem(pageData.getPage().getPageStory());
            type = Constants.STORIES;
        }

        if (pageData.getPage().getIsTrending() == 0) {
            binding.trending.setVisibility(View.GONE);
        } else {
            binding.trending.setVisibility(View.VISIBLE);
        }

        Utils.setImageFromPath(getBaseContext(), binding.profileImage, pageData.getPage().getCoverImgPath(), binding.progress);
        binding.creatorTitle.setText(pageData.getPage().getAbout());
        binding.creatorName.setText(pageData.getPage().getCreatorName() + " " + pageData.getPage().getCreatorLname());
        binding.time.setText(pageData.getPage().getPublishedOn());
        binding.integrityScore.setText("Integrity score : " + pageData.getPage().getIntegrityScore());

        if (pageData.getPage().getCreatorBadge() != null && pageData.getPage().getCreatorBadge().size() > 0) {
            BadgesAdapter badgesAdapter = new BadgesAdapter(pageData.getPage().getCreatorBadge());
            binding.badgeRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
            binding.badgeRecyclerView.setAdapter(badgesAdapter);
        }

        if (pageData.getPage().getIsFollow() == 0) {
            binding.toolbar.follow.setText("Follow");
        } else {
            binding.toolbar.follow.setText("Following");
        }

    }

    @Override
    public void pageFollowed(String message, int isFollow) {
        pageData.getPage().setIsFollow(isFollow);
        if (isFollow == 0)
            binding.toolbar.follow.setText("Follow");
        else
            binding.toolbar.follow.setText("Following");
    }

    @Override
    public void pageDataFetched(PageDetailResponse.Data pageData, String type) {
        GlobalMethods.intentMethodWithBundle(this, Bundles.getInstance().setDataForResult(type, pageId, "", "", ""), ResultActivity.class);
    }

    private void onClick(View view) {

        switch (view.getId()) {

            case R.id.stories:
                if (!type.equalsIgnoreCase(STORIES)) {
                    binding.listLayout.setVisibility(View.VISIBLE);
                    if (pageData.getPage().getIsPageAdmin() == 0)
                        binding.title.setText(getResources().getString(R.string.page_stories));
                    else
                        binding.title.setText(getResources().getString(R.string.stories_plus));
                    setStoriesItem(pageData.getPage().getPageStory());
                    type = Constants.STORIES;
                }
                break;
            case R.id.members:
                if (!type.equalsIgnoreCase(MEMBERS)) {
                    binding.listLayout.setVisibility(View.VISIBLE);
                    if (pageData.getPage().getIsPageAdmin() == 0)
                        binding.title.setText(getResources().getString(R.string.page_members));
                    else
                        binding.title.setText(getResources().getString(R.string.members_plus));
                    setMembersItem(pageData.getPage().getPageMember());
                    type = MEMBERS;
                }
                break;
            case R.id.interests:
                if (!type.equalsIgnoreCase(INTERESTS)) {
                    binding.listLayout.setVisibility(View.VISIBLE);
                    binding.title.setText(getResources().getString(R.string.page_interest));
                    setInterestItem(pageData.getPage().getPageInterest());
                    type = INTERESTS;
                }
                break;
            case R.id.chat:
                if (!type.equalsIgnoreCase(CHAT)) {
                    binding.listLayout.setVisibility(View.VISIBLE);
                    binding.title.setText(getResources().getString(R.string.page_chat));
                    setChatItem();
                    type = CHAT;
                }
                break;
            case R.id.profile_image:
               // GlobalMethods.intentMethodWithBundle(this, Bundles.getInstance().setMessagePage(Constants.MESSAGE_TYPE_INBOX, "25"), MessageActivity.class);
                break;
            case R.id.see_more:
                if (type.equalsIgnoreCase(Constants.STORIES)) {
                    GlobalMethods.intentMethodWithBundle(this, Bundles.getInstance().setDataForResult(type, pageId, "", "", ""), ResultActivity.class);
                } else if (type.equalsIgnoreCase(MEMBERS)) {
                    GlobalMethods.intentMethodWithBundle(this, Bundles.getInstance().setDataForResult(type, pageId, "", "", ""), ResultActivity.class);
                } else if (type.equalsIgnoreCase(INTERESTS)) {

                } else if (type.equalsIgnoreCase(CHAT)) {

                }
                break;

            case R.id.title:

                PageSearch.searchPage(this, type, new PageSearch.SearchCallback() {
                    @Override
                    public void search(String searchText, String filter, String findInPage) {
                        GlobalMethods.intentMethodWithBundle(getContext(), Bundles.getInstance().setDataForResult(type, pageId, searchText, filter, findInPage), ResultActivity.class);
                    }
                });

                break;

            case R.id.edit:

                Bundle bundle = new Bundle();
                bundle.putString("type", Constants.PAGE_UPDATE);
                bundle.putString("data", Utils.getGsonParser().toJson(pageData));
                startUploadActivity(CreatePageTitleActivity.class, bundle);
                finish();

                break;
        }
    }

    private void setStoriesItem(List<PageDetailResponse.PageStory> storyList) {

        StoriesAdapter storiesAdapter = new StoriesAdapter(storyList);
        binding.recyclerView.setAdapter(storiesAdapter);
        storiesAdapter.setListener(new StoriesAdapter.ItemCallback() {
            @Override
            public void itemClicked(Bundle bundle) {
                startUploadActivity(StoryDetailPage.class, bundle);
            }
        });

        binding.storyType.stories.setBackgroundColor(getResources().getColor(R.color.white));
        binding.storyType.members.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.storyType.interests.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.storyType.chat.setBackgroundColor(getResources().getColor(R.color.neon));

    }

    private void setMembersItem(List<PageDetailResponse.PageMember> memberList) {

        MembersAdapter membersAdapter = new MembersAdapter(memberList);
        binding.recyclerView.setAdapter(membersAdapter);
        membersAdapter.setListener(new MembersAdapter.ItemCallback() {
            @Override
            public void itemClicked(Bundle bundle) {
                startUploadActivity(HomeYouActivity.class, bundle);
            }
        });

        binding.storyType.stories.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.storyType.members.setBackgroundColor(getResources().getColor(R.color.white));
        binding.storyType.interests.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.storyType.chat.setBackgroundColor(getResources().getColor(R.color.neon));

    }

    private void setInterestItem(List<PageDetailResponse.PageInterest> interestList) {
        List<String> interest = new ArrayList<>();
        for (int i = 0; i < interestList.size(); i++) {
            interest.add(interestList.get(i).getTitle());
        }
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        FlexboxAdapter adapter = new FlexboxAdapter(interest, false, 8);
        binding.recyclerView.setAdapter(adapter);
        binding.storyType.stories.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.storyType.members.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.storyType.interests.setBackgroundColor(getResources().getColor(R.color.white));
        binding.storyType.chat.setBackgroundColor(getResources().getColor(R.color.neon));

    }

    private void setChatItem() {

        ChatAdapter chatAdapter = new ChatAdapter(getLoremDummyData());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(chatAdapter);
        binding.storyType.stories.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.storyType.members.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.storyType.interests.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.storyType.chat.setBackgroundColor(getResources().getColor(R.color.white));

    }

    private List<String> getLoremDummyData() {
        List<String> data = new ArrayList<>();
        data.add("Lorem Ipsum dolor sit");
        data.add("Lorem Ipsum dolor sit");
        data.add("Lorem Ipsum dolor sit");
        data.add("Lorem Ipsum dolor sit");
        data.add("Lorem Ipsum dolor sit");
        data.add("Lorem Ipsum dolor sit");
        data.add("Lorem Ipsum dolor sit");
        data.add("Lorem Ipsum dolor sit");
        return data;
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
}
