package com.app.pixstory.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.pixstory.BuildConfig;
import com.app.pixstory.data.DataManager;

public final class Preferences implements PreferenceService {

    // for slider
    private static final String IS_FIRST_TIME_LAUNCH_SLIDER = "IsFirstTimeLaunchSlider";
    private static final String KEY_LOGIN_MODE = "KEY_LOGIN_MODE";
    private static final String KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN";
    private static final String KEY_STORY_TITLE = "KEY_STORY_TITLE";
    private static final String KEY_STORY_NARRATIVE = "KEY_STORY_NARRATIVE";
    private static final String KEY_STORY_FORMAT = "KEY_STORY_FORMAT";

    private static SharedPreferences pref;

    private Preferences() {
    }

    public static PreferenceService setup(Context context) {
        pref = context.getSharedPreferences(BuildConfig.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return new Preferences();
    }

    @Override
    public void setLoginMode(int data) {
        pref.edit().putInt(KEY_LOGIN_MODE, data).apply();
    }

    @Override
    public int getLoginMode() {
        return pref.getInt(KEY_LOGIN_MODE, DataManager.LogInMode.LOGGED_IN_MODE_FIRST_TIME.getType());
    }

    // manage slider
    @Override
    public void setFirstTimeLaunchSlider(boolean isFirstTime) {
        pref.edit().putBoolean(IS_FIRST_TIME_LAUNCH_SLIDER, isFirstTime).apply();
    }

    @Override
    public boolean isFirstTimeLaunchSlider() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH_SLIDER, true);
    }

    @Override
    public void setAccessToken(String token) {
        pref.edit().putString(KEY_ACCESS_TOKEN, token).apply();
    }

    @Override
    public String getAccessToken() {
        return pref.getString(KEY_ACCESS_TOKEN, "");
    }

    @Override
    public void setStoryTitle(String title) {
        pref.edit().putString(KEY_STORY_TITLE, title).apply();
    }

    @Override
    public String getStoryTitle() {
        return pref.getString(KEY_STORY_TITLE, "");
    }

    @Override
    public String getStoryNarrative() {
        return pref.getString(KEY_STORY_NARRATIVE, "");
    }

    @Override
    public void setStoryNarrative(String narrative) {
        pref.edit().putString(KEY_STORY_NARRATIVE, narrative).apply();
    }

    @Override
    public void setStoryFormat(String storyFormat) {
        pref.edit().putString(KEY_STORY_FORMAT, storyFormat).apply();
    }

    @Override
    public String getStoryFormat() {
        return pref.getString(KEY_STORY_FORMAT, "");
    }
}
