package com.app.pixstory.data.model.story;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoryInterest {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("id")
    @Expose
    private Integer id;

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

}
