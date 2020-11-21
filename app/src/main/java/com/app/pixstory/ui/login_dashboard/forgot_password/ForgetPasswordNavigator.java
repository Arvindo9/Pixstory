package com.app.pixstory.ui.login_dashboard.forgot_password;

import com.app.pixstory.base.BaseNavigator;

public interface ForgetPasswordNavigator extends BaseNavigator {

    void onVerificationResponse(boolean b, String type, String countryCode, String mobile, String message, boolean resendOtp, String token, String otp);

    void openChangePassword(String typeForget, String token, String countryCode, String phone, String username, String email, String type);

    void passwordChanged();

}
