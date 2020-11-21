package com.app.pixstory.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseApplication;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

public class GlobalMethods {

    public static void intentMethod(Context context, Class cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);

    }

    public static void intentMethodWithValue(Context context, String loginType, String value, Class cls) {
        Intent intent = new Intent(context, cls);
        intent.putExtra("type", loginType);
        intent.putExtra("value", value);
        context.startActivity(intent);
    }

    // send data vai bundle

    public static void intentMethodWithBundle(Context context, Bundle bundle, Class cls) {
        Intent intent = new Intent(context, cls);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    // Validation for Username and Password
    public static boolean validateUsernamePassword(Context context, EditText username, EditText password) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.shake);
        boolean status = true;
        if (username.getText().toString().trim().equals("") && password.getText().toString().trim().equals("")) {
            status = false;
            username.startAnimation(animation);
            password.startAnimation(animation);
        } else if (username.getText().toString().trim().equals("") && username.length() > 7 && username.length() < 17){
            status = false;
            username.startAnimation(animation);
        } else if (password.getText().toString().trim().equals("")) {
            status = false;
            password.startAnimation(animation);
        }
        return status;
    }

    // Validation for Mobile
   /* public static boolean validateMobileValidation(EditText username, CountryCodePicker countryCodePicker) {
        countryCodePicker.registerPhoneNumberTextView(username);
        Animation animation = AnimationUtils.loadAnimation(username.getContext(), R.anim.shake);
        boolean status = true;

        if (username.getText().toString().trim().equals("")) {
            status = false;
            username.startAnimation(animation);
        } else if (!countryCodePicker.isValid()) {
            status = false;
            Toast.makeText(username.getContext(), "Number " + countryCodePicker.getFullNumber() +
                    " not valid!!!", Toast.LENGTH_SHORT).show();
        }
        return status;
    }*/
    // Validation for Mobile
    public static boolean validateMobileValidation(EditText username, CountryCodePicker countryCodePicker) {
        //countryCodePicker.registerPhoneNumberTextView(username);
        Animation animation = AnimationUtils.loadAnimation(username.getContext(), R.anim.shake);
        boolean status = true;

        if (username.getText().toString().trim().equals("")) {
            status = false;
            username.startAnimation(animation);
        }
        else if(countryCodePicker.getDefaultCountryCode().isEmpty())
        {
            status = false;
            Toast.makeText(username.getContext(), "Enter valid country code", Toast.LENGTH_SHORT).show();
        }
//        else if (!countryCodePicker.isValid()) {
//            status = false;
//            Toast.makeText(username.getContext(), "Number " + countryCodePicker.getFullNumber() +
//                    " not valid!!!", Toast.LENGTH_SHORT).show();
//        }
        return status;
    }

    // Validation for Email
    public static boolean validateEmailValidation(Context context, EditText username) {
        boolean isValidEmail = Utils.checkEmailForValidity(username.getText().toString().trim());
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.shake);
        boolean status = true;

        if (username.getText().toString().trim().equals("")) {
            status = false;
            username.startAnimation(animation);
        } else if (!isValidEmail) {
            status = false;
            Toast.makeText(context, "Invalid Email address.", Toast.LENGTH_SHORT).show();
        }
        return status;
    }


    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) BaseApplication.getContext().getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void intentMethodWithoutBackstack(Context context, Class cls) {
        Intent intent = new Intent(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

    }

    public static boolean validatePassword(Context context, EditText password) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.shake);
        boolean status = true;
        if (password.getText().toString().trim().equals("")) {
            status = false;
            password.startAnimation(animation);
        }
        return status;
    }

    public static boolean validateEmailUsernameValidation(Context context, EditText email, EditText username) {
        boolean isValidEmail = Utils.checkEmailForValidity(email.getText().toString().trim());
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.shake);
        boolean status = true;

        if (email.getText().toString().trim().equals("")) {
            status = false;
            email.startAnimation(animation);
        } else if (!isValidEmail) {
            status = false;
            Toast.makeText(context, "Invalid Email address.", Toast.LENGTH_SHORT).show();
        }
        else if (username.getText().toString().trim().equals("") && username.length() > 7 && username.length() < 17) {
            status = false;
            username.startAnimation(animation);
        }
        return status;
    }


}
