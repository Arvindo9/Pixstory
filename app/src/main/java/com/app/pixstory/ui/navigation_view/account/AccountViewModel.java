package com.app.pixstory.ui.navigation_view.account;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import java.net.UnknownHostException;

public class AccountViewModel extends BaseViewModel<AccountNavigator> {

    public void getUserPrefrence() {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .getUserPrefrence("Bearer"+getDataManager().getAccessToken())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().getPreference(response.getData());
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

    public void updateUserPreference(boolean yourProfileActivity, int yourProfileActivityType, boolean myProfileActivity,
                                     boolean allowMessage, int allowMessageType, boolean helpOther)
    {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .updateUserPrefrence("Bearer"+getDataManager().getAccessToken(), yourProfileActivity? 1:0, myProfileActivity? 1:0,
                        allowMessage? 1:0, helpOther? 1:0,yourProfileActivityType,allowMessageType)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().prefrenceUpdated();
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