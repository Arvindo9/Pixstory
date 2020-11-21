package com.app.pixstory.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Integrity {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("score")
    @Expose
    private String score;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("added_time")
    @Expose
    private String addedTime;

    @SerializedName("added_on")
    @Expose
    private String addedOn;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(String addedTime) {
        this.addedTime = addedTime;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }
}