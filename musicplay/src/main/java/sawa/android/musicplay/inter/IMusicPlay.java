package sawa.android.musicplay.inter;

import sawa.android.musicplay.constants.MusicPlayMode;

/**
 * Created by hasee on 2017/5/1.
 */
public interface IMusicPlay<T> extends IPlayPolicy<T>, IListPolicy<T> {
    void setPlayMode(@MusicPlayMode String playMode);
}
