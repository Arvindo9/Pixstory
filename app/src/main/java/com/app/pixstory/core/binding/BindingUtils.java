package com.app.pixstory.core.binding;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pixstory.BuildConfig;
import com.app.pixstory.R;
import com.app.pixstory.data.model.db.messages.MessageUsers;
import com.app.pixstory.data.model.db.messages.Messages;
import com.app.pixstory.ui.messages.message.adapter.MessageAdapter;
import com.app.pixstory.ui.messages.message.adapterBulletin.MessageBulletinAdapter;
import com.app.pixstory.ui.messages.messageUser.messageUserAdapter.MessageUserAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.app.pixstory.utils.Constants.BASE_PATH;

public class BindingUtils {
    @BindingAdapter({"ImageUrl", "ProgressBar"})
    public static void setStringPhotos(ImageView imageView, String data, ProgressBar progressBar) {
        Glide.with(imageView)
                .load(BuildConfig.IMAGE_URL + data)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .placeholder(R.drawable.image_not_found)
                .into(imageView);
    }

    @BindingAdapter({"ImageUrl"})
    public static void setCommonImage(ImageView imageView, String data) {
        Glide.with(imageView)
                .load(BuildConfig.IMAGE_URL + data)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }

    @BindingAdapter({"ImageUrlBinding"})
//    public static void setStringPhotos(ImageView imageView, String data, ProgressBar progressBar) {
    public static void setStringPhotos(ImageView imageView, String data) {
        Glide.with(imageView)
                .load(BASE_PATH + data)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
//                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {
//                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .placeholder(R.drawable.image_not_found)
                .into(imageView);
    }



    @BindingAdapter({"MessageAdapter"})
    public static void addMessageAdapter(@NotNull RecyclerView recyclerView, List<Messages> messages) {
        MessageAdapter adapter = (MessageAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.clearItems();
            adapter.addItems(messages);
        }
    }


    @BindingAdapter({"MessageUserAdapter"})
    public static void addMessageAdapterUsers(@NotNull RecyclerView recyclerView, List<MessageUsers> message) {
        MessageUserAdapter adapter = (MessageUserAdapter) recyclerView.getAdapter();
        if (adapter != null && message !=null) {
            adapter.clearItems();
            adapter.addItems(message);
        }
    }

    @BindingAdapter({"MessageBulletinAdapter"})
    public static void addMessageBulletinAdapter(@NotNull RecyclerView recyclerView, List<MessageUsers> messages) {
        MessageBulletinAdapter adapter = (MessageBulletinAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.clearItems();
            adapter.addItems(messages);
        }
    }


}
