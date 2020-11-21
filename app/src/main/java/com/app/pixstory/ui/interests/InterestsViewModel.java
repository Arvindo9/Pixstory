package com.app.pixstory.ui.interests;

import androidx.lifecycle.MutableLiveData;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.base.views.BaseModelView;
import com.app.pixstory.data.model.add_user_interest.AddUserInterestData;
import com.app.pixstory.data.model.db.messages.MessageUsersNew;
import com.app.pixstory.data.model.global_search.GlobalInterestData;
import com.app.pixstory.data.model.global_search.GlobalInterestResponse;
import com.app.pixstory.data.model.interest.Interest;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static com.app.pixstory.utils.Constants.BEARER;

public class InterestsViewModel extends BaseViewModel<InterestNavigator> {

    String token = BEARER + getDataManager().getAccessToken();

    public InterestsViewModel() {
        modelLiveData = new MutableLiveData<>();
        newUsersLiveData = new MutableLiveData<>();
    }

    private final MutableLiveData<List<BaseModelView>> modelLiveData;
  //  private MutableLiveData<List<BaseModelView>> addUserInterestData;
    private final MutableLiveData<List<GlobalInterestData>> newUsersLiveData;

    //List view---------------------------------------

    MutableLiveData<List<BaseModelView>> getLiveData() {
        return modelLiveData;
    }

   /* MutableLiveData<List<BaseModelView>> getAddUserInterestData() {
        return addUserInterestData;
    }*/

    public MutableLiveData<List<GlobalInterestData>> getGlobalInterestLiveData() {
        return newUsersLiveData;
    }


    public void getInterest() {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .getMaterDetail(token)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            if (response != null) {
                                isLoading(false);
                                if (response.getSuccess()) {
                                    if (response.getInterest() != null && !response.getInterest().isEmpty()) {
                                        ArrayList<BaseModelView> list = new ArrayList<>();
                                        for (Interest data : response.getInterest()) {
                                            list.add(new BaseModelView(data.getId(), data.getTitle()));
                                        }
                                        modelLiveData.setValue(list);
                                    }
                                } else {
                                    getNavigator().message(response.getMessage());
                                }
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            if (throwable instanceof UnknownHostException) {
                                getNavigator().message(R.string.default_network_error);
                            } else {
                                getNavigator().message(R.string.default_error);
                            }
                            throwable.printStackTrace();
                        })
        );
    }

    public void addUserInterest(String title) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .addUserInterest(token, title)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            if (response != null) {
                                isLoading(false);
                                if (response.getSuccess()) {
                                    if (response.getData() != null) {
                                      //  addUserInterestData = new MutableLiveData<>();
                                     //   ArrayList<AddUserInterestData> list = new ArrayList<>();
                                    //    list.add(new AddUserInterestData(response.getData().getId(), response.getData().getTitle()));
                                      //  addUserInterestData.setValue(list);
                                        getNavigator().onAddUserInterestResponse(response.getSuccess(), response.getData());
                                    }
                                } else {
                                    getNavigator().message(response.getMessage());
                                }
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            if (throwable instanceof UnknownHostException) {
                                getNavigator().message(R.string.default_network_error);
                            } else {
                                getNavigator().message(R.string.default_error);
                            }
                            throwable.printStackTrace();
                        })
        );
    }

    public void linkUserInterest(int interest_id, int[] category_id) {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .linkUserInterest(token, interest_id, category_id)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            if (response != null) {
                                isLoading(false);
                                if (response.getSuccess()) {
                                    if (response.getData() != null) {
                                        getNavigator().onLinkInterestResponse(response.getSuccess(), response.getData());
                                    }
                                } else {
                                    getNavigator().message(response.getMessage());
                                }
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            if (throwable instanceof UnknownHostException) {
                                getNavigator().message(R.string.default_network_error);
                            } else {
                                getNavigator().message(R.string.default_error);
                            }
                            throwable.printStackTrace();
                        })
        );
    }


    //Message User list--------------------------
    //New message user api
    public void getGlobalInterestList(String search) {
        getNavigator().showProgress(true);
        getCompositeDisposable().add(getDataManager()
                .getInterestGlobalSearch(token, search)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null) {
                        if (response.getSuccess() && response.getData() != null) {
                            newUsersLiveData.setValue(response.getData());
                        } else {
                            getNavigator().message(response.getMessage());
                        }
                    }
                    getNavigator().showProgress(false);
                }, throwable -> {
                    getNavigator().showProgress(false);
                    throwable.printStackTrace();
                    if (throwable instanceof UnknownHostException) {
                        getNavigator().message(R.string.default_network_error);
                    } else {
                        getNavigator().message(R.string.default_error);
                    }
                }));
    }

}
