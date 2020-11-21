package com.app.pixstory.ui.dashboard.you.model;

/**
 * Created by Kamlesh Yadav on 06-04-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class YouFilterModel {
    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Boolean isChecked = false;
    public String filterName;

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getSetDataName() {
        return setDataName;
    }

    public void setSetDataName(String setDataName) {
        this.setDataName = setDataName;
    }

    public String setDataName;

    public String getSetFilterAction() {
        return setFilterAction;
    }

    public void setSetFilterAction(String setFilterAction) {
        this.setFilterAction = setFilterAction;
    }

    public String setFilterAction;
}
