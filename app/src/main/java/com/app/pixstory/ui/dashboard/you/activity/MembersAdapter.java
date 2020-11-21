package com.app.pixstory.ui.dashboard.you.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pixstory.R;
import com.app.pixstory.data.model.PageDetailResponse;
import com.app.pixstory.ui.dashboard.others.adapter.BadgesAdapter;

import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {

    private List<PageDetailResponse.PageMember> memberList;
    private ItemCallback itemCallback;
    public interface ItemCallback
    {
        void itemClicked(Bundle bundle);
    }

    public MembersAdapter(List<PageDetailResponse.PageMember> memberList) {
        this.memberList = memberList;
    }

    public void setListener(ItemCallback itemCallback)
    {
        this.itemCallback = itemCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.members_layout, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text.setText(memberList.get(position).getUsername());
        holder.integrityScore.setText("Integrity Score : " + memberList.get(position).getIntegrityScore());
        if (memberList.get(position).getLevel() == 1) {
            holder.admin.setVisibility(View.VISIBLE);
        } else {
            holder.admin.setVisibility(View.GONE);
        }

        if (memberList.get(position).getBadge() != null && memberList.get(position).getBadge().size() > 0) {
            BadgesAdapter badgesAdapter = new BadgesAdapter(memberList.get(position).getBadge());
            holder.badgeRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            holder.badgeRecyclerView.setAdapter(badgesAdapter);
        }

        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("user_id", memberList.get(position).getUserId());
                itemCallback.itemClicked(bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text, integrityScore, admin;
        RecyclerView badgeRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            integrityScore = itemView.findViewById(R.id.integrity_score);
            admin = itemView.findViewById(R.id.admin);
            badgeRecyclerView = itemView.findViewById(R.id.badge_recycler_view);
        }
    }
}
