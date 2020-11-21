package com.app.pixstory.data.local.prefs;

/**
 * Author : Arvindo Mondal
 * Created on 20-02-2020
 */
public interface PreferenceService {

    void setLoginMode(int data);

    int getLoginMode();

    void setFirstTimeLaunchSlider(boolean isFirstTime);

    boolean isFirstTimeLaunchSlider();

    void setAccessToken(String token);

    String getAccessToken();

    void setStoryTitle(String  title);

    String getStoryTitle();

    String getStoryNarrative();

    void setStoryNarrative(String narrative);

    void setStoryFormat(String storyFormat);

    String getStoryFormat();
}
