package com.app.pixstory.ui.dashboard.you.fragments.friends;

import androidx.lifecycle.ViewModel;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.ui.dashboard.you.navigator.DeleteImageNavigator;

import java.net.UnknownHostException;

import static com.app.pixstory.utils.Constants.BEARER;

public class FriendsViewModel extends BaseViewModel<FriendsNavigator> {

    String token = BEARER + getDataManager().getAccessToken();

    public void getFriendList(int current_user, int total_user) {
        getCompositeDisposable().add(getDataManager()
                .getFriendList(token, current_user, total_user)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            if (response != null) {
                                if (response.getSuccess()) {
                                    if (response.getFriendData() != null) {
                                        ((FriendsNavigator) getNavigator()).onFriendResponse(response.getSuccess(),
                                                response.getFriendData(),
                                                response.getFriendData().getFriendList());
                                    }
                                } else {
                                    ((FriendsNavigator) getNavigator()).message(response.getMessage());
                                }
                            } else {
                                ((FriendsNavigator) getNavigator()).message(R.string.default_error);
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
