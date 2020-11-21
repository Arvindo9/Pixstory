package com.app.pixstory.ui.dashboard.you.adapter;

import android.view.View;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseAdapter;
import com.app.pixstory.ui.dashboard.you.model.YouFilterModel;
import com.app.pixstory.databinding.AdapterYouFilterBinding;
import com.app.pixstory.ui.dashboard.you.model.YouFilterViewModel;

import java.util.ArrayList;

public class YouFilterAdapter extends BaseAdapter<AdapterYouFilterBinding, YouFilterModel> {
    private static final String TAG = YouFilterAdapter.class.getSimpleName();

    /**
     * @param adapterList list args require to bind adapter up to the size of array
     */
    public YouFilterAdapter(ArrayList<YouFilterModel> adapterList) {
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
        return R.layout.adapter_you_filter;
    }

    /**
     * Initialised View Holder
     *
     * @param binding DataBinding
     * @return new ViewHolder<B, D>(binding);
     */
    @Override
    public ViewHolder getViewHolder(AdapterYouFilterBinding binding) {
        return new ViewHolder<AdapterYouFilterBinding, YouFilterModel>(binding) {
            /**
             * If there is anything to do then do here otherwise leave it blank
             *
             * @param binding  layout reference for single view
             * @param data     for single view
             * @param position position of ArrayList
             */
            @Override
            protected void doSomeWorkHere(AdapterYouFilterBinding binding, YouFilterModel data, int position) {
                binding.filter.setText(data.getFilterName());
                if (list.get(position).getChecked()) {
                    binding.filter.setBackgroundResource(R.drawable.shape_white_black_25);
                } else {
                    binding.filter.setBackgroundResource(R.drawable.shape_neon_black_25);
                }
            }

            /**
             * @param data binding.setCalendarData(new AdapterViewModel(CalendarData));
             */
            @Override
            protected void bindData(YouFilterModel data) {
                binding.setData(new YouFilterViewModel(data));
            }

            /**
             * Method to set click listeners on view holder or groups
             *
             * @param thisContext set it on method : binding.layout.setOnClickListener(thisContext);
             * @param binding     DataBinding
             * @param data        CalendarData
             */
            @Override
            public void setClickListeners(ViewHolderClickListener thisContext, AdapterYouFilterBinding binding, YouFilterModel data) {
                binding.filter.setOnClickListener(thisContext);
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
            protected ViewHolderClickListener viewHolderReference(AdapterYouFilterBinding binding, YouFilterModel data, int position) {
                return new ViewHolderClickListener<AdapterYouFilterBinding, YouFilterModel>(binding, data, position) {
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
                        if (view.getId() == R.id.filter){
                            for (int i = 0; i < list.size(); i++){
                                if (i == position){
                                    list.get(i).setChecked(true);
                                    notifyDataSetChanged();
                                } else {
                                    list.get(i).setChecked(false);
                                }
                            }
                            getListener().onAdapterItem(data, YouFilterAdapter.TAG, position);
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
