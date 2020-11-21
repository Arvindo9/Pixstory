package com.app.pixstory.data.model.db.messages;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.app.pixstory.data.local.db.utils.DateConverter;
import com.app.pixstory.data.local.db.utils.MessageConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Author : Arvindo Mondal
 * Created on 23-02-2020
 */
@Entity(tableName = "messageUsers")
public class MessageUsers implements Parcelable {

    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    @Expose
    private Integer userId;

    @ColumnInfo(name = "profile_image")
    @SerializedName("profile_image")
    @Expose
    private String profileImage = "";

    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    private String name = "";

    @ColumnInfo(name = "username")
    @SerializedName("username")
    @Expose
    private String userName = "";

    @ColumnInfo(name = "gender")
    @SerializedName("gender")
    @Expose
    private String gender = "";

    @TypeConverters(MessageConverter.class)
    @ColumnInfo(name = "message")
    @SerializedName(value = "message", alternate = "bulletin")
    @Expose
    private Messages message;


    @TypeConverters(MessageConverter.class)
    @SerializedName("messages")
    @Expose
    private List<Messages> messages = null;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "messageTime")
    @SerializedName("messageTime")
    @Expose
    private String messageTime = "";


    @SerializedName("follow")
    @Expose
    private Boolean follow;
    @SerializedName("pro")
    @Expose
    private Boolean pro;

    @TypeConverters(MessageConverter.class)
    @SerializedName("badges")
    @Expose
    private List<Badge> badges = null;











    @SerializedName("creator_id")
    @Expose
    private Integer creatorId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("location")
    @Expose
    private String location = "";







    protected MessageUsers(Parcel in) {
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        profileImage = in.readString();
        name = in.readString();
        userName = in.readString();
        gender = in.readString();
        message = in.readParcelable(Messages.class.getClassLoader());
        messages = in.createTypedArrayList(Messages.CREATOR);
        messageTime = in.readString();
        byte tmpFollow = in.readByte();
        follow = tmpFollow == 0 ? null : tmpFollow == 1;
        byte tmpPro = in.readByte();
        pro = tmpPro == 0 ? null : tmpPro == 1;
        badges = in.createTypedArrayList(Badge.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userId);
        }
        dest.writeString(profileImage);
        dest.writeString(name);
        dest.writeString(userName);
        dest.writeString(gender);
        dest.writeParcelable(message, flags);
        dest.writeTypedList(messages);
        dest.writeString(messageTime);
        dest.writeByte((byte) (follow == null ? 0 : follow ? 1 : 2));
        dest.writeByte((byte) (pro == null ? 0 : pro ? 1 : 2));
        dest.writeTypedList(badges);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MessageUsers> CREATOR = new Creator<MessageUsers>() {
        @Override
        public MessageUsers createFromParcel(Parcel in) {
            return new MessageUsers(in);
        }

        @Override
        public MessageUsers[] newArray(int size) {
            return new MessageUsers[size];
        }
    };

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Messages getMessage() {
        return message;
    }

    public void setMessage(Messages message) {
        this.message = message;
    }

    public Boolean getFollow() {
        return follow;
    }

    public void setFollow(Boolean follow) {
        this.follow = follow;
    }

    public Boolean getPro() {
        return pro;
    }

    public void setPro(Boolean pro) {
        this.pro = pro;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    public List<Messages> getMessages() {
        return messages;
    }

    public void setMessages(List<Messages> messages) {
        this.messages = messages;
    }

    public MessageUsers(){}
}

