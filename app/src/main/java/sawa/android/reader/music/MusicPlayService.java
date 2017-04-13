package sawa.android.reader.music;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.IOException;

import sawa.android.reader.global.Application;
import sawa.android.reader.util.LogUtil;

/**
 * Created by mc100 on 2017/4/14.
 */

public class MusicPlayService extends Service {

    private MediaPlayer player = new MediaPlayer();
    private static final String ACTION_PLAY = "play";
    private static final String ACTION_REPLAY = "replay";
    private static final String ACTION_PAUSE = "pause";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        String action = intent.getStringExtra("action");
        if (ACTION_PLAY.equals(action)) {
            if (TextUtils.isEmpty(url)) {
                return super.onStartCommand(intent, flags, startId);
            }
            play(url);
        } else if (ACTION_PAUSE.equals(action)) {
            pause();
        } else if (ACTION_REPLAY.equals(action)) {
            replay();
        }
        return super.onStartCommand(intent, Service.START_REDELIVER_INTENT, startId);
    }

    public static void startPlay(String url) {
        Intent intent = new Intent(Application.get(), MusicPlayService.class);
        intent.putExtra("action", ACTION_PLAY);
        intent.putExtra("url", url);
        Application.get().startService(intent);
    }

    public static void startPause() {
        Intent intent = new Intent(Application.get(), MusicPlayService.class);
        intent.putExtra("action", ACTION_PAUSE);
        Application.get().startService(intent);
    }

    public static void startReplay() {
        Intent intent = new Intent(Application.get(), MusicPlayService.class);
        intent.putExtra("action", ACTION_REPLAY);
        Application.get().startService(intent);
    }

    private void play(String url) {
        try {
            player.reset();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(url);
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    mp.start();
                }
            });
            player.prepareAsync();
        } catch (IOException e) {
            LogUtil.e(e);
        }
    }

    private void replay() {
        player.start();
    }

    private void pause() {
        player.pause();
    }
}
