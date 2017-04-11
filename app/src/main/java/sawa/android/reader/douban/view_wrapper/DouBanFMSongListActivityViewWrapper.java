package sawa.android.reader.douban.view_wrapper;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import sawa.android.reader.R;
import sawa.android.reader.common.BaseViewWrapper;
import sawa.android.reader.common.PlusImageView;
import sawa.android.reader.main.view_wrapper.ViewRecycleViewWrapper;

/**
 * Created by mc100 on 2017/4/11.
 */
public class DouBanFMSongListActivityViewWrapper extends BaseViewWrapper {

    ViewRecycleViewWrapper viewRecycleViewWrapper;

    public DouBanFMSongListActivityViewWrapper(View rootView) {
        super(rootView);
    }

    public Toolbar toolbar() {
        return (Toolbar) getRootView().findViewById(R.id.toolbar);
    }

    public CollapsingToolbarLayout toolbarLayout() {
        return (CollapsingToolbarLayout) getRootView().findViewById(R.id.toolbar_layout);
    }

    public PlusImageView coverImageView() {
        return (PlusImageView) getRootView().findViewById(R.id.pv_cover);
    }

    public PlusImageView creatorPictureImageView() {
        return (PlusImageView) getRootView().findViewById(R.id.pv_creator_picture);
    }

    public TextView titleTextView() {
        return (TextView) getRootView().findViewById(R.id.tv_title);
    }

    public TextView creatorNameTextView() {
        return (TextView) getRootView().findViewById(R.id.tv_creator);
    }

    public TextView collectedCountTextView() {
        return (TextView) getRootView().findViewById(R.id.tv_collected_count);
    }

    public CheckBox starCheckBox() {
        return (CheckBox) getRootView().findViewById(R.id.cb_star);
    }

    public CheckBox downloadCheckBox() {
        return (CheckBox) getRootView().findViewById(R.id.cb_download);
    }

    public CheckBox shareCheckBox() {
        return (CheckBox) getRootView().findViewById(R.id.cb_share);
    }

    public RecyclerView containerRecycleView() {
        if (viewRecycleViewWrapper == null) {
            viewRecycleViewWrapper = new ViewRecycleViewWrapper(getRootView());
        }
        return viewRecycleViewWrapper.contentRecycleView();
    }
}
