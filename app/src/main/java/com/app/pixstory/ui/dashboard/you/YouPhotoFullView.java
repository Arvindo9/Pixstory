package com.app.pixstory.ui.dashboard.you;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.data.model.upload.Data;
import com.app.pixstory.databinding.YouFullPhotoViewBinding;
import com.app.pixstory.ui.dashboard.upload.activity.story.CreateStoryFilterActivity;
import com.app.pixstory.ui.dashboard.you.adapter.YouFullPhotoViewPagerAdapter;
import com.app.pixstory.ui.dashboard.you.model.YouPhotoFullViewModel;
import com.app.pixstory.ui.dashboard.you.navigator.DeleteImageNavigator;
import com.app.pixstory.utils.Constants;

import java.util.ArrayList;

import static com.app.pixstory.utils.Constants.BEARER;
import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;

/**
 * Created by Kamlesh Yadav on 18-04-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class YouPhotoFullView extends BaseActivity<YouFullPhotoViewBinding, YouPhotoFullViewModel>
        implements DeleteImageNavigator {
    private com.app.pixstory.databinding.YouFullPhotoViewBinding binding;
    private YouPhotoFullViewModel viewModel;
    public String token = "";
    YouFullPhotoViewPagerAdapter youFullPhotoViewPagerAdapter;
    int delete_position = 0;
    private ArrayList<Data> data;

    @Override
    public int getLayout() {
        return R.layout.you_full_photo_view;
    }

    @Override
    protected void getBinding(YouFullPhotoViewBinding binding) {

        this.binding = binding;
    }

    @Override
    protected void getViewModel(YouPhotoFullViewModel viewModel) {

        this.viewModel = viewModel;
    }

    @Override
    protected Class<YouPhotoFullViewModel> setViewModel() {
        return YouPhotoFullViewModel.class;
    }

    @Override
    protected void init() {
        viewModel.setNavigator(this);
        token = BEARER + viewModel.getToken();

        itemLists();
        setUP();
    }

    private void setUP() {
        binding.toolbarLayout.close.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        if (view.getId() == R.id.close) {
            finish();
        }
    }

    public void itemLists() {
        Bundle bundle = getIntent().getBundleExtra(Constants.KEY_DEFAULT_ACTIVITY_BUNDLE);
        if (bundle != null) {
            ArrayList<Data> dataArrayList = bundle.getParcelableArrayList("key");
            int position = bundle.getInt("position");
            if (dataArrayList != null) {
                youFullPhotoViewPagerAdapter = new YouFullPhotoViewPagerAdapter(getApplicationContext(), dataArrayList, position, this);
                binding.pager.setAdapter(youFullPhotoViewPagerAdapter);
                binding.pager.setCurrentItem(position);
                youFullPhotoViewPagerAdapter.setDeleteCallback(this::deletePhoto);
                youFullPhotoViewPagerAdapter.setFavouriteCallback(this::favouritePhoto);
                youFullPhotoViewPagerAdapter.setEditCallback(this::editPhoto);
            }
        }
    }

    private void editPhoto(ArrayList<Data> dataArrayList, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("key", dataArrayList);
        bundle.putString("from", "edit");
        bundle.putInt("position", position);
        startUploadActivity(CreateStoryFilterActivity.class, bundle);
    }

    private void favouritePhoto(int photo_id, int is_fav, int position, ArrayList<Data> dataArrayList) {
        if (is_fav == 0){
            viewModel.favouritePhoto(token, 1, photo_id);
        } else {
            viewModel.favouritePhoto(token, 0, photo_id);
        }

    }

    private void deletePhoto(int photo_id, int position, ArrayList<Data> dataArrayList) {
        delete_position = position;
        data = dataArrayList;
        viewModel.deleteSingleImage(token, photo_id);
    }



    @Override
    public void onImageDeletedResponse(Boolean success, String message) {
        if (success) {
            showToast(message);
            SelectImages.is_deleted = true;
            data.remove(delete_position);
            youFullPhotoViewPagerAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onImageFavouriteResponse(Boolean success, String message) {
        if (success){
            showToast(message);
            SelectImages.is_deleted = true;
            youFullPhotoViewPagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void message(int message) {

    }

    @Override
    public void message(String message) {

    }
}
