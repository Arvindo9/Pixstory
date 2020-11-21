package com.app.pixstory.ui.dashboard.you.fragments.followings;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;

import java.net.UnknownHostException;

import static com.app.pixstory.utils.Constants.BEARER;

public class FollowingViewModel extends BaseViewModel<FollowingsNavigator> {

    String token = BEARER + getDataManager().getAccessToken();

    public void getFollowingList(int current_user, int total_user) {
        getCompositeDisposable().add(getDataManager()
                .getFollowingList(token, current_user, total_user)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            if (response != null) {
                                if (response.getSuccess()) {
                                    if (response.getData() != null) {
                                        ((FollowingsNavigator) getNavigator()).onFollowingResponse(response.getSuccess(),
                                                response.getData(),
                                                response.getData().getFollowingList());
                                    }
                                } else {
                                    ((FollowingsNavigator) getNavigator()).message(response.getMessage());
                                }
                            } else {
                                ((FollowingsNavigator) getNavigator()).message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            throwable.printStackTrace();
                        })
        );
    }

    public void doFollow(int receiver, int follow, int position) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .followRequest("Bearer" + getDataManager().getAccessToken(), receiver, follow)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().followStatusChanged(response.getMessage(), position, follow);

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
