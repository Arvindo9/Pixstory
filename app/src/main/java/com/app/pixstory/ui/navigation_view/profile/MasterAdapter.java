package com.app.pixstory.ui.navigation_view.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.app.pixstory.R;

import java.util.List;

public class MasterAdapter extends BaseAdapter {

    private List<String> data;

    public MasterAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Holder holder = new Holder();
        String data = getItem(position);
        if(view == null)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.radio_layout,null);
            holder.rb = view.findViewById(R.id.rb);
            view.setTag(holder);
        }
        else
        {
            holder = (Holder) view.getTag();
        }

        holder.rb.setText(data);
        return view;
    }
    private class Holder
    {
        RadioButton rb;
    }
}
