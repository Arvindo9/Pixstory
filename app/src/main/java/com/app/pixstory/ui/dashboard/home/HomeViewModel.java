package com.app.pixstory.ui.dashboard.home;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;

public class HomeViewModel extends BaseViewModel {

    public String getToken() {
        return getDataManager().getAccessToken();
    }

    public void getStoryHome(String token, int page, int limit, int limit_story, String filter) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .getStoryHome(token, page, limit, limit_story, filter)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    if (response.getData() != null) {
                                        ((HomeNavigator) getNavigator()).onStoryHomeResponse(
                                                response.getSuccess(),
                                                response.getData());
                                    }
                                } else {
                                    ((HomeNavigator) getNavigator()).message(response.getMessage());
                                }
                            } else {
                                ((HomeNavigator) getNavigator()).message(R.string.default_error);
                            }
                        },
                        throwable -> {

                            isLoading(false);
                            throwable.printStackTrace();
                        })
        );

    }
}
