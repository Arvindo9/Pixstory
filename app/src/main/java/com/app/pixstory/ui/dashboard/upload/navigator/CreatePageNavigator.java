package com.app.pixstory.ui.dashboard.upload.navigator;


import com.app.pixstory.base.BaseNavigator;
import com.app.pixstory.data.model.create_page.PageData;

/**
 * Author       : Arvindo Mondal
 * Created date : 13-08-2019
 * Email        : arvindomondal@gmail.com
 * Designation  : Programmer
 * Strength     : Never give up
 * Motto        : To be known as great Mathematician
 * Skills       : Algorithms and logic
 */
public interface CreatePageNavigator extends BaseNavigator {
    void onPageResponse(Boolean success, PageData data);
}
