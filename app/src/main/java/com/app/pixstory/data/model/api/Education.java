package com.app.pixstory.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Education {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("degree")
    @Expose
    private String degree;
    @SerializedName("university")
    @Expose
    private String university;
    @SerializedName("institue")
    @Expose
    private String institue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getInstitue() {
        return institue;
    }

    public void setInstitue(String  institue) {
        this.institue = institue;
    }

}
