package com.app.pixstory.base.flex;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.pixstory.R;
import com.app.pixstory.data.model.add_user_interest.AddUserInterestData;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Kamlesh Yadav on 29-04-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class InterestFlexboxAdapter extends RecyclerView.Adapter<InterestFlexboxAdapter.ViewHolder> {

    private List<AddUserInterestData> data;
    private Listener listener;
    public interface Listener {
        /**
         *
         * @param data original data item of list
         */
        void onAdapterItem(List<AddUserInterestData> data, int position, TextView view);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Listener getListener() {
        return listener;
    }

    public InterestFlexboxAdapter(List<AddUserInterestData> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.base_text_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.baseView.setText(data.get(position).getTitle());

        holder.baseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListener().onAdapterItem(data, position, holder.baseView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView baseView;

        public ViewHolder(View itemView) {
            super(itemView);
            baseView = itemView.findViewById(R.id.baseView);
        }
    }
}

