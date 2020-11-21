package com.app.pixstory.data.model.home;

import com.app.pixstory.data.model.badges.Badge;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kamlesh Yadav on 07-04-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class HomeData {
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_first_name")
    @Expose
    private String userFirstName;
    @SerializedName("user_last_name")
    @Expose
    private String userLastName;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("integrity_score")
    @Expose
    private Integer integrityScore;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("story_title")
    @Expose
    private String storyTitle;
    @SerializedName("story_id")
    @Expose
    private Integer storyId;
    @SerializedName("story_narrative")
    @Expose
    private String storyNarrative;
    @SerializedName("published_on")
    @Expose
    private String publishedOn;
    @SerializedName("story_cover_image")
    @Expose
    private String storyCoverImage;
    @SerializedName("badges")
    @Expose
    private List<Badge> badges = null;
    @SerializedName("challenge_count")
    @Expose
    private Integer challengeCount;
    @SerializedName("support_count")
    @Expose
    private Integer supportCount;
    @SerializedName("note_count")
    @Expose
    private Integer noteCount;
    @SerializedName("is_feature")
    @Expose
    private Integer isFeature;
    @SerializedName("is_trending")
    @Expose
    private Integer isTrending;
    @SerializedName("view_count")
    @Expose
    private Integer viewCount;
    @SerializedName("array_type")
    @Expose
    private String arrayType;
    @SerializedName("page_type")
    @Expose
    private Integer pageType;
    @SerializedName("page_title")
    @Expose
    private String pageTitle;
    @SerializedName("page_id")
    @Expose
    private Integer pageId;
    @SerializedName("page_cover_image")
    @Expose
    private String pageCoverImage;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getIntegrityScore() {
        return integrityScore;
    }

    public void setIntegrityScore(Integer integrityScore) {
        this.integrityScore = integrityScore;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
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

    public String getStoryNarrative() {
        return storyNarrative;
    }

    public void setStoryNarrative(String storyNarrative) {
        this.storyNarrative = storyNarrative;
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

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
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

    public Integer getIsFeature() {
        return isFeature;
    }

    public void setIsFeature(Integer isFeature) {
        this.isFeature = isFeature;
    }

    public Integer getIsTrending() {
        return isTrending;
    }

    public void setIsTrending(Integer isTrending) {
        this.isTrending = isTrending;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public String getArrayType() {
        return arrayType;
    }

    public void setArrayType(String arrayType) {
        this.arrayType = arrayType;
    }

    public Integer getPageType() {
        return pageType;
    }

    public void setPageType(Integer pageType) {
        this.pageType = pageType;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public String getPageCoverImage() {
        return pageCoverImage;
    }

    public void setPageCoverImage(String pageCoverImage) {
        this.pageCoverImage = pageCoverImage;
    }
}
