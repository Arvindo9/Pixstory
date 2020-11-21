package com.app.pixstory.ui.navigation_view.faqs;

import androidx.lifecycle.SavedStateHandle;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;

import java.net.UnknownHostException;

public class FAQsViewModel extends BaseViewModel<FAQNavigator> {


    public void getFAQList(String question) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .getFAQList("Bearer" + getDataManager().getAccessToken(), question)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().faqFetched(response.getData());
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