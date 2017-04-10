package sawa.android.reader.main.view_wrapper;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import sawa.android.reader.R;
import sawa.android.reader.common.BaseViewWrapper;
import sawa.android.reader.global.Application;

/**
 * Created by hasee on 2017/3/11.
 */
public class ViewRecycleViewWrapper extends BaseViewWrapper {

    public ViewRecycleViewWrapper(View rootView) {
        super(rootView);
    }

    public ViewRecycleViewWrapper(int layoutResId) {
        super(View.inflate(Application.get(), layoutResId, null));
    }

    /*public TextView contentTextView() {
        return (TextView) getRootView().findViewById(R.id.tv_content);
    }
*/
    public RecyclerView contentRecycleView() {
        return (RecyclerView) getRootView().findViewById(R.id.rv_container);
    }
}
