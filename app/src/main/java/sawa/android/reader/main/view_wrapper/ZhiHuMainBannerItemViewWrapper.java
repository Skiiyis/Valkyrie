package sawa.android.reader.main.view_wrapper;

import android.view.View;
import android.widget.TextView;

import sawa.android.reader.R;
import sawa.android.reader.common.BaseViewWrapper;
import sawa.android.reader.common.PlusImageView;

/**
 * Created by mc100 on 2017/4/13.
 */
public class ZhiHuMainBannerItemViewWrapper extends BaseViewWrapper {

    public ZhiHuMainBannerItemViewWrapper(View rootView) {
        super(rootView);
    }

    public TextView titleTextView() {
        return (TextView) rootView().findViewById(R.id.tv_title);
    }

    public PlusImageView thumbnailPlusImageView() {
        return (PlusImageView) rootView().findViewById(R.id.iv_thumbnail);
    }
}
