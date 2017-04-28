package sawa.android.musicplay;

import android.media.AudioManager;
import android.media.MediaPlayer;

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

    protected MediaPlayer player = new MediaPlayer();
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

    @Override
    public void play(T t) {
        try {
            player.reset();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(getSource(t));
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
                    play(listPolicy.next());
                }
            });

            player.prepareAsync();
        } catch (IOException e) {
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

    public abstract String getSource(T t);
}
