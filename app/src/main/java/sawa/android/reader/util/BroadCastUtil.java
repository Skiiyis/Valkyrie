package sawa.android.reader.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sawa.android.reader.global.Application;

/**
 * Created by mc100 on 2017/5/3.
 */

public class BroadcastUtil {

    public static BroadCastReceiverOperator local() {
        return new LocalBroadCastReceiverOperator();
    }

    public static BroadCastReceiverOperator global() {
        return new GlobalBroadCastReceiverOperator();
    }

    /**
     * 跨进程广播
     */
    public static class GlobalBroadCastReceiverOperator implements BroadCastReceiverOperator {

        @Override
        public void register(BroadcastReceiver receiver, String... action) {
            IntentFilter filter = new IntentFilter();
            for (int i = 0; i < action.length; i++) {
                filter.addAction(action[i]);
            }
            Application.get().registerReceiver(receiver, filter);
        }

        @Override
        public void register(BroadcastReceiver receiver, IntentFilter filter) {
            Application.get().registerReceiver(receiver, filter);
        }

        @Override
        public void unregister(BroadcastReceiver receiver) {
            Application.get().unregisterReceiver(receiver);
        }

        @Override
        public void unregister(String action) throws Exception {
            throw new Exception("unSupport unregister!");
        }

        @Override
        public void sendBroadcast(Intent intent) {
            Application.get().sendBroadcast(intent);
        }

        @Override
        public void sendBroadcast(String action) {
            Application.get().sendBroadcast(new Intent(action));
        }

        @Override
        public void receiveOnce(BroadcastReceiver receiver, String... action) {
            register(new OnceBroadcastReceiver(receiver, this), action);
        }
    }

    /**
     * 进程内广播
     */
    public static class LocalBroadCastReceiverOperator implements BroadCastReceiverOperator {

        @Override
        public void register(BroadcastReceiver receiver, String... action) {
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(Application.get());
            IntentFilter filter = new IntentFilter();
            for (int i = 0; i < action.length; i++) {
                filter.addAction(action[i]);
            }
            manager.registerReceiver(receiver, filter);
        }

        @Override
        public void register(BroadcastReceiver receiver, IntentFilter filter) {
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(Application.get());
            manager.registerReceiver(receiver, filter);
        }

        @Override
        public void unregister(BroadcastReceiver receiver) {
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(Application.get());
            manager.unregisterReceiver(receiver);
        }


        @Override
        public void unregister(String action) throws Exception {
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(Application.get());
            Field mReceivers = LocalBroadcastManager.class.getDeclaredField("mReceivers");
            mReceivers.setAccessible(true);
            HashMap<BroadcastReceiver, ArrayList<IntentFilter>> receivers = (HashMap<BroadcastReceiver, ArrayList<IntentFilter>>) mReceivers.get(manager);

            loop:
            for (Map.Entry<BroadcastReceiver, ArrayList<IntentFilter>> entry : receivers.entrySet()) {
                for (IntentFilter filter : entry.getValue()) {
                    for (int i = 0; i < filter.countActions(); i++) {
                        if (filter.getAction(i).equals(action)) {
                            unregister(entry.getKey());
                            continue loop;
                        }
                    }
                }
            }
        }

        @Override
        public void sendBroadcast(Intent intent) {
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(Application.get());
            manager.sendBroadcast(intent);
        }

        @Override
        public void sendBroadcast(String action) {
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(Application.get());
            manager.sendBroadcast(new Intent(action));
        }

        @Override
        public void receiveOnce(BroadcastReceiver receiver, String... action) {
            register(new OnceBroadcastReceiver(receiver, this), action);
        }
    }

    /**
     * 代理BroadcastReceiver使用后就丢弃
     */
    private static class OnceBroadcastReceiver extends BroadcastReceiver {
        private final BroadcastReceiver receiver;
        private final BroadCastReceiverOperator broadCastReceiverOperator;

        public OnceBroadcastReceiver(BroadcastReceiver receiver, BroadCastReceiverOperator broadCastReceiverOperator) {
            this.receiver = receiver;
            this.broadCastReceiverOperator = broadCastReceiverOperator;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            receiver.onReceive(context, intent);
            broadCastReceiverOperator.unregister(this);
        }
    }

    public interface BroadCastReceiverOperator {

        void register(BroadcastReceiver receiver, String... action);

        void register(BroadcastReceiver receiver, IntentFilter filter);

        void unregister(BroadcastReceiver receiver);

        void unregister(String action) throws Exception;

        void sendBroadcast(Intent intent);

        void sendBroadcast(String action);

        void receiveOnce(BroadcastReceiver receiver, String... action);
    }
}
