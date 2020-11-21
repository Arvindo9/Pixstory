package com.app.pixstory.base;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.pixstory.data.DataManager;
import com.app.pixstory.data.DataManagerService;
import com.app.pixstory.utils.rx.AppSchedulerProvider;
import com.app.pixstory.utils.rx.SchedulerProvider;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Author : Arvindo Mondal
 * Created on 20-02-2020
 */
public abstract class BaseViewModel<N> extends ViewModel {

    private DataManager mDataManager;
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSwipeRefresh = new MutableLiveData<>();

    private final ObservableBoolean mIsLoading = new ObservableBoolean(false);

    private SchedulerProvider mSchedulerProvider;

    private CompositeDisposable mCompositeDisposable;

    private WeakReference<N> mNavigator;


    protected BaseViewModel() {
        this.mDataManager = DataManagerService.getInstance();
        this.mSchedulerProvider = AppSchedulerProvider.getInstance();
        this.mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onCleared() {
        mCompositeDisposable.dispose();
        super.onCleared();
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public ObservableBoolean getIsLoading() {
        return mIsLoading;
    }

    public void setIsLoading(boolean isLoading) {
        mIsLoading.set(isLoading);
    }

    public N getNavigator() {
        return mNavigator.get();
    }

    public void setNavigator(N navigator) {
        this.mNavigator = new WeakReference<>(navigator);
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }

    public LiveData<Boolean> getLoading()
    {
        return loading;
    }

    public LiveData<Boolean> getRefreshing()
    {
        return isSwipeRefresh;
    }



    public void isLoading(boolean loading)
    {
        this.loading.postValue(loading);
    }

    public void isSwipeRefresh(boolean loading)
    {
        this.isSwipeRefresh.postValue(loading);
    }

}
