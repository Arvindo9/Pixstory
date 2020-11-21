package com.app.pixstory.ui.dashboard;

import android.content.Intent;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.core.dialog.logout.Logout;
import com.app.pixstory.core.dialog.universal_search.UniversalSearch;
import com.app.pixstory.data.model.access.Data;
import com.app.pixstory.data.model.api.AccountDetailResponse;
import com.app.pixstory.databinding.ActivityDashboardBinding;
import com.app.pixstory.ui.dashboard.home.HomeFragment;
import com.app.pixstory.ui.dashboard.upload.activity.page.UploadPageActivity;
import com.app.pixstory.ui.dashboard.upload.activity.photos.CameraGalleryListActivity;
import com.app.pixstory.ui.dashboard.upload.activity.story.UploadActivity;
import com.app.pixstory.ui.login_dashboard.login_signup_dashboard.LoginSignUpUserDashboard;
import com.app.pixstory.ui.messages.inbox.InboxActivity;
import com.app.pixstory.ui.navigation_view.account.AccountActivity;
import com.app.pixstory.ui.navigation_view.faqs.FAQsFragment;
import com.app.pixstory.ui.navigation_view.profile.ProfileActivity;
import com.app.pixstory.ui.navigation_view.terms_and_conditions.TermsAndConditionsFragment;
import com.app.pixstory.utils.GlobalMethods;
import com.app.pixstory.utils.Utils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import static com.app.pixstory.utils.Constants.HOME_FILTER;
import static com.app.pixstory.utils.Constants.HOME_LOADER;
import static com.app.pixstory.utils.Constants.PAGE_TYPE;

public class DashboardActivity extends BaseActivity<ActivityDashboardBinding, DashboardModel>
        implements View.OnClickListener, DashboardNavigator, BottomNavigationView.OnNavigationItemSelectedListener {

    ActivityDashboardBinding binding;
    public static int REQUEST_MICROPHONE = 0;
    private DashboardModel viewModel;
    public BottomSheetBehavior createStoryBehaviour, createPageBehaviour, uploadPhotoBehaviour;
    GoogleApiClient googleApiClient;
    boolean doubleBackToExitPressedOnce = false;



    @Override
    public int getLayout() {
        return R.layout.activity_dashboard;
    }

    @Override
    protected void getBinding(ActivityDashboardBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(DashboardModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected Class<DashboardModel> setViewModel() {
        return DashboardModel.class;
    }

    @Override
    protected void init() {
        viewModel.setNavigator(this);
        navigationDrawer();
        clickListener();
        setCreateStoryBehaviour();
        setCreatePageBehaviour();
        setUploadPhotoBehaviour();
        pageType();

        viewModel.getLoggedOut().observe(this, aBoolean -> {

            if (aBoolean) {
                GlobalMethods.intentMethodWithoutBackstack(getBaseContext(), LoginSignUpUserDashboard.class);
            } else {
                showToast("Logout failed!!!");
            }

        });

        viewModel.getLoading().observe(this, (Observer) o -> {
            if ((Boolean) o) {
                showSimmerLoader(HOME_LOADER);
            } else {
                hideSimmerLoader();
            }
        });

        refreshToken();
        viewModel.getUserLiveData().observe(this, new Observer<AccountDetailResponse.User>() {
            @Override
            public void onChanged(AccountDetailResponse.User user) {
                binding.sideNav.name.setText(user.getName() + " " + user.getLname());
                Utils.setImageFromPath(getBaseContext(), binding.sideNav.profileImage, user.getProfileImage(), binding.sideNav.progressBar);
            }
        });

        binding.appBarMain.layoutBottomNavigation.customBottomBar.inflateMenu(R.menu.bottom_nav_menu);
        binding.appBarMain.layoutBottomNavigation.customBottomBar.setSelectedItemId(R.id.navigation_upload);
        //getting bottom navigation view and attaching the listener
        binding.appBarMain.layoutBottomNavigation.customBottomBar.setOnNavigationItemSelectedListener(DashboardActivity.this);
    }

    private void refreshToken() {
     //   viewModel.refreshToken();
        viewModel.getProfileData();
        loadFragment(new HomeFragment());

    }

    private void setCreateStoryBehaviour() {
        createStoryBehaviour = BottomSheetBehavior.from(binding.appBarMain.bottomSheetCreateStory.bottomSheetLl);
        // set callback for changes
        createStoryBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
               /* if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    createStoryBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }*/
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.appBarMain.blur.setVisibility(View.VISIBLE);
                binding.appBarMain.blur.setAlpha(slideOffset);
            }
        });

    }

    // create page
    private void setCreatePageBehaviour() {
        createPageBehaviour = BottomSheetBehavior.from(binding.appBarMain.bottomSheetCreatePage.bottomSheetLl);
        // set callback for changes
        createPageBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
               /* if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    createStoryBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }*/
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.appBarMain.blur.setVisibility(View.VISIBLE);
                binding.appBarMain.blur.setAlpha(slideOffset);
            }
        });
    }

    // upload photo
    private void setUploadPhotoBehaviour() {
        uploadPhotoBehaviour = BottomSheetBehavior.from(binding.appBarMain.bottomSheetUploadPhoto.bottomSheetLl);
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
                binding.appBarMain.blur.setVisibility(View.VISIBLE);
                binding.appBarMain.blur.setAlpha(slideOffset);
            }
        });
    }

    private void clickListener() {
        binding.sideNav.navProfile.setOnClickListener(this);
        binding.sideNav.navAccount.setOnClickListener(this);
        binding.sideNav.navShareApp.setOnClickListener(this);
        binding.sideNav.navSubscribe.setOnClickListener(this);
        binding.sideNav.navFaqs.setOnClickListener(this);
        binding.sideNav.navTermsAndConditions.setOnClickListener(this);
        binding.sideNav.navLogout.setOnClickListener(this);
        binding.appBarMain.universalSearch.setOnClickListener(this);
        binding.appBarMain.bottomSheetCreateStory.photos.setOnClickListener(this);
        binding.appBarMain.bottomSheetCreateStory.story.setOnClickListener(this);
        binding.appBarMain.bottomSheetCreateStory.page.setOnClickListener(this);
        binding.appBarMain.bottomSheetCreatePage.createPage.setOnClickListener(this);
        binding.appBarMain.bottomSheetUploadPhoto.camera.setOnClickListener(this);
        binding.appBarMain.bottomSheetUploadPhoto.phoneGallery.setOnClickListener(this);
    }

    private void navigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.appBarMain.toolbarMain, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        manager.popBackStack();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        Fragment fragment;
        switch (v.getId()) {
            case R.id.nav_profile:
                GlobalMethods.intentMethod(this, ProfileActivity.class);
                break;
            case R.id.nav_account:
                GlobalMethods.intentMethod(this, AccountActivity.class);
                break;
            case R.id.nav_share_app:
                break;
            case R.id.nav_subscribe:
                break;
            case R.id.nav_faqs:
                fragment = new FAQsFragment();
                loadFragment(fragment);
                break;
            case R.id.nav_terms_and_conditions:
                fragment = new TermsAndConditionsFragment();
                loadFragment(fragment);
                break;
            case R.id.nav_logout:
                Logout.showDialog(this, googleApiClient, viewModel);
                break;
            case R.id.universal_search:
                UniversalSearch.universalSearch(this);
                break;
            case R.id.photos:
                binding.appBarMain.bottomSheetCreateStory.photos.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
                binding.appBarMain.bottomSheetCreateStory.story.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
                binding.appBarMain.bottomSheetCreateStory.page.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
                createStoryBehaviour.setHideable(true);
                createStoryBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                uploadPhotoBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.story:
                binding.appBarMain.bottomSheetCreateStory.photos.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
                binding.appBarMain.bottomSheetCreateStory.story.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
                binding.appBarMain.bottomSheetCreateStory.page.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
                createStoryBehaviour.setHideable(true);
                createStoryBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                startActivity(UploadActivity.class);
                break;
            case R.id.page:
                binding.appBarMain.bottomSheetCreateStory.photos.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
                binding.appBarMain.bottomSheetCreateStory.story.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
                binding.appBarMain.bottomSheetCreateStory.page.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
                createStoryBehaviour.setHideable(true);
                createStoryBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                createPageBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.createPage:
                createPageBehaviour.setHideable(true);
                createPageBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                startActivity(UploadPageActivity.class);
                break;
            case R.id.camera:
                binding.appBarMain.bottomSheetUploadPhoto.camera.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
                binding.appBarMain.bottomSheetUploadPhoto.phoneGallery.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
                uploadPhotoBehaviour.setHideable(true);
                uploadPhotoBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                Intent cameraIntent = new Intent(this, CameraGalleryListActivity.class);
                cameraIntent.putExtra("image", "camera");
                startActivity(cameraIntent);
                break;
            case R.id.phone_gallery:
                binding.appBarMain.bottomSheetUploadPhoto.camera.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
                binding.appBarMain.bottomSheetUploadPhoto.phoneGallery.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
                uploadPhotoBehaviour.setHideable(true);
                uploadPhotoBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                Intent galleryIntent = new Intent(this, CameraGalleryListActivity.class);
                galleryIntent.putExtra("image", "gallery");
                startActivity(galleryIntent);
                break;
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void pageType() {
        binding.appBarMain.bottomSheetCreatePage.radio.setOnClickListener(v -> {
            if (!binding.appBarMain.bottomSheetCreatePage.radio.isSelected()) {
                binding.appBarMain.bottomSheetCreatePage.radio.setChecked(true);
                binding.appBarMain.bottomSheetCreatePage.radio.setSelected(true);
                PAGE_TYPE = 1;
            } else {
                binding.appBarMain.bottomSheetCreatePage.radio.setChecked(false);
                binding.appBarMain.bottomSheetCreatePage.radio.setSelected(false);
                PAGE_TYPE = 0;
            }
        });
    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onTokenResponse(Boolean success, Data data) {
        if (success) {
            viewModel.setToken(data.getToken());
            viewModel.getProfileData();
            loadFragment(new HomeFragment());
        }
    }

    @Override
    public void message(int message) {
        showToast(message);
    }

    @Override
    public void message(String message) {
        showToast(message);
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
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                HOME_FILTER = "";
                createStoryBehaviour.setHideable(true);
                createStoryBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                createPageBehaviour.setHideable(true);
                createPageBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                uploadPhotoBehaviour.setHideable(true);
                uploadPhotoBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                fragment = new HomeFragment();
                loadFragment(fragment);
                return true;
            case R.id.navigation_upload:
                createStoryBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                return true;
            case R.id.navigation_chat:
                createStoryBehaviour.setHideable(true);
                createStoryBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                createPageBehaviour.setHideable(true);
                createPageBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                uploadPhotoBehaviour.setHideable(true);
                uploadPhotoBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                startActivity(InboxActivity.class);
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }
}
