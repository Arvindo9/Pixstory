package com.app.pixstory.ui.dashboard.you.adapter;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.pixstory.R;
import com.app.pixstory.base.BaseAdapter;
import com.app.pixstory.base.flex.FlexboxAdapter;
import com.app.pixstory.core.binding.BindingUtils;
import com.app.pixstory.data.DataManager;
import com.app.pixstory.data.DataManagerService;
import com.app.pixstory.data.model.api.AddInterestResponse;
import com.app.pixstory.data.model.api.UserHomeListResponse;
import com.app.pixstory.data.model.you_user.Interest;
import com.app.pixstory.data.model.you_user.UserList;
import com.app.pixstory.data.remote.APIService;
import com.app.pixstory.databinding.AdapterUsernameBinding;
import com.app.pixstory.ui.dashboard.upload.api.ApiClient;
import com.app.pixstory.utils.Utils;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.pixstory.utils.Constants.YOU_CHAT;
import static com.app.pixstory.utils.Constants.YOU_FOLLOW;

public class UsernamesAdapter extends BaseAdapter<AdapterUsernameBinding, UserList> {
    public static final String TAG = UsernamesAdapter.class.getSimpleName();

    private final int LIMIT = 3;
    private DataManager dataManager;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    /**
     * @param adapterList list args require to bind adapter up to the size of array
     */
    public UsernamesAdapter(ArrayList<UserList> adapterList) {
        super(adapterList);
        dataManager = DataManagerService.getInstance();
    }

    /**
     * @param position current index of ArrayList
     * @return return 0 if single layout xml else override this method for multiple xml or elements
     */
    @Override
    protected int getItemViewTypeAdapter(int position) {
        return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void add(UserList r) {
        list.add(r);
        notifyItemInserted(list.size() - 1);
    }

    public void addAll(List<UserList> moveResults) {
        for (UserList result : moveResults) {
            //  add(result);
        }
    }

    public void remove(UserList r) {
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
        UserList result = getItem(position);

        if (result != null) {
            // list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public UserList getItem(int position) {
        return list.get(position);
    }

    /**
     * @return R.layout.layout_file
     */
    @Override
    protected int getLayout() {
        return R.layout.adapter_username;
    }

    /**
     * Initialised View Holder
     *
     * @param binding DataBinding
     * @return new ViewHolder<B, D>(binding);
     */
    @Override
    public ViewHolder getViewHolder(AdapterUsernameBinding binding) {
        return new ViewHolder<AdapterUsernameBinding, UserList>(binding) {
            /**
             * If there is anything to do then do here otherwise leave it blank
             *
             * @param binding  layout reference for single view
             * @param data     for single view
             * @param position position of ArrayList
             */
            @Override
            protected void doSomeWorkHere(AdapterUsernameBinding binding, UserList data, int position) {
                BindingUtils.setStringPhotos(binding.banner, data.getProfileImage(), binding.progressBar);
                binding.username.setText(data.getUsername());
                binding.integrityScore.setText("Integrity Score:" + data.getIntegrityScore());
                binding.location.setText(data.getCountryName());

                // FRIEND
                if (data.getIsFriend() == 1) {
                    binding.friend.setText("Friend");
                } else if (data.getIsFriend() == 2) {
                    binding.friend.setText("Friend of friend");
                } else {
                    binding.friend.setVisibility(View.GONE);
                }
                // PRO USER
                if (data.getIsPro() != null) {
                    if (data.getIsPro() == 0)
                        binding.proUser.setVisibility(View.GONE);
                    else
                        binding.proUser.setVisibility(View.VISIBLE);
                }

                // BADGE
                if (data.getBadges() != null && data.getBadges().size() > 0) {
                    BadgeAdapter badgesAdapter = new BadgeAdapter(data.getBadges());
                    binding.recyclerViewBadges.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
                    binding.recyclerViewBadges.setAdapter(badgesAdapter);
                }

                // USER INTEREST
                List<String> interestList = getStringInterest(data.getInterest());
                FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(itemView.getContext());
                binding.interest.setLayoutManager(layoutManager);
                FlexboxAdapter adapter;
                if (data.getInterestCount() <= LIMIT || interestList.size() == data.getInterestCount())
                    adapter = new FlexboxAdapter(interestList, false, 12);
                else
                    adapter = new FlexboxAdapter(interestList, true, 12);
                binding.interest.setAdapter(adapter);

                adapter.setInterestListener(new FlexboxAdapter.FlexCallback() {
                    @Override
                    public void addInterest() {
                        Utils.vibrate(itemView.getContext());
                        paginateInterest(data.getItem() + 1, data);
                    }
                });
            }

            private void paginateInterest(int i, UserList data) {
                ApiClient.getClient().create(APIService.class)
                        .addInterest("Bearer" + dataManager.getAccessToken(), data.getId(), i, LIMIT)
                        .enqueue(new Callback<AddInterestResponse>() {
                            @Override
                            public void onResponse(Call<AddInterestResponse> call, Response<AddInterestResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.body() != null && response.body().getSuccess()) {
                                        data.setSize(data.getSize() + response.body().getData().size());
                                        //size.set(position, size.get(position) + response.body().getData().size());

                                        List<Interest> interestList = new ArrayList<>();

                                        for (int i = 0; i < response.body().getData().size(); i++) {
                                            interestList.add(new Interest(response.body().getData().get(i)));
                                        }
                                        data.setInterest(interestList);
                                        //data.getUserInterest().addAll(interestList);
                                        if (data.getSize().compareTo(data.getInterestCount()) == 0) {
                                            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(itemView.getContext());
                                            binding.interest.setLayoutManager(layoutManager);
                                            FlexboxAdapter adapter = new FlexboxAdapter(getStringInterest(data.getInterest()), false, 12);
                                            binding.interest.setAdapter(adapter);
                                        } else {
                                            notifyDataSetChanged();
                                        }
                                        data.setItem(i);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<AddInterestResponse> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
            }

            private List<String> getStringInterest(List<Interest> interests) {
                List<String> interest = new ArrayList<>();
                for (int i = 0; i < interests.size(); i++) {
                    interest.add(interests.get(i).getInterest());
                }
                return interest;
            }

            /**
             * @param data binding.setCalendarData(new AdapterViewModel(CalendarData));
             */
            @Override
            protected void bindData(UserList data) {

            }

            /**
             * Method to set click listeners on view holder or groups
             *
             * @param thisContext set it on method : binding.layout.setOnClickListener(thisContext);
             * @param binding     DataBinding
             * @param data        CalendarData
             */
            @Override
            public void setClickListeners(ViewHolderClickListener thisContext, AdapterUsernameBinding binding, UserList data) {
                binding.follow.setOnClickListener(thisContext);
                binding.chat.setOnClickListener(thisContext);
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
            protected ViewHolderClickListener viewHolderReference(AdapterUsernameBinding binding, UserList data, int position) {
                return new ViewHolderClickListener<AdapterUsernameBinding, UserList>(binding, data, position) {
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
                        if (view.getId() == R.id.follow) {
                            getListener().onAdapterItem(data, YOU_FOLLOW, position);
                        } else if (view.getId() == R.id.chat) {
                            getListener().onAdapterItem(data, YOU_CHAT, position);
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

    private static DiffUtil.ItemCallback<UserHomeListResponse.Data> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<UserHomeListResponse.Data>() {
                @Override
                public boolean areItemsTheSame(UserHomeListResponse.Data oldItem, UserHomeListResponse.Data newItem) {
                    return oldItem.getUserId() == newItem.getUserId();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(UserHomeListResponse.Data oldItem, UserHomeListResponse.Data newItem) {
                    return oldItem.equals(newItem);
                }
            };

}
