package com.app.pixstory.ui.dashboard.others;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.core.dialog.universal_search.UniversalSearch;
import com.app.pixstory.databinding.ActivityOthersBinding;
import com.app.pixstory.ui.dashboard.DashboardActivity;
import com.app.pixstory.ui.dashboard.others.pager.HomeOtherPager;
import com.app.pixstory.ui.dashboard.upload.activity.story.UploadActivity;

import com.app.pixstory.ui.messages.inbox.InboxActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;


public class HomeOthersActivity extends BaseActivity<ActivityOthersBinding, HomeOthersViewModel>
        implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    ActivityOthersBinding binding;
    HomeOthersViewModel viewModel;
    public BottomSheetBehavior createStoryBehaviour;
    private void setCreateStoryBehaviour() {
        createStoryBehaviour = BottomSheetBehavior.from(binding.bottomSheetCreateStory.bottomSheetLl);
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
                binding.blur.setVisibility(View.VISIBLE);
                binding.blur.setAlpha(slideOffset);
            }
        });

    }

    public void tabLayout() {
        // Adding the tabs using addTab() method.
        binding.tabLayout.tabLayout.addTab(binding.tabLayout.tabLayout.newTab().setText(getResources().getString(R.string.by_interests)));
        binding.tabLayout.tabLayout.addTab(binding.tabLayout.tabLayout.newTab().setText(getResources().getString(R.string.nearby)));
        // Create out pager adapter
        PagerAdapter pagerAdapter = new HomeOtherPager(getSupportFragmentManager(), binding.tabLayout.tabLayout.getTabCount());
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setOffscreenPageLimit(0);
        binding.viewPager.disableScroll(true);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout.tabLayout));
        binding.tabLayout.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
                //Location.enableLocation(HomeOthersActivity.this);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void clickListener() {
        binding.layoutToolbar.search.setOnClickListener(this);
        binding.bottomSheetCreateStory.story.setOnClickListener(this);
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
    protected void init() {
        bottomNavigation();
        toolbar(binding.layoutToolbar.toolbar, binding.layoutToolbar.toolbarTitle, R.string.other);
        clickListener();
        tabLayout();
        setCreateStoryBehaviour();
    }

    private void bottomNavigation() {
        // bottom navigation
        binding.layoutBottomNavigation.customBottomBar.inflateMenu(R.menu.bottom_nav_menu);
        binding.layoutBottomNavigation.customBottomBar.setSelectedItemId(R.id.navigation_upload);
        //getting bottom navigation view and attaching the listener
        binding.layoutBottomNavigation.customBottomBar.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected Class<HomeOthersViewModel> setViewModel() {
        return HomeOthersViewModel.class;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_others;
    }

    @Override
    protected void getBinding(ActivityOthersBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(HomeOthersViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.search) {
            UniversalSearch.universalSearch(this);
        } else if (view.getId() == R.id.story) {
            createStoryBehaviour.setHideable(true);
            createStoryBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            startActivity(UploadActivity.class);
        }
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
                createStoryBehaviour.setHideable(true);
                createStoryBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                startActivity(DashboardActivity.class);
                finishAffinity();
                return true;
            case R.id.navigation_upload:
                createStoryBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                /*finish();
              startActivity(UploadActivity.class);*/
                return true;
            case R.id.navigation_chat:
                createStoryBehaviour.setHideable(true);
                createStoryBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                finish();
                startActivity(InboxActivity.class);
                return true;
        }
        return false;
    }
}
