package com.app.pixstory.ui.login_dashboard.login_signup_dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.databinding.ActivityLoginSignupUserDashboardBinding;
import com.app.pixstory.ui.login_dashboard.login_signup_dashboard.adapter.UserLoginAdapter;
import com.app.pixstory.core.social.BaseGoogleLogin;
import com.app.pixstory.core.social.BaseLinkedInLogin;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

import static com.app.pixstory.core.social.BaseGoogleLogin.RC_SIGN_IN;

public class LoginSignUpUserDashboard extends BaseActivity<ActivityLoginSignupUserDashboardBinding, LoginViewModel>
        implements TabLayout.OnTabSelectedListener,
        GoogleApiClient.OnConnectionFailedListener {

    ActivityLoginSignupUserDashboardBinding binding;
    LoginViewModel viewModel;

    @Override
    public int getLayout() {
        return R.layout.activity_login_signup_user_dashboard;
    }

    @Override
    protected void getBinding(ActivityLoginSignupUserDashboardBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(LoginViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected Class<LoginViewModel> setViewModel() {
        return LoginViewModel.class;
    }

    @Override
    protected void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        tabLayout();
    }

    public void tabLayout() {
        // Adding the tabs using addTab() method.
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getResources().getString(R.string.login)));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getResources().getString(R.string.signup)));
        // Create out pager adapter
        PagerAdapter pagerAdapter = new UserLoginAdapter(getSupportFragmentManager(), binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setOffscreenPageLimit(0);
        binding.viewPager.disableScroll(true);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(this);

        LinearLayout linearLayout = (LinearLayout) binding.tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.BLACK);
        drawable.setSize(3, 1);
        linearLayout.setDividerDrawable(drawable);

        googleLogin();
        linkedInLogIn();

    }

    private void googleLogin() {
        BaseGoogleLogin.getInstance().setup(this, this::onSuccessGoogleLogin);
    }

    private void onSuccessGoogleLogin(String googleId, String personName, String email, String firstName, String lastName) {
        //call viewModel
     /*   Toast.makeText(this, "Name:" + personName + "\n" +
                "Email:" + email, Toast.LENGTH_SHORT).show();*/
        viewModel.doLogInWithGoogle(googleId, personName, email, firstName, lastName);
    }

    private void linkedInLogIn() {
        BaseLinkedInLogin.getInstance().setUp(this, this::onSuccessLinkedInLogin);
    }

    private void onSuccessLinkedInLogin(JSONObject jsonObject) {
        try {
            Toast.makeText(this, "Email:" + jsonObject.get("emailAddress").toString() + "\n" +
                    "Formatted Name:" + jsonObject.get("formattedName").toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            BaseGoogleLogin.getInstance().onActivityResult(requestCode, resultCode, data);
        } else {
            BaseLinkedInLogin.getInstance().onActivityResult(requestCode, resultCode, data);
        }
    }
}



