package com.app.pixstory.data.model.version_check;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppVersion {
    @SerializedName("app_version_name")
    @Expose
    private String appVersionName;
    @SerializedName("app_version_code")
    @Expose
    private Integer appVersionCode;

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public Integer getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(Integer appVersionCode) {
        this.appVersionCode = appVersionCode;
    }
}
