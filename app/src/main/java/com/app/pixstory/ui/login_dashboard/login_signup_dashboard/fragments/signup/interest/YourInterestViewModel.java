package com.app.pixstory.ui.login_dashboard.login_signup_dashboard.fragments.signup.interest;

import androidx.lifecycle.MutableLiveData;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.base.views.BaseModelView;
import com.app.pixstory.data.DataManager;
import com.app.pixstory.data.model.interest.Interest;
import com.app.pixstory.utils.Constants;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YourInterestViewModel extends BaseViewModel<InterestNavigator> {

    private final MutableLiveData<List<BaseModelView>> modelLiveData;

    public YourInterestViewModel(){
        modelLiveData = new MutableLiveData<>();
    }

    //List view---------------------------------------

    MutableLiveData<List<BaseModelView>> getLiveData() {
        return modelLiveData;
    }

    //mobile otp verification
    void getInterest(){
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .getInterestList(getDataManager().getAccessToken())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            if(response != null){
                                isLoading(false);
                                if(response.getSuccess()) {
                                    if (response.getInterest() != null && !response.getInterest().isEmpty()) {
                                        ArrayList<BaseModelView> list = new ArrayList<>();
                                        for (Interest data: response.getInterest()) {
                                            list.add(new BaseModelView(data.getId(), data.getTitle()));
                                        }
                                        modelLiveData.setValue(list);
                                    }
                                }
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            if(throwable instanceof UnknownHostException) {
                                getNavigator().message(R.string.default_network_error);
                            }
                            else{
                                getNavigator().message(R.string.default_error);
                            }
                            throwable.printStackTrace();
                        })
        );
    }

    public void setPreferences(String loginType, boolean yourProfileActivity, int yourProfileActivityType, boolean myProfileActivity,
                               boolean allowMessage, int allowMessageType, boolean helpOther, HashMap<Integer, Integer> interests) {
        isLoading(true);
        String token = "Bearer" + getDataManager().getAccessToken();
        int[] interest = new int[interests.size()];

        int i = 0;
        for (int key : interests.keySet()) {
            interest[i++] = key;
        }
        getCompositeDisposable().add(getDataManager()
                .setPreferences(token, yourProfileActivity? 1:0, yourProfileActivityType, myProfileActivity? 1:0,
                        allowMessage? 1:0, allowMessageType, helpOther? 1:0, interest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            if(response != null){
                                isLoading(false);
                                if(response.getSuccess()) {
                                    //TODO on success Signup

                                    if(loginType.equals(Constants.LOGIN_TYPE_MOBILE_NUMBER)){
                                        getDataManager().setLoginMode(DataManager.LogInMode.LOGGED_IN_MODE_LOGGED_IN_MOBILE.getType());
                                    }
                                    else if(loginType.equals(Constants.LOGIN_TYPE_USER)){
                                        getDataManager().setLoginMode(DataManager.LogInMode.LOGGED_IN_MODE_LOGGED_IN_USER_PASS.getType());
                                    }
                                    else if(loginType.equals(Constants.LOGIN_TYPE_EMAIL_ID)){
                                        getDataManager().setLoginMode(DataManager.LogInMode.LOGGED_IN_MODE_LOGGED_IN_EMAIL.getType());
                                    }
                                    else if(loginType.equals(Constants.LOGIN_TYPE_FACEBOOK)){
                                        getDataManager().setLoginMode(DataManager.LogInMode.LOGGED_IN_MODE_LOGGED_IN_FACEBOOK.getType());
                                    }
                                    else if(loginType.equals(Constants.LOGIN_TYPE_GOOGLE)){
                                        getDataManager().setLoginMode(DataManager.LogInMode.LOGGED_IN_MODE_LOGGED_IN_GOOGLE.getType());
                                    }
                                    else if(loginType.equals(Constants.LOGIN_TYPE_LINKEDIN)){
                                        getDataManager().setLoginMode(DataManager.LogInMode.LOGGED_IN_MODE_LOGGED_IN_LINKED_IN.getType());
                                    }
                                    getNavigator().onSuccess();
                                }
                                else{
                                    getNavigator().message(response.getMessage());
                                }
                            }
                            else{
                                getNavigator().message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            if(throwable instanceof UnknownHostException) {
                                getNavigator().message(R.string.default_network_error);
                            }
                            else{
                                getNavigator().message(R.string.default_error);
                            }
                            throwable.printStackTrace();
                        })
        );
    }
}
