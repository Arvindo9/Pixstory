package com.app.pixstory.ui.splash;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.databinding.ActivitySplashBinding;
import com.app.pixstory.ui.dashboard.DashboardActivity;
import com.app.pixstory.ui.intro.IntroPage;
import com.app.pixstory.ui.login_dashboard.login_signup_dashboard.LoginSignUpUserDashboard;
import com.facebook.AccessToken;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> implements SplashNavigator{

    private ActivitySplashBinding binding;
    private SplashViewModel viewModel;
    //replace package string with your package string
    public static final String PACKAGE = "com.app.pixstory";

    @Override
    public int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void getBinding(ActivitySplashBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(SplashViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected Class<SplashViewModel> setViewModel() {
        return SplashViewModel.class;
    }

    @Override
    protected void init() {
        setup();
        viewModel.setNavigator(this);
        viewModel.start();
      //  generateHashkey();
    }

    private void setup() {
        REQUEST_PERMISSION_FOR_ACTIVITY = false;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    // make the application close if splash activity is closed in Android
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void openDashboardFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, LoginSignUpUserDashboard.class));
            finish();
        }
    }

    public void generateHashkey(){
        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    PACKAGE,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKay = Base64.encodeToString(md.digest(), Base64.NO_WRAP);

                *//*((TextView) findViewById(R.id.hashKey))
                        .setText(Base64.encodeToString(md.digest(),
                                Base64.NO_WRAP));*//*


            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }*/

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    PACKAGE,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    //Navigator----------------------

    @Override
    public void openWelcomeScreen() {
        startActivity(IntroPage.class);
        finish();
    }

    @Override
    public void openLoginScreen() {
        startActivity(LoginSignUpUserDashboard.class);
        finish();
    }

    @Override
    public void openDashboardScreen() {
        startActivity(DashboardActivity.class);
        finish();
    }
}
