package com.app.pixstory.data.local.db.dao.messages;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.app.pixstory.data.model.db.messages.MessageUsers;

import java.util.List;

/**
 * Author : Arvindo Mondal
 * Created on 05-03-2020
 */
@Dao
public interface MessageUsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MessageUsers model);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MessageUsers> modelList);

    @Query("SELECT * FROM messageUsers order by messageTime limit 100")
    List<MessageUsers> getMessageUsers();
}
