package com.app.pixstory.data.model;

import com.app.pixstory.data.model.api.UserHomeListResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MemberData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("integrity_score")
    @Expose
    private Integer integrityScore;
    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("level")
    @Expose
    private String level;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("interest")
    @Expose
    private List<Interest> interest = null;
    @SerializedName("is_follow")
    @Expose
    private Integer isFollow;
    @SerializedName("is_friend")
    @Expose
    private Integer isFriend;
    @SerializedName("friend_text")
    @Expose
    private String friendText;
    @SerializedName("badges")
    @Expose
    private List<UserHomeListResponse.UserBadge> badges = null;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("interest_count")
    @Expose
    private int interestCount;

    private Integer item = 1;

    private Integer size = 3;

    public int getInterestCount() {
        return interestCount;
    }

    public void setInterestCount(int interestCount) {
        this.interestCount = interestCount;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Integer getIntegrityScore() {
        return integrityScore;
    }

    public void setIntegrityScore(Integer integrityScore) {
        this.integrityScore = integrityScore;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<Interest> getInterest() {
        return interest;
    }

    public void setInterest(List<Interest> interest) {
        this.interest = interest;
    }

    public Integer getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(Integer isFollow) {
        this.isFollow = isFollow;
    }

    public Integer getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(Integer isFriend) {
        this.isFriend = isFriend;
    }

    public String getFriendText() {
        return friendText;
    }

    public void setFriendText(String friendText) {
        this.friendText = friendText;
    }

    public List<UserHomeListResponse.UserBadge> getBadges() {
        return badges;
    }

    public void setBadges(List<UserHomeListResponse.UserBadge> badges) {
        this.badges = badges;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}