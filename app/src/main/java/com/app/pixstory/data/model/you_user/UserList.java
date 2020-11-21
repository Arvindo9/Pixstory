package com.app.pixstory.data.model.you_user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kamlesh Yadav on 01-05-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class UserList {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lname")
    @Expose
    private String lname;
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
    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("interest_count")
    @Expose
    private Integer interestCount;
    @SerializedName("interest")
    @Expose
    private List<Interest> interest = null;
    @SerializedName("is_follow")
    @Expose
    private Integer isFollow;
    @SerializedName("is_friend")
    @Expose
    private Integer isFriend;
    @SerializedName("is_pro")
    @Expose
    private Integer isPro;
    @SerializedName("friend_text")
    @Expose
    private String friendText;
    @SerializedName("badges")
    @Expose
    private List<YouBadge> badges = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
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

    public Integer getInterestCount() {
        return interestCount;
    }

    public void setInterestCount(Integer interestCount) {
        this.interestCount = interestCount;
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

    public Integer getIsPro() {
        return isPro;
    }

    public void setIsPro(Integer isPro) {
        this.isPro = isPro;
    }

    public String getFriendText() {
        return friendText;
    }

    public void setFriendText(String friendText) {
        this.friendText = friendText;
    }

    public List<YouBadge> getBadges() {
        return badges;
    }

    public void setBadges(List<YouBadge> badges) {
        this.badges = badges;
    }

    private Integer item = 1;

    private Integer size = 3;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }
}
