package com.app.pixstory.data.model.access;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Author : Arvindo Mondal
 * Created on 06-03-2020
 */
public class Data {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("token")
    @Expose
    private String token = "";

    @SerializedName("form_completion")
    @Expose
    private Integer type;

    @SerializedName("user")
    @Expose
    private User user;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
