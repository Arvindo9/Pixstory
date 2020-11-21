package com.app.pixstory.data.model.api.messages;


import com.app.pixstory.data.model.db.messages.BulletinUsers;
import com.app.pixstory.data.model.db.messages.MessageUsers;
import com.app.pixstory.data.model.db.messages.MessageUsersNew;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author       : Arvindo Mondal
 * Created date : 13-08-2019
 */
public class Data {

    @SerializedName("new_users_list")
    @Expose
    private List<MessageUsersNew> messageUsersNew;

    @SerializedName(value = "message_users",alternate = "message_users_bulletins")
    @Expose
    private List<MessageUsers> messageUsers;

    @SerializedName("message_user")
    @Expose
    private MessageUsers messageUser;

    @SerializedName("bulletin_subject")
    @Expose
    private String subject;

    @SerializedName("bulletin_creator")
    @Expose
    private String name;


    public MessageUsers getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(MessageUsers messageUser) {
        this.messageUser = messageUser;
    }

    public List<MessageUsersNew> getMessageUsersNew() {
        return messageUsersNew;
    }

    public void setMessageUsersNew(List<MessageUsersNew> messageUsersNew) {
        this.messageUsersNew = messageUsersNew;
    }

    public List<MessageUsers> getMessageUsers() {
        return messageUsers;
    }

    public void setMessageUsers(List<MessageUsers> messageUsers) {
        this.messageUsers = messageUsers;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
