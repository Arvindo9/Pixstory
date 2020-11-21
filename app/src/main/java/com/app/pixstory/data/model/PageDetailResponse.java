package com.app.pixstory.data.model;

import com.app.pixstory.data.model.api.UserHomeListResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PageDetailResponse {

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

        @SerializedName("page")
        @Expose
        private Page page;

        public Page getPage() {
            return page;
        }

        public void setPage(Page page) {
            this.page = page;
        }

    }
    public static class Page {
        @SerializedName("creator_id")
        @Expose
        private Integer creatorId;
        @SerializedName("creator_name")
        @Expose
        private String creatorName;
        @SerializedName("creator_username")
        @Expose
        private String creatorUsername;
        @SerializedName("creator_lname")
        @Expose
        private String creatorLname;
        @SerializedName("integrity_score")
        @Expose
        private Integer integrityScore;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("about")
        @Expose
        private String about;
        @SerializedName("audio_about_path")
        @Expose
        private Object audioAboutPath;
        @SerializedName("cover_img_path")
        @Expose
        private String coverImgPath;
        @SerializedName("photo_id")
        @Expose
        private Integer photoId;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("creator_badge")
        @Expose
        private List<UserHomeListResponse.UserBadge> creatorBadge = null;
        @SerializedName("published_on")
        @Expose
        private String publishedOn;
        @SerializedName("page_interest")
        @Expose
        private List<PageInterest> pageInterest = null;
        @SerializedName("page_member")
        @Expose
        private List<PageMember> pageMember = null;
        @SerializedName("page_story")
        @Expose
        private List<PageStory> pageStory = null;
        @SerializedName("is_trending")
        @Expose
        private Integer isTrending;
        @SerializedName("page_chat")
        @Expose
        private List<Object> pageChat = null;
        @SerializedName("is_follow")
        @Expose
        private Integer isFollow;
        @SerializedName("is_page_admin")
        @Expose
        private Integer isPageAdmin;
        @SerializedName("page_view")
        @Expose
        private Integer pageView;

        public Integer getPhotoId() {
            return photoId;
        }

        public void setPhotoId(Integer photoId) {
            this.photoId = photoId;
        }

        public Integer getPageView() {
            return pageView;
        }

        public void setPageView(Integer pageView) {
            this.pageView = pageView;
        }

        public Integer getIsPageAdmin() {
            return isPageAdmin;
        }

        public void setIsPageAdmin(Integer isPageAdmin) {
            this.isPageAdmin = isPageAdmin;
        }

        public Integer getIsFollow() {
            return isFollow;
        }

        public void setIsFollow(Integer isFollow) {
            this.isFollow = isFollow;
        }

        public Integer getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(Integer creatorId) {
            this.creatorId = creatorId;
        }

        public String getCreatorName() {
            return creatorName;
        }

        public void setCreatorName(String creatorName) {
            this.creatorName = creatorName;
        }

        public String getCreatorUsername() {
            return creatorUsername;
        }

        public void setCreatorUsername(String creatorUsername) {
            this.creatorUsername = creatorUsername;
        }

        public String getCreatorLname() {
            return creatorLname;
        }

        public void setCreatorLname(String creatorLname) {
            this.creatorLname = creatorLname;
        }

        public Integer getIntegrityScore() {
            return integrityScore;
        }

        public void setIntegrityScore(Integer integrityScore) {
            this.integrityScore = integrityScore;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public Object getAudioAboutPath() {
            return audioAboutPath;
        }

        public void setAudioAboutPath(Object audioAboutPath) {
            this.audioAboutPath = audioAboutPath;
        }

        public String getCoverImgPath() {
            return coverImgPath;
        }

        public void setCoverImgPath(String coverImgPath) {
            this.coverImgPath = coverImgPath;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public List<UserHomeListResponse.UserBadge> getCreatorBadge() {
            return creatorBadge;
        }

        public void setCreatorBadge(List<UserHomeListResponse.UserBadge> creatorBadge) {
            this.creatorBadge = creatorBadge;
        }

        public String getPublishedOn() {
            return publishedOn;
        }

        public void setPublishedOn(String publishedOn) {
            this.publishedOn = publishedOn;
        }

        public List<PageInterest> getPageInterest() {
            return pageInterest;
        }

        public void setPageInterest(List<PageInterest> pageInterest) {
            this.pageInterest = pageInterest;
        }

        public List<PageMember> getPageMember() {
            return pageMember;
        }

        public void setPageMember(List<PageMember> pageMember) {
            this.pageMember = pageMember;
        }

        public List<PageStory> getPageStory() {
            return pageStory;
        }

        public void setPageStory(List<PageStory> pageStory) {
            this.pageStory = pageStory;
        }

        public Integer getIsTrending() {
            return isTrending;
        }

        public void setIsTrending(Integer isTrending) {
            this.isTrending = isTrending;
        }

        public List<Object> getPageChat() {
            return pageChat;
        }

        public void setPageChat(List<Object> pageChat) {
            this.pageChat = pageChat;
        }
    }

    public static class PageInterest {

        @SerializedName("id")
        @Expose
        private Integer id;

        @SerializedName("title")
        @Expose
        private String title;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }

    public static class PageMember {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("lname")
        @Expose
        private String lname;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("integrity_score")
        @Expose
        private Integer integrityScore;
        @SerializedName("level")
        @Expose
        private Integer level;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("badge")
        @Expose
        private List<UserHomeListResponse.UserBadge> badge;


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

        public Integer getIntegrityScore() {
            return integrityScore;
        }

        public void setIntegrityScore(Integer integrityScore) {
            this.integrityScore = integrityScore;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public List<UserHomeListResponse.UserBadge> getBadge() {
            return badge;
        }

        public void setBadge(List<UserHomeListResponse.UserBadge> badge) {
            this.badge = badge;
        }
    }

    public static class PageStory {

        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("title")
        @Expose
        private String title;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }

}