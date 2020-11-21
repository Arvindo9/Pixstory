package com.app.pixstory.ui.dashboard.you.fragments.followings;

import com.app.pixstory.base.BaseNavigator;
import com.app.pixstory.data.model.you_user.FollowingData;
import com.app.pixstory.data.model.you_user.UserList;
import com.app.pixstory.data.model.you_user.FriendData;

import java.util.List;

/**
 * Created by Kamlesh Yadav on 01-05-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public interface FollowingsNavigator extends BaseNavigator {
    void followStatusChanged(String message, int position, int follow);

    void onFollowingResponse(Boolean success, FollowingData followingData, List<UserList> followingList);

}
