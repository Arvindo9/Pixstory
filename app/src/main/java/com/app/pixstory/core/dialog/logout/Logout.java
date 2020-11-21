package com.app.pixstory.core.dialog.logout;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.app.pixstory.R;
import com.app.pixstory.databinding.AlertUnfollowBinding;
import com.app.pixstory.ui.dashboard.DashboardModel;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;

public class Logout {
    public static void showDialog(Context context, GoogleApiClient googleApiClient, DashboardModel model) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            AlertUnfollowBinding binding = DataBindingUtil.inflate(inflater, R.layout.alert_unfollow, null, false);
            binding.message.setText("Do you really want to logout?");
            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setView(binding.getRoot());
            alert.setCancelable(false);
            final AlertDialog dialog = alert.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            binding.cancel.setOnClickListener(view -> dialog.dismiss());
            binding.close.setOnClickListener(v -> dialog.dismiss());
            binding.ok.setOnClickListener(view -> {
                LoginManager.getInstance().logOut();
                model.logout(googleApiClient);
                dialog.dismiss();
            });
        }
    }
}
