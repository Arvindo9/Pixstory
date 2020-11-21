package com.app.pixstory.ui.dashboard.upload.activity.story;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.core.binding.BindingUtils;
import com.app.pixstory.core.dialog.caption.AddCaption;
import com.app.pixstory.data.model.interest.Interest;
import com.app.pixstory.data.model.story.StoryData;
import com.app.pixstory.data.model.upload.Data;
import com.app.pixstory.data.model.upload_details.DetailData;
import com.app.pixstory.databinding.ActivityCreateStoryFilterBinding;
import com.app.pixstory.ui.dashboard.upload.model.CreateStoryFilterViewModel;
import com.app.pixstory.ui.dashboard.upload.model.MediaModel;
import com.app.pixstory.ui.dashboard.upload.navigator.UploadNavigator;
import com.app.pixstory.ui.interests.Interests;
import com.app.pixstory.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;

public class CreateStoryFilterActivity extends BaseActivity<ActivityCreateStoryFilterBinding, CreateStoryFilterViewModel>
        implements UploadNavigator {
    private ActivityCreateStoryFilterBinding binding;
    private CreateStoryFilterViewModel viewModel;
    private ArrayList<Data> data;
    public String captionStr = "", photoCreditsStr;
    String banner = "";
    int id = 0;
    int position = 0;
    String edit_caption = "", edit_credit = "";
    private String token = "";


    @Override
    public int getLayout() {
        return R.layout.activity_create_story_filter;
    }

    @Override
    protected void getBinding(ActivityCreateStoryFilterBinding binding) {

        this.binding = binding;
    }

    @Override
    protected void getViewModel(CreateStoryFilterViewModel viewModel) {

        this.viewModel = viewModel;
    }

    @Override
    protected Class<CreateStoryFilterViewModel> setViewModel() {
        return CreateStoryFilterViewModel.class;
    }

    @Override
    protected void init() {
        viewModel.setNavigator(this);
        toolbar(binding.layoutToolbar.toolbar, binding.layoutToolbar.toolbarTitle, R.string.filter);
        // get master list interest
        initialization();
        clickListener();
        viewModel.getLoading().observe(this, o -> {
            if ((Boolean) o) {
                showSimmerLoader(DEFAULT_LOADER);
            } else {
                hideSimmerLoader();
            }
        });

    }

    private void initialization() {
        data = new ArrayList<>();
        Bundle bundle = getIntent().getBundleExtra(Constants.KEY_DEFAULT_ACTIVITY_BUNDLE);
        if (bundle != null) {
            String from = bundle.getString("from");
            if (from != null) {
                if (from.equals("Reorder")) {
                    data = bundle.getParcelableArrayList("key");
                    banner = bundle.getString("banner");
                    id = bundle.getInt("id");
                    BindingUtils.setStringPhotos(binding.banner, banner, binding.progressBar);
                } else if (from.equals("edit")) {
                    data = bundle.getParcelableArrayList("key");
                    position = bundle.getInt("position");

                    BindingUtils.setStringPhotos(binding.banner, data.get(position).getPath(), binding.progressBar);
                    edit_caption = data.get(position).getCaption();
                    edit_credit = data.get(position).getCredit();
                    id = data.get(position).getId();
                }
            }
        }
    }


    private void clickListener() {
        binding.addCaption.setOnClickListener(this::onClick);
        binding.addInterest.setOnClickListener(this::onClick);
        binding.ok.setOnClickListener(this::onClick);
        binding.layoutToolbar.cancel.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        if (view.getId() == R.id.addCaption) {
            AddCaption.addCaption(this, binding.addCaptiontxt, binding.photoCredits, edit_caption, edit_credit);
        } else if (view.getId() == R.id.addInterest) {
            startActivity(Interests.class);
        } else if (view.getId() == R.id.ok) {
            dataList();
        } else if (view.getId() == R.id.cancel) {
            finish();
        }
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void dataList() {
        captionStr = binding.addCaptiontxt.getText().toString().trim();
        photoCreditsStr = binding.photoCredits.getText().toString().trim();

        if (captionStr.equals("") || photoCreditsStr.equals("")) {
            finish();
        } else {
            //-----------------------interest id------------------------
            int[] interest_id = new int[0];
            if (Interests.interestsId != null) {
                interest_id = new int[Interests.interestsId.size()];
                int j = 0;
                for (int key : Interests.interestsId.keySet()) {
                    interest_id[j++] = key;
                }
            }
            viewModel.addPhotoDetails(id, captionStr, photoCreditsStr, interest_id);
        }
    }

    @Override
    public void onMyPhotoResponse(boolean b, List<Data> data, String type) {
        if (b) {
            showToast("Caption and Credits added successfully");
        } else {
            showToast("not Added");
        }
    }

    @Override
    public void onDetailResponse(boolean b, DetailData detailData) {
        if (b) {
            showToast("Caption and Credits added successfully");
            finish();
        } else {
            showToast("not Added");
        }
    }

    @Override
    public void onStoryResponse(boolean b, StoryData storyData) {

    }

    @Override
    public void onFileUploadSuccess(MediaModel model, Data uploadModel) {

    }

    @Override
    public void onMasterInterest(Boolean success, List<Interest> interest) {

    }

    @Override
    public void message(int message) {
        showToast(message);
    }

    @Override
    public void message(String message) {
        showToast(message);
    }
}
