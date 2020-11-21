package com.app.pixstory.ui.dashboard.you.activity.factory;


import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.app.pixstory.data.model.MemberData;
import com.app.pixstory.ui.dashboard.you.activity.datasource.SearchMemberDataSource;

public class SearchMemberFactory extends DataSource.Factory<Integer, MemberData> {

    private MutableLiveData<SearchMemberDataSource> liveData = new MutableLiveData<>();
    private int pageId;
    private String searchText, searchType,findInPage;

    public SearchMemberFactory(int pageId, String searchText, String searchType, String findInPage) {
        this.pageId = pageId;
        this.searchText = searchText;
        this.searchType = searchType;
        this.findInPage = findInPage;
    }

    @Override
    public DataSource<Integer, MemberData> create() {
        SearchMemberDataSource storiesDataSource = new SearchMemberDataSource(pageId, searchText, searchType, findInPage);
        liveData.postValue(storiesDataSource);
        return storiesDataSource;
    }

    public MutableLiveData<SearchMemberDataSource> getMutableLiveData() {
        return liveData;
    }

}