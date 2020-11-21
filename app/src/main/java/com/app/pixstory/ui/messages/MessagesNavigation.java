package com.app.pixstory.ui.messages;


import com.app.pixstory.base.BaseNavigator;
import com.app.pixstory.data.model.api.CountryResponse;
import com.app.pixstory.data.model.db.messages.MessageUsers;

import java.util.List;

/**
 * Author       : Arvindo Mondal
 * Created date : 13-08-2019
 */
public interface MessagesNavigation extends BaseNavigator {
    void onNewMessageSend(List<MessageUsers> messageUsers);

    void showProgress(boolean status, int type);

    void setCountryList(List<CountryResponse.Data> data);

    void onNewMessageBulletinSend(List<MessageUsers> bulletinUsers);
}
