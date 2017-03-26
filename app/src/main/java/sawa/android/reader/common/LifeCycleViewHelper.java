package sawa.android.reader.common;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 管理lifeCycleView的帮助类
 * Created by hasee on 2017/3/11.
 */
public class LifeCycleViewHelper {

    private ArrayList<WeakReference<LifeCycleView>> lifeCycleViews = new ArrayList<>();

    public void addLifeCycleView(LifeCycleView lifeCycleView) {
        lifeCycleViews.add(new WeakReference<>(lifeCycleView));
    }

    public void clear() {
        lifeCycleViews.clear();
    }

    public void onStart() {
        for (WeakReference<LifeCycleView> lifeCycleView : lifeCycleViews) {
            if (lifeCycleView.get() != null) {
                lifeCycleView.get().onStart();
            }
        }
    }

    public void onRestart() {
        for (WeakReference<LifeCycleView> lifeCycleView : lifeCycleViews) {
            if (lifeCycleView.get() != null) {
                lifeCycleView.get().onRestart();
            }
        }
    }

    public void onStop() {
        for (WeakReference<LifeCycleView> lifeCycleView : lifeCycleViews) {
            if (lifeCycleView.get() != null) {
                lifeCycleView.get().onStop();
            }
        }
    }
}
