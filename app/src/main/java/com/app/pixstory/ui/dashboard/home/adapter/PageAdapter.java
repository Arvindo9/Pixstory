package com.app.pixstory.ui.dashboard.home.adapter;

import android.view.View;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseAdapter;
import com.app.pixstory.core.binding.BindingUtils;
import com.app.pixstory.data.model.home.HomeData;
import com.app.pixstory.databinding.PageAdapterBinding;

import java.util.ArrayList;

/**
 * Created by Kamlesh Yadav on 21-04-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class PageAdapter extends BaseAdapter<PageAdapterBinding, HomeData> {
    /**
     * @param adapterList list args require to bind adapter up to the size of array
     */
    public PageAdapter(ArrayList<HomeData> adapterList) {
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
        return R.layout.page_adapter;
    }

    /**
     * Initialised View Holder
     *
     * @param binding DataBinding
     * @return new ViewHolder<B, D>(binding);
     */
    @Override
    public ViewHolder getViewHolder(PageAdapterBinding binding) {
        return new ViewHolder<PageAdapterBinding, HomeData>(binding) {
            /**
             * If there is anything to do then do here otherwise leave it blank
             *
             * @param binding  layout reference for single view
             * @param data     for single view
             * @param position position of ArrayList
             */
            @Override
            protected void doSomeWorkHere(PageAdapterBinding binding, HomeData data, int position) {
                BindingUtils.setStringPhotos(binding.banner, data.getPageCoverImage(), binding.progressBar);

                binding.title.setText(data.getPageTitle());

                if (data.getIsTrending() == 1) {
                    binding.isTrending.setVisibility(View.VISIBLE);

                } else {
                    binding.isTrending.setVisibility(View.GONE);

                }

            }

            /**
             * @param data binding.setCalendarData(new AdapterViewModel(CalendarData));
             */
            @Override
            protected void bindData(HomeData data) {

            }

            /**
             * Method to set click listeners on view holder or groups
             *
             * @param thisContext set it on method : binding.layout.setOnClickListener(thisContext);
             * @param binding     DataBinding
             * @param data        CalendarData
             */
            @Override
            public void setClickListeners(ViewHolderClickListener thisContext, PageAdapterBinding binding, HomeData data) {
                binding.banner.setOnClickListener(thisContext);
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
            protected ViewHolderClickListener viewHolderReference(PageAdapterBinding binding, HomeData data, int position) {
                return new ViewHolderClickListener<PageAdapterBinding, HomeData>(binding, data, position) {
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
                        if (view.getId() == R.id.banner){
                            getListener().onAdapterItem(data, "page", position);
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
