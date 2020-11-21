package com.app.pixstory.data.local.db;


import com.app.pixstory.data.model.db.messages.MessageUsers;
import com.app.pixstory.data.model.db.messages.Messages;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Author       : Arvindo Mondal
 * Created on   : 09-05-2019
 * Email        : arvindomondal@gmail.com
 * Designation  : Programmer
 */
public interface DatabaseService {

    Flowable<Boolean> saveMessageUsers(List<MessageUsers> list);

    Flowable<List<MessageUsers>> getMessageUsers();

    Flowable<Boolean> saveMessages(List<Messages> list);

    Flowable<List<Messages>> getMessages(String userId);
}
