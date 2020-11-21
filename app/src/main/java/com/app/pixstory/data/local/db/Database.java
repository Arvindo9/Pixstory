package com.app.pixstory.data.local.db;

import android.content.Context;

import androidx.room.Room;

import com.app.pixstory.BuildConfig;
import com.app.pixstory.data.model.db.messages.MessageUsers;
import com.app.pixstory.data.model.db.messages.Messages;
import com.app.pixstory.utils.Constants;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Author       : Arvindo Mondal
 * Created on   : 09-05-2019
 * Email        : arvindomondal@gmail.com
 * Designation  : Programmer
 */
public final class Database implements DatabaseService {

    private static AppDatabase database;

    private Database(){
        //private
    }

    public static DatabaseService setup(Context context){
        database = Room.databaseBuilder(context, AppDatabase.class,
                BuildConfig.DATABASE_NAME).fallbackToDestructiveMigration()
//                .openHelperFactory(factory)
                .build();
        return new Database();
    }

    @Override
    public Flowable<Boolean> saveMessageUsers(List<MessageUsers> list) {
        return Flowable.fromCallable(() -> {
            database.messageUsersDao().insertAll(list);
            return true;
        });
    }

    @Override
    public Flowable<List<MessageUsers>> getMessageUsers() {
        return Flowable.fromCallable(() -> database.messageUsersDao().getMessageUsers());
    }

    @Override
    public Flowable<Boolean> saveMessages(List<Messages> list) {
        return Flowable.fromCallable(() -> {
            database.messagesDao().insertAll(list);
            return true;
        });
    }

    @Override
    public Flowable<List<Messages>> getMessages(String userId) {
        return Flowable.fromCallable(() -> database.messagesDao().getMessages(userId));
    }

/*
    @Override
    public Flowable<Boolean> saveTerms(List<Terms> list) {
        return Flowable.fromCallable(() -> {
            database.termsDao().insertAll(list);
            return true;
        });
    }

    @Override
    public Flowable<List<Terms>> getTerms() {
        return Flowable.fromCallable(() -> database.termsDao().getTermsList());
    }

    */
}
