package sawa.android.reader.music;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import sawa.android.reader.R;
import sawa.android.reader.common.BaseViewWrapper;

/**
 * Created by mc100 on 2017/4/14.
 */

public class MusicPlayView extends BaseViewWrapper {

    public MusicPlayView(View rootView) {
        super(rootView);
    }

    public TextView songNameTextView() {
        return (TextView) rootView().findViewById(R.id.tv_song_name);
    }

    public CheckBox playCheckBox() {
        return (CheckBox) rootView().findViewById(R.id.cb_play);
    }

    public CheckBox playlistCheckBox() {
        return (CheckBox) rootView().findViewById(R.id.cb_playlist);
    }
}
