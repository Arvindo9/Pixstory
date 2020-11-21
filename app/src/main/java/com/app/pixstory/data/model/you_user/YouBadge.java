package com.app.pixstory.data.model.you_user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kamlesh Yadav on 01-05-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class YouBadge {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("badge_icon_path")
    @Expose
    private String badgeIconPath;
    @SerializedName("disable_badge_icon")
    @Expose
    private String disableBadgeIcon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getDisableBadgeIcon() {
        return disableBadgeIcon;
    }

    public void setDisableBadgeIcon(String disableBadgeIcon) {
        this.disableBadgeIcon = disableBadgeIcon;
    }

}
