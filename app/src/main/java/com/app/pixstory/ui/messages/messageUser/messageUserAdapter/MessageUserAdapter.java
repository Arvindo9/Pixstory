package com.app.pixstory.ui.messages.messageUser.messageUserAdapter;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseAdapter;
import com.app.pixstory.data.model.db.messages.MessageUsers;
import com.app.pixstory.databinding.AdapterMessageUserBinding;
import com.app.pixstory.utils.Constants;

import java.util.ArrayList;

/**
 * Author : Arvindo Mondal
 * Created on 23-02-2020
 */
public class MessageUserAdapter extends BaseAdapter<AdapterMessageUserBinding, MessageUsers> {
    private ViewHolder viewHolder;
    private AdapterMessageUserBinding bindingAdapter;
    private String type = Constants.MESSAGE_TYPE_INBOX;

    public void setup(String type) {
        this.type = type;
    }

    /**
     * @param adapterList list args require to bind adapter up to the size of array
     */
    public MessageUserAdapter(ArrayList<MessageUsers> adapterList) {
        super(adapterList);
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
        return R.layout.adapter_message_user;
    }

    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    public AdapterMessageUserBinding getBinding(){
        return bindingAdapter;
    }

    /**
     * Initialised View Holder
     *
     * @param binding DataBinding
     * @return new ViewHolder<B, D>(binding);
     */
    @Override
    public ViewHolder getViewHolder(AdapterMessageUserBinding binding) {
        viewHolder = new ViewHolder<AdapterMessageUserBinding, MessageUsers>(binding) {
            private RelativeLayout layoutBackground;
            private LinearLayout layoutForeground;

            public RelativeLayout getLayoutBackground() {
                return layoutBackground;
            }

            public LinearLayout getLayoutForeground() {
                return layoutForeground;
            }

            @SuppressLint("ClickableViewAccessibility")
            @Override
            protected void doSomeWorkHere(AdapterMessageUserBinding binding, MessageUsers data, int position) {
                bindingAdapter = binding;
                if(type.equals(Constants.MESSAGE_TYPE_INBOX)) {
                    if ((data.getMessage().getAttachment() != null) ||
                            (data.getMessage().getFileName() != null &&
                                    !data.getMessage().getFileName().isEmpty())) {
                        binding.messageUserCard.messageUserCardData.layout.setVisibility(View.GONE);
                        binding.messageUserCard.messageUserCardDataAudio.layout.setVisibility(View.VISIBLE);
                        binding.messageUserCard.messageUserCardDataAudio.layoutFollow.setVisibility(View.GONE);
                    }else {
                        binding.messageUserCard.messageUserCardData.layout.setVisibility(View.VISIBLE);
                        binding.messageUserCard.messageUserCardData.follow.setVisibility(View.GONE);
                        binding.messageUserCard.messageUserCardDataAudio.layout.setVisibility(View.GONE);
                    }
                }
                else if(type.equals(Constants.MESSAGE_TYPE_BULLETIN)){
                    binding.messageUserCard.proTag.setVisibility(View.GONE);
                    binding.messageUserCard.messageUserCardData.layout.setVisibility(View.GONE);
                    binding.messageUserCard.messageUserCardDataAudio.layout.setVisibility(View.GONE);
//                    binding.messageUserCard.messageUserCardDataBulletin.layout.setVisibility(View.VISIBLE);

                    if (data.getMessage().getFileName() != null && !data.getMessage().getFileName().isEmpty()) {
                        binding.messageUserCard.messageUserCardDataBulletin.layout.setVisibility(View.GONE);
                        binding.messageUserCard.messageUserCardDataAudio.follow.setVisibility(View.GONE);
                        binding.messageUserCard.messageUserCardDataAudio.layout.setVisibility(View.VISIBLE);
                    } else {
                        binding.messageUserCard.messageUserCardDataBulletin.layout.setVisibility(View.VISIBLE);
                        binding.messageUserCard.messageUserCardData.follow.setVisibility(View.GONE);
                        binding.messageUserCard.messageUserCardDataAudio.layout.setVisibility(View.GONE);
                    }
                }

                if(data.getMessage()!=null && data.getMessage().getMessageSide() &&
                        data.getMessage().getHasRequest()!=null && data.getMessage().getMessageStatus()!=null){
                    if((data.getMessage().getHasRequest() == 0 && data.getMessage().getMessageStatus()==1) ||
                            (data.getMessage().getHasRequest() == 1 && data.getMessage().getMessageStatus()==1) ||
                            (data.getMessage().getHasRequest() == 2 && data.getMessage().getMessageStatus()==1)
                    ){
                        binding.messageUserCard.foregroundLayout.setBackgroundColor(
                                ContextCompat.getColor(itemView.getContext(), R.color.neon));
                    }
                    else{
                        binding.messageUserCard.foregroundLayout.setBackgroundColor(
                                ContextCompat.getColor(itemView.getContext(), R.color.white));
                    }
                }



                binding.messageUserCard.messageUserCardDataAudio.playProgress.setOnTouchListener((view, motionEvent) -> true);
            }

            /**
             * @param data binding.setCalendarData(new AdapterViewModel(CalendarData));
             */
            @Override
            protected void bindData(MessageUsers data) {
                binding.setData(new MessageUserAdapterViewModel(data));
            }

            /**
             * Method to set click listeners on view holder or groups
             *
             * @param thisContext set it on method : binding.layout.setOnClickListener(thisContext);
             * @param binding     DataBinding
             * @param data        CalendarData
             */
            @Override
            public void setClickListeners(ViewHolderClickListener thisContext, AdapterMessageUserBinding binding,
                                          MessageUsers data) {
                binding.messageUserCard.foregroundLayout.setOnClickListener(thisContext);
            }

            /**
             * Initialised holder by new operator
             *
             * @param binding  DataBinding
             * @param data     dataList
             * @param position of adapter
             * @return new ViewHolderClickListener<B, D>(binding, CalendarData, position)
             */
            @Override
            protected ViewHolderClickListener viewHolderReference(AdapterMessageUserBinding binding,
                                                                  MessageUsers data, int position) {
                return new ViewHolderClickListener<AdapterMessageUserBinding, MessageUsers>(binding, data, position){
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
                            case R.id.foregroundLayout:
                                getListener().onAdapterItem(data, "MessageUser", position);
                                break;
                        }
                    }
                };
            }
        };

        return viewHolder;
    }

    /**
     * @return new FilterClass();
     */
    @Override
    protected FilterClass initialisedFilterClass() {
        return null;
    }

    //Items deletes--------------------------

    public MessageUsers getData(int position){
        return list.get(position);
    }

    public void removeItem(int position){
        list.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(MessageUsers data, int position) {
        list.add(position, data);
        // notify item added by position
        notifyItemInserted(position);
    }
}