package com.app.pixstory.ui.dashboard.you.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.app.pixstory.R;
import com.app.pixstory.base.flex.PhotoFlexboxAdapter;
import com.app.pixstory.core.binding.BindingUtils;
import com.app.pixstory.data.model.upload.Data;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;

/**
 * Created by Kamlesh Yadav on 18-04-2020.
 * Eighteen Pixels India Private Limited Lucknow U.P
 * kamlesh@18pixels.com
 */
public class YouFullPhotoViewPagerAdapter extends PagerAdapter {
    private ArrayList<Data> listData;
    private LayoutInflater inflater;
    private Context context;
    private int position_image;
    Activity activity;

    private DeleteCallback deleteCallback;
    private FavouriteCallback favouriteCallback;
    private EditCallback editCallback;

    public void setDeleteCallback(DeleteCallback deleteCallback) {
        this.deleteCallback = deleteCallback;
    }

    public void setFavouriteCallback(FavouriteCallback favouriteCallback) {
        this.favouriteCallback = favouriteCallback;
    }

    public void setEditCallback(EditCallback editCallback){
        this.editCallback = editCallback;
    }

    public interface DeleteCallback {
        void delete(int photo_id, int position, ArrayList<Data> dataArrayList);

    }
    public interface FavouriteCallback{
        void favourite(int photo_id, int is_fav, int position, ArrayList<Data> dataArrayList);
    }

    public interface EditCallback{
        void edit(ArrayList<Data> dataArrayList, int position);
    }


    public YouFullPhotoViewPagerAdapter(Context context, ArrayList<Data> listData, int position_image, Activity activity) {
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
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }
    @Override
    public int getCount() {
        return listData.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View slider_layout = inflater.inflate(R.layout.adapter_photo_screen, view, false);
        assert slider_layout != null;
        ImageView banner = slider_layout.findViewById(R.id.banner);
        ImageView audio = slider_layout.findViewById(R.id.audio);
        TextView caption = slider_layout.findViewById(R.id.caption);
        LinearLayoutCompat delete = slider_layout.findViewById(R.id.delete);
        ImageView favIcon = slider_layout.findViewById(R.id.fav_icon);
        LinearLayoutCompat favourites = slider_layout.findViewById(R.id.favourites);
        RecyclerView interest = slider_layout.findViewById(R.id.interest);
        TextView interest_txt = slider_layout.findViewById(R.id.interest_txt);
        LinearLayoutCompat edit = slider_layout.findViewById(R.id.edit);
        final ProgressBar progressBar = slider_layout.findViewById(R.id.progressBar);

        if (listData.get(position).getInterest().size() > 0){
            interest_txt.setVisibility(View.VISIBLE);
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
            interest.setLayoutManager(layoutManager);
            PhotoFlexboxAdapter adapter = new PhotoFlexboxAdapter(listData.get(position).getInterest());
            interest.setAdapter(adapter);
        } else {
            interest_txt.setVisibility(View.GONE);
        }


        BindingUtils.setStringPhotos(banner, listData.get(position).getPath(), progressBar);
        caption.setText(listData.get(position).getCaption() + " " + listData.get(position).getCredit());
        if (listData.get(position).getCaption() == null){
            audio.setVisibility(View.GONE);
        } else {
            audio.setVisibility(View.VISIBLE);
        }

        if (listData.get(position).getIsFav() == 0){
            favIcon.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            favIcon.setColorFilter(ContextCompat.getColor(context, R.color.neon), android.graphics.PorterDuff.Mode.SRC_IN);
        }


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCallback.delete(listData.get(position).getId(), position, listData);
            }
        });

        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favouriteCallback.favourite(listData.get(position).getId(), listData.get(position).getIsFav(),  position, listData);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCallback.edit(listData, position);
            }
        });

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



