package com.app.pixstory.ui.dashboard.others.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pixstory.R;


import com.app.pixstory.data.model.api.UserHomeListResponse;
import com.app.pixstory.utils.Utils;

import java.util.List;

public class BadgesAdapter extends RecyclerView.Adapter<BadgesAdapter.ViewHolder> {

    private List<UserHomeListResponse.UserBadge> badgeList;

    public BadgesAdapter(List<UserHomeListResponse.UserBadge> badgeList) {
        this.badgeList = badgeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.badges_view, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Utils.setImageFromPath(holder.itemView.getContext(), holder.imageView, badgeList.get(position).getBadgeIconPath(), holder.progress);
    }

    @Override
    public int getItemCount() {
        return badgeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ProgressBar progress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            progress = itemView.findViewById(R.id.progress);
        }
    }
}
