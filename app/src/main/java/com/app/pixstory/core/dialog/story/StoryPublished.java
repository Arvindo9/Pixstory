package com.app.pixstory.core.dialog.story;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.app.pixstory.R;
import com.app.pixstory.databinding.AlertStoryPublishedBinding;
import com.app.pixstory.ui.dashboard.you.HomeYouActivity;
import com.app.pixstory.ui.interests.Interests;

import java.net.IDN;
import java.util.LinkedHashMap;

import static com.app.pixstory.utils.Constants.CREATION_TYPE;
import static com.app.pixstory.utils.Constants.USER_TYPE;

public class StoryPublished {
    public static void storyPublishedSuccessfully(Context context, Activity activity, Intent intent, String isType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            AlertStoryPublishedBinding binding = DataBindingUtil.inflate(inflater, R.layout.alert_story_published, null, false);
            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setView(binding.getRoot());
            alert.setCancelable(false);
            final AlertDialog dialog = alert.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            if (CREATION_TYPE.equals("page")){
                binding.message.setText(R.string.your_page_is_published);
            }

            binding.close.setOnClickListener(v -> {
                if (CREATION_TYPE.equals("page")){
                    dialog.dismiss();
                    activity.finishAffinity();
                    USER_TYPE = "page";
                    Intent intent1 = new Intent(activity, HomeYouActivity.class);
                    activity.startActivity(intent1);
                } else {
                    Interests.interestsId = new LinkedHashMap<>();
                    if (isType.equals("support") || isType.equals("challenge")) {
                        dialog.dismiss();
                        activity.finishAffinity();
                        USER_TYPE = "self";
                        Intent intent1 = new Intent(activity, HomeYouActivity.class);
                        activity.startActivity(intent1);
                    } else {
                        dialog.dismiss();
                        activity.finishAffinity();
                        USER_TYPE = "self";
                        Intent intent1 = new Intent(activity, HomeYouActivity.class);
                        activity.startActivity(intent1);
                    }
                }



            });

        }

    }
}
