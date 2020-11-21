package com.app.pixstory.ui.dashboard.upload.activity.page;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.core.binding.BindingUtils;

import com.app.pixstory.core.dialog.caption.AddTitle;

import com.app.pixstory.data.model.PageDetailResponse;
import com.app.pixstory.data.model.upload.Data;
import com.app.pixstory.databinding.ActivityCreateStoryFilterBinding;

import com.app.pixstory.ui.dashboard.upload.model.CreateStoryFilterViewModel;
import com.app.pixstory.ui.interests.Interests;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class CreatePageTitleActivity extends BaseActivity<ActivityCreateStoryFilterBinding, CreateStoryFilterViewModel> {
    private ActivityCreateStoryFilterBinding binding;
    private CreateStoryFilterViewModel viewModel;
    private ArrayList<Data> data;
    int position = 0;
    private String type;
    private PageDetailResponse.Data pageData;

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
        toolbar(binding.layoutToolbar.toolbar, binding.layoutToolbar.toolbarTitle, R.string.filter);
        initialization();
        clickListener();

    }

    /*private void initialization() {
        data = new ArrayList<>();
        Bundle bundle = getIntent().getBundleExtra(Constants.KEY_DEFAULT_ACTIVITY_BUNDLE);
        if (bundle != null) {
            data = bundle.getParcelableArrayList("key");
            position = bundle.getInt("position");
            BindingUtils.setStringPhotos(binding.banner, data.get(position).getPath(), binding.progressBar);
            setupCreatePage();
        }
    }*/

    private void initialization() {
        data = new ArrayList<>();
        Bundle bundle = getIntent().getBundleExtra(Constants.KEY_DEFAULT_ACTIVITY_BUNDLE);
        if (bundle != null) {
            type = bundle.getString("type");
            if (type != null && type.equalsIgnoreCase(Constants.PAGE_CREATE)) {
                data = bundle.getParcelableArrayList("key");
                position = bundle.getInt("position");
                BindingUtils.setStringPhotos(binding.banner, data.get(position).getPath(), binding.progressBar);
                setupCreatePage();
            } else {

                pageData = Utils.getGsonParser().fromJson(String.valueOf(bundle.get("data")), PageDetailResponse.Data.class);

                BindingUtils.setStringPhotos(binding.banner, Objects.requireNonNull(pageData).getPage().getCoverImgPath(), binding.progressBar);

                binding.addCaptiontxt.setVisibility(View.VISIBLE);
                binding.photoCredits.setVisibility(View.VISIBLE);

                binding.addCaptiontxt.setText(pageData.getPage().getTitle());
                binding.photoCredits.setText(pageData.getPage().getAbout());

                setupCreatePage();
            }

        }
    }

    private void setupCreatePage() {
        if (type.equalsIgnoreCase("create"))
            binding.addCaption.setText(R.string.add_title);
        else
            binding.addCaption.setText("Update Title");
        binding.cbTitle.setVisibility(View.GONE);
        binding.layoutToolbar.toolbarTitle.setVisibility(View.GONE);
    }


    /*private void setupCreatePage() {
        binding.addCaption.setText(R.string.add_title);
        binding.cbTitle.setVisibility(View.GONE);
        binding.layoutToolbar.toolbarTitle.setVisibility(View.GONE);
    }*/

    private void clickListener() {
        binding.addCaption.setOnClickListener(this::onClick);
        binding.addInterest.setOnClickListener(this::onClick);
        binding.ok.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        if (view.getId() == R.id.addCaption) {
            AddTitle.addTitle(this, position, binding.addCaptiontxt, binding.photoCredits, type);
        } else if (view.getId() == R.id.addInterest) {
            startActivity(Interests.class);
        } else if (view.getId() == R.id.ok) {
            dataList();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            dataList();
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        dataList();
        super.onBackPressed();
    }

    private void dataList() {
        String addTitleStr = binding.addCaptiontxt.getText().toString().trim();
        String pageAboutStr = binding.photoCredits.getText().toString().trim();

        if (addTitleStr.isEmpty()) {
            showToast("Please add story title");
            return;
        }

        if (pageAboutStr.isEmpty()) {
            showToast("Please add page description");
            return;
        }

        if (type.equalsIgnoreCase("create")) {
            if (Interests.interestsId == null || Interests.interestsId.size() == 0) {
                showToast("Please add interest for story");
                return;
            } else {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("key", data);
                bundle.putInt("position", position);
                bundle.putString("page_title", addTitleStr);
                bundle.putString("page_about", pageAboutStr);
                bundle.putString("type", type);
                startUploadActivity(CreatePageActivity.class, bundle);
                finish();
            }
        } else {
            pageData.getPage().setTitle(addTitleStr);
            pageData.getPage().setAbout(pageAboutStr);
            Bundle bundle = new Bundle();
            bundle.putString("data", Utils.getGsonParser().toJson(pageData));
            bundle.putString("type", type);
            startUploadActivity(CreatePageActivity.class, bundle);
            finish();
        }
    }

   /* private void dataList() {
        String addTitleStr = binding.addCaptiontxt.getText().toString().trim();
        String pageAboutStr = binding.photoCredits.getText().toString().trim();

        if (addTitleStr.equals("")) {
            showToast("Please add story title");
        } else if ( pageAboutStr.equals("")){
            showToast("Please add page description");
        } else if (Interests.interestsId == null){
            showToast("Please add interest for story");
        } else if (Interests.interestsId.size() > 0){
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("key", data);
            bundle.putInt("position", position);
            bundle.putString("page_title", addTitleStr);
            bundle.putString("page_about", pageAboutStr);
            startUploadActivity(CreatePageActivity.class, bundle);
            finish();
        } else {
            showToast("Please add interest for story");
        }

    }*/
}
