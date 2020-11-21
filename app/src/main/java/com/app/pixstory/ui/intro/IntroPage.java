package com.app.pixstory.ui.intro;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.data.local.prefs.Preferences;
import com.app.pixstory.databinding.ActivityIntroPageBinding;
import com.app.pixstory.ui.login_dashboard.login_signup_dashboard.LoginSignUpUserDashboard;

public class IntroPage extends BaseActivity<ActivityIntroPageBinding, IntroViewModel> {

    ActivityIntroPageBinding binding;
    private int[] layouts;
    private IntroViewModel viewModel;

    @Override
    protected Class<IntroViewModel> setViewModel() {
        return IntroViewModel.class;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_intro_page;
    }

    @Override
    protected void getBinding(ActivityIntroPageBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(IntroViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // making notification bar transparent
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro_page);


        //layout of all welcome slider
        layouts = new int[]{
                R.layout.intro_first,
                R.layout.intro_second,
                R.layout.intro_third
        };
        // adding bottom dots

        // making notification bar transparent
        //  changeStatusBarColor();

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        binding.viewPager.setAdapter(myViewPagerAdapter);
        binding.viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        //  binding.indicator.setViewPager(binding.viewPager);

        binding.btnSkip.setOnClickListener(v -> launchHomeScreen());

        binding.btnNext.setOnClickListener(v -> {
            // checking for last page
            // if last page home screen will be launched
            int current = getItem(+1);
            if (current < layouts.length) {
                // move to next screen
                binding.viewPager.setCurrentItem(current);
            } else {
                launchHomeScreen();
            }
        });
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            binding.pageIndicatorView.setSelection(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                binding.btnNext.setText(getString(R.string.ok));
                binding.btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                binding.btnNext.setText(getString(R.string.next));
                binding.btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };


    private void launchHomeScreen() {
        viewModel.setLoginMode();
        startActivity(new Intent(this, LoginSignUpUserDashboard.class));
        finish();
    }

    private int getItem(int i) {
        return binding.viewPager.getCurrentItem() + i;
    }

    /**
     * View pager adapter
     */
    private class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        private MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
