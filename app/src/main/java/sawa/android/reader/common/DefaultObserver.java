package sawa.android.reader.common;

import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.ToastUtils;

import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 一个默认的Observer实现，加入弱引用防止内存泄漏
 * Created by mc100 on 2017/4/13.
 */
public abstract class DefaultObserver<C, T> implements Observer<T> {

    private final WeakReference<C> c;

    public DefaultObserver(C c) {
        this.c = new WeakReference<>(c);
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T value) {

    }

    @Override
    public void onError(Throwable e) {
        if (!NetworkUtils.isConnected() || !NetworkUtils.isAvailableByPing()) {
            ToastUtils.showShortToast("网络连接不可用，请检查您的网络设置");
        }
    }

    @Override
    public void onComplete() {

    }

    public C ref() {
        return c.get();
    }
}
