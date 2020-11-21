package com.app.pixstory.data.model.api;

import com.app.pixstory.data.model.badges.Badge;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AccountDetailResponse {

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
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("user")
        @Expose
        private User user;
        @SerializedName("badges")
        @Expose
        private List<Badge> badges = null;
        @SerializedName("followers")
        @Expose
        private Integer followers;
        @SerializedName("following")
        @Expose
        private Integer following;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public List<Badge> getBadges() {
            return badges;
        }

        public void setBadges(List<Badge> badges) {
            this.badges = badges;
        }

        public Integer getFollowers() {
            return followers;
        }

        public void setFollowers(Integer followers) {
            this.followers = followers;
        }

        public Integer getFollowing() {
            return following;
        }

        public void setFollowing(Integer following) {
            this.following = following;
        }

    }

    public class User {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("lname")
        @Expose
        private String lname;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("country_code")
        @Expose
        private String countryCode;
        @SerializedName("country_id")
        @Expose
        private Integer countryId;
        @SerializedName("integrity_score")
        @Expose
        private Integer integrityScore;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("bio_text")
        @Expose
        private String bioText;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("thumb_profile_image")
        @Expose
        private String thumbProfileImage;
        @SerializedName("age")
        @Expose
        private Integer age;
        @SerializedName("country_name")
        @Expose
        private String countryName;
        @SerializedName("email")
        @Expose
        private String email;

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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public Integer getCountryId() {
            return countryId;
        }

        public void setCountryId(Integer countryId) {
            this.countryId = countryId;
        }

        public Integer getIntegrityScore() {
            return integrityScore;
        }

        public void setIntegrityScore(Integer integrityScore) {
            this.integrityScore = integrityScore;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getBioText() {
            return bioText;
        }

        public void setBioText(String bioText) {
            this.bioText = bioText;
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

        public String getThumbProfileImage() {
            return thumbProfileImage;
        }

        public void setThumbProfileImage(String thumbProfileImage) {
            this.thumbProfileImage = thumbProfileImage;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

}
