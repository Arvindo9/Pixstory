package com.app.pixstory.ui.messages.message;

import com.app.pixstory.base.BaseNavigator;

/**
 * Author       : Arvindo Mondal
 * Created on   : 05-09-2019
 */
interface MessageNavigator extends BaseNavigator {
    void showProgress(boolean status, int type);
}
