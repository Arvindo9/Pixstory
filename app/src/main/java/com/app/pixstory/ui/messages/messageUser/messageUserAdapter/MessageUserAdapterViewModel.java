package com.app.pixstory.ui.messages.messageUser.messageUserAdapter;

import androidx.databinding.ObservableField;

import com.app.pixstory.data.model.db.messages.Badge;
import com.app.pixstory.data.model.db.messages.MessageUsers;

/**
 * Author : Arvindo Mondal
 * Created on 23-02-2020
 */
public class MessageUserAdapterViewModel {
    public final ObservableField<String> name;
    public final ObservableField<String> subject;
    public final ObservableField<String> message;
    public final ObservableField<String> messageFallow;
    public final ObservableField<String> messageFallowBulletin;
    public final ObservableField<String> time;
    public final ObservableField<String> profilePic;
    public final ObservableField<Boolean> pro;
    public final ObservableField<Boolean> badge;
    public final ObservableField<String> badge1;
    public final ObservableField<String> badge2;
    public final ObservableField<String> badge3;
    public final ObservableField<String> location;
    public final ObservableField<Boolean> fallow;

    public MessageUserAdapterViewModel(MessageUsers data) {
        name = new ObservableField<>(data.getUserName());
        profilePic = new ObservableField<>(data.getProfileImage());
        pro = new ObservableField<>(data.getPro());
        fallow = new ObservableField<>(data.getFollow());
        subject = new ObservableField<>();
        message = new ObservableField<>();
        time = new ObservableField<>();
        location = new ObservableField<>();
        if(data.getMessage()!=null) {
            subject.set(data.getMessage().getSubject());
            message.set(data.getMessage().getMessage());
            time.set(data.getMessage().getAddedOn());
            location.set(data.getMessage().getCountryName());
        }

        messageFallow = new ObservableField<>();
        messageFallowBulletin = new ObservableField<>();

        badge1 = new ObservableField<>("");
        badge2 = new ObservableField<>("");
        badge3 = new ObservableField<>("");
        badge = new ObservableField<>(data.getBadges() != null && !data.getBadges().isEmpty());
        if(data.getBadges() != null && !data.getBadges().isEmpty()) {
            for (int i=0;i<data.getBadges().size(); i++) {
                if (data.getBadges().get(i).getBadgeIconPath() != null &&
                        !data.getBadges().get(i).getBadgeIconPath().isEmpty()) {
                    if (i == 0) {
                        badge1.set(data.getBadges().get(i).getBadgeIconPath());
                    } else if (i == 1) {
                        badge2.set(data.getBadges().get(i).getBadgeIconPath());
                    } else if (i == 2) {
                        badge3.set(data.getBadges().get(i).getBadgeIconPath());
                    }
                }
            }
        }
    }
}
