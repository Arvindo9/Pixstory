package com.app.pixstory.data.model.upload_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("audio_caption_path")
    @Expose
    private Object audioCaptionPath;
    @SerializedName("credit")
    @Expose
    private String credit;

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

    public Object getAudioCaptionPath() {
        return audioCaptionPath;
    }

    public void setAudioCaptionPath(Object audioCaptionPath) {
        this.audioCaptionPath = audioCaptionPath;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
}
