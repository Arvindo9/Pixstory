package com.app.pixstory.ui.messages.message.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseAdapter;
import com.app.pixstory.data.model.db.messages.Messages;
import com.app.pixstory.databinding.AdapterMessageBinding;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Author       : Arvindo Mondal
 * Created on   : 05-09-2019
 * Designation  : Programmer
 * Strength     : Never give up
 * Motto        : To be known as great Mathematician
 * Skills       : Algorithms and logic
 */
public class MessageAdapter extends BaseAdapter<AdapterMessageBinding, Messages> {

    private MessageAdapterListener listener;
    private String name;
    private String outputFile;
    private MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private Runnable runnable;
    private boolean isAudioRecordPlay = false;
    private String type = Constants.MESSAGE_TYPE_INBOX;

    public void setup(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public MessageAdapter(ArrayList<Messages> list) {
        super(list);
    }

    public void addItems(List<Messages> model) {
        list.addAll(model);
        listener.onAdapterDataUpdate(list.size() - 1);
        notifyDataSetChanged();
    }

    public void clearItems() {
        list.clear();
        mainList.clear();
        notifyDataSetChanged();
    }

    public void setListener(MessageAdapterListener listener) {
        this.listener = listener;
    }

    /**
     * @param position current index of ArrayList
     * @return return 0 if single layout xml else override this method for multiple xml or elements
     */
    @Override
    protected int getItemViewTypeAdapter(int position) {
        return 0;
    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    protected int getLayout() {
        return R.layout.adapter_message;
    }

    @Override
    public ViewHolder getViewHolder(AdapterMessageBinding binding) {
        return new ViewHolder<AdapterMessageBinding, Messages>(binding) {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            protected void doSomeWorkHere(AdapterMessageBinding binding, Messages data, int position) {
                if(data.getMessageType() == Constants.MESSAGE_TYPE_CHAT_AUDIO){
                    binding.textLayoutLeft.setVisibility(View.GONE);
                    binding.textLayoutRight.setVisibility(View.GONE);
                    binding.layoutPlay.setVisibility(View.VISIBLE);
                    if(data.getMessageSide()) {
                        binding.nameAudio.setText(name);
                    }
                }

                if(data.getMessageSide()){
                    binding.textLayoutRight.setVisibility(View.GONE);
                }
                else {
                    binding.textLayoutLeft.setVisibility(View.GONE);
                }

                if(!data.getMessageSide() && data.getHasRequest() == 1){
                    binding.textLayoutRight.setVisibility(View.GONE);
                    binding.textLayoutLeft.setVisibility(View.GONE);
                    binding.layoutFriendRequest.setVisibility(View.VISIBLE);

                    if (data.getRequestTypeId() == 0) {
                        binding.layoutFollow.setVisibility(View.GONE);
                    }
                    else if (data.getRequestTypeId() == 2) {
                        binding.follow.setText(R.string.approve);
                    }
                    if(data.getRequestTitle()==null || data.getRequestTitle().isEmpty() ||
                        data.getRequestTitle().equalsIgnoreCase("none")){
                        binding.followBody.setVisibility(View.GONE);
                    }
                }
                else if(data.getMessageSide() && data.getHasRequest() == 1){
                    binding.textLayoutRight.setVisibility(View.GONE);
                    binding.textLayoutLeft.setVisibility(View.GONE);
                    binding.layoutFriendRequest.setVisibility(View.VISIBLE);

                    if (data.getRequestTypeId() == 0) {
                        binding.layoutFollow.setVisibility(View.GONE);
                    }
                    else if (data.getRequestTypeId() == 2) {
                        binding.follow.setText(R.string.approve);
                    }
                    if(data.getRequestTitle()==null || data.getRequestTitle().isEmpty() ||
                            data.getRequestTitle().equalsIgnoreCase("none")){
                        binding.followBody.setVisibility(View.GONE);
                    }
                }

                binding.playProgress.setOnTouchListener((view, motionEvent) -> true);
/*
                binding.play.setOnClickListener(v ->{
                    binding.playProgress.setOnTouchListener((view, motionEvent) -> true);
                    previewAudioClick(binding, itemView.getContext(), data);
                });
                */
            }

            @Override
            protected void bindData(Messages data) {
                binding.setData(new MessageAdapterViewModel(data, name));
            }

            /**
             * Method to set click listeners on view holder or groups
             *
             * @param thisContext set it on method : binding.layout.setOnClickListener(thisContext);
             * @param binding     DataBinding
             * @param data        data
             */
            @Override
            public void setClickListeners(ViewHolderClickListener thisContext, AdapterMessageBinding binding, Messages data) {
                binding.play.setOnClickListener(thisContext);
                binding.follow.setOnClickListener(thisContext);
            }

            /**
             * Initialised holder by new operator
             *
             * @param data     dataList
             * @param position of adapter
             * @return new ViewHolderClickListener<B, D>(binding, data, position)
             */
            @Override
            protected ViewHolderClickListener viewHolderReference(AdapterMessageBinding binding1, Messages data, int position) {
                return new ViewHolderClickListener<AdapterMessageBinding, Messages>(binding1, data, position){
                    /**
                     * Called when a view has been clicked.
                     *
                     * @param view The view that was clicked.
                     *             switch (view.getId()){
                     *             case R.id.id:
                     *             // itemView.getContext().startActivity();
                     *             break;
                     *             }
                     */
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()){
                            case R.id.play:
                                binding.play.setImageResource(R.drawable.ic_pause_big);
                                if(isAudioRecordPlay){
                                    mMediaPlayer.stop();
                                    mMediaPlayer.release();
                                    mMediaPlayer = null;
                                    binding1.play.setImageResource(R.drawable.ic_play_big);
                                    mHandler.removeCallbacks(runnable);
                                    isAudioRecordPlay =false;
                                    return;
                                }

                                isAudioRecordPlay = true;
                                previewAudioClick(binding1, itemView.getContext(), data);
                                break;

                            case R.id.follow:
                                listener.onFollowClick(data);
                                break;

                        }
                    }
                };
            }
        };
    }

    /**
     * @return new FilterClass();
     */
    @Override
    protected FilterClass initialisedFilterClass() {
        return null;
    }

    public interface MessageAdapterListener {
        void onRetryClick();

        void onAdapterDataUpdate(int lastDataPosition);

        void onFollowClick(Messages data);
    }

    private void previewAudioClick(AdapterMessageBinding binding1, Context context, Messages data) {
        mMediaPlayer = null;
        mMediaPlayer = new MediaPlayer();
        if(runnable!=null) {
            mHandler.removeCallbacks(runnable);
        }
        try {
            String url = Constants.BASE_PATH + data.getFileName();
            Logger.e("Audio file path adapter:" + url);
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepare();
            mMediaPlayer.start();

            binding1.playProgress.setMax(mMediaPlayer.getDuration()/100);
            runnable = new Runnable() {
                @Override
                public void run() {
                    if(mMediaPlayer != null){
                        int mCurrentPosition = mMediaPlayer.getCurrentPosition() / 100;
                        binding1.playProgress.setProgress(mCurrentPosition);
                    }
                    mHandler.postDelayed(this, 100);
                }
            };
            ((Activity)context).runOnUiThread(runnable);

            binding1.playProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                binding1.play.setImageResource(R.drawable.ic_play_big);
                mHandler.removeCallbacks(runnable);
                isAudioRecordPlay = false;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}