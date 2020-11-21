package com.app.pixstory.ui.login_dashboard.forgot_password;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;

import java.net.UnknownHostException;

public class ForgotPasswordViewModel extends BaseViewModel {


    public void verificationRequest(String type, String username, String email, String countryCode, String phone, boolean resendOtp) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .forgetPassword(type, email, username, countryCode, phone)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    ((ForgetPasswordNavigator) getNavigator()).onVerificationResponse(response.getSuccess(), type, countryCode,
                                            phone, response.getMessage(), resendOtp, response.getData().getToken(), response.getData().getOtp());
                                } else {
                                    ((ForgetPasswordNavigator) getNavigator()).onVerificationResponse(false, type, countryCode,
                                            phone, response.getMessage(), resendOtp, "", "");
                                }
                            } else {
                                ((ForgetPasswordNavigator) getNavigator()).onVerificationResponse(false, type, countryCode,
                                        phone, null, resendOtp, "", "");
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            ((ForgetPasswordNavigator) getNavigator()).onVerificationResponse(false, type, countryCode,
                                    phone, null, resendOtp, "", "");
                            throwable.printStackTrace();
                        })
        );
    }

    public void onVerifyOtpMobile(String type, String countryCode, String mobile, String otp, String token) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .verifyForgetPhoneOtp(type, otp, countryCode, mobile)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    ((ForgetPasswordNavigator) getNavigator()).openChangePassword("", token, countryCode, mobile, "","",type);

                                } else {
                                    ((ForgetPasswordNavigator) getNavigator()).message(response.getMessage());
                                }
                            } else {
                                ((ForgetPasswordNavigator) getNavigator()).message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            if (throwable instanceof UnknownHostException) {
                                ((ForgetPasswordNavigator) getNavigator()).message(R.string.default_network_error);
                            } else {
                                ((ForgetPasswordNavigator) getNavigator()).message(R.string.default_error);
                            }
                            throwable.printStackTrace();
                        })
        );
    }

    public void updatePassword(String password, String confirmPassword, String type, String token, String countryCode, String phone,String username,String email) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .updatePassword(password, confirmPassword, token, type, countryCode, phone, username,email)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    ((ForgetPasswordNavigator) getNavigator()).passwordChanged();

                                } else {
                                    ((ForgetPasswordNavigator) getNavigator()).message(response.getMessage());
                                }
                            } else {
                                ((ForgetPasswordNavigator) getNavigator()).message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            if (throwable instanceof UnknownHostException) {
                                ((ForgetPasswordNavigator) getNavigator()).message(R.string.default_network_error);
                            } else {
                                ((ForgetPasswordNavigator) getNavigator()).message(R.string.default_error);
                            }
                            throwable.printStackTrace();
                        })
        );
    }

    public void onVerifyOtpEmail(String username,String email, String otp, String token, String typeForget,String type) {

        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .verifyOtpEmail(otp, email, username,token, typeForget)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {

                                    ((ForgetPasswordNavigator) getNavigator()).openChangePassword(typeForget, token, "", "",username, email,type);

                                } else {
                                    ((ForgetPasswordNavigator) getNavigator()).message(response.getMessage());
                                }
                            } else {
                                ((ForgetPasswordNavigator) getNavigator()).message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            if (throwable instanceof UnknownHostException) {
                                ((ForgetPasswordNavigator) getNavigator()).message(R.string.default_network_error);
                            } else {
                                ((ForgetPasswordNavigator) getNavigator()).message(R.string.default_error);
                            }
                            throwable.printStackTrace();
                        })
        );

    }


}
