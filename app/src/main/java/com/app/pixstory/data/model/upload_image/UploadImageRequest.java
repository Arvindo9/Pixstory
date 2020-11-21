package com.app.pixstory.data.model.upload_image;

import com.app.pixstory.data.model.upload.Data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class UploadImageRequest {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data uploadModel = null;

    @SerializedName("error")
    @Expose
    private Map<String, List<String>> error = null;


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getUploadModel() {
        return uploadModel;
    }

    public void setUploadModel(Data uploadModel) {
        this.uploadModel = uploadModel;
    }

    public Map<String, List<String>> getError() {
        return error;
    }

    public void setError(Map<String, List<String>> error) {
        this.error = error;
    }

}
