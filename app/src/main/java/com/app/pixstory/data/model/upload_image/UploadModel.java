package com.app.pixstory.data.model.upload_image;

import android.os.Parcel;
import android.os.Parcelable;

public class UploadModel implements Parcelable {
    public String id;

    protected UploadModel(Parcel in) {
        id = in.readString();
        path = in.readString();
    }

    public static final Creator<UploadModel> CREATOR = new Creator<UploadModel>() {
        @Override
        public UploadModel createFromParcel(Parcel in) {
            return new UploadModel(in);
        }

        @Override
        public UploadModel[] newArray(int size) {
            return new UploadModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String path;

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
        dest.writeString(id);
        dest.writeString(path);
    }
}
