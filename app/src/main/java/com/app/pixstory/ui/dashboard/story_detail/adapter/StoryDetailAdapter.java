package com.app.pixstory.ui.dashboard.story_detail.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.app.pixstory.R;
import com.app.pixstory.core.binding.BindingUtils;
import com.app.pixstory.data.model.story.PhotoStory;
import com.app.pixstory.ui.dashboard.story_detail.StoryPhotoFullViewActivity;

import java.util.ArrayList;
import java.util.List;

public class StoryDetailAdapter extends PagerAdapter {
    private List<PhotoStory> listData;
    private LayoutInflater inflater;
    private Context context;
    private int position_image;
    Activity activity;
    private String banners, story_title;

    public StoryDetailAdapter(Context context, List<PhotoStory> listData, int position_image, Activity activity, String story_title) {
        this.context = context;
        this.listData = listData;
        this.position_image = position_image;
        this.activity = activity;
        this.story_title = story_title;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (listData != null) {
            listData = new ArrayList<>();
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return listData.size();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View slider_layout = inflater.inflate(R.layout.adapter_story_slide, view, false);
        assert slider_layout != null;
        ImageView banner = slider_layout.findViewById(R.id.banner);
        TextView caption = slider_layout.findViewById(R.id.caption);
        ImageView full_view = slider_layout.findViewById(R.id.full_view);
        final ProgressBar progressBar = slider_layout.findViewById(R.id.progressBar);


        banners = listData.get(position).getPath();
        BindingUtils.setStringPhotos(banner, listData.get(position).getPath(), progressBar);
        caption.setText(listData.get(position).getCaption() + "...   " + listData.get(position).getCredit());

        full_view.setOnClickListener(v -> {
            Intent intent = new Intent(context, StoryPhotoFullViewActivity.class);
            intent.putParcelableArrayListExtra("info", (ArrayList<? extends Parcelable>) listData);
            intent.putExtra("position", position);
            intent.putExtra("story_title", story_title);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
        view.addView(slider_layout, 0);

        return slider_layout;
    }

    /**
     * Determines whether a page View is associated with a specific key object
     * as returned by {@link #instantiateItem(ViewGroup, int)}. This method is
     * required for a PagerAdapter to function properly.
     *
     * @param view   Page View to check for association with <code>object</code>
     * @param object Object to check for association with <code>view</code>
     * @return true if <code>view</code> is associated with the key object <code>object</code>
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}

