package com.app.pixstory.ui.dashboard.you.activity;

import com.app.pixstory.base.BaseNavigator;

public interface ResultNavigator extends BaseNavigator {
    void followStatusChanged(String message, int position, int follow);

    void storyShared(String message);
}
