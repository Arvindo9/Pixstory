package com.app.pixstory.base;

import android.app.Application;
import android.content.Context;

import androidx.core.provider.FontRequest;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;

import com.app.pixstory.data.DataManagerService;
import com.app.pixstory.data.local.db.Database;
import com.app.pixstory.data.local.prefs.PreferenceService;
import com.app.pixstory.data.local.prefs.Preferences;
import com.app.pixstory.data.remote.APIs;
import com.facebook.FacebookSdk;
import com.facebook.stetho.Stetho;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

public class BaseApplication extends Application {

    private static BaseApplication context;
    private static final int CERTIFICATES = 120;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        setupRepository();

        setUpFacebook();
        setUpStetho();
        setupEmoji();
    }

    private void setupRepository() {
        DataManagerService.setup(Preferences.setup(this), APIs.setup(this), Database.setup(this));
    }

    private void setUpStetho(){
        Stetho.initializeWithDefaults(this);
    }

    private void setUpFacebook() {
        FacebookSdk.sdkInitialize(this.getApplicationContext());
    }

    @Override
    public void onLowMemory() {
        Runtime.getRuntime().gc();
        super.onLowMemory();
    }


    private void setupEmoji() {
        FontRequest fontRequest = new FontRequest(
                "com.example.fontprovider",
                "com.example",
                "emoji compat Font Query",
                CERTIFICATES);
        EmojiCompat.Config config = new FontRequestEmojiCompatConfig(this, fontRequest);
        EmojiCompat.init(config);

        EmojiManager.install(new GoogleEmojiProvider());
    }

}
