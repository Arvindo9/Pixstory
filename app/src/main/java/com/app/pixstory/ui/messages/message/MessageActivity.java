package com.app.pixstory.ui.messages.message;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.pixstory.BR;
import com.app.pixstory.R;
import com.app.pixstory.base.BaseActivity;
import com.app.pixstory.data.model.db.messages.Messages;
import com.app.pixstory.databinding.ActivityMessageBinding;
import com.app.pixstory.ui.messages.message.adapter.MessageAdapter;
import com.app.pixstory.ui.messages.message.adapterBulletin.MessageBulletinAdapter;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.util.Bundles;
import com.app.pixstory.utils.util.Logger;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.vanniktech.emoji.EmojiPopup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.app.pixstory.utils.Constants.REQUEST_MICROPHONE;

/**
 * Author       : Arvindo Mondal
 * Created on   : 05-09-2019
 */
public class MessageActivity extends BaseActivity<ActivityMessageBinding, MessageViewModel>
        implements MessageNavigator, MessageAdapter.MessageAdapterListener, MessageBulletinAdapter.MessageAdapterListener {
    public static final String TAG = MessageActivity.class.getSimpleName();

    private MessageAdapter adapter;
    private MessageBulletinAdapter adapterBulletin;
    private ActivityMessageBinding binding;
    private MessageViewModel viewModel;

    private int dataType;
    private int userId;
    private String roseType = "";
    private LinearLayoutManager layoutManager;
    private EmojiPopup emojiPopup;
    private String type;
    private String userName;


    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private boolean isAudioRecordPlay = false;
    private Handler mHandler = new Handler();
    private MediaPlayer mMediaPlayer;
    private File audioFile;


    /**
     * @return R.layout.layout_file
     */
    @Override
    public int getLayout() {
        return R.layout.activity_message;
    }

    @Override
    protected void getBinding(ActivityMessageBinding binding) {
        this.binding = binding;
    }

    /**
     * Override for set binding variable
     *
     * @return BR.data;
     */
    @Override
    public int getBindingVariable() {
        return BR.data;
    }

    @Override
    protected Class<MessageViewModel> setViewModel() {
        return MessageViewModel.class;
    }

    @Override
    protected void getViewModel(MessageViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Do anything on onCreate after binding
     * viewModel.setNavigator(this);
     */
    @Override
    protected void init() {
        viewModel.setNavigator(this);
        binding.setData(viewModel);

        setUp();
        setUpAdapter();
        subscribeToLiveData();

        setupAudioMessage();
        setupBroadcast();
//        viewModel.fcmToken();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setUp() {
        Bundle bundle = getIntent().getExtras();
        type = Bundles.getInstance().getMessageType(bundle);
        userId = Bundles.getInstance().getMessageUserId(bundle);
        userName = Bundles.getInstance().getMessageUserName(bundle);

        emojiPopup = EmojiPopup.Builder.fromRootView(binding.layout).build(binding.messageLayout.messages);

        toolbar(binding.toolbarLayout.toolbar, binding.toolbarLayout.toolbarTitle, userName);


        binding.messageLayout.send.setOnClickListener(v -> onSendMessageClick());
//        binding.messageLayout.sendAudioMessage.setOnClickListener(v -> {onAudioMessageClick();});
        binding.messageLayout.imogi.setOnClickListener(v -> onEmojiIconClick());
    }

    private void setUpAdapter() {
        if(type.equals(Constants.MESSAGE_TYPE_INBOX)) {
            binding.subject.setVisibility(View.GONE);
            binding.listView.setVisibility(View.VISIBLE);
            binding.listViewBulletin.setVisibility(View.GONE);
            adapter = new MessageAdapter(new ArrayList<>());
            adapter.setListener(this);
            adapter.setup(type, userName);
            layoutManager = new LinearLayoutManager(this);
            layoutManager.setStackFromEnd(true);
            binding.listView.setLayoutManager(layoutManager);
            binding.listView.setAdapter(adapter);
            binding.listView.hasFixedSize();
            viewModel.getMessagesApis(String.valueOf(userId));
        }
        else {
            binding.listView.setVisibility(View.GONE);
            binding.listViewBulletin.setVisibility(View.VISIBLE);
            adapterBulletin = new MessageBulletinAdapter(new ArrayList<>());
            adapterBulletin.setListener(this);
            adapterBulletin.setup(type);
            layoutManager = new LinearLayoutManager(this);
            layoutManager.setStackFromEnd(true);
            binding.listViewBulletin.setLayoutManager(layoutManager);
            binding.listViewBulletin.setAdapter(adapterBulletin);
            binding.listViewBulletin.hasFixedSize();
            viewModel.getMessagesBulletinApis(String.valueOf(userId));
        }
    }

    private void subscribeToLiveData() {
        viewModel.getLiveData().observe(this, data ->
                viewModel.addDataToList(data));
        viewModel.getBulletinLiveData().observe(this, data ->
                viewModel.addDataToListBulletin(data));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        }catch (Exception ignore){}
        super.onDestroy();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupAudioMessage() {
        binding.messageLayout.sendAudioMessage.setOnClickListener(v -> showToast(R.string.message_mic_click));
        binding.messageLayout.sendAudioMessage.setOnLongClickListener(v -> {
            if(isStoragePermissionGranted()) {
                if(isMicPermissionGranted()) {
                    playAudioClick();
                    isAudioRecordPlay = true;
                }
            }
            return true;
        });

        binding.messageLayout.sendAudioMessage.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // stop your timer.
                if(isAudioRecordPlay) {
                    isAudioRecordPlay = false;
                    try {
                        stopAudioClick();
                    }catch (IllegalStateException ignore){};

                    if(audioFile.exists()) {
                        Logger.e("Audio audioFile exist success:" + outputFile);
                        if(type.equals(Constants.MESSAGE_TYPE_INBOX)) {
                            viewModel.sendMessageAudio(userId, audioFile, outputFile);
                        }
                        else {
                            //TODO send bulletin text
                            viewModel.sendMessageFileBulletin(userId, audioFile, outputFile);
                        }
                    }
                }
            }
            return false;
        });
    }

    //Navigator--------------------------------

    @Override
    public void showProgress(boolean status, int type) {
        if(status) {
            binding.progressBar.progressBar.setVisibility(View.VISIBLE);
        }
        else{
            binding.progressBar.progressBar.setVisibility(View.GONE);
        }
    }

    public void onSendMessageClick() {
        String message = binding.messageLayout.messages.getText() != null ?
                binding.messageLayout.messages.getText().toString() : "";
        if (!message.isEmpty()) {
//            viewModel.sendMessage(new Messages(userId, message));
            if(type.equals(Constants.MESSAGE_TYPE_INBOX)) {
                viewModel.sendMessage(String.valueOf(userId), message);
            }
            else{
                //TODO send bulletin audio
                viewModel.sendMessageBulletin(String.valueOf(userId), message);
            }
            binding.messageLayout.messages.setText("");
            hideKeyboard();
        }
    }

    public void onEmojiIconClick() {
        emojiPopup = EmojiPopup.Builder.fromRootView(binding.layout).build(binding.messageLayout.messages);
        emojiPopup.toggle(); // Toggles visibility of the Popup.
        emojiPopup.dismiss(); // Dismisses the Popup.
        emojiPopup.isShowing(); // Returns true when Popup is showing.
    }

    @Override
    public void onRetryClick() {

    }

    @Override
    public void onAdapterDataUpdate(int lastDataPosition) {
        if (lastDataPosition > 0) {
            layoutManager.scrollToPositionWithOffset(lastDataPosition, 5);
        }
    }

    @Override
    public void onFollowClick(Messages data) {
        if(data.getRequestTypeId() == 1) {
            viewModel.onFollow(data.getUserId());
        }
        else if(data.getRequestTypeId() == 2){
            viewModel.onApprove(data.getUserId());
        }
    }

    //Messages event------------

    private void setupBroadcast() {
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver,
                new IntentFilter(Constants.INTENT_SERVER_RECEIVE_MESSAGES));
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra(Constants.KEY_FROM_SERVICE_MESSAGE);
            Messages model = intent.getParcelableExtra(Constants.KEY_FROM_SERVICE_MESSAGE_DATA);
            Logger.e("receiver " + "Got message: " + message);
            if (message != null && model!=null) {
                Logger.e("receiver " + "Got message: success" + message);
                if (message.equals(Constants.VALUE_FROM_SERVICE_MESSAGE)) {
//                viewModel.getMessageDataPush(roseType, modelInbox.getPrimaryKey());
                    viewModel.updateMessagesPush(model);
                }
            }
        }
    };

    @Override
    public void message(int message) {

    }

    @Override
    public void message(String message) {
        showToast(message);
    }

    //Recorder--------------------

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

    private void playAudioClick() {
        if(myAudioRecorder == null){
            setupRecorder();
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
/*
    @SuppressLint("LogNotTimber")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
            if(requestCode == Constants.REQUEST_WRITE_FILE) {
                setupRecorder();
            }
        }
    }
*/
}