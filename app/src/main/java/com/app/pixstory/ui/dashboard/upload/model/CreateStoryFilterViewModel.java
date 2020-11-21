package com.app.pixstory.ui.dashboard.upload.model;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.ui.dashboard.upload.navigator.UploadNavigator;

import static com.app.pixstory.utils.Constants.BEARER;

public class CreateStoryFilterViewModel extends BaseViewModel {

    String token = BEARER + getDataManager().getAccessToken();

    public void addPhotoDetails(Integer id, String captionStr, String photoCreditsStr, int[] interest) {
        isLoading(true);

        getCompositeDisposable().add(getDataManager()
                .addPhotoDetails(token, id, captionStr, photoCreditsStr, interest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    if (response.getData() != null) {
                                        ((UploadNavigator) getNavigator()).onDetailResponse(response.getSuccess(), response.getData());
                                    }
                                } else {
                                    ((UploadNavigator) getNavigator()).message(response.getMessage());
                                }
                            } else {
                                ((UploadNavigator) getNavigator()).message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            ((UploadNavigator) getNavigator()).message(R.string.default_error);
                            isLoading(false);

                            throwable.printStackTrace();
                        })
        );
    }

}
