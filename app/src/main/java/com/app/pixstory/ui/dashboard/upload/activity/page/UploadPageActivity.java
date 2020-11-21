package com.app.pixstory.ui.dashboard.upload.activity.page;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.core.dialog.universal_search.UniversalSearch;
import com.app.pixstory.data.model.interest.Interest;
import com.app.pixstory.data.model.story.StoryData;
import com.app.pixstory.data.model.upload.Data;
import com.app.pixstory.data.model.upload_details.DetailData;
import com.app.pixstory.databinding.ActivityUploadPageBinding;
import com.app.pixstory.ui.dashboard.upload.model.MediaModel;
import com.app.pixstory.ui.dashboard.upload.model.UploadViewModel;
import com.app.pixstory.ui.dashboard.upload.navigator.UploadNavigator;
import com.app.pixstory.ui.dashboard.upload.adapter.SelectImagesAdapter;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.util.FileProviders;
import com.app.pixstory.utils.util.Logger;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;
import static com.app.pixstory.utils.Constants.USER_ID;
import static com.app.pixstory.utils.Constants.USER_TYPE;

/**
 * Author       : Arvindo Mondal
 * Created date : 13-08-2019
 */
public class UploadPageActivity extends BaseActivity<ActivityUploadPageBinding, UploadViewModel> implements
        UploadNavigator {
    private static final String TAG = UploadPageActivity.class.getSimpleName();
    private com.app.pixstory.databinding.ActivityUploadPageBinding binding;
    private UploadViewModel viewModel;
    private ArrayList<Data> uploadArray, getUploadArray;

    @Override
    public int getLayout() {
        return R.layout.activity_upload_page;
    }

    @Override
    protected void getBinding(ActivityUploadPageBinding binding) {
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
        uploadArray = new ArrayList<>();
        getUploadArray = new ArrayList<>();
        toolbar(binding.layoutToolbar.toolbar, binding.layoutToolbar.toolbarTitle, R.string.select_images);
        clickListener();
        viewModel.getLoading().observe(this, o -> {
            if ((Boolean) o) {
                showSimmerLoader(DEFAULT_LOADER);
            } else {
                hideSimmerLoader();
            }
        });

        viewModel.yourPhotos(USER_TYPE, "unfav", USER_ID, 0, 15);
    }

    private void initAdapter(List<Data> data) {
        SelectImagesAdapter youPhotosAdapter = new SelectImagesAdapter(new ArrayList<>());
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(youPhotosAdapter);
        youPhotosAdapter.setListener(this::onAdapterItem);
        youPhotosAdapter.addItems(data);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void onAdapterItem(Object model, String id, int position) {
        if (model instanceof List) {
            getUploadArray = (ArrayList<Data>) model;
        }
    }

    private void clickListener() {
        binding.layoutToolbar.search.setOnClickListener(this::onClick);
        binding.layoutToolbar.cancel.setOnClickListener(this::onClick);
        binding.createStoryFrom.yourPhotos.setOnClickListener(this::onClick);
        binding.createStoryFrom.favourites.setOnClickListener(this::onClick);
        binding.createStoryFrom.phoneGallery.setOnClickListener(this::onClick);
        binding.next.setOnClickListener(this::onClick);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onClick(@NotNull View v) {
        if (v.getId() == R.id.search) {
            UniversalSearch.universalSearch(this);
        } else if (v.getId() == R.id.cancel) {
            finish();
        } else if (v.getId() == R.id.your_photos) {
            binding.createStoryFrom.yourPhotos.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
            binding.createStoryFrom.favourites.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.createStoryFrom.phoneGallery.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            viewModel.yourPhotos(USER_TYPE, "unfav", USER_ID, 0, 15);
        } else if (v.getId() == R.id.favourites) {
            binding.createStoryFrom.yourPhotos.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.createStoryFrom.favourites.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
            binding.createStoryFrom.phoneGallery.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            viewModel.yourPhotos(USER_TYPE, "fav", USER_ID, 0, 15);
        } else if (v.getId() == R.id.phone_gallery) {
            binding.createStoryFrom.yourPhotos.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.createStoryFrom.favourites.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.createStoryFrom.phoneGallery.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
            selectFromGallery();
        } else if (v.getId() == R.id.next) {
            next();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void selectFromGallery() {
        if (ContextCompat.checkSelfPermission(UploadPageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UploadPageActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                    Constants.ACTION_GALLERY_SINGLE_IMAGE);
        }
    }

    private void next() {
        uploadArray.clear();
        for (int i = 0; i < getUploadArray.size(); i++) {
            if (getUploadArray.get(i).isChecked) {
                getUploadArray.size();
                uploadArray.add(getUploadArray.get(i));
            }
        }

        if (uploadArray.size() != 1) {
            showToast(R.string.please_select_single_image);
            return;
        }

        LinkedHashSet<Data> dataHashSet = new LinkedHashSet<>(uploadArray);
        uploadArray.clear();
        uploadArray.addAll(dataHashSet);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("key", uploadArray);
        bundle.putInt("position", 0);
        bundle.putString("type", Constants.PAGE_CREATE);
        startUploadActivity(CreatePageTitleActivity.class, bundle);

       /* startActivity(CreatePageTitleActivity.class,
                Bundles.getInstance().setCreateStoryFilterOld(Constants.STORY_TYPE_CREATE_PAGE,
                        Constants.STORY_TYPE_CREATE_PAGE_OLD_UPLOAD, uploadArray, 0));
        finish();*/
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.ACTION_GALLERY_SINGLE_IMAGE:
                    if (data != null && data.getData() != null) {
                        onReceiveImage(data.getData());
/*
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getBaseActivity()
                                    .getContentResolver(), data.getData());
                            model.setBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        */
                    }
                    break;
            }
        }
    }

    @SuppressLint("NewApi")
    private void onReceiveImage(Uri uri) {
        String filePath;
        try {
            filePath = FileProviders.getFilePath(this, uri);
            if (filePath != null) {
                try {
                    //set image if require
//                    binding.profileInfo.profileImage.setImageBitmap(BitmapFactory.decodeFile(filePath));

                    File file = new File(filePath);
                    if (file.isFile()) {
                        Logger.e("file exist");
                        Logger.e("file name" + file.getName());
                        Logger.e("file size " + file.length());

                        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                        Logger.e("file extension" + extension);

                        //TODO compression later
                        //Compress file toreduce file size
/*
                        File newFile = MediaUtil.compressImage(file, filePath);
                        if(newFile.isFile() && newFile.exists()){
                            Logger.e("newFile exist");
                            Logger.e("newFile name " + newFile.getName());
                            Logger.e("newFile size " + newFile.length());
                        }
*/

                        try {
//                            onSuccessFileReceive(filePath, newFile, newFile.getName(), extension);
                            onSuccessFileReceive(filePath, file, file.getName(), extension);
                        } catch (Exception e) {
                            e.printStackTrace();
//                            onSuccessFileReceive(filePath, file, file.getName(), extension);
                        }
                    } else {
                        Logger.e("file not exist");
                        showToast(R.string.file_not_found);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    showToast(R.string.file_not_found);
                }
            }
        } catch (Exception ignore) {
            showToast(R.string.file_not_found);
        }
    }

    //Success receive---------------
    private void onSuccessFileReceive(String filePath, File file, String fileName, String extension) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        MediaModel mediaModel = new MediaModel(TAG, bitmap, file, fileName, extension);
        mediaModel.setActionType(Constants.ACTION_GALLERY_SINGLE_IMAGE);
//        viewModel.setModel(mediaModel);
        viewModel.uploadFileToServer(mediaModel);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void message(int message) {
        showToast(message);
    }

    @Override
    public void message(String message) {
        showToast(message);
    }

    @Override
    public void onFileUploadSuccess(MediaModel model, Data uploadModel) {
        //TODO on profile upload
        if (uploadModel != null) {
           /* startActivity(CreatePageTitleActivity.class,
                    Bundles.getInstance().setCreateStoryFilter(Constants.STORY_TYPE_CREATE_PAGE,
                            Constants.STORY_TYPE_CREATE_PAGE_NEW_UPLOAD, uploadModel));
            finish();*/
            ArrayList<Data> uploadArrays = new ArrayList<>();
            uploadArrays.add(uploadModel);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("key", uploadArrays);
            bundle.putInt("position", 0);
            bundle.putString("type", Constants.PAGE_CREATE);
            startUploadActivity(CreatePageTitleActivity.class, bundle);

        }
    }

    @Override
    public void onMasterInterest(Boolean success, List<Interest> interest) {

    }

    @Override
    public void onMyPhotoResponse(boolean b, List<Data> data, String type) {
        if (b) {
            initAdapter(data);
        }
    }

    @Override
    public void onDetailResponse(boolean b, DetailData detailData) {

    }

    @Override
    public void onStoryResponse(boolean b, StoryData storyData) {

    }
}
