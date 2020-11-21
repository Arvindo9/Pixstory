package com.app.pixstory.ui.messages.message.adapterBulletin;

import androidx.databinding.ObservableField;

import com.app.pixstory.data.model.db.messages.MessageUsers;
import com.app.pixstory.data.model.db.messages.Messages;
import com.app.pixstory.utils.Constants;

/**
 * Author : Arvindo Mondal
 * Created on 28-04-2020
 */
public class MessageBulletinAdapterViewModel {
    public final ObservableField<String> name;
    public final ObservableField<String> message;
    public final ObservableField<String> messageFallowTitle;
    public final ObservableField<String> messageFallow;
    public final ObservableField<String> messageTime;

    public final ObservableField<Boolean> messageSide;
    public final ObservableField<Boolean> isAudio;

    MessageBulletinAdapterViewModel(MessageUsers data) {
        this.name = new ObservableField<>(data.getUserName());

        this.messageSide = new ObservableField<>(data.getMessage().getMessageSide());
        this.isAudio = new ObservableField<>(data.getMessage().getMessageType() == Constants.MESSAGE_TYPE_CHAT_AUDIO);
        this.message = new ObservableField<>(data.getMessage().getMessage());
        this.messageTime = new ObservableField<>(data.getMessage().getMessageTime());
        this.messageFallowTitle = new ObservableField<>("");
        this.messageFallow = new ObservableField<>("");

    }
}
