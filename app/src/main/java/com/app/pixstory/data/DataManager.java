package com.app.pixstory.data;

import com.app.pixstory.data.local.db.DatabaseService;
import com.app.pixstory.data.local.prefs.PreferenceService;
import com.app.pixstory.data.remote.APIService;

/**
 * Author : Arvindo Mondal
 * Created on 20-02-2020
 */
public interface DataManager extends PreferenceService, DatabaseService, APIService {

    enum LogInMode {
        LOGGED_IN_MODE_FIRST_TIME(0),
        LOGGED_IN_MODE_LOGGED_OUT(1),                       //login screen
        LOGGED_IN_MODE_LOGGED_IN_MOBILE(2),
        LOGGED_IN_MODE_LOGGED_IN_EMAIL(3),
        LOGGED_IN_MODE_LOGGED_IN_GOOGLE(4),
        LOGGED_IN_MODE_LOGGED_IN_LINKED_IN(5),
        LOGGED_IN_MODE_LOGGED_IN_FACEBOOK(6),
        LOGGED_IN_MODE_LOGGED_IN_USER_PASS(7),
        LOGGED_IN_MODE_LOGGED_IN_GUEST(8);

        private final int mType;

        LogInMode(int type) {
            mType = type;
        }

        public int getType() {
            return mType;
        }
    }
}
