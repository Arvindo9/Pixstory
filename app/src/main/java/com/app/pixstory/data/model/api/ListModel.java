package com.app.pixstory.data.model.api;

public class ListModel {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private String nameData;

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Boolean isChecked = false;

    public int getBanner() {
        return banner;
    }

    public void setBanner(int banner) {
        this.banner = banner;
    }

    private int banner;

    public void setNameData(String filterDatum) {
        this.nameData = filterDatum;
    }

    public String getNameData(){
        return nameData;
    }
}
