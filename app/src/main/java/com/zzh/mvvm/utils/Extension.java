package com.zzh.mvvm.utils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.zzh.mvvm.R;

public class Extension {
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String url) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(view.getContext()).applyDefaultRequestOptions(options).load(url).transition(
                new DrawableTransitionOptions().crossFade(1000)).into(view);
    }
}
