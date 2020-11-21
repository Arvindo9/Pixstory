package com.app.pixstory.ui.login_dashboard.login_signup_dashboard.fragments.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.Observer;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseFragment;
import com.app.pixstory.core.dialog.otp.Verification;
import com.app.pixstory.core.social.BaseGoogleLogin;
import com.app.pixstory.core.social.BaseLinkedInLogin;
import com.app.pixstory.databinding.FragmentLoginBinding;
import com.app.pixstory.ui.dashboard.DashboardActivity;
import com.app.pixstory.ui.login_dashboard.forgot_password.ForgotPasswordActivity;
import com.app.pixstory.ui.login_dashboard.login_signup_dashboard.LoginViewModel;
import com.app.pixstory.ui.login_dashboard.login_signup_dashboard.fragments.signup.help_us.HelpUsActivity;
import com.app.pixstory.ui.login_dashboard.login_signup_dashboard.fragments.signup.interest.HighlightYourInterests;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.GlobalMethods;
import com.app.pixstory.utils.util.Bundles;
import com.app.pixstory.utils.web_view.TermsAndConditionActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.linkedin.platform.LISessionManager;

import java.util.Arrays;
import java.util.HashMap;

import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;
import static com.app.pixstory.utils.Constants.FROM_LOGIN;
import static com.app.pixstory.utils.Constants.LOGIN_TYPE_EMAIL_ID;
import static com.app.pixstory.utils.Constants.LOGIN_TYPE_MOBILE_NUMBER;
import static com.app.pixstory.utils.Constants.LOGIN_TYPE_USER;
import static com.facebook.FacebookSdk.getApplicationContext;

public class LoginFragment extends BaseFragment<FragmentLoginBinding, LoginViewModel>
        implements View.OnClickListener, LoginNavigator, Verification.VerificationCallback {
    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;
    private CallbackManager callbackManager;
    private static final String PUBLIC_PROFILE = "public_profile";
    private static final String EMAIL = "email";
    private String loginTypeStr = LOGIN_TYPE_USER;

    private void initialization() {
        binding.layoutMobile.troubleLogin.setVisibility(View.VISIBLE);
        binding.layoutEmail.troubleLogin.setVisibility(View.VISIBLE);
        binding.layoutUser.troubleLogin.setVisibility(View.VISIBLE);
    }

    private void clickListener() {
        binding.layoutUser.troubleLogin.setOnClickListener(this);
        binding.layoutEmail.troubleLogin.setOnClickListener(this);
        binding.layoutMobile.troubleLogin.setOnClickListener(this);
        // facebook login
        binding.facebookLogin.setOnClickListener(this);
        // google login
        binding.googleLogin.setOnClickListener(this);
        // linkedIn login
        binding.linkedinLogin.setOnClickListener(this);
        // mobile login
        binding.mobileLogin.setOnClickListener(this);
        // email login
        binding.emailLogin.setOnClickListener(this);
        // login
        binding.login.setOnClickListener(this);
        // skip
        binding.skip.setOnClickListener(this);
        binding.tnc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trouble_login:
                GlobalMethods.intentMethod(getContext(), ForgotPasswordActivity.class);
                break;
            case R.id.facebook_login:
                binding.loginButton.performClick();
                break;
            case R.id.google_login:
                BaseGoogleLogin.getInstance().signInGoogle();
                break;
            case R.id.linkedin_login:
                BaseLinkedInLogin.getInstance().signInLinkedIn(getActivity());
                break;
            case R.id.mobile_login:
                mobileLogin();
                break;
            case R.id.email_login:
                emailLogin();
                break;
            case R.id.login:
                loginButton();
                break;
            case R.id.skip:
                viewModel.skipUser(getContext());
                break;
            case R.id.tnc:
                GlobalMethods.intentMethod(getContext(), TermsAndConditionActivity.class);
                break;

        }
    }

    private void mobileLogin() {
        if (binding.mobile.getText().toString().trim().equalsIgnoreCase(LOGIN_TYPE_MOBILE_NUMBER)) {
            loginTypeStr = LOGIN_TYPE_MOBILE_NUMBER;
        } else {
            loginTypeStr = binding.mobile.getText().toString().trim();
        }

        if (loginTypeStr.equals(LOGIN_TYPE_MOBILE_NUMBER)) {
            binding.layoutMobile.layoutMobile.setVisibility(View.VISIBLE);
            binding.layoutUser.layoutUser.setVisibility(View.GONE);
            binding.layoutEmail.layoutEmail.setVisibility(View.GONE);
            binding.mobile.setText(getResources().getString(R.string.signed_user));
            binding.mobile.setTextColor(getResources().getColor(R.color.black));
            binding.mobileImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_black_24dp));
            binding.email.setText(getResources().getString(R.string.email));
            binding.email.setTextColor(getResources().getColor(R.color.email));
            binding.emailImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_email));
        } else {
            binding.layoutMobile.layoutMobile.setVisibility(View.GONE);
            binding.layoutUser.layoutUser.setVisibility(View.VISIBLE);
            binding.layoutEmail.layoutEmail.setVisibility(View.GONE);
            binding.mobile.setText(getResources().getString(R.string.mobile));
            binding.mobile.setTextColor(getResources().getColor(R.color.black));
            binding.mobileImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_mobile));
            binding.email.setText(getResources().getString(R.string.email));
            binding.email.setTextColor(getResources().getColor(R.color.email));
            binding.emailImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_email));
        }
    }

    private void emailLogin() {
        if (binding.email.getText().toString().trim().equalsIgnoreCase(LOGIN_TYPE_EMAIL_ID)) {
            loginTypeStr = LOGIN_TYPE_EMAIL_ID;
        } else {
            loginTypeStr = binding.email.getText().toString().trim();
        }

        if (loginTypeStr.equals(LOGIN_TYPE_EMAIL_ID)) {
            binding.layoutMobile.layoutMobile.setVisibility(View.GONE);
            binding.layoutUser.layoutUser.setVisibility(View.GONE);
            binding.layoutEmail.layoutEmail.setVisibility(View.VISIBLE);
            binding.mobile.setText(getResources().getString(R.string.mobile));
            binding.mobileImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_mobile));
            binding.email.setText(getResources().getString(R.string.signed_user));
            binding.email.setTextColor(getResources().getColor(R.color.black));
            binding.emailImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_black_24dp));

        } else {
            binding.layoutMobile.layoutMobile.setVisibility(View.GONE);
            binding.layoutUser.layoutUser.setVisibility(View.VISIBLE);
            binding.layoutEmail.layoutEmail.setVisibility(View.GONE);
            binding.mobile.setText(getResources().getString(R.string.mobile));
            binding.mobileImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_mobile));
            binding.email.setText(getResources().getString(R.string.email));
            binding.email.setTextColor(getResources().getColor(R.color.email));
            binding.emailImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_email));
        }
    }

    private void loginButton() {
        switch (loginTypeStr) {
            case LOGIN_TYPE_MOBILE_NUMBER:
                if (GlobalMethods.validateMobileValidation(binding.layoutMobile.mobile, binding.layoutMobile.ccp)) {
                    String countryCode = binding.layoutMobile.ccp.getDefaultCountryCode();
                    if (!countryCode.contains("+")) {
                        countryCode = "+" + countryCode;
                    }
                    //API to request OTP for mobile
                    //showProgress();

                    viewModel.verificationRequestMobile(countryCode, binding.layoutMobile.mobile.getText() != null ?
                            binding.layoutMobile.mobile.getText().toString().trim() : "", false, Constants.TYPE_LOGIN);
                }
                break;
            case LOGIN_TYPE_EMAIL_ID:
                if (GlobalMethods.validateEmailValidation(getContext(), binding.layoutEmail.email)) {
                    //  Verification.verify(getBaseActivity(), null, "",binding.layoutEmail.email.getText().toString().trim(), loginTypeStr, false);

                    //showProgress();
                    viewModel.sendEmailOtp(String.valueOf(binding.layoutEmail.email.getText()), Constants.TYPE_LOGIN, false);

                }
                break;
            case LOGIN_TYPE_USER:
                if (GlobalMethods.validateUsernamePassword(getContext(), binding.layoutUser.username,
                        binding.layoutUser.password)) {

                    //showProgress();
                    viewModel.onLoginWithUsernamePassword(LOGIN_TYPE_USER,String.valueOf(binding.layoutUser.username.getText()), String.valueOf(binding.layoutUser.password.getText()));

                    // GlobalMethods.intentMethod(getContext(), DashboardActivity.class);
                }
                break;
        }
    }

    @Override
    protected void init() {
        viewModel.setNavigator(this);
        initialization();
        clickListener();
        setUpFacebook();


        viewModel.getLoading().observe(getViewLifecycleOwner(), new Observer() {
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

    void tmp() {
        if (getBaseActivity() != null) {
            getBaseActivity().startActivity(HelpUsActivity.class,
                    Bundles.getInstance().setSignUpDataSocial(Constants.LOGIN_TYPE_FACEBOOK, null));
            getBaseActivity().finish();
        }
    }

    @Override
    protected Class<LoginViewModel> setViewModel() {
        return LoginViewModel.class;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_login;
    }

    @Override
    protected void getBinding(FragmentLoginBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(LoginViewModel viewModel) {
        this.viewModel = viewModel;
    }

    private void setUpFacebook() {
        callbackManager = CallbackManager.Factory.create();

        binding.loginButton.setReadPermissions(Arrays.asList(PUBLIC_PROFILE, EMAIL));
        // If you are using in a fragment, call
        binding.loginButton.setFragment(this);

        // Callback registration
        binding.loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        (object, response) -> {
                            viewModel.getFacebookData(getContext(), object);
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location"); // Parameters que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                // App code
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(getActivity(),
                requestCode, resultCode, data);
    }

    @Override
    public void openAppUpgrade(String versionName, int versionCode) {
        //TODO open upgrade dialog
    }

    @Override
    public void onVerificationResponse(boolean status, String countryCode, String mobile, String message, boolean resendOtp) {
        if (status) {
            if (message != null) {
                getBaseActivity().showToast(message);
            } else {
                getBaseActivity().showToast(R.string.otp_send_message);
            }
//            Verification.verify(getBaseActivity(), this::onVerifyRequest, countryCode, mobile, loginTypeStr);
            Verification.verify(getBaseActivity(), this, countryCode, mobile, "", LOGIN_TYPE_MOBILE_NUMBER, resendOtp, "",FROM_LOGIN);
        } else {
            getBaseActivity().showToast(message);
        }
    }

    @Override
    public void onEmailVerificationResponse(boolean b, String email, String message, boolean resendOtp, String token) {
        if (b) {
            if (message != null) {
                getBaseActivity().showToast(message);
            } else {
                getBaseActivity().showToast(R.string.otp_send_message);
            }

            Verification.verify(getBaseActivity(), this, "", "", email, LOGIN_TYPE_EMAIL_ID, false, token,FROM_LOGIN);
        } else {
            getBaseActivity().showToast(message);
        }
    }


    @Override
    public void openSignupEmail(String email) {

    }


    @Override
    public void openInterest() {
        //TODO open interest
        if (getBaseActivity() != null) {
            getBaseActivity().startActivity(HighlightYourInterests.class, Bundles.getInstance().setSignUpType(loginTypeStr));
            getBaseActivity().finish();
        }
    }

    @Override
    public void openHomeActivity() {
        hideSimmerLoader();
        //TODO open home activity
        if (getBaseActivity() != null) {
            getBaseActivity().startActivity(DashboardActivity.class);
        }
    }

    @Override
    public void openSignup(String countryCode, String mobile) {
        if (getBaseActivity() != null) {
            getBaseActivity().startActivity(HelpUsActivity.class,
                    Bundles.getInstance().setSignUpData(loginTypeStr, countryCode, mobile));
            getBaseActivity().finish();
        }
    }

    @Override
    public void openSignupFromSocial(String loginType, HashMap<String, String> data) {
        if (getBaseActivity() != null) {
            getBaseActivity().startActivity(HelpUsActivity.class,
                    Bundles.getInstance().setSignUpDataSocial(loginType, data));
            getBaseActivity().finish();
        }
    }


    @Override
    public void message(int message) {
        getBaseActivity().showToast(message);
    }

    @Override
    public void message(String message) {
        getBaseActivity().showToast(message);
    }

    @Override
    public void onVerifyRequestMobile(String countryCodePicker, String mobile, String loginType, String otp) {
        //API to request OTP for mobile
        viewModel.onVerifyOtpMobile(countryCodePicker, mobile, otp);
    }

    @Override
    public void onResendOtpMobile(String loginType, String countryCode, String mobile, String type) {
        //resend otp
        viewModel.verificationRequestMobile(countryCode, mobile, true, type);
    }

    @Override
    public void onVerifyRequestEmail(String email, String loginType, String otp, String token) {

        viewModel.onVerifyOtpEmail(email, otp, token, Constants.VERIFY_TYPE_LOGIN);
    }


    @Override
    public void onResendOtpEmail(String loginType, String email) {
        viewModel.sendEmailOtp(email, loginType, true);
    }

    @Override
    public void skipSignUp() {

    }

    @Override
    public void skipVerification(String mobile, String countryCode, String otp) {

    }
}
