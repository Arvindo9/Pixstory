package com.app.pixstory.core.dialog.otp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.app.pixstory.R;
import com.app.pixstory.databinding.AlertVerificationOtpBinding;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.receiver.SmsReceiver;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

import static com.app.pixstory.utils.Constants.FROM_ACCOUNT;
import static com.app.pixstory.utils.Constants.FROM_HELP;
import static com.app.pixstory.utils.Constants.LOGIN_TYPE_EMAIL_ID;
import static com.app.pixstory.utils.Constants.LOGIN_TYPE_FACEBOOK;
import static com.app.pixstory.utils.Constants.LOGIN_TYPE_GOOGLE;
import static com.app.pixstory.utils.Constants.LOGIN_TYPE_MOBILE_NUMBER;

public class Verification {
    private static CountDownTimer timer;
    private static AlertVerificationOtpBinding binding;

    public interface VerificationCallback {

        void onVerifyRequestMobile(String countryCodePicker, String mobile, String loginType, String otp);

        void onResendOtpMobile(String loginType, String countryCode, String mobile, String type);

        void onVerifyRequestEmail(String email, String loginType, String otp, String token);

        void onResendOtpEmail(String loginType, String email);

        void skipSignUp();

        void skipVerification(String mobile, String countryCode, String otp);
    }

    public static void verify(Context context, VerificationCallback callback, String countryCode,
                              String mobile, String email, String loginType, boolean resendOtp, String token, String from) {

        if (resendOtp && binding != null) {
            startTimmer(binding);
            return;
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            binding = DataBindingUtil.inflate(inflater,
                    R.layout.alert_verification_otp, null, false);
            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setView(binding.getRoot());
            alert.setCancelable(false);
            final AlertDialog dialog = alert.create();
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            if (LOGIN_TYPE_EMAIL_ID.equals(loginType)) {
                if (from.equalsIgnoreCase(FROM_HELP)) {
                    binding.skip.setVisibility(View.VISIBLE);
                    binding.changeVerificationType.setVisibility(View.GONE);
                }
                binding.changeVerificationType.setText(R.string.change_email_address);
            } else if (LOGIN_TYPE_MOBILE_NUMBER.equals(loginType)) {
                if (from.equalsIgnoreCase(FROM_ACCOUNT)) {
                    binding.skip.setVisibility(View.VISIBLE);
                    binding.changeVerificationType.setVisibility(View.GONE);
                }
                binding.changeVerificationType.setText(R.string.change_mobile_number);
            } else if (LOGIN_TYPE_FACEBOOK.equals(loginType) || LOGIN_TYPE_GOOGLE.equals(loginType)) {
                binding.skip.setVisibility(View.VISIBLE);
            }

            binding.verificationCodeMessage.setText("Enter OTP received on" + " " + countryCode + "-" + " " + mobile);
            binding.changeVerificationType.setOnClickListener(v -> dialog.dismiss());
            binding.close.setOnClickListener(view -> dialog.dismiss());
            binding.verify.setOnClickListener(view -> {


                if (binding.firstPinView.getText() != null && binding.firstPinView.getText().toString().length() == 4) {
                    if (LOGIN_TYPE_EMAIL_ID.equals(loginType)) {
                        callback.onVerifyRequestEmail(email, loginType, binding.firstPinView.getText().toString(), token);
                    } else if (LOGIN_TYPE_MOBILE_NUMBER.equals(loginType) || LOGIN_TYPE_FACEBOOK.equals(loginType) || LOGIN_TYPE_GOOGLE.equals(loginType)) {
                        callback.onVerifyRequestMobile(countryCode, mobile, loginType, binding.firstPinView.getText().toString());
                        if (from.equalsIgnoreCase(FROM_ACCOUNT))
                            dialog.dismiss();
                    }
                }

                //  dialog.dismiss();

            });
            binding.resentOtp.setOnClickListener(v -> {
                if (binding.waitForOtp.getText().toString().contentEquals(context.getText(R.string.done))) {
                    //resend otp
                    timer.cancel();
                    binding.resentOtp.setVisibility(View.GONE);

                    if (LOGIN_TYPE_EMAIL_ID.equals(loginType)) {
                        callback.onResendOtpEmail(loginType, email);
                    } else if (LOGIN_TYPE_MOBILE_NUMBER.equals(loginType)) {
                        callback.onResendOtpMobile(loginType, countryCode, mobile, Constants.TYPE_LOGIN);
                    }

                }
            });

            binding.skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (from.equalsIgnoreCase(FROM_ACCOUNT))
                        callback.skipVerification(mobile, countryCode, Objects.requireNonNull(binding.firstPinView.getText()).toString().trim());
                    else
                        callback.skipSignUp();
                    dialog.dismiss();
                }
            });

            SmsRetrieverClient client = SmsRetriever.getClient(context);
            Task<Void> task = client.startSmsRetriever();
            task.addOnSuccessListener(aVoid -> {
                // Android will provide message once receive. Start your broadcast receiver.
                IntentFilter filter = new IntentFilter();
                filter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
                context.registerReceiver(new SmsReceiver(), filter);
            });
            task.addOnFailureListener(e -> {
                // Failed to start retriever, inspect Exception for more details
            });

            binding.resentOtp.setVisibility(View.GONE);
            if (timer != null) {
                timer.cancel();
            }
            startTimmer(binding);
        }
    }

    private static void startTimmer(AlertVerificationOtpBinding binding) {
        // count down
        timer = new CountDownTimer(60000, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                binding.waitForOtp.setText(binding.getRoot().getResources().getString(R.string.wait_for_otp) + " "
                        + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                binding.waitForOtp.setText(R.string.done);
                binding.resentOtp.setVisibility(View.VISIBLE);
            }

        };
        timer.start();
    }


}
