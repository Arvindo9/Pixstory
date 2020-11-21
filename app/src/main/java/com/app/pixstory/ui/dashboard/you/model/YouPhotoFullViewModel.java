package com.app.pixstory.ui.dashboard.you.model;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.ui.dashboard.you.navigator.DeleteImageNavigator;

public class YouPhotoFullViewModel extends BaseViewModel {

    public String getToken() {
        return getDataManager().getAccessToken();
    }

    public void deleteSingleImage(String token, int photo_id) {
        getCompositeDisposable().add(getDataManager()
                .deleteSingleImages(token, photo_id)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
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
                            throwable.printStackTrace();
                        })
        );

    }

    public void favouritePhoto(String token, int is_fav, int photo_id) {
        getCompositeDisposable().add(getDataManager()
                .setImageFavourite(token, is_fav, photo_id)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            if (response != null) {
                                if (response.getSuccess()) {
                                    ((DeleteImageNavigator) getNavigator()).onImageFavouriteResponse(response.getSuccess(),
                                            response.getMessage());
                                } else {
                                    ((DeleteImageNavigator) getNavigator()).message(response.getMessage());
                                }
                            } else {
                                ((DeleteImageNavigator) getNavigator()).message(R.string.default_error);
                            }
                        },
                        throwable -> {

                            throwable.printStackTrace();
                        })
        );
    }
}
