package com.app.pixstory.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPrefrenceResponse {

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
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("profile_activity_others")
        @Expose
        private Integer profileActivityOthers;
        @SerializedName("profile_activity_type")
        @Expose
        private Integer profileActivityType;
        @SerializedName("profile_activity_interest")
        @Expose
        private Integer profileActivityInterest;
        @SerializedName("allow_message")
        @Expose
        private Integer allowMessage;
        @SerializedName("message_type")
        @Expose
        private Integer messageType;
        @SerializedName("help_query")
        @Expose
        private Integer helpQuery;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getProfileActivityOthers() {
            return profileActivityOthers;
        }

        public void setProfileActivityOthers(Integer profileActivityOthers) {
            this.profileActivityOthers = profileActivityOthers;
        }

        public Integer getProfileActivityType() {
            return profileActivityType;
        }

        public void setProfileActivityType(Integer profileActivityType) {
            this.profileActivityType = profileActivityType;
        }

        public Integer getProfileActivityInterest() {
            return profileActivityInterest;
        }

        public void setProfileActivityInterest(Integer profileActivityInterest) {
            this.profileActivityInterest = profileActivityInterest;
        }

        public Integer getAllowMessage() {
            return allowMessage;
        }

        public void setAllowMessage(Integer allowMessage) {
            this.allowMessage = allowMessage;
        }

        public Integer getMessageType() {
            return messageType;
        }

        public void setMessageType(Integer messageType) {
            this.messageType = messageType;
        }

        public Integer getHelpQuery() {
            return helpQuery;
        }

        public void setHelpQuery(Integer helpQuery) {
            this.helpQuery = helpQuery;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }
}