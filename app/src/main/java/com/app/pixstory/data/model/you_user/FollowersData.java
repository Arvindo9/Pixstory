package com.app.pixstory.data.model.you_user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kamlesh Yadav on 01-05-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class FollowersData {
    @SerializedName("follower_count")
    @Expose
    private Integer followerCount;
    @SerializedName("follow_list")
    @Expose
    private List<UserList> followList = null;

    public Integer getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }

    public List<UserList> getFollowList() {
        return followList;
    }

    public void setFollowList(List<UserList> followList) {
        this.followList = followList;
    }
}
