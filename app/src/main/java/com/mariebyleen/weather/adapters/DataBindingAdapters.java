package com.mariebyleen.weather.adapters;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class DataBindingAdapters {

    @BindingAdapter("app:srcCompat")
    public static void setImageResource(ImageView imageView, int resource){
        imageView.setImageResource(resource);
    }

    @BindingAdapter("app:srcCompat")
    public static void setImageResource(ImageView imageView, String iconCode) {
        Context context = imageView.getContext();
        String url = "http://openweathermap.org/img/w/" + iconCode + ".png";
        Glide.with(context).load(url).into(imageView);
    }
}