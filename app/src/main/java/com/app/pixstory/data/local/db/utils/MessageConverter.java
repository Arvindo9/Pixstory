package com.app.pixstory.data.local.db.utils;

import androidx.room.TypeConverter;

import com.app.pixstory.data.model.db.messages.Badge;
import com.app.pixstory.data.model.db.messages.Interest;
import com.app.pixstory.data.model.db.messages.Messages;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Author : Arvindo Mondal
 * Created on 05-03-2020
 */
public class MessageConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static List<Messages> messagesToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Messages>>() {}.getType();
        return gson.fromJson(data, listType);
    }
    @TypeConverter
    public static String messagesToString(List<Messages> list) {
        return gson.toJson(list);
    }

    @TypeConverter
    public static Messages messagesToString(String data) {
        if (data == null) {
            return new Messages();
        }
        Type listType = new TypeToken<Messages>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String messagesToString(Messages list) {
        return gson.toJson(list);
    }


    @TypeConverter
    public static List<Badge> badgeToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Badge>>() {}.getType();
        return gson.fromJson(data, listType);
    }
    @TypeConverter
    public static String badgeToString(List<Badge> list) {
        return gson.toJson(list);
    }


    @TypeConverter
    public static List<Interest> InterestToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Interest>>() {}.getType();
        return gson.fromJson(data, listType);
    }
    @TypeConverter
    public static String InterestToString(List<Interest> list) {
        return gson.toJson(list);
    }

}
