package sawa.android.common.global;

import android.content.Context;

/**
 * Created by hasee on 2017/3/8.
 */
public class Application extends android.app.Application {

    public static android.app.Application context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
    }

    public static Context get() {
        return context;
    }
}
