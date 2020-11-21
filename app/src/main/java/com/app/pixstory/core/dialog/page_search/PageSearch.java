package com.app.pixstory.core.dialog.page_search;

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
import android.widget.CompoundButton;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.databinding.AlertPageSearchBinding;
import com.app.pixstory.utils.Constants;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class PageSearch {

    private static SpeechRecognizer recognizer;
    private static Intent intent = null;
    public static int REQUEST_MICROPHONE = 0;
    private static boolean TEST_ERROR = false;
    private static long then;
    private static SearchCallback searchCallback;
    private static String filter,type,findInPage;

    public interface SearchCallback {
        void search(String searchText, String filter,String findInPage);
    }

    @SuppressLint("ClickableViewAccessibility")
    public static void searchPage(Activity activity,String ty,SearchCallback callback) {
        if(ty.equalsIgnoreCase(Constants.STORIES))
            filter = Constants.STORIES;
        else
            filter = Constants.MEMBERS;
        findInPage = "1";
        searchCallback = callback;
        type = ty;
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

    public static void searchItem(Context context) {
        AlertPageSearchBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.alert_page_search, null, false);
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(binding.getRoot());
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();

        if(type.equalsIgnoreCase(Constants.STORIES)) {
            binding.stories.setBackgroundColor(context.getResources().getColor(R.color.white));
            binding.members.setBackgroundColor(context.getResources().getColor(R.color.neon));
        }
        else {
            binding.stories.setBackgroundColor(context.getResources().getColor(R.color.neon));
            binding.members.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            speech(context, binding);

            binding.close.setOnClickListener(v -> dialog.dismiss());

            binding.ok.setOnClickListener(v -> {
                if (Objects.requireNonNull(binding.type.getText()).toString().trim().isEmpty()) {
                    ((BaseActivity) context).showToast("Enter search text");
                    return;
                }
                dialog.dismiss();
                searchCallback.search(binding.type.getText().toString().trim(), filter,findInPage);
            });

            binding.searchInPage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    findInPage = isChecked ? "1" : "0";
                }
            });

        }

        binding.members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter = Constants.MEMBERS;
                binding.members.setBackgroundColor(context.getResources().getColor(R.color.white));
                binding.stories.setBackgroundColor(context.getResources().getColor(R.color.neon));
            }
        });

        binding.stories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter = Constants.STORIES;
                binding.members.setBackgroundColor(context.getResources().getColor(R.color.neon));
                binding.stories.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        });


    }

    @SuppressLint("ClickableViewAccessibility")
    private static void speech(Context context, AlertPageSearchBinding binding) {
        binding.universalSearchButton.speak.setOnClickListener(v -> {
            if (TEST_ERROR || recognizer == null) {
                Log.i("SPEECH_TEST", "onClick: setting recognizer");

                intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "blar");
                intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toString());

                recognizer = SpeechRecognizer.createSpeechRecognizer(context);
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
