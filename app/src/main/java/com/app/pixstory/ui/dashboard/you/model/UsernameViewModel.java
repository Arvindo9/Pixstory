package com.app.pixstory.ui.dashboard.you.model;

import androidx.lifecycle.MutableLiveData;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.base.views.BaseModelView;
import com.app.pixstory.data.model.interest.Interest;
import com.app.pixstory.data.model.user_count.UserCountData;
import com.app.pixstory.data.model.user_count.UserCountResponse;
import com.app.pixstory.ui.dashboard.you.navigator.UserCountNavigator;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static com.app.pixstory.utils.Constants.BEARER;

/**
 * Created by Kamlesh Yadav on 01-05-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class UsernameViewModel extends BaseViewModel<UserCountNavigator> {

    private final MutableLiveData<UserCountData> countLiveData;

    public UsernameViewModel(){
        countLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<UserCountData> getCountData(){
        return countLiveData;
    }

    String token = BEARER + getDataManager().getAccessToken();

    public void getCount() {
        getCompositeDisposable().add(getDataManager()
                .getFriendFollowFollowingList(token)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            if (response != null) {
                                if (response.getSuccess()) {
                                    if (response.getData() != null) {
                                        countLiveData.setValue(response.getData());
                                    }
                                } else {
                                    getNavigator().message(response.getMessage());
                                }
                            }
                        },
                        throwable -> {

                            if (throwable instanceof UnknownHostException) {
                             //   getNavigator().message(R.string.default_network_error);
                            } else {
                              //  getNavigator().message(R.string.default_error);
                            }
                            throwable.printStackTrace();
                        })
        );
    }
}
