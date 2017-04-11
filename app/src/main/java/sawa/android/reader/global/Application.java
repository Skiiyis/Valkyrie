package sawa.android.reader.global;

import android.content.Context;

import com.blankj.utilcode.utils.Utils;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by hasee on 2017/3/8.
 */
public class Application extends android.app.Application {

    public static android.app.Application context;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        Utils.init(this);
        this.context = this;
    }

    public static Context get() {
        return context;
    }
}
