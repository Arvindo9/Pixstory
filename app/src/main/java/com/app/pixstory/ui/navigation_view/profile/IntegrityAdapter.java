package com.app.pixstory.ui.navigation_view.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.pixstory.R;
import com.app.pixstory.data.model.api.Integrity;

import java.util.List;

public class IntegrityAdapter extends BaseAdapter {

    private List<Integrity> integrityList;

    public IntegrityAdapter(List<Integrity> integrityList) {
        this.integrityList = integrityList;
    }

    @Override
    public int getCount() {
        return integrityList.size();
    }

    @Override
    public Integrity getItem(int position) {
        return integrityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Holder holder = new Holder();
        Integrity integrity = getItem(position);

        if(view == null)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_text,null);
            holder.text = view.findViewById(R.id.text);
            view.setTag(holder);
        }
        else
        {
            holder = (Holder) view.getTag();
        }

        holder.text.setText(integrity.getMsg() + " on " + integrity.getAddedOn() + " | " + integrity.getAddedTime());

        return view;
    }

    private class Holder
    {
        TextView text;
    }
}
