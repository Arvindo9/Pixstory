package com.app.pixstory.ui.dashboard.you.model;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.ui.dashboard.you.navigator.DeleteImageNavigator;

public class SelectImagesViewModel extends BaseViewModel {

    public String getToken() {
        return getDataManager().getAccessToken();
    }

    public void deleteImage(String token, int[] photo_id) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .deleteImages(token, photo_id)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    ((DeleteImageNavigator) getNavigator()).onImageDeletedResponse(response.getSuccess(),
                                            response.getMessage());
                                } else {
                                    ((DeleteImageNavigator) getNavigator()).message(response.getMessage());
                                }
                            } else {
                                ((DeleteImageNavigator) getNavigator()).message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            throwable.printStackTrace();
                        })
        );

    }
}
