package com.app.pixstory.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pixstory.R;

import java.util.ArrayList;
import java.util.Objects;

public class GalleryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Uri> mArrayUri;
    private TextView photosSelected;

    public GalleryAdapter(Context context, ArrayList<Uri> mArrayUri, TextView photosSelected) {

        this.context = context;
        this.mArrayUri = mArrayUri;
        this.photosSelected = photosSelected;
    }

    @Override
    public int getCount() {
        return mArrayUri.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayUri.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("ViewHolder") View itemView = Objects.requireNonNull(inflater).inflate(R.layout.gv_item, parent, false);

        ImageView ivGallery = itemView.findViewById(R.id.ivGallery);
        ImageView deleteItem = itemView.findViewById(R.id.deleteItem);

        Uri uri = mArrayUri.get(position);

        ivGallery.setImageURI(uri);

        deleteItem.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                mArrayUri.remove(position);
                notifyDataSetChanged();
                int count = mArrayUri.size();
                photosSelected.setText(count + " " + context.getResources().getString(R.string.photos_selected));
            }
        });

        return itemView;
    }

}
