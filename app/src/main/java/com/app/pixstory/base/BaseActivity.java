package com.app.pixstory.base;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.app.pixstory.R;
import com.app.pixstory.utils.Constants;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;
import static com.app.pixstory.utils.Constants.HOME_LOADER;
import static com.app.pixstory.utils.Constants.STORY_VIEW_LOADER;
import static com.app.pixstory.utils.Constants.UPLOAD_LOADER;
import static com.app.pixstory.utils.Constants.YOU_STORY_LOADER;

public abstract class BaseActivity<B extends ViewDataBinding, V extends ViewModel> extends AppCompatActivity
        implements BaseFragment.Callback {

    protected boolean REQUEST_PERMISSION_FOR_ACTIVITY = true;
    private B binding;
    private V viewModel;
    private Dialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUp();
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if(REQUEST_PERMISSION_FOR_ACTIVITY) {
//            checkAllPermission();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null;
        viewModel = null;
        System.gc();
    }

    private void setUp() {
        dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        binding = DataBindingUtil.setContentView(this, getLayout());
        viewModel = ViewModelProviders.of(this).get(setViewModel());
        if (getBindingVariable() != 0) {
            binding.setVariable(getBindingVariable(), viewModel);
        }
        binding.executePendingBindings();
        getBinding(binding);
        getViewModel(viewModel);
    }

    public void showSimmerLoader(String pageLoader) {
        View view = null;
       /* if (pageLoader.equals(DEFAULT_LOADER)) {
            view = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        } else if (pageLoader.equals(HOME_LOADER)){
            view = LayoutInflater.from(this).inflate(R.layout.simmer_loader_home_page, null);
        } else if (pageLoader.equals(YOU_STORY_LOADER)){
            view = LayoutInflater.from(this).inflate(R.layout.simmer_loader_you_page, null);
        } else if (pageLoader.equals(STORY_VIEW_LOADER)){
            view = LayoutInflater.from(this).inflate(R.layout.simmer_loader_story_view, null);
        } else if (pageLoader.equals(UPLOAD_LOADER)){
            view = LayoutInflater.from(this).inflate(R.layout.simmer_loader_upload, null);
        }*/

        if (pageLoader.equals(DEFAULT_LOADER)) {
            view = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        } else if (pageLoader.equals(HOME_LOADER)){
            view = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        } else if (pageLoader.equals(YOU_STORY_LOADER)){
            view = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        } else if (pageLoader.equals(STORY_VIEW_LOADER)){
            view = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        } else if (pageLoader.equals(UPLOAD_LOADER)){
            view = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        }

        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        dialog.show();
    }

    public void hideSimmerLoader() {
        if (dialog.isShowing())
            dialog.dismiss();
    }


    /**
     * Override for set binding variable
     *
     * @return BR.CalendarData;
     */
    public int getBindingVariable() {
        return 0;
    }

    @LayoutRes
    public abstract int getLayout();

    protected abstract void getBinding(B binding);

    protected abstract void getViewModel(V viewModel);

    protected abstract Class<V> setViewModel();

    protected abstract void init();

    public void toolbar(Toolbar toolbar, TextView toolbarTitle, @StringRes int title) {
        // toolbar action
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            toolbarTitle.setText(getText(title));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button_24dp);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public void toolbar(Toolbar toolbar, TextView toolbarTitle, String title) {
        // toolbar action
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            toolbarTitle.setText(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button_24dp);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public void toolbar(Toolbar toolbar) {
        // toolbar action
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button_24dp);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

    }

    //fragment callback--------------------------

    @Override
    public void onFragmentAttached(Fragment fragment) {

    }

    @Override
    public void onFragmentDetached() {

    }

    //---------------------------

    //-------------------Permission----------------------------

    /**
     * override this method when manually asked for permission and get permission granted
     */
    public void onPermissionGranted() {

    }

    public boolean isPermissionGranted() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isPermissionGrantedCamera() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    public void checkAllPermission() {
        try {
            requestAppPermissions(new String[]{
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    R.string.str_ShowOnPermisstion,
                    Constants.REQUEST_PERMISSIONS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions,
                                           @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //permission already granted
            onPermissionGranted();
        } else {
            Snackbar mSnackBar = Snackbar.make(findViewById(android.R.id.content),
                    getString(R.string.str_ShowOnPermisstion),
                    Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                    v -> {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(intent);
                    });
            mSnackBar.show();
        }
    }

    public void requestAppPermissions(@NotNull final String[] requestedPermissions,
                                      final int stringId, final int requestCode) {
        SparseIntArray mErrorString = new SparseIntArray();
        mErrorString.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                Snackbar mSnackBar = Snackbar.make(findViewById(android.R.id.content), stringId,
                        Snackbar.LENGTH_INDEFINITE).setAction("GRANT",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(BaseActivity.this,
                                        requestedPermissions, requestCode);
                            }
                        });
                mSnackBar.show();
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        }
    }

    //---------------------------------------------------------------

    /**
     * Call this method to set up the toolbar and title
     *
     * @param title title in toolbar
     */
    public void setTitle(int title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(@StringRes int message) {
        Toast.makeText(this, getString(message), Toast.LENGTH_SHORT).show();
    }

    //-----------------------TaskActivity----------------------------

    /**
     * If keypad is showing it can be hide immediately
     */
    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * @return true if network available else false for not
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * @param view method which focus on EditText view
     */
    public void requestFocus(View view) {
        if (view.requestFocus()) {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
    }

    public <C extends Activity> void refreshActivity() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    public <C extends Activity> void startActivity(Class<C> targetActivityClass) {
        Intent intent = new Intent(this, targetActivityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        //  startActivity(new Intent(this, targetActivityClass));
    }

    public <C extends Activity> void startActivity(Class<C> targetActivityClass, Bundle bundle) {
        Intent intent = new Intent(this, targetActivityClass);
//        intent.putExtra(Constants.KEY_DEFAULT_ACTIVITY_BUNDLE, bundle);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public <C extends Activity> void startUploadActivity(Class<C> targetActivityClass, Bundle bundle) {
        Intent intent = new Intent(this, targetActivityClass);
        intent.putExtra(Constants.KEY_DEFAULT_ACTIVITY_BUNDLE, bundle);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    public <C extends Activity> void startActivity(Class<C> targetActivityClass, String value) {
        Intent intent = new Intent(this, targetActivityClass);
        intent.putExtra(Constants.KEY_DEFAULT_ACTIVITY_INTENT, value);
        startActivity(intent);
    }

    public <C extends Activity> void startActivity(Class<C> targetActivityClass, int value) {
        Intent intent = new Intent(this, targetActivityClass);
        intent.putExtra(Constants.KEY_DEFAULT_ACTIVITY_INTENT_INT, value);
        startActivity(intent);
    }

    /**
     *
     * @param fragment new Fragment();
     * @param tag Fragment.TAG or Fragment.class.getSimpleName()
     */
/*
    public void startFragment(Fragment fragment, String tag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment, tag);
//        transaction.addToBackStack(fragmentClassName);
        transaction.commit();
    }
*/

    /**
     *
     * @param fragment new Fragment();
     * @param tag Fragment.TAG or Fragment.class.getSimpleName()
     * @param bundle for extra params
     */
/*
    public void startFragment(Fragment fragment, String tag, Bundle bundle){
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment, tag);
//        transaction.addToBackStack(fragmentClassName);
        transaction.commit();
    }
*/

    /**
     * @param dialog new Dialog();
     * @param tag    Dialog.TAG or Dialog.class.getSimpleName()
     */
    public void startDialog(DialogFragment dialog, String tag) {
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.default_dialog);
        dialog.show(getSupportFragmentManager(), tag);
    }

    /**
     * @param dialog new Dialog();
     * @param tag    Dialog.TAG or Dialog.class.getSimpleName()
     * @param bundle for extra params
     */
    public void startDialog(DialogFragment dialog, String tag, Bundle bundle) {
        dialog.setArguments(bundle);
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.default_dialog);
        dialog.show(getSupportFragmentManager(), tag);
    }

    /**
     * @param dialog new Dialog();
     * @param tag    Dialog.TAG or Dialog.class.getSimpleName()
     * @param bundle for extra params
     */
    public void startDialog(DialogFragment dialog, String tag, Bundle bundle, @StyleRes int styleId) {
        dialog.setArguments(bundle);
        dialog.setStyle(DialogFragment.STYLE_NORMAL, styleId);
        dialog.show(getSupportFragmentManager(), tag);
    }

    public void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Grant permission");
        builder.setMessage("Pixstory need permissions");
        builder.setPositiveButton("Go to settings", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    public void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


}
