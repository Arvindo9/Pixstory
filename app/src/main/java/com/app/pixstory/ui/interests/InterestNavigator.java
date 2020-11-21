package com.app.pixstory.ui.interests;

import com.app.pixstory.base.BaseNavigator;
import com.app.pixstory.data.model.add_user_interest.AddUserInterestData;
import com.app.pixstory.data.model.global_search.GlobalInterestData;
import com.app.pixstory.data.model.link_user_interest.LinkData;

import java.util.List;

public interface InterestNavigator extends BaseNavigator {
    void onAddUserInterestResponse(boolean b, AddUserInterestData addUserInterestData);

    void onLinkInterestResponse(boolean b, List<LinkData> linkData);

    void onNewMessageSend(Boolean success, List<GlobalInterestData> messageUsers);

    void showProgress(boolean status);
}
