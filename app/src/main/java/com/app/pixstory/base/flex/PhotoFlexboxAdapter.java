package com.app.pixstory.base.flex;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.pixstory.R;
import com.app.pixstory.data.model.upload.Interest;

import java.util.List;

/**
 * Created by Kamlesh Yadav on 19-04-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class PhotoFlexboxAdapter extends RecyclerView.Adapter<PhotoFlexboxAdapter.ViewHolder> {

    private List<Interest> data;

    public PhotoFlexboxAdapter(List<Interest> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flex_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.flexiText.setText(data.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView flexiText;

        public ViewHolder(View itemView) {
            super(itemView);
            flexiText = itemView.findViewById(R.id.flexi_text);
        }
    }
}

