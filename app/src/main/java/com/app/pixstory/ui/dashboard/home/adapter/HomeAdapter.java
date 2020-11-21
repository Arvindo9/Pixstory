package com.app.pixstory.ui.dashboard.home.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.app.pixstory.BuildConfig;
import com.app.pixstory.R;
import com.app.pixstory.base.BaseAdapter;
import com.app.pixstory.core.binding.BindingUtils;
import com.app.pixstory.data.model.home.HomeData;
import com.app.pixstory.data.model.home.HomeResponse;
import com.app.pixstory.data.model.page_list.PageListData;
import com.app.pixstory.databinding.AdapterHomeBinding;
import com.app.pixstory.databinding.CardPageHomeAdapterBinding;
import com.app.pixstory.databinding.CardUserFooterBinding;
import com.app.pixstory.databinding.CardUserMiddleBinding;
import com.app.pixstory.ui.dashboard.you.activity.PageDetailActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import static com.app.pixstory.base.BaseApplication.getContext;


public class HomeAdapter extends BaseAdapter<AdapterHomeBinding, HomeData> {

    private int viewHorizontal = -11;
    private int viewSquare = -13;
    private int viewPage = -15;
    private int viewAds = -20;
    private RecyclerView recyclerView;
    public static final String TAG = HomeAdapter.class.getSimpleName();
    public ArrayList<HomeData> pageData;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    public HomeAdapter(ArrayList<HomeData> data, RecyclerView recyclerView) {
        super(data);
        this.recyclerView = recyclerView;
        pageData = new ArrayList<>();
    }

    /**
     * @param position current index of ArrayList
     * @return return 0 if single layout xml else override this method for multiple xml or elements
     */
    @Override
    protected int getItemViewTypeAdapter(int position) {
        if(list.get(position) == null){
            return viewAds;
        } else if (list.get(position).getPageType() == 1) {
            return viewHorizontal;
        } else if (list.get(position).getPageType() == 2) {
            return viewSquare;
        } else if (list.get(position).getPageType() == 3) {
            return viewPage;
        }
        return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void add(HomeData r) {
        list.add(r);
        notifyItemInserted(list.size() - 1);
    }

    public void addAll(List<HomeData> moveResults) {
        for (HomeData result : moveResults) {
          //  add(result);
        }
    }

    public void remove(HomeData r) {
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
       // add(new HomeData());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = list.size() - 1;
        HomeData result = getItem(position);

        if (result != null) {
        //    list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public HomeData getItem(int position) {
        return list.get(position);
    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    protected int getLayout() {
        return R.layout.adapter_home;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == viewAds){
            return new AdviewHolder(googleAdsBanner(parent));
        }
        else if (viewType == viewHorizontal) {
            AdapterHomeBinding binding = (AdapterHomeBinding)
                    createBinding(parent, viewType, R.layout.adapter_home);
            return getViewHolder(binding);
        } else if (viewType == viewSquare) {
            CardUserMiddleBinding binding = (CardUserMiddleBinding)
                    createBinding(parent, viewType, R.layout.card_user_middle);
            return getSquareViewHolder(binding);
        } else if (viewType == viewPage) {
            CardPageHomeAdapterBinding binding = (CardPageHomeAdapterBinding)
                    createBinding(parent, viewType, R.layout.card_page_home_adapter);
            return getPageViewHolder(binding);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    private AdView googleAdsBanner(ViewGroup parent){
        AdView adview = new AdView(parent.getContext());
        adview.setAdSize(AdSize.MEDIUM_RECTANGLE);

        // this is the good adview
//        adview.setAdUnitId(AppConstants.ADS_UNIT_ID);
        adview.setAdUnitId(BuildConfig.ADS_BANNER_UNIT);

        float density = parent.getContext().getResources().getDisplayMetrics().density;
        int height = Math.round(AdSize.MEDIUM_RECTANGLE.getHeight() * density);

//        AbsListView.LayoutParams params = new
//                AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height);
//        adview.setLayoutParams(params);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,10, 0,25);
        adview.setLayoutParams(params);

        // dont use below if testing on a device
        // follow https://developers.google.com/admob/android/quick-start?hl=en to setup testing device
        AdRequest request = new AdRequest.Builder().build();
        adview.loadAd(request);
        return adview;
    }


    // HORIZONTAL IMAGE
    /**
     * Initialised View Holder
     *
     * @param binding DataBinding
     * @return new ViewHolder<B, D>(binding);
     */
    @Override
    public ViewHolder getViewHolder(AdapterHomeBinding binding) {
        return new ViewHolder<AdapterHomeBinding, HomeData>(binding) {
            /**
             * If there is anything to do then do here otherwise leave it blank
             *
             * @param binding  layout reference for single view
             * @param data     for single view
             * @param position position of ArrayList
             */
            @Override
            protected void doSomeWorkHere(AdapterHomeBinding binding, HomeData data, int position) {
                BindingUtils.setStringPhotos(binding.layoutUserHeader.banner, data.getStoryCoverImage(), binding.layoutUserHeader.progressBarBanner);
                BindingUtils.setStringPhotos(binding.layoutUserHeader.userImage, data.getProfileImage(), binding.layoutUserHeader.progressBar);
                binding.layoutUserHeader.username.setText(data.getUsername());
                binding.layoutUserHeader.integrityScore.setText("Integrity score:" + " " + data.getIntegrityScore());
                binding.layoutUserHeader.title.setText(data.getStoryTitle());
                binding.layoutUserHeader.publishedOn.setText(data.getPublishedOn());
                binding.layoutUserHeader.viewCount.setText(data.getViewCount() + " " + "Views");

                if (data.getIsTrending() == 1) {
                    binding.layoutUserHeader.isTrending.setVisibility(View.VISIBLE);
                } else {
                    binding.layoutUserHeader.isTrending.setVisibility(View.GONE);
                }

                if (data.getSupportCount() > 0) {
                    binding.layoutUserHeader.supportCount.setText(String.valueOf(data.getSupportCount()));
                } else {
                    binding.layoutUserHeader.supportCount.setVisibility(View.GONE);
                }

                if (data.getChallengeCount() > 0) {
                    binding.layoutUserHeader.challengeCount.setText(String.valueOf(data.getChallengeCount()));
                } else {
                    binding.layoutUserHeader.challengeCount.setVisibility(View.GONE);
                }

                if (data.getNoteCount() > 0) {
                    binding.layoutUserHeader.notesCount.setText(String.valueOf(data.getNoteCount()));
                } else {
                    binding.layoutUserHeader.notesCount.setVisibility(View.GONE);
                }

                BadgeAdapter badgeAdapter = new BadgeAdapter(new ArrayList<>());
                binding.layoutUserHeader.badgeRecyclerView.setAdapter(badgeAdapter);
                binding.layoutUserHeader.badgeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
                badgeAdapter.addItems(list.get(position).getBadges());
            }

            /**
             * @param data binding.setCalendarData(new AdapterViewModel(CalendarData));
             */
            @Override
            protected void bindData(HomeData data) {
                binding.setData(new HomeInterestViewModel(data));
            }

            /**
             * Method to set click listeners on view holder or groups
             *
             * @param thisContext set it on method : binding.layout.setOnClickListener(thisContext);
             * @param binding     DataBinding
             * @param data        CalendarData
             */
            @Override
            public void setClickListeners(ViewHolderClickListener thisContext, AdapterHomeBinding binding, HomeData data) {
                binding.layoutUserHeader.userBanner.setOnClickListener(thisContext);
                binding.layoutUserHeader.profileSection.setOnClickListener(thisContext);

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
            protected ViewHolderClickListener viewHolderReference(AdapterHomeBinding binding, HomeData data, int position) {
                return new ViewHolderClickListener<AdapterHomeBinding, HomeData>(binding, data, position) {
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
                        if (view.getId() == R.id.user_banner) {
                            getListener().onAdapterItem(data, "horizontal_banner", position);
                        } else if (view.getId() == R.id.profileSection) {
                            getListener().onAdapterItem(data, "horizontal_profile", position);
                        }
                    }
                };
            }
        };
    }

    // SQUARE IMAGE
    private ViewHolder getSquareViewHolder(CardUserMiddleBinding binding) {
        return new ViewHolder<CardUserMiddleBinding, HomeData>(binding) {
            /**
             * If there is anything to do then do here otherwise leave it blank
             *
             * @param binding  layout reference for single view
             * @param data     for single view
             * @param position position of ArrayList
             */
            @Override
            protected void doSomeWorkHere(CardUserMiddleBinding binding, HomeData data, int position) {
                BindingUtils.setStringPhotos(binding.banner, data.getStoryCoverImage(), binding.progressBar);
                binding.username.setText(data.getUsername());
                binding.integrityScore.setText("Integrity score:" + " " + data.getIntegrityScore());
                binding.title.setText(data.getStoryTitle());
                binding.publishedOn.setText(data.getPublishedOn());
                binding.viewCount.setText(data.getViewCount() + " " + "Views");
                if (data.getIsTrending() == 1) {
                    binding.isTrending.setVisibility(View.VISIBLE);
                } else {
                    binding.isTrending.setVisibility(View.GONE);
                }
                if (data.getSupportCount() > 0) {
                    binding.supportCount.setText(String.valueOf(data.getSupportCount()));
                } else {
                    binding.supportCount.setVisibility(View.GONE);
                }

                if (data.getChallengeCount() > 0) {
                    binding.challengeCount.setText(String.valueOf(data.getChallengeCount()));
                } else {
                    binding.challengeCount.setVisibility(View.GONE);
                }

                if (data.getNoteCount() > 0) {
                    binding.notesCount.setText(String.valueOf(data.getNoteCount()));
                } else {
                    binding.notesCount.setVisibility(View.GONE);
                }

                BadgeAdapter badgeAdapter = new BadgeAdapter(new ArrayList<>());
                binding.badgeRecyclerView.setAdapter(badgeAdapter);
                binding.badgeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
                badgeAdapter.addItems(list.get(position).getBadges());
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
            public void setClickListeners(ViewHolderClickListener thisContext, CardUserMiddleBinding binding, HomeData data) {
                binding.banner.setOnClickListener(thisContext);
                binding.username.setOnClickListener(thisContext);
                binding.userBanner.setOnClickListener(thisContext);
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
            protected ViewHolderClickListener viewHolderReference(CardUserMiddleBinding binding, HomeData data, int position) {
                return new ViewHolderClickListener<CardUserMiddleBinding, HomeData>(binding, data, position) {
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
                            getListener().onAdapterItem(data, "square_banner", position);
                        } else if (view.getId() == R.id.user_banner) {
                            getListener().onAdapterItem(data, "square_banner", position);
                        } else if (view.getId() == R.id.username) {
                            getListener().onAdapterItem(data, "square_profile", position);
                        }
                    }
                };
            }
        };
    }

    // PAGE (VERTICAL IMAGE)
    private ViewHolder getPageViewHolder(CardPageHomeAdapterBinding binding) {
        return new ViewHolder<CardPageHomeAdapterBinding, HomeData>(binding) {
            /**
             * If there is anything to do then do here otherwise leave it blank
             *
             * @param binding  layout reference for single view
             * @param data     for single view
             * @param position position of ArrayList
             */
            @Override
            protected void doSomeWorkHere(CardPageHomeAdapterBinding binding, HomeData data, int position) {
                pageData.add(data);
                if (pageData.size() == 2) {
                    PageAdapter pageAdapter = new PageAdapter(new ArrayList<>());
                       pageAdapter.setListener(this::onAdapterItem);
                    binding.pageRecyclerView.setAdapter(pageAdapter);
                    binding.pageRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    pageAdapter.addItems(pageData);
                }
            }

            private void onAdapterItem(Object data, String string, int position) {
                if (data instanceof HomeData){
                    if (((HomeData) data).getPageId() != null && ((HomeData) data).getPageCoverImage() != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("page_id", ((HomeData) data).getPageId());
                        bundle.putString("image", ((HomeData) data).getPageCoverImage());
                        Intent intent = new Intent(getContext(), PageDetailActivity.class);
                        intent.putExtra("bundl", bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                    }
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
            public void setClickListeners(ViewHolderClickListener thisContext, CardPageHomeAdapterBinding binding, HomeData data) {
                //  binding.banner.setOnClickListener(thisContext);
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
            protected ViewHolderClickListener viewHolderReference(CardPageHomeAdapterBinding binding, HomeData data, int position) {
                return new ViewHolderClickListener<CardPageHomeAdapterBinding, HomeData>(binding, data, position) {
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
                            getListener().onAdapterItem(data, "vertical", position);
                        }
                    }
                };
            }
        };
    }

    private class AdviewHolder extends ViewHolder<ViewDataBinding, HomeData> {

        public AdviewHolder(AdView adview) {
            super(adview);
        }
        @Override
        protected void doSomeWorkHere(ViewDataBinding binding, HomeData data, int position) {}
        @Override
        protected void bindData(HomeData data) {}
        @Override
        public void setClickListeners(ViewHolderClickListener thisContext, ViewDataBinding binding,
                                      HomeData data) {}
        @Override
        protected ViewHolderClickListener viewHolderReference(ViewDataBinding binding, HomeData data,
                                                              int position) {
            return null;
        }
    }



    /**
     * @return new FilterClass();
     */
    @Override
    protected FilterClass initialisedFilterClass() {
        return null;
    }

}
