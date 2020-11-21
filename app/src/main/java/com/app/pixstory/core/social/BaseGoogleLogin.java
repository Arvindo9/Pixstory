package com.app.pixstory.core.social;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.app.pixstory.R;
import com.app.pixstory.ui.login_dashboard.login_signup_dashboard.LoginSignUpUserDashboard;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;

public class BaseGoogleLogin implements GoogleApiClient.OnConnectionFailedListener {


    // google
    private static final String TAG = LoginSignUpUserDashboard.class.getSimpleName();
    public static final int RC_SIGN_IN = 007;
    private ProgressDialog mProgressDialog;
    private BaseGoogleLoginListener callBack;

    private GoogleApiClient googleApiClient;

    private static BaseGoogleLogin baseSocialLogin;

    private Context context;

    private BaseGoogleLogin() {

    }

    public interface BaseGoogleLoginListener {
        void onSuccessGoogleLogin(String googleId, String personName, String email, String firstName, String lastName);
    }

    public static BaseGoogleLogin getInstance() {
        if (baseSocialLogin == null) {
            baseSocialLogin = new BaseGoogleLogin();
        }
        return baseSocialLogin;
    }

    public void setup(Context context, BaseGoogleLoginListener callBack) {
        this.context = context;
        this.callBack = callBack;
        setUpGoogle();
    }

    public void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        ((Activity) context).startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            int statusCode = result.getStatus().getStatusCode();
            handleSignInResult(result);

        }
    }

    private void setUpGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage((FragmentActivity) context, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        onStart();
    }

    public void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String firstName = acct.getGivenName();
            String lastName = acct.getFamilyName();
            //  String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            String personId = acct.getId();

            Log.e(TAG, "Name: " + personName + ", email: " + email);
            Log.e(TAG, "personId: " + personId);

            callBack.onSuccessGoogleLogin(personId, personName, email, firstName, lastName);

            //  viewModel.doLogInWithGoogle(personName, email);

            /*txtName.setText(personName);
            txtEmail.setText(email);
            Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);

            updateUI(true);*/
        } else {
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
    }

    public void onStart() {
        if (googleApiClient != null) {
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);


            if (opr.isDone()) {
                // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                // and the GoogleSignInResult will be available instantly.
                Log.d(TAG, "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                // If the user has not previously signed in on this device or the sign-in has expired,
                // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                // single sign-on will occur in this branch.
                showProgressDialog();
                opr.setResultCallback(googleSignInResult -> {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                });
            }
        }
    }

    private void showProgressDialog() {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.setMessage(context.getString(R.string.loading));
                mProgressDialog.setIndeterminate(true);
            }

            mProgressDialog.show();
        }catch (Exception ignore){}
    }

    private void hideProgressDialog() {
        try{
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.hide();
            }
        }catch (Exception ignore){}
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
