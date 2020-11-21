package com.app.pixstory.data.model.you_user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kamlesh Yadav on 01-05-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class FollowingData {
    @SerializedName("following_count")
    @Expose
    private Integer followingCount;
    @SerializedName("following_list")
    @Expose
    private List<UserList> followingList = null;

    public Integer getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }

    public List<UserList> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(List<UserList> followingList) {
        this.followingList = followingList;
    }
}
