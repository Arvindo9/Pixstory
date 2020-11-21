package com.app.pixstory.ui.dashboard.you.activity.factory;


import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.app.pixstory.data.model.StoryData;
import com.app.pixstory.ui.dashboard.you.activity.datasource.SeeMoreStoriesDataSource;

public class SeeMoreStoriesFactory extends DataSource.Factory<Integer, StoryData> {

    private MutableLiveData<SeeMoreStoriesDataSource> liveData = new MutableLiveData<>();
    private int pageId;

    public SeeMoreStoriesFactory(int pageId) {
        this.pageId = pageId;
    }

    @Override
    public DataSource<Integer, StoryData> create() {
        SeeMoreStoriesDataSource interestDataSource = new SeeMoreStoriesDataSource(pageId);
        liveData.postValue(interestDataSource);
        return interestDataSource;
    }

    public MutableLiveData<SeeMoreStoriesDataSource> getMutableLiveData() {
        return liveData;
    }

}