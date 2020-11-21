package com.app.pixstory.ui.dashboard.upload.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * Author       : Arvindo Mondal
 * Created on   : 23-07-2019
 */
public class MediaModel implements Parcelable {
    public String tag;
    public String dataPath;
    public boolean isSelected = false;
    public Uri uri;
    public String extension;
    private Bitmap bitmap;
    private File file;
    private String fileName;
    private int actionType;

    public MediaModel() {
    }

    public MediaModel(String tag, Bitmap bitmap, File file, String fileName, String extension) {
        this.tag = tag;
        this.bitmap = bitmap;
        this.file = file;
        this.fileName = fileName;
        this.extension = extension;
    }

    protected MediaModel(Parcel in) {
        actionType = in.readInt();
        tag = in.readString();
        dataPath = in.readString();
        isSelected = in.readByte() != 0;
        uri = in.readParcelable(Uri.class.getClassLoader());
        extension = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        fileName = in.readString();
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }


    public static final Creator<MediaModel> CREATOR = new Creator<MediaModel>() {
        @Override
        public MediaModel createFromParcel(Parcel in) {
            return new MediaModel(in);
        }

        @Override
        public MediaModel[] newArray(int size) {
            return new MediaModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(actionType);
        dest.writeString(dataPath);
        dest.writeString(tag);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeParcelable(uri, flags);
        dest.writeString(extension);
        dest.writeParcelable(bitmap, flags);
        dest.writeString(fileName);
    }
}
