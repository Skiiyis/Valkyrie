package sawa.android.musicplay.constants;

import android.support.annotation.StringDef;

import static sawa.android.musicplay.constants.MusicPlayStatus.PAUSE;
import static sawa.android.musicplay.constants.MusicPlayStatus.PLAYING;
import static sawa.android.musicplay.constants.MusicPlayStatus.STOP;

/**
 * Created by mc100 on 2017/4/28.
 */

@StringDef({STOP, PAUSE, PLAYING})
public @interface MusicPlayStatus {
    String STOP = "STOP";
    String PAUSE = "PAUSE";
    String PLAYING = "PLAYING";
}
