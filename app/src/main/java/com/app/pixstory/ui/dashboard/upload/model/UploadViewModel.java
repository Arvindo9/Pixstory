package com.app.pixstory.ui.dashboard.upload.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.data.model.api.AccountDetailResponse;
import com.app.pixstory.ui.dashboard.upload.navigator.UploadNavigator;
import com.app.pixstory.utils.util.Logger;

import java.net.UnknownHostException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.app.pixstory.utils.Constants.BEARER;

public class UploadViewModel extends BaseViewModel {
    public String getToken() {
        return getDataManager().getAccessToken();
    }
    String token = BEARER + getDataManager().getAccessToken();

    public String getStoryTitle(){
        return getDataManager().getStoryTitle();
    }

    public String getStoryNarrative(){
        return getDataManager().getStoryNarrative();
    }

    public void yourPhotos(String userType, String type, int userId, int page, int limit) {
        isLoading(true);
        isSwipeRefresh(true);
        getCompositeDisposable().add(getDataManager()
                .getUserPhoto(token, userType, type, userId, page, limit)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            isSwipeRefresh(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    if (response.getData() != null) {
                                        ((UploadNavigator) getNavigator()).onMyPhotoResponse(response.getSuccess(), response.getData(), type);
                                    }
                                } else {
                                    ((UploadNavigator) getNavigator()).message(response.getMessage());
                                }
                            } else {
                                ((UploadNavigator) getNavigator()).message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            throwable.printStackTrace();
                        })
        );

    }

    public void setIsPref(String title, String narrative) {
        getDataManager().setStoryTitle(title);
        getDataManager().setStoryNarrative(narrative);
    }

    public void onStart() {
        getDataManager().setStoryTitle("");
        getDataManager().setStoryNarrative("");
    }

    public void setStoryFormat(String storyFormat) {
        getDataManager().setStoryFormat(storyFormat);
    }

    public void storyFormat() {
        getDataManager().setStoryFormat("");
    }

    public void publishStory(int[] imageIdList, String storyFormat, String publishOption, int[] interest_id, String isType, int parentStoryId) {
        isLoading(true);
        String story_title = getDataManager().getStoryTitle();
        String story_narrative = getDataManager().getStoryNarrative();
        getCompositeDisposable().add(getDataManager()
                .createStory(token, imageIdList, storyFormat, story_title, story_narrative, publishOption, interest_id, isType, parentStoryId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    if (response.getData() != null) {
                                        ((UploadNavigator) getNavigator()).onStoryResponse(response.getSuccess(), response.getData());
                                    }
                                } else {
                                    ((UploadNavigator) getNavigator()).message(response.getMessage());
                                }
                            } else {
                                ((UploadNavigator) getNavigator()).message(R.string.default_error);
                            }
                        },
                        throwable -> {
                            ((UploadNavigator) getNavigator()).message(R.string.default_error);
                            isLoading(false);
                            throwable.printStackTrace();
                        })
        );
    }

    public void uploadFileToServer(MediaModel model) {
        String token = "Bearer " + getDataManager().getAccessToken();
        String unique_key = String.valueOf(System.currentTimeMillis());

        if(model == null || model.getFile()==null || !model.getFile().exists()){
            Logger.e("File not found");
            return;
        }

        RequestBody uniqueKey = RequestBody.create(MediaType.parse("multipart/form-data"), unique_key);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), model.getFile());
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", model.getFileName(), requestFile);

        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .uploadSingleImage(token, uniqueKey, body)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    isLoading(false);
                    if (response != null && response.getSuccess()) {
                        Logger.e("File res: " + response);
                        if(response.getUploadModel() != null) {
                            if (getNavigator() instanceof UploadNavigator) {
                                ((UploadNavigator) getNavigator()).message("File upload successfully");
                                ((UploadNavigator) getNavigator()).onFileUploadSuccess(model, response.getUploadModel());
                            }
                        }
                    }
                    isLoading(false);
                }, throwable -> {
                    isLoading(false);
                    throwable.printStackTrace();
                }));
    }

    public void getInterest(String token) {
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
                                        ((UploadNavigator)getNavigator()).onMasterInterest(response.getSuccess(), response.getInterest());
                                    }
                                }
                            }
                        },
                        throwable -> {
                            isLoading(false);
                            throwable.printStackTrace();
                        })
        );
    }


    // user profile details
    public MutableLiveData<AccountDetailResponse.User> userLiveData = new MutableLiveData<>();

    public LiveData<AccountDetailResponse.User> getUserLiveData() {
        return userLiveData;
    }

    // data and badge
    public MutableLiveData<AccountDetailResponse.Data> userLiveDatas = new MutableLiveData<>();

    public LiveData<AccountDetailResponse.Data> getUserLiveDatas() {
        return userLiveDatas;
    }

    public void getProfileData() {
        //isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .getAccountDetailData(token, 0)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            //isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    userLiveData.setValue(response.getData().getUser());
                                    userLiveDatas.setValue(response.getData());
                                    userLiveDatas.setValue(response.getData());
                                } else {
                                    ((UploadNavigator) getNavigator()).message(response.getMessage());
                                }
                            } else {
                                ((UploadNavigator) getNavigator()).message(response.getMessage());
                            }
                        },
                        throwable -> {
                            //isLoading(false);
                            if (throwable instanceof UnknownHostException) {
                                ((UploadNavigator) getNavigator()).message(R.string.default_network_error);
                            } else {
                                ((UploadNavigator) getNavigator()).message(R.string.default_error);
                            }
                            throwable.printStackTrace();
                        })
        );
    }
}

