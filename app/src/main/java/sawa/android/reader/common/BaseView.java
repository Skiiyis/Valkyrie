package sawa.android.reader.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import sawa.android.reader.global.Application;

/**
 * Created by hasee on 2017/3/11.
 */
public abstract class BaseView extends FrameLayout implements LifeCycleView {

    public BaseView(Context context) {
        super(context);
        init();
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View contentView = View.inflate(Application.get(), getLayoutId(), this);
        onInflate(contentView);
    }

    public void onInflate(View contentView) {

    }

    protected abstract int getLayoutId();

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onStop() {

    }
}
