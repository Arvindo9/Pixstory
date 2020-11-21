package com.app.pixstory.ui.navigation_view.terms_and_conditions;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.app.pixstory.R;

public class TermsAndConditionsFragment extends Fragment {

    private Dialog dialog;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        TermsAndConditionsViewModel viewModel = new ViewModelProvider(this).get(TermsAndConditionsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);
        // final TextView name = root.findViewById(R.id.name);
        final TextView content = root.findViewById(R.id.content);

        viewModel.getPageData();

        viewModel.getData().observe(getViewLifecycleOwner(), data -> {
            if (data != null) {
                //name.setText(data.getName());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    content.setText(Html.fromHtml(data.getContent(), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    content.setText(Html.fromHtml(data.getContent()));
                }
            }
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), (Observer) o -> {
            if ((Boolean) o) {
                showProgress();
            } else {
                hideProgres();
            }
        });

        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void showProgress() {
        View view = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        dialog.show();
    }

    public void hideProgres() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        } else {
            dialog.dismiss();
        }
    }
}