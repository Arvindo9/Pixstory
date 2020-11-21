package com.app.pixstory.ui.dashboard.upload.activity.story;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.core.binding.BindingUtils;
import com.app.pixstory.core.dialog.story.StoryPublished;
import com.app.pixstory.data.model.interest.Interest;
import com.app.pixstory.data.model.story.StoryData;
import com.app.pixstory.data.model.upload.Data;
import com.app.pixstory.data.model.upload_details.DetailData;
import com.app.pixstory.databinding.ActivityStoryPageBinding;
import com.app.pixstory.ui.dashboard.home.adapter.BadgeAdapter;
import com.app.pixstory.ui.dashboard.upload.adapter.StorySlideAdapter;
import com.app.pixstory.ui.dashboard.upload.model.MediaModel;
import com.app.pixstory.ui.dashboard.upload.model.UploadViewModel;
import com.app.pixstory.ui.dashboard.upload.navigator.UploadNavigator;
import com.app.pixstory.ui.interests.Interests;
import com.app.pixstory.utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.app.pixstory.base.BaseApplication.getContext;
import static com.app.pixstory.utils.Constants.CREATION_TYPE;
import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;
import static com.app.pixstory.utils.Constants.IS_TYPE;
import static com.app.pixstory.utils.Constants.PUBLISH_OPTION;
import static com.app.pixstory.utils.Constants.STORY_FORMAT;
import static com.app.pixstory.utils.Constants.STORY_ID;

public class StoryPreviewActivity extends BaseActivity<ActivityStoryPageBinding, UploadViewModel> implements UploadNavigator {
    private com.app.pixstory.databinding.ActivityStoryPageBinding binding;
    private UploadViewModel viewModel;
    private ArrayList<Data> createStoryModels;
    public BottomSheetBehavior publishOptionBehaviour;
    float density = 0.0f;
    private static int NUM_PAGES = 0;
    private static int currentPage = 0;

    @Override
    public int getLayout() {
        return R.layout.activity_story_page;
    }

    @Override
    protected void getBinding(ActivityStoryPageBinding binding) {

        this.binding = binding;
    }

    @Override
    protected void getViewModel(UploadViewModel viewModel) {

        this.viewModel = viewModel;
    }

    @Override
    protected Class<UploadViewModel> setViewModel() {
        return UploadViewModel.class;
    }

    @Override
    protected void init() {
        initialization();
        initPager();
        clickListener();
        viewModel.setNavigator(this);
        viewModel.getLoading().observe(this, o -> {
            if ((Boolean) o) {
                showSimmerLoader(DEFAULT_LOADER);
            } else {
                hideSimmerLoader();
            }
        });

        otpVerified();
        choosePublishOption();
        // GET USER PROFILE DATA
        getProfileData();
    }

    private void getProfileData() {
        binding.cardUserProfile.followChat.setVisibility(View.GONE);
        binding.cardUserProfile.report.setVisibility(View.GONE);
        // get user detail
        viewModel.getProfileData();
        viewModel.getUserLiveData().observe(this, user -> {
            BindingUtils.setStringPhotos(binding.cardUserProfile.profileImage, user.getProfileImage(), binding.cardUserProfile.progressBar);
            binding.cardUserProfile.username.setText(user.getUsername());
            binding.cardUserProfile.integrityScore.setText(getResources().getString(R.string.integrity_score) + user.getIntegrityScore());
        });

        //follower
        viewModel.getUserLiveDatas().observe(this, data -> {
            // SHOW BADGE
            BadgeAdapter badgeAdapter = new BadgeAdapter(new ArrayList<>());
            binding.cardUserProfile.badgeRecyclerView.setAdapter(badgeAdapter);
            binding.cardUserProfile.badgeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
            badgeAdapter.addItems(data.getBadges());
        });

    }

    private void initialization() {
        toolbar(binding.layoutToolbar.toolbar, binding.layoutToolbar.toolbarTitle, R.string.preview);
        binding.cardUserProfile.progressBar.setVisibility(View.GONE);
        binding.cardUserProfile.title.setText(viewModel.getStoryTitle());
        binding.cardStoryNarrative.narrative.setText(viewModel.getStoryNarrative());
        Bundle bundle = getIntent().getBundleExtra(Constants.KEY_DEFAULT_ACTIVITY_BUNDLE);
        if (bundle != null) {
            createStoryModels = bundle.getParcelableArrayList("key");
            String from = getIntent().getStringExtra("from");
            if (from.equals("quick")) {
                itemLists(createStoryModels);
            } else if (from.equals("flick")) {
                itemAnimationLists(createStoryModels);
            } else {
                itemLists(createStoryModels);
            }
        }
    }

    private void clickListener() {
        binding.cardStorySlide.next.setOnClickListener(this::onClick);
        binding.cardStorySlide.previous.setOnClickListener(this::onClick);
        binding.publish.setOnClickListener(this::onClick);
        binding.bottomSheetPublishOption.closePublishOption.setOnClickListener(this::onClick);
        binding.bottomSheetPublishOption.publishStory.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        if (view.getId() == R.id.next) {
            binding.cardStorySlide.pager.setCurrentItem(getItem(+1), true);
        } else if (view.getId() == R.id.previous) {
            binding.cardStorySlide.pager.setCurrentItem(getItem(-1), true);
        } else if (view.getId() == R.id.publish) {
            publishOptionBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getId() == R.id.close_publish_option) {
            publishOptionBehaviour.setHideable(true);
            publishOptionBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (view.getId() == R.id.publish_story) {
            // image id ------------
            LinkedHashMap<Integer, Integer> imageId = new LinkedHashMap<>();
            for (int i = 0; i < createStoryModels.size(); i++) {
                imageId.put(createStoryModels.get(i).getId(), createStoryModels.get(i).getId());

            }

            int[] image_id = new int[imageId.size()];

            int i = 0;
            for (int key : imageId.keySet()) {
                image_id[i++] = key;
            }
            //-----------------------interest id------------------------
            int[] interest_id = new int[0];
            if (Interests.interestsId != null) {
                interest_id = new int[Interests.interestsId.size()];
                int j = 0;
                for (int key : Interests.interestsId.keySet()) {
                    interest_id[j++] = key;
                }
            }

            //--------------------------------

            if (IS_TYPE.equals("support")) {
                viewModel.publishStory(image_id, STORY_FORMAT, PUBLISH_OPTION, interest_id, IS_TYPE, STORY_ID);
            } else if (IS_TYPE.equals("challenge")) {
                viewModel.publishStory(image_id, STORY_FORMAT, PUBLISH_OPTION, interest_id, IS_TYPE, STORY_ID);
            } else if (IS_TYPE.equals("default")) {
                viewModel.publishStory(image_id, STORY_FORMAT, PUBLISH_OPTION, interest_id, "", 0);
            }
        }
    }

    private void otpVerified() {
        publishOptionBehaviour = BottomSheetBehavior.from(binding.bottomSheetPublishOption.publishOption);
        // set callback for changes
        publishOptionBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    publishOptionBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.blur.setVisibility(View.VISIBLE);
                binding.blur.setAlpha(slideOffset);
                binding.bottomSheetPublishOption.closePublishOption.animate().scaleX(0.05f + slideOffset).scaleY(0.05f + slideOffset).setDuration(0).start();
            }
        });
    }

    private void choosePublishOption() {
        binding.bottomSheetPublishOption.publishOptionRg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.for_public) {
                PUBLISH_OPTION = "public";
            } else if (checkedId == R.id.friend) {
                PUBLISH_OPTION = "friends";
            } else if (checkedId == R.id.only_for_me) {
                PUBLISH_OPTION = "only_me";
            }
        });
    }

    private int getItem(int i) {
        return binding.cardStorySlide.pager.getCurrentItem() + i;
    }


    private void initPager() {
        binding.cardStorySlide.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == (binding.cardStorySlide.pager.getAdapter().getCount() - 1)) {
                    binding.cardStorySlide.next.setVisibility(View.GONE);
                } else {
                    binding.cardStorySlide.next.setVisibility(View.VISIBLE);
                }

                if (position == 0) {
                    binding.cardStorySlide.previous.setVisibility(View.GONE);
                } else {
                    binding.cardStorySlide.previous.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void itemLists(ArrayList<Data> createStoryModels) {
        binding.cardStorySlide.pager.setAdapter(new StorySlideAdapter(getApplicationContext(), createStoryModels, 0, this));
        binding.cardStorySlide.pager.setCurrentItem(0);
    }

    public void itemAnimationLists(ArrayList<Data> createStoryModels) {
        binding.cardStorySlide.pager.setAdapter(new StorySlideAdapter(getApplicationContext(), createStoryModels, 0, this));
        try {
            density = getResources().getDisplayMetrics().density;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Set circle indicator radius
        NUM_PAGES = createStoryModels.size();
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                binding.cardStorySlide.pager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);

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
    public void onMyPhotoResponse(boolean b, List<Data> data, String type) {

    }

    @Override
    public void onDetailResponse(boolean b, DetailData detailData) {

    }

    @Override
    public void onStoryResponse(boolean b, StoryData storyData) {
        if (b) {
            publishOptionBehaviour.setHideable(true);
            publishOptionBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            CREATION_TYPE = "story";
            StoryPublished.storyPublishedSuccessfully(this, this, getIntent(), IS_TYPE);
        } else {
            showToast("Story not created");
        }

    }

    @Override
    public void onFileUploadSuccess(MediaModel model, Data uploadModel) {

    }

    @Override
    public void onMasterInterest(Boolean success, List<Interest> interest) {

    }

    @Override
    public void message(int message) {

    }

    @Override
    public void message(String message) {

    }
}
