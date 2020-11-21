package com.app.pixstory.ui.dashboard.you.adapter;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseAdapter;
import com.app.pixstory.core.binding.BindingUtils;
import com.app.pixstory.data.model.get_story_user.Story;
import com.app.pixstory.data.model.upload.Data;
import com.app.pixstory.databinding.AdapterYouStoriesBinding;
import com.app.pixstory.ui.dashboard.home.adapter.BadgeAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.app.pixstory.base.BaseApplication.getContext;

public class YouStoriesAdapter extends BaseAdapter<AdapterYouStoriesBinding, Story> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    /**
     * @param adapterList list args require to bind adapter up to the size of array
     */
    public YouStoriesAdapter(ArrayList<Story> adapterList) {
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

    public void add(Story r) {
        list.add(r);
        notifyItemInserted(list.size() - 1);
    }

    public void addAll(List<Story> moveResults) {
        for (Story result : moveResults) {
            //  add(result);
        }
    }

    public void remove(Story r) {
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
        // add(new Data());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = list.size() - 1;
        Story result = getItem(position);

        if (result != null) {
           // list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Story getItem(int position) {
        return list.get(position);
    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    protected int getLayout() {
        return R.layout.adapter_you_stories;
    }

    /**
     * Initialised View Holder
     *
     * @param binding DataBinding
     * @return new ViewHolder<B, D>(binding);
     */
    @Override
    public ViewHolder getViewHolder(AdapterYouStoriesBinding binding) {
        return new ViewHolder<AdapterYouStoriesBinding, Story>(binding) {
            /**
             * If there is anything to do then do here otherwise leave it blank
             *
             * @param binding  layout reference for single view
             * @param data     for single view
             * @param position position of ArrayList
             */
            @Override
            protected void doSomeWorkHere(AdapterYouStoriesBinding binding, Story data, int position) {
                    BindingUtils.setStringPhotos(binding.listUserSection.banner, data.getCoverImgPath(), binding.listUserSection.progressBar);
                BindingUtils.setStringPhotos(binding.listUserSection.userImage, data.getProfileImage(), binding.listUserSection.progressBarBanner);
                binding.listUserSection.username.setText(data.getUsername());
                binding.listUserSection.integrityScore.setText("Integrity score:" + " " + data.getIntegrityScore());
                binding.listUserSection.title.setText(data.getTitle());
                binding.listUserSection.publishedOn.setText(data.getPublishedOn());
                binding.listUserSection.viewCount.setText(data.getViewCount() + " " + "Views");
                if (data.getSupportCount() > 0){
                    binding.listUserSection.supportCount.setText(String.valueOf(data.getSupportCount()));
                } else {
                    binding.listUserSection.supportCount.setVisibility(View.GONE);
                }

                if (data.getChallengeCount() > 0){
                    binding.listUserSection.challengeCount.setText(String.valueOf(data.getChallengeCount()));
                } else {
                    binding.listUserSection.challengeCount.setVisibility(View.GONE);
                }

                if (data.getNoteCount() > 0){
                    binding.listUserSection.notesCount.setText(String.valueOf(data.getNoteCount()));
                } else {
                    binding.listUserSection.notesCount.setVisibility(View.GONE);
                }

                BadgeAdapter badgeAdapter = new BadgeAdapter(new ArrayList<>());
                binding.listUserSection.badgeRecyclerView.setAdapter(badgeAdapter);
                binding.listUserSection.badgeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
                badgeAdapter.addItems(list.get(position).getBadges());
            }

            /**
             * @param data binding.setCalendarData(new AdapterViewModel(CalendarData));
             */
            @Override
            protected void bindData(Story data) {

            }

            /**
             * Method to set click listeners on view holder or groups
             *
             * @param thisContext set it on method : binding.layout.setOnClickListener(thisContext);
             * @param binding     DataBinding
             * @param data        CalendarData
             */
            @Override
            public void setClickListeners(ViewHolderClickListener thisContext, AdapterYouStoriesBinding binding, Story data) {
                binding.listUserSection.profileSection.setOnClickListener(thisContext);
                binding.listUserSection.userBanner.setOnClickListener(thisContext);
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
            protected ViewHolderClickListener viewHolderReference(AdapterYouStoriesBinding binding, Story data, int position) {
                return new ViewHolderClickListener<AdapterYouStoriesBinding, Story>(binding, data, position) {
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
                        if (view.getId() == R.id.user_banner){
                            getListener().onAdapterItem(data, "", position);
                        } else if (view.getId() == R.id.profileSection){

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
