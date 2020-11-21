package com.app.pixstory.ui.login_dashboard.login_signup_dashboard.fragments.signup.help_us;

import android.content.Context;
import android.util.Log;

import com.app.pixstory.BuildConfig;
import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.utils.Utils;

import java.net.UnknownHostException;

public class HelpUsViewModel extends BaseViewModel<HelpUsNavigator> {

    void doSignUp(Context context, String loginType, String firstName, String lastName,
                  String userName, String gender, String dob,
                  String countryId, String phoneNumber, String email, String password, String confirmPassword,
                  String socialId, String countryCode, String emailVerified, String mobileVerified) {

        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .signup(loginType, firstName, lastName, userName, gender, dob, countryId, phoneNumber, email, password,
                        confirmPassword, emailVerified, mobileVerified, Utils.getDeviceId(context), BuildConfig.PLATFORM, countryCode,
                        socialId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess() && response.getData() != null) {
                                    if (response.getData().getToken() != null) {
                                        getDataManager().setAccessToken(response.getData().getToken());
                                        getNavigator().onSuccessSignUp(loginType);
                                    } else {
                                        getNavigator().message(response.getMessage());
                                    }
                                } else {
                                    getNavigator().onError(response.getError());
                                }
                            } else {
                                getNavigator().message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            if (throwable instanceof UnknownHostException) {
                                getNavigator().message(R.string.default_network_error);
                            } else {
                                getNavigator().message(R.string.default_error);
                            }
                            Log.e("doSignUp", "false");
                            throwable.printStackTrace();
                        }));
    }

    public void generateUserName(String name) {
        getCompositeDisposable().add(getDataManager()
                .generateUserName(name)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            if (response != null) {
                                if (response.getSuccess()) {
                                    if (response.getError() == null) {
                                        getNavigator().onGenerateUserName(name, true);
                                    } else if (response.getError().containsKey("username")) {
                                        getNavigator().message(response.getMessage());
                                        getNavigator().onGenerateUserName(name, false);
                                    }
                                } else {
                                    getNavigator().onError(response.getError());
                                    getNavigator().onGenerateUserName(name, false);
                                }
                            } else {
                                getNavigator().message(R.string.default_error);
                                getNavigator().onGenerateUserName(name, false);
                            }

                        },
                        throwable -> {
                            getNavigator().onGenerateUserName(name, false);
                            if (throwable instanceof UnknownHostException) {
                                getNavigator().message(R.string.default_network_error);
                            } else {
                                getNavigator().message(R.string.default_error);
                            }
                            Log.e("doSignUp", "false");
                            throwable.printStackTrace();
                        })
        );
    }

    public void checkMobile(String mobile) {
        getCompositeDisposable().add(getDataManager()
                .checkMobile(mobile)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            if (response != null) {
                                if (response.getSuccess()) {
                                    if (response.getError() == null) {
                                        getNavigator().checkMobile(mobile, true);
                                    } else if (response.getError().containsKey("username")) {
                                        getNavigator().message(response.getMessage());
                                        getNavigator().checkMobile(mobile, false);
                                    }
                                } else {
                                    getNavigator().onError(response.getError());
                                    getNavigator().checkMobile(mobile, false);
                                }
                            } else {
                                getNavigator().message(R.string.default_error);
                                getNavigator().checkMobile(mobile, false);
                            }

                        },
                        throwable -> {
                            getNavigator().checkMobile(mobile, false);
                            if (throwable instanceof UnknownHostException) {
                                getNavigator().message(R.string.default_network_error);
                            } else {
                                getNavigator().message(R.string.default_error);
                            }
                            Log.e("doSignUp", "false");
                            throwable.printStackTrace();
                        })
        );
    }

    //From login and sign up
    public void verificationRequestMobile(String countryCode, String mobile, boolean resendOtp, String type, String loginType) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .verificationRequestMobile(countryCode, mobile, type)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().onVerificationResponse(response.getSuccess(), countryCode,
                                            mobile, response.getMessage(), resendOtp, loginType);
                                } else {
                                    getNavigator().onVerificationResponse(false, countryCode,
                                            mobile, response.getMessage(), resendOtp, loginType);
                                }
                            } else {
                                getNavigator().onVerificationResponse(false, countryCode,
                                        mobile, null, resendOtp, loginType);
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            getNavigator().onVerificationResponse(false, countryCode,
                                    mobile, null, resendOtp, loginType);
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
                                    getNavigator().onMobileVerifies(true, countryCode, mobile);
                                } else {
                                    getNavigator().message(response.getMessage());
                                }
                            } else {
                                getNavigator().message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            if (throwable instanceof UnknownHostException) {
                                getNavigator().message(R.string.default_network_error);
                            } else {
                                getNavigator().message(R.string.default_error);
                            }
                            throwable.printStackTrace();
                        })
        );
    }

    public void getCountryList() {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .countryList()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().setCountryList(response.getData());
                                } else {
                                    getNavigator().message(response.getMessage());
                                }
                            } else {
                                getNavigator().message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            if (throwable instanceof UnknownHostException) {
                                getNavigator().message(R.string.default_network_error);
                            } else {
                                getNavigator().message(R.string.default_error);
                            }
                            throwable.printStackTrace();
                        })
        );
    }
}
