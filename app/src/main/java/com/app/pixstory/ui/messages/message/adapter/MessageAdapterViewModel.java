package com.app.pixstory.ui.messages.message.adapter;

import androidx.databinding.ObservableField;

import com.app.pixstory.data.model.db.messages.Messages;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.General;

/**
 * Author       : Arvindo Mondal
 * Created on   : 05-09-2019
 * Designation  : Programmer
 * Strength     : Never give up
 * Motto        : To be known as great Mathematician
 * Skills       : Algorithms and logic
 */
public class MessageAdapterViewModel {
    public final ObservableField<String> name;
    public final ObservableField<String> message;
    public final ObservableField<String> messageFallowTitle;
    public final ObservableField<String> messageFallow;
    public final ObservableField<String> messageTime;

    public final ObservableField<Boolean> messageSide;

    MessageAdapterViewModel(Messages data, String name) {
        this.name = new ObservableField<>(name);
        this.messageSide = new ObservableField<>(data.getMessageSide());
        this.message = new ObservableField<>(data.getMessage());
        this.messageTime = new ObservableField<>(data.getTime());
        this.messageFallowTitle = new ObservableField<>(data.getMessage());
        this.messageFallow = new ObservableField<>(data.getRequestTitle());

    }

    private String getDate(String messageTime, String previousTime, int position) {
        if(position > 0 && General.getEqualsDate(messageTime, previousTime)){
            return "";
        }
        return General.getDateReadable(messageTime);
    }

    private String getDate(String date){
        return General.getDateReadableTime(date);
    }
}
