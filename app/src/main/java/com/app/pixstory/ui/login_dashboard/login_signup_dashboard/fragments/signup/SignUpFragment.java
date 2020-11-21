package com.app.pixstory.ui.login_dashboard.login_signup_dashboard.fragments.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.app.pixstory.R;
import com.app.pixstory.base.BaseFragment;
import com.app.pixstory.core.dialog.otp.Verification;
import com.app.pixstory.core.social.BaseGoogleLogin;
import com.app.pixstory.core.social.BaseLinkedInLogin;
import com.app.pixstory.databinding.FragmentSignUpBinding;
import com.app.pixstory.ui.dashboard.DashboardActivity;
import com.app.pixstory.ui.login_dashboard.login_signup_dashboard.LoginViewModel;
import com.app.pixstory.ui.login_dashboard.login_signup_dashboard.SignupViewModel;
import com.app.pixstory.ui.login_dashboard.login_signup_dashboard.fragments.login.LoginNavigator;
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
import java.util.Arrays;
import java.util.HashMap;

import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;
import static com.app.pixstory.utils.Constants.FROM_SIGNUP;
import static com.app.pixstory.utils.Constants.LOGIN_TYPE_EMAIL_ID;
import static com.app.pixstory.utils.Constants.LOGIN_TYPE_MOBILE_NUMBER;

public class SignUpFragment extends BaseFragment<FragmentSignUpBinding, SignupViewModel>
        implements View.OnClickListener, SignUpNavigator, Verification.VerificationCallback {
    private FragmentSignUpBinding binding;
    private SignupViewModel viewModel;
    private CallbackManager callbackManager;
    private static final String PUBLIC_PROFILE = "public_profile";
    private static final String EMAIL = "email";
    private String loginTypeStr = LOGIN_TYPE_MOBILE_NUMBER;

    @Override
    protected void init() {
        viewModel.setNavigator(this);
        setUpFacebook();
        initialization();
        clickListener();

        viewModel.getLoading().observe(getViewLifecycleOwner(), o -> {
            if((Boolean) o)
            {
                showSimmerLoader(DEFAULT_LOADER);
            }
            else
            {
                hideSimmerLoader();
            }
        });

    }

    @Override
    protected Class<SignupViewModel> setViewModel() {
        return SignupViewModel.class;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_sign_up;
    }

    @Override
    protected void getBinding(FragmentSignUpBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(SignupViewModel viewModel) {
        this.viewModel = viewModel;
    }


    private void initialization() {
        binding.layoutMobile.troubleLogin.setVisibility(View.GONE);
    }

    private void clickListener() {
        binding.proceed.setOnClickListener(this);
        binding.facebookLogin.setOnClickListener(this);
        binding.btnSignIn.setOnClickListener(this);
        binding.googleLogin.setOnClickListener(this);
        binding.emailLogin.setOnClickListener(this);
        binding.skip.setOnClickListener(this);
        binding.tnc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.proceed:
                signUpProceed();
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
            case R.id.email_login:
                emailLogin();
                break;
            case R.id.skip:
                viewModel.skipUser(getContext());
                break;
            case R.id.tnc:
                GlobalMethods.intentMethod(getContext(), TermsAndConditionActivity.class);
                break;
        }
    }

    private void signUpProceed() {
        switch (loginTypeStr) {
            case LOGIN_TYPE_MOBILE_NUMBER:
                if (GlobalMethods.validateMobileValidation(binding.layoutMobile.mobile, binding.layoutMobile.ccp)) {
                    String countryCode = binding.layoutMobile.ccp.getDefaultCountryCode();
                    if(!countryCode.contains("+")){
                        countryCode = "+" + countryCode;
                    }
                    //API to request OTP for mobile
                    viewModel.verificationRequestMobile(countryCode, binding.layoutMobile.mobile.getText() != null?
                            binding.layoutMobile.mobile.getText().toString().trim():"", false, Constants.TYPE_SIGNUP);
                }
                break;
            case LOGIN_TYPE_EMAIL_ID:
                if (GlobalMethods.validateEmailValidation(getContext(), binding.layoutEmail.email)) {
                    // viewModel.emailSignUp(getContext(), LOGIN_TYPE_EMAIL_ID, Objects.requireNonNull(binding.layoutEmail.email.getText()).toString().trim());

                    viewModel.sendEmailOtp(String.valueOf(binding.layoutEmail.email.getText()), Constants.TYPE_SIGNUP, false);
                }
                break;
        }
    }

    private void emailLogin() {

        loginTypeStr = binding.email.getText().toString().trim().toLowerCase();

        if (loginTypeStr.equals(LOGIN_TYPE_EMAIL_ID)) {
            binding.layoutMobile.layoutMobile.setVisibility(View.GONE);
            binding.layoutEmail.layoutEmail.setVisibility(View.VISIBLE);
            binding.email.setText(getResources().getString(R.string.mobile));
            binding.email.setTextColor(getResources().getColor(R.color.black));
            binding.emailImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_mobile));
        } else {
            binding.layoutMobile.layoutMobile.setVisibility(View.VISIBLE);
            binding.layoutEmail.layoutEmail.setVisibility(View.GONE);
            binding.email.setText(getResources().getString(R.string.email));
            binding.email.setTextColor(getResources().getColor(R.color.email));
            binding.emailImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_email));
        }
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
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Navigator---------------------

    @Override
    public void openAppUpgrade(String versionName, int versionCode) {

    }

    @Override
    public void onVerificationResponse(boolean status, String countryCode, String mobile, String message, boolean resendOtp) {
        if (status) {
            if (message != null) {
                getBaseActivity().showToast(message);
            } else {
                getBaseActivity().showToast(R.string.otp_send_message);

            }
            Verification.verify(getBaseActivity(), this, countryCode, mobile, "", LOGIN_TYPE_MOBILE_NUMBER, resendOtp, "", FROM_SIGNUP);
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

            Verification.verify(getBaseActivity(), this, "", "", email, LOGIN_TYPE_EMAIL_ID, false, token, FROM_SIGNUP);
        } else {
            getBaseActivity().showToast(message);
        }
    }
    @Override
    public void openSignupEmail(String email) {
        if (getBaseActivity() != null) {
            getBaseActivity().startActivity(HelpUsActivity.class,
                    Bundles.getInstance().setSignUpEmail(LOGIN_TYPE_EMAIL_ID, email));
            getBaseActivity().finish();
        }
    }

    @Override
    public void openInterest() {
        //TODO open interest
        if(getBaseActivity() != null) {
            getBaseActivity().startActivity(HighlightYourInterests.class, Bundles.getInstance().setSignUpType(loginTypeStr));
            getBaseActivity().finish();
        }
    }

    @Override
    public void openHomeActivity() {
        //TODO open home activity
        if(getBaseActivity() != null) {
            getBaseActivity().startActivity(DashboardActivity.class);
            getBaseActivity().finish();
        }
    }

    @Override
    public void openSignup(String countryCode, String mobile) {
        if(getBaseActivity() != null) {
            getBaseActivity().startActivity(HelpUsActivity.class,
                    Bundles.getInstance().setSignUpData(loginTypeStr, countryCode, mobile));
            getBaseActivity().finish();
        }
    }

    @Override
    public void openSignupFromSocial(String loginType, HashMap<String, String> data) {
        if(getBaseActivity() != null) {
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
        viewModel.verificationRequestMobile(countryCode, type, true, type);
    }

    @Override
    public void onVerifyRequestEmail(String email, String loginType, String otp, String token) {
        viewModel.onVerifyOtpEmail(email, otp, token, Constants.VERIFY_TYPE_REGISTER);
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
