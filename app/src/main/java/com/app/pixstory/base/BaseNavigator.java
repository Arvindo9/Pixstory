package com.app.pixstory.base;

import androidx.annotation.StringRes;

/**
 * Author : Arvindo Mondal
 * Created on 14-03-2020
 */
public interface BaseNavigator {

    void message(@StringRes int message);

    void message(String message);
}
