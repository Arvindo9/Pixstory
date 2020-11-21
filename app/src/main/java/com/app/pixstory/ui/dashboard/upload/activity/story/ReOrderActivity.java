package com.app.pixstory.ui.dashboard.upload.activity.story;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pixstory.BuildConfig;
import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.core.binding.BindingUtils;
import com.app.pixstory.data.model.upload.Data;
import com.app.pixstory.data.remote.FileUploader;
import com.app.pixstory.databinding.ActivityReOrderBinding;
import com.app.pixstory.ui.dashboard.DashboardActivity;
import com.app.pixstory.ui.dashboard.upload.adapter.ReOrderAdapter;
import com.app.pixstory.ui.dashboard.upload.model.UploadViewModel;
import com.app.pixstory.ui.dashboard.you.HomeYouActivity;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.drag_drop.ItemMoveCallback;
import com.app.pixstory.utils.drag_drop.StartDragListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import static com.app.pixstory.utils.Constants.BASE_PATH;
import static com.app.pixstory.utils.Constants.CAMERA_GALLERY_LIST;
import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;
import static com.app.pixstory.utils.Constants.FROM;

public class ReOrderActivity extends BaseActivity<ActivityReOrderBinding, UploadViewModel> implements StartDragListener {
    private com.app.pixstory.databinding.ActivityReOrderBinding binding;
    private UploadViewModel viewModel;
    private ArrayList<Data> data;
    int position = 0;
    Uri banner_uri = null;

    ProgressDialog mProgressDialog;
    private ProgressDialog pDialog;
    String success = "false";
    String response = "";
    URL url;
    AsyncTask mMyTask;
    ArrayList<String> files = new ArrayList<>();
    String from = "";
    ReOrderAdapter reorderAdapter;
    ItemTouchHelper touchHelper;


    @Override
    public int getLayout() {
        return R.layout.activity_re_order;
    }

    @Override
    protected void getBinding(ActivityReOrderBinding binding) {
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
        mProgressDialog = new ProgressDialog(this);
        pDialog = new ProgressDialog(this);
        toolbar(binding.layoutToolbar.toolbar, binding.layoutToolbar.toolbarTitle, R.string.re_order);
        binding.layoutToolbar.search.setVisibility(View.GONE);
        getData();
        clickListener();
    }

    private void getData() {
        Bundle bundle = getIntent().getBundleExtra(Constants.KEY_DEFAULT_ACTIVITY_BUNDLE);
        if (bundle != null) {
            data = bundle.getParcelableArrayList("key");
            position = bundle.getInt("position");
            from = bundle.getString(FROM);
            BindingUtils.setStringPhotos(binding.banner, data.get(0).getPath(), binding.progressBar);
            banner_uri = Uri.parse(BASE_PATH + data.get(0).getPath());
            initAdapter(data);
            try {
                url = new URL(banner_uri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private void initAdapter(ArrayList<Data> data) {
        reorderAdapter = new ReOrderAdapter(data, this);
        binding.recyclerView.setHasFixedSize(true);
        ItemTouchHelper.Callback callback =
                new ItemMoveCallback(reorderAdapter);
         touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(binding.recyclerView);
        binding.recyclerView.setAdapter(reorderAdapter);


        reorderAdapter.setListener(this::onAdapterItem);
        reorderAdapter.setCallback(this::onDataSwap);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void onAdapterItem(Object object, int position, String banner, int id) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("key", data);
            bundle.putString("banner", banner);
            bundle.putInt("id", id);
            bundle.putString("from", "Reorder");
            startUploadActivity(CreateStoryFilterActivity.class, bundle);

    }


    /**
     * @param dataObject type cast to actual model data
     * @param position   destination position
     */
    void onDataSwap(Object dataObject, int position)  {
        if (dataObject instanceof Data && position == 0) {
            //TODO Refresh banner image
            BindingUtils.setStringPhotos(binding.banner, ((Data) dataObject).getPath(), binding.progressBar);
            banner_uri = Uri.parse(BASE_PATH + ((Data) dataObject).getPath());
            try {
                url = new URL(banner_uri.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private void clickListener() {
        binding.next.setOnClickListener(this::onClick);
        binding.crop.setOnClickListener(this::onClick);
        binding.layoutToolbar.cancel.setOnClickListener(this::onClick);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void onClick(View view) {
        if (view.getId() == R.id.next) {
            try {
                proceedForReview();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (view.getId() == R.id.crop) {
            mMyTask = new DownloadTask().execute(url);
        } else if (view.getId() == R.id.cancel) {
            finish();
        }
    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }

    private class DownloadTask extends AsyncTask<URL, Void, Bitmap> {
        protected void onPreExecute() {
            showSimmerLoader(DEFAULT_LOADER);
        }

        protected Bitmap doInBackground(URL... urls) {
            URL url = urls[0];
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                return BitmapFactory.decodeStream(bufferedInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result) {
            // Hide the progress dialog
            hideSimmerLoader();
            if (result != null) {
                bitmapToUriConverter(result);

            } else {
                // Notify user that an error occurred while downloading image
                Toast.makeText(ReOrderActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Uri bitmapToUriConverter(Bitmap mBitmap) {
        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // Calculate inSampleSize
      //      options.inSampleSize = calculateInSampleSize(options, 100, 100);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
          //  Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, 200, 200, true);
            File file = new File(getApplicationContext().getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");
            FileOutputStream out = getApplicationContext().openFileOutput(file.getName(),
                    Context.MODE_PRIVATE);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = file.getAbsolutePath();
            File f = new File(realPath);
            uri = Uri.fromFile(f);

            CropImage.activity(uri)
                    .setAspectRatio(16, 9)
                    .setMinCropWindowSize(1000, 1000)
                    .setAllowFlipping(true)
                    .setRotationDegrees(90)
                    .setGuidelinesThickness(0)
                    .setMinCropWindowSize(375,212)
                    .setMinCropResultSize(375, 212)
                    .start(ReOrderActivity.this);
        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
        return uri;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public void proceedForReview() throws FileNotFoundException {
        if (data.size() > 0) {
            if (data.size() > 12) {
                Toast.makeText(this, "Maximum 12 photos allowed in a single story", Toast.LENGTH_SHORT).show();
            } else {
                final BottomSheetDialog dialog = new BottomSheetDialog(this);
                progressLoader(data, dialog);
                new RequestPostsAsyncTask(this, dialog).execute();
            }
        } else {
            Toast.makeText(this, "Please Add photo", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    String path = resultUri.getPath();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    binding.banner.setImageBitmap(bitmap);
                    new SaveImageTask(bitmap, path).execute(stream.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                files = new ArrayList<>();
                //   getBitmapUri(getApplicationContext(), bitmap);
                //   files.add(resultUri.toString());
               /* String path = resultUri.getPath();
                BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                bmpFactoryOptions.inSampleSize = 4;
                Bitmap bitmap = BitmapFactory.decodeFile(path, bmpFactoryOptions);
                bitmap = getImageAttribute(bitmap, path);
                Bitmap bmp = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                //int jdhfjdg = bmp.getByteCount();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                new SaveImageTask(bmp, path).execute(stream.toByteArray());*/
                //   bitmap = getImageAttribute(bitmap, path);


             /*   String captured_image_path = getFilename();
                File imageFile = new File(captured_image_path);
                Uri imageFileUri = Uri.fromFile(imageFile);*/

                /*if (files.size() > 0) {
                    uploadFiles();
                }*/
             //   binding.banner.setImageBitmap(bitmap);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Error occured!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        return retVal;
    }

    @SuppressLint("StaticFieldLeak")
    private class SaveImageTask extends AsyncTask<byte[], Void, String> {
        String path;
        Bitmap bitmap;

        SaveImageTask(Bitmap bitmap, String path) {
            this.bitmap = bitmap;
            this.path = path;
        }

        @Override
        protected void onPreExecute() {
            showSimmerLoader(DEFAULT_LOADER);
        }

        @Override
        protected String doInBackground(byte[]... data) {
            try {
                File outFile = new File(path);
                if (outFile.exists()) {
                    outFile.delete();
                }
                FileOutputStream outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return path;
        }

        @Override
        protected void onPostExecute(String result) {
            hideSimmerLoader();
            if (!result.equals("")) {
                files.add(path);
                if (files.size() > 0) {
                    uploadFiles();
                }
            } else {
                Toast.makeText(ReOrderActivity.this, "Problem in image saving!", Toast.LENGTH_SHORT).show();
            }
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
        fileUploader.uploadFiles(unique_key, "image", filesToUpload, "Bearer" + viewModel.getToken(), new FileUploader.FileUploaderCallback() {
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
                            String dataString = obj.optString("data");
                            if (dataString != "null") {
                                JSONObject jsonObject = new JSONObject(dataString);
                                String id = jsonObject.optString("id");
                                String path = jsonObject.optString("path");
                                String thumbnail = jsonObject.optString("thumbnail");

                                Data data1 = new Data();
                                data1.setId(Integer.parseInt(id));
                                data1.setPath(path);
                                data1.setThumbnail(thumbnail);
                                data.set(position, data1);
                                initAdapter(data);
                            } else {
                                Toast.makeText(ReOrderActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    } else {
                        Toast.makeText(ReOrderActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

    @SuppressLint("StaticFieldLeak")
    private class RequestPostsAsyncTask extends AsyncTask<String, String, String> {
        Context context;
        String json;
        BottomSheetDialog dialog;

        private RequestPostsAsyncTask(Context context, BottomSheetDialog dialog) {
            this.context = context;
            this.dialog = dialog;
        }

        @Override
        protected void onPreExecute() {

            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            try {
                for (int i = 0; i < data.size(); i++) {
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("id", data.get(i).getId());
                    jsonObject1.put("order", data.get(i).getId());
                    jsonObject1.put("encode", "data:image/jpeg;base64," + data.get(i).getPath());
                    jsonObject1.put("uri", data.get(i).getId());
                    jsonArray.put(jsonObject1);
                }

                try {
                    jsonObject.put("userid", "123");
                    jsonObject.put("request", jsonArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            json = jsonObject.toString();
            // json = json.replaceAll("[\\[\\]]", "");
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            HttpURLConnection connection = null;
            try {
                URL url = new URL(BuildConfig.VISION_BASE_URL + "upload_image");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Client-Service", "frontend-client");
                connection.setRequestProperty("Auth-Key", "ttlapplication");
                connection.setRequestProperty("Content-type", "application/json");
                connection.setRequestProperty("cache-control", "application/json");
                connection.setDoOutput(true);
                connection.connect();
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(json);
                writer.flush();
                int response = connection.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder builder = new StringBuilder();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    result = builder.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @SuppressLint("ResourceAsColor")
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            if (from != null){
                if (from.equals(CAMERA_GALLERY_LIST)){
                    finishAffinity();
                    startActivity(HomeYouActivity.class);
                    showToast("Photo uploaded successfully.");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("key", data);
                    startUploadActivity(Content.class, bundle);
                }
            } else {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("key", data);
                startUploadActivity(Content.class, bundle);
            }

        }
    }

    Thread thread;

    private void progressLoader(final ArrayList<Data> imageModels, BottomSheetDialog dialog) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.alert_loader, null);
        //  final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(alertLayout);
        dialog.setCancelable(false);
        dialog.show();

        final int[] progressStatus = {0};
        final Handler handler = new Handler();

        final ProgressBar progressBar = alertLayout.findViewById(R.id.progressBar);
        final ProgressBar progressBarRound = alertLayout.findViewById(R.id.progressBarRound);
        final TextView progressText = alertLayout.findViewById(R.id.progress_text);
        ImageView close = alertLayout.findViewById(R.id.close);
        progressBar.setMax(imageModels.size());

        close.setOnClickListener(view -> dialog.dismiss());

        // Start long running operation in a background thread
        thread = new Thread() {
            public void run() {
                while (progressStatus[0] < imageModels.size()) {
                    progressStatus[0] += 1;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(() -> {
                        progressBar.setProgress(progressStatus[0]);
                        progressText.setText(progressStatus[0] + " " + "out of" + " " + imageModels.size());
                    });
                    try {
                        synchronized (this) {
                            wait(3000);

                            runOnUiThread(() -> {
                                int count = progressStatus[0];
                                if (imageModels.size() == count) {
                                    progressBar.setVisibility(View.GONE);
                                    progressBarRound.setVisibility(View.VISIBLE);
                                    //  dialog.dismiss();
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
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
