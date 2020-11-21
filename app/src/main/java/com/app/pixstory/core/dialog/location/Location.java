package com.app.pixstory.core.dialog.location;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.app.pixstory.R;
import com.app.pixstory.databinding.AlertEnableLocationBinding;

public class Location {

    public static void enableLocation(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            AlertEnableLocationBinding binding = DataBindingUtil.inflate(inflater, R.layout.alert_enable_location, null, false);
            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setView(binding.getRoot());
            alert.setCancelable(false);
            final AlertDialog dialog = alert.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            binding.deny.setOnClickListener(view -> dialog.dismiss());
            binding.close.setOnClickListener(v -> dialog.dismiss());
            binding.allow.setOnClickListener(view -> {
                dialog.dismiss();

            });
        }

    }
}
