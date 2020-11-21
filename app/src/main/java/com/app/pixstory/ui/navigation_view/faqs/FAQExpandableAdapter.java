package com.app.pixstory.ui.navigation_view.faqs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.pixstory.R;
import java.util.HashMap;
import java.util.List;

public class FAQExpandableAdapter extends BaseExpandableListAdapter {

    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;
    private AudioCallback audioCallback;
    private boolean play;

    interface AudioCallback {
        void playAudio(String question);

        void stopAudio();
    }

    public FAQExpandableAdapter(AudioCallback audioCallback, List<String> expandableListTitle, HashMap<String, List<String>> expandableListDetail) {
        this.audioCallback = audioCallback;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_layout, null);
        }
        TextView listTitleTextView = convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public View getChildView(int listPosition, int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_layout, null);
        }
        TextView expandedListTextView = convertView.findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);

        ImageView audio = convertView.findViewById(R.id.audio);

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!play) {
                    audioCallback.playAudio((String) getGroup(listPosition));
                    play = true;
                } else {
                    audioCallback.stopAudio();
                    play = false;
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}