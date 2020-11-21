package com.app.pixstory.ui.navigation_view.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.pixstory.R;
import com.app.pixstory.data.model.api.Citation;

import java.util.List;

public class CitationAdapter extends BaseAdapter {

    private List<Citation> data;
    private CitationAction citationAction;
    int count;

    interface CitationAction {
        void linkClicked(int position);
    }

    public CitationAdapter(CitationAction citationAction,List<Citation> data,int count) {
        this.data = data;
        this.citationAction = citationAction;
        this.count = count;
    }

    @Override
    public int getCount() {
        return count;
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

        Holder holder = new Holder();
        Citation citation = getItem(position);
        if (view == null) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.citation_text, null);
            holder.link = view.findViewById(R.id.link);
            view.setTag(holder);

        } else {

            holder = (Holder) view.getTag();
        }

        holder.link.setText(citation.getDescription());
        holder.link.setTextColor(parent.getContext().getResources().getColor(R.color.blue));

        holder.link.setTag(position);
        holder.link.setOnClickListener(v -> citationAction.linkClicked((Integer) v.getTag()));

        return view;
    }

    private class Holder {
        TextView link;
    }
}
