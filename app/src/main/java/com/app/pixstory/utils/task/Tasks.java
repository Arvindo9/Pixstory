package com.app.pixstory.utils.task;

import androidx.databinding.ObservableBoolean;

import com.app.pixstory.data.DataManager;
import com.app.pixstory.data.DataManagerService;
import com.app.pixstory.utils.rx.AppSchedulerProvider;
import com.app.pixstory.utils.rx.SchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Author : Arvindo Mondal
 * Created on 20-02-2020
 */
public class Tasks implements Task{
    private static Task task;

    private DataManager mDataManager;

    private final ObservableBoolean mIsLoading = new ObservableBoolean(false);

    private SchedulerProvider mSchedulerProvider;

    private CompositeDisposable mCompositeDisposable;


    private Tasks() {
        this.mDataManager = DataManagerService.getInstance();
        this.mSchedulerProvider = AppSchedulerProvider.getInstance();
        this.mCompositeDisposable = new CompositeDisposable();
    }

    public static Task getInstance(){
        if(task == null){
            task = new Tasks();
        }
        return task;
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

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }


}
