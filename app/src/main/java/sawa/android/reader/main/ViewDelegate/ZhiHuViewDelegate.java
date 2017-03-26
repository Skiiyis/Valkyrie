package sawa.android.reader.main.ViewDelegate;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import sawa.android.reader.R;
import sawa.android.reader.common.BaseViewDelegate;

/**
 * Created by hasee on 2017/3/11.
 */
public class ZhiHuViewDelegate extends BaseViewDelegate {

    public ZhiHuViewDelegate(View rootView) {
        super(rootView);
    }

    public ZhiHuViewDelegate(int layoutResId) {
        super(layoutResId);
    }

    /*public TextView contentTextView() {
        return (TextView) getRootView().findViewById(R.id.tv_content);
    }
*/
    public RecyclerView contentRecycleView() {
        return (RecyclerView) getRootView().findViewById(R.id.rv_container);
    }
}
