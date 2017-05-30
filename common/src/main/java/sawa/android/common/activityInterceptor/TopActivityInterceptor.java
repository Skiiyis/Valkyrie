package sawa.android.common.activityInterceptor;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by hasee on 2017/5/30.
 */
public class TopActivityInterceptor extends DefaultInterceptor {

    private static Activity topActivity;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        topActivity = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        topActivity = activity;
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (topActivity == activity) {
            topActivity = null;
        }
    }

    public static Activity getTopActivity() {
        return topActivity;
    }
}
