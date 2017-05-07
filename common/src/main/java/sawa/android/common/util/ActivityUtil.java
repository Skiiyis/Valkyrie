package sawa.android.common.util;

import android.app.Activity;
import android.view.View;

/**
 * Created by mc100 on 2017/4/28.
 */

public class ActivityUtil {

    public static Activity findActivity(View view) {

        if (view == null) {
            return null;
        } else {
            View targetView;
            for (targetView = view; !(targetView.getContext() instanceof Activity); targetView = (View) targetView.getParent()) {
                if (!(targetView.getParent() instanceof View)) {
                    return null;
                }
            }

            return (Activity) targetView.getContext();
        }
    }
}
