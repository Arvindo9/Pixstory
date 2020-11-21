package com.app.pixstory.ui.dashboard.you.activity.factory;


import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.app.pixstory.data.model.StoryData;
import com.app.pixstory.ui.dashboard.you.activity.datasource.SearchStoriesDataSource;

public class SearchStoriesFactory extends DataSource.Factory<Integer, StoryData> {

    private MutableLiveData<SearchStoriesDataSource> liveData = new MutableLiveData<>();
    private int pageId;
    private String searchText, searchType,findInPage;

    public SearchStoriesFactory(int pageId, String searchText, String searchType, String findInPage) {
        this.pageId = pageId;
        this.searchText = searchText;
        this.searchType = searchType;
        this.findInPage = findInPage;
    }

    @Override
    public DataSource<Integer, StoryData> create() {
        SearchStoriesDataSource storiesDataSource = new SearchStoriesDataSource(pageId, searchText, searchType, findInPage);
        liveData.postValue(storiesDataSource);
        return storiesDataSource;
    }

    public MutableLiveData<SearchStoriesDataSource> getMutableLiveData() {
        return liveData;
    }

}