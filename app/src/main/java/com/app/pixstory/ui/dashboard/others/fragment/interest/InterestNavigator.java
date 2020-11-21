package com.app.pixstory.ui.dashboard.others.fragment.interest;

import com.app.pixstory.base.BaseNavigator;

public interface InterestNavigator extends BaseNavigator {

    void followStatusChanged(String message, int position, int follow);
}
