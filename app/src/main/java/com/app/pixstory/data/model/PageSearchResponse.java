package com.app.pixstory.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PageSearchResponse {

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

    public static class Data {

        @SerializedName("story_data")
        @Expose
        private List<StoryData> storyData = null;
        @SerializedName("page_data")
        @Expose
        private Object pageData;
        @SerializedName("photo_data")
        @Expose
        private Object photoData;
        @SerializedName("user_data")
        @Expose
        List<MemberData> userData = null;
        @SerializedName("faq_data")
        @Expose
        private Object faqData;

        public List<StoryData> getStoryData() {
            return storyData;
        }

        public void setStoryData(List<StoryData> storyData) {
            this.storyData = storyData;
        }

        public Object getPageData() {
            return pageData;
        }

        public void setPageData(Object pageData) {
            this.pageData = pageData;
        }

        public Object getPhotoData() {
            return photoData;
        }

        public void setPhotoData(Object photoData) {
            this.photoData = photoData;
        }

        public List<MemberData> getUserData() {
            return userData;
        }

        public void setUserData(List<MemberData> userData) {
            this.userData = userData;
        }

        public Object getFaqData() {
            return faqData;
        }

        public void setFaqData(Object faqData) {
            this.faqData = faqData;
        }

    }

}


