package com.app.pixstory.ui.dashboard.you;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.core.dialog.delete.Delete;
import com.app.pixstory.data.model.upload.Data;
import com.app.pixstory.databinding.SelectImagesBinding;
import com.app.pixstory.ui.dashboard.upload.activity.story.ReOrderActivity;
import com.app.pixstory.ui.dashboard.upload.adapter.SelectImagesAdapter;
import com.app.pixstory.ui.dashboard.you.model.SelectImagesViewModel;
import com.app.pixstory.ui.dashboard.you.navigator.DeleteImageNavigator;
import com.app.pixstory.utils.Constants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

public class SelectImages extends BaseActivity<SelectImagesBinding, SelectImagesViewModel>
        implements DeleteImageNavigator {
    private SelectImagesBinding binding;
    private SelectImagesViewModel viewModel;
    SelectImagesAdapter youPhotosAdapter;
    public boolean is_selected = false;
    String token = "";
    public ArrayList<Data> dataArrayList;
    public ArrayList<Data> dataSelectedArrayList;
    public static boolean is_deleted = false;

    @Override
    public int getLayout() {
        return R.layout.select_images;
    }

    @Override
    protected void getBinding(SelectImagesBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(SelectImagesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected Class<SelectImagesViewModel> setViewModel() {
        return SelectImagesViewModel.class;
    }

    @Override
    protected void init() {
        viewModel.setNavigator(this);
        token = "Bearer" + viewModel.getToken();
        dataArrayList = new ArrayList<>();
        dataSelectedArrayList = new ArrayList<>();
        toolbar(binding.layoutToolbar.toolbar, binding.layoutToolbar.toolbarTitle, R.string.select_images);
        clickListener();
        initAdapter();
    }

    private void clickListener() {
        binding.createStory.setOnClickListener(this::onClick);
        binding.selectAll.setOnClickListener(this::onClick);
        binding.delete.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        if (view.getId() == R.id.create_story) {
            createStory();
        } else if (view.getId() == R.id.select_all) {
            selectAll();
        } else if (view.getId() == R.id.delete) {
            deleteImages();
        }
    }

    private void createStory() {
        dataSelectedArrayList = new ArrayList<>();
        if (dataArrayList.size() > 0)
            for (int i = 0; i < dataArrayList.size(); i++) {
                if (dataArrayList.get(i).isChecked) {
                    dataSelectedArrayList.add(dataArrayList.get(i));
                }
            }

        LinkedHashSet<Data> dataHashSet = new LinkedHashSet<>();
        dataHashSet.addAll(dataSelectedArrayList);
        dataSelectedArrayList.clear();
        dataSelectedArrayList.addAll(dataHashSet);
        if (dataSelectedArrayList.size() < 1) {
            Toast.makeText(this, "Please select an Image", Toast.LENGTH_SHORT).show();

        } else if (dataSelectedArrayList.size() > 12) {
            Toast.makeText(this, "Max. 12 Image allowed", Toast.LENGTH_SHORT).show();
        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("key", dataSelectedArrayList);
            bundle.putInt("position", 0);
            startUploadActivity(ReOrderActivity.class, bundle);
        }
    }

    private void deleteImages() {
        dataSelectedArrayList = new ArrayList<>();
        if (dataArrayList.size() > 0)
            for (int i = 0; i < dataArrayList.size(); i++) {
                if (dataArrayList.get(i).isChecked) {
                    dataSelectedArrayList.add(dataArrayList.get(i));
                }
            }

        LinkedHashSet<Data> dataHashSet = new LinkedHashSet<>();
        dataHashSet.addAll(dataSelectedArrayList);
        dataSelectedArrayList.clear();
        dataSelectedArrayList.addAll(dataHashSet);

        // image id ------------
        LinkedHashMap<Integer, Integer> imageId = new LinkedHashMap<>();
        for (int i = 0; i < dataSelectedArrayList.size(); i++) {
            imageId.put(dataSelectedArrayList.get(i).getId(), dataSelectedArrayList.get(i).getId());

        }

        int[] image_id = new int[imageId.size()];

        int i = 0;
        for (int key : imageId.keySet()) {
            image_id[i++] = key;
        }
        Delete.deleteItem(this, viewModel, token,image_id);

    }

    private void selectAll() {
        if (!is_selected) {
            is_selected = true;
            binding.selectAll.setText(getResources().getString(R.string.un_select));
            youPhotosAdapter.selectAllImage(true);
        } else {
            is_selected = false;
            binding.selectAll.setText(getResources().getString(R.string.select_all));
            youPhotosAdapter.selectAllImage(false);
        }

    }


    private void initAdapter() {
        Bundle bundle = getIntent().getBundleExtra(Constants.KEY_DEFAULT_ACTIVITY_BUNDLE);
        if (bundle != null) {
            ArrayList<Data> dataArrayList = bundle.getParcelableArrayList("key");
            if (dataArrayList != null) {
                youPhotosAdapter = new SelectImagesAdapter(new ArrayList<>());
                binding.recyclerView.setHasFixedSize(true);
                binding.recyclerView.setAdapter(youPhotosAdapter);

                youPhotosAdapter.setListener(this::onAdapterItem);
                youPhotosAdapter.addItems(dataArrayList);
                binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            }
        }
    }


    private void onAdapterItem(Object data, String tag, int position) {
        if (data instanceof List) {
            dataArrayList = (ArrayList<Data>) data;
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
    public void message(int message) {

    }

    @Override
    public void message(String message) {

    }

    @Override
    public void onImageDeletedResponse(Boolean success, String message) {
        if (success){
            showToast(message);
            is_deleted = true;
            finish();
        }
    }

    @Override
    public void onImageFavouriteResponse(Boolean success, String message) {

    }
}
