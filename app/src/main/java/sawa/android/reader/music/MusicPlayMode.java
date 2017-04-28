package sawa.android.reader.music;

import android.support.annotation.StringDef;

import static sawa.android.reader.music.MusicPlayMode.LIST_LOOP;
import static sawa.android.reader.music.MusicPlayMode.LIST_RANDOM;
import static sawa.android.reader.music.MusicPlayMode.SINGLE_LOOP;

/**
 * Created by mc100 on 2017/4/28.
 */

@StringDef({SINGLE_LOOP, LIST_LOOP, LIST_RANDOM})
public @interface MusicPlayMode {
    String SINGLE_LOOP = "SINGLE_LOOP";
    String LIST_LOOP = "LIST_LOOP";
    String LIST_RANDOM = "LIST_RANDOM";
}
