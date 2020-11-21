package com.app.pixstory.data.model.story;

import com.app.pixstory.data.model.badges.Badge;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoryData {
    @SerializedName("story")
    @Expose
    private Story story;
    @SerializedName("published_on")
    @Expose
    private String publishedOn;
    @SerializedName("badges")
    @Expose
    private List<Badge> badges = null;
    @SerializedName("is_fav")
    @Expose
    private Integer isFav;
    @SerializedName("photo_story")
    @Expose
    private List<PhotoStory> photoStory = null;
    @SerializedName("story_interest")
    @Expose
    private List<StoryInterest> storyInterest = null;
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
    @SerializedName("is_trending")
    @Expose
    private Integer isTrending;
    @SerializedName("tag_count")
    @Expose
    private Integer tagCount;

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    public Integer getIsFav() {
        return isFav;
    }

    public void setIsFav(Integer isFav) {
        this.isFav = isFav;
    }

    public List<PhotoStory> getPhotoStory() {
        return photoStory;
    }

    public void setPhotoStory(List<PhotoStory> photoStory) {
        this.photoStory = photoStory;
    }

    public List<StoryInterest> getStoryInterest() {
        return storyInterest;
    }

    public void setStoryInterest(List<StoryInterest> storyInterest) {
        this.storyInterest = storyInterest;
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

    public Integer getIsTrending() {
        return isTrending;
    }

    public void setIsTrending(Integer isTrending) {
        this.isTrending = isTrending;
    }

    public Integer getTagCount() {
        return tagCount;
    }

    public void setTagCount(Integer tagCount) {
        this.tagCount = tagCount;
    }
}
