package com.app.pixstory.data.local.db.dao.messages;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.app.pixstory.data.model.db.messages.Messages;

import java.util.List;

/**
 * Author : Arvindo Mondal
 * Created on 05-03-2020
 */
@Dao
public interface MessagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Messages model);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Messages> modelList);

    @Query("SELECT * FROM messages where userId = :userId order by messageId limit 100")
    List<Messages> getMessages(String userId);
}
