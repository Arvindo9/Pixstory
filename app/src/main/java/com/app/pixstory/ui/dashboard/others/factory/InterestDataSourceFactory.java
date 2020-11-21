package com.app.pixstory.ui.dashboard.others.factory;


import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.app.pixstory.data.model.api.UserHomeListResponse;
import com.app.pixstory.ui.dashboard.others.datasource.InterestDataSource;

public class InterestDataSourceFactory extends DataSource.Factory<Integer,UserHomeListResponse.Data>{

    private MutableLiveData<InterestDataSource> liveData = new MutableLiveData<>() ;

    @Override
    public DataSource<Integer, UserHomeListResponse.Data> create() {
        InterestDataSource interestDataSource = new InterestDataSource();
        liveData.postValue(interestDataSource);
        return interestDataSource;
    }

    public MutableLiveData<InterestDataSource> getMutableLiveData() {
        return liveData;
    }

}