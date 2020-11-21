package com.app.pixstory.ui.dashboard.you.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.data.model.api.AccountDetailResponse;
import com.app.pixstory.ui.dashboard.you.navigator.HomeYouNavigator;

import java.net.UnknownHostException;

import static com.app.pixstory.utils.Constants.BEARER;

public class HomeYouViewModel extends BaseViewModel<HomeYouNavigator> {

    String token = BEARER + getDataManager().getAccessToken();

    public void getStoryUser(String userType, String type, int userId, int page, int limit) {
        isLoading(true);
        isSwipeRefresh(true);
        getCompositeDisposable().add(getDataManager()
                .getStoryUser(token, userType, type, userId, page, limit)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            isSwipeRefresh(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    if (response.getData() != null) {
                                        getNavigator().onStoryUserResponse(
                                                response.getSuccess(),
                                                response.getData(),
                                                response.getData().getStory());
                                    }
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

    public void getPageList(String userType, String type, int userId, int page, int limit) {
        isLoading(true);
        isSwipeRefresh(true);
        getCompositeDisposable().add(getDataManager()
                .pageListHome(token, userType, type, userId, page, limit)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            isSwipeRefresh(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    if (response.getData() != null) {
                                        getNavigator().onPageResponse(
                                                response.getSuccess(),
                                                response.getData());
                                    }
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

    public void yourPhotos(String userType, String type, int userId, int page, int limit) {
        isLoading(true);
        isSwipeRefresh(true);
        getCompositeDisposable().add(getDataManager()
                .getUserPhoto(token, userType, type, userId, page, limit)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    if (response.getData() != null) {
                                        getNavigator().onMyPhotoResponse(response.getSuccess(),
                                                response.getData(),
                                                type);
                                    }
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

    // self user profile details
    public MutableLiveData<AccountDetailResponse.User> selfUserLiveData = new MutableLiveData<>();

    public LiveData<AccountDetailResponse.User> getSelfUserLiveData() {
        return selfUserLiveData;
    }

    // data and badge
    public MutableLiveData<AccountDetailResponse.Data> selfUserBadgeLiveData = new MutableLiveData<>();

    public LiveData<AccountDetailResponse.Data> getSelfUserBadgeLiveData() {
        return selfUserBadgeLiveData;
    }

    public void getUserProfileData(int userId) {
        //isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .getAccountDetailData(token, userId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            //isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    selfUserLiveData.setValue(response.getData().getUser());
                                    selfUserBadgeLiveData.setValue(response.getData());
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

    public void getOtherUserProfileData(int userId) {
        //isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .getUserDetailById(token, userId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            //isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    selfUserLiveData.setValue(response.getData().getUser());
                                    selfUserBadgeLiveData.setValue(response.getData());
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
