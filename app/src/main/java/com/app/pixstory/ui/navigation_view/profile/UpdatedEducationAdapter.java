package com.app.pixstory.ui.navigation_view.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.app.pixstory.R;
import com.app.pixstory.data.model.api.Education;
import com.app.pixstory.databinding.NewEducationLayoutBinding;

import java.util.List;

public class UpdatedEducationAdapter extends BaseAdapter {

    private List<Education> data;
    private EducationAction educationAction;

    interface EducationAction {
        void deleteEducation(int position);

        void updateEducation(int position);
    }


    public UpdatedEducationAdapter(EducationAction educationAction, List<Education> data) {
        this.educationAction = educationAction;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Education getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        NewEducationLayoutBinding binding;
        if (view == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.new_education_layout, parent, false);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (NewEducationLayoutBinding) view.getTag();
        }

        binding.setEducation(this.getItem(position));

        binding.remove.setTag(position);
        binding.remove.setOnClickListener(v -> educationAction.deleteEducation((Integer) v.getTag()));

        binding.modify.setTag(position);
        binding.modify.setOnClickListener(v -> educationAction.updateEducation((Integer) v.getTag()));

        return view;
    }
}
