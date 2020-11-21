package com.app.pixstory.ui.dashboard.home.adapter;

import android.graphics.Color;
import android.view.View;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseAdapter;
import com.app.pixstory.data.model.api.ListModel;
import com.app.pixstory.databinding.AdapterInterestHomeBinding;

import java.util.ArrayList;

public class HomeInterestPageAdapter extends BaseAdapter<AdapterInterestHomeBinding, ListModel> {
    public static final String TAG = HomeInterestPageAdapter.class.getSimpleName();
    int row_index = -1;

    /**
     * @param position current index of ArrayList
     * @return return 0 if single layout xml else override this method for multiple xml or elements
     */
    @Override
    protected int getItemViewTypeAdapter(int position) {
        return 0;
    }


    public HomeInterestPageAdapter(ArrayList<ListModel> data) {
        super(data);
    }

    @Override
    protected int getLayout() {
        return R.layout.adapter_interest_home;
    }

    /**
     * Initialised View Holder
     *
     * @param binding DataBinding
     * @return new ViewHolder<B, D>(binding);
     */
    @Override
    public ViewHolder getViewHolder(AdapterInterestHomeBinding binding) {
        return new ViewHolder<AdapterInterestHomeBinding, ListModel>(binding) {
            /**
             * If there is anything to do then do here otherwise leave it blank
             *
             * @param binding  layout reference for single view
             * @param data     for single view
             * @param position position of ArrayList
             */
            @Override
            protected void doSomeWorkHere(AdapterInterestHomeBinding binding, ListModel data, int position) {
                binding.interest.setText(data.getName());
                if (list.get(position).getChecked()) {
                    binding.interest.setBackgroundResource(R.drawable.shape_white_black_25);
                } else {
                    binding.interest.setBackgroundResource(R.drawable.shape_neon_black_25);
                }
            }

            /**
             * @param data binding.setCalendarData(new AdapterViewModel(CalendarData));
             */
            @Override
            protected void bindData(ListModel data) {
                //  binding.setData(new HomeInterestViewModel(data));
            }

            /**
             * Method to set click listeners on view holder or groups
             *
             * @param thisContext set it on method : binding.layout.setOnClickListener(thisContext);
             * @param binding     DataBinding
             * @param data        CalendarData
             */
            @Override
            public void setClickListeners(ViewHolderClickListener thisContext, AdapterInterestHomeBinding binding, ListModel data) {
                binding.interest.setOnClickListener(thisContext);
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
            protected ViewHolderClickListener viewHolderReference(AdapterInterestHomeBinding binding, ListModel data, int position) {
                return new ViewHolderClickListener<AdapterInterestHomeBinding, ListModel>(binding, data, position) {
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
                        if (view.getId() == R.id.interest) {
                            for (int i = 0; i < list.size(); i++){
                                if (i == position){
                                    list.get(i).setChecked(true);
                                   notifyDataSetChanged();
                                } else {
                                    list.get(i).setChecked(false);
                                }
                            }

                            getListener().onAdapterItem(data, HomeInterestPageAdapter.TAG, position);

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
