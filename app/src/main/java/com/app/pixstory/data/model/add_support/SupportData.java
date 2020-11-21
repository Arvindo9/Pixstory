package com.app.pixstory.data.model.add_support;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SupportData implements Parcelable {
    @SerializedName("story_id")
    @Expose
    private Integer storyId;
    @SerializedName("story_title")
    @Expose
    private String storyTitle;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_first_name")
    @Expose
    private String userFirstName;
    @SerializedName("user_last_name")
    @Expose
    private String userLastName;
    @SerializedName("badges")
    @Expose
    private List<Object> badges = null;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("integrity_score")
    @Expose
    private Integer integrityScore;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("msg")
    @Expose
    private String msg;

    protected SupportData(Parcel in) {
        if (in.readByte() == 0) {
            storyId = null;
        } else {
            storyId = in.readInt();
        }
        storyTitle = in.readString();
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        userFirstName = in.readString();
        userLastName = in.readString();
        gender = in.readString();
        if (in.readByte() == 0) {
            integrityScore = null;
        } else {
            integrityScore = in.readInt();
        }
        profileImage = in.readString();
        msg = in.readString();
    }

    public static final Creator<SupportData> CREATOR = new Creator<SupportData>() {
        @Override
        public SupportData createFromParcel(Parcel in) {
            return new SupportData(in);
        }

        @Override
        public SupportData[] newArray(int size) {
            return new SupportData[size];
        }
    };

    public Integer getStoryId() {
        return storyId;
    }

    public void setStoryId(Integer storyId) {
        this.storyId = storyId;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public List<Object> getBadges() {
        return badges;
    }

    public void setBadges(List<Object> badges) {
        this.badges = badges;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getIntegrityScore() {
        return integrityScore;
    }

    public void setIntegrityScore(Integer integrityScore) {
        this.integrityScore = integrityScore;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (storyId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(storyId);
        }
        dest.writeString(storyTitle);
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userId);
        }
        dest.writeString(userFirstName);
        dest.writeString(userLastName);
        dest.writeString(gender);
        if (integrityScore == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(integrityScore);
        }
        dest.writeString(profileImage);
        dest.writeString(msg);
    }
}
