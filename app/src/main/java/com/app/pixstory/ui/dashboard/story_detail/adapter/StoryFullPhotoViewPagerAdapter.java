package com.app.pixstory.ui.dashboard.story_detail.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.app.pixstory.R;
import com.app.pixstory.core.binding.BindingUtils;
import com.app.pixstory.data.model.story.PhotoStory;

import java.util.ArrayList;

public class StoryFullPhotoViewPagerAdapter extends PagerAdapter {
    private ArrayList<PhotoStory> listData;
    private LayoutInflater inflater;
    private Context context;
    private int position_image;
    Activity activity;


    public StoryFullPhotoViewPagerAdapter(Context context, ArrayList<PhotoStory> listData, int position_image, Activity activity) {
        this.context = context;
        this.listData = listData;
        this.position_image = position_image;
        this.activity = activity;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (listData != null) {
            listData = new ArrayList<>();
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View slider_layout = inflater.inflate(R.layout.adapter_story_full_photo_view, view, false);
        assert slider_layout != null;
        ImageView banner = slider_layout.findViewById(R.id.banner);
        ImageView audio = slider_layout.findViewById(R.id.audio);
        TextView title = slider_layout.findViewById(R.id.title);
        TextView caption = slider_layout.findViewById(R.id.caption);
        final ProgressBar progressBar = slider_layout.findViewById(R.id.progressBar);

        BindingUtils.setStringPhotos(banner, listData.get(position).getPath(), progressBar);
        caption.setText(listData.get(position).getCaption());
        if (listData.get(position).getCaption() == null){
            audio.setVisibility(View.GONE);
        } else {
            audio.setVisibility(View.VISIBLE);
        }

        view.addView(slider_layout, 0);

        return slider_layout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    public class CommonItemSpaceDecoration extends RecyclerView.ItemDecoration {
        private int mSpace;

        CommonItemSpaceDecoration(int space) {
            this.mSpace = space;
        }


        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(mSpace, mSpace, mSpace, mSpace);
        }
    }
}


