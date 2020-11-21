package com.app.pixstory.data.model.db.messages;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.app.pixstory.data.local.db.utils.DateConverter;
import com.app.pixstory.data.local.db.utils.MessageConverter;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.General;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

/**
 * Author : Arvindo Mondal
 * Created on 05-03-2020
 */
@Entity(tableName = "messages")
public class Messages implements Parcelable {

    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "messageId")
    @SerializedName("message_id")
    @Expose
    private Integer messageId;

    @ColumnInfo(name = "message")
    @SerializedName(value = "message", alternate = {"bulletin_message", "bulletin_chat_message"})
    @Expose
    private String message = "";

    @ColumnInfo(name = "subject")
    @SerializedName(value = "bulletin_subject")
    @Expose
    private String subject = "";

    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    @Expose
    private String createdAt = "";

    @ColumnInfo(name = "message_type")
    @SerializedName(value = "message_type", alternate = "msg_type")
    @Expose
    private Integer messageType = Constants.MESSAGE_TYPE_CHAT_TEXT;

    @Ignore
    @SerializedName("attachmentFile")
    @Expose
    private File attachment;

    @ColumnInfo(name = "attachment")
    @SerializedName(value = "attachment",alternate = {"voice_message", "chat_voice_message"})
    @Expose
    private String fileName = "";

    @ColumnInfo(name = "user_id_to")
    @SerializedName("user_id_to")
    @Expose
    private Integer userIdFrom;

    @ColumnInfo(name = "has_request")
    @SerializedName("has_request")
    @Expose
    private Integer hasRequest = 0;

    @ColumnInfo(name = "userId")
    @SerializedName("user_id_from")
    @Expose
    private Integer userId;

    @ColumnInfo(name = "message_side")
    @SerializedName("message_side")
    @Expose
    private Boolean messageSide = false;

    @ColumnInfo(name = "time")
    @SerializedName("time")
    @Expose
    private String time = "";

    @ColumnInfo(name = "added_on")
    @SerializedName("added_on")
    @Expose
    private String addedOn = "";

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "messageSendTime")
    @SerializedName("messageSendTime")
    @Expose
    private String messageSendTime = "";

    @SerializedName("message_status")
    @Expose
    private Integer messageStatus = 0;
    @SerializedName("message_time")
    @Expose
    private String messageTime = "";
    @SerializedName("card_type")
    @Expose
    private String cardType = "";
    @SerializedName("count_unread")
    @Expose
    private Integer countUnread;



    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("request_type_id")
    @Expose
    private Integer requestTypeId;
    @SerializedName("request_title")
    @Expose
    private String requestTitle;




//Bulletin--------------


    @SerializedName("bulletin_id")
    @Expose
    private Integer bulletinId;
    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("country_name")
    @Expose
    private String countryName = "";

    @TypeConverters(MessageConverter.class)
    @SerializedName("interest")
    @Expose
    private List<Interest> interest = null;

















    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @NotNull
    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(@NotNull Integer messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Integer getUserIdFrom() {
        return userIdFrom;
    }

    public void setUserIdFrom(Integer userIdFrom) {
        this.userIdFrom = userIdFrom;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getMessageSide() {
        return messageSide;
    }

    public void setMessageSide(Boolean messageSide) {
        this.messageSide = messageSide;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public String getMessageSendTime() {
        return messageSendTime;
    }

    public void setMessageSendTime(String messageSendTime) {
        this.messageSendTime = messageSendTime;
    }

    public void setAttachment(File attachment) {
        this.attachment = attachment;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getAttachment() {
        return attachment;
    }

    public Integer getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(Integer messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Integer getCountUnread() {
        return countUnread;
    }

    public void setCountUnread(Integer countUnread) {
        this.countUnread = countUnread;
    }

    public Integer getBulletinId() {
        return bulletinId;
    }

    public void setBulletinId(Integer bulletinId) {
        this.bulletinId = bulletinId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<Interest> getInterest() {
        return interest;
    }

    public void setInterest(List<Interest> interest) {
        this.interest = interest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(Integer requestTypeId) {
        this.requestTypeId = requestTypeId;
    }

    public String getRequestTitle() {
        return requestTitle;
    }

    public void setRequestTitle(String requestTitle) {
        this.requestTitle = requestTitle;
    }

    public Integer getHasRequest() {
        return hasRequest;
    }

    public void setHasRequest(Integer hasRequest) {
        this.hasRequest = hasRequest;
    }

    public Messages(){}

    @Ignore
    public Messages(int userId, String message){
        this.userId = userId;
        this.message = message;
        messageSide = true;
        messageSendTime = General.getCurrentDateTime();
    }

    @Ignore
    public Messages(int userId, File file, String fileName){
        this.userId = userId;
        this.attachment = file;
        this.fileName = fileName;
        messageSide = true;
        messageType = Constants.MESSAGE_TYPE_CHAT_AUDIO;
        messageSendTime = General.getCurrentDateTime();
    }

    @Ignore
    protected Messages(@NotNull Parcel in) {
        if (in.readByte() == 0) {
            messageId = null;
        } else {
            messageId = in.readInt();
        }
        message = in.readString();
        createdAt = in.readString();
        if (in.readByte() == 0) {
            messageType = null;
        } else {
            messageType = in.readInt();
        }
        fileName = in.readString();
        if (in.readByte() == 0) {
            userIdFrom = null;
        } else {
            userIdFrom = in.readInt();
        }
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        byte tmpMessageSide = in.readByte();
        messageSide = tmpMessageSide == 0 ? null : tmpMessageSide == 1;
        time = in.readString();
        addedOn = in.readString();
        messageSendTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (messageId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(messageId);
        }
        dest.writeString(message);
        dest.writeString(createdAt);
        if (messageType == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(messageType);
        }
        dest.writeString(fileName);
        if (userIdFrom == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userIdFrom);
        }
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userId);
        }
        dest.writeByte((byte) (messageSide == null ? 0 : messageSide ? 1 : 2));
        dest.writeString(time);
        dest.writeString(addedOn);
        dest.writeString(messageSendTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Messages> CREATOR = new Creator<Messages>() {
        @Override
        public Messages createFromParcel(Parcel in) {
            return new Messages(in);
        }

        @Override
        public Messages[] newArray(int size) {
            return new Messages[size];
        }
    };

}
