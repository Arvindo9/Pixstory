package com.app.pixstory.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Interest {

    public Interest(String interest) {
        this.interest = interest;
    }

    @SerializedName("interest")
    @Expose
    private String interest;

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

}
