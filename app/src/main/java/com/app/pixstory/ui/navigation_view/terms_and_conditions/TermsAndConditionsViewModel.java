package com.app.pixstory.ui.navigation_view.terms_and_conditions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.data.model.api.StaticPageResponse;

public class TermsAndConditionsViewModel extends BaseViewModel {

    private MutableLiveData<StaticPageResponse.Data> data = new MutableLiveData<>();

    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public void getPageData() {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .fetchStaticPages("terms-and-condition-page")
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    data.postValue(response.getData());
                                } else {
                                    data.postValue(null);
                                }
                            } else {
                                data.postValue(null);
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            data.postValue(null);
                            throwable.printStackTrace();
                        })
        );
    }

    public LiveData<StaticPageResponse.Data> getData() {
        return data;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void isLoading(boolean loading) {
        this.loading.postValue(loading);
    }
}