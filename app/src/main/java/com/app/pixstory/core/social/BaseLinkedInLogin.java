package com.app.pixstory.core.social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;

public class BaseLinkedInLogin {

    private static BaseLinkedInLogin baseLinkedInLogin;
    private BaseLinkedInListener callback;
    private Context context;

    private static final String host = "api.linkedin.com";
    // private static final String topCardUrl = "https://" + host + "/v2/me/~:" + "(email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";
  //  private static final String topCardUrl = "https://api.linkedin.com/v2/me?projection=(id,firstName,lastName,profilePicture(displayImage~:playableStreams))";
    private static final String topCardUrl = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,email-address)";
    private BaseLinkedInLogin(){

    }

    public interface BaseLinkedInListener {
        void onSuccessLinkedInLogin(JSONObject jsonObject);
    }

    public static BaseLinkedInLogin getInstance(){
        if (baseLinkedInLogin == null){
            baseLinkedInLogin = new BaseLinkedInLogin();
        }
        return baseLinkedInLogin;
    }

    public void setUp(Context context, BaseLinkedInListener callback){
        this.context = context;
        this.callback = callback;

    }


    public void signInLinkedIn(Activity activity){
        LISessionManager.getInstance(getApplicationContext()).init(activity, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                 Toast.makeText(getApplicationContext(), "success" + LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAuthError(LIAuthError error) {

                Toast.makeText(getApplicationContext(), "failed " + error.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }, true);
    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        LISessionManager.getInstance(getApplicationContext()).onActivityResult((Activity) context,
                requestCode, resultCode, data);

        linkededinApiHelper();
    }

    public void linkededinApiHelper(){
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(context, topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                  //  setprofile(result.getResponseDataAsJson());
                    callback.onSuccessLinkedInLogin(result.getResponseDataAsJson());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError error) {
                // ((TextView) findViewById(R.id.error)).setText(error.toString());

            }
        });
    }
}
