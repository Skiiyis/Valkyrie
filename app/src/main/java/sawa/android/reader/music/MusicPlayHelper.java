package sawa.android.reader.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import sawa.android.musicplay.constants.MusicPlayMode;
import sawa.android.reader.global.Application;
import sawa.android.common.util.BroadcastUtil;
import sawa.android.common.util.LogUtil;

/**
 * Created by hasee on 2017/5/6.
 */
public class MusicPlayHelper {

    private LinkedList<MusicPlayOpElement> opElements = new LinkedList<>();

    public MusicPlayHelper status() {

        final MusicPlayOpElement nextElement = new MusicPlayOpElement(this, MusicPlayService.ACTION_SEEK) {
            @Override
            public void operation() {
                Application.get().startService(get(MusicPlayService.ACTION_STATUS));
            }
        };

        opElements.addLast(nextElement);
        return this;
    }

    public MusicPlayHelper seek(final int percent) {

        final MusicPlayOpElement nextElement = new MusicPlayOpElement(this, MusicPlayService.ACTION_SEEK) {
            @Override
            public void operation() {
                Application.get().startService(get(MusicPlayService.ACTION_SEEK).putExtra("seek", percent));
            }
        };

        opElements.addLast(nextElement);
        return this;
    }

    public MusicPlayHelper stop() {

        final MusicPlayOpElement nextElement = new MusicPlayOpElement(this, MusicPlayService.ACTION_STOP) {
            @Override
            public void operation() {
                Application.get().startService(get(MusicPlayService.ACTION_STOP));
            }
        };

        opElements.addLast(nextElement);
        return this;
    }

    public MusicPlayHelper pause() {

        final MusicPlayOpElement nextElement = new MusicPlayOpElement(this, MusicPlayService.ACTION_PAUSE) {
            @Override
            public void operation() {
                Application.get().startService(get(MusicPlayService.ACTION_PAUSE));
            }
        };

        opElements.addLast(nextElement);
        return this;
    }

    public MusicPlayHelper rePlay() {

        final MusicPlayOpElement nextElement = new MusicPlayOpElement(this, MusicPlayService.ACTION_REPLAY) {
            @Override
            public void operation() {
                Application.get().startService(get(MusicPlayService.ACTION_REPLAY));
            }
        };

        opElements.addLast(nextElement);
        return this;
    }

    public MusicPlayHelper list() {
        final MusicPlayOpElement nextElement = new MusicPlayOpElement(this, MusicPlayService.ACTION_LIST) {
            @Override
            public void operation() {
                Application.get().startService(get(MusicPlayService.ACTION_LIST));
            }
        };

        opElements.addLast(nextElement);
        return this;
    }

    public MusicPlayHelper position() {

        final MusicPlayOpElement nextElement = new MusicPlayOpElement(this, MusicPlayService.ACTION_POSITION) {
            @Override
            public void operation() {
                Application.get().startService(get(MusicPlayService.ACTION_POSITION));
            }
        };

        opElements.addLast(nextElement);
        return this;
    }

    public MusicPlayHelper clear() {
        final MusicPlayOpElement nextElement = new MusicPlayOpElement(this, MusicPlayService.ACTION_CLEAR) {
            @Override
            public void operation() {
                Application.get().startService(get(MusicPlayService.ACTION_CLEAR));
            }
        };

        opElements.addLast(nextElement);
        return this;
    }

    public MusicPlayHelper to(final int position) {
        final MusicPlayOpElement nextElement = new MusicPlayOpElement(this, MusicPlayService.ACTION_TO) {
            @Override
            public void operation() {
                Application.get().startService(get(MusicPlayService.ACTION_TO).putExtra("to", position));
            }
        };

        opElements.addLast(nextElement);
        return this;
    }

    public MusicPlayHelper prev() {
        final MusicPlayOpElement nextElement = new MusicPlayOpElement(this, MusicPlayService.ACTION_PREV) {
            @Override
            public void operation() {
                Application.get().startService(get(MusicPlayService.ACTION_PREV));
            }
        };

        opElements.addLast(nextElement);
        return this;
    }

    public MusicPlayHelper next() {
        final MusicPlayOpElement nextElement = new MusicPlayOpElement(this, MusicPlayService.ACTION_NEXT) {
            @Override
            public void operation() {
                Application.get().startService(get(MusicPlayService.ACTION_NEXT));
            }
        };

        opElements.addLast(nextElement);
        return this;
    }

    public MusicPlayHelper setPlayMode(@MusicPlayMode final String playMode) {

        final MusicPlayOpElement nextElement = new MusicPlayOpElement(this, MusicPlayService.ACTION_SET_PLAY_MODE) {
            @Override
            public void operation() {
                Intent intent = get(MusicPlayService.ACTION_SET_PLAY_MODE).putExtra("playMode", playMode);
                Application.get().startService(intent);
            }
        };
        opElements.addLast(nextElement);
        return this;
    }

    public <T> MusicPlayHelper add(final List<T> t) {

        final MusicPlayOpElement nextElement = new MusicPlayOpElement(this, MusicPlayService.ACTION_ADD) {
            @Override
            public void operation() {
                ArrayList<T> newList = new ArrayList<>();
                newList.addAll(t);
                Application.get().startService(get(MusicPlayService.ACTION_ADD).putExtra("songList", newList));
            }
        };
        opElements.addLast(nextElement);
        return this;
    }

    public <T> MusicPlayHelper add(final T t) {

        final MusicPlayOpElement nextElement = new MusicPlayOpElement(this, MusicPlayService.ACTION_ADD) {
            @Override
            public void operation() {
                ArrayList<T> newList = new ArrayList<>();
                newList.add(t);
                Application.get().startService(get(MusicPlayService.ACTION_ADD).putExtra("songList", newList));
            }
        };
        opElements.addLast(nextElement);
        return this;
    }

    /**
     * 从第一个节点开始执行， operation > receiver，一直执行到最后一个receiver，由最后一个节点执行完触发清除任务链的逻辑
     *
     * @param receiver
     */
    public void submit(BroadcastReceiver receiver) {
        if (opElements.size() == 0) {
            throw new RuntimeException("找不到任何的操作节点");
        }
        /**
         * 替换最后一个任务节点的receiver
         */
        MusicPlayOpElement last = opElements.getLast();
        last.receiver = new ProxyBroadcastReceiver(null, last.receiver, MusicPlayHelper.this);

        /**
         * 注册所有的节点并从第一个开始执行
         */
        Iterator<MusicPlayOpElement> iterator = opElements.iterator();
        while (iterator.hasNext()) {
            MusicPlayOpElement element = iterator.next();
            if (TextUtils.isEmpty(element.action) || element.receiver == null) {
                throw new RuntimeException("错误的操作节点");
            }
            BroadcastUtil.global().receiveOnce(element.receiver, element.action);
        }
        opElements.get(0).operation();
    }

    /**
     * 与submit方式类似，最后一个节点替换成observe的，这里会发送每次的操作intent
     *
     * @return
     */
    public Observable<Intent> observe() {
        return Observable
                .create(new ObservableOnSubscribe<Intent>() {
                    @Override
                    public void subscribe(ObservableEmitter<Intent> e) throws Exception {
                        if (opElements.size() == 0) {
                            throw new RuntimeException("找不到任何的操作节点");
                        }
                        Iterator<MusicPlayOpElement> iterator = opElements.iterator();
                        while (iterator.hasNext()) {
                            MusicPlayOpElement element = iterator.next();
                            if (TextUtils.isEmpty(element.action) || element.receiver == null) {
                                throw new RuntimeException("错误的操作节点");
                            }
                            BroadcastUtil.global().receiveOnce(new ProxyBroadcastReceiver(e, element.receiver, iterator.hasNext() ? null : MusicPlayHelper.this), element.action);
                        }
                        opElements.get(0).operation();
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    private static Intent get(String action) {
        Intent intent = new Intent(Application.get(), MusicPlayService.class);
        intent.putExtra("action", action);
        return intent;
    }

    /**
     * 在正确的时间调用，防止内存泄漏
     */
    public void unregister() {
        if (opElements.size() != 0) {
            Iterator<MusicPlayOpElement> iterator = opElements.iterator();
            while (iterator.hasNext()) {
                BroadcastUtil.global().unregister(iterator.next().receiver);
            }
            opElements.clear();
        }
    }

    /**
     * 调用链上最后一个接受者，1.接收后自动注销 2.接受后完成所有操作任务并取消任务链 3.兼容了回调和observe两种模式
     */
    public static class ProxyBroadcastReceiver extends BroadcastReceiver {

        private WeakReference<MusicPlayHelper> helper;
        private WeakReference<ObservableEmitter<Intent>> e;
        private BroadcastReceiver receiver;

        public ProxyBroadcastReceiver(ObservableEmitter<Intent> e, BroadcastReceiver receiver, MusicPlayHelper helper) {
            this.e = new WeakReference<>(e);
            this.helper = new WeakReference<>(helper);
            this.receiver = receiver;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            this.receiver.onReceive(context, intent);
            ObservableEmitter<Intent> emitter = e.get();

            if (emitter != null && intent != null) {
                emitter.onNext(intent);
            }
            if (helper.get() != null) {
                helper.get().opElements.clear();
            }
        }
    }

    /**
     * 执行任务的链条环节
     */
    public static abstract class MusicPlayOpElement {

        private BroadcastReceiver receiver;
        private String action;
        private WeakReference<MusicPlayHelper> helper;

        public MusicPlayOpElement(final MusicPlayHelper helper, String action) {
            this.action = action;
            this.helper = new WeakReference<>(helper);
            this.receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    LogUtil.e(intent.getAction());
                    MusicPlayHelper playHelper = MusicPlayOpElement.this.helper.get();
                    if (playHelper != null) {
                        int index = playHelper.opElements.indexOf(MusicPlayOpElement.this);
                        if (index < playHelper.opElements.size() - 1) {
                            playHelper.opElements.get(index + 1).operation();
                        }
                    }
                }
            };
        }

        public abstract void operation();
    }
}
