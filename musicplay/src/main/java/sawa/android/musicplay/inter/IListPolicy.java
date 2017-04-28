package sawa.android.musicplay.inter;

import java.util.List;

/**
 * Created by hasee on 2017/5/1.
 */
public interface IListPolicy<T> {

    T next();

    T prev();

    T to(int position);

    void add(T t);

    void add(List<T> t);

    void clear();

    int position();

    List<T> list();
}
