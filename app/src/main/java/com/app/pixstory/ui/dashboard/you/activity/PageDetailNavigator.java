package com.app.pixstory.ui.dashboard.you.activity;

import com.app.pixstory.base.BaseNavigator;
import com.app.pixstory.data.model.PageDetailResponse;

import java.util.List;

public interface PageDetailNavigator extends BaseNavigator {

    void pageDetailFetched(PageDetailResponse.Data pageData);

    void pageFollowed(String message, int isFollow);

    void pageDataFetched(PageDetailResponse.Data pageData, String type);
}
