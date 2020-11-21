package com.app.pixstory.ui.dashboard.you.activity;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;

public class PageDetailViewModel extends BaseViewModel<PageDetailNavigator> {

    public String getToken() {
        return getDataManager().getAccessToken();
    }

    public void getPageDetail(int pageId) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .getPageDetail("Bearer" + getToken(), pageId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    if (response.getData() != null) {
                                        getNavigator().pageDetailFetched(response.getData());
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

    public void followPage(int pageId, int isFollow) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .followPage("Bearer" + getToken(), pageId, isFollow)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                        getNavigator().pageFollowed(response.getMessage(), isFollow);
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
}
