package com.app.pixstory.ui.dashboard.you;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.core.binding.BindingUtils;
import com.app.pixstory.data.model.get_story_user.Story;
import com.app.pixstory.data.model.get_story_user.StoryUserData;
import com.app.pixstory.data.model.page_list.PageListData;
import com.app.pixstory.data.model.upload.Data;
import com.app.pixstory.databinding.ActivityHomeYouBinding;
import com.app.pixstory.ui.dashboard.DashboardActivity;
import com.app.pixstory.ui.dashboard.home.adapter.BadgeAdapter;
import com.app.pixstory.ui.dashboard.story_detail.StoryDetailPage;
import com.app.pixstory.ui.dashboard.upload.activity.page.UploadPageActivity;
import com.app.pixstory.ui.dashboard.upload.activity.photos.CameraGalleryListActivity;
import com.app.pixstory.ui.dashboard.upload.activity.story.UploadActivity;
import com.app.pixstory.ui.dashboard.you.activity.PageDetailActivity;
import com.app.pixstory.ui.dashboard.you.adapter.YouFilterAdapter;
import com.app.pixstory.ui.dashboard.you.adapter.YouPagesAdapter;
import com.app.pixstory.ui.dashboard.you.adapter.YouPhotosAdapter;
import com.app.pixstory.ui.dashboard.you.adapter.YouStoriesAdapter;
import com.app.pixstory.ui.dashboard.you.model.HomeYouViewModel;
import com.app.pixstory.ui.dashboard.you.model.YouFilterModel;
import com.app.pixstory.ui.dashboard.you.navigator.HomeYouNavigator;
import com.app.pixstory.ui.messages.inbox.InboxActivity;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.ConstantsData;
import com.app.pixstory.utils.PaginationScrollListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

import static com.app.pixstory.base.BaseApplication.getContext;
import static com.app.pixstory.utils.Constants.CREATED;
import static com.app.pixstory.utils.Constants.CREATION_TYPE;
import static com.app.pixstory.utils.Constants.FAV;
import static com.app.pixstory.utils.Constants.FROM_YOU;
import static com.app.pixstory.utils.Constants.PAGE_CREATED;
import static com.app.pixstory.utils.Constants.PAGE_FOLLOWING;
import static com.app.pixstory.utils.Constants.PAGE_MEMBERSHIP;
import static com.app.pixstory.utils.Constants.PAGE_REQUEST_CREATED;
import static com.app.pixstory.utils.Constants.PAGE_TYPE;
import static com.app.pixstory.utils.Constants.PHOTO_CREATED;
import static com.app.pixstory.utils.Constants.PHOTO_FAVOURITES;
import static com.app.pixstory.utils.Constants.PHOTO_FILTER_TYPE;
import static com.app.pixstory.utils.Constants.STORY_CREATED;
import static com.app.pixstory.utils.Constants.STORY_FAVOURITES;
import static com.app.pixstory.utils.Constants.STORY_FILTER_TYPE;
import static com.app.pixstory.utils.Constants.UN_FAV;
import static com.app.pixstory.utils.Constants.USER_ID;
import static com.app.pixstory.utils.Constants.USER_TYPE;
import static com.app.pixstory.utils.Constants.YOU_PAGE_PAGES;
import static com.app.pixstory.utils.Constants.YOU_PAGE_PHOTOS;
import static com.app.pixstory.utils.Constants.YOU_PAGE_STORIES;
import static com.app.pixstory.utils.Constants.YOU_STORY_LOADER;

public class HomeYouActivity extends BaseActivity<ActivityHomeYouBinding, HomeYouViewModel>
        implements HomeYouNavigator, BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityHomeYouBinding binding;
    private HomeYouViewModel viewModel;
    public BottomSheetBehavior createStoryBehaviour, createPageBehaviour, uploadPhotoBehaviour;
    private List<Story> storyList;
    private List<Data> dataList;

    private static final int PAGE_START = 1;

    // STORY
    private int TOTAL_STORY = 5;
    private int TOTAL_STORY_COUNT = 1;
    private int currentStory = PAGE_START;
    private boolean isLoadingStory = false;
    private boolean isLastStory = false;
    // PAGE
    private int TOTAL_PAGE = 5;
    private int TOTAL_PAGE_COUNT = 1;
    private int currentPage = PAGE_START;
    private boolean isLoadingPage = false;
    private boolean isLastPage = false;
    // PHOTO
    private int TOTAL_PHOTO = 15;
    private int TOTAL_PHOTO_COUNT = 1;
    private int currentPhotoUnfav = PAGE_START;
    private boolean isLoadingPhoto = false;
    private boolean isLastPhoto = false;

    private YouPhotosAdapter youPhotosAdapter;
    private YouPagesAdapter youPagesAdapter;
    private YouStoriesAdapter youStoriesAdapter;

    static boolean scroll_down;

    private int followers_int = 0, following_int = 0;


    // UPLOAD IMAGE, CREATE STORY AND CREATE PAGE BOTTOM SHEET BEHAVIOUR
    private void setCreateStoryBehaviour() {
        createStoryBehaviour = BottomSheetBehavior.from(binding.bottomSheetCreateStory.bottomSheetLl);
        // set callback for changes
        createStoryBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.blur.setVisibility(View.VISIBLE);
                binding.blur.setAlpha(slideOffset);
            }
        });

    }

    // BOTTOM SHEET BEHAVIOUR FOR PAGE BRAND OR CORPORATE
    private void setCreatePageBehaviour() {
        createPageBehaviour = BottomSheetBehavior.from(binding.bottomSheetCreatePage.bottomSheetLl);
        createPageBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.blur.setVisibility(View.VISIBLE);
                binding.blur.setAlpha(slideOffset);
            }
        });
    }

    // upload photo
    private void setUploadPhotoBehaviour() {
        uploadPhotoBehaviour = BottomSheetBehavior.from(binding.bottomSheetUploadPhoto.bottomSheetLl);
        // set callback for changes
        uploadPhotoBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
               /* if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    createStoryBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }*/
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.blur.setVisibility(View.VISIBLE);
                binding.blur.setAlpha(slideOffset);
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.activity_home_you;
    }

    @Override
    protected void getBinding(ActivityHomeYouBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(HomeYouViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected Class<HomeYouViewModel> setViewModel() {
        return HomeYouViewModel.class;
    }

    @Override
    protected void init() {
        toolbar(binding.layoutToolbar.toolbar, binding.layoutToolbar.toolbarTitle, R.string.you);
        viewModel.setNavigator(this);
        dataList = new ArrayList<>();
        pageSetUp();
        clickListener();
        initAdapterFilter(YOU_PAGE_STORIES);
        setCreateStoryBehaviour();
        setCreatePageBehaviour();
        setUploadPhotoBehaviour();
        pageType();
        initialization();

    }

    private void initialization() {
        // FETCH DATA FROM HOME PAGE (YOU FOR ME AND YOU FOR OTHER)
        Bundle bundle = getIntent().getBundleExtra(Constants.KEY_DEFAULT_ACTIVITY_BUNDLE);
        if (bundle != null) {
            storyAdapter();
            USER_TYPE = "others";
            USER_ID = bundle.getInt("user_id");
            binding.mainProgress.setVisibility(View.VISIBLE);
            getUserProfileData(USER_ID);
            viewModel.getStoryUser(USER_TYPE, STORY_FILTER_TYPE, USER_ID, currentStory, TOTAL_STORY);
        } else if (FROM_YOU.equals("you")) {
            getUserProfileData(0);
            storyAdapter();
            USER_TYPE = "self";
            USER_ID = 0;
            binding.mainProgress.setVisibility(View.VISIBLE);
            viewModel.getStoryUser(USER_TYPE, STORY_FILTER_TYPE, USER_ID, currentStory, TOTAL_STORY);
        } else if (CREATION_TYPE.equals("page")) {
            getUserProfileData(0);
            getPageAction();
        } else {
            getUserProfileData(0);
            getPhotoAction();
        }

        if (USER_TYPE.equals("others")) {
            binding.layoutYouHeader.followChat.setVisibility(View.VISIBLE);
            binding.layoutYouHeader.followingLl.setVisibility(View.GONE);
        } else if (USER_TYPE.equals(USER_TYPE)) {
            binding.layoutYouHeader.followChat.setVisibility(View.GONE);
            binding.layoutYouHeader.followingLl.setVisibility(View.VISIBLE);
        }

        // SHOW AND HIDE USER PROFILE
        userAction();
    }

    private void getUserProfileData(int userId) {
        // get user detail
        viewModel.getUserProfileData(userId);
        viewModel.getSelfUserLiveData().observe(this, user -> {
            BindingUtils.setStringPhotos(binding.layoutYouHeader.profileImage, user.getProfileImage(), binding.layoutYouHeader.progressBar);
            binding.layoutYouHeader.integrityScore.setText(getResources().getString(R.string.integrity_score) + user.getIntegrityScore());
            binding.layoutYouHeader.location.setText(user.getCountryName());
            binding.layoutYouHeader.bio.setText(user.getBioText());
        });

        //follower
        viewModel.getSelfUserBadgeLiveData().observe(this, data -> {
            followers_int = data.getFollowers();
            following_int = data.getFollowing();
            binding.layoutYouHeader.follower.setText(String.valueOf(followers_int));
            binding.layoutYouHeader.following.setText(String.valueOf(following_int));


            // SHOW BADGE
            BadgeAdapter badgeAdapter = new BadgeAdapter(new ArrayList<>());
            binding.layoutYouHeader.badgeRecyclerView.setAdapter(badgeAdapter);
            binding.layoutYouHeader.badgeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
            badgeAdapter.addItems(data.getBadges());
        });
    }

    private void getPhotoAction() {
        binding.recyclerViewStory.setVisibility(View.GONE);
        binding.recyclerViewPage.setVisibility(View.GONE);
        binding.recyclerViewPhotos.setVisibility(View.VISIBLE);
        USER_TYPE = "self";
        TOTAL_PHOTO_COUNT = 1;
        currentPhotoUnfav = PAGE_START;
        isLoadingPhoto = false;
        isLastPhoto = false;
        photoAdapter();
        // PHOTOS API CALL
        viewModel.yourPhotos(USER_TYPE, UN_FAV, USER_ID, currentPhotoUnfav, TOTAL_PHOTO);
        initAdapterFilter(YOU_PAGE_PHOTOS);
        binding.selectImages.setVisibility(View.VISIBLE);
        binding.layoutYouPageSetup.youStories.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.layoutYouPageSetup.youPhotos.setBackgroundColor(getResources().getColor(R.color.white));
        binding.layoutYouPageSetup.youPages.setBackgroundColor(getResources().getColor(R.color.neon));
    }

    private void getPageAction() {
        binding.recyclerViewStory.setVisibility(View.GONE);
        binding.recyclerViewPage.setVisibility(View.VISIBLE);
        binding.recyclerViewPhotos.setVisibility(View.GONE);
        USER_TYPE = "self";
        TOTAL_PAGE_COUNT = 1;
        currentPage = PAGE_START;
        isLoadingPage = false;
        isLastPage = false;
        pageAdapter();
        // PAGE API CALL
        viewModel.getPageList(USER_TYPE, PAGE_REQUEST_CREATED, USER_ID, currentPage, TOTAL_PAGE);
        initAdapterFilter(YOU_PAGE_PAGES);
        binding.layoutYouPageSetup.youStories.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.layoutYouPageSetup.youPhotos.setBackgroundColor(getResources().getColor(R.color.neon));
        binding.layoutYouPageSetup.youPages.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void initAdapterFilter(String filter) {
        YouFilterAdapter youFilterAdapter = new YouFilterAdapter(new ArrayList<>());
        youFilterAdapter.setListener(this::onFilterAdapterItem);
        binding.youFilter.setAdapter(youFilterAdapter);
        if (filter.equals(YOU_PAGE_STORIES)) {
            youFilterAdapter.addItems(ConstantsData.youFilterStoriesList());
        } else if (filter.equals(YOU_PAGE_PHOTOS)) {
            youFilterAdapter.addItems(ConstantsData.youFilterPhotoList());
        } else if (filter.equals(YOU_PAGE_PAGES)) {
            youFilterAdapter.addItems(ConstantsData.youFilterPagesList());
        }
    }

    // ACTION ON FILTER APPLIED ON STORIES, PAGES AND PHOTOS
    private void onFilterAdapterItem(Object data, String tag, int position) {
        if (data instanceof YouFilterModel) {
            // FOR HEADER VISIBILITY
            if (((YouFilterModel) data).getSetFilterAction().equals(STORY_FAVOURITES)) {
                binding.layoutYouHeader.appBarLayout.setVisibility(View.GONE);
            } else if (((YouFilterModel) data).getSetFilterAction().equals(STORY_CREATED)) {
                binding.layoutYouHeader.appBarLayout.setVisibility(View.VISIBLE);
            }

            // FOR FILTER ACTION
            // STORY CREATED
            if (((YouFilterModel) data).getSetFilterAction().equals(STORY_CREATED)) {
                binding.mainProgress.setVisibility(View.VISIBLE);
                STORY_FILTER_TYPE = ((YouFilterModel) data).getSetDataName();
                TOTAL_STORY_COUNT = 1;
                currentStory = PAGE_START;
                isLoadingStory = false;
                isLastStory = false;
                storyAdapter();
                viewModel.getStoryUser(USER_TYPE, STORY_FILTER_TYPE, USER_ID, currentStory, TOTAL_STORY);
                // STORY FAVOURITE
            } else if (((YouFilterModel) data).getSetFilterAction().equals(STORY_FAVOURITES)) {
                binding.mainProgress.setVisibility(View.VISIBLE);
                STORY_FILTER_TYPE = ((YouFilterModel) data).getSetDataName();
                TOTAL_STORY_COUNT = 1;
                currentStory = PAGE_START;
                isLoadingStory = false;
                isLastStory = false;
                storyAdapter();
                viewModel.getStoryUser(USER_TYPE, STORY_FILTER_TYPE, USER_ID, currentStory, TOTAL_STORY);
                // PAGE CREATED
            } else if (((YouFilterModel) data).getSetFilterAction().equals(PAGE_CREATED)) {
                binding.mainProgress.setVisibility(View.VISIBLE);
                TOTAL_PAGE_COUNT = 1;
                currentPage = PAGE_START;
                isLoadingPage = false;
                isLastPage = false;
                pageAdapter();
                viewModel.getPageList(USER_TYPE, ((YouFilterModel) data).getSetDataName(), USER_ID, currentPage, TOTAL_PAGE);
                // PAGE FOLLOWING
            } else if (((YouFilterModel) data).getSetFilterAction().equals(PAGE_FOLLOWING)) {
                binding.mainProgress.setVisibility(View.VISIBLE);
                TOTAL_PAGE_COUNT = 1;
                currentPage = PAGE_START;
                isLoadingPage = false;
                isLastPage = false;
                pageAdapter();
                viewModel.getPageList(USER_TYPE, ((YouFilterModel) data).getSetDataName(), USER_ID, currentPage, TOTAL_PAGE);
                // PAGE MEMBERSHIP
            } else if (((YouFilterModel) data).getSetFilterAction().equals(PAGE_MEMBERSHIP)) {
                binding.mainProgress.setVisibility(View.VISIBLE);
                TOTAL_PAGE_COUNT = 1;
                currentPage = PAGE_START;
                isLoadingPage = false;
                isLastPage = false;
                pageAdapter();
                viewModel.getPageList(USER_TYPE, ((YouFilterModel) data).getSetDataName(), USER_ID, currentPage, TOTAL_PAGE);
                // PHOTO CREATED
            } else if (((YouFilterModel) data).getSetFilterAction().equals(PHOTO_CREATED)) {
                binding.mainProgress.setVisibility(View.VISIBLE);
                dataList = new ArrayList<>();
                PHOTO_FILTER_TYPE = ((YouFilterModel) data).getSetDataName();
                TOTAL_PHOTO_COUNT = 1;
                currentPhotoUnfav = PAGE_START;
                isLoadingPhoto = false;
                isLastPhoto = false;
                photoAdapter();
                viewModel.yourPhotos(USER_TYPE, PHOTO_FILTER_TYPE, USER_ID, currentPhotoUnfav, TOTAL_PHOTO);
                // PHOTO FAVOURITE
            } else if (((YouFilterModel) data).getSetFilterAction().equals(PHOTO_FAVOURITES)) {
                binding.mainProgress.setVisibility(View.VISIBLE);
                dataList = new ArrayList<>();
                PHOTO_FILTER_TYPE = ((YouFilterModel) data).getSetDataName();
                TOTAL_PHOTO_COUNT = 1;
                currentPhotoUnfav = PAGE_START;
                isLoadingPhoto = false;
                isLastPhoto = false;
                photoAdapter();
                viewModel.yourPhotos(USER_TYPE, PHOTO_FILTER_TYPE, USER_ID, currentPhotoUnfav, TOTAL_PHOTO);
            }
        }
    }

    // ACTION ON  STORIES, PAGES AND PHOTOS TAB BUTTON
    private void pageSetUp() {
        STORY_FILTER_TYPE = "unfav";
        PAGE_REQUEST_CREATED = "created";
        PHOTO_FILTER_TYPE = "unfav";
        // bottom navigation
        binding.layoutBottomNavigation.customBottomBar.inflateMenu(R.menu.bottom_nav_menu);
        binding.layoutBottomNavigation.customBottomBar.setSelectedItemId(R.id.navigation_upload);
        //getting bottom navigation view and attaching the listener
        binding.layoutBottomNavigation.customBottomBar.setOnNavigationItemSelectedListener(this);

        binding.layoutYouPageSetup.radioGroupYou.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.you_stories) {
                binding.recyclerViewStory.setVisibility(View.VISIBLE);
                binding.recyclerViewPage.setVisibility(View.GONE);
                binding.recyclerViewPhotos.setVisibility(View.GONE);
                STORY_FILTER_TYPE = "unfav";
                TOTAL_STORY_COUNT = 1;
                currentStory = PAGE_START;
                isLoadingStory = false;
                isLastStory = false;
                storyAdapter();
                // STORY API CALL
                viewModel.getStoryUser(USER_TYPE, STORY_FILTER_TYPE, USER_ID, currentStory, TOTAL_STORY);
                initAdapterFilter(YOU_PAGE_STORIES);
                binding.selectImages.setVisibility(View.GONE);
                binding.layoutYouPageSetup.youStories.setBackgroundColor(getResources().getColor(R.color.white));
                binding.layoutYouPageSetup.youPhotos.setBackgroundColor(getResources().getColor(R.color.neon));
                binding.layoutYouPageSetup.youPages.setBackgroundColor(getResources().getColor(R.color.neon));
            } else if (checkedId == R.id.you_pages) {
                binding.recyclerViewStory.setVisibility(View.GONE);
                binding.recyclerViewPage.setVisibility(View.VISIBLE);
                binding.recyclerViewPhotos.setVisibility(View.GONE);
                TOTAL_PAGE_COUNT = 1;
                currentPage = PAGE_START;
                isLoadingPage = false;
                isLastPage = false;
                pageAdapter();
                // PAGES API CALL
                viewModel.getPageList(USER_TYPE, PAGE_REQUEST_CREATED, USER_ID, currentPage, TOTAL_PAGE);
                initAdapterFilter(YOU_PAGE_PAGES);
                binding.selectImages.setVisibility(View.GONE);
                binding.layoutYouPageSetup.youStories.setBackgroundColor(getResources().getColor(R.color.neon));
                binding.layoutYouPageSetup.youPhotos.setBackgroundColor(getResources().getColor(R.color.neon));
                binding.layoutYouPageSetup.youPages.setBackgroundColor(getResources().getColor(R.color.white));
            } else if (checkedId == R.id.you_photos) {
                binding.recyclerViewStory.setVisibility(View.GONE);
                binding.recyclerViewPage.setVisibility(View.GONE);
                binding.recyclerViewPhotos.setVisibility(View.VISIBLE);
                TOTAL_PHOTO_COUNT = 1;
                currentPhotoUnfav = PAGE_START;
                isLoadingPhoto = false;
                isLastPhoto = false;
                photoAdapter();
                // PHOTOS API CALL
                viewModel.yourPhotos(USER_TYPE, UN_FAV, USER_ID, currentPhotoUnfav, TOTAL_PHOTO);
                initAdapterFilter(YOU_PAGE_PHOTOS);
                binding.selectImages.setVisibility(View.VISIBLE);
                binding.layoutYouPageSetup.youStories.setBackgroundColor(getResources().getColor(R.color.neon));
                binding.layoutYouPageSetup.youPhotos.setBackgroundColor(getResources().getColor(R.color.white));
                binding.layoutYouPageSetup.youPages.setBackgroundColor(getResources().getColor(R.color.neon));
            }
        });
    }

    private void clickListener() {
        binding.layoutYouHeader.followerLl.setOnClickListener(this::onClick);
        binding.layoutYouHeader.followingLl.setOnClickListener(this::onClick);
        binding.selectImages.setOnClickListener(this::onClick);
        binding.bottomSheetCreateStory.photos.setOnClickListener(this::onClick);
        binding.bottomSheetCreateStory.story.setOnClickListener(this::onClick);
        binding.bottomSheetCreateStory.page.setOnClickListener(this::onClick);
        binding.bottomSheetCreatePage.createPage.setOnClickListener(this::onClick);
        binding.bottomSheetUploadPhoto.camera.setOnClickListener(this::onClick);
        binding.bottomSheetUploadPhoto.phoneGallery.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        if (view.getId() == R.id.follower_ll) {
            if (followers_int == 0) {
                showToast(R.string.dont_have_followers);
            } else {
                startActivity(YouUserNameActivity.class, 2);
            }
        } else if (view.getId() == R.id.following_ll) {
            if (following_int == 0) {
                showToast(R.string.dont_have_following);
            } else {
                startActivity(YouUserNameActivity.class, 1);
            }
        } else if (view.getId() == R.id.select_images) {
            selectImages();
        } else if (view.getId() == R.id.photos) {
            binding.bottomSheetCreateStory.photos.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
            binding.bottomSheetCreateStory.story.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.bottomSheetCreateStory.page.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            createStoryBehaviour.setHideable(true);
            createStoryBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            uploadPhotoBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getId() == R.id.story) {
            binding.bottomSheetCreateStory.photos.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.bottomSheetCreateStory.story.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
            binding.bottomSheetCreateStory.page.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            createStoryBehaviour.setHideable(true);
            createStoryBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            startActivity(UploadActivity.class);
        } else if (view.getId() == R.id.page) {
            binding.bottomSheetCreateStory.photos.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.bottomSheetCreateStory.story.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.bottomSheetCreateStory.page.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
            createStoryBehaviour.setHideable(true);
            createStoryBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            createPageBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getId() == R.id.camera) {
            binding.bottomSheetUploadPhoto.camera.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
            binding.bottomSheetUploadPhoto.phoneGallery.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            uploadPhotoBehaviour.setHideable(true);
            uploadPhotoBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            Intent cameraIntent = new Intent(this, CameraGalleryListActivity.class);
            cameraIntent.putExtra("image", "camera");
            startActivity(cameraIntent);
        } else if (view.getId() == R.id.phone_gallery) {
            binding.bottomSheetUploadPhoto.camera.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.bottomSheetUploadPhoto.phoneGallery.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
            uploadPhotoBehaviour.setHideable(true);
            uploadPhotoBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            Intent galleryIntent = new Intent(this, CameraGalleryListActivity.class);
            galleryIntent.putExtra("image", "gallery");
            startActivity(galleryIntent);
        } else if (view.getId() == R.id.createPage) {
            createPageBehaviour.setHideable(true);
            createPageBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            startActivity(UploadPageActivity.class);
        }
    }

    private void selectImages() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("key", (ArrayList<? extends Parcelable>) dataList);
        startUploadActivity(SelectImages.class, bundle);
    }

    private void pageType() {
        binding.bottomSheetCreatePage.radio.setOnClickListener(v -> {
            if (!binding.bottomSheetCreatePage.radio.isSelected()) {
                binding.bottomSheetCreatePage.radio.setChecked(true);
                binding.bottomSheetCreatePage.radio.setSelected(true);
                PAGE_TYPE = 1;
            } else {
                binding.bottomSheetCreatePage.radio.setChecked(false);
                binding.bottomSheetCreatePage.radio.setSelected(false);
                PAGE_TYPE = 0;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(DashboardActivity.class);
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void message(int message) {

    }

    @Override
    public void message(String message) {
        //   showToast(message);
        binding.mainProgress.setVisibility(View.GONE);
        binding.recyclerViewStory.setVisibility(View.GONE);
        binding.recyclerViewPage.setVisibility(View.GONE);
        binding.recyclerViewPhotos.setVisibility(View.GONE);
        binding.noRecordFound.setVisibility(View.VISIBLE);

    }

    @Override
    public void onStoryUserResponse(Boolean success, StoryUserData data, List<Story> story) {
        if (success) {

            binding.mainProgress.setVisibility(View.GONE);
            binding.recyclerViewStory.setVisibility(View.VISIBLE);
            binding.recyclerViewPage.setVisibility(View.GONE);
            binding.recyclerViewPhotos.setVisibility(View.GONE);
            binding.noRecordFound.setVisibility(View.GONE);
            if (story.size() > 0) {
                if (currentStory == 1) {
                    storyList = new ArrayList<>();
                    storyList = story;
                    youStoriesAdapter.addItems(storyList);
                    if (currentStory <= TOTAL_STORY_COUNT) youStoriesAdapter.addLoadingFooter();
                    else isLastStory = true;
                } else {
                    storyList = new ArrayList<>();
                    storyList = story;
                    youStoriesAdapter.removeLoadingFooter();
                    isLoadingStory = false;
                    youStoriesAdapter.addItems(storyList);
                    TOTAL_STORY_COUNT = currentStory + 1;
                    if (currentStory != TOTAL_STORY_COUNT) youStoriesAdapter.addLoadingFooter();
                    else isLastStory = true;
                }
            }


        } else {
            binding.mainProgress.setVisibility(View.GONE);
            binding.recyclerViewStory.setVisibility(View.GONE);
            binding.recyclerViewPage.setVisibility(View.GONE);
            binding.recyclerViewPhotos.setVisibility(View.GONE);
            binding.noRecordFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageResponse(Boolean success, List<PageListData> data) {
        if (success) {
            binding.mainProgress.setVisibility(View.GONE);
            binding.recyclerViewPage.setVisibility(View.VISIBLE);
            binding.recyclerViewStory.setVisibility(View.GONE);
            binding.recyclerViewPhotos.setVisibility(View.GONE);
            binding.noRecordFound.setVisibility(View.GONE);
            if (currentPage == 1) {
                youPagesAdapter.addItems(data);
                if (currentPage <= TOTAL_PAGE_COUNT) youPagesAdapter.addLoadingFooter();
                else isLastPage = true;
            } else {
                youPagesAdapter.removeLoadingFooter();
                isLoadingPage = false;
                youPagesAdapter.addItems(data);
                TOTAL_PAGE_COUNT = currentPage + 1;
                if (currentPage != TOTAL_PAGE_COUNT) youPagesAdapter.addLoadingFooter();
                else isLastPage = true;
            }
        } else {
            binding.mainProgress.setVisibility(View.GONE);
            binding.recyclerViewPage.setVisibility(View.GONE);
            binding.recyclerViewStory.setVisibility(View.GONE);
            binding.recyclerViewPhotos.setVisibility(View.GONE);
            binding.noRecordFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMyPhotoResponse(boolean b, List<Data> data, String type) {
        if (b) {
            binding.mainProgress.setVisibility(View.GONE);
            //  dataList = new ArrayList<>();
            dataList.addAll(data);
            if (type.equals(FAV)) {
                if (currentPhotoUnfav == 1) {
                    youPhotosAdapter.addItems(data);
                    if (currentPhotoUnfav <= TOTAL_PHOTO_COUNT) youPhotosAdapter.addLoadingFooter();
                    else isLastPhoto = true;
                } else {
                    youPhotosAdapter.removeLoadingFooter();
                    isLoadingPhoto = false;
                    youPhotosAdapter.addItems(data);
                    TOTAL_PHOTO_COUNT = currentPhotoUnfav + 1;
                    if (currentPhotoUnfav != TOTAL_PHOTO_COUNT) youPhotosAdapter.addLoadingFooter();
                    else isLastPhoto = true;
                }
                //  youPhotosAdapter.addItems(data);
            } else {

                if (currentPhotoUnfav == 1) {
                    youPhotosAdapter.addItems(data);
                    if (currentPhotoUnfav <= TOTAL_PHOTO_COUNT) youPhotosAdapter.addLoadingFooter();
                    else isLastPhoto = true;
                } else {
                    youPhotosAdapter.removeLoadingFooter();
                    isLoadingPhoto = false;
                    youPhotosAdapter.addItems(data);
                    TOTAL_PHOTO_COUNT = currentPhotoUnfav + 1;
                    if (currentPhotoUnfav != TOTAL_PHOTO_COUNT) youPhotosAdapter.addLoadingFooter();
                    else isLastPhoto = true;
                }
            }

        } else {
            binding.mainProgress.setVisibility(View.GONE);
        }
    }


    private void storyAdapter() {
        youStoriesAdapter = new YouStoriesAdapter(new ArrayList<>());
        binding.recyclerViewStory.setHasFixedSize(true);
        binding.recyclerViewStory.setItemAnimator(new DefaultItemAnimator());
        youStoriesAdapter.setListener(this::onStoryAdapterItem);
        binding.recyclerViewStory.setAdapter(youStoriesAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerViewStory.setLayoutManager(linearLayoutManager);
        binding.recyclerViewStory.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoadingStory = true;
                currentStory += 1;
                binding.mainProgress.setVisibility(View.VISIBLE);
                loadNextPageStory();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_STORY_COUNT;
            }

            @Override
            public boolean isLastPage() {
                return isLastStory;
            }

            @Override
            public boolean isLoading() {
                return isLoadingStory;
            }
        });
    }

    private void loadNextPageStory() {
        viewModel.getStoryUser(USER_TYPE, STORY_FILTER_TYPE, USER_ID, currentStory, TOTAL_STORY);
    }

    private void onStoryAdapterItem(Object data, String tah, int position) {
        if (data instanceof Story) {
            Bundle bundle = new Bundle();
            bundle.putInt("story_id", ((Story) data).getId());
            startUploadActivity(StoryDetailPage.class, bundle);
        }
    }

    //PAGE ADAPTER
    private void pageAdapter() {
        youPagesAdapter = new YouPagesAdapter(new ArrayList<>());
        binding.recyclerViewPage.setAdapter(youPagesAdapter);
        youPagesAdapter.setListener(this::onPageAdapterItem);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.recyclerViewPage.setLayoutManager(gridLayoutManager);
        binding.recyclerViewPage.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoadingPage = true;
                currentPage += 1;
                binding.mainProgress.setVisibility(View.VISIBLE);
                loadNextPages();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGE_COUNT;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoadingPage;
            }
        });
    }

    // PAGE ITEM CLICK
    private void onPageAdapterItem(Object data, String s, int i) {
        if (data instanceof PageListData) {
            if (((PageListData) data).getId() != null && ((PageListData) data).getCoverImgPath() != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("page_id", ((PageListData) data).getId());
                bundle.putString("image", ((PageListData) data).getCoverImgPath());
                startUploadActivity(PageDetailActivity.class, bundle);
            }
        }
    }

    private void loadNextPages() {
        viewModel.getPageList(USER_TYPE, PAGE_REQUEST_CREATED, USER_ID, currentPage, TOTAL_PAGE);
    }

    private void photoAdapter() {
        youPhotosAdapter = new YouPhotosAdapter(new ArrayList<>());
        binding.recyclerViewPhotos.setHasFixedSize(true);
        binding.recyclerViewPhotos.setAdapter(youPhotosAdapter);
        youPhotosAdapter.setListener(this::onPhotoAdapterItem);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        binding.recyclerViewPhotos.setLayoutManager(gridLayoutManager);
        binding.recyclerViewPhotos.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoadingPhoto = true;
                currentPhotoUnfav += 1;
                binding.mainProgress.setVisibility(View.VISIBLE);
                loadNextPhotoUnFav();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PHOTO_COUNT;
            }

            @Override
            public boolean isLastPage() {
                return isLastPhoto;
            }

            @Override
            public boolean isLoading() {
                return isLoadingPhoto;
            }
        });

    }

    private void loadNextPhotoUnFav() {
        viewModel.yourPhotos(USER_TYPE, PHOTO_FILTER_TYPE, USER_ID, currentPhotoUnfav, TOTAL_PHOTO);
    }

    // SEE THE IMAGES ON FULL VIEW
    private void onPhotoAdapterItem(Object data, String tag, int position) {
        if (data instanceof Data) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("key", (ArrayList<? extends Parcelable>) dataList);
            bundle.putInt("position", position);
            startUploadActivity(YouPhotoFullView.class, bundle);
        }
    }


    @Override
    protected void onRestart() {
        if (SelectImages.is_deleted) {
            dataList = new ArrayList<>();
            SelectImages.is_deleted = false;
            viewModel.yourPhotos(USER_TYPE, PHOTO_FILTER_TYPE, USER_ID, currentPhotoUnfav, TOTAL_PHOTO);
        }
        super.onRestart();
    }

    /**
     * Called when an item in the bottom navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item and false if the item should not be
     * selected. Consider setting non-selectable items as disabled preemptively to make them
     * appear non-interactive.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                uploadPhotoBehaviour.setHideable(true);
                uploadPhotoBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                createStoryBehaviour.setHideable(true);
                createStoryBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                startActivity(DashboardActivity.class);
                finishAffinity();
                return true;
            case R.id.navigation_upload:
                createStoryBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                return true;
            case R.id.navigation_chat:
                uploadPhotoBehaviour.setHideable(true);
                uploadPhotoBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                createStoryBehaviour.setHideable(true);
                createStoryBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                finish();
                startActivity(InboxActivity.class);
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(DashboardActivity.class);
        finishAffinity();
    }

    private void userAction() {
        binding.recyclerViewStory.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (scroll_down) {
                    binding.layoutYouHeader.appBarLayout.setVisibility(View.GONE);
                } else {
                    binding.layoutYouHeader.appBarLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 70) {
                    //scroll down
                    scroll_down = true;

                } else if (dy < -5) {
                    //scroll up
                    scroll_down = false;
                }
            }
        });


        binding.recyclerViewPage.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (scroll_down) {
                    binding.layoutYouHeader.appBarLayout.setVisibility(View.GONE);
                } else {
                    binding.layoutYouHeader.appBarLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 70) {
                    //scroll down
                    scroll_down = true;

                } else if (dy < -5) {
                    //scroll up
                    scroll_down = false;
                }
            }
        });

        binding.recyclerViewPhotos.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (scroll_down) {
                    binding.layoutYouHeader.appBarLayout.setVisibility(View.GONE);
                } else {
                    binding.layoutYouHeader.appBarLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 70) {
                    //scroll down
                    scroll_down = true;

                } else if (dy < -5) {
                    //scroll up
                    scroll_down = false;
                }
            }
        });
    }
}
