package sawa.android.musicplay;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sawa.android.musicplay.constants.MusicPlayMode;
import sawa.android.musicplay.constants.MusicPlayStatus;
import sawa.android.musicplay.inter.IListPolicy;
import sawa.android.musicplay.inter.IMusicPlay;

/**
 * Created by hasee on 2017/5/1.
 */
public abstract class AbsMusicPlayer<T> implements IMusicPlay<T> {

    protected MediaPlayer player;
    public String playStatus = MusicPlayStatus.STOP;
    private IListPolicy<T> listPolicy;
    private List<T> t;

    public AbsMusicPlayer() {
        this.t = new ArrayList<>();
        setPlayMode(MusicPlayMode.LIST_LOOP);
    }

    @Override
    public void setPlayMode(@MusicPlayMode String playMode) {
        if (null == listPolicy) {
            this.listPolicy = ListPolicyFactory.create(playMode, t);
        } else {
            this.listPolicy = ListPolicyFactory.create(playMode, listPolicy);
        }
    }

    @Override
    public T next() {
        T t = listPolicy.next();
        play(t);
        return t;
    }

    @Override
    public T prev() {
        T t = listPolicy.prev();
        play(t);
        return t;
    }

    @Override
    public T to(int position) {
        T t = listPolicy.to(position);
        play(t);
        return t;
    }

    @Override
    public void add(T t) {
        listPolicy.add(t);
    }

    @Override
    public void add(List<T> t) {
        listPolicy.add(t);
    }

    @Override
    public void clear() {
        listPolicy.clear();
    }

    @Override
    public int position() {
        return listPolicy.position();
    }

    private void play(T t) {
        playStatus = MusicPlayStatus.STOP;
        try {
            if (player == null) {
                player = new MediaPlayer();
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        playStatus = MusicPlayStatus.PLAYING;
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
                        Log.e("error", what + ":" + extra);
                        player = null;
                        return false;
                    }
                });

                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        play(listPolicy.next());
                    }
                });
            }
            player.reset();
            player.setDataSource(getSource(t));
            player.prepareAsync();
        } catch (IOException e) {
            Log.e("MusicPlayer:play", e.getMessage());
        }
    }

    @Override
    public void rePlay() {
        switch (playStatus) {
            case MusicPlayStatus.PAUSE:
                player.start();
                return;
            case MusicPlayStatus.STOP:
                to(0);
                return;
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
    public void seek(int percent) {
        player.seekTo(percent);
    }

    @Override
    public String status() {
        return playStatus;
    }

    @Override
    public List<T> list() {
        return listPolicy.list();
    }

    public abstract String getSource(T t);
}
