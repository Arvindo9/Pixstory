package com.app.pixstory.core.dialog.follow;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.app.pixstory.R;
import com.app.pixstory.databinding.AlertUnfollowBinding;

import java.util.Objects;

public class Follow {

    private static FollowCallback callback;

    public interface FollowCallback {
        void task();
    }

    public static void unFollow(Context context, int follow, String name, FollowCallback call) {
        callback = call;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            AlertUnfollowBinding binding = DataBindingUtil.inflate(inflater, R.layout.alert_unfollow, null, false);
            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setView(binding.getRoot());
            alert.setCancelable(false);
            final AlertDialog dialog = alert.create();
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            if (follow == 0) {
                binding.message.setText("Follow " + name);
            } else {
                binding.message.setText("Unfollow " + name);
            }
            binding.cancel.setOnClickListener(view -> dialog.dismiss());
            binding.close.setOnClickListener(v -> dialog.dismiss());
            binding.ok.setOnClickListener(view -> {
                dialog.dismiss();
                callback.task();
            });
        }

    }
}
