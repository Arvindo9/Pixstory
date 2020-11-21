package com.app.pixstory.ui.dashboard.upload.model;


import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.ui.dashboard.upload.navigator.CreatePageNavigator;
import com.app.pixstory.ui.dashboard.upload.navigator.UploadNavigator;

/**
 * Author       : Arvindo Mondal
 * Created date : 13-08-2019
 * Designation  : Programmer
 * Strength     : Never give up
 * Motto        : To be known as great Mathematician
 * Skills       : Algorithms and logic
 */
public class CreatePageViewModel extends BaseViewModel<CreatePageNavigator> {

    public String getToken() {
        return getDataManager().getAccessToken();
    }

    public void publishPage(String token,String type,String pageId, int path, int pageType, String addTitleStr, String pageLayoutStr, String publishOption, String pageStatus, int[] interest_id) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .createPage(token,type,pageId, path, pageType, addTitleStr, pageLayoutStr, publishOption, pageStatus, interest_id)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    if (response.getData() != null) {
                                        getNavigator().onPageResponse(response.getSuccess(), response.getData());
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
}
