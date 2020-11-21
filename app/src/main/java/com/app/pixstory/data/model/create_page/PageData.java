package com.app.pixstory.data.model.create_page;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kamlesh Yadav on 11-04-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class PageData {
    @SerializedName("page")
    @Expose
    private Page page;
    @SerializedName("published_on")
    @Expose
    private String publishedOn;
    @SerializedName("photo_story")
    @Expose
    private List<Object> photoStory = null;
    @SerializedName("page_interest")
    @Expose
    private List<PageInterest> pageInterest = null;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
    }

    public List<Object> getPhotoStory() {
        return photoStory;
    }

    public void setPhotoStory(List<Object> photoStory) {
        this.photoStory = photoStory;
    }

    public List<PageInterest> getPageInterest() {
        return pageInterest;
    }

    public void setPageInterest(List<PageInterest> pageInterest) {
        this.pageInterest = pageInterest;
    }

}
