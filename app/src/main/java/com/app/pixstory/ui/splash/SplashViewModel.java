package com.app.pixstory.ui.splash;

import android.os.Handler;

import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.data.DataManager;

/**
 * Created by     : Kamlesh Yadav
 * Created on     : 1/8/2020.
 * Company        :Eighteen Pixels India Private Limited
 * Email          :kamlesh@18pixels.com
 */
public class SplashViewModel extends BaseViewModel<SplashNavigator> {
    private static final long SPLASH_TIME_OUT = 3000;
    private boolean isAppReady;
    private boolean isWaitComplete;
    private int taskId;

    void start(){
        isAppReady = false;
        isWaitComplete = false;

        waitForThreadComplete();

        decideNextScreen();
    }

    private void decideNextScreen(){
        taskId = getDataManager().getLoginMode();

        isAppReady = true;
        onNextScreenDecided();
    }

    private void onNextScreenDecided(){
        if(isWaitComplete && isAppReady) {
            DataManager.LogInMode data = DataManager.LogInMode.values()[taskId];
            switch (data) {
                case LOGGED_IN_MODE_FIRST_TIME:
                    getNavigator().openWelcomeScreen();
                    break;
                case LOGGED_IN_MODE_LOGGED_OUT:
                    getNavigator().openLoginScreen();
                    break;
                case LOGGED_IN_MODE_LOGGED_IN_MOBILE:
                case LOGGED_IN_MODE_LOGGED_IN_EMAIL:
                case LOGGED_IN_MODE_LOGGED_IN_GOOGLE:
                case LOGGED_IN_MODE_LOGGED_IN_LINKED_IN:
                case LOGGED_IN_MODE_LOGGED_IN_FACEBOOK:
                    getNavigator().openDashboardScreen();
                    break;
                case LOGGED_IN_MODE_LOGGED_IN_GUEST:
                    getNavigator().openDashboardScreen();
                    break;
            }
        }
    }

    private void waitForThreadComplete(){
        new Handler().postDelayed(() -> {
            isWaitComplete = true;
            onNextScreenDecided();
        }, SPLASH_TIME_OUT);
    }

}
