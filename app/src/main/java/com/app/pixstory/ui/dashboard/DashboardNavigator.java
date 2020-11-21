package com.app.pixstory.ui.dashboard;

import com.app.pixstory.base.BaseNavigator;
import com.app.pixstory.data.model.access.Data;

interface DashboardNavigator extends BaseNavigator {
    void onTokenResponse(Boolean success, Data data);

}
