package com.app.pixstory.ui.dashboard.upload.activity.story;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.core.dialog.universal_search.UniversalSearch;
import com.app.pixstory.data.model.interest.Interest;
import com.app.pixstory.data.model.story.StoryData;
import com.app.pixstory.data.model.upload.Data;
import com.app.pixstory.data.model.upload_details.DetailData;
import com.app.pixstory.data.remote.FileUploader;
import com.app.pixstory.databinding.ActivityUploadBinding;
import com.app.pixstory.ui.dashboard.upload.adapter.SelectImagesAdapter;
import com.app.pixstory.ui.dashboard.upload.model.MediaModel;
import com.app.pixstory.ui.dashboard.upload.model.UploadViewModel;
import com.app.pixstory.ui.dashboard.upload.navigator.UploadNavigator;
import com.app.pixstory.utils.PaginationScrollListener;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

import static com.app.pixstory.utils.Constants.FAV;
import static com.app.pixstory.utils.Constants.UN_FAV;
import static com.app.pixstory.utils.Constants.USER_ID;
import static com.app.pixstory.utils.Constants.USER_TYPE;

public class UploadActivity extends BaseActivity<ActivityUploadBinding, UploadViewModel> implements
        UploadNavigator, SwipeRefreshLayout.OnRefreshListener {

    private com.app.pixstory.databinding.ActivityUploadBinding binding;
    private UploadViewModel viewModel;
    private ArrayList<Data> uploadArray, getUploadArray, unFavArray, favArray;
    ArrayList<String> files = new ArrayList<>();
    private ProgressDialog pDialog;
    String success = "false";
    String response = "";
    String token = "";
    private boolean isFavDone = false;
    private boolean isFav = false;
    private boolean isUnFav = false;

    private static final int PAGE_START = 1;
    // Un Favourites photo
    private int TOTAL_PHOTO = 15;
    private int TOTAL_PHOTO_COUNT = 1;
    private int currentPhotoUnfav = PAGE_START;
    private boolean isLoadingPhoto = false;
    private boolean isLastPhoto = false;
    // Favourite photo
    private int TOTAL_FAV_PHOTO_COUNT = 1;
    private int currentPhotoFav = PAGE_START;
    private boolean isLoadingFavPhoto = false;
    private boolean isLastFavPhoto = false;
    private GridLayoutManager gridLayoutManager;
    private GridLayoutManager gridFavLayoutManager;
    private SelectImagesAdapter selectImagesAdapter;
    private SelectImagesAdapter selectFavImagesAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_upload;
    }

    @Override
    protected void getBinding(ActivityUploadBinding binding) {
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
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        viewModel.setNavigator(this);
        token = "Bearer" + viewModel.getToken();
        uploadArray = new ArrayList<>();
        getUploadArray = new ArrayList<>();
        unFavArray = new ArrayList<>();
        favArray = new ArrayList<>();
        pDialog = new ProgressDialog(this);
        toolbar(binding.layoutToolbar.toolbar, binding.layoutToolbar.toolbarTitle, R.string.select_images);
        clickListener();

        viewModel.getRefreshing().observe(this, o -> {
            if ((Boolean) o) {
                binding.swipeRefreshLayout.setRefreshing(true);
            } else {
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });

        unFavAdapter();
        loadFirstPageUnFav();


    }

    private void loadFirstPageUnFav() {
        binding.mainProgress.setVisibility(View.VISIBLE);
        viewModel.yourPhotos(USER_TYPE, UN_FAV, USER_ID, currentPhotoUnfav, TOTAL_PHOTO);
    }

    private void loadNextPageUnFav() {
        binding.mainProgress.setVisibility(View.VISIBLE);
        viewModel.yourPhotos(USER_TYPE, UN_FAV, USER_ID, currentPhotoUnfav, TOTAL_PHOTO);
    }

    private void unFavAdapter() {
        selectImagesAdapter = new SelectImagesAdapter(new ArrayList<>());
        binding.unFavRecyclerView.setHasFixedSize(true);
        binding.unFavRecyclerView.setAdapter(selectImagesAdapter);

        selectImagesAdapter.setListener(this::onUnFavAdapterItem);
        gridLayoutManager = new GridLayoutManager(this, 3);
        binding.unFavRecyclerView.setLayoutManager(gridLayoutManager);
        binding.unFavRecyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoadingPhoto = true;
                currentPhotoUnfav += 1;
                loadNextPageUnFav();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PHOTO_COUNT;
            }

            @Override
            public boolean isLastPage() {
                return isLastPhoto;
            }

            @Override
            public boolean isLoading() {
                return isLoadingPhoto;
            }
        });

    }

    private void onUnFavAdapterItem(Object model, String id, int position) {
        if (model instanceof List) {
            unFavArray = (ArrayList<Data>) model;
        }
    }


    private void favAdapter() {
        selectFavImagesAdapter = new SelectImagesAdapter(new ArrayList<>());
        binding.favRecyclerView.setHasFixedSize(true);
        binding.favRecyclerView.setAdapter(selectFavImagesAdapter);
        selectFavImagesAdapter.setListener(this::onFavAdapterItem);
        gridFavLayoutManager = new GridLayoutManager(this, 3);
        binding.favRecyclerView.setLayoutManager(gridFavLayoutManager);

        binding.favRecyclerView.addOnScrollListener(new PaginationScrollListener(gridFavLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoadingFavPhoto = true;
                currentPhotoFav += 1;
                loadNextPhotoFav();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_FAV_PHOTO_COUNT;
            }

            @Override
            public boolean isLastPage() {
                return isLastFavPhoto;
            }

            @Override
            public boolean isLoading() {
                return isLoadingFavPhoto;
            }
        });

    }

    private void loadNextPhotoFav() {
        binding.mainProgress.setVisibility(View.VISIBLE);
        viewModel.yourPhotos(USER_TYPE, FAV, USER_ID, currentPhotoFav, TOTAL_PHOTO);
    }

    private void onFavAdapterItem(Object model, String id, int position) {
        if (model instanceof List) {
            favArray = (ArrayList<Data>) model;
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
    public void onClick(View v) {
        if (v.getId() == R.id.search) {
            UniversalSearch.universalSearch(this);
        } else if (v.getId() == R.id.cancel) {
            finish();
        } else if (v.getId() == R.id.your_photos) {
            binding.createStoryFrom.yourPhotos.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
            binding.createStoryFrom.favourites.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.createStoryFrom.phoneGallery.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            isUnFav = true;
            isFav = false;
            binding.unFavRecyclerView.setVisibility(View.VISIBLE);
            binding.favRecyclerView.setVisibility(View.GONE);
        } else if (v.getId() == R.id.favourites) {
            binding.createStoryFrom.yourPhotos.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.createStoryFrom.favourites.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_white_black_25));
            binding.createStoryFrom.phoneGallery.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_neon_black_25));
            binding.unFavRecyclerView.setVisibility(View.GONE);
            binding.favRecyclerView.setVisibility(View.VISIBLE);
            isFav = true;
            isUnFav = false;
            if (!isFavDone) {
                favAdapter();
                binding.mainProgress.setVisibility(View.VISIBLE);
                viewModel.yourPhotos(USER_TYPE, "fav", USER_ID, 0, TOTAL_PHOTO);
            }
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
        if (ContextCompat.checkSelfPermission(UploadActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UploadActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        }
    }

    private void next() {
        getUploadArray = new ArrayList<>();
        uploadArray = new ArrayList<>();
        getUploadArray.addAll(unFavArray);
        getUploadArray.addAll(favArray);
        if (getUploadArray.size() > 0)
            for (int i = 0; i < getUploadArray.size(); i++) {
                if (getUploadArray.get(i).isChecked) {
                    uploadArray.add(getUploadArray.get(i));
                }
            }


        LinkedHashSet<Data> dataHashSet = new LinkedHashSet<>();
        dataHashSet.addAll(uploadArray);
        uploadArray.clear();
        uploadArray.addAll(dataHashSet);
        if (uploadArray.size() < 1) {
            Toast.makeText(this, "Please select an Image", Toast.LENGTH_SHORT).show();

        } else if (uploadArray.size() > 12) {
            Toast.makeText(this, "Max. 12 Image allowed", Toast.LENGTH_SHORT).show();
        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("key", uploadArray);
            bundle.putInt("position", 0);
            startUploadActivity(ReOrderActivity.class, bundle);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && null != data) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            if (data.getData() != null) {

                Uri mImageUri = data.getData();
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                try {
                    getApplication().getContentResolver().takePersistableUriPermission(mImageUri, takeFlags);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                Cursor cursor = getContentResolver().query(mImageUri,
                        filePathColumn, null, null, null);
                Objects.requireNonNull(cursor).moveToFirst();
                cursor.close();
                files = new ArrayList<>();
                getImageFilePath(mImageUri);
                if (files.size() > 0) {
                    uploadFiles();
                }
            } else {

                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    itemLists(count, data);
                }
            }
        }
    }

    public void itemLists(int count, Intent data) {
        files = new ArrayList<>();
        if (count > 12) {
            Toast.makeText(this, "Max image size 12", Toast.LENGTH_SHORT).show();
        } else if (count == 0) {
            Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < count; i++) {
                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                getImageFilePath(imageUri);
            }
            if (files.size() > 0) {
                uploadFiles();
            }

        }
    }

    public void getImageFilePath(Uri uri) {
        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];
        Cursor cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            files.add(imagePath);
            // compressImage(imagePath);
            cursor.close();
        }

    }

    public void uploadFiles() {
        File[] filesToUpload = new File[files.size()];
        for (int i = 0; i < files.size(); i++) {
            filesToUpload[i] = new File(files.get(i));
        }
        String unique_key = String.valueOf(System.currentTimeMillis());
        showProgress("Uploading image ...");
        FileUploader fileUploader = new FileUploader();
        fileUploader.uploadFiles(unique_key, "image", filesToUpload, token, new FileUploader.FileUploaderCallback() {
            @Override
            public void onError() {
                hideProgress();
            }

            @Override
            public void onFinish(String[] responses) {
                hideProgress();
                for (int i = 0; i < responses.length; i++) {
                    response = responses[i];
                    Log.e("RESPONSE " + i, responses[i]);
                    if (!response.equals("")) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            success = obj.optString("success");
                            String message = obj.optString("message");
                            String data = obj.optString("data");
                            if (data != "null") {
                                JSONObject jsonObject = new JSONObject(data);
                                String id = jsonObject.optString("id");
                                String path = jsonObject.optString("path");
                                String thumbnail = jsonObject.optString("thumbnail");

                                Data data1 = new Data();
                                data1.setId(Integer.parseInt(id));
                                data1.setPath(path);
                                data1.setThumbnail(thumbnail);
                                uploadArray.add(data1);
                            } else {
                                Toast.makeText(UploadActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    } else {
                        Toast.makeText(UploadActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }

                if (!response.equals("")) {
                    if (success.equals("true")) {
                           /* Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("key", uploadArray);
                            bundle.putInt("position", 0);
                            startUploadActivity(ReOrderActivity.class, bundle);*/

                        getUploadArray = new ArrayList<>();
                        getUploadArray.addAll(unFavArray);
                        getUploadArray.addAll(favArray);
                        for (int j = 0; j < getUploadArray.size(); j++) {
                            if (getUploadArray.get(j).isChecked) {
                                if (getUploadArray.size() > 0) {
                                    uploadArray.add(getUploadArray.get(j));
                                }
                            }
                        }

                        LinkedHashSet<Data> dataHashSet = new LinkedHashSet<>();
                        dataHashSet.addAll(uploadArray);
                        uploadArray.clear();
                        uploadArray.addAll(dataHashSet);
                        Bundle bundle = new Bundle();
                        if (uploadArray.size() > 12) {
                            Toast.makeText(UploadActivity.this, "Max image size 12", Toast.LENGTH_SHORT).show();
                        } else {
                            bundle.putParcelableArrayList("key", uploadArray);
                            bundle.putInt("position", 0);
                            startUploadActivity(ReOrderActivity.class, bundle);
                        }

                    }
                }
            }

            @Override
            public void onProgressUpdate(int currentpercent, int totalpercent, int filenumber) {
                updateProgress(totalpercent, "Uploading file " + filenumber, "");
                Log.e("Progress Status", currentpercent + " " + totalpercent + " " + filenumber);
            }
        });
    }

    public void updateProgress(int val, String title, String msg) {
        pDialog.setTitle(title);
        pDialog.setMessage(msg);
        pDialog.setProgress(val);
    }

    public void showProgress(String str) {
        try {
            pDialog.setCancelable(false);
            pDialog.setTitle("Please wait");
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setMax(100); // Progress Dialog Max Value
            pDialog.setMessage(str);
            if (pDialog.isShowing())
                pDialog.dismiss();
            pDialog.show();
        } catch (Exception e) {

        }
    }

    public void hideProgress() {
        try {
            if (pDialog.isShowing())
                pDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
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

    // compress image size
    public String compressImage(String imageUri) {

        // String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imageUri, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(imageUri, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(imageUri);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = imageUri;
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        files.add(filename);

        return filename;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
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
    public void onMyPhotoResponse(boolean b, List<Data> data, String type) {
        if (b) {
            binding.mainProgress.setVisibility(View.GONE);
            if (type.equals(FAV)) {
                isFavDone = true;
                if (currentPhotoFav == 1) {
                    selectFavImagesAdapter.addItems(data);
                    if (currentPhotoFav <= TOTAL_FAV_PHOTO_COUNT)
                        selectFavImagesAdapter.addLoadingFooter();
                    else isLastFavPhoto = true;
                } else {
                    selectFavImagesAdapter.removeLoadingFooter();
                    isLoadingFavPhoto = false;
                    selectFavImagesAdapter.addItems(data);
                    if (currentPhotoFav != TOTAL_FAV_PHOTO_COUNT)
                        selectFavImagesAdapter.addLoadingFooter();
                    else isLoadingFavPhoto = true;
                }
            } else {
                if (currentPhotoUnfav == 1) {
                    selectImagesAdapter.addItems(data);
                    if (currentPhotoUnfav <= TOTAL_PHOTO_COUNT)
                        selectImagesAdapter.addLoadingFooter();
                    else isLastPhoto = true;
                } else {
                    selectImagesAdapter.removeLoadingFooter();
                    isLoadingPhoto = false;
                    selectImagesAdapter.addItems(data);
                    if (currentPhotoUnfav != TOTAL_PHOTO_COUNT)
                        selectImagesAdapter.addLoadingFooter();
                    else isLastPhoto = true;
                }
            }
        } else {
            binding.mainProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetailResponse(boolean b, DetailData detailData) {

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

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        if (isNetworkAvailable()) {
            if (isFav) {
                binding.mainProgress.setVisibility(View.VISIBLE);
                favArray = new ArrayList<>();
                viewModel.yourPhotos(USER_TYPE, FAV, USER_ID, currentPhotoFav, TOTAL_PHOTO);
            } else if (isUnFav) {
                unFavAdapter();
                TOTAL_PHOTO_COUNT = 1;
                currentPhotoUnfav = PAGE_START;
                isLoadingPhoto = false;
                isLastPhoto = false;
                binding.mainProgress.setVisibility(View.VISIBLE);
                unFavArray = new ArrayList<>();
                viewModel.yourPhotos(USER_TYPE, UN_FAV, USER_ID, currentPhotoUnfav, TOTAL_PHOTO);
            } else {
                unFavAdapter();
                TOTAL_PHOTO_COUNT = 1;
                currentPhotoUnfav = PAGE_START;
                isLoadingPhoto = false;
                isLastPhoto = false;
                binding.mainProgress.setVisibility(View.VISIBLE);
                unFavArray = new ArrayList<>();
                viewModel.yourPhotos(USER_TYPE, UN_FAV, USER_ID, currentPhotoUnfav, TOTAL_PHOTO);
            }

        } else {
            binding.swipeRefreshLayout.setRefreshing(false);
        }
    }
}
