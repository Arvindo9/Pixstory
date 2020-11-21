package com.app.pixstory.ui.dashboard.you.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.app.pixstory.R;
import com.app.pixstory.data.model.StoryData;
import com.app.pixstory.ui.dashboard.others.adapter.BadgesAdapter;
import com.app.pixstory.utils.Utils;

public class SeeMoreStoriesAdapter extends PagedListAdapter<StoryData, SeeMoreStoriesAdapter.ViewHolder> {

    public SeeMoreStoriesAdapter() {
        super(DIFF_CALLBACK);
    }

    private ItemCallback itemCallback;

    public interface ItemCallback {
        void itemClicked(Bundle bundle);
        void shareStory(int storyId);
    }

    public void setListener(ItemCallback itemCallback) {
        this.itemCallback = itemCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user_middle, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        StoryData storyData = getItem(position);
        if (storyData != null) {
            holder.bottomText.setVisibility(View.GONE);
            holder.username.setText(storyData.getUsername());
            holder.title.setText(storyData.getStoryTitle());
            holder.integrityScore.setText("Integrity Score : " + storyData.getIntegrityScore());
            holder.publishedOn.setText(storyData.getPublishedOn());
            holder.viewCount.setText(storyData.getViewCount() + " Views");
            Utils.setImageFromPath(holder.itemView.getContext(), holder.banner, storyData.getStoryCoverImage(), holder.progressBar);
            if (storyData.getSupportCount() > 0) {
                holder.supportCount.setVisibility(View.VISIBLE);
                holder.supportCount.setText(String.valueOf(storyData.getSupportCount()));
            } else {
                holder.supportCount.setVisibility(View.GONE);
            }

            if (storyData.getChallengeCount() > 0) {
                holder.challengeCount.setVisibility(View.VISIBLE);
                holder.challengeCount.setText(String.valueOf(storyData.getChallengeCount()));
            } else {
                holder.challengeCount.setVisibility(View.GONE);
            }

            if (storyData.getNoteCount() > 0) {
                holder.notesCount.setVisibility(View.VISIBLE);
                holder.notesCount.setText(String.valueOf(storyData.getNoteCount()));
            } else {
                holder.notesCount.setVisibility(View.GONE);
            }

            if (storyData.getBadges() != null && storyData.getBadges().size() > 0) {
                BadgesAdapter badgesAdapter = new BadgesAdapter(storyData.getBadges());
                holder.badgeRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
                holder.badgeRecyclerView.setAdapter(badgesAdapter);
            }

            holder.username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putInt("user_id", storyData.getUserId());
                    itemCallback.itemClicked(bundle);

                }
            });

            holder.banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putInt("story_id", storyData.getStoryId());
                    itemCallback.itemClicked(bundle);
                }
            });

            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    itemCallback.shareStory(storyData.getStoryId());
                }
            });

        }
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
        TextView username, integrityScore, publishedOn, viewCount, title, supportCount, challengeCount, notesCount, bottomText;
        ImageView banner,share;
        ProgressBar progressBar;
        RecyclerView badgeRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            title = itemView.findViewById(R.id.title);
            integrityScore = itemView.findViewById(R.id.integrity_score);
            banner = itemView.findViewById(R.id.banner);
            progressBar = itemView.findViewById(R.id.progressBar);
            supportCount = itemView.findViewById(R.id.support_count);
            challengeCount = itemView.findViewById(R.id.challenge_count);
            notesCount = itemView.findViewById(R.id.notes_count);
            publishedOn = itemView.findViewById(R.id.published_on);
            viewCount = itemView.findViewById(R.id.view_count);
            badgeRecyclerView = itemView.findViewById(R.id.badgeRecyclerView);
            share = itemView.findViewById(R.id.share);
            bottomText = itemView.findViewById(R.id.bottom_text);
        }
    }

    private static DiffUtil.ItemCallback<StoryData> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<StoryData>() {
                @Override
                public boolean areItemsTheSame(StoryData oldItem, StoryData newItem) {
                    return oldItem.getUserId() == newItem.getUserId();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(StoryData oldItem, StoryData newItem) {
                    return oldItem.equals(newItem);
                }
            };

}
