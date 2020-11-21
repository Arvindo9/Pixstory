package com.app.pixstory.ui.dashboard.you.adapter;

import android.view.View;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseAdapter;
import com.app.pixstory.core.binding.BindingUtils;
import com.app.pixstory.data.model.page_list.PageListData;
import com.app.pixstory.data.model.upload.Data;
import com.app.pixstory.databinding.AdapterYouPagesBinding;

import java.util.ArrayList;
import java.util.List;

public class YouPagesAdapter extends BaseAdapter<AdapterYouPagesBinding, PageListData> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    /**
     * @param adapterList list args require to bind adapter up to the size of array
     */
    public YouPagesAdapter(ArrayList<PageListData> adapterList) {
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

    public void add(PageListData r) {
        list.add(r);
        notifyItemInserted(list.size() - 1);
    }

    public void addAll(List<PageListData> moveResults) {
        for (PageListData result : moveResults) {
            // add(result);
        }
    }

    public void remove(PageListData r) {
        int position = list.indexOf(r);
        if (position > -1) {
            //  list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            //   remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        //  add(new PageListData());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = list.size() - 1;
        PageListData result = getItem(position);

        if (result != null) {
            //   list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public PageListData getItem(int position) {
        return list.get(position);
    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    protected int getLayout() {
        return R.layout.adapter_you_pages;
    }

    /**
     * Initialised View Holder
     *
     * @param binding DataBinding
     * @return new ViewHolder<B, D>(binding);
     */
    @Override
    public ViewHolder getViewHolder(AdapterYouPagesBinding binding) {
        return new ViewHolder<AdapterYouPagesBinding, PageListData>(binding) {
            /**
             * If there is anything to do then do here otherwise leave it blank
             *
             * @param binding  layout reference for single view
             * @param data     for single view
             * @param position position of ArrayList
             */
            @Override
            protected void doSomeWorkHere(AdapterYouPagesBinding binding, PageListData data, int position) {
                BindingUtils.setStringPhotos(binding.layoutYouPages.banner, data.getCoverImgPath(), binding.layoutYouPages.progressBar);
                binding.layoutYouPages.title.setText(data.getTitle());
                binding.followersCount.setText(data.getFollowerCount() + " " + "Followers");
            }

            /**
             * @param data binding.setCalendarData(new AdapterViewModel(CalendarData));
             */
            @Override
            protected void bindData(PageListData data) {

            }

            /**
             * Method to set click listeners on view holder or groups
             *
             * @param thisContext set it on method : binding.layout.setOnClickListener(thisContext);
             * @param binding     DataBinding
             * @param data        CalendarData
             */
            @Override
            public void setClickListeners(ViewHolderClickListener thisContext, AdapterYouPagesBinding binding, PageListData data) {
                binding.layoutYouPages.banner.setOnClickListener(thisContext);
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
            protected ViewHolderClickListener viewHolderReference(AdapterYouPagesBinding binding, PageListData data, int position) {
                return new ViewHolderClickListener<AdapterYouPagesBinding, PageListData>(binding, data, position) {

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
