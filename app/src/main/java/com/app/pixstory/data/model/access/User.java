package com.app.pixstory.data.model.access;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Author : Arvindo Mondal
 * Created on 19-03-2020
 */
public class User {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lname")
    @Expose
    private String lastName;
    @SerializedName("form_completion")
    @Expose
    private Integer formCompletion;
    @SerializedName("country_id")
    @Expose
    private Integer countryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
}
