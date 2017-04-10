package sawa.android.reader.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
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

    private boolean circular;

    public PlusImageView(Context context) {
        super(context);
    }

    public PlusImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.sawa);
        circular = typedArray.getBoolean(R.styleable.sawa_circular, false);
        typedArray.recycle();
    }

    public PlusImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PlusImageView load(String url, int drawableResId) {
        Picasso.with(Application.get()).load(url).placeholder(drawableResId).error(R.drawable.image_loading_error).into(this);
        return this;
    }

    public PlusImageView load(File file, int drawableResId) {
        Picasso.with(Application.get()).load(file).placeholder(drawableResId).error(R.drawable.image_loading_error).into(this);
        return this;
    }

    public PlusImageView load(int resId, int drawableResId) {
        Picasso.with(Application.get()).load(resId).placeholder(drawableResId).error(R.drawable.image_loading_error).into(this);
        return this;
    }

    public PlusImageView load(Uri uri, int drawableResId) {
        Picasso.with(Application.get()).load(uri).placeholder(drawableResId).error(R.drawable.image_loading_error).into(this);
        return this;
    }

    public PlusImageView load(String url) {
        Picasso.with(Application.get()).load(url).placeholder(R.drawable.image_loading_place_holder).error(R.drawable.image_loading_error).into(this);
        return this;
    }

    public PlusImageView load(File file) {
        Picasso.with(Application.get()).load(file).placeholder(R.drawable.image_loading_place_holder).error(R.drawable.image_loading_error).into(this);
        return this;
    }

    public PlusImageView load(int resId) {
        Picasso.with(Application.get()).load(resId).placeholder(R.drawable.image_loading_place_holder).error(R.drawable.image_loading_error).into(this);
        return this;
    }

    public PlusImageView load(Uri uri) {
        Picasso.with(Application.get()).load(uri).placeholder(R.drawable.image_loading_place_holder).error(R.drawable.image_loading_error).into(this);
        return this;
    }

    public PlusImageView circular() {
        circular = true;
        invalidate();
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (circular) {
            Drawable drawable = getDrawable();
            if (null == drawable) {
                return;
            }
            Bitmap b = getCircleBitmap(drawableToBitmap(drawable));
            final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
            final Rect rectDest = new Rect(0, 0, getWidth(), getHeight());
            Paint paint = new Paint();
            canvas.drawBitmap(b, rectSrc, rectDest, paint);
        } else {
            super.onDraw(canvas);
        }
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        Rect bounds = drawable.getBounds();
        int h = bounds.bottom - bounds.top;
        int w = bounds.right - bounds.left;
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 获取圆形图片方法
     *
     * @param bitmap
     * @return Bitmap
     * @author caizhiming
     */
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        int x = bitmap.getWidth();

        canvas.drawCircle(x / 2, x / 2, x / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
