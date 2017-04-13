package sawa.android.reader.douban.view_wrapper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import sawa.android.reader.R;
import sawa.android.reader.common.BaseViewWrapper;
import sawa.android.reader.common.PlusImageView;

/**
 * Created by mc100 on 2017/4/12.
 */

public class DouBanFMSongListDetailItemViewWrapper extends BaseViewWrapper {

    private TextView songNameTextView;
    private TextView singerNameTextView;
    private PlusImageView coverImageView;
    private ImageView moreImageView;
    private ImageView waveImageView;

    public DouBanFMSongListDetailItemViewWrapper(View rootView) {
        super(rootView);
    }

    public TextView songNameTextView() {
        if (songNameTextView == null) {
            songNameTextView = (TextView) rootView().findViewById(R.id.tv_song_name);
        }
        return songNameTextView;
    }

    public TextView singerNameTextView() {
        if (singerNameTextView == null) {
            singerNameTextView = (TextView) rootView().findViewById(R.id.tv_singer_name);
        }
        return singerNameTextView;
    }

    public PlusImageView coverImageView() {
        if (coverImageView == null) {
            coverImageView = (PlusImageView) rootView().findViewById(R.id.pv_cover);
        }
        return coverImageView;
    }

    public ImageView moreImageView() {
        if (moreImageView == null) {
            moreImageView = (ImageView) rootView().findViewById(R.id.iv_more);
        }
        return moreImageView;
    }

    public ImageView waveImageView() {
        if (waveImageView == null) {
            waveImageView = (ImageView) rootView().findViewById(R.id.iv_wave);
        }
        return waveImageView;
    }

}
