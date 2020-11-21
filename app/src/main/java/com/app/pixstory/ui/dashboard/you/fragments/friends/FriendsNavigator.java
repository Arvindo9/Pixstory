package com.app.pixstory.ui.dashboard.you.fragments.friends;

import com.app.pixstory.base.BaseNavigator;
import com.app.pixstory.data.model.you_user.UserList;
import com.app.pixstory.data.model.you_user.FriendData;

import java.util.List;

/**
 * Created by Kamlesh Yadav on 30-04-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public interface FriendsNavigator extends BaseNavigator {

    void onFriendResponse(Boolean success, FriendData data, List<UserList> friendList);
    void followStatusChanged(String message, int position, int follow);
}
