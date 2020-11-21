package com.app.pixstory.data.model.api;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserHomeListResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private Object error;
    @SerializedName("data")
    @Expose
    private List<Data> data = null;

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

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {

        @SerializedName("user_id")
        @Expose
        private Integer userId;
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
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("country_id")
        @Expose
        private Integer countryId;
        @SerializedName("country_name")
        @Expose
        private String countryName;
        @SerializedName("user_badge")
        @Expose
        private List<UserBadge> userBadge = null;
        @SerializedName("is_follow")
        @Expose
        private Integer isFollow;
        @SerializedName("is_friend")
        @Expose
        private Integer isFriend;
        @SerializedName("friend")
        @Expose
        private String friend;
        @SerializedName("friend_text")
        @Expose
        private String friendText;
        @SerializedName("user_interest")
        @Expose
        private List<UserInterest> userInterest = null;
        @SerializedName("is_pro")
        @Expose
        private Integer isPro;
        @SerializedName("interest_count")
        @Expose
        private Integer interestCount;

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

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
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

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public List<UserBadge> getUserBadge() {
            return userBadge;
        }

        public void setUserBadge(List<UserBadge> userBadge) {
            this.userBadge = userBadge;
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

        public String getFriend() {
            return friend;
        }

        public void setFriend(String friend) {
            this.friend = friend;
        }

        public List<UserInterest> getUserInterest() {
            return userInterest;
        }

        public void setUserInterest(List<UserInterest> userInterest) {
            this.userInterest = userInterest;
        }

        public String getFriendText() {
            return friendText;
        }

        public void setFriendText(String friendText) {
            this.friendText = friendText;
        }

        public Integer getIsPro() {
            return isPro;
        }

        public void setIsPro(Integer isPro) {
            this.isPro = isPro;
        }

        public Integer getInterestCount() {
            return interestCount;
        }

        public void setInterestCount(Integer interestCount) {
            this.interestCount = interestCount;
        }
    }

    public static class UserInterest {

        public UserInterest(String interest) {
            this.interest = interest;
        }

        @SerializedName("interest")
        @Expose
        private String interest;

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

    }

    public static class UserBadge {
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("badge_icon_path")
        @Expose
        private String badgeIconPath;

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
    }


}

