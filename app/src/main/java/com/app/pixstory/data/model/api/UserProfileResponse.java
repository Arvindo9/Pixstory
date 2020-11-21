package com.app.pixstory.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserProfileResponse {

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

        @SerializedName("id")
        @Expose
        private Integer id;

        @SerializedName("bio_text")
        @Expose
        private String bioText;

        @SerializedName("lname")
        @Expose
        private String lname;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("username")
        @Expose
        private String username;

        @SerializedName("email")
        @Expose
        private String email;

        @SerializedName("mobile")
        @Expose
        private String mobile;

        @SerializedName("profile_image")
        @Expose
        private String profileImage;

        @SerializedName("country_code")
        @Expose
        private String countryCode;

        @SerializedName("dob")
        @Expose
        private Object dob;

        @SerializedName("country_id")
        @Expose
        private Integer countryId;

        @SerializedName("age")
        @Expose
        private Integer age;

        @SerializedName("country_name")
        @Expose
        private String countryName;

        @SerializedName("citation")
        @Expose
        private List<Citation> citation = null;

        @SerializedName("qualification")
        @Expose
        private List<Education> education = null;

        @SerializedName("work")
        @Expose
        private List<Work> work = null;

        @SerializedName("integrity")
        @Expose
        private List<Integrity> integrity = null;

        @SerializedName("interest")
        @Expose
        private List<String> interest = null;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getBioText() {
            return bioText;
        }

        public void setBioText(String bioText) {
            this.bioText = bioText;
        }

        public String getLname() {
            return lname;
        }

        public void setLname(String lname) {
            this.lname = lname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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

        public Object getDob() {
            return dob;
        }

        public void setDob(Object dob) {
            this.dob = dob;
        }

        public Integer getCountryId() {
            return countryId;
        }

        public void setCountryId(Integer countryId) {
            this.countryId = countryId;
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

        public List<Citation> getCitation() {
            return citation;
        }

        public void setCitation(List<Citation> citation) {
            this.citation = citation;
        }

        public List<Work> getWork() {
            return work;
        }

        public void setWork(List<Work> work) {
            this.work = work;
        }

        public List<Education> getEducation() {
            return education;
        }

        public void setEducation(List<Education> education) {
            this.education = education;
        }

        public List<String> getInterest() {
            return interest;
        }

        public void setInterest(List<String> interest) {
            this.interest = interest;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public List<Integrity> getIntegrity() {
            return integrity;
        }

        public void setIntegrity(List<Integrity> integrity) {
            this.integrity = integrity;
        }
    }
}

