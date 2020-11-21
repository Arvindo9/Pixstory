package com.app.pixstory.ui.dashboard.upload.activity.photos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.data.model.upload.Data;
import com.app.pixstory.data.remote.FileUploader;
import com.app.pixstory.databinding.ActivityCameraGalleryListBinding;
import com.app.pixstory.ui.dashboard.upload.activity.story.ReOrderActivity;
import com.app.pixstory.ui.dashboard.upload.model.CameraGalleryViewModel;
import com.app.pixstory.ui.dashboard.upload.navigator.CameraGalleryListNavigator;
import com.app.pixstory.utils.FileCompressor;
import com.app.pixstory.utils.GalleryAdapter;
import com.app.pixstory.utils.Utils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static androidx.core.content.FileProvider.getUriForFile;
import static com.app.pixstory.utils.Constants.CAMERA_GALLERY_LIST;
import static com.app.pixstory.utils.Constants.FROM;
import static com.app.pixstory.utils.Constants.IS_TYPE;

/**
 * Created by Kamlesh Yadav on 16-04-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class CameraGalleryListActivity extends BaseActivity<ActivityCameraGalleryListBinding, CameraGalleryViewModel> implements CameraGalleryListNavigator {
    private com.app.pixstory.databinding.ActivityCameraGalleryListBinding binding;
    private CameraGalleryViewModel viewModel;
    private ArrayList<Uri> imageUri;
    private ArrayList<Data> uploadArray;
    private static final int REQUEST_CAMERA = 100;
    private String fileName;
    ArrayList<String> files = new ArrayList<>();
    ArrayList<File> fileArrayList;
    private ProgressDialog pDialog;
    String token = "";
    String success = "false";
    String response = "";
    private FileCompressor mCompressor;
    private String IS_IMAGE_FROM = "";

    @Override
    public int getLayout() {
        return R.layout.activity_camera_gallery_list;
    }

    @Override
    protected void getBinding(ActivityCameraGalleryListBinding binding) {

        this.binding = binding;
    }

    @Override
    protected void getViewModel(CameraGalleryViewModel viewModel) {

        this.viewModel = viewModel;
    }

    @Override
    protected Class<CameraGalleryViewModel> setViewModel() {
        return CameraGalleryViewModel.class;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void init() {
        toolbar(binding.layoutToolbar.toolbar, binding.layoutToolbar.toolbarTitle, R.string.select_images);
        imageUri = new ArrayList<>();
        uploadArray = new ArrayList<>();
        pDialog = new ProgressDialog(this);
        viewModel.setNavigator(this);
        mCompressor = new FileCompressor(this);
        token = "Bearer" + viewModel.getToken();
        initialData();
        clickListener();

    }

    private void clickListener() {
        binding.addMore.setOnClickListener(this::onClick);
        binding.upload.setOnClickListener(this::onClick);
        binding.layoutToolbar.cancel.setOnClickListener(this::onClick);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void onClick(View view) {
        if (view.getId() == R.id.add_more) {
            addMore();
        } else if (view.getId() == R.id.upload) {
            uploadPhotos();
        } else if (view.getId() == R.id.cancel){
            finish();
        }
    }

    private void uploadPhotos() {
        if (imageUri != null) {
            if (imageUri.size() == 0) {
                Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
            } else if (imageUri.size() > 12) {
                Toast.makeText(this, "Max image size 12", Toast.LENGTH_SHORT).show();
            } else {
                if (IS_IMAGE_FROM.equalsIgnoreCase("camera")) {
                    if (fileArrayList != null) {
                        if (fileArrayList.size() > 0) {
                            uploadCameraFiles(fileArrayList);
                        }
                    }
                } else if (IS_IMAGE_FROM.equals("gallery")) {
                    files = new ArrayList<>();
                    for (int i = 0; i < imageUri.size(); i++) {
                        getImageFilePath(imageUri.get(i));
                    }
                    if (files.size() > 0) {
                        uploadGalleryFiles(files);
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void addMore() {
        Intent intent = getIntent();
        String image_str = intent.getStringExtra("image");
        if (image_str != null) {
            if (image_str.equals("gallery")) {
                selectFromGallery();
            } else if (image_str.equals("camera")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermission();
                } else {
                    pickImageFromCamera();
                }

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initialData() {
        Intent intent = getIntent();
        String image_str = intent.getStringExtra("image");
        if (image_str != null) {
            if (image_str.equals("gallery")) {
                IS_IMAGE_FROM = "gallery";
                selectFromGallery();
            } else if (image_str.equals("camera")) {
                IS_IMAGE_FROM = "camera";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermission();
                } else {
                    pickImageFromCamera();
                }

            }
        }
    }

    private void pickImageFromCamera() {
        fileName = System.currentTimeMillis() + ".jpg";
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getCacheImagePath(fileName));
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_CAMERA);
        }
    }

    private Uri getCacheImagePath(String fileName) {
        File path = new File(getExternalCacheDir(), "camera");
        if (!path.exists())
            path.mkdirs();
        File image = new File(path, fileName);
        return getUriForFile(CameraGalleryListActivity.this, getPackageName() + ".provider", image);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void selectFromGallery() {
        if (ContextCompat.checkSelfPermission(CameraGalleryListActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CameraGalleryListActivity.this,
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
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
                    imageUri.add(mImageUri);
                    Collections.reverse(imageUri);
                    itemLists(imageUri);
                } else {
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                        itemSize(count, data);
                    }
                }
            } else if (requestCode == REQUEST_CAMERA && resultCode != 0) {
                Uri uri = getCacheImagePath(fileName);
                CropImage.activity(uri).start(this);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri uri = result.getUri();
                    imageUri.add(uri);
                    Collections.reverse(imageUri);
                    itemLists(imageUri);
                    setCameraImage();
                }
            }
        } catch (Exception e) {
            finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setCameraImage() {
        fileArrayList = new ArrayList<>();
        try {
            for (int i = 0; i < imageUri.size(); i++) {
                File mPhotoFile = mCompressor.compressToFile(new File(Objects.requireNonNull(Utils.getPath(this, imageUri.get(i)))));
                fileArrayList.add(mPhotoFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void itemSize(int count, Intent data) {
        if (count > 12) {
            Toast.makeText(this, "Max image size 12", Toast.LENGTH_SHORT).show();
            finish();
        } else if (count == 0) {
            Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < count; i++) {
                Uri uri = data.getClipData().getItemAt(i).getUri();
                imageUri.add(uri);
                Collections.reverse(imageUri);
                itemLists(imageUri);
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
            cursor.close();
        }

    }

    public void uploadCameraFiles(ArrayList<File> files) {
        File[] filesToUpload = new File[files.size()];
        for (int i = 0; i < files.size(); i++) {
            filesToUpload[i] = files.get(i);
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
                                Toast.makeText(CameraGalleryListActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    } else {
                        Toast.makeText(CameraGalleryListActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("key", uploadArray);
                bundle.putInt("position", 0);
                bundle.putString(FROM, CAMERA_GALLERY_LIST);
                startUploadActivity(ReOrderActivity.class, bundle);
            }

            @Override
            public void onProgressUpdate(int currentpercent, int totalpercent, int filenumber) {
                updateProgress(totalpercent, "Uploading file " + filenumber, "");
                Log.e("Progress Status", currentpercent + " " + totalpercent + " " + filenumber);
            }
        });
    }


    public void uploadGalleryFiles(ArrayList<String> files) {
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
                                Toast.makeText(CameraGalleryListActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    } else {
                        Toast.makeText(CameraGalleryListActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("key", uploadArray);
                bundle.putInt("position", 0);
                bundle.putString(FROM, CAMERA_GALLERY_LIST);
                startUploadActivity(ReOrderActivity.class, bundle);
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

    public void itemLists(ArrayList<Uri> galleryListModel) {
        int count = galleryListModel.size();
        binding.layoutToolbar.toolbarTitle.setText(count + " " + getResources().getString(R.string.photos_selected));
        final GalleryAdapter adapter = new GalleryAdapter(this, galleryListModel, binding.layoutToolbar.toolbarTitle);
        binding.grid.setAdapter(adapter);
    }

    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            pickImageFromCamera();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        showToast("Error occurred " + error.toString());
                    }
                })
                .check();
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
}
