package com.app.pixstory.data.model.story;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotoStory implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("credit")
    @Expose
    private String credit;

    public PhotoStory(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        path = in.readString();
        caption = in.readString();
        credit = in.readString();
    }

    public static final Creator<PhotoStory> CREATOR = new Creator<PhotoStory>() {
        @Override
        public PhotoStory createFromParcel(Parcel in) {
            return new PhotoStory(in);
        }

        @Override
        public PhotoStory[] newArray(int size) {
            return new PhotoStory[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
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
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(path);
        dest.writeString(caption);
        dest.writeString(credit);
    }
}
