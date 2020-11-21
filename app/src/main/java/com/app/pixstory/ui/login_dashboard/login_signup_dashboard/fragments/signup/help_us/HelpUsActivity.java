package com.app.pixstory.ui.login_dashboard.login_signup_dashboard.fragments.signup.help_us;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.core.dialog.date.DateDialog;
import com.app.pixstory.core.dialog.otp.Verification;
import com.app.pixstory.data.model.api.CountryResponse;
import com.app.pixstory.databinding.ActivityHelpUsBinding;
import com.app.pixstory.ui.login_dashboard.login_signup_dashboard.fragments.signup.interest.HighlightYourInterests;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.GlobalMethods;
import com.app.pixstory.utils.Utils;
import com.app.pixstory.utils.util.Bundles;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;
import static com.app.pixstory.utils.Constants.EMAIL_VERIFIED_FALSE;
import static com.app.pixstory.utils.Constants.EMAIL_VERIFIED_TRUE;
import static com.app.pixstory.utils.Constants.FROM_HELP;
import static com.app.pixstory.utils.Constants.LOGIN_TYPE_EMAIL_ID;
import static com.app.pixstory.utils.Constants.LOGIN_TYPE_FACEBOOK;
import static com.app.pixstory.utils.Constants.LOGIN_TYPE_GOOGLE;
import static com.app.pixstory.utils.Constants.LOGIN_TYPE_LINKEDIN;
import static com.app.pixstory.utils.Constants.LOGIN_TYPE_MOBILE_NUMBER;
import static com.app.pixstory.utils.Constants.MOBILE_VERIFIED_FALSE;
import static com.app.pixstory.utils.Constants.MOBILE_VERIFIED_TRUE;
import static com.app.pixstory.utils.Constants.SIGN_UP_CONFIRM_PASSWORD;
import static com.app.pixstory.utils.Constants.SIGN_UP_DOB;
import static com.app.pixstory.utils.Constants.SIGN_UP_EMAIL;
import static com.app.pixstory.utils.Constants.SIGN_UP_FIRST_NAME;
import static com.app.pixstory.utils.Constants.SIGN_UP_GENDER;
import static com.app.pixstory.utils.Constants.SIGN_UP_LAST_NAME;
import static com.app.pixstory.utils.Constants.SIGN_UP_MOBILE;
import static com.app.pixstory.utils.Constants.SIGN_UP_PASSWORD;

public class HelpUsActivity extends BaseActivity<ActivityHelpUsBinding, HelpUsViewModel>
        implements View.OnClickListener, HelpUsNavigator, Verification.VerificationCallback {

    ActivityHelpUsBinding binding;
    private HelpUsViewModel viewModel;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    String loginType = "", username = "", mobile = "";
    private boolean isUsernameValid;
    private String countryCode = "", countryId = "";
    private String phoneNumber;
    private boolean isMobileVerified, showPassword, showConfirmPassword, nextClicked;
    private String fbId = "";
    private String googleId = "";
    private String linkedIn = "";
    private GoogleApiClient googleApiClient;
    private List<String> countryList = new ArrayList<>();
    private List<CountryResponse.Data> data = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void init() {
        viewModel.setNavigator(this);
        setup();
        viewModel.getCountryList();
        gender();
        clickListener();
        uniqueUser();
        uniqueMobile();

        viewModel.getLoading().observe(this, aBoolean -> {
            if (aBoolean) {
                showSimmerLoader(DEFAULT_LOADER);
            } else {
                hideSimmerLoader();
            }
        });

    }

    private void setup() {
        toolbar(binding.layoutToolbar.toolbar, binding.layoutToolbar.toolbarTitle, R.string.help_us);

        Bundle bundle = getIntent().getExtras();
        loginType = Bundles.getInstance().getSignUpDataType(bundle);
        if (loginType.equals(LOGIN_TYPE_MOBILE_NUMBER)) {
            isMobileVerified = true;
            countryCode = Bundles.getInstance().getSignUpDataCountryCode(bundle);
            phoneNumber = Bundles.getInstance().getSignUpDataMobile(bundle);
            binding.ccp.setDefaultCountryUsingNameCodeAndApply(countryCode.replace(countryCode, "+"));
            binding.phoneNumber.setText(phoneNumber);
            binding.ccp.setEnabled(false);
            binding.phoneNumber.setEnabled(false);
        } else if (loginType.equalsIgnoreCase(LOGIN_TYPE_EMAIL_ID)) {
            isMobileVerified = false;
            binding.email.setEnabled(false);
            binding.email.setText(Bundles.getInstance().getSignUpDataEmail(bundle));
        } else if (loginType.equalsIgnoreCase(LOGIN_TYPE_FACEBOOK)) {
            isMobileVerified = false;
            HashMap<String, String> data = Bundles.getInstance().getSignUpData(bundle);
            if (data != null && !data.isEmpty()) {
                if (!Objects.requireNonNull(data.get("facebook")).isEmpty()) {
                    fbId = data.get("facebook");
                }
                if (!Objects.requireNonNull(data.get("google")).isEmpty()) {
                    googleId = data.get("google");
                }
                if (!Objects.requireNonNull(data.get("linkedin")).isEmpty()) {
                    linkedIn = data.get("linkedin");
                }

                if (!Objects.requireNonNull(data.get("firstName")).isEmpty()) {
                    String asd = data.get("firstName");
                    Log.e("fist", asd);
                    binding.firstName.setText(data.get("firstName"));
                }
                if (!Objects.requireNonNull(data.get("lastName")).isEmpty()) {
                    String asd = data.get("lastName");
                    Log.e("lastName", asd);
                    binding.lastName.setText(data.get("lastName"));
                }
                if (!Objects.requireNonNull(data.get("email")).isEmpty()) {
                    String asd = data.get("email");
                    Log.e("email", asd);
                    binding.email.setText(data.get("email"));
                }
            }
        } else if (loginType.equalsIgnoreCase(LOGIN_TYPE_GOOGLE)) {
            isMobileVerified = false;
            HashMap<String, String> data = Bundles.getInstance().getSignUpData(bundle);
            if (data != null && !data.isEmpty()) {
                if (!Objects.requireNonNull(data.get("facebook")).isEmpty()) {
                    fbId = data.get("facebook");
                }
                if (!Objects.requireNonNull(data.get("google")).isEmpty()) {
                    googleId = data.get("google");
                }
                if (!Objects.requireNonNull(data.get("linkedin")).isEmpty()) {
                    linkedIn = data.get("linkedin");
                }

                if (!Objects.requireNonNull(data.get("firstName")).isEmpty()) {
                    String asd = data.get("firstName");
                    Log.e("fist", asd);
                    binding.firstName.setText(data.get("firstName"));
                }
                if (!Objects.requireNonNull(data.get("lastName")).isEmpty()) {
                    String asd = data.get("lastName");
                    Log.e("lastName", asd);
                    binding.lastName.setText(data.get("lastName"));
                }
                if (!Objects.requireNonNull(data.get("email")).isEmpty()) {
                    String asd = data.get("email");
                    Log.e("email", asd);
                    binding.email.setText(data.get("email"));
                }
            }
        }

        binding.preferredUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    binding.inputTextPreferredUserName.setError(null);
                    //   binding.done.setVisibility(View.GONE);
                    //   binding.exist.setVisibility(View.GONE);
                    binding.progressBarUser.setVisibility(View.GONE);
                }
            }
        });

        binding.phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    binding.inputTextPhoneNumber.setError(null);
                    //   binding.doneMobile.setVisibility(View.GONE);
                    //   binding.existMobile.setVisibility(View.GONE);
                    binding.progressBarMobile.setVisibility(View.GONE);
                }
            }
        });


        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryId = String.valueOf(data.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void uniqueUser() {
        binding.preferredUserName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                username = Objects.requireNonNull(binding.preferredUserName.getText()).toString().trim();
                if (username.length() > 7 && username.length() < 17) {
                    //  binding.done.setVisibility(View.GONE);
                    //  binding.exist.setVisibility(View.GONE);
                    binding.progressBarUser.setVisibility(View.VISIBLE);
                    viewModel.generateUserName(username);
                    binding.male.setChecked(true);
                } else {
                    //   binding.done.setVisibility(View.GONE);
                    //   binding.exist.setVisibility(View.GONE);
                    binding.progressBarUser.setVisibility(View.GONE);
                    if (username.length() > 0)
                        binding.inputTextPreferredUserName.setError("Invalid username");
                }
            }
        });
    }

    private void uniqueMobile() {
        binding.phoneNumber.setOnFocusChangeListener((v, hasFocus) -> {
            mobile = Objects.requireNonNull(binding.phoneNumber.getText()).toString().trim();
            if (!hasFocus) {
                if (mobile.length() >= 10 && mobile.length() <= 14) {
                    //   binding.doneMobile.setVisibility(View.GONE);
                    //   binding.existMobile.setVisibility(View.GONE);
                    binding.progressBarMobile.setVisibility(View.VISIBLE);
                    viewModel.checkMobile(mobile);
                } else {
                    //   binding.doneMobile.setVisibility(View.GONE);
                    //    binding.existMobile.setVisibility(View.GONE);
                    binding.progressBarMobile.setVisibility(View.GONE);
                    if (mobile.length() > 0)
                        binding.inputTextPhoneNumber.setError("Invalid phone number");

                }
            }
        });
    }

    @Override
    public void onGenerateUserName(String name, boolean status) {
        if (status) {
            binding.progressBarUser.setVisibility(View.GONE);
            // binding.done.setVisibility(View.VISIBLE);
            binding.inputTextPreferredUserName.setError(null);
            isUsernameValid = true;
            if (nextClicked) {
                doSignup();
                nextClicked = false;
            }

        } else {
            binding.progressBarUser.setVisibility(View.GONE);
            //  binding.exist.setVisibility(View.VISIBLE);
            isUsernameValid = false;
        }
    }

    @Override
    public void checkMobile(String mobile, boolean status) {
        if (status) {
            binding.progressBarMobile.setVisibility(View.GONE);
            //   binding.doneMobile.setVisibility(View.VISIBLE);
            binding.inputTextPhoneNumber.setError(null);
            isMobileVerified = true;
            if (nextClicked) {
                doSignup();
                nextClicked = false;
            }
        } else {
            binding.progressBarMobile.setVisibility(View.GONE);
            //  binding.existMobile.setVisibility(View.VISIBLE);
            isMobileVerified = false;
        }
    }

    private void getFacebookData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            binding.firstName.setText(bundle.getString("first_name"));
            binding.lastName.setText(bundle.getString("last_name"));
            binding.email.setText(bundle.getString("email"));
            binding.dateOfBirth.setText(bundle.getString("birthday"));
            String mobile_number_str = bundle.getString("first_name");
            //  String last_name = bundle.getString("last_name");
            //  String email = bundle.getString("email");
            //   String gender = bundle.getString("gender");
            //   String birthday = bundle.getString("birthday");
            String profile_pic = bundle.getString("profile_pic");


        }
    }

    @Override
    protected Class<HelpUsViewModel> setViewModel() {
        return HelpUsViewModel.class;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_help_us;
    }

    @Override
    protected void getBinding(ActivityHelpUsBinding binding) {
        this.binding = binding;
    }


    @Override
    protected void getViewModel(HelpUsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void clickListener() {
        binding.dateOfBirth.setOnClickListener(this);
        binding.next.setOnClickListener(this);
        binding.showPassword.setOnClickListener(this);
        binding.showConfirmPassword.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            google();
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.date_of_birth) {
            DateDialog dateFragment = new DateDialog();
            dateFragment.setup(this::date, DateDialog.TYPE_DATE_BIRTH);
            startDialog(dateFragment, DateDialog.TAG);
        } else if (v.getId() == R.id.next) {
            nextButton();
        } else if (v.getId() == R.id.show_password) {
            if (!showPassword) {
                showPassword = true;
                binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                showPassword = false;
                binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            Utils.setFocus(binding.password);

        } else if (v.getId() == R.id.show_confirm_password) {
            if (!showConfirmPassword) {
                showConfirmPassword = true;
                binding.confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                showConfirmPassword = false;
                binding.confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }

            Utils.setFocus(binding.confirmPassword);
        }
    }

    private void nextButton() {
        nextClicked = true;
        SIGN_UP_MOBILE = Objects.requireNonNull(binding.phoneNumber.getText()).toString().trim();
        SIGN_UP_FIRST_NAME = Objects.requireNonNull(binding.firstName.getText()).toString().trim();
        SIGN_UP_LAST_NAME = Objects.requireNonNull(binding.lastName.getText()).toString().trim();
        username = Objects.requireNonNull(binding.preferredUserName.getText()).toString().trim();
        SIGN_UP_DOB = Objects.requireNonNull(binding.dateOfBirth.getText()).toString().trim();
        SIGN_UP_EMAIL = Objects.requireNonNull(binding.email.getText()).toString().trim();
        SIGN_UP_PASSWORD = Objects.requireNonNull(binding.password.getText()).toString().trim();
        SIGN_UP_CONFIRM_PASSWORD = Objects.requireNonNull(binding.confirmPassword.getText()).toString().trim();

        if (SIGN_UP_FIRST_NAME.equals("")) {
            Toast.makeText(this, "Please enter first name", Toast.LENGTH_SHORT).show();
        } else if (SIGN_UP_LAST_NAME.equals("")) {
            Toast.makeText(this, "Please enter last name", Toast.LENGTH_SHORT).show();
        } else if (username.equals("")) {
            Toast.makeText(this, "Please enter preferred user name", Toast.LENGTH_SHORT).show();
        } else if (username.length() < 8) {
            Toast.makeText(this, "Preferred user name at least 8 character", Toast.LENGTH_SHORT).show();
        } else if (binding.genderRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
        } else if (SIGN_UP_DOB.equals("")) {
            Toast.makeText(this, "Please select Date of birth", Toast.LENGTH_SHORT).show();
        } else if (SIGN_UP_EMAIL.equals("")) {
            Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT).show();
        } else if (SIGN_UP_MOBILE.equals("")) {
            Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
        } else if (SIGN_UP_PASSWORD.equals("")) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        } else if (SIGN_UP_CONFIRM_PASSWORD.equals("")) {
            Toast.makeText(this, "Please enter confirm password", Toast.LENGTH_SHORT).show();
        } else if (!SIGN_UP_PASSWORD.equals(SIGN_UP_CONFIRM_PASSWORD)) {
            Toast.makeText(this, "Password and Confirm password must be same", Toast.LENGTH_SHORT).show();
        } else if (!isUsernameValid) {

            //Toast.makeText(this, "Username not valid", Toast.LENGTH_SHORT).show();

            username = Objects.requireNonNull(binding.preferredUserName.getText()).toString().trim();
            if (username.length() > 7 && username.length() < 17) {
                //  binding.done.setVisibility(View.GONE);
                //  binding.exist.setVisibility(View.GONE);
                binding.progressBarUser.setVisibility(View.VISIBLE);
                viewModel.generateUserName(username);
            } else {
                //   binding.done.setVisibility(View.GONE);
                //   binding.exist.setVisibility(View.GONE);
                binding.progressBarUser.setVisibility(View.GONE);
                if (username.length() > 0)
                    binding.inputTextPreferredUserName.setError("Invalid username");
            }

        } else if (!isMobileVerified) {

            //  Toast.makeText(this, "Phone number not valid", Toast.LENGTH_SHORT).show();

            mobile = Objects.requireNonNull(binding.phoneNumber.getText()).toString().trim();
            if (mobile.length() >= 10 && mobile.length() <= 14) {
                //   binding.doneMobile.setVisibility(View.GONE);
                //   binding.existMobile.setVisibility(View.GONE);
                binding.progressBarMobile.setVisibility(View.VISIBLE);
                viewModel.checkMobile(mobile);
            } else {
                //   binding.doneMobile.setVisibility(View.GONE);
                //    binding.existMobile.setVisibility(View.GONE);
                binding.progressBarMobile.setVisibility(View.GONE);
                if (mobile.length() > 0)
                    binding.inputTextPhoneNumber.setError("Invalid phone number");

            }

        } else {
            doSignup();
        }
    }


    private void doSignup() {
        if (loginType.equals(LOGIN_TYPE_MOBILE_NUMBER)) {
            viewModel.doSignUp(this, loginType, SIGN_UP_FIRST_NAME, SIGN_UP_LAST_NAME, username,
                    SIGN_UP_GENDER, SIGN_UP_DOB, countryId, phoneNumber, SIGN_UP_EMAIL, SIGN_UP_PASSWORD,
                    SIGN_UP_CONFIRM_PASSWORD, "", countryCode, EMAIL_VERIFIED_FALSE, MOBILE_VERIFIED_TRUE);
        } else {

            if (!isMobileVerified) {
                if (GlobalMethods.validateMobileValidation(binding.phoneNumber, binding.ccp)) {
                    String countryCode = binding.ccp.getDefaultCountryCode();
                    if (!countryCode.contains("+")) {
                        countryCode = "+" + countryCode;
                    }

                    viewModel.verificationRequestMobile(countryCode, binding.phoneNumber.getText() != null ?
                            binding.phoneNumber.getText().toString().trim() : "", false, Constants.TYPE_SIGNUP, loginType);
                }
            } else {
                phoneNumber = binding.phoneNumber.getText().toString().trim();
                countryCode = binding.ccp.getSelectedCountryCode();
                if (!countryCode.contains("+")) {
                    countryCode = "+" + countryCode;
                }

                if (loginType.equals(LOGIN_TYPE_FACEBOOK)) {
                    viewModel.doSignUp(this, loginType, SIGN_UP_FIRST_NAME, SIGN_UP_LAST_NAME, username,
                            SIGN_UP_GENDER, SIGN_UP_DOB, countryId, phoneNumber, SIGN_UP_EMAIL, SIGN_UP_PASSWORD,
                            SIGN_UP_CONFIRM_PASSWORD, fbId, countryCode, EMAIL_VERIFIED_FALSE, MOBILE_VERIFIED_TRUE);
                } else if (loginType.equals(LOGIN_TYPE_GOOGLE)) {
                    viewModel.doSignUp(this, loginType, SIGN_UP_FIRST_NAME, SIGN_UP_LAST_NAME, username,
                            SIGN_UP_GENDER, SIGN_UP_DOB, countryId, phoneNumber, SIGN_UP_EMAIL, SIGN_UP_PASSWORD,
                            SIGN_UP_CONFIRM_PASSWORD, googleId, countryCode, EMAIL_VERIFIED_FALSE, MOBILE_VERIFIED_TRUE);
                } else if (loginType.equals(LOGIN_TYPE_LINKEDIN)) {

                    viewModel.doSignUp(this, loginType, SIGN_UP_FIRST_NAME, SIGN_UP_LAST_NAME, username,
                            SIGN_UP_GENDER, SIGN_UP_DOB, countryId, phoneNumber, SIGN_UP_EMAIL, SIGN_UP_PASSWORD,
                            SIGN_UP_CONFIRM_PASSWORD, linkedIn, countryCode, EMAIL_VERIFIED_FALSE, MOBILE_VERIFIED_TRUE);
                } else if (loginType.equals(LOGIN_TYPE_EMAIL_ID)) {
                    // phoneNumber = binding.phoneNumber.getText().toString().trim();
                    viewModel.doSignUp(this, loginType, SIGN_UP_FIRST_NAME, SIGN_UP_LAST_NAME, username,
                            SIGN_UP_GENDER, SIGN_UP_DOB, countryId, phoneNumber, SIGN_UP_EMAIL, SIGN_UP_PASSWORD,
                            SIGN_UP_CONFIRM_PASSWORD, "", countryCode, EMAIL_VERIFIED_TRUE, isMobileVerified ? MOBILE_VERIFIED_TRUE : MOBILE_VERIFIED_FALSE);
                }

            }
        }
    }

    /**
     * @param readableData date will return in string format
     * @param sendDate     date will return to send to server
     * @param params       date will return in int format
     */
    public void date(String readableData, String sendDate, String... params) {
        binding.dateOfBirth.setText(readableData);
        SIGN_UP_DOB = readableData;

        binding.email.requestFocus();
        Utils.showSoftKeyboard(getBaseContext(), binding.email);
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user asynchronously -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(HelpUsActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NotNull String[] permissions, @NotNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        //  locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    private void gender() {
        binding.genderRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.male) {
                SIGN_UP_GENDER = "male";
            } else if (checkedId == R.id.female) {
                SIGN_UP_GENDER = "female";
            } else if (checkedId == R.id.other) {
                SIGN_UP_GENDER = "others";
            }
        });
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
    public void onSuccessSignUp(String loginType) {

        startActivity(HighlightYourInterests.class, Bundles.getInstance().setSignUpType(loginType));
        finish();
    }

    @Override
    public void onError(Map<String, List<String>> error) {
        Log.e("Sign up", "error");

        if (error != null) {
            if (error.containsKey("dob")) {
                showToast(error.get("first_name").get(0));
                return;
            }
            if (error.containsKey("first_name") && error.get("first_name").get(0) != null) {
                binding.firstName.setText("");
                binding.inputTextFirstName.setError(error.get("first_name").get(0));
            }
            if (error.containsKey("last_name")) {
                binding.lastName.setText("");
                binding.inputTextLastName.setError(error.get("last_name").get(0));
            }
            if (error.containsKey("username")) {
                //binding.preferredUserName.setText("");
                binding.inputTextPreferredUserName.setError(error.get("username").get(0));
            }
            if (error.containsKey("mobile")) {
                //  binding.phoneNumber.setText("");
                binding.inputTextPhoneNumber.setError(error.get("mobile").get(0));
            }
            if (error.containsKey("email")) {
                binding.email.setText("");
                binding.inputTextEmail.setError(error.get("email").get(0));
            }
            if (error.containsKey("password")) {
                binding.password.setText("");
                binding.inputTextPassword.setError(error.get("password").get(0));
            }
            if (error.containsKey("confirm_password")) {
                binding.confirmPassword.setText("");
                binding.inputTextConfirmPassword.setError(error.get("confirm_password").get(0));
            }
        }
    }

    @Override
    public void onMobileVerifies(boolean status, String countryCode, String mobile) {
        if (status) {
            this.countryCode = countryCode;
            this.phoneNumber = mobile;
            isMobileVerified = true;
            nextButton();
        } else {
            isMobileVerified = false;
        }
    }

    @Override
    public void onVerificationResponse(boolean status, String countryCode, String mobile, String message, boolean resendOtp, String loginType) {
        this.phoneNumber = mobile;

        if (status) {
            if (message != null) {
                showToast(message);
            } else {
                showToast(R.string.otp_send_message);
            }
            Verification.verify(this, this, countryCode, mobile, "", loginType, resendOtp, "", FROM_HELP);
        } else {
            showToast(message);
        }
    }

    @Override
    public void setCountryList(List<CountryResponse.Data> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            countryList.add(data.get(i).getCountryName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
    }

    @Override
    public void skipSignUp() {
        phoneNumber = Objects.requireNonNull(binding.phoneNumber.getText()).toString().trim();
        countryCode = binding.ccp.getSelectedCountryCode();
        if (loginType.equals(LOGIN_TYPE_FACEBOOK)) {
            viewModel.doSignUp(this, loginType, SIGN_UP_FIRST_NAME, SIGN_UP_LAST_NAME, username,
                    SIGN_UP_GENDER, SIGN_UP_DOB, countryId, phoneNumber, SIGN_UP_EMAIL, SIGN_UP_PASSWORD,
                    SIGN_UP_CONFIRM_PASSWORD, fbId, countryCode, EMAIL_VERIFIED_FALSE, isMobileVerified ? MOBILE_VERIFIED_TRUE : MOBILE_VERIFIED_FALSE);
        } else if (loginType.equals(LOGIN_TYPE_GOOGLE)) {
            viewModel.doSignUp(this, loginType, SIGN_UP_FIRST_NAME, SIGN_UP_LAST_NAME, username,
                    SIGN_UP_GENDER, SIGN_UP_DOB, countryId, phoneNumber, SIGN_UP_EMAIL, SIGN_UP_PASSWORD,
                    SIGN_UP_CONFIRM_PASSWORD, googleId, countryCode, EMAIL_VERIFIED_FALSE, isMobileVerified ? MOBILE_VERIFIED_TRUE : MOBILE_VERIFIED_FALSE);
        } else if (loginType.equals(LOGIN_TYPE_LINKEDIN)) {
            viewModel.doSignUp(this, loginType, SIGN_UP_FIRST_NAME, SIGN_UP_LAST_NAME, username,
                    SIGN_UP_GENDER, SIGN_UP_DOB, countryId, phoneNumber, SIGN_UP_EMAIL, SIGN_UP_PASSWORD,
                    SIGN_UP_CONFIRM_PASSWORD, linkedIn, countryCode, EMAIL_VERIFIED_FALSE, isMobileVerified ? MOBILE_VERIFIED_TRUE : MOBILE_VERIFIED_FALSE);
        } else {
            viewModel.doSignUp(this, loginType, SIGN_UP_FIRST_NAME, SIGN_UP_LAST_NAME, username,
                    SIGN_UP_GENDER, SIGN_UP_DOB, countryId, phoneNumber, SIGN_UP_EMAIL, SIGN_UP_PASSWORD,
                    SIGN_UP_CONFIRM_PASSWORD, "", countryCode, EMAIL_VERIFIED_TRUE, isMobileVerified ? MOBILE_VERIFIED_TRUE : MOBILE_VERIFIED_FALSE);
        }
    }

    @Override
    public void skipVerification(String mobile, String countryCode, String otp) {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        google();
    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleApiClient.connect();
        super.onStart();
    }

    public void google() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        //     logoutAPI();
                    }
                });
    }

    @Override
    public void onVerifyRequestMobile(String countryCodePicker, String mobile, String loginType, String otp) {
        //API to request OTP for mobile
        viewModel.onVerifyOtpMobile(countryCodePicker, mobile, otp);
    }

    @Override
    public void onResendOtpMobile(String loginType, String countryCode, String mobile, String type) {
        viewModel.verificationRequestMobile(countryCode, type, true, type, loginType);
    }

    @Override
    public void onVerifyRequestEmail(String email, String loginType, String otp, String token) {

    }

    @Override
    public void onResendOtpEmail(String loginType, String email) {

    }
}