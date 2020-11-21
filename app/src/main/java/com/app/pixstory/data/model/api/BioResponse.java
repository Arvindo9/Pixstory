package com.app.pixstory.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BioResponse {

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
        @SerializedName("bio_text")
        @Expose
        private String bioText;
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

        public String getBioText() {
            return bioText;
        }

        public void setBioText(String bioText) {
            this.bioText = bioText;
        }

        public Integer getIntegrityScore() {
            return integrityScore;
        }

        public void setIntegrityScore(Integer integrityScore) {
            this.integrityScore = integrityScore;
        }

    }
}


