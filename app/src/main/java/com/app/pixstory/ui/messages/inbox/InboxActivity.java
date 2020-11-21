package com.app.pixstory.ui.messages.inbox;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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
import com.app.pixstory.databinding.ActivityMessagesBinding;
import com.app.pixstory.ui.messages.MessagesNavigation;
import com.app.pixstory.ui.messages.MessagesViewModel;
import com.app.pixstory.ui.messages.adapter.AutoSuggestAdapter;
import com.app.pixstory.ui.messages.bulletin.BulletinActivity;
import com.app.pixstory.ui.messages.messageUser.MessageUserFragment;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.util.Bundles;
import com.app.pixstory.utils.util.Logger;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.app.pixstory.utils.Constants.REQUEST_MICROPHONE;

/**
 * Author : Arvindo Mondal
 * Created on 20-02-2020
 */
public class InboxActivity extends BaseActivity<ActivityMessagesBinding, MessagesViewModel>
        implements MessagesNavigation {
    private ActivityMessagesBinding binding;
    private MessagesViewModel viewModel;
    private BottomSheetBehavior createMessageBehaviour;
    private AutoSuggestAdapter autoSuggestAdapter;
    private int userId = -111;
    private MessageUserFragment fragment;

    private int messageType = Constants.MESSAGE_TYPE_CHAT_TEXT;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private boolean isAudioRecordPlay = false;
    private Handler mHandler = new Handler();
    private Runnable runnable;
    private MediaPlayer mMediaPlayer;
    private File audioFile;

    @Override
    protected Class<MessagesViewModel> setViewModel() {
        return MessagesViewModel.class;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_messages;
    }

    @Override
    protected void getBinding(ActivityMessagesBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void getViewModel(MessagesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected void init() {
        viewModel.setNavigator(this);
        subscribeToLiveData();
        setUpType();
        setupCreateMessage();
        setup();
        setupTabs();
        setupAutoNewUsers();
        viewModel.tmp();
    }

    private void subscribeToLiveData() {
        viewModel.getNewUsersLiveData().observe(this, data -> {
            autoSuggestAdapter.addItems(data);
        });
    }

    private void setUpType() {
        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if(navHost != null) {
            NavController navController = navHost.getNavController();

            NavInflater navInflater = navController.getNavInflater();
            NavGraph graph = navInflater.inflate(R.navigation.nav_message_inbox_graph);

            graph.setStartDestination(R.id.messageUserFragment);
            navController.setGraph(graph, Bundles.getInstance().setMessageUser(Constants.MESSAGE_TYPE_INBOX));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setup() {
        toolbar(binding.toolbarLayout.toolbar, binding.toolbarLayout.toolbarTitle, R.string.messages);
        binding.toolbarLayout.search.setOnClickListener(v -> UniversalSearch.universalSearch(this));
        binding.createMessage.send.setOnClickListener(v -> sendMessage());

        binding.createMessage.closeCreateMessage.setOnClickListener(v -> {
            createMessageBehaviour.setHideable(true);
            createMessageBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            clear();
        });

        binding.messageCreateLayout.createMessage.setOnClickListener(v ->
                createMessageBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED));
    }

    void clear(){
        binding.createMessage.message.setText("");
        binding.createMessage.user.setText("");
        binding.createMessage.message.setVisibility(View.VISIBLE);
        binding.createMessage.layoutPlay.setVisibility(View.GONE);
        audioFile = null;
    }

    private void sendMessage() {
        if(userId < 1){
            return;
        }
        if(binding.createMessage.message.getVisibility() == View.VISIBLE) {
            String message = Objects.requireNonNull(binding.createMessage.message.getText()).toString();
            if (userId > 0 && !message.isEmpty()) {
                viewModel.sendNewMessage(userId, message);
//                createMessageBehaviour.setHideable(true);
//                createMessageBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                clear();
            }
        }
        else{
            if(userId > 0 && audioFile != null && audioFile.exists()) {
                Logger.e("Audio audioFile exist success:" + outputFile);
                viewModel.sendNewMessageFile(userId, audioFile, outputFile);
//                createMessageBehaviour.setHideable(true);
//                createMessageBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                clear();
            }
        }
//        binding.createMessage.message.setText("");
//        binding.createMessage.user.setText("");
    }

    private void setupTabs() {
        // Adding the tabs using addTab() method.
        binding.tabLayout.tabLayout.addTab(binding.tabLayout.tabLayout.newTab().setText(getResources().getString(R.string.inbox)));
        binding.tabLayout.tabLayout.addTab(binding.tabLayout.tabLayout.newTab().setText(getResources().getString(R.string.bulletin_board)));
        binding.tabLayout.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 1){
                    startActivity(BulletinActivity.class);
                    try {
                        binding.tabLayout.tabLayout.getTabAt(0).select();
                    }catch (Exception ignore){}
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

    @SuppressLint("ClickableViewAccessibility")
    private void setupCreateMessage() {
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
        binding.createMessage.sendAudioMessage.setOnClickListener(v -> showToast(R.string.message_mic_click));
        binding.createMessage.sendAudioMessage.setOnLongClickListener(v -> {
            if(isStoragePermissionGranted()) {
                if(isMicPermissionGranted()) {
                    playAudioClick();
                    isAudioRecordPlay = true;
                }
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

    private void setupAutoNewUsers() {
        //Setting up the adapter for AutoSuggest
        binding.createMessage.progressBar.progressBar.setVisibility(View.GONE);
        autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);
        binding.createMessage.user.setThreshold(1);
        binding.createMessage.user.setAdapter(autoSuggestAdapter);
        binding.createMessage.user.setOnItemClickListener(
                (parent, view, position, id) -> {
                    //TODO onItem select
                    userId = autoSuggestAdapter.getItem(position)!=null?autoSuggestAdapter.getItem(position).getUserId():-111;
                });
        binding.createMessage.user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Logger.e("onTextChanged:" + s + ", " + start + ", " + before + ", " + count);
                Log.e("onTextChanged:", s + ", " + start + ", " + before + ", " + count);
                userId = -111;
                if(before <= count) {
                    if (!TextUtils.isEmpty(binding.createMessage.user.getText())) {
//                if (binding.createMessage.user.getText().toString().length() == 2) {
                        viewModel.getNewUserList(binding.createMessage.user.getText().toString());
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onFragmentAttached(Fragment fragment) {
        if(fragment instanceof MessageUserFragment){
            this.fragment = (MessageUserFragment) fragment;
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
    public void onNewMessageSend(List<MessageUsers> messageUsers) {
        if(fragment != null){
            fragment.onNewMessageSend(messageUsers);
        }
    }

    @Override
    public void showProgress(boolean status, int type) {
        if(type == 1) {
            binding.createMessage.progressBar.progressBar.setVisibility(status ? View.VISIBLE : View.GONE);
            return;
        }

        if(status) {
            binding.createMessage.progressBarBig.progressBar.setVisibility(View.VISIBLE);
        }
        else{
            binding.createMessage.progressBarBig.progressBar.setVisibility(View.GONE);
            createMessageBehaviour.setHideable(true);
            createMessageBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            clear();
        }
    }

    @Override
    public void setCountryList(List<CountryResponse.Data> data) {

    }

    @Override
    public void onNewMessageBulletinSend(List<MessageUsers> bulletinUsers) {
        //Ignore
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
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.REQUEST_WRITE_FILE);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }
}