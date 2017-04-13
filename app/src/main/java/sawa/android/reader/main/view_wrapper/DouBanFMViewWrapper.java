package sawa.android.reader.main.view_wrapper;

import android.view.View;
import android.widget.TextView;

import sawa.android.reader.R;
import sawa.android.reader.common.BaseViewWrapper;
import sawa.android.reader.common.PlusImageView;

/**
 * Created by hasee on 2017/3/27.
 */
public class DouBanFMViewWrapper extends BaseViewWrapper {

    private PlusImageView coverImageView;
    private PlusImageView statusImageView;
    private TextView titleTextView;
    private TextView countTextView;

    public DouBanFMViewWrapper(View rootView) {
        super(rootView);
    }

    public PlusImageView coverImageView() {
        if (coverImageView == null) {
            coverImageView = (PlusImageView) rootView().findViewById(R.id.pv_cover);
        }
        return coverImageView;
    }

    public PlusImageView statusImageView() {
        if (statusImageView == null) {
            statusImageView = (PlusImageView) rootView().findViewById(R.id.pv_status);
        }
        return statusImageView;
    }

    public TextView titleTextView() {
        if (titleTextView == null) {
            titleTextView = (TextView) rootView().findViewById(R.id.tv_text);
        }
        return titleTextView;
    }

    public TextView collectedCountTextView(){
        if (countTextView == null) {
            countTextView = (TextView) rootView().findViewById(R.id.tv_collected_count);
        }
        return countTextView;
    }
}
