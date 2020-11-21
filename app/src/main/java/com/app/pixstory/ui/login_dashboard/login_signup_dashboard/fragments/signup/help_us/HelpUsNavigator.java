package com.app.pixstory.ui.login_dashboard.login_signup_dashboard.fragments.signup.help_us;

import com.app.pixstory.base.BaseNavigator;
import com.app.pixstory.data.model.api.CountryResponse;

import java.util.List;
import java.util.Map;

/**
 * Author : Arvindo Mondal
 * Created on 16-03-2020
 */
public interface HelpUsNavigator extends BaseNavigator {
    void onSuccessSignUp(String loginType);

    void onError(Map<String, List<String>> error);

  //  void onGenerateUserName(String name, boolean status, boolean callSignUp);

    void onGenerateUserName(String name, boolean status);

    void checkMobile(String mobile,boolean status);

    void onMobileVerifies(boolean status, String countryCode, String mobile);

  //  void onVerificationResponse(boolean status, String countryCode, String mobile, String message, boolean resendOtp);
    void onVerificationResponse(boolean status, String countryCode, String mobile, String message, boolean resendOtp,String loginType);
    void setCountryList(List<CountryResponse.Data> data);
}
