package com.app.pixstory.ui.dashboard.others.factory;


import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.app.pixstory.data.model.api.UserHomeListResponse;
import com.app.pixstory.ui.dashboard.others.datasource.NearbyDataSource;

public class NearbyDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<NearbyDataSource> liveData = new MutableLiveData<>() ;

    @Override
    public DataSource<Integer, UserHomeListResponse.Data> create() {
        NearbyDataSource nearbyDataSource = new NearbyDataSource();
        liveData.postValue(nearbyDataSource);
        return nearbyDataSource;
    }
    public MutableLiveData<NearbyDataSource> getMutableLiveData() {
        return liveData;
    }
}