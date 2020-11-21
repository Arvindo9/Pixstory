package com.app.pixstory.ui.login_dashboard.login_signup_dashboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.app.pixstory.BuildConfig;
import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.data.DataManager;
import com.app.pixstory.ui.dashboard.DashboardActivity;
import com.app.pixstory.ui.login_dashboard.login_signup_dashboard.fragments.login.LoginNavigator;
import com.app.pixstory.ui.login_dashboard.login_signup_dashboard.fragments.signup.SignUpNavigator;
import com.app.pixstory.utils.GlobalMethods;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;

import static com.app.pixstory.utils.Constants.LOGIN_TYPE_FACEBOOK;
import static com.app.pixstory.utils.Constants.LOGIN_TYPE_GOOGLE;
import static com.app.pixstory.utils.Constants.LOGIN_TYPE_LINKEDIN;
import static com.app.pixstory.utils.Constants.VERIFY_TYPE_LOGIN;
import static com.app.pixstory.utils.Constants.VERIFY_TYPE_REGISTER;

public class SignupViewModel extends BaseViewModel {

    public void sendEmailOtp(String email, String type, boolean resendOtp) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .sendOtpEmail(email, type)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    ((SignUpNavigator)getNavigator()).onEmailVerificationResponse(response.getSuccess(), email, response.getMessage(), resendOtp, response.getData().getToken());
                                } else {
                                    ((SignUpNavigator) getNavigator()).onEmailVerificationResponse(false, email, response.getMessage(), resendOtp, "");
                                }
                            } else {
                                ((SignUpNavigator) getNavigator()).onEmailVerificationResponse(false, email, response.getMessage(), resendOtp, "");
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            ((SignUpNavigator) getNavigator()).onEmailVerificationResponse(false, email, null, resendOtp, "");
                            throwable.printStackTrace();
                        })
        );
    }

    public void onVerifyOtpEmail(String email, String otp, String token, String type) {

        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .verifyOtpEmail(otp, email, "",token, type)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {

                                    if (type.equals(VERIFY_TYPE_LOGIN)) {
                                        getDataManager().setAccessToken(response.getData().getToken());
                                        ((SignUpNavigator) getNavigator()).openHomeActivity();
                                        getDataManager().setLoginMode(DataManager.LogInMode.LOGGED_IN_MODE_LOGGED_IN_EMAIL.getType());
                                    } else if (type.equals(VERIFY_TYPE_REGISTER)) {

                                        ((SignUpNavigator) getNavigator()).openSignupEmail(email);

                                    }

                                } else {
                                    ((SignUpNavigator) getNavigator()).message(response.getMessage());
                                }
                            } else {
                                ((SignUpNavigator) getNavigator()).message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            if (throwable instanceof UnknownHostException) {
                                ((SignUpNavigator) getNavigator()).message(R.string.default_network_error);
                            } else {
                                ((SignUpNavigator) getNavigator()).message(R.string.default_error);
                            }
                            throwable.printStackTrace();
                        })
        );

    }

    public void doLogInWithGoogle(String googleId, String personName, String email, String firstName, String lastName) {

        HashMap<String, String> list = new HashMap<>();
        list.put("firstName", firstName);
        list.put("lastName", lastName);
        list.put("personName", personName);
        list.put("email", email);
        list.put("facebook", "");
        list.put("google", googleId);
        list.put("linkedin", "");
        doSocialLogin(LOGIN_TYPE_GOOGLE, "", "", googleId, list);
    }

    public Bundle getFacebookData(Context context, JSONObject object) {
        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");
            URL profile_pic = null;
            try {
                profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
            String f_name = "", l_name = "", email = "", gender = "", age = "";
            bundle.putString("idFacebook", id);
            if (object.has("first_name")) {
                f_name = object.getString("first_name");
                bundle.putString("first_name", object.getString("first_name"));
            }
            if (object.has("last_name")) {
                l_name = object.getString("last_name");
                bundle.putString("last_name", object.getString("last_name"));
            }
            if (object.has("email")) {
                email = object.getString("email");
                bundle.putString("email", object.getString("email"));
            }
            if (object.has("gender")) {
                gender = object.getString("gender");
                bundle.putString("gender", object.getString("gender"));
            }
            if (object.has("birthday")) {
                age = object.getString("birthday");
                bundle.putString("birthday", object.getString("birthday"));
            }
//              doLoginWithFacebook(id, f_name, l_name, email, gender, age, profile_pic);
            HashMap<String, String> list = new HashMap<>();
            list.put("firstName", f_name);
            list.put("lastName", l_name);
            list.put("email", email);
            list.put("gender", gender);
            list.put("age", age);
            list.put("mobile", age);
            list.put("facebook", id);
            list.put("google", "");
            list.put("linkedin", "");

//            ((LoginNavigator)getNavigator()).onFacebookResponse(Constants.LOGIN_TYPE_FACEBOOK, id, "", "", list);
            doSocialLogin(LOGIN_TYPE_FACEBOOK, id, "", "", list);
            return bundle;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void skipUser(Context context) {
        GlobalMethods.intentMethod(context, DashboardActivity.class);
    }

    //APIs-----------------------

    public void doSocialLogin(String loginType, String facebookId, String linkedIn, String google, HashMap<String, String> data) {
        isLoading(true);
        String social_id = "";

        if (loginType.equals(LOGIN_TYPE_GOOGLE))
            social_id = google;
        else if (loginType.equals(LOGIN_TYPE_FACEBOOK))
            social_id = facebookId;
        else if (loginType.equals(LOGIN_TYPE_LINKEDIN))
            social_id = linkedIn;

        getCompositeDisposable().add(getDataManager()
                .login(loginType, "", "", "", "", "", social_id)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    if (response.getData().getFormCompletion() == 1) {
                                        //Open interest
                                        getDataManager().setAccessToken(response.getData().getToken());
                                        ((SignUpNavigator) getNavigator()).openInterest();
                                    } else if (response.getData().getFormCompletion() == 2) {
                                        //Open Home
                                        getDataManager().setAccessToken(response.getData().getToken());
                                        if (loginType.equals(LOGIN_TYPE_GOOGLE)) {
                                            getDataManager().setLoginMode(DataManager.LogInMode.LOGGED_IN_MODE_LOGGED_IN_GOOGLE.getType());
                                        } else if (loginType.equals(LOGIN_TYPE_FACEBOOK)) {
                                            getDataManager().setLoginMode(DataManager.LogInMode.LOGGED_IN_MODE_LOGGED_IN_FACEBOOK.getType());
                                        }
                                        ((SignUpNavigator) getNavigator()).openHomeActivity();
                                    } else {
                                        // getDataManager().setAccessToken(response.getData().getToken());
                                        ((SignUpNavigator) getNavigator()).openSignupFromSocial(loginType, data);
                                    }
                                } else {
                                    if (response.getError().getFormCompletion() == 0) {
                                        //getDataManager().setAccessToken(response.getData().getToken());
                                        ((SignUpNavigator) getNavigator()).openSignupFromSocial(loginType, data);
                                    }
                                }
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            throwable.printStackTrace();
                        })
        );
    }

    //From login and sign up
    public void verificationRequestMobile(String countryCode, String mobile, boolean resendOtp, String type) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .verificationRequestMobile(countryCode, mobile, type)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    ((SignUpNavigator) getNavigator()).onVerificationResponse(response.getSuccess(), countryCode,
                                            mobile, response.getMessage(), resendOtp);
                                } else {
                                    ((SignUpNavigator) getNavigator()).onVerificationResponse(false, countryCode,
                                            mobile, response.getMessage(), resendOtp);
                                }
                            } else {
                                ((SignUpNavigator) getNavigator()).onVerificationResponse(false, countryCode,
                                        mobile, null, resendOtp);
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            ((SignUpNavigator) getNavigator()).onVerificationResponse(false, countryCode,
                                    mobile, null, resendOtp);
                            throwable.printStackTrace();
                        })
        );
    }

    //mobile otp verification
    public void onVerifyOtpMobile(String countryCode, String mobile, String otp) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .verificationRequestMobileOtp(countryCode, mobile, otp)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    if (response.getData() != null && response.getData().getType() != null) {
                                        if (response.getData().getType() == 1) {
                                            //Open interest
                                            getDataManager().setAccessToken(response.getData().getToken());
                                            ((SignUpNavigator) getNavigator()).openInterest();
                                        } else if (response.getData().getType() == 2) {
                                            //Open Home
                                            getDataManager().setAccessToken(response.getData().getToken());
                                            getDataManager().setLoginMode(DataManager.LogInMode.LOGGED_IN_MODE_LOGGED_IN_MOBILE.getType());
                                            ((SignUpNavigator) getNavigator()).openHomeActivity();
                                        } else {
                                            getDataManager().setAccessToken(response.getData().getToken());
                                            ((SignUpNavigator) getNavigator()).openSignup(countryCode, mobile);
                                        }
                                    }
                                } else {
                                    ((SignUpNavigator) getNavigator()).message(response.getMessage());
                                }
                            } else {
                                ((SignUpNavigator) getNavigator()).message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            if (throwable instanceof UnknownHostException) {
                                ((SignUpNavigator) getNavigator()).message(R.string.default_network_error);
                            } else {
                                ((SignUpNavigator) getNavigator()).message(R.string.default_error);
                            }
                            throwable.printStackTrace();
                        })
        );
    }

    // app version
    public void appVersion(String loginType, String inputValue) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .getAppVersion()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null && response.getStatus()) {
                                String vName = response.getAppVersion().getAppVersionName();
                                int vCode = response.getAppVersion().getAppVersionCode();

                                if (vCode > BuildConfig.VERSION_CODE) {
                                    if (getNavigator() instanceof LoginNavigator) {
                                        ((SignUpNavigator) getNavigator()).openAppUpgrade(vName, vCode);
                                    }
                                }
                            }
                        },
                        throwable -> {
                            isLoading(true);
                            throwable.printStackTrace();
                        }
                )
        );
    }

}
