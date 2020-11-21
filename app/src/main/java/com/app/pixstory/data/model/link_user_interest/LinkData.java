package com.app.pixstory.data.model.link_user_interest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkData {
    @SerializedName("interest")
    @Expose
    private String interest;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
