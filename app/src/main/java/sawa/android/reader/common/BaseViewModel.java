package sawa.android.reader.common;

import java.io.Serializable;

/**
 * Created by hasee on 2017/3/11.
 */
public abstract class BaseViewModel<V extends BaseViewWrapper, M extends Serializable> {

    protected final V view;

    public BaseViewModel(V view) {
        this.view = view;
    }

    public abstract void bind(M model);
}
