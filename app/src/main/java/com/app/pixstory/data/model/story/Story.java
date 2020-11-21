package com.app.pixstory.data.model.story;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Story {
    @SerializedName("user_first_name")
    @Expose
    private String userFirstName;
    @SerializedName("user_last_name")
    @Expose
    private String userLastName;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("integrity_score")
    @Expose
    private Integer integrityScore;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("story_type")
    @Expose
    private String storyType;
    @SerializedName("cover_img_path")
    @Expose
    private String coverImgPath;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("narrative")
    @Expose
    private String narrative;
    @SerializedName("audio_narrative_path")
    @Expose
    private Object audioNarrativePath;
    @SerializedName("published")
    @Expose
    private String published;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStoryType() {
        return storyType;
    }

    public void setStoryType(String storyType) {
        this.storyType = storyType;
    }

    public String getCoverImgPath() {
        return coverImgPath;
    }

    public void setCoverImgPath(String coverImgPath) {
        this.coverImgPath = coverImgPath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public Object getAudioNarrativePath() {
        return audioNarrativePath;
    }

    public void setAudioNarrativePath(Object audioNarrativePath) {
        this.audioNarrativePath = audioNarrativePath;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }


}
