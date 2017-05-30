package sawa.android.common.global;

import android.content.Context;

import sawa.android.common.activityInterceptor.TopActivityInterceptor;

/**
 * Created by hasee on 2017/3/8.
 */
public class Application extends android.app.Application {

    public static android.app.Application context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        registerActivityLifecycleCallbacks(new TopActivityInterceptor());
    }

    public static Context get() {
        return context;
    }
}
