package com.app.pixstory.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.data.DataManager;
import com.app.pixstory.data.model.api.AccountDetailResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.net.UnknownHostException;

public class DashboardModel extends BaseViewModel<DashboardNavigator> {

    public MutableLiveData<Boolean> loggedOut = new MutableLiveData<>();

    public void setToken(String token) {

        getDataManager().setAccessToken(token);
    }


    public void refreshToken() {
        isLoading(true);
        String token = getDataManager().getAccessToken();
        getCompositeDisposable().add(getDataManager()
                .refreshToken(token)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess() && response.getData() != null &&
                                        response.getData().getToken() != null) {
                                    getNavigator().onTokenResponse(
                                            response.getSuccess(),
                                            response.getData());
                                    // getDataManager().setAccessToken(response.getData().getToken());
                                } else {
                                    getNavigator().message(response.getMessage());
                                }
                            } else {
                                getNavigator().message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            throwable.printStackTrace();
                        })
        );
    }

    public void logout(GoogleApiClient googleApiClient) {
        google(googleApiClient);
        facebook();
        logoutAPI();

    }

    public void google(GoogleApiClient googleApiClient) {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        //     logoutAPI();
                    }
                });
    }

    public void facebook() {
        LoginManager.getInstance().logOut();
        //logoutAPI();
    }

    public void logoutAPI() {
        try {
            isLoading(true);
            getCompositeDisposable().add(getDataManager()
                    .logout("Bearer" + getDataManager().getAccessToken())
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(response -> {
                                isLoading(false);
                                if (response != null) {
                                    if (response.getSuccess()) {
                                        loggedOut.postValue(true);
                                        getDataManager().setLoginMode(DataManager.LogInMode.LOGGED_IN_MODE_LOGGED_OUT.getType());
                                    } else {
                                        getNavigator().message("Logout failed!!!");
                                    }
                                } else {
                                    getNavigator().message("Logout failed!!!");
                                    loggedOut.postValue(false);
                                }
                            },
                            throwable -> {
                                isLoading(false);
                                throwable.printStackTrace();
                                loggedOut.postValue(true);
                            })
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public LiveData<Boolean> getLoggedOut() {
        return loggedOut;
    }


    public MutableLiveData<AccountDetailResponse.User> userLiveData = new MutableLiveData<>();

    public LiveData<AccountDetailResponse.User> getUserLiveData() {
        return userLiveData;
    }

    public void getProfileData() {
        //isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .getAccountDetailData("Bearer" + getDataManager().getAccessToken(), 0)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            //isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    userLiveData.setValue(response.getData().getUser());
                                } else {
                                    getNavigator().message(response.getMessage());
                                }
                            } else {
                                getNavigator().message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            //isLoading(false);
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

