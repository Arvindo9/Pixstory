package com.app.pixstory.ui.messages.message;

import android.util.Log;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.lifecycle.MutableLiveData;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.data.model.db.messages.MessageUsers;
import com.app.pixstory.data.model.db.messages.MessageUsersNew;
import com.app.pixstory.data.model.db.messages.Messages;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.util.Logger;
import com.google.firebase.iid.FirebaseInstanceId;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Author       : Arvindo Mondal
 * Created on   : 05-09-2019
 */
public class MessageViewModel extends BaseViewModel<MessageNavigator> {
    public final ObservableField<String> profilePic;
    public final ObservableField<String> name;
    public final ObservableField<String> subject;
    public final ObservableField<String> activeTime;

    public final ObservableList<Messages> modelObservableList = new ObservableArrayList<>();
    private final MutableLiveData<List<Messages>> modelLiveData;

    public final ObservableList<MessageUsers> modelBulletinObservableList = new ObservableArrayList<>();
    private final MutableLiveData<List<MessageUsers>> modelBulletinLiveData;

    public MessageViewModel() {
        this.modelLiveData = new MutableLiveData<>();
        this.modelBulletinLiveData = new MutableLiveData<>();
        this.profilePic = new ObservableField<>();
        this.name = new ObservableField<>();
        this.subject = new ObservableField<>();
        this.activeTime = new ObservableField<>();
    }

    public <MU> void setData(MU model) {
        String time = "";
    }

    //List view---------------------------------------

    private ObservableList<Messages> getObservableList() {
        return modelObservableList;
    }

    void addDataToList(List<Messages> list) {
        modelObservableList.clear();
        modelObservableList.addAll(list);
    }

    MutableLiveData<List<Messages>> getLiveData() {
        return modelLiveData;
    }

    private void setLiveData(List<Messages> list) {
        modelLiveData.setValue(list);
    }

    //----------------
    void addDataToListBulletin(List<MessageUsers> list) {
        modelBulletinObservableList.clear();
        modelBulletinObservableList.addAll(list);
    }

    MutableLiveData<List<MessageUsers>> getBulletinLiveData() {
        return modelBulletinLiveData;
    }

    private void setBulletinLiveData(List<Messages> list) {
        modelLiveData.setValue(list);
    }

    //Additional--------------------------------------

    void sendMessageAudio(int userId, File file, String filePath) {
//        updateMessages(new Messages(userId, file, filePath));
        sendMessageFile(userId, file, filePath);
    }

    private void updateMessages(Messages model) {
        List<Messages> messagesList = new ArrayList<>(modelObservableList);
        messagesList.add(model);
        setLiveData(messagesList);
    }

    private void updateMessagesBulletin(List<MessageUsers> model) {
        List<MessageUsers> messagesList = new ArrayList<>(modelBulletinObservableList);
        messagesList.addAll(model);
        modelBulletinLiveData.setValue(messagesList);
    }

    private void updateMessagesPush(List<Messages> list) {
        if(!modelObservableList.equals(list)) {
            setLiveData(list);
        }
    }

    void updateMessagesPush(Messages model) {
        modelObservableList.add(model);
    }


    //messages---------------------------------

    //fcm

    void fcmToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
            .addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e("DashboardViewModel", "getInstanceId failed", task.getException());
                    return;
                }

                if(task.getResult() != null) {
                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    // Log and toast
                    Log.e("DashboardViewModel", token);

                  //  pushFcmToken(token);
                }
            });
    }

   /* private void pushFcmToken(String token) {
        getCompositeDisposable().add(getDataManager()
                .pushFcmToken("token", token)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().newThread())
                .subscribe(response -> {
                    if (response != null && response.getSuccess()) {
//                        getDataManager().setFcmToken(false);
                    }
                    else {
//                        getDataManager().setFcmToken(true);
                    }
                }, throwable -> {
                    throwable.printStackTrace();
//                    getDataManager().setFcmToken(true);
                }));
    }*/

    @NotNull
    public static JSONObject paramsPushFcmToken(String token)
            throws JSONException, ArrayIndexOutOfBoundsException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        return jsonObject;
    }

    //Apis-------------------------------

    //Message user DB
    public void getMessageUsersDb(String userId) {
        getCompositeDisposable().add(getDataManager()
                .getMessages(userId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null && !response.isEmpty()) {
                        modelLiveData.setValue(response);
                    }
                    getMessagesApis(userId);
                }, throwable -> getMessagesApis(userId)));
    }

    //Message user APIS
    public void getMessagesApis(String userId) {
        int page = 1;
        String token = "Bearer " + getDataManager().getAccessToken();
        Logger.e("Token:" + token);
        getCompositeDisposable().add(getDataManager()
                .getMessages(token, userId, page, Constants.DEFAULT_PAGE_LIMIT_MESSAGE)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null && response.getData()!=null && response.getData().getMessageUser() != null &&
                            response.getData().getMessageUser().getMessages()!= null){
                        List<Messages> list = response.getData().getMessageUser().getMessages();
                        Collections.reverse(list);
                        modelLiveData.setValue(list);
//                        saveMessages(list);
                    }
                }, Throwable::printStackTrace));
    }

    private void saveMessages(List<Messages> list){
        getCompositeDisposable().add(getDataManager()
                .saveMessages(list)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().newThread())
                .subscribe(response -> {
                    if (response != null) {

                    }
                }, Throwable::printStackTrace));
    }

    private void saveSendMessageUser(List<MessageUsers> messageUsers){
        getCompositeDisposable().add(getDataManager()
                .saveMessageUsers(messageUsers)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().newThread())
                .subscribe(response -> {
                    if (response != null) {

                    }
                }, Throwable::printStackTrace));
    }

    //Send message------------------------

    public void sendMessage(@NotNull String userId, String message) {
        String token = "Bearer " + getDataManager().getAccessToken();
        getCompositeDisposable().add(getDataManager()
                .sendMessage(token, userId, message)
                .map(response -> {
                    if (response != null) {
                        if(response.getSuccess() && response.getData()!= null && response.getData().getMessageUsers()!=null) {
                            for(int i = 0; i<response.getData().getMessageUsers().size(); i++){
                                if(response.getData().getMessageUsers().get(i).getMessage()!=null){
                                    response.getData().getMessageUsers().get(i).setMessageTime(
                                            response.getData().getMessageUsers().get(i).getMessage().getCreatedAt()
                                    );
                                }
                            }
                        }
                    }
                    return response;
                })
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null) {
                        if(response.getSuccess() && response.getData()!= null && response.getData().getMessageUsers()!=null &&
                                !response.getData().getMessageUsers().isEmpty()) {
                            updateMessages(response.getData().getMessageUsers().get(0).getMessage());
//                            saveSendMessageUser(response.getData().getMessageUsers());
                        }
                        else{
                            getNavigator().message(response.getMessage());
                        }
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    if(throwable instanceof UnknownHostException) {
                        getNavigator().message(R.string.default_network_error);
                    }
                    else{
                        getNavigator().message(R.string.default_error);
                    }
                }));
    }

    public void sendMessageFile(int userId, File file, @NotNull String filePath) {
        String token = "Bearer " + getDataManager().getAccessToken();
        getNavigator().showProgress(true, 0);

        RequestBody userID = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(userId));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("audio", file.getName(), requestFile);
        getCompositeDisposable().add(getDataManager()
                .sendMessageFile(token,userID, body)
                .map(response -> {
                    if (response != null) {
                        if(response.getSuccess() && response.getData()!= null && response.getData().getMessageUsers()!=null) {
                            for(int i = 0; i<response.getData().getMessageUsers().size(); i++){
                                if(response.getData().getMessageUsers().get(i).getMessage()!=null){
                                    response.getData().getMessageUsers().get(i).setMessageTime(
                                            response.getData().getMessageUsers().get(i).getMessage().getCreatedAt()
                                    );
                                }
                            }
                        }
                    }
                    return response;
                })
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null) {
                        if(response.getSuccess() && response.getData()!= null && response.getData().getMessageUsers()!=null &&
                                !response.getData().getMessageUsers().isEmpty()) {
                            updateMessages(response.getData().getMessageUsers().get(0).getMessage());
//                            saveSendMessageUser(response.getData().getMessageUsers());
                        }
                        else{
                            getNavigator().message(response.getMessage());
                        }
                    }
                    getNavigator().showProgress(false, 0);
                }, throwable -> {
                    getNavigator().showProgress(false, 0);
                    throwable.printStackTrace();
                    if(throwable instanceof UnknownHostException) {
                        getNavigator().message(R.string.default_network_error);
                    }
                    else{
                        getNavigator().message(R.string.default_error);
                    }
                }));
    }

    //Bulletin-----------------------------

    //Message user APIS Bulletin
    public void getMessagesBulletinApis(String bulletinId) {
        int page = 1;
        String token = "Bearer " + getDataManager().getAccessToken();
        Logger.e("Token:" + token);
        getCompositeDisposable().add(getDataManager()
                .getMessagesBulletin(token, bulletinId, page, Constants.DEFAULT_PAGE_LIMIT_MESSAGE)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null && response.getData()!=null && response.getData().getMessageUsers() != null
                            && !response.getData().getMessageUsers().isEmpty()){
                        List<MessageUsers> list = response.getData().getMessageUsers();
                        Collections.reverse(list);
                        modelBulletinLiveData.setValue(list);
                        subject.set(response.getData().getSubject());
                        name.set(response.getData().getName());
                    }
                }, Throwable::printStackTrace));
    }

    //Send message------------------------

    public void sendMessageBulletin(@NotNull String bulletinId, String message) {
        String token = "Bearer " + getDataManager().getAccessToken();
        getCompositeDisposable().add(getDataManager()
                .sendMessageBulletin(token, bulletinId, message)
                .map(response -> {
                    if (response != null) {
                        if(response.getSuccess() && response.getData()!= null && response.getData().getMessageUsers()!=null) {
                            for(int i = 0; i<response.getData().getMessageUsers().size(); i++){
                                if(response.getData().getMessageUsers().get(i).getMessage()!=null){
                                    response.getData().getMessageUsers().get(i).setMessageTime(
                                            response.getData().getMessageUsers().get(i).getMessage().getCreatedAt()
                                    );
                                }
                            }
                        }
                    }
                    return response;
                })
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null) {
                        if(response.getSuccess() && response.getData()!= null && response.getData().getMessageUsers()!=null &&
                                !response.getData().getMessageUsers().isEmpty()) {
                            updateMessagesBulletin(response.getData().getMessageUsers());
//                            saveSendMessageUser(response.getData().getMessageUsers());
                        }
                        else{
                            getNavigator().message(response.getMessage());
                        }
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    if(throwable instanceof UnknownHostException) {
                        getNavigator().message(R.string.default_network_error);
                    }
                    else{
                        getNavigator().message(R.string.default_error);
                    }
                }));
    }

    public void sendMessageFileBulletin(int bulletinId, File file, @NotNull String filePath) {
        String token = "Bearer " + getDataManager().getAccessToken();
        getNavigator().showProgress(true, 0);

        RequestBody userID = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(bulletinId));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("audio", file.getName(), requestFile);
        getCompositeDisposable().add(getDataManager()
                .sendMessageFileBulletin(token,userID, body)
                .map(response -> {
                    if (response != null) {
                        if(response.getSuccess() && response.getData()!= null && response.getData().getMessageUsers()!=null) {
                            for(int i = 0; i<response.getData().getMessageUsers().size(); i++){
                                if(response.getData().getMessageUsers().get(i).getMessage()!=null){
                                    response.getData().getMessageUsers().get(i).setMessageTime(
                                            response.getData().getMessageUsers().get(i).getMessage().getCreatedAt()
                                    );
                                }
                            }
                        }
                    }
                    return response;
                })
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null) {
                        if(response.getSuccess() && response.getData()!= null && response.getData().getMessageUsers()!=null &&
                                !response.getData().getMessageUsers().isEmpty()) {
                            updateMessagesBulletin(response.getData().getMessageUsers());
//                            saveSendMessageUser(response.getData().getMessageUsers());
                        }
                        else{
                            getNavigator().message(response.getMessage());
                        }
                    }
                    getNavigator().showProgress(false, 0);
                }, throwable -> {
                    getNavigator().showProgress(false, 0);
                    throwable.printStackTrace();
                    if(throwable instanceof UnknownHostException) {
                        getNavigator().message(R.string.default_network_error);
                    }
                    else{
                        getNavigator().message(R.string.default_error);
                    }
                }));
    }

    public void onFollow(Integer userId) {
        String token = "Bearer " + getDataManager().getAccessToken();
        Logger.e("Token:" + token);
        getNavigator().showProgress(true, 0);
        getCompositeDisposable().add(getDataManager()
                .sendFollow(token, String.valueOf(userId), 1)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null){
                        getMessagesApis(String.valueOf(userId));
                    }
                    getNavigator().showProgress(false, 0);
                }, throwable -> {
                    throwable.printStackTrace();
                    getNavigator().showProgress(false, 0);
                }));
    }

    public void onApprove(Integer userId) {
        String token = "Bearer " + getDataManager().getAccessToken();
        Logger.e("Token:" + token);
        getNavigator().showProgress(true, 0);
        getCompositeDisposable().add(getDataManager()
                .sendFollow(token, String.valueOf(userId), 1)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null){
                        getMessagesApis(String.valueOf(userId));
                    }
                    getNavigator().showProgress(false, 0);
                }, throwable -> {
                    throwable.printStackTrace();
                    getNavigator().showProgress(false, 0);
                }));
    }
}