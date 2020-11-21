package com.app.pixstory.ui.navigation_view.faqs;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.data.model.api.FAQResponse;
import com.app.pixstory.databinding.ActivityFaqsBinding;
import com.app.pixstory.databinding.FragmentFaqsBinding;
import com.app.pixstory.utils.Utils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.app.pixstory.utils.Constants.DEFAULT_LOADER;

public class FAQActivity extends BaseActivity<ActivityFaqsBinding, FAQsViewModel> implements FAQNavigator, FAQExpandableAdapter.AudioCallback, View.OnClickListener {

    private ActivityFaqsBinding binding;
    private FAQsViewModel viewModel;
    private HashMap<String, List<String>> expandableListDetail = new HashMap<>();
    private TextToSpeech textToSpeech;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    private FAQExpandableAdapter faqExpandableAdapter;

    @Override
    protected Class<FAQsViewModel> setViewModel() {
        return FAQsViewModel.class;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_faqs;
    }

    @Override
    protected void getBinding(ActivityFaqsBinding binding) {

        this.binding = binding;
    }

    @Override
    protected void getViewModel(FAQsViewModel viewModel) {

        this.viewModel = viewModel;
    }

    @Override
    protected void init() {
        toolbar(binding.toolbar.toolbar, binding.toolbar.text, R.string.faqs);
        viewModel.setNavigator(this);
        viewModel.getFAQList("");
        binding.audio.setOnClickListener(this);
        binding.submit.setOnClickListener(this);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        SpeechRecognitionListener listener = new SpeechRecognitionListener();
        mSpeechRecognizer.setRecognitionListener(listener);

        textToSpeech = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.US);
            }
        });

        binding.expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            parent.smoothScrollToPosition(groupPosition);
            ImageView imageView = v.findViewById(R.id.expandable_icon);
            if (parent.isGroupExpanded(groupPosition)) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_up_arrow));
            } else {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
            }
            return false;
        });

        binding.expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int g = 0; g < faqExpandableAdapter.getGroupCount(); g++) {
                    if (g != groupPosition) {
                        binding.expandableListView.collapseGroup(g);
                    }
                }
            }
        });

        binding.expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                return false;
            }
        });

        viewModel.getLoading().observe(this, aBoolean -> {
            if (aBoolean) {
                showSimmerLoader(DEFAULT_LOADER);
            } else {
                hideSimmerLoader();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void faqFetched(List<FAQResponse.Data> data) {

        if (data.size() > 0) {
            expandableListDetail = new HashMap<>();
            for (int i = 0; i < data.size(); i++) {
                List<String> listData = new ArrayList<>();
                listData.add(data.get(i).getAnswer());
                expandableListDetail.put(data.get(i).getQuestion(), listData);
            }
            List<String> expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
            faqExpandableAdapter = new FAQExpandableAdapter(this, expandableListTitle, expandableListDetail);
            binding.expandableListView.setAdapter(faqExpandableAdapter);
            binding.expandableListView.expandGroup(0);
        } else {
            showToast("No answer found for your question");
        }
    }

    @Override
    public void message(int message) {
        showToast(message);
    }

    @Override
    public void message(String message) {
        showToast(message);
    }

    @Override
    public void playAudio(String question) {
        String toSpeak = Objects.requireNonNull(expandableListDetail.get(question)).get(0);
        textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void stopAudio() {
        pauseSpeech();
    }

    public void onPause() {
        stopSpeech();
        super.onPause();
    }

    private void pauseSpeech() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    private void stopSpeech() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.audio:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    audioRecordPermission();
                } else {
                    recordAudio();
                }
                break;

            case R.id.submit:
                if (Objects.requireNonNull(binding.ques.getText()).toString().trim().isEmpty()) {
                    showToast("Enter question");
                    return;
                }
                viewModel.getFAQList(Objects.requireNonNull(binding.ques.getText()).toString().trim());
                binding.ques.setText("");
                break;
        }
    }

    private void recordAudio() {

        Utils.setLocalImage(this, binding.audio, R.drawable.ic_using_mic);
        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        binding.ques.setText("");
        binding.ques.setHint("Speak your question now");
        binding.ques.setCursorVisible(false);

    }

    private void audioRecordPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.RECORD_AUDIO)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        recordAudio();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        showSettingsDialog();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private class SpeechRecognitionListener implements RecognitionListener {

        @Override
        public void onBeginningOfSpeech() {
            binding.ques.setHint("Recognizing...");
            binding.ques.setCursorVisible(false);
        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {
            Utils.setLocalImage(getBaseContext(), binding.audio, R.drawable.ic_mic);
        }

        @Override
        public void onError(int error) {
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onReadyForSpeech(Bundle params) {
        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (matches != null && matches.size() > 0) {
                binding.ques.setText(matches.get(0));
                binding.ques.setCursorVisible(true);
                Utils.setFocus(binding.ques);
                binding.ques.setHint("");
            } else {
                showToast("Speech not recognized.Try again!!!");
            }
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }
    }

    @Override
    public void onDestroy() {
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.destroy();
        }
        super.onDestroy();
    }
}
