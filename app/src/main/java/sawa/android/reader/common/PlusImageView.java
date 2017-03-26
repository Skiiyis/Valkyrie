package sawa.android.reader.common;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import sawa.android.reader.R;
import sawa.android.reader.global.Application;

/**
 * Created by hasee on 2017/3/12.
 */
public class PlusImageView extends ImageView {
    public PlusImageView(Context context) {
        super(context);
    }

    public PlusImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlusImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void load(String url, int drawableResId) {
        Picasso.with(Application.get()).load(url).placeholder(drawableResId).error(R.drawable.image_loading_error).into(this);
    }

    public void load(File file, int drawableResId) {
        Picasso.with(Application.get()).load(file).placeholder(drawableResId).error(R.drawable.image_loading_error).into(this);
    }

    public void load(int resId, int drawableResId) {
        Picasso.with(Application.get()).load(resId).placeholder(drawableResId).error(R.drawable.image_loading_error).into(this);
    }

    public void load(Uri uri, int drawableResId) {
        Picasso.with(Application.get()).load(uri).placeholder(drawableResId).error(R.drawable.image_loading_error).into(this);
    }

    public void load(String url) {
        Picasso.with(Application.get()).load(url).placeholder(R.drawable.image_loading_place_holder).error(R.drawable.image_loading_error).into(this);
    }

    public void load(File file) {
        Picasso.with(Application.get()).load(file).placeholder(R.drawable.image_loading_place_holder).error(R.drawable.image_loading_error).into(this);
    }

    public void load(int resId) {
        Picasso.with(Application.get()).load(resId).placeholder(R.drawable.image_loading_place_holder).error(R.drawable.image_loading_error).into(this);
    }

    public void load(Uri uri) {
        Picasso.with(Application.get()).load(uri).placeholder(R.drawable.image_loading_place_holder).error(R.drawable.image_loading_error).into(this);
    }
}
