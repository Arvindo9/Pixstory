package com.app.pixstory.ui.navigation_view.account.account_settings;

import android.annotation.SuppressLint;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.data.model.api.AccountCombinedResponse;
import com.app.pixstory.data.model.api.AccountDetailResponse;
import com.app.pixstory.data.model.api.CountryResponse;

import java.net.UnknownHostException;

import io.reactivex.Flowable;

public class AccountSettingsViewModel extends BaseViewModel<AccountSettingsNavigator> {

    @SuppressLint("CheckResult")
    public void getAccountDetailData() {
        isLoading(true);
        Flowable<AccountDetailResponse> accountData = getDataManager()
                .getAccountDetailData("Bearer" + getDataManager().getAccessToken(), 0)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui());

        Flowable<CountryResponse> countryData = getDataManager()
                .countryList()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui());

        getCompositeDisposable().add(Flowable.zip(accountData, countryData, AccountCombinedResponse::new).subscribe(response -> {
                    isLoading(false);
                    if (response != null) {
                        if (response.getAccountDetailResponse() != null && response.getCountryResponse() != null) {
                            if (response.getAccountDetailResponse().getSuccess() && response.getCountryResponse().getSuccess()) {
                                getNavigator().accountDetailsFetched(response.getAccountDetailResponse().getData().getUser(), response.getCountryResponse().getData());
                            } else {
                                getNavigator().message(response.getAccountDetailResponse().getMessage());
                            }
                        } else {
                            getNavigator().message(R.string.default_error);
                        }
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
                }));
    }

    public void updateAccountDetail(String type, String firstName, String lastName, String email, String username, String gender, String dob, String countryId, String mobile, String countryCode) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .updateAccountDetail("Bearer" + getDataManager().getAccessToken(), firstName, lastName, email, username, gender, dob, countryId, mobile, countryCode)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().accountUpdated(response.getMessage(), type);
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

    public void checkUsername(String username) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .generateUserName(username)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {

                                    getNavigator().usernameValid();

                                } else {
                                    getNavigator().usernameInvalid(response.getError());
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

    public void updatePassword(String oldPassword, String newPassword, String confirmPassword) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .changePassword("Bearer" + getDataManager().getAccessToken(), oldPassword, newPassword, confirmPassword)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().passwordChanged(response.getMessage());

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

    public void onVerifyOtpMobile(String type, String verifyType, String email, String token, String mobile, String countryCode, String otp) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .verifyUpdateEmailMobile("Bearer" + getDataManager().getAccessToken(), type, verifyType, email, token, mobile, countryCode, otp)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().onMobileVerifies(true, response.getMessage());
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
