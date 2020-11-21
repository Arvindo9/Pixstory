package com.app.pixstory.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.pixstory.R;
import com.app.pixstory.databinding.ButtonAddCaptionBinding;

import java.util.ArrayList;
import java.util.Locale;

public class Speak {
    // Toggle to test errors
    private static boolean TEST_ERROR = false;
    private static SpeechRecognizer recognizer;
    private static Intent intent = null;
    // Reset each time startListening is called.
    private static long then;
    public static int REQUEST_MICROPHONE = 0;

    @SuppressLint("ClickableViewAccessibility")
    public static void narratePlease(Activity activity, ButtonAddCaptionBinding binding, AppCompatEditText addNarrative) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_MICROPHONE);
            } else {
                speech(activity, binding, addNarrative);
            }
        } else {
            speech(activity, binding, addNarrative);
        }

    }



    @SuppressLint("ClickableViewAccessibility")
    public static void speech(Activity activity, ButtonAddCaptionBinding binding, AppCompatEditText addNarrative) {

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
                    binding.speak.setVisibility(View.GONE);
                    binding.speakNow.setVisibility(View.VISIBLE);
                    addNarrative.setHint("Speak Now...");
                }

                @Override
                public void onBeginningOfSpeech() {
                    binding.speak.setVisibility(View.GONE);
                    binding.speakNow.setVisibility(View.VISIBLE);
                    addNarrative.setHint("Speaking...");
                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {
                    binding.speak.setVisibility(View.GONE);
                    binding.speakNow.setVisibility(View.VISIBLE);
                    addNarrative.setHint("Wait...");
                }

                @Override
                public void onError(int error) {
                    binding.speak.setVisibility(View.VISIBLE);
                    binding.speakNow.setVisibility(View.GONE);
                    addNarrative.setHint(R.string.type);
                }

                @Override
                public void onResults(Bundle results) {
                    if (!TEST_ERROR) {
                        binding.speak.setVisibility(View.VISIBLE);
                        binding.speakNow.setVisibility(View.GONE);
                        Log.i("SPEECH_TEST", "onResults: destroying recognizer");

                        if (recognizer != null) {
                            recognizer.cancel();
                            recognizer.destroy();
                            recognizer = null;

                            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                            //displaying the first match
                            if (matches != null && !matches.isEmpty())
                                addNarrative.setText(matches.get(0));
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

    }
}
