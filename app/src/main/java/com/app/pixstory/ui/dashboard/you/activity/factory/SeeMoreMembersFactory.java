package com.app.pixstory.ui.dashboard.you.activity.factory;


import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.app.pixstory.data.model.MemberData;
import com.app.pixstory.ui.dashboard.you.activity.datasource.SeeMoreMemberDataSource;

public class SeeMoreMembersFactory extends DataSource.Factory<Integer, MemberData> {

    private MutableLiveData<SeeMoreMemberDataSource> liveData = new MutableLiveData<>();
    private int pageId;

    public SeeMoreMembersFactory(int pageId) {
        this.pageId = pageId;
    }

    @Override
    public DataSource<Integer, MemberData> create() {
        SeeMoreMemberDataSource seeMoreMemberDataSource = new SeeMoreMemberDataSource(pageId);
        liveData.postValue(seeMoreMemberDataSource);
        return seeMoreMemberDataSource;
    }

    public MutableLiveData<SeeMoreMemberDataSource> getMutableLiveData() {
        return liveData;
    }

}