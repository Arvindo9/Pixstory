package com.app.pixstory.ui.navigation_view.account;

import com.app.pixstory.base.BaseNavigator;
import com.app.pixstory.data.model.api.UserPrefrenceResponse;

public interface AccountNavigator extends BaseNavigator {

    void getPreference(UserPrefrenceResponse.Data data);

    void prefrenceUpdated();
}
