package com.app.pixstory.data.model.db.messages;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Author : Arvindo Mondal
 * Created on 24-04-2020
 */
public class Badge implements Parcelable {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName(value = "badge_icon_path",alternate = "bagde_icon_path")
    @Expose
    private String badgeIconPath;

    public Badge(){}

    protected Badge(Parcel in) {
        title = in.readString();
        badgeIconPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(badgeIconPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Badge> CREATOR = new Creator<Badge>() {
        @Override
        public Badge createFromParcel(Parcel in) {
            return new Badge(in);
        }

        @Override
        public Badge[] newArray(int size) {
            return new Badge[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBadgeIconPath() {
        return badgeIconPath;
    }

    public void setBadgeIconPath(String badgeIconPath) {
        this.badgeIconPath = badgeIconPath;
    }
}
