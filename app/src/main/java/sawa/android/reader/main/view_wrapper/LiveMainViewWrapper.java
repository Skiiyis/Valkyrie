package sawa.android.reader.main.view_wrapper;

import android.view.View;
import android.widget.TextView;

import sawa.android.reader.R;
import sawa.android.reader.common.BaseViewWrapper;
import sawa.android.reader.common.PlusImageView;

/**
 * Created by mc100 on 2017/4/19.
 */
public class LiveMainViewWrapper extends BaseViewWrapper {

    private PlusImageView roomThumbPlusImageView;
    private PlusImageView liverPlusImageView;
    private TextView roomNameTextView;
    private TextView liverTextView;
    private TextView audienceTextView;

    public LiveMainViewWrapper(View rootView) {
        super(rootView);
    }

    public PlusImageView roomThumbPlusImageView() {
        if (roomThumbPlusImageView == null) {
            roomThumbPlusImageView = (PlusImageView) rootView().findViewById(R.id.pv_room_thumb);
        }
        return roomThumbPlusImageView;
    }

    public PlusImageView liverPlusImageView() {
        if (liverPlusImageView == null) {
            liverPlusImageView = (PlusImageView) rootView().findViewById(R.id.pv_liver_header);
        }
        return liverPlusImageView;
    }

    public TextView roomNameTextView(){
        if (roomNameTextView == null) {
            roomNameTextView = (TextView) rootView().findViewById(R.id.tv_room_name);
        }
        return roomNameTextView;
    }

    public TextView liverTextView(){
        if (liverTextView == null) {
            liverTextView = (TextView) rootView().findViewById(R.id.tv_liver);
        }
        return liverTextView;
    }

    public TextView audienceTextView(){
        if (audienceTextView == null) {
            audienceTextView = (TextView) rootView().findViewById(R.id.tv_audience);
        }
        return audienceTextView;
    }
}
