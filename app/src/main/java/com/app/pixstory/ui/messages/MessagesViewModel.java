package com.app.pixstory.ui.messages;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.lifecycle.MutableLiveData;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseViewModel;
import com.app.pixstory.data.model.db.messages.MessageUsers;
import com.app.pixstory.data.model.db.messages.MessageUsersNew;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.util.Logger;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Author : Arvindo Mondal
 * Created on 20-02-2020
 */
public class MessagesViewModel extends BaseViewModel<MessagesNavigation> {
    public final ObservableList<MessageUsersNew> newUsersList;
    private final MutableLiveData<List<MessageUsersNew>> newUsersLiveData;

    public final ObservableList<MessageUsers> modelMessageUsers;
    private final MutableLiveData<List<MessageUsers>> modelMessageUsersLiveData;

    public MessagesViewModel(){
        newUsersList = new ObservableArrayList<>();
        newUsersLiveData = new MutableLiveData<>();
        modelMessageUsers = new ObservableArrayList<>();
        modelMessageUsersLiveData = new MutableLiveData<>();
    }

    public void addMessageUsersNew(List<MessageUsersNew> list) {
        newUsersList.clear();
        newUsersList.addAll(list);
    }

    public void addMessageUsers(List<MessageUsers> list) {
        modelMessageUsers.clear();
        modelMessageUsers.addAll(list);
    }

    public MutableLiveData<List<MessageUsersNew>> getNewUsersLiveData() {
        return newUsersLiveData;
    }

    public MutableLiveData<List<MessageUsers>> getMessageUsersLiveData() {
        return modelMessageUsersLiveData;
    }

    //Message User list--------------------------
    //New message user api
    public void getNewUserList(String search) {
        getNavigator().showProgress(true, 1);
        String token = "Bearer " + getDataManager().getAccessToken();
        getCompositeDisposable().add(getDataManager()
                .getMessageNewUserList(token, search)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null) {
                        if(response.getSuccess() && response.getData()!= null && response.getData().getMessageUsersNew()!=null) {
                            newUsersLiveData.setValue(response.getData().getMessageUsersNew());
                        }
                        else{
                            getNavigator().message(response.getMessage());
                        }
                    }
                    getNavigator().showProgress(false, 1);
                }, throwable -> {
                    getNavigator().showProgress(false, 1);
                    throwable.printStackTrace();
                    if(throwable instanceof UnknownHostException) {
                        getNavigator().message(R.string.default_network_error);
                    }
                    else{
                        getNavigator().message(R.string.default_error);
                    }
                }));
    }

    //Message user DB
    public void getMessageUsersDb() {
//        getMessageUsersApis();
//        if(true)
//            return;
        getCompositeDisposable().add(getDataManager()
                .getMessageUsers()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null && !response.isEmpty()) {
                        modelMessageUsersLiveData.setValue(response);
                    }
                    getMessageUsersApis();
                }, throwable -> getMessageUsersApis()));
    }

    //Message user APIS
    public void getMessageUsersApis() {
        int page =1;
        String token = "Bearer " + getDataManager().getAccessToken();
        Logger.e("Token:" + token);
        getCompositeDisposable().add(getDataManager()
                .getMessageUsers(token, page, Constants.DEFAULT_PAGE_LIMIT_MESSAGE)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null && response.getData()!=null && response.getData().getMessageUsers() != null &&
                            !response.getData().getMessageUsers().isEmpty()){
                        modelMessageUsersLiveData.setValue(response.getData().getMessageUsers());
                        saveMessageUser(response.getData().getMessageUsers());
                    }
                }, Throwable::printStackTrace));
    }

    public void sendNewMessage(@NotNull int userId, String message) {
        getNavigator().showProgress(true, 0);
        String token = "Bearer " + getDataManager().getAccessToken();
        getCompositeDisposable().add(getDataManager()
                .sendMessage(token, String.valueOf(userId), message)
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
                        if(response.getSuccess() && response.getData()!= null && response.getData().getMessageUsers()!=null) {
                            getNavigator().onNewMessageSend(response.getData().getMessageUsers());
                            saveSendMessageUser(response.getData().getMessageUsers());
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

    public void sendNewMessageFile(int userId, File file, @NotNull String filePath) {
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
                        if(response.getSuccess() && response.getData()!= null && response.getData().getMessageUsers()!=null) {
                            getNavigator().onNewMessageSend(response.getData().getMessageUsers());
                            saveSendMessageUser(response.getData().getMessageUsers());
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

    private void saveMessageUser(List<MessageUsers> messageUsers){
        getCompositeDisposable().add(getDataManager()
                .saveMessageUsers(messageUsers)
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

    private void sendMessage(){}

    //When new message send to new user from activity
    public void onNewMessageSend(List<MessageUsers> messageUsers) {
        List<MessageUsers> list = new ArrayList<>(modelMessageUsers);
        list.addAll(messageUsers);
        modelMessageUsersLiveData.setValue(list);
    }

    //When new message send to new user from activity
    public void onNewMessageSend(String type, List<MessageUsers> messageUsers, String bulletinType) {
        if(type.equals(Constants.MESSAGE_TYPE_INBOX)){
            getMessageUsersApis();
        }
        else{
            getMessageUsersBulletinApis(bulletinType);
        }
    }

    public void tmp() {
        Logger.e("token:" + getDataManager().getAccessToken());
        Logger.e("L token:", getDataManager().getAccessToken());
    }

    public void getCountryList() {
        isLoading(true);
        getCompositeDisposable().add(getDataManager()
                .countryList()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                            isLoading(false);
                            if (response != null) {
                                if (response.getSuccess()) {
                                    getNavigator().setCountryList(response.getData());
                                } else {
                                    getNavigator().message(response.getMessage());
                                }
                            } else {
                                getNavigator().message(R.string.default_error);
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

    //Bulletin board--------------------

    //Message user APIS
    public void getMessageUsersBulletinApis(String type) {
        int page =1;
        String token = "Bearer " + getDataManager().getAccessToken();
        Logger.e("Token:" + token);
//        String type = "default";
        getCompositeDisposable().add(getDataManager()
                .getMessageUsersBulletin(token, type, 0, page, Constants.DEFAULT_PAGE_LIMIT_MESSAGE)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null && response.getData()!=null && response.getData().getMessageUsers() != null &&
                            !response.getData().getMessageUsers().isEmpty()){
                        modelMessageUsersLiveData.setValue(response.getData().getMessageUsers());
                    }
                }, Throwable::printStackTrace));
    }

    public void sendNewMessageBullet(String subject, String message, String countryId, int[] interest_id) {
        Logger.e("Send:" + countryId);
        getNavigator().showProgress(true, 0);

        String token = "Bearer " + getDataManager().getAccessToken();
        getCompositeDisposable().add(getDataManager()
                .sendMessageBulletin(token, subject, message, interest_id, countryId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null) {
                        if(response.getSuccess() && response.getData()!= null && response.getData().getMessageUsers()!=null) {
                            getNavigator().onNewMessageBulletinSend(response.getData().getMessageUsers());
                        }
                        else{
                            getNavigator().message(response.getMessage());
                        }
                    }
                    getNavigator().showProgress(false, 0);
                }, throwable -> {
                    throwable.printStackTrace();
                    if(throwable instanceof UnknownHostException) {
                        getNavigator().message(R.string.default_network_error);
                    }
                    else{
                        getNavigator().message(R.string.default_error);
                    }
                    getNavigator().showProgress(false, 0);
                }));
    }

    public void sendNewMessageFileBullet(String subject, File audioFile, String outputFile, String countryId,
                                         int[] interest_id) {
        String token = "Bearer " + getDataManager().getAccessToken();
        getNavigator().showProgress(true, 0);

        RequestBody subjectLine = RequestBody.create(MediaType.parse("multipart/form-data"), subject);
        RequestBody country = RequestBody.create(MediaType.parse("multipart/form-data"), countryId);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), audioFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("audio", audioFile.getName(), requestFile);
        getCompositeDisposable().add(getDataManager()
                .sendMessageFileBulletin(token, subjectLine, country, interest_id, body)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null) {
                        if(response.getSuccess() && response.getData()!= null && response.getData().getMessageUsers()!=null) {
                            getNavigator().onNewMessageBulletinSend(response.getData().getMessageUsers());
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

    public void removeCard(int userId) {
        getNavigator().showProgress(true, 0);

        String token = "Bearer " + getDataManager().getAccessToken();
        getCompositeDisposable().add(getDataManager()
                .deleteMessageUser(token, userId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response != null) {
                        if(!response.getSuccess() && response.getMessage()!=null){
                            getNavigator().message(response.getMessage());
                        }
                    }
                    getNavigator().showProgress(false, 0);
                }, throwable -> {
                    throwable.printStackTrace();
                    if(throwable instanceof UnknownHostException) {
                        getNavigator().message(R.string.default_network_error);
                    }
                    else{
                        getNavigator().message(R.string.default_error);
                    }
                    getNavigator().showProgress(false, 0);
                }));
    }
}