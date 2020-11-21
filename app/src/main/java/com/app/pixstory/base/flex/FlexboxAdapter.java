package com.app.pixstory.base.flex;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.pixstory.R;

import java.util.List;

public class FlexboxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> data;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;
    private boolean status;
    private FlexCallback callback;
    private float textSize;

    public interface FlexCallback {
        void addInterest();
    }

    public FlexboxAdapter(List<String> data, boolean status,float textSize) {
        this.data = data;
        this.status = status;
        this.textSize = textSize;
    }

    public void setInterestListener(FlexCallback callback) {
        this.callback = callback;
    }

    @Override
    public int getItemViewType(int position) {

        if (status && position == data.size()) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.flex_layout, parent, false);
            return new ViewHolder(itemView);
        } else if (viewType == TYPE_FOOTER) {
            View footerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.flex_footer, parent, false);
            return new FooterViewHolder(footerView);
        } else
            return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            footerHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.addInterest();
                }
            });
        } else {
            ViewHolder itemViewHolder = (ViewHolder) holder;
            itemViewHolder.flexiText.setText(data.get(position));
            itemViewHolder.flexiText.setTextSize(textSize);
        }
    }

    @Override
    public int getItemCount() {
        return status ? data.size() + 1 : data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView flexiText;

        public ViewHolder(View itemView) {
            super(itemView);
            flexiText = itemView.findViewById(R.id.flexi_text);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        ImageView add;

        public FooterViewHolder(View itemView) {
            super(itemView);
            add = itemView.findViewById(R.id.add);
        }
    }

}
