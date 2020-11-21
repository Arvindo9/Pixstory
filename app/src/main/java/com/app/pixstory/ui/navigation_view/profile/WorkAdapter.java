package com.app.pixstory.ui.navigation_view.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.pixstory.R;
import com.app.pixstory.data.model.api.Work;

import java.util.List;

public class WorkAdapter extends BaseAdapter {

    private List<Work> data;
    private int count;

    public WorkAdapter(List<Work> data,int count) {
        this.data = data;
        this.count = count;
    }

    @Override
    public int getCount() {
        return count;
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

        Holder holder = new Holder();
        Work work = getItem(position);
        if (view == null) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_text, null);
            holder.text = view.findViewById(R.id.text);
            view.setTag(holder);

        } else {

            holder = (Holder) view.getTag();
        }

        holder.text.setText(work.getJobTitle() + " | " + work.getOrganisation());

        return view;
    }

    private class Holder {
        TextView text;
    }
}
