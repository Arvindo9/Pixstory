package com.app.pixstory.ui.dashboard.story_detail.adapter;

import android.view.View;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseAdapter;
import com.app.pixstory.data.model.add_support.SupportData;
import com.app.pixstory.databinding.AdapterAddSupportBinding;

import java.util.ArrayList;

public class AddSupportAdapter extends BaseAdapter<AdapterAddSupportBinding, SupportData> {

    String actionFrom = "";
    /**
     * @param adapterList list args require to bind adapter up to the size of array
     */
    public AddSupportAdapter(ArrayList<SupportData> adapterList, String actionFrom) {
        super(adapterList);
        this.actionFrom = actionFrom;
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
        return R.layout.adapter_add_support;
    }

    /**
     * Initialised View Holder
     *
     * @param binding DataBinding
     * @return new ViewHolder<B, D>(binding);
     */
    @Override
    public ViewHolder getViewHolder(AdapterAddSupportBinding binding) {
        return new ViewHolder<AdapterAddSupportBinding, SupportData>(binding) {
            /**
             * If there is anything to do then do here otherwise leave it blank
             *
             * @param binding  layout reference for single view
             * @param data     for single view
             * @param position position of ArrayList
             */
            @Override
            protected void doSomeWorkHere(AdapterAddSupportBinding binding, SupportData data, int position) {
                binding.username.setText(data.getUserFirstName() + " " + data.getUserLastName());
                if (actionFrom.equals("support") || actionFrom.equals("challenge")) {
                    binding.title.setText(data.getStoryTitle());
                }else {
                    binding.title.setText(data.getMsg());
                }
            }

            /**
             * @param data binding.setCalendarData(new AdapterViewModel(CalendarData));
             */
            @Override
            protected void bindData(SupportData data) {

            }

            /**
             * Method to set click listeners on view holder or groups
             *
             * @param thisContext set it on method : binding.layout.setOnClickListener(thisContext);
             * @param binding     DataBinding
             * @param data        CalendarData
             */
            @Override
            public void setClickListeners(ViewHolderClickListener thisContext, AdapterAddSupportBinding binding, SupportData data) {
                binding.addSupport.setOnClickListener(thisContext);
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
            protected ViewHolderClickListener viewHolderReference(AdapterAddSupportBinding binding, SupportData data, int position) {
                return new ViewHolderClickListener<AdapterAddSupportBinding, SupportData>(binding, data, position) {
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
                        if (view.getId() == R.id.add_support){
                            getListener().onAdapterItem(data, actionFrom, position);
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
}
