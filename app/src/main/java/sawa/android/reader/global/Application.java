package sawa.android.reader.global;

import android.content.Context;

import com.blankj.utilcode.utils.Utils;

/**
 * Created by hasee on 2017/3/8.
 */
public class Application extends android.app.Application {

    public static android.app.Application context;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        this.context = this;
    }

    public static Context get() {
        return context;
    }
}
