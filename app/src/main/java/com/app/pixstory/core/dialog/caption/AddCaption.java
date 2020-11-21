package com.app.pixstory.core.dialog.caption;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.app.pixstory.R;
import com.app.pixstory.databinding.AlertAddCaptionBinding;

import java.util.ArrayList;
import java.util.Locale;

public class AddCaption {
    private static SpeechRecognizer recognizer;
    private static Intent intent = null;
    public static int REQUEST_MICROPHONE = 0;

    // Toggle to test errors
    private static boolean TEST_ERROR = false;

    // Reset each time startListening is called.
    private static long then;

    @SuppressLint("ClickableViewAccessibility")
    public static void addCaption(Activity activity, TextView addCaption, TextView photoCredits, String edit_caption, String edit_credit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_MICROPHONE);
            } else {
                searchItem(activity, addCaption, photoCredits, edit_caption, edit_credit);
            }
        } else {
            searchItem(activity, addCaption, photoCredits, edit_caption, edit_credit);
        }

    }

    public static void searchItem(Activity activity, TextView addCaption, TextView photoCredits, String edit_caption, String edit_credit) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            AlertAddCaptionBinding binding = DataBindingUtil.inflate(inflater, R.layout.alert_add_caption, null, false);
            final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            alert.setView(binding.getRoot());
            alert.setCancelable(false);
            final AlertDialog dialog = alert.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            speech(activity, binding);
            binding.addCaption.setText(edit_caption);
            binding.photoCredits.setText(edit_credit);
            binding.ok.setOnClickListener(view -> {
                addCaption.setText(binding.addCaption.getText().toString().trim());
                photoCredits.setText(binding.photoCredits.getText().toString().trim());
                dialog.dismiss();

            });
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private static void speech(Activity activity, AlertAddCaptionBinding binding) {
        binding.addCaptionButton.speak.setOnClickListener(v -> {
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
                        binding.addCaptionButton.speak.setVisibility(View.GONE);
                        binding.addCaptionButton.speakNow.setVisibility(View.VISIBLE);
                        binding.addCaption.setHint("Speak Now...");
                    }

                    @Override
                    public void onBeginningOfSpeech() {
                        binding.addCaptionButton.speak.setVisibility(View.GONE);
                        binding.addCaptionButton.speakNow.setVisibility(View.VISIBLE);
                        binding.addCaption.setHint("Speaking...");
                    }

                    @Override
                    public void onRmsChanged(float rmsdB) {

                    }

                    @Override
                    public void onBufferReceived(byte[] buffer) {

                    }

                    @Override
                    public void onEndOfSpeech() {
                        binding.addCaptionButton.speak.setVisibility(View.GONE);
                        binding.addCaptionButton.speakNow.setVisibility(View.VISIBLE);
                        binding.addCaption.setHint("Wait...");
                    }

                    @Override
                    public void onError(int error) {
                        binding.addCaptionButton.speak.setVisibility(View.VISIBLE);
                        binding.addCaptionButton.speakNow.setVisibility(View.GONE);
                        binding.addCaption.setHint(R.string.type);
                    }

                    @Override
                    public void onResults(Bundle results) {
                        if (!TEST_ERROR) {
                            binding.addCaptionButton.speak.setVisibility(View.VISIBLE);
                            binding.addCaptionButton.speakNow.setVisibility(View.GONE);
                            Log.i("SPEECH_TEST", "onResults: destroying recognizer");

                            if (recognizer != null) {
                                recognizer.cancel();
                                recognizer.destroy();
                                recognizer = null;

                                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                                //displaying the first match
                                if (matches != null && !matches.isEmpty())
                                    binding.addCaption.setText(matches.get(0));

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
