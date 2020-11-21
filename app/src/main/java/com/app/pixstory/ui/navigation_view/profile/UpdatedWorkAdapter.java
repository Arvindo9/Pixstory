package com.app.pixstory.ui.navigation_view.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.databinding.DataBindingUtil;
import com.app.pixstory.R;
import com.app.pixstory.data.model.api.Work;
import com.app.pixstory.databinding.NewWorkLayoutBinding;
import java.util.List;

public class UpdatedWorkAdapter extends BaseAdapter {

    List<Work> data;
    WorkAction workAction;

    interface WorkAction
    {
        void deleteWork(int position);
        void updateWork(int position);
    }

    public UpdatedWorkAdapter(WorkAction workAction,List<Work> data) {
        this.workAction = workAction;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Work getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        NewWorkLayoutBinding binding;
        if (view == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.new_work_layout, parent, false);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (NewWorkLayoutBinding) view.getTag();
        }

        binding.setWork(this.getItem(position));

        binding.delete.setTag(position);
        binding.delete.setOnClickListener(v -> workAction.deleteWork((Integer) v.getTag()));

        binding.update.setTag(position);
        binding.update.setOnClickListener(v -> workAction.updateWork((Integer) v.getTag()));

        return view;
    }
}
