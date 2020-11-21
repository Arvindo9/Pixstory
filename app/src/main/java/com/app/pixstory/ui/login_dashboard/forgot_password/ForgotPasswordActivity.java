package com.app.pixstory.ui.login_dashboard.forgot_password;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.databinding.ActivityForgotPasswordBinding;
import com.app.pixstory.utils.GlobalMethods;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.Objects;

import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;
import static com.app.pixstory.utils.Constants.LOGIN_TYPE_EMAIL_ID;
import static com.app.pixstory.utils.Constants.LOGIN_TYPE_MOBILE_NUMBER;
import static com.app.pixstory.utils.Constants.VERIFY_TYPE_FORGET;

public class ForgotPasswordActivity extends BaseActivity<ActivityForgotPasswordBinding, ForgotPasswordViewModel> implements View.OnClickListener, ForgetPasswordNavigator {

    ActivityForgotPasswordBinding binding;
    ForgotPasswordViewModel viewModel;
    public BottomSheetBehavior forgotPasswordBehaviour, verificationCodeBehaviour,
            changePasswordBehaviour, otpVerifiedBehaviour;
    private String loginTypeStr = LOGIN_TYPE_MOBILE_NUMBER;
    String countryCode, phone, token, otp,username,email;
    boolean showPassword, showConfirmPassword;
    String cp_type_forget, cp_token, cp_countryCode, cp_phone, cp_email, cp_username,cp_type;
    private CountDownTimer timer;

    @Override
    public int getLayout() {
        return R.layout.activity_forgot_password;
    }

    @Override
    protected void getBinding(ActivityForgotPasswordBinding binding) {

        this.binding = binding;
    }

    @Override
    protected void getViewModel(ForgotPasswordViewModel viewModel) {

        this.viewModel = viewModel;
    }

    @Override
    protected Class<ForgotPasswordViewModel> setViewModel() {
        return ForgotPasswordViewModel.class;
    }

    @Override
    protected void init() {
        toolbar(binding.forgotPassword.layoutToolbar.toolbar);
        viewModel.setNavigator(this);
        onClickListener();
        forgotPassword();
        verificationCode();
        changePassword();
        otpVerified();

        viewModel.getLoading().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                if ((Boolean) o) {
                    showSimmerLoader(DEFAULT_LOADER);
                } else {
                    hideSimmerLoader();
                }
            }
        });

    }

    public void onClickListener() {
        binding.forgotPassword.forgotPassword.setOnClickListener(this);
        binding.bottomSheetForgotPassword.close.setOnClickListener(this);
        binding.bottomSheetVerificationCode.closeVerification.setOnClickListener(this);
        binding.bottomSheetForgotPassword.forgotPasswordNext.setOnClickListener(this);
        binding.bottomSheetVerificationCode.verify.setOnClickListener(this);
        binding.bottomSheetVerificationCode.resendOtp.setOnClickListener(this);
        binding.bottomSheetChangePassword.closeChangePassword.setOnClickListener(this);
        binding.bottomSheetChangePassword.changePasswordButton.setOnClickListener(this);
        binding.bottomSheetOtpVerified.closeOtpVerified.setOnClickListener(this);
        binding.bottomSheetForgotPassword.email.setOnClickListener(this);
        binding.bottomSheetChangePassword.showNewPassword.setOnClickListener(this);
        binding.bottomSheetChangePassword.showReEnterPassword.setOnClickListener(this);
        binding.bottomSheetOtpVerified.done.setOnClickListener(this);
    }

    private void forgotPassword() {
        forgotPasswordBehaviour = BottomSheetBehavior.from(binding.bottomSheetForgotPassword.bottomSheetLl);
        // set callback for changes
        forgotPasswordBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    forgotPasswordBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.forgotPassword.blur.setVisibility(View.VISIBLE);
                binding.forgotPassword.blur.setAlpha(slideOffset);
            }
        });

    }

    private void verificationCode() {
        verificationCodeBehaviour = BottomSheetBehavior.from(binding.bottomSheetVerificationCode.verificationCode);
        // set callback for changes
        verificationCodeBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    verificationCodeBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.forgotPassword.blur.setVisibility(View.VISIBLE);
                binding.forgotPassword.blur.setAlpha(slideOffset);
                binding.bottomSheetVerificationCode.closeVerification.animate().scaleX(0.05f + slideOffset).scaleY(0.05f + slideOffset).setDuration(0).start();
            }
        });

    }

    private void changePassword() {
        changePasswordBehaviour = BottomSheetBehavior.from(binding.bottomSheetChangePassword.changePassword);

        // set callback for changes
        changePasswordBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    changePasswordBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.forgotPassword.blur.setVisibility(View.VISIBLE);
                binding.forgotPassword.blur.setAlpha(slideOffset);
                binding.bottomSheetChangePassword.closeChangePassword.animate().scaleX(0.05f + slideOffset).scaleY(0.05f + slideOffset).setDuration(0).start();
            }
        });

    }

    private void otpVerified() {
        otpVerifiedBehaviour = BottomSheetBehavior.from(binding.bottomSheetOtpVerified.otpVerified);
        // set callback for changes
        otpVerifiedBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    otpVerifiedBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.forgotPassword.blur.setVisibility(View.VISIBLE);
                binding.forgotPassword.blur.setAlpha(slideOffset);
                binding.bottomSheetOtpVerified.closeOtpVerified.animate().scaleX(0.05f + slideOffset).scaleY(0.05f + slideOffset).setDuration(0).start();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgot_password:
                forgotPasswordBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.close:
                forgotPasswordBehaviour.setHideable(true);
                forgotPasswordBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.forgot_password_next:
                forgotPasswordButton(loginTypeStr);
                break;
            case R.id.close_verification:
                verificationCodeBehaviour.setHideable(true);
                verificationCodeBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.verify:

                if (loginTypeStr.equals(LOGIN_TYPE_MOBILE_NUMBER)) {
                    viewModel.onVerifyOtpMobile(LOGIN_TYPE_MOBILE_NUMBER, countryCode, phone, Objects.requireNonNull(binding.bottomSheetVerificationCode.firstPinView.getText()).toString(), token);
                } else {
                    viewModel.onVerifyOtpEmail(username,email, otp, token, VERIFY_TYPE_FORGET,LOGIN_TYPE_EMAIL_ID);
                }
                binding.bottomSheetVerificationCode.firstPinView.setText("");

                break;
            case R.id.close_change_password:
                changePasswordBehaviour.setHideable(true);
                changePasswordBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.change_password_button:

                if (!GlobalMethods.validatePassword(this, binding.bottomSheetChangePassword.newPassword)) {
                    showToast("Enter password");
                    return;
                }
                if (!GlobalMethods.validatePassword(this, binding.bottomSheetChangePassword.newReEnterPassword)) {
                    showToast("Enter confirm password");
                    return;
                }

                viewModel.updatePassword(String.valueOf(binding.bottomSheetChangePassword.newPassword.getText()), String.valueOf(binding.bottomSheetChangePassword.newReEnterPassword.getText()), cp_type, cp_token, cp_countryCode, cp_phone, cp_username, cp_email);

                break;
            case R.id.close_otp_verified:
                otpVerifiedBehaviour.setHideable(true);
                otpVerifiedBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.email:
                emailLogin();
                break;

            case R.id.done:
                finish();
                break;

            case R.id.show_new_password:

                if (!showPassword) {
                    showPassword = true;
                    binding.bottomSheetChangePassword.newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    showPassword = false;
                    binding.bottomSheetChangePassword.newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                binding.bottomSheetChangePassword.newPassword.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.bottomSheetChangePassword.newPassword.setSelection(binding.bottomSheetChangePassword.newPassword.getText().length());
                    }
                });

                break;

            case R.id.show_re_enter_password:

                if (!showConfirmPassword) {
                    showConfirmPassword = true;
                    binding.bottomSheetChangePassword.newReEnterPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    showConfirmPassword = false;
                    binding.bottomSheetChangePassword.newReEnterPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

                binding.bottomSheetChangePassword.newReEnterPassword.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.bottomSheetChangePassword.newReEnterPassword.setSelection(binding.bottomSheetChangePassword.newReEnterPassword.getText().length());
                    }
                });

                break;

            case R.id.resend_otp:

                if (binding.bottomSheetVerificationCode.waitForOtp.getText().toString().contentEquals(getText(R.string.done))) {
                    //resend otp
                    timer.cancel();
                    binding.bottomSheetVerificationCode.resendOtp.setVisibility(View.GONE);

                    if (loginTypeStr.equals(LOGIN_TYPE_MOBILE_NUMBER)) {
                        viewModel.verificationRequest(
                                loginTypeStr,
                                "",
                                "",
                                countryCode,
                                binding.bottomSheetForgotPassword.layoutMobile.mobile.getText() != null ? binding.bottomSheetForgotPassword.layoutMobile.mobile.getText().toString().trim() : "",
                                true);
                    } else {
                        viewModel.verificationRequest(
                                loginTypeStr,
                                binding.bottomSheetForgotPassword.layoutEmail.username.getText() != null ? binding.bottomSheetForgotPassword.layoutEmail.username.getText().toString().trim() : "",
                                binding.bottomSheetForgotPassword.layoutEmail.email.getText() != null ? binding.bottomSheetForgotPassword.layoutEmail.email.getText().toString().trim() : "",
                                "",
                                "",
                                true);
                    }

                }

                break;

        }
    }

    private void emailLogin() {
        loginTypeStr = binding.bottomSheetForgotPassword.email.getText().toString().trim().toLowerCase();

        if (loginTypeStr.equals(LOGIN_TYPE_EMAIL_ID)) {
            binding.bottomSheetForgotPassword.layoutMobile.layoutMobile.setVisibility(View.GONE);
            binding.bottomSheetForgotPassword.layoutEmail.layoutEmail.setVisibility(View.VISIBLE);
            binding.bottomSheetForgotPassword.email.setText(getResources().getString(R.string.mobile));
            binding.bottomSheetForgotPassword.enterRegistered.setText(getResources().getString(R.string.enter_registered_email_address));

        } else {
            binding.bottomSheetForgotPassword.layoutMobile.layoutMobile.setVisibility(View.VISIBLE);
            binding.bottomSheetForgotPassword.layoutEmail.layoutEmail.setVisibility(View.GONE);
            binding.bottomSheetForgotPassword.email.setText(getResources().getString(R.string.email));
            binding.bottomSheetForgotPassword.enterRegistered.setText(getResources().getString(R.string.enter_registered_mobile_number));
        }
    }

    private void forgotPasswordButton(String type) {

        if (type.equals(LOGIN_TYPE_MOBILE_NUMBER)) {

            phone = Objects.requireNonNull(binding.bottomSheetForgotPassword.layoutMobile.mobile.getText()).toString().trim();

            if (GlobalMethods.validateMobileValidation(binding.bottomSheetForgotPassword.layoutMobile.mobile, binding.bottomSheetForgotPassword.layoutMobile.ccp)) {
                countryCode = binding.bottomSheetForgotPassword.layoutMobile.ccp.getDefaultCountryCode();
                if (!countryCode.contains("+")) {
                    countryCode = "+" + countryCode;
                }

                viewModel.verificationRequest(type, "", "", countryCode, binding.bottomSheetForgotPassword.layoutMobile.mobile.getText() != null ?
                        binding.bottomSheetForgotPassword.layoutMobile.mobile.getText().toString().trim() : "", false);

                //binding.bottomSheetForgotPassword.layoutMobile.mobile.setText("");
            }
        } else {
            if (GlobalMethods.validateEmailUsernameValidation(this, binding.bottomSheetForgotPassword.layoutEmail.email, binding.bottomSheetForgotPassword.layoutEmail.username)) {

                email = String.valueOf(binding.bottomSheetForgotPassword.layoutEmail.email.getText());
                username = String.valueOf(binding.bottomSheetForgotPassword.layoutEmail.username.getText());
                viewModel.verificationRequest(type, username, email, "", "", false);

                /*binding.bottomSheetForgotPassword.layoutEmail.email.setText("");
                binding.bottomSheetForgotPassword.layoutEmail.username.setText("");*/

            }
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
        showToast(message);
    }

    @Override
    public void message(String message) {
        showToast(message);
    }

    @Override
    public void onVerificationResponse(boolean b, String type, String countryCode, String mobile, String message, boolean resendOtp, String token, String otp) {
        if (b) {
            if (message != null) {
                showToast(message);
            } else {
                showToast(R.string.otp_send_message);
            }

            if (timer != null) {
                timer.cancel();
            }
            startTimmer(binding);
            binding.bottomSheetVerificationCode.resendOtp.setVisibility(View.GONE);
            this.otp = otp;
            this.token = token;
            forgotPasswordBehaviour.setHideable(true);
            forgotPasswordBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            verificationCodeBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
            if (type.equals(LOGIN_TYPE_EMAIL_ID)) {
                binding.bottomSheetVerificationCode.verificationCodeMessage.setText(getResources().getString(R.string.enter_otp_email));
                binding.bottomSheetVerificationCode.changeVerificationType.setText(getResources().getString(R.string.change_email_address));
                binding.bottomSheetVerificationCode.resendOtp.setText(getResources().getString(R.string.resent_verification_mail));
                binding.bottomSheetVerificationCode.waitForOtp.setVisibility(View.GONE);

                binding.bottomSheetChangePassword.newPassword.setText("");
                binding.bottomSheetChangePassword.newReEnterPassword.setText("");

            } else {
                binding.bottomSheetVerificationCode.verificationCodeMessage.setText(getResources().getString(R.string.enter_otp_mobile) + " " + mobile);
                binding.bottomSheetVerificationCode.changeVerificationType.setText(getResources().getString(R.string.change_mobile_number));
                binding.bottomSheetVerificationCode.resendOtp.setText(getResources().getString(R.string.resent_opt));
                binding.bottomSheetVerificationCode.waitForOtp.setVisibility(View.VISIBLE);

                binding.bottomSheetForgotPassword.layoutMobile.mobile.setText("");
            }

        } else {
            showToast(message);
        }
    }

    @Override
    public void openChangePassword(String typeForget, String token, String countryCode, String phone,String username,String email,String type) {
        Log.i("", "" + token);

        cp_type_forget = typeForget;
        cp_token = token;
        cp_countryCode = countryCode;
        cp_phone = phone;
        cp_email = email;
        cp_username = username;
        cp_type = type;

        verificationCodeBehaviour.setHideable(true);
        verificationCodeBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        changePasswordBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    @Override
    public void passwordChanged() {
        changePasswordBehaviour.setHideable(true);
        changePasswordBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        otpVerifiedBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void startTimmer(ActivityForgotPasswordBinding binding) {
        // count down
        timer = new CountDownTimer(60000, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {

                binding.bottomSheetVerificationCode.waitForOtp.setText(binding.getRoot().getResources().getString(R.string.wait_for_otp) + " "
                        + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                binding.bottomSheetVerificationCode.waitForOtp.setText(R.string.done);
                binding.bottomSheetVerificationCode.resendOtp.setVisibility(View.VISIBLE);
            }

        };
        timer.start();
    }


}
