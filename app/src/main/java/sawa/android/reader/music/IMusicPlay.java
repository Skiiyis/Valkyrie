package sawa.android.reader.music;

import java.util.List;

import sawa.android.reader.douban.bean.DouBanFMSongListDetail;

/**
 * Created by mc100 on 2017/4/28.
 */

public interface IMusicPlay {

    void play();

    void play(DouBanFMSongListDetail.Song song);

    void play(int position);

    void add(DouBanFMSongListDetail.Song song);

    void pause();

    void stop();

    void clearPlayList();

    void add(List<DouBanFMSongListDetail.Song> songList);

    void setPlayMode(@MusicPlayMode String playMode);

    void next();

    void prev();

    void seek(int percent);
}
