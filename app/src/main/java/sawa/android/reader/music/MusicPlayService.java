package sawa.android.reader.music;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sawa.android.reader.douban.bean.DouBanFMSongListDetail;
import sawa.android.reader.util.LogUtil;

/**
 * Created by mc100 on 2017/4/14.
 */

public class MusicPlayService extends Service implements IMusicPlay {

    private MediaPlayer player = new MediaPlayer();
    private List<DouBanFMSongListDetail.Song> songList = new ArrayList<>();
    private List<DouBanFMSongListDetail.Song> songList$ = new ArrayList<>();
    private String playMode = MusicPlayMode.LIST_LOOP;
    private String playStatus = MusicPlayStatus.STOP;
    private DouBanFMSongListDetail.Song currentSong;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, Service.START_REDELIVER_INTENT, startId);
    }

    private void play(String url) {
        try {
            player.reset();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(url);
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (!MusicPlayStatus.PLAYING.equals(playStatus)) {
                        mp.start();
                        playStatus = MusicPlayStatus.PLAYING;
                    }
                }
            });

            player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {

                }
            });

            player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            });

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    next();
                }
            });

            player.prepareAsync();
        } catch (IOException e) {
            LogUtil.e(e);
        }
    }

    @Override
    public void play() {
        switch (playStatus) {
            case MusicPlayStatus.STOP:
                player.prepareAsync();
                break;
            case MusicPlayStatus.PAUSE:
                player.start();
                break;
            case MusicPlayStatus.PLAYING:
                break;
        }
    }

    @Override
    public void play(DouBanFMSongListDetail.Song song) {
        add(song);
        this.currentSong = song;
        play(song.getUrl());
    }

    @Override
    public void play(int position) {
        if (position < songList.size()) {
            DouBanFMSongListDetail.Song song = songList.get(position);
            this.currentSong = song;
            play(currentSong.getUrl());
        }
    }

    @Override
    public void pause() {
        player.pause();
        this.playStatus = MusicPlayStatus.PAUSE;
    }

    @Override
    public void stop() {
        player.stop();
        this.playStatus = MusicPlayStatus.STOP;
    }

    @Override
    public void clearPlayList() {
        this.songList.clear();
        this.songList$.clear();
    }

    @Override
    public void add(DouBanFMSongListDetail.Song song) {
        this.songList.add(0, song);
        this.songList$.add(0, song);
    }

    @Override
    public void add(List<DouBanFMSongListDetail.Song> songList) {
        this.songList.addAll(songList);
        this.songList$.addAll(songList);
    }

    @Override
    public void setPlayMode(@MusicPlayMode String playMode) {
        this.playMode = playMode;
        player.setLooping(false);
        songList$.clear();
        songList$.addAll(songList);
        if (MusicPlayMode.LIST_RANDOM.equals(playMode)) {
            Collections.shuffle(songList$);
        } else if (MusicPlayMode.SINGLE_LOOP.equals(playMode)) {
            player.setLooping(true);
        }
    }

    @Override
    public void next() {
        int currentPosition = songList$.indexOf(currentSong);
        currentSong = songList$.get((currentPosition + songList$.size() + 1) % songList$.size());
        play(currentSong.getUrl());
    }

    @Override
    public void prev() {
        int currentPosition = songList$.indexOf(currentSong);
        currentSong = songList$.get((currentPosition + songList$.size() - 1) % songList$.size());
        play(currentSong.getUrl());
    }

    @Override
    public void seek(int percent) {

    }
}
