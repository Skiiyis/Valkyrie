package sawa.android.musicplay.inter;

import sawa.android.musicplay.constants.MusicPlayStatus;

/**
 * Created by hasee on 2017/5/1.
 */
public interface IPlayPolicy<T> {

    void rePlay();

    void pause();

    void stop();

    void seek(int percent);

    @MusicPlayStatus String status();
}
