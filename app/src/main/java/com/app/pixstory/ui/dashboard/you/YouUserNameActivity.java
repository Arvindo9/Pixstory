package com.app.pixstory.ui.dashboard.you;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.viewpager.widget.PagerAdapter;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.core.dialog.universal_search.UniversalSearch;
import com.app.pixstory.databinding.ActivityYouUserNameBinding;
import com.app.pixstory.ui.dashboard.you.model.HomeYouViewModel;
import com.app.pixstory.ui.dashboard.you.model.UsernameViewModel;
import com.app.pixstory.ui.dashboard.you.navigator.UserCountNavigator;
import com.app.pixstory.ui.dashboard.you.pager.YouUsernamePager;
import com.app.pixstory.utils.Constants;
import com.google.android.material.tabs.TabLayout;

public class YouUserNameActivity extends BaseActivity<ActivityYouUserNameBinding, UsernameViewModel>
        implements View.OnClickListener, UserCountNavigator {


    private ActivityYouUserNameBinding binding;
    private UsernameViewModel viewModel;
    private int friend_count = 0, followers_count = 0, following_count = 0;

    @Override
    public int getLayout() {
        return R.layout.activity_you_user_name;
    }

    @Override
    protected void getBinding(ActivityYouUserNameBinding binding) {

        this.binding = binding;
    }

    @Override
    protected void getViewModel(UsernameViewModel viewModel) {

        this.viewModel = viewModel;
    }

    @Override
    protected Class<UsernameViewModel> setViewModel() {
        return UsernameViewModel.class;
    }

    @Override
    protected void init() {
        setUp();
        toolbar(binding.layoutToolbar.toolbar, binding.layoutToolbar.toolbarTitle, R.string.username);
        clickListener();
        getCountData();


    }

    public void setUp() {
        viewModel.getCount();
    }

    private void getCountData() {
        viewModel.getCountData().observe(this, data -> {
            friend_count = data.getFriendCount();
            followers_count = data.getFollowerCount();
            following_count = data.getFollowingCount();
            tabLayout();
        });
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search) {
            UniversalSearch.universalSearch(this);
        }
    }

    public void tabLayout() {
        Intent intent = getIntent();
        int values = intent.getIntExtra(Constants.KEY_DEFAULT_ACTIVITY_INTENT_INT, 0);
        // Adding the tabs using addTab() method.
       /* if (friend_count > 0) {
            binding.tabLayout.tabLayout.addTab(binding.tabLayout.tabLayout.newTab().setText(friend_count + " " + "Friends"));
        }
        if (following_count > 0) {
            binding.tabLayout.tabLayout.addTab(binding.tabLayout.tabLayout.newTab().setText(following_count + " " + "Followings"));
        }
        if (followers_count > 0) {
            binding.tabLayout.tabLayout.addTab(binding.tabLayout.tabLayout.newTab().setText(followers_count + " " + "Followers"));
        }*/

          binding.tabLayout.tabLayout.addTab(binding.tabLayout.tabLayout.newTab().setText(friend_count + " " + "Friends"));
          binding.tabLayout.tabLayout.addTab(binding.tabLayout.tabLayout.newTab().setText(following_count + " " + "Followings"));
          binding.tabLayout.tabLayout.addTab(binding.tabLayout.tabLayout.newTab().setText(followers_count + " " + "Followers"));


        // Create out pager adapter
        PagerAdapter pagerAdapter = new YouUsernamePager(getSupportFragmentManager(), binding.tabLayout.tabLayout.getTabCount());
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setOffscreenPageLimit(3);
        //  binding.viewPager.disableScroll(true);
        binding.viewPager.setCurrentItem(values);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout.tabLayout));

        binding.tabLayout.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
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

    }

    @Override
    public void message(String message) {

    }
}
