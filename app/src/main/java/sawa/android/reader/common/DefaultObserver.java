package sawa.android.reader.common;

import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 一个默认的Observer实现，加入弱引用防止内存泄漏
 * Created by mc100 on 2017/4/13.
 */
public class DefaultObserver<C, T> implements Observer<T> {

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

    }

    @Override
    public void onComplete() {

    }

    public C ref() {
        return c.get();
    }
}
