package com.app.pixstory.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

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
    private Err error;
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

    public Err getError() {
        return error;
    }

    public void setError(Err error) {
        this.error = error;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("form_status")
        @Expose
        private String formStatus;
        @SerializedName("form_completion")
        @Expose
        private Integer formCompletion;
        @SerializedName("user")
        @Expose
        private User user;
        @SerializedName("token")
        @Expose
        private String token;

        public String getFormStatus() {
            return formStatus;
        }

        public void setFormStatus(String formStatus) {
            this.formStatus = formStatus;
        }

        public Integer getFormCompletion() {
            return formCompletion;
        }

        public void setFormCompletion(Integer formCompletion) {
            this.formCompletion = formCompletion;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

    }

    public class User {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("lname")
        @Expose
        private String lname;
        @SerializedName("form_completion")
        @Expose
        private Integer formCompletion;
        @SerializedName("country_id")
        @Expose
        private Integer countryId;
        @SerializedName("integrity_score")
        @Expose
        private Integer integrityScore;

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

        public Integer getFormCompletion() {
            return formCompletion;
        }

        public void setFormCompletion(Integer formCompletion) {
            this.formCompletion = formCompletion;
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

    }

    public class Err
    {
        @SerializedName("form_status")
        @Expose
        private String formStatus;
        @SerializedName("form_completion")
        @Expose
        private Integer formCompletion;

        public String getFormStatus() {
            return formStatus;
        }

        public void setFormStatus(String formStatus) {
            this.formStatus = formStatus;
        }

        public Integer getFormCompletion() {
            return formCompletion;
        }

        public void setFormCompletion(Integer formCompletion) {
            this.formCompletion = formCompletion;
        }
    }

}




