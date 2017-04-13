package sawa.android.reader.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by hasee on 2017/3/8.
 */
public abstract class BaseFragment extends RxFragment {

    protected abstract int layoutResId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(layoutResId(), null);
        container.addView(contentView);
        onInflated(contentView);
        return contentView;
    }

    protected abstract void onInflated(View contentView);
}
