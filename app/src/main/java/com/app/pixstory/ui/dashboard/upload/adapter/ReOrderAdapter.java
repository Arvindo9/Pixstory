package com.app.pixstory.ui.dashboard.upload.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.app.pixstory.R;
import com.app.pixstory.core.binding.BindingUtils;
import com.app.pixstory.data.model.upload.Data;
import com.app.pixstory.utils.drag_drop.StartDragListener;
import com.app.pixstory.utils.drag_drop.ItemMoveCallback;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Kamlesh Yadav on 28-04-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class ReOrderAdapter extends RecyclerView.Adapter<ReOrderAdapter.MyViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    private ArrayList<Data> data;

    private final StartDragListener mStartDragListener;

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        /**
         * @param dataObject type cast to actual model data
         * @param position   destination position
         */
        void onDataSwap(Object dataObject, int position);
    }

    private Listener listener;
    public interface Listener {
        /**
         *
         * @param data original data item of list
         */
        void onAdapterItem(Object data, int position, String banner, int id);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Listener getListener() {
        return listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        View rowView;
        ImageView banner, drag;
        ProgressBar progressBar;


        public MyViewHolder(View itemView) {
            super(itemView);

            rowView = itemView;
            banner = itemView.findViewById(R.id.banner);
            drag = itemView.findViewById(R.id.drag);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public ReOrderAdapter(ArrayList<Data> data, StartDragListener startDragListener) {
        mStartDragListener = startDragListener;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_reorder_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.progressBar.setVisibility(View.GONE);
        BindingUtils.setStringPhotos(holder.banner, data.get(position).getThumbnail());

        holder.drag.setOnTouchListener((v, event) -> {
            if (event.getAction() ==
                    MotionEvent.ACTION_DOWN) {
                mStartDragListener.requestDrag(holder);
            }
            return false;
        });

        holder.banner.setOnClickListener(v -> getListener().onAdapterItem(data,  position, data.get(position).getPath(), data.get(position).getId()));
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(data, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(data, i, i - 1);
            }
        }
         if (fromPosition == 0) {
            callback.onDataSwap(data.get(fromPosition), fromPosition);
        } else {
            callback.onDataSwap(data.get(toPosition), toPosition);
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.GRAY);

    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.WHITE);

    }
}
