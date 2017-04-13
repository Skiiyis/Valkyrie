package sawa.android.reader.music;

import java.util.ArrayList;
import java.util.List;

import sawa.android.reader.douban.bean.DouBanFMSongListDetail;

/**
 * Created by mc100 on 2017/4/14.
 */

public enum MusicPlayManager {
    MUSIC_PLAY_MANAGER;

    private List<DouBanFMSongListDetail.Song> playList = new ArrayList<>();
    private int currentPlayIndex = -10;
    private Long listId = -1L;
    private Boolean isPlaying = false;

    private void setPlayList(DouBanFMSongListDetail list) {
        List<DouBanFMSongListDetail.Song> songs = list.getSongs();
        if (null == songs || songs.size() == 0) {
            return;
        }
        this.playList.clear();
        this.playList.addAll(songs);
        listId = list.getId();
    }

    public boolean isSameList(Long listId) {
        if (listId == null) {
            return false;
        }
        return this.listId.longValue() == listId.longValue();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isPlaying(int position) {
        return currentPlayIndex == position;
    }

    public boolean isPlaying(DouBanFMSongListDetail.Song song) {
        return song.getSid().equals(playList.get(currentPlayIndex).getSid());
    }

    public boolean isPausing() {
        return !isPlaying && currentPlayIndex != -10;
    }

    public boolean isStopping() {
        return !isPlaying() && isPausing();
    }

    public void play(int index) {
        if (null == playList || playList.size() == 0) {
            return;
        }
        currentPlayIndex = index;
        MusicPlayService.startPlay(this.playList.get(index).getUrl());
        isPlaying = true;
    }

    public void playList(DouBanFMSongListDetail list) {
        playList(list, 0);
    }

    public void playList(DouBanFMSongListDetail list, int position) {
        setPlayList(list);
        play(position);
    }

    public void playNext() {
        if (null == this.playList || this.playList.size() == 0) {
            return;
        }
        int nextPosition = (currentPlayIndex + 1) % playList.size();
        play(nextPosition);
    }

    public void playPrev() {
        if (null == this.playList || this.playList.size() == 0) {
            return;
        }
        int nextPosition = (currentPlayIndex + playList.size() - 1) % playList.size();
        play(nextPosition);
    }

    public void pause() {
        MusicPlayService.startPause();
        isPlaying = false;
    }

    public void replay() {
        MusicPlayService.startReplay();
        isPlaying = true;
    }
}
