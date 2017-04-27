package sawa.android.reader.douban.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import sawa.android.reader.R;
import sawa.android.reader.common.BaseActivity;
import sawa.android.reader.douban.bean.DouBanFMSongListDetail;
import sawa.android.reader.douban.view_wrapper.DouBanFMSongDetailDialogFragmentViewWrapper;
import sawa.android.reader.util.StarUtil;

/**
 * Created by mc100 on 2017/4/27.
 */

public class DouBanFMSongDetailDialogFragment extends DialogFragment {

    private DouBanFMSongDetailDialogFragmentViewWrapper viewWrapper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewWrapper = new DouBanFMSongDetailDialogFragmentViewWrapper(LayoutInflater.from(getContext()).inflate(R.layout.fragment_douban_fm_song_detail, container, false));
        DouBanFMSongListDetail.Song song = (DouBanFMSongListDetail.Song) getArguments().getSerializable("song");
        viewWrapper.coverImageView().load(song.getPicture());
        viewWrapper.titleTextView().setText(song.getTitle());
        String singers = null;
        for (DouBanFMSongListDetail.Singer singer : song.getSingers()) {
            singers = singers + " " + singer.getName();
        }
        viewWrapper.creatorNameTextView().setText(singers);

        RxView.clicks(viewWrapper.cancelTextView())
                .compose(((BaseActivity) getActivity()).bindToLifecycle())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        dismiss();
                    }
                });

        /**
         * 如果歌单已star，则unStar后设置checkBox check false
         * 反之亦然
         */
        RxView.clicks(viewWrapper.starImageView())
                .compose(((BaseActivity) getActivity()).bindToLifecycle())
                .observeOn(Schedulers.io())
                .map(new Function<Object, Boolean>() {
                    @Override
                    public Boolean apply(Object o) throws Exception {
                        //@TODO
                        return false;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isStar) throws Exception {
                        viewWrapper.starImageView().setImageResource(isStar ? R.drawable.ic_star_32dp : R.drawable.ic_star_32dp_default);
                    }
                });

        Observable.just(song)
                .compose(((BaseActivity) getActivity()).<DouBanFMSongListDetail.Song>bindToLifecycle())
                .observeOn(Schedulers.io())
                .map(new Function<DouBanFMSongListDetail.Song, Boolean>() {
                    @Override
                    public Boolean apply(DouBanFMSongListDetail.Song song) throws Exception {
                        return StarUtil.isStar(song);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isStar) throws Exception {
                        viewWrapper.starImageView().setImageResource(isStar ? R.drawable.ic_star_32dp : R.drawable.ic_star_32dp_default);
                    }
                });

        return viewWrapper.rootView();
    }

    public static DouBanFMSongDetailDialogFragment create(Activity activity, DouBanFMSongListDetail.Song song) {
        Bundle args = new Bundle();
        args.putSerializable("song", song);
        return (DouBanFMSongDetailDialogFragment) Fragment.instantiate(activity, DouBanFMSongDetailDialogFragment.class.getName(), args);
    }
}
