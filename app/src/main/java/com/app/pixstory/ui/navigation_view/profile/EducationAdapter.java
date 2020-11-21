package com.app.pixstory.ui.navigation_view.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.pixstory.R;
import com.app.pixstory.data.model.api.Education;
import com.app.pixstory.data.model.api.UserProfileResponse;

import java.util.List;

public class EducationAdapter extends BaseAdapter {

    private List<Education> data;
    private int count;

    public EducationAdapter(List<Education> data,int count) {
        this.data = data;
        this.count = count;
    }

    @Override
    public int getCount() {
        return count;
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

        Holder holder = new Holder();
        Education education = getItem(position);
        if (view == null) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_text, null);
            holder.text = view.findViewById(R.id.text);
            view.setTag(holder);

        } else {

            holder = (Holder) view.getTag();
        }

        holder.text.setText(education.getDegree() + " | " + education.getUniversity() + " | " + education.getInstitue());

        return view;
    }

    private class Holder {
        TextView text;
    }
}
