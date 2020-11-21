package com.app.pixstory.data.model;

import com.app.pixstory.data.model.api.UserHomeListResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoryData {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("integrity_score")
    @Expose
    private Integer integrityScore;
    @SerializedName("story_title")
    @Expose
    private String storyTitle;
    @SerializedName("story_id")
    @Expose
    private Integer storyId;
    @SerializedName("published_on")
    @Expose
    private String publishedOn;
    @SerializedName("story_cover_image")
    @Expose
    private String storyCoverImage;
    @SerializedName("badges")
    @Expose
    private List<UserHomeListResponse.UserBadge> badges = null;
    @SerializedName("challenge_count")
    @Expose
    private Integer challengeCount;
    @SerializedName("support_count")
    @Expose
    private Integer supportCount;
    @SerializedName("note_count")
    @Expose
    private Integer noteCount;
    @SerializedName("view_count")
    @Expose
    private Integer viewCount;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getIntegrityScore() {
        return integrityScore;
    }

    public void setIntegrityScore(Integer integrityScore) {
        this.integrityScore = integrityScore;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public Integer getStoryId() {
        return storyId;
    }

    public void setStoryId(Integer storyId) {
        this.storyId = storyId;
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
    }

    public String getStoryCoverImage() {
        return storyCoverImage;
    }

    public void setStoryCoverImage(String storyCoverImage) {
        this.storyCoverImage = storyCoverImage;
    }

    public List<UserHomeListResponse.UserBadge> getBadges() {
        return badges;
    }

    public void setBadges(List<UserHomeListResponse.UserBadge> badges) {
        this.badges = badges;
    }

    public Integer getChallengeCount() {
        return challengeCount;
    }

    public void setChallengeCount(Integer challengeCount) {
        this.challengeCount = challengeCount;
    }

    public Integer getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(Integer supportCount) {
        this.supportCount = supportCount;
    }

    public Integer getNoteCount() {
        return noteCount;
    }

    public void setNoteCount(Integer noteCount) {
        this.noteCount = noteCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

}
