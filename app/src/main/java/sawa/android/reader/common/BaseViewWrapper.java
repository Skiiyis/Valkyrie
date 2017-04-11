package sawa.android.reader.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by hasee on 2017/3/11.
 */
public abstract class BaseViewWrapper extends RecyclerView.ViewHolder {

    public BaseViewWrapper(View rootView) {
        super(rootView);
    }

    public View getRootView() {
        return itemView;
    }
}
