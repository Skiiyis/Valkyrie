package sawa.android.reader.music;

import android.view.View;
import android.widget.CompoundButton;

import sawa.android.reader.R;
import sawa.android.reader.common.BaseFragment;

/**
 * Created by mc100 on 2017/4/14.
 */
public class MusicPlayFragment extends BaseFragment {

    private MusicPlayView musicPlayView;

    @Override
    protected int layoutResId() {
        return R.layout.view_music_play;
    }

    @Override
    protected void onInflated(View contentView) {
        musicPlayView = new MusicPlayView(contentView);
        musicPlayView.playCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MusicPlayManager.MUSIC_PLAY_MANAGER.replay();
                } else {
                    MusicPlayManager.MUSIC_PLAY_MANAGER.pause();
                }
            }
        });

        musicPlayView.playlistCheckBox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查看播放列表
            }
        });
    }
}
