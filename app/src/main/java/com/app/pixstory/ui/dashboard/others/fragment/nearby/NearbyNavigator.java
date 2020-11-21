package com.app.pixstory.ui.dashboard.others.fragment.nearby;

import com.app.pixstory.base.BaseNavigator;

public interface NearbyNavigator extends BaseNavigator {

    void followStatusChanged(String message, int position, int follow);

}
