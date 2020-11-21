package com.app.pixstory.core.dialog.universal_search;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.app.pixstory.R;
import com.app.pixstory.databinding.AlertUniversalSearchBinding;
import com.app.pixstory.ui.universal_search.UniversalSearchActivity;
import com.app.pixstory.utils.GlobalMethods;

import java.util.ArrayList;
import java.util.Locale;

public class UniversalSearch {

    private static SpeechRecognizer recognizer;
    private static Intent intent = null;
    public static int REQUEST_MICROPHONE = 0;

    // Toggle to test errors
    private static boolean TEST_ERROR = false;

    // Reset each time startListening is called.
    private static long then;

    @SuppressLint("ClickableViewAccessibility")
    public static void universalSearch(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_MICROPHONE);
            } else {
                searchItem(activity);
            }
        } else {
            searchItem(activity);
        }

    }

    public static void searchItem(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        AlertUniversalSearchBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.alert_universal_search, null, false);
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(binding.getRoot());
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        if(dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            speech(activity, binding);

            binding.close.setOnClickListener(v -> dialog.dismiss());

            binding.ok.setOnClickListener(v -> {
                dialog.dismiss();
                GlobalMethods.intentMethod(activity, UniversalSearchActivity.class);
            });
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private static void speech(Activity activity, AlertUniversalSearchBinding binding) {
        binding.universalSearchButton.speak.setOnClickListener(v -> {
            if (TEST_ERROR || recognizer == null) {
                Log.i("SPEECH_TEST", "onClick: setting recognizer");

                intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, activity.getPackageName());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "blar");
                intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toString());

                recognizer = SpeechRecognizer.createSpeechRecognizer(activity);
                recognizer.setRecognitionListener(new RecognitionListener() {
                    @Override
                    public void onReadyForSpeech(Bundle params) {
                        binding.universalSearchButton.speak.setVisibility(View.GONE);
                        binding.universalSearchButton.speakNow.setVisibility(View.VISIBLE);
                        binding.type.setHint("Speak Now...");
                    }

                    @Override
                    public void onBeginningOfSpeech() {
                        binding.universalSearchButton.speak.setVisibility(View.GONE);
                        binding.universalSearchButton.speakNow.setVisibility(View.VISIBLE);
                        binding.type.setHint("Speaking...");
                    }

                    @Override
                    public void onRmsChanged(float rmsdB) {

                    }

                    @Override
                    public void onBufferReceived(byte[] buffer) {

                    }

                    @Override
                    public void onEndOfSpeech() {
                        binding.universalSearchButton.speak.setVisibility(View.GONE);
                        binding.universalSearchButton.speakNow.setVisibility(View.VISIBLE);
                        binding.type.setHint("Wait...");
                    }

                    @Override
                    public void onError(int error) {
                        binding.universalSearchButton.speak.setVisibility(View.VISIBLE);
                        binding.universalSearchButton.speakNow.setVisibility(View.GONE);
                        binding.type.setHint(R.string.type);
                    }

                    @Override
                    public void onResults(Bundle results) {
                        if (!TEST_ERROR) {
                            binding.universalSearchButton.speak.setVisibility(View.VISIBLE);
                            binding.universalSearchButton.speakNow.setVisibility(View.GONE);
                            Log.i("SPEECH_TEST", "onResults: destroying recognizer");

                            if (recognizer != null) {
                                recognizer.cancel();
                                recognizer.destroy();
                                recognizer = null;

                                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                                //displaying the first match
                                if (matches != null && !matches.isEmpty())
                                    binding.type.setText(matches.get(0));
                            }

                        }
                    }

                    @Override
                    public void onPartialResults(Bundle partialResults) {

                    }

                    @Override
                    public void onEvent(int eventType, Bundle params) {

                    }
                });
            }

            then = System.currentTimeMillis();
            recognizer.startListening(intent);
        });
    }
}
