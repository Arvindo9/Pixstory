package com.app.pixstory.core.dialog.delete;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.app.pixstory.R;
import com.app.pixstory.databinding.AlertDeleteBinding;
import com.app.pixstory.databinding.AlertUnfollowBinding;
import com.app.pixstory.ui.dashboard.you.model.SelectImagesViewModel;

/**
 * Created by Kamlesh Yadav on 18-04-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class Delete {

    public static void deleteItem(Context context, SelectImagesViewModel viewModel, String token, int[] image_id) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null){
            AlertDeleteBinding binding = DataBindingUtil.inflate(inflater, R.layout.alert_delete, null, false);
            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setView(binding.getRoot());
            alert.setCancelable(false);
            final AlertDialog dialog = alert.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            binding.no.setOnClickListener(view -> dialog.dismiss());
            binding.close.setOnClickListener(v -> dialog.dismiss());
            binding.yes.setOnClickListener(view -> {
                dialog.dismiss();
                viewModel.deleteImage(token, image_id);


            });
        }

    }
}
