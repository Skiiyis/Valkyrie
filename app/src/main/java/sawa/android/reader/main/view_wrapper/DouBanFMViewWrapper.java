package sawa.android.reader.main.view_wrapper;

import android.view.View;
import android.widget.TextView;

import sawa.android.reader.R;
import sawa.android.reader.common.BaseViewWrapper;
import sawa.android.reader.common.PlusImageView;
import sawa.android.reader.main.view_model.DouBanFMViewModel;

/**
 * Created by hasee on 2017/3/27.
 */
public class DouBanFMViewWrapper extends BaseViewWrapper {

    private DouBanFMViewModel viewModel;
    private PlusImageView coverImageView;
    private PlusImageView statusImageView;
    private TextView textTextView;

    public DouBanFMViewWrapper(View rootView) {
        super(rootView);
    }

    public PlusImageView coverImageView() {
        if (coverImageView == null) {
            coverImageView = (PlusImageView) getRootView().findViewById(R.id.pv_cover);
        }
        return coverImageView;
    }

    public PlusImageView statusImageView() {
        if (statusImageView == null) {
            statusImageView = (PlusImageView) getRootView().findViewById(R.id.pv_status);
        }
        return statusImageView;
    }

    public TextView textTextView() {
        if (textTextView == null) {
            textTextView = (TextView) getRootView().findViewById(R.id.tv_text);
        }
        return textTextView;
    }

    public DouBanFMViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(DouBanFMViewModel viewModel) {
        this.viewModel = viewModel;
    }
}
