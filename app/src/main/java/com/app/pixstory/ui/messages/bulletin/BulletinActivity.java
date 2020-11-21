package com.app.pixstory.ui.messages.bulletin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.core.dialog.universal_search.UniversalSearch;
import com.app.pixstory.data.model.api.CountryResponse;
import com.app.pixstory.data.model.db.messages.MessageUsers;
import com.app.pixstory.databinding.ActivityMessagesBulletinBinding;
import com.app.pixstory.ui.interests.Interests;
import com.app.pixstory.ui.messages.MessagesNavigation;
import com.app.pixstory.ui.messages.MessagesViewModel;
import com.app.pixstory.ui.messages.messageUser.MessageUserFragment;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.util.Bundles;
import com.app.pixstory.utils.util.Logger;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.app.pixstory.utils.Constants.REQUEST_MICROPHONE;

/**
 * Author : Arvindo Mondal
 * Created on 21-02-2020
 */
public class BulletinActivity extends BaseActivity<ActivityMessagesBulletinBinding, MessagesViewModel>
        implements MessagesNavigation {

    private BottomSheetBehavior createMessageBehaviour;
    private ActivityMessagesBulletinBinding binding;
    private MessagesViewModel viewModel;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private boolean isAudioRecordPlay = false;
    private Handler mHandler = new Handler();
    private Runnable runnable;
    private MediaPlayer mMediaPlayer;
    private File audioFile;
    private List<String> countryList = new ArrayList<>();
    private List<CountryResponse.Data> data = new ArrayList<>();
    private String countryId = "";
    private MessageUserFragment fragment;

    @Override
    public int getLayout() {
        return R.layout.activity_messages_bulletin;
    }

    @Override
    protected void getBinding(ActivityMessagesBulletinBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(MessagesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected Class<MessagesViewModel> setViewModel() {
        return MessagesViewModel.class;
    }

    @Override
    protected void init() {
        viewModel.setNavigator(this);
        setUpType();
        setupCreateMessage();
        setup();
        setupTabs();
    }

    private void setUpType() {
        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if(navHost != null) {
            NavController navController = navHost.getNavController();

            NavInflater navInflater = navController.getNavInflater();
            NavGraph graph = navInflater.inflate(R.navigation.nav_message_inbox_graph);

            graph.setStartDestination(R.id.messageUserFragment);
            navController.setGraph(graph, Bundles.getInstance().setMessageUser(Constants.MESSAGE_TYPE_BULLETIN));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setup() {
        toolbar(binding.layoutToolbar.toolbar, binding.layoutToolbar.toolbarTitle, R.string.bulletin_board);
        binding.layoutToolbar.search.setOnClickListener(v -> UniversalSearch.universalSearch(this));
        binding.createMessage.location.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                updateLocation();
            }
        });

        binding.createMessage.send.setOnClickListener(v -> sendMessage());

        binding.createMessage.closeCreateMessage.setOnClickListener(v -> {
            createMessageBehaviour.setHideable(true);
            createMessageBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            clear();
        });

        binding.messageCreateLayout.createMessage.setOnClickListener(v ->
                createMessageBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED));
    }

    @Override
    public void onFragmentAttached(Fragment fragment) {
        if(fragment instanceof MessageUserFragment){
            this.fragment = (MessageUserFragment) fragment;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupCreateMessage1() {
        binding.createMessage.addInterest.setOnClickListener(v -> addInterestClick());
        binding.createMessage.closeCreateMessage.setOnClickListener(v -> {
            createMessageBehaviour.setHideable(true);
            createMessageBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });
        binding.createMessage.cancelPlay.setOnClickListener(v ->{
                    binding.createMessage.message.setVisibility(View.VISIBLE);
                    binding.createMessage.layoutPlay.setVisibility(View.GONE);
                });

        binding.createMessage.play.setOnClickListener(v ->{
            previewAudioClick();
            binding.createMessage.play.setImageResource(R.drawable.ic_pause_black_24dp);
        });
        binding.createMessage.sendAudioMessage.setOnLongClickListener(v -> {
            if(isStoragePermissionGranted()) {
                playAudioClick();
                isAudioRecordPlay = true;
            }
            return true;
        });
        binding.createMessage.sendAudioMessage.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // stop your timer.
                if(isAudioRecordPlay) {
                    isAudioRecordPlay = false;
                    try {
                        stopAudioClick();
                    }catch (IllegalStateException ignore){};
                    binding.createMessage.message.setVisibility(View.GONE);
                    binding.createMessage.layoutPlay.setVisibility(View.VISIBLE);
                }
            }
            return false;
        });

        binding.createMessage.playProgress.setOnTouchListener((view, motionEvent) -> true);

        binding.messageCreateLayout.createMessage.setOnClickListener(v ->
                createMessageBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED));
        createMessageBehaviour = BottomSheetBehavior.from(binding.createMessage.layout);
        // set callback for changes
        createMessageBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    createMessageBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.blur.setVisibility(View.VISIBLE);
                binding.blur.setAlpha(slideOffset);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupCreateMessage() {
        viewModel.getCountryList();
        binding.createMessage.country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryId = String.valueOf(data.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.createMessage.addInterest.setOnClickListener(v -> addInterestClick());
        binding.createMessage.closeCreateMessage.setOnClickListener(v -> {
            createMessageBehaviour.setHideable(true);
            createMessageBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            clear();
        });
        binding.createMessage.cancelPlay.setOnClickListener(v ->{
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mHandler.removeCallbacks(runnable);

            binding.createMessage.message.setVisibility(View.VISIBLE);
            binding.createMessage.layoutPlay.setVisibility(View.GONE);
        });

        binding.createMessage.play.setOnClickListener(v ->{
            binding.createMessage.play.setImageResource(R.drawable.ic_pause_big);
            if(isAudioRecordPlay){
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
                binding.createMessage.play.setImageResource(R.drawable.ic_play_big);
                mHandler.removeCallbacks(runnable);
                isAudioRecordPlay =false;
                return;
            }

            isAudioRecordPlay = true;
            previewAudioClick();
        });
        binding.createMessage.sendAudioMessage.setOnLongClickListener(v -> {
            if(isStoragePermissionGranted()) {
                if(isMicPermissionGranted()) {
                    playAudioClick();
                    isAudioRecordPlay = true;
                }
            }
            return true;
        });

        binding.createMessage.sendAudioMessage.setOnClickListener(v -> showToast(R.string.message_mic_click));
        binding.createMessage.sendAudioMessage.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // stop your timer.
                if(isAudioRecordPlay) {
                    isAudioRecordPlay = false;
                    try {
                        stopAudioClick();
                    }catch (IllegalStateException ignore){};
                    binding.createMessage.message.setVisibility(View.GONE);
                    binding.createMessage.layoutPlay.setVisibility(View.VISIBLE);
                }
            }
            return false;
        });

        binding.createMessage.playProgress.setOnTouchListener((view, motionEvent) -> true);

        binding.messageCreateLayout.createMessage.setOnClickListener(v ->
                createMessageBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED));
        createMessageBehaviour = BottomSheetBehavior.from(binding.createMessage.layout);
        // set callback for changes
        createMessageBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    createMessageBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.blur.setVisibility(View.VISIBLE);
                binding.blur.setAlpha(slideOffset);
            }
        });
    }

    void clear(){
        binding.createMessage.message.setText("");
        binding.createMessage.subjectLine.setText("");
        binding.createMessage.message.setVisibility(View.VISIBLE);
        binding.createMessage.layoutPlay.setVisibility(View.GONE);
        audioFile = null;
    }

    private void updateLocation() {

    }

    private void addInterestClick() {
        startActivity(Interests.class);
    }

    private void setupTabs() {
        // Adding the tabs using addTab() method.
        binding.tabLayout.tabLayout.addTab(binding.tabLayout.tabLayout.newTab().setText(getResources().getString(R.string.interests)));
        binding.tabLayout.tabLayout.addTab(binding.tabLayout.tabLayout.newTab().setText(getResources().getString(R.string.country)));
        binding.tabLayout.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    fragment.onBulletinType(Constants.MESSAGE_TYPE_BULLETIN_INTEREST);
                }
                else {
                    fragment.onBulletinType(Constants.MESSAGE_TYPE_BULLETIN_INTEREST);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void sendMessage() {
        //-----------------------interest id------------------------
        int[] interest_id = new int[0];
        if (Interests.interestsId != null) {
            interest_id = new int[Interests.interestsId.size()];
            int j = 0;
            for (int key : Interests.interestsId.keySet()) {
                interest_id[j++] = key;
            }
        }
        if(interest_id.length<1){
            showToast(R.string.please_select_interest);
            return;
        }

        if(binding.createMessage.location.isChecked()){
            if(countryId.isEmpty()){
                showToast(R.string.select_country);
                return;
            }
        }

        if(binding.createMessage.message.getVisibility() == View.VISIBLE) {
            String message = Objects.requireNonNull(binding.createMessage.message.getText()).toString();
            if(Objects.requireNonNull(binding.createMessage.subjectLine.getText()).toString().isEmpty()){
                showToast(R.string.write_subject);
            }
            else if (!message.isEmpty()) {
                viewModel.sendNewMessageBullet(binding.createMessage.subjectLine.getText().toString(),
                        message, countryId, interest_id);
//                createMessageBehaviour.setHideable(true);
//                createMessageBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                clear();
            }
            else{
                showToast(R.string.write_your_message);
            }
        }
        else{
            if(Objects.requireNonNull(binding.createMessage.subjectLine.getText()).toString().isEmpty()){
                showToast(R.string.write_subject);
            }
            else if(audioFile != null && audioFile.exists()) {
                Logger.e("Audio audioFile exist success:" + outputFile);
                viewModel.sendNewMessageFileBullet(binding.createMessage.subjectLine.getText().toString(),
                        audioFile, outputFile, countryId, interest_id);
//                createMessageBehaviour.setHideable(true);
//                createMessageBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                clear();
            }
        }
//        binding.createMessage.message.setText("");
//        binding.createMessage.subjectLine.setText("");
    }

    //Additional-----------------

    @Override
    public void onNewMessageSend(List<MessageUsers> messageUsers) {
        //Ignore
    }

    @Override
    public void showProgress(boolean status, int type) {
        if(status) {
            binding.createMessage.progressBar.progressBar.setVisibility(View.VISIBLE);
        }
        else{
            binding.createMessage.progressBar.progressBar.setVisibility(View.GONE);
            createMessageBehaviour.setHideable(true);
            createMessageBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            clear();
        }
    }

    @Override
    public void setCountryList(@NotNull List<CountryResponse.Data> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            countryList.add(data.get(i).getCountryName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.createMessage.country.setAdapter(adapter);
    }

    @Override
    public void onNewMessageBulletinSend(List<MessageUsers> bulletinUsers) {
        if(fragment != null){
            fragment.onNewMessageSend(bulletinUsers);
        }
    }

    //audio-----------------------

    private void setupRecorder(){
//        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Constants.APP_DIRECTORY
//                + "/" + System.currentTimeMillis()+ ".3gp";

        File directory = new File(Environment.getExternalStorageDirectory(), Constants.APP_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File subDirectory = new File(Environment.getExternalStorageDirectory(), Constants.APP_DIRECTORY + "/" +
                Constants.APP_FOLDER_AUDIO);
        if (!subDirectory.exists()) {
            subDirectory.mkdirs();
        }

        audioFile = new File(Environment.getExternalStorageDirectory(), Constants.APP_DIRECTORY + "/" +
                Constants.APP_FOLDER_AUDIO + "/" + System.currentTimeMillis() + Constants.APP_AUDIO_FILE_FORMAT);
        outputFile = audioFile.getAbsolutePath();

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);
        Logger.e("Audio audioFile path activity:" + outputFile);
        Logger.e("Audio audioFile path activity:" + audioFile.getAbsolutePath());
    }

    private void stopAudioClick() throws IllegalStateException{
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;
//        record.setEnabled(true);
//        stop.setEnabled(false);
//        play.setEnabled(true);
        showToast("Audio Recorder stopped");
    }

    private void previewAudioClick() {
        mMediaPlayer = null;
        mMediaPlayer = new MediaPlayer();
        if(runnable!=null) {
            mHandler.removeCallbacks(runnable);
        }
        try {
            Logger.e("Audio file path adapter:" + outputFile);
            mMediaPlayer.setDataSource(outputFile);
            mMediaPlayer.prepare();
            mMediaPlayer.start();

            binding.createMessage.playProgress.setMax(mMediaPlayer.getDuration()/100);
            runnable = new Runnable() {
                @Override
                public void run() {
                    if(mMediaPlayer != null){
                        int mCurrentPosition = mMediaPlayer.getCurrentPosition() / 100;
                        binding.createMessage.playProgress.setProgress(mCurrentPosition);
                    }
                    mHandler.postDelayed(this, 100);
                }
            };
            runOnUiThread(runnable);

            binding.createMessage.playProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(mMediaPlayer != null && fromUser){
                        mMediaPlayer.seekTo(progress * 100);
                    }
                }
            });

            mMediaPlayer.setOnCompletionListener(mp ->{
                binding.createMessage.play.setImageResource(R.drawable.ic_play_big);
                mHandler.removeCallbacks(runnable);
                isAudioRecordPlay = false;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playAudioClick() {
        if(myAudioRecorder == null){
//            if(isMicPermissionGranted()) {
            setupRecorder();
//            }
        }
        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IllegalStateException ise) {
            // make something ...
        } catch (IOException ioe) {
            // make something
        }
        showToast(R.string.start_recording);
    }
    //permission------------------

    private boolean isMicPermissionGranted(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_MICROPHONE);
            return false;
        }
        return true;
    }

    @SuppressLint("LogNotTimber")
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.REQUEST_WRITE_FILE);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
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
}
