package com.app.pixstory.ui.dashboard.upload.activity.story;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.core.binding.BindingUtils;
import com.app.pixstory.core.dialog.story.StoryPublished;
import com.app.pixstory.data.model.interest.Interest;
import com.app.pixstory.data.model.story.StoryData;
import com.app.pixstory.data.model.upload.Data;
import com.app.pixstory.data.model.upload_details.DetailData;
import com.app.pixstory.databinding.FormatBinding;
import com.app.pixstory.ui.dashboard.upload.model.MediaModel;
import com.app.pixstory.ui.dashboard.upload.navigator.UploadNavigator;
import com.app.pixstory.ui.dashboard.upload.model.UploadViewModel;
import com.app.pixstory.ui.interests.Interests;
import com.app.pixstory.utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.app.pixstory.utils.Constants.CREATION_TYPE;
import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;
import static com.app.pixstory.utils.Constants.IS_TYPE;
import static com.app.pixstory.utils.Constants.PUBLISH_OPTION;
import static com.app.pixstory.utils.Constants.STORY_FORMAT;
import static com.app.pixstory.utils.Constants.STORY_ID;

public class Format extends BaseActivity<FormatBinding, UploadViewModel> implements UploadNavigator {
    private com.app.pixstory.databinding.FormatBinding binding;
    private UploadViewModel viewModel;
    private ArrayList<Data> createStoryModels;
    public BottomSheetBehavior publishOptionBehaviour;

    @Override
    public int getLayout() {
        return R.layout.format;
    }

    @Override
    protected void getBinding(FormatBinding binding) {

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
        viewModel.getLoading().observe(this, o -> {
            if ((Boolean) o) {
                showSimmerLoader(DEFAULT_LOADER);
            } else {
                hideSimmerLoader();
            }
        });
        toolbar(binding.layoutToolbar.toolbar, binding.layoutToolbar.toolbarTitle, R.string.format);
        clickListener();

        Bundle bundle = getIntent().getBundleExtra(Constants.KEY_DEFAULT_ACTIVITY_BUNDLE);
        if (bundle != null) {
            createStoryModels = bundle.getParcelableArrayList("key");
            BindingUtils.setStringPhotos(binding.banner, createStoryModels.get(0).getPath(), binding.progressBar);
        }

        bottomSheetPublish();
        choosePublishOption();
    }

    private void clickListener() {
        binding.preview.setOnClickListener(this::onClick);
        binding.quickStory.setOnClickListener(this::onClick);
        binding.flickStory.setOnClickListener(this::onClick);
        binding.proStory.setOnClickListener(this::onClick);
        binding.next.setOnClickListener(this::onClick);
        binding.layoutToolbar.cancel.setOnClickListener(this::onClick);
        binding.bottomSheetPublishOption.closePublishOption.setOnClickListener(this::onClick);
        binding.bottomSheetPublishOption.publishStory.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        if (view.getId() == R.id.preview) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("key", createStoryModels);
            bundle.putString("from", STORY_FORMAT);
            startUploadActivity(StoryPreviewActivity.class, bundle);
        } else if (view.getId() == R.id.quick_story) {
            binding.quickStory.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
            binding.flickStory.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.proStory.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            STORY_FORMAT = "quick";
            viewModel.setStoryFormat(STORY_FORMAT);
        } else if (view.getId() == R.id.flick_story) {
            binding.quickStory.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.flickStory.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
            binding.proStory.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            STORY_FORMAT = "flick";
            viewModel.setStoryFormat(STORY_FORMAT);
        } else if (view.getId() == R.id.pro_story) {
            binding.quickStory.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.flickStory.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.proStory.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
            Toast.makeText(this, "Pro-Story is under review", Toast.LENGTH_SHORT).show();
            STORY_FORMAT = "pro";
            viewModel.setStoryFormat(STORY_FORMAT);
        } else if (view.getId() == R.id.cancel) {
            finish();
        } else if (view.getId() == R.id.next) {
            publishOptionBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getId() == R.id.close_publish_option) {
            publishOptionBehaviour.setHideable(true);
            publishOptionBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (view.getId() == R.id.publish_story) {
            // image id ------------
            LinkedHashMap<Integer, Integer> imageId = new LinkedHashMap<>();
            for (int i = 0; i < createStoryModels.size(); i++) {
                imageId.put(createStoryModels.get(i).getId(), createStoryModels.get(i).getId());

            }

            int[] image_id = new int[imageId.size()];

            int i = 0;
            for (int key : imageId.keySet()) {
                image_id[i++] = key;
            }
            //-----------------------interest id------------------------
            int[] interest_id = new int[0];
            if (Interests.interestsId != null) {
               interest_id = new int[Interests.interestsId.size()];
                int j = 0;
                for (int key : Interests.interestsId.keySet()) {
                    interest_id[j++] = key;
                }
            }

            //--------------------------------

            if (IS_TYPE.equals("support")){
                viewModel.publishStory(image_id, STORY_FORMAT, PUBLISH_OPTION, interest_id, IS_TYPE, STORY_ID);
            } else if (IS_TYPE.equals("challenge")){
                viewModel.publishStory(image_id, STORY_FORMAT, PUBLISH_OPTION, interest_id, IS_TYPE, STORY_ID);
            } else if (IS_TYPE.equals("default")){
                viewModel.publishStory(image_id, STORY_FORMAT, PUBLISH_OPTION, interest_id, "", 0);
            }

        }
    }

    private void bottomSheetPublish() {
        publishOptionBehaviour = BottomSheetBehavior.from(binding.bottomSheetPublishOption.publishOption);
        // set callback for changes
        publishOptionBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    publishOptionBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.blur.setVisibility(View.VISIBLE);
                binding.blur.setAlpha(slideOffset);
                binding.bottomSheetPublishOption.closePublishOption.animate().scaleX(0.05f + slideOffset).scaleY(0.05f + slideOffset).setDuration(0).start();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        viewModel.storyFormat();
        super.onStart();
    }

    private void choosePublishOption() {
        binding.bottomSheetPublishOption.publishOptionRg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.for_public) {
                PUBLISH_OPTION = "public";
            } else if (checkedId == R.id.friend) {
                PUBLISH_OPTION = "friends";
            } else if (checkedId == R.id.only_for_me) {
                PUBLISH_OPTION = "only_me";
            }
        });
    }

    @Override
    public void onMyPhotoResponse(boolean b, List<Data> data, String type) {

    }

    @Override
    public void onDetailResponse(boolean b, DetailData detailData) {

    }

    @Override
    public void onStoryResponse(boolean b, StoryData storyData) {
        if (b) {
            publishOptionBehaviour.setHideable(true);
            publishOptionBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            CREATION_TYPE = "story";
            StoryPublished.storyPublishedSuccessfully(this, this, getIntent(), IS_TYPE);
        } else {
            showToast("Story not created");
        }
    }

    @Override
    public void onFileUploadSuccess(MediaModel model, Data uploadModel) {

    }

    @Override
    public void onMasterInterest(Boolean success, List<Interest> interest) {

    }

    @Override
    public void message(int message) {

    }

    @Override
    public void message(String message) {

    }
}
