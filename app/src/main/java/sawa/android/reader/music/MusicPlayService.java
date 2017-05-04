package sawa.android.reader.music;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import sawa.android.musicplay.AbsMusicPlayer;
import sawa.android.musicplay.constants.MusicPlayMode;
import sawa.android.musicplay.inter.IMusicPlay;
import sawa.android.reader.douban.bean.DouBanFMSongListDetail;
import sawa.android.reader.util.BroadcastUtil;
import sawa.android.reader.util.LogUtil;

/**
 * Created by mc100 on 2017/4/14.
 */

public class MusicPlayService extends Service implements IMusicPlay<DouBanFMSongListDetail.Song> {

    public static final String ACTION_REPLAY = "rePlay";
    public static final String ACTION_PAUSE = "pause";
    public static final String ACTION_STOP = "stop";

    public static final String ACTION_NEXT = "next";
    public static final String ACTION_PREV = "prev";
    public static final String ACTION_TO = "to";

    public static final String ACTION_SET_PLAY_MODE = "setPlayMode";

    public static final String ACTION_ADD = "add";
    public static final String ACTION_CLEAR = "clear";
    public static final String ACTION_POSITION = "position";
    public static final String ACTION_LIST = "list";

    public static final String ACTION_SEEK = "seek";
    public static final String ACTION_STATUS = "status";

    private AbsMusicPlayer<DouBanFMSongListDetail.Song> musicPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        musicPlayer = new AbsMusicPlayer<DouBanFMSongListDetail.Song>() {

            @Override
            public String getSource(DouBanFMSongListDetail.Song song) {
                return song.getUrl();
            }
        };
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getStringExtra("action");

        switch (action) {
            case ACTION_REPLAY:
                rePlay();
                BroadcastUtil.global().sendBroadcast(ACTION_REPLAY);
                break;
            case ACTION_PAUSE:
                pause();
                BroadcastUtil.global().sendBroadcast(ACTION_PAUSE);
                break;
            case ACTION_STOP:
                stop();
                BroadcastUtil.global().sendBroadcast(ACTION_STOP);
                break;
            case ACTION_ADD:
                LogUtil.e("add:" + System.currentTimeMillis());
                List<DouBanFMSongListDetail.Song> songs = (List<DouBanFMSongListDetail.Song>) intent.getSerializableExtra("songList");
                if (songs != null && songs.size() != 0) {
                    add(songs);
                    BroadcastUtil.global().sendBroadcast(new Intent(ACTION_ADD));
                }
                break;
            case ACTION_CLEAR:
                LogUtil.e("clear:" + System.currentTimeMillis());
                BroadcastUtil.global().sendBroadcast(ACTION_CLEAR);
                break;
            case ACTION_NEXT:
                DouBanFMSongListDetail.Song next = next();
                BroadcastUtil.global().sendBroadcast(new Intent(ACTION_NEXT).putExtra("song", next));
                break;
            case ACTION_PREV:
                DouBanFMSongListDetail.Song prev = prev();
                BroadcastUtil.global().sendBroadcast(new Intent(ACTION_NEXT).putExtra("song", prev));
                break;
            case ACTION_TO:
                LogUtil.e("to:" + System.currentTimeMillis());
                int toPosition = intent.getIntExtra("to", -1);
                if (toPosition != -1 && toPosition > 0) {
                    DouBanFMSongListDetail.Song to = to(toPosition);
                    BroadcastUtil.global().sendBroadcast(new Intent(ACTION_TO).putExtra("song", to));
                }
                break;
            case ACTION_SEEK:
                int seek = intent.getIntExtra("seek", -1);
                if (seek != -1) {
                    seek(seek);
                }
                BroadcastUtil.global().sendBroadcast(ACTION_SEEK);
                break;
            case ACTION_SET_PLAY_MODE:
                setPlayMode(intent.getStringExtra("playMode"));
                BroadcastUtil.global().sendBroadcast(ACTION_SET_PLAY_MODE);
                break;
            case ACTION_LIST:
                ArrayList<DouBanFMSongListDetail.Song> list = new ArrayList<>();
                list.addAll(list());
                BroadcastUtil.global().sendBroadcast(new Intent(ACTION_LIST).putExtra("list", list));
                break;
            case ACTION_STATUS:
                BroadcastUtil.global().sendBroadcast(new Intent(ACTION_STATUS).putExtra("status", status()));
                break;
            case ACTION_POSITION:
                BroadcastUtil.global().sendBroadcast(new Intent(ACTION_POSITION).putExtra("position", new Integer(position())));
                break;
        }
        return super.onStartCommand(intent, Service.START_REDELIVER_INTENT, startId);
    }

    @Override
    public void setPlayMode(@MusicPlayMode String playMode) {
        musicPlayer.setPlayMode(playMode);
    }

    @Override
    public DouBanFMSongListDetail.Song next() {
        return musicPlayer.next();
    }

    @Override
    public DouBanFMSongListDetail.Song prev() {
        return musicPlayer.prev();
    }

    @Override
    public DouBanFMSongListDetail.Song to(int position) {
        return musicPlayer.to(position);
    }

    @Override
    public void add(DouBanFMSongListDetail.Song song) {
        musicPlayer.add(song);
    }

    @Override
    public void add(List<DouBanFMSongListDetail.Song> t) {
        musicPlayer.add(t);
    }

    @Override
    public void clear() {
        musicPlayer.clear();
    }

    @Override
    public int position() {
        return musicPlayer.position();
    }

    @Override
    public List<DouBanFMSongListDetail.Song> list() {
        return musicPlayer.list();
    }

    @Override
    public void rePlay() {
        musicPlayer.rePlay();
    }

    @Override
    public void pause() {
        musicPlayer.pause();
    }

    @Override
    public void stop() {
        musicPlayer.stop();
    }

    @Override
    public void seek(int percent) {
        musicPlayer.seek(percent);
    }

    @Override
    public String status() {
        return musicPlayer.status();
    }
}
