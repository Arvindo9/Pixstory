package com.app.pixstory.ui.dashboard.you.adapter;

import android.view.View;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseAdapter;
import com.app.pixstory.core.binding.BindingUtils;
import com.app.pixstory.data.model.upload.Data;
import com.app.pixstory.databinding.AdapterYouPhotosBinding;

import java.util.ArrayList;
import java.util.List;

public class YouPhotosAdapter extends BaseAdapter<AdapterYouPhotosBinding, Data> {
    public static final String TAG = YouPhotosAdapter.class.getSimpleName();

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    /**
     * @param adapterList list args require to bind adapter up to the size of array
     */
    public YouPhotosAdapter(ArrayList<Data> adapterList) {
        super(adapterList);
    }

    /**
     * @param position current index of ArrayList
     * @return return 0 if single layout xml else override this method for multiple xml or elements
     */
    @Override
    protected int getItemViewTypeAdapter(int position) {
        return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void add(Data r) {
        list.add(r);
        notifyItemInserted(list.size() - 1);
    }

    public void addAll(List<Data> moveResults) {
        for (Data result : moveResults) {
            //  add(result);
        }
    }

    public void remove(Data r) {
        int position = list.indexOf(r);
        if (position > -1) {
          //  list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
          //  remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
       //  add(new Data());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = list.size() - 1;
        Data result = getItem(position);

        if (result != null) {
          //  list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Data getItem(int position) {
        return list.get(position);
    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    protected int getLayout() {
        return R.layout.adapter_you_photos;
    }

    /**
     * Initialised View Holder
     *
     * @param binding DataBinding
     * @return new ViewHolder<B, D>(binding);
     */
    @Override
    public ViewHolder getViewHolder(AdapterYouPhotosBinding binding) {
        return new ViewHolder<AdapterYouPhotosBinding, Data>(binding) {
            /**
             * If there is anything to do then do here otherwise leave it blank
             *
             * @param binding  layout reference for single view
             * @param data     for single view
             * @param position position of ArrayList
             */
            @Override
            protected void doSomeWorkHere(AdapterYouPhotosBinding binding, Data data, int position) {
                binding.layoutYouPhotos.delete.setVisibility(View.GONE);
                BindingUtils.setStringPhotos(binding.layoutYouPhotos.banner, data.getThumbnail(), binding.layoutYouPhotos.progressBar);

            }

            /**
             * @param data binding.setCalendarData(new AdapterViewModel(CalendarData));
             */
            @Override
            protected void bindData(Data data) {

            }

            /**
             * Method to set click listeners on view holder or groups
             *
             * @param thisContext set it on method : binding.layout.setOnClickListener(thisContext);
             * @param binding     DataBinding
             * @param data        CalendarData
             */
            @Override
            public void setClickListeners(ViewHolderClickListener thisContext, AdapterYouPhotosBinding binding, Data data) {
                binding.layoutYouPhotos.banner.setOnClickListener(thisContext);
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
            protected ViewHolderClickListener viewHolderReference(AdapterYouPhotosBinding binding, Data data, int position) {
                return new ViewHolderClickListener<AdapterYouPhotosBinding, Data>(binding, data, position) {
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
                        if (view.getId() == R.id.banner) {
                            getListener().onAdapterItem(data, YouPhotosAdapter.TAG, position);
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
