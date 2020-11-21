package com.app.pixstory.ui.dashboard.upload.activity.story;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import androidx.annotation.NonNull;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.base.Speak;
import com.app.pixstory.core.binding.BindingUtils;
import com.app.pixstory.data.model.interest.Interest;
import com.app.pixstory.data.model.story.StoryData;
import com.app.pixstory.data.model.upload.Data;
import com.app.pixstory.data.model.upload_details.DetailData;
import com.app.pixstory.databinding.ContentBinding;
import com.app.pixstory.ui.dashboard.upload.model.MediaModel;
import com.app.pixstory.ui.dashboard.upload.model.UploadViewModel;
import com.app.pixstory.ui.dashboard.upload.navigator.UploadNavigator;
import com.app.pixstory.ui.interests.Interests;
import com.app.pixstory.utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;

public class Content extends BaseActivity<ContentBinding, UploadViewModel> {
    private com.app.pixstory.databinding.ContentBinding binding;
    private UploadViewModel viewModel;
    private ArrayList<Data> data;

    @Override
    public int getLayout() {
        return R.layout.content;
    }

    @Override
    protected void getBinding(ContentBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(UploadViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected Class<UploadViewModel> setViewModel() {
        return UploadViewModel.class;
    }

    @Override
    protected void init() {
        viewModel.setNavigator(this);
        data = new ArrayList<>();
        toolbar(binding.layoutToolbar.toolbar, binding.layoutToolbar.toolbarTitle, R.string.content);
        clickListener();
        Bundle bundle = getIntent().getBundleExtra(Constants.KEY_DEFAULT_ACTIVITY_BUNDLE);
        if (bundle != null) {
            data = bundle.getParcelableArrayList("key");
            BindingUtils.setStringPhotos(binding.banner, data.get(0).getPath(), binding.progressBar);

        }
    }

    private void clickListener() {
        binding.addInterest.setOnClickListener(this::onClick);
        binding.next.setOnClickListener(this::onClick);
        binding.addCaptionButton.speak.setOnClickListener(this::onClick);
        binding.layoutToolbar.cancel.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        if (view.getId() == R.id.speak) {
            Speak.narratePlease(this, binding.addCaptionButton, binding.addNarrative);
        } else if (view.getId() == R.id.addInterest) {
            startActivity(Interests.class);
        } else if (view.getId() == R.id.next) {
            next();
        } else if (view.getId() == R.id.cancel) {
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // Great! User has recorded and saved the audio file
            } else if (resultCode == RESULT_CANCELED) {
                // Oops! User has canceled the recording
            }
        }
    }

    private void next() {
        String storyTitle = binding.addTitle.getText().toString().trim();
        String storyNarrative = binding.addNarrative.getText().toString().trim();

        if (storyTitle.equals("")) {
            showToast("Please enter story title");
            binding.titleTxt.setTextColor(getResources().getColor(R.color.red));
        } else if (storyNarrative.equals("")) {
            binding.titleTxt.setTextColor(getResources().getColor(R.color.black));
            binding.addNarrativeTxt.setTextColor(getResources().getColor(R.color.red));
            showToast("Please enter story narrative");
        } else if (Interests.interestsId == null) {
            showToast("Please add interest for story");
            binding.addNarrativeTxt.setTextColor(getResources().getColor(R.color.black));
        } else if (Interests.interestsId.size() > 0) {
            // set story title and narrative
            viewModel.setIsPref(storyTitle, storyNarrative);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("key", data);
            startUploadActivity(Format.class, bundle);
        } else {
            showToast("Please add interest for story");
        }

    }

    @Override
    protected void onStart() {
        viewModel.onStart();
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
