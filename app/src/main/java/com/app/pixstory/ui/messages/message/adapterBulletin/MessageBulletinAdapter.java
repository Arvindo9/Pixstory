package com.app.pixstory.ui.messages.message.adapterBulletin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseAdapter;
import com.app.pixstory.data.model.db.messages.MessageUsers;
import com.app.pixstory.data.model.db.messages.Messages;
import com.app.pixstory.databinding.AdapterMessageBinding;
import com.app.pixstory.databinding.AdapterMessageBulletinBinding;
import com.app.pixstory.utils.Constants;
import com.app.pixstory.utils.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : Arvindo Mondal
 * Created on 28-04-2020
 */
public class MessageBulletinAdapter extends BaseAdapter<AdapterMessageBulletinBinding, MessageUsers> {

    private MessageAdapterListener listener;
    private String outputFile;
    private MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private Runnable runnable;
    private boolean isAudioRecordPlay = false;
    private String type = Constants.MESSAGE_TYPE_INBOX;

    public void setup(String type) {
        this.type = type;
    }

    public MessageBulletinAdapter(ArrayList<MessageUsers> list) {
        super(list);
    }

    public void addItems(List<MessageUsers> model) {
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
        return R.layout.adapter_message_bulletin;
    }

    /**
     * Initialised View Holder
     *
     * @param binding DataBinding
     * @return new ViewHolder<B, D>(binding);
     */
    @Override
    public ViewHolder getViewHolder(AdapterMessageBulletinBinding binding) {
        return new ViewHolder<AdapterMessageBulletinBinding, MessageUsers>(binding){
            /**
             * If there is anything to do then do here otherwise leave it blank
             *
             * @param binding  layout reference for single view
             * @param data     for single view
             * @param position position of ArrayList
             */
            @SuppressLint("ClickableViewAccessibility")
            @Override
            protected void doSomeWorkHere(AdapterMessageBulletinBinding binding, MessageUsers data, int position) {
                if(data.getMessage().getMessageType() == Constants.MESSAGE_TYPE_CHAT_AUDIO){
                    binding.textLayoutLeft.setVisibility(View.GONE);
                    binding.textLayoutRight.setVisibility(View.GONE);
                    binding.layoutPlay.setVisibility(View.VISIBLE);
                    if(data.getMessage().getMessageSide()) {
                        binding.nameAudio.setText(data.getUserName());
                    }
                }

                binding.playProgress.setOnTouchListener((view, motionEvent) -> true);
            }

            /**
             * @param data binding.setCalendarData(new AdapterViewModel(CalendarData));
             */
            @Override
            protected void bindData(MessageUsers data) {
                binding.setData(new MessageBulletinAdapterViewModel(data));
            }

            /**
             * Method to set click listeners on view holder or groups
             *
             * @param thisContext set it on method : binding.layout.setOnClickListener(thisContext);
             * @param binding     DataBinding
             * @param data        CalendarData
             */
            @Override
            public void setClickListeners(ViewHolderClickListener thisContext, AdapterMessageBulletinBinding binding, MessageUsers data) {
                binding.play.setOnClickListener(thisContext);
            }

            /**
             * Initialised holder by new operator
             *
             * @param binding1  DataBinding
             * @param data     dataList
             * @param position of adapter
             * @return new ViewHolderClickListener<B, D>(binding, CalendarData, position)
             */
            @Override
            protected ViewHolderClickListener viewHolderReference(AdapterMessageBulletinBinding binding1, MessageUsers data, int position) {
                return new ViewHolderClickListener<AdapterMessageBulletinBinding, MessageUsers>(binding1, data, position){
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
    }

    private void previewAudioClick(AdapterMessageBulletinBinding binding1, Context context, MessageUsers data) {
        mMediaPlayer = null;
        mMediaPlayer = new MediaPlayer();
        if(runnable!=null) {
            mHandler.removeCallbacks(runnable);
        }
        try {
            String url = Constants.BASE_PATH + data.getMessage().getFileName();
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
