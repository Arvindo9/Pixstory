package com.app.pixstory.ui.intro;

import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.data.DataManager;

/**
 * Author : Arvindo Mondal
 * Created on 20-02-2020
 */
public class IntroViewModel extends BaseViewModel {
    public void setLoginMode() {
        getDataManager().setLoginMode(DataManager.LogInMode.LOGGED_IN_MODE_LOGGED_OUT.getType());
    }
}
