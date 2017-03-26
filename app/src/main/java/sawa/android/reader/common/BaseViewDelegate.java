package sawa.android.reader.common;

import android.view.View;

import sawa.android.reader.global.Application;

/**
 * Created by hasee on 2017/3/11.
 */
public abstract class BaseViewDelegate {

    private View rootView;

    public BaseViewDelegate(View rootView) {
        this.rootView = rootView;
    }

    public BaseViewDelegate(int layoutResId) {
        this.rootView = View.inflate(Application.get(), layoutResId, null);
    }

    public View getRootView() {
        return rootView;
    }
}
