package com.app.pixstory.ui.dashboard.you.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pixstory.R;
import com.app.pixstory.base.flex.FlexboxAdapter;
import com.app.pixstory.data.DataManager;
import com.app.pixstory.data.DataManagerService;
import com.app.pixstory.data.model.Interest;
import com.app.pixstory.data.model.MemberData;
import com.app.pixstory.data.model.PageMemberResponse;
import com.app.pixstory.data.model.api.AddInterestResponse;
import com.app.pixstory.data.remote.APIService;
import com.app.pixstory.ui.dashboard.others.adapter.BadgesAdapter;
import com.app.pixstory.ui.dashboard.upload.api.ApiClient;
import com.app.pixstory.utils.Utils;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeeMoreMemberAdapter extends PagedListAdapter<MemberData, SeeMoreMemberAdapter.ViewHolder> {

    private ItemClick itemClick;
    private final int LIMIT = 3;
    private DataManager dataManager;
    private FlexboxAdapter adapter;

    public SeeMoreMemberAdapter() {
        super(DIFF_CALLBACK);
        dataManager = DataManagerService.getInstance();
    }


    public interface ItemClick {
        void follow(MemberData data, int position);

        void chat(MemberData data,int position);

        void itemClicked(Bundle bundle);

    }

    public void setListener(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MemberData memberData = getItem(position);
        if (memberData != null) {
            List<String> interestList = getStringInterest(memberData.getInterest());
            holder.name.setText(memberData.getUsername());
            holder.integrityScore.setText("Integrity Score : " + memberData.getIntegrityScore());
            Utils.setImageFromPath(holder.itemView.getContext(), holder.image, memberData.getProfileImage(), holder.progressBar);
            holder.location.setText(memberData.getCountryName());

            if (memberData.getIsFriend() == 1)
                holder.friend.setText("Friend");
            else if (memberData.getIsFriend() == 2)
                holder.friend.setText("Friend of friend");
            else
                holder.friend.setVisibility(View.GONE);

            if (memberData.getBadges() != null && memberData.getBadges().size() > 0) {
                BadgesAdapter badgesAdapter = new BadgesAdapter(memberData.getBadges());
                holder.badgesRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
                holder.badgesRecyclerView.setAdapter(badgesAdapter);
            }

            if (memberData.getIsFollow() == 0)
                holder.follow.setText("Follow");
            else
                holder.follow.setText("Following");

            holder.follow.setOnClickListener(v -> itemClick.follow(memberData, position));

            holder.chat.setOnClickListener(v -> itemClick.chat(memberData, position));

            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(holder.itemView.getContext());
            holder.interest.setLayoutManager(layoutManager);
            if (memberData.getInterest().size() <= LIMIT && memberData.getInterest().size() == memberData.getInterestCount())
                adapter = new FlexboxAdapter(interestList, false, 12);
            else
                adapter = new FlexboxAdapter(interestList, true, 12);
            holder.interest.setAdapter(adapter);

            adapter.setInterestListener(new FlexboxAdapter.FlexCallback() {
                @Override
                public void addInterest() {

                    Utils.vibrate(holder.itemView.getContext());
                    paginateInterest(memberData.getItem() + 1, memberData, holder);

                }
            });

            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putInt("user_id", memberData.getUserId());
                    itemClick.itemClicked(bundle);
                }
            });

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putInt("user_id", memberData.getUserId());
                    itemClick.itemClicked(bundle);
                }
            });

        }
    }

    private void paginateInterest(int i, MemberData data, ViewHolder holder) {

        ApiClient.getClient().create(APIService.class)
                .addInterest("Bearer" + dataManager.getAccessToken(), data.getUserId(), i, LIMIT)
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
                                    FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(holder.itemView.getContext());
                                    holder.interest.setLayoutManager(layoutManager);
                                    FlexboxAdapter adapter = new FlexboxAdapter(getStringInterest(data.getInterest()), false, 12);
                                    holder.interest.setAdapter(adapter);
                                } else {
                                    notifyDataSetChanged();
                                }
                                data.setItem(i);
                                //item.set(position, i);
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

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView interest;
        TextView integrityScore, name, location, friend, follow, chat;
        CircleImageView image;
        ProgressBar progressBar;
        RecyclerView badgesRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            interest = itemView.findViewById(R.id.interest);
            integrityScore = itemView.findViewById(R.id.integrity_score);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            progressBar = itemView.findViewById(R.id.progressBar);
            badgesRecyclerView = itemView.findViewById(R.id.badge_recycler_view);
            location = itemView.findViewById(R.id.location);
            friend = itemView.findViewById(R.id.friend);
            follow = itemView.findViewById(R.id.follow);
            chat = itemView.findViewById(R.id.chat);
        }
    }

    private static DiffUtil.ItemCallback<MemberData> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<MemberData>() {
                @Override
                public boolean areItemsTheSame(MemberData oldItem, MemberData newItem) {
                    return oldItem.getUserId() == newItem.getUserId();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(MemberData oldItem, MemberData newItem) {
                    return oldItem.equals(newItem);
                }
            };

}
