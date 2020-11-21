package com.app.pixstory.ui.dashboard.you.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.app.pixstory.R;
import com.app.pixstory.data.model.PageDetailResponse;
import com.app.pixstory.utils.Constants;
import java.util.Objects;

public class ResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String type;
    private PageDetailResponse.Data pageData;

    public ResultAdapter(String type, PageDetailResponse.Data pageData) {
        this.type = type;
        this.pageData = pageData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (type.equalsIgnoreCase(Constants.STORIES))
            viewHolder = new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.result_layout, null));
        else if (type.equalsIgnoreCase(Constants.MEMBERS))
            viewHolder = new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout, null));
        return Objects.requireNonNull(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder1) {

            ((ViewHolder1) holder).story.setText(pageData.getPage().getPageStory().get(position).getTitle());

        } else if (holder instanceof ViewHolder2) {

            ((ViewHolder2) holder).name.setText(pageData.getPage().getPageMember().get(position).getUsername());
            ((ViewHolder2) holder).integrityScore.setText("Integrity Score : " + pageData.getPage().getPageMember().get(position).getIntegrityScore());

        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (type.equalsIgnoreCase(Constants.STORIES))
            count = pageData.getPage().getPageStory().size();
        else if (type.equalsIgnoreCase(Constants.MEMBERS))
            count = pageData.getPage().getPageMember().size();
        return count;
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView story;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            story = itemView.findViewById(R.id.story);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {
        RecyclerView interest;
        TextView integrityScore, name;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            interest = itemView.findViewById(R.id.interest);
            integrityScore = itemView.findViewById(R.id.integrity_score);
            name = itemView.findViewById(R.id.name);
        }
    }
}
