package com.app.pixstory.data.local.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.app.pixstory.data.local.db.dao.messages.MessageUsersDao;
import com.app.pixstory.data.local.db.dao.messages.MessagesDao;
import com.app.pixstory.data.local.db.utils.DateConverter;
import com.app.pixstory.data.local.db.utils.MessageConverter;

import com.app.pixstory.data.model.db.messages.MessageUsers;
import com.app.pixstory.data.model.db.messages.Messages;

/**
 * Author       : Arvindo Mondal
 * Created on   : 09-05-2019
 * Email        : arvindomondal@gmail.com
 * Designation  : Programmer
 */
@Database(entities = {Messages.class, MessageUsers.class},
        version = 3, exportSchema = false)

@TypeConverters({DateConverter.class, MessageConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract MessageUsersDao messageUsersDao();

    public abstract MessagesDao messagesDao();
}
