package com.app.pixstory.ui.dashboard.others.adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pixstory.R;
import com.app.pixstory.base.flex.FlexboxAdapter;
import com.app.pixstory.data.DataManager;
import com.app.pixstory.data.DataManagerService;
import com.app.pixstory.data.model.api.AddInterestResponse;
import com.app.pixstory.data.model.api.UserHomeListResponse;
import com.app.pixstory.data.remote.APIService;
import com.app.pixstory.ui.dashboard.upload.api.ApiClient;
import com.app.pixstory.utils.Utils;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ByInterestAdapter extends PagedListAdapter<UserHomeListResponse.Data, ByInterestAdapter.ViewHolder> {

    private ItemClick itemClick;
    private final int LIMIT = 3;
    private DataManager dataManager;


    public ByInterestAdapter() {
        super(DIFF_CALLBACK);
        dataManager = DataManagerService.getInstance();
    }

    public interface ItemClick {

        void itemClicked(Bundle bundle);
        void follow(UserHomeListResponse.Data data, int position);

        void chat(UserHomeListResponse.Data data,int position);

        void message(String message);

        void message(int message);
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_other, viewGroup, false);

        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserHomeListResponse.Data data = getItem(position);

        if (data != null) {
            List<String> interestList = getStringInterest(data.getUserInterest());

            if (data.getIntegrityScore() == null)
                holder.integrityScore.setText("Integrity score : N/A");
            else
                holder.integrityScore.setText("Integrity score : " + data.getIntegrityScore());

            holder.name.setText(data.getUsername());
            holder.location.setText(data.getCountryName());

            if (data.getIsFriend() == 1)
                holder.friend.setText("Friend");
            else if (data.getIsFriend() == 2)
                holder.friend.setText("Friend of friend");
            else
                holder.friend.setVisibility(View.GONE);

            if (data.getIsPro() == 0)
                holder.proText.setVisibility(View.GONE);
            else
                holder.proText.setVisibility(View.VISIBLE);

            if (data.getIsFollow() == 0)
                holder.follow.setText("Follow");
            else
                holder.follow.setText("Unfollow");

            if (data.getUserBadge() != null && data.getUserBadge().size() > 0) {
                BadgesAdapter badgesAdapter = new BadgesAdapter(data.getUserBadge());
                holder.badgesRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
                holder.badgesRecyclerView.setAdapter(badgesAdapter);
            }

            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(holder.itemView.getContext());
            holder.interest.setLayoutManager(layoutManager);
            FlexboxAdapter adapter;
            if (data.getInterestCount() <= LIMIT || interestList.size() == data.getInterestCount())
                adapter = new FlexboxAdapter(interestList, false, 12);
            else
                adapter = new FlexboxAdapter(interestList, true, 12);
            holder.interest.setAdapter(adapter);

            adapter.setInterestListener(new FlexboxAdapter.FlexCallback() {
                @Override
                public void addInterest() {

                    Utils.vibrate(holder.itemView.getContext());
                    paginateInterest(data.getItem() + 1, data, holder);

                }
            });

            Utils.setImageFromPath(holder.itemView.getContext(), holder.image, data.getProfileImage(), holder.progressBar);

            holder.follow.setOnClickListener(v ->

                    itemClick.follow(data, position));

            holder.chat.setOnClickListener(v ->

                    itemClick.chat(data, position));

            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putInt("user_id", data.getUserId());
                    itemClick.itemClicked(bundle);
                }
            });

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putInt("user_id", data.getUserId());
                    itemClick.itemClicked(bundle);
                }
            });

        } else {
            Toast.makeText(holder.itemView.getContext(), "User data is null", Toast.LENGTH_LONG).show();
        }
    }

    private void paginateInterest(int i, UserHomeListResponse.Data data, ViewHolder holder) {

        ApiClient.getClient().create(APIService.class)
                .addInterest("Bearer" + dataManager.getAccessToken(), data.getUserId(), i, LIMIT)
                .enqueue(new Callback<AddInterestResponse>() {
                    @Override
                    public void onResponse(Call<AddInterestResponse> call, Response<AddInterestResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getSuccess()) {
                                data.setSize(data.getSize() + response.body().getData().size());
                                //size.set(position, size.get(position) + response.body().getData().size());

                                List<UserHomeListResponse.UserInterest> interestList = new ArrayList<>();

                                for (int i = 0; i < response.body().getData().size(); i++) {
                                    interestList.add(new UserHomeListResponse.UserInterest(response.body().getData().get(i)));
                                }
                                data.setUserInterest(interestList);
                                //data.getUserInterest().addAll(interestList);

                                if (data.getSize().compareTo(data.getInterestCount()) == 0) {
                                    FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(holder.itemView.getContext());
                                    holder.interest.setLayoutManager(layoutManager);
                                    FlexboxAdapter adapter = new FlexboxAdapter(getStringInterest(data.getUserInterest()), false, 12);
                                    holder.interest.setAdapter(adapter);
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


    private List<String> getStringInterest(List<UserHomeListResponse.UserInterest> interests) {
        List<String> interest = new ArrayList<>();
        for (int i = 0; i < interests.size(); i++) {
            interest.add(interests.get(i).getInterest());
        }
        return interest;
    }

    public void setListener(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, integrityScore, location, chat, follow, friend;
        ImageView proText;
        CircleImageView image;
        RecyclerView interest;
        ProgressBar progressBar;
        RecyclerView badgesRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            integrityScore = itemView.findViewById(R.id.integrity_score);
            location = itemView.findViewById(R.id.location);
            image = itemView.findViewById(R.id.image);
            interest = itemView.findViewById(R.id.interest);
            progressBar = itemView.findViewById(R.id.progressBar);
            follow = itemView.findViewById(R.id.follow);
            chat = itemView.findViewById(R.id.chat);
            friend = itemView.findViewById(R.id.friend);
            proText = itemView.findViewById(R.id.pro_text);
            badgesRecyclerView = itemView.findViewById(R.id.recycler_view_badges);
        }
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

