package com.app.pixstory.ui.login_dashboard.login_signup_dashboard.fragments.signup;

import com.app.pixstory.base.BaseNavigator;

import java.util.HashMap;

public interface SignUpNavigator extends BaseNavigator {

    void openAppUpgrade(String versionName, int versionCode);

    void onVerificationResponse(boolean b, String countryCode, String mobile, String message, boolean resendOtp);

    void openInterest();

    void openHomeActivity();

    void openSignup(String countryCode, String mobile);

    void openSignupFromSocial(String loginType, HashMap<String, String> data);

    void onEmailVerificationResponse(boolean b, String email, String message, boolean resendOtp,String token);

    void openSignupEmail(String email);
}
