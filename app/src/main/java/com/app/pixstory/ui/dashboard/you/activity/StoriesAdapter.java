package com.app.pixstory.ui.dashboard.you.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pixstory.R;
import com.app.pixstory.data.model.PageDetailResponse;
import com.app.pixstory.ui.dashboard.story_detail.StoryDetailPage;

import java.util.List;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.ViewHolder> {

    private List<PageDetailResponse.PageStory> storyList;
    private  ItemCallback itemCallback;
    public interface ItemCallback
    {
        void itemClicked(Bundle bundle);
    }

    public StoriesAdapter(List<PageDetailResponse.PageStory> storyList) {
        this.storyList = storyList;
    }

    public void setListener(ItemCallback itemCallback)
    {
        this.itemCallback = itemCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.stories_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text.setText(storyList.get(position).getTitle());
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putInt("story_id", storyList.get(position).getId());
                itemCallback.itemClicked(bundle);

            }
        });
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }
}
