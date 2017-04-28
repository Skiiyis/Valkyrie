package sawa.android.musicplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sawa.android.musicplay.constants.MusicPlayMode;
import sawa.android.musicplay.inter.IListPolicy;

/**
 * Created by hasee on 2017/5/1.
 */
public class ListPolicyFactory {

    public static <T> IListPolicy<T> create(@MusicPlayMode String playMode, List<T> t) {
        switch (playMode) {
            case MusicPlayMode.LIST_LOOP:
                return new TurnListPolicy<>(t);
            case MusicPlayMode.LIST_RANDOM:
                return new RandomListPolicy<>(t);
            case MusicPlayMode.SINGLE_LOOP:
                return new LoopListPolicy<>(t);
        }
        return null;
    }

    public static <T> IListPolicy<T> create(@MusicPlayMode String playMode, IListPolicy<T> policy) {
        switch (playMode) {
            case MusicPlayMode.LIST_LOOP:
                TurnListPolicy<T> tTurnListPolicy = new TurnListPolicy<>(policy.list());
                tTurnListPolicy.to(policy.position());
                return tTurnListPolicy;
            case MusicPlayMode.LIST_RANDOM:
                RandomListPolicy<T> tRandomListPolicy = new RandomListPolicy<>(policy.list());
                tRandomListPolicy.to(policy.position());
                return tRandomListPolicy;
            case MusicPlayMode.SINGLE_LOOP:
                LoopListPolicy<T> tLoopListPolicy = new LoopListPolicy<>(policy.list());
                tLoopListPolicy.to(policy.position());
                return tLoopListPolicy;
        }
        return null;
    }

    /**
     * 循环模式的列表
     *
     * @param <T>
     */
    private static class LoopListPolicy<T> implements IListPolicy<T> {

        private final List<T> t = new ArrayList<>();
        private int position = 0;

        public LoopListPolicy(List<T> t) {
            this.t.addAll(t);
        }

        @Override
        public T next() {
            return t.get(position);
        }

        @Override
        public T prev() {
            return t.get(position);
        }

        @Override
        public T to(int position) {
            this.position = position;
            return t.get(position);
        }

        @Override
        public void add(T t) {
            this.t.add(0, t);
        }

        @Override
        public void add(List<T> t) {
            this.t.addAll(0, t);
        }

        @Override
        public void clear() {
            t.clear();
        }

        @Override
        public int position() {
            return position;
        }

        @Override
        public List<T> list() {
            return t;
        }
    }

    /**
     * 顺序模式的列表
     *
     * @param <T>
     */
    private static class TurnListPolicy<T> implements IListPolicy<T> {

        private final List<T> t = new ArrayList<>();
        private int position = 0;

        public TurnListPolicy(List<T> t) {
            this.t.addAll(t);
        }

        @Override
        public T next() {
            position = (position + t.size() + 1) % t.size();
            return t.get(position);
        }

        @Override
        public T prev() {
            position = (position + t.size() - 1) % t.size();
            return t.get(position);
        }

        @Override
        public T to(int position) {
            this.position = position;
            return t.get(position);
        }

        @Override
        public void add(T t) {
            this.t.add(0, t);
        }

        @Override
        public void add(List<T> t) {
            this.t.addAll(0, t);
        }

        @Override
        public void clear() {
            t.clear();
        }

        @Override
        public int position() {
            return position;
        }

        @Override
        public List<T> list() {
            return t;
        }
    }

    /**
     * 随机模式的列表
     *
     * @param <T>
     */
    private static class RandomListPolicy<T> implements IListPolicy<T> {

        private final List<T> t = new ArrayList<>();
        private final List<T> t$ = new ArrayList<>();
        private int position = 0;

        public RandomListPolicy(List<T> t) {
            this.t.addAll(t);
            this.t$.addAll(t);
            Collections.shuffle(t$);
        }

        @Override
        public T next() {
            position = (this.position + t$.size() + 1) % t$.size();
            return t$.get(position);
        }

        @Override
        public T prev() {
            position = (this.position + t$.size() - 1) % t$.size();
            return t$.get(position);
        }

        @Override
        public T to(int position) {
            T t = this.t.get(position);
            this.position = t$.indexOf(t);
            return t;
        }

        @Override
        public void add(T t) {
            this.t.add(0, t);
            this.t$.add(0, t);
        }

        @Override
        public void add(List<T> t) {
            this.t.addAll(0, t);
            this.t$.addAll(0, t);
        }

        @Override
        public void clear() {
            t.clear();
            t$.clear();
        }

        @Override
        public int position() {
            return t.indexOf(t$.get(position));
        }

        @Override
        public List<T> list() {
            return t;
        }
    }
}
