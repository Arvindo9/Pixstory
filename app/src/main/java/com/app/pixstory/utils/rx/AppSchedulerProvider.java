package com.app.pixstory.utils.rx;


import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 31-Oct-18
 */
public class AppSchedulerProvider implements SchedulerProvider {

    private static SchedulerProvider provider;

    private AppSchedulerProvider(){}

    public static SchedulerProvider getInstance(){
        if(provider == null){
            provider = new AppSchedulerProvider();
        }
        return provider;
    }

    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler single() {
        return Schedulers.single();
    }

    @Override
    public Scheduler trampoline() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler newThread() {
        return Schedulers.newThread();
    }
}
