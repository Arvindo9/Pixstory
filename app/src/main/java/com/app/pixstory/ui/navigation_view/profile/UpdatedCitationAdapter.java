package com.app.pixstory.ui.navigation_view.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.app.pixstory.R;
import com.app.pixstory.data.model.api.Citation;
import com.app.pixstory.databinding.NewCitationLayoutBinding;

import java.util.List;

public class UpdatedCitationAdapter extends BaseAdapter {

    List<Citation> data;
    CitationAction citationAction;

    interface CitationAction {
        void deleteCitation(int position);

        void updateCitation(int position);

        void linkClicked(int position);
    }

    public UpdatedCitationAdapter(CitationAction citationAction, List<Citation> data) {
        this.citationAction = citationAction;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Citation getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        NewCitationLayoutBinding binding;
        if (view == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.new_citation_layout, parent, false);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (NewCitationLayoutBinding) view.getTag();
        }

        binding.setCitation(this.getItem(position));

        binding.link.setTextColor(parent.getContext().getResources().getColor(R.color.blue));

        binding.remove.setTag(position);
        binding.remove.setOnClickListener(v -> citationAction.deleteCitation((Integer) v.getTag()));

        binding.modify.setTag(position);
        binding.modify.setOnClickListener(v -> citationAction.updateCitation((Integer) v.getTag()));

        binding.link.setTag(position);
        binding.link.setOnClickListener(v -> citationAction.linkClicked((Integer) v.getTag()));

        return view;
    }
}
