package sawa.android.reader.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import sawa.android.musicplay.constants.MusicPlayMode;
import sawa.android.reader.douban.bean.DouBanFMSongListDetail;
import sawa.android.reader.global.Application;
import sawa.android.common.util.BroadcastUtil;
import sawa.android.common.util.LogUtil;

/**
 * Created by mc100 on 2017/5/3.
 */

public class MusicPlayManager {

    public static Observable<Boolean> setPlayMode(@MusicPlayMode final String playMode) {
        return Observable.
                create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                        BroadcastUtil.global().receiveOnce(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                e.onNext(true);
                            }
                        }, MusicPlayService.ACTION_SET_PLAY_MODE);
                        Intent intent = get(MusicPlayService.ACTION_SET_PLAY_MODE).putExtra("playMode", playMode);
                        Application.get().startService(intent);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static Observable<DouBanFMSongListDetail.Song> next() {
        return Observable
                .create(new ObservableOnSubscribe<DouBanFMSongListDetail.Song>() {
                    @Override
                    public void subscribe(final ObservableEmitter<DouBanFMSongListDetail.Song> e) throws Exception {
                        BroadcastUtil.global().receiveOnce(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                DouBanFMSongListDetail.Song song = (DouBanFMSongListDetail.Song) intent.getSerializableExtra("song");
                                if (song != null) {
                                    e.onNext(song);
                                } else {
                                    e.onError(new NullPointerException("no next song"));
                                }
                            }
                        }, MusicPlayService.ACTION_NEXT);
                        Application.get().startService(get(MusicPlayService.ACTION_NEXT));
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static Observable<DouBanFMSongListDetail.Song> prev() {
        return Observable
                .create(new ObservableOnSubscribe<DouBanFMSongListDetail.Song>() {
                    @Override
                    public void subscribe(final ObservableEmitter<DouBanFMSongListDetail.Song> e) throws Exception {
                        BroadcastUtil.global().receiveOnce(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                DouBanFMSongListDetail.Song song = (DouBanFMSongListDetail.Song) intent.getSerializableExtra("song");
                                if (song != null) {
                                    e.onNext(song);
                                } else {
                                    e.onError(new NullPointerException("no prev song"));
                                }
                            }
                        }, MusicPlayService.ACTION_PREV);
                        Application.get().startService(get(MusicPlayService.ACTION_PREV));
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static Observable<DouBanFMSongListDetail.Song> to(final int position) {
        return Observable
                .create(new ObservableOnSubscribe<DouBanFMSongListDetail.Song>() {
                    @Override
                    public void subscribe(final ObservableEmitter<DouBanFMSongListDetail.Song> e) throws Exception {
                        BroadcastUtil.global().receiveOnce(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                DouBanFMSongListDetail.Song song = (DouBanFMSongListDetail.Song) intent.getSerializableExtra("song");
                                if (song != null) {
                                    e.onNext(song);
                                    LogUtil.e("receiverTo(" + System.currentTimeMillis() + "):" + song.getTitle());
                                } else {
                                    e.onError(new NullPointerException("no position song"));
                                }
                            }
                        }, MusicPlayService.ACTION_TO);
                        LogUtil.e("to(" + System.currentTimeMillis() + ")");
                        Application.get().startService(get(MusicPlayService.ACTION_TO).putExtra("to", position));
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static Observable<Boolean> add(final DouBanFMSongListDetail.Song song) {
        return Observable.
                create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                        BroadcastUtil.global().receiveOnce(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                e.onNext(true);
                            }
                        }, MusicPlayService.ACTION_ADD);

                        ArrayList<DouBanFMSongListDetail.Song> songList = new ArrayList<>();
                        songList.add(song);
                        Application.get().startService(get(MusicPlayService.ACTION_ADD).putExtra("songList", songList));
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static Observable<Boolean> add(final List<DouBanFMSongListDetail.Song> t) {

        return Observable.
                create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                        BroadcastUtil.global().receiveOnce(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                e.onNext(true);
                                LogUtil.e("receiverAdd(" + System.currentTimeMillis() + "):true");
                            }
                        }, MusicPlayService.ACTION_ADD);

                        ArrayList<DouBanFMSongListDetail.Song> songList = new ArrayList<>();
                        songList.addAll(t);
                        LogUtil.e("add(" + System.currentTimeMillis() + ")");
                        Application.get().startService(get(MusicPlayService.ACTION_ADD).putExtra("songList", songList));
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static Observable<Boolean> clear() {
        return Observable.
                create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                        BroadcastUtil.global().receiveOnce(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                e.onNext(true);
                                LogUtil.e("receiverClear(" + System.currentTimeMillis() + "):true");
                            }
                        }, MusicPlayService.ACTION_CLEAR);
                        LogUtil.e("clear(" + System.currentTimeMillis() + ")");
                        Application.get().startService(get(MusicPlayService.ACTION_CLEAR));
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static Observable<Integer> position() {
        return Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(final ObservableEmitter<Integer> e) throws Exception {
                        BroadcastUtil.global().receiveOnce(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                Integer position = (Integer) intent.getSerializableExtra("position");
                                if (position != null) {
                                    e.onNext(position);
                                } else {
                                    e.onError(new NullPointerException("unknown current position"));
                                }
                            }
                        }, MusicPlayService.ACTION_POSITION);
                        Application.get().startService(get(MusicPlayService.ACTION_POSITION));
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static Observable<List<DouBanFMSongListDetail.Song>> list() {
        return Observable
                .create(new ObservableOnSubscribe<List<DouBanFMSongListDetail.Song>>() {
                    @Override
                    public void subscribe(final ObservableEmitter<List<DouBanFMSongListDetail.Song>> e) throws Exception {
                        BroadcastUtil.global().receiveOnce(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                List<DouBanFMSongListDetail.Song> list = (List<DouBanFMSongListDetail.Song>) intent.getSerializableExtra("list");
                                if (list != null) {
                                    e.onNext(list);
                                } else {
                                    e.onError(new NullPointerException("no list song"));
                                }
                            }
                        }, MusicPlayService.ACTION_LIST);
                        Application.get().startService(get(MusicPlayService.ACTION_LIST));
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static Observable<Boolean> rePlay() {
        return Observable
                .create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                        BroadcastUtil.global().receiveOnce(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                e.onNext(true);
                            }
                        }, MusicPlayService.ACTION_REPLAY);
                        Application.get().startService(get(MusicPlayService.ACTION_REPLAY));
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static Observable<Boolean> pause() {
        return Observable
                .create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                        BroadcastUtil.global().receiveOnce(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                e.onNext(true);
                            }
                        }, MusicPlayService.ACTION_PAUSE);
                        Application.get().startService(get(MusicPlayService.ACTION_PAUSE));
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static Observable<Boolean> stop() {
        return Observable
                .create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                        BroadcastUtil.global().receiveOnce(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                e.onNext(true);
                            }
                        }, MusicPlayService.ACTION_STOP);
                        Application.get().startService(get(MusicPlayService.ACTION_STOP));
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static Observable<Boolean> seek(final int percent) {
        return Observable
                .create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(final ObservableEmitter<Boolean> e) throws Exception {
                        BroadcastUtil.global().receiveOnce(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                e.onNext(true);
                            }
                        }, MusicPlayService.ACTION_SEEK);
                        Application.get().startService(get(MusicPlayService.ACTION_SEEK).putExtra("seek", percent));
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static Observable<String> status() {
        return Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(final ObservableEmitter<String> e) throws Exception {
                        BroadcastUtil.global().receiveOnce(new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                String status = intent.getStringExtra("status");
                                if (!TextUtils.isEmpty(status)) {
                                    e.onNext(status);
                                } else {
                                    e.onError(new NullPointerException("no status error"));
                                }
                            }
                        }, MusicPlayService.ACTION_STATUS);
                        Application.get().startService(get(MusicPlayService.ACTION_STATUS));
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    private static Intent get(String action) {
        Intent intent = new Intent(Application.get(), MusicPlayService.class);
        intent.putExtra("action", action);
        return intent;
    }
}
