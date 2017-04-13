package sawa.android.reader.main.view_wrapper;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import sawa.android.reader.R;
import sawa.android.reader.common.BaseViewWrapper;

/**
 * Created by mc100 on 2017/4/11.
 */
public class MainActivityViewWrapper extends BaseViewWrapper {

    public MainActivityViewWrapper(View rootView) {
        super(rootView);
    }

    public ViewPager containerViewPager() {
        return (ViewPager) rootView().findViewById(R.id.vp_main_container);
    }

    public RadioGroup buttonRadioGroup() {
        return (RadioGroup) rootView().findViewById(R.id.rg_bottom);
    }
}
