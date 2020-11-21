package com.app.pixstory.data.model.you_user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kamlesh Yadav on 01-05-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class FriendData {
    @SerializedName("friend_count")
    @Expose
    private Integer friendCount;
    @SerializedName("friend_list")
    @Expose
    private List<UserList> friendList = null;

    public Integer getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(Integer friendCount) {
        this.friendCount = friendCount;
    }

    public List<UserList> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<UserList> friendList) {
        this.friendList = friendList;
    }


}
