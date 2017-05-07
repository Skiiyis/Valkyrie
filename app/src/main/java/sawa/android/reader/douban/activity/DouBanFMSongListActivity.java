package sawa.android.reader.douban.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.blankj.utilcode.utils.ToastUtils;
import com.jakewharton.rxbinding2.view.RxView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import sawa.android.reader.R;
import sawa.android.reader.common.BaseActivity;
import sawa.android.reader.common.DefaultObserver;
import sawa.android.reader.douban.bean.DouBanFMSongListDetail;
import sawa.android.reader.douban.view_model.DouBanFMSongListDetailItemViewModel;
import sawa.android.reader.douban.view_wrapper.DouBanFMSongDetailPopupWindowViewWrapper;
import sawa.android.reader.douban.view_wrapper.DouBanFMSongListActivityViewWrapper;
import sawa.android.reader.douban.view_wrapper.DouBanFMSongListDetailItemViewWrapper;
import sawa.android.reader.global.Application;
import sawa.android.reader.http.DouBanFMApi;
import sawa.android.reader.music.MusicPlayHelper;
import sawa.android.reader.music.MusicPlayService;
import sawa.android.reader.util.CacheUtil;
import sawa.android.common.util.LogUtil;
import sawa.android.reader.util.StarUtil;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 豆瓣歌单详情
 */
public class DouBanFMSongListActivity extends BaseActivity {

    private DouBanFMSongListActivityViewWrapper viewWrapper;
    private String CACHE_KEY = "songListId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        final String songListId = getIntent().getStringExtra(CACHE_KEY);
        DouBanFMSongListDetailObserver detailObserver = new DouBanFMSongListDetailObserver(this);

        /**
         * 先走离线缓存
         *//*
        Observable.just(songListId)
                .compose(this.<String>bindToLifecycle())
                .observeOn(Schedulers.io())
                .map(new Function<String, DouBanFMSongListDetail>() {
                    @Override
                    public DouBanFMSongListDetail apply(String songListId) throws Exception {
                        return CacheUtil.cache(CACHE_KEY + songListId, DouBanFMSongListDetail.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(detailObserver);*/

        /**
         * 拉取线上内容有更新则立即更新UI，并更新缓存
         */
        Observable.just(songListId)
                .compose(this.<String>bindToLifecycle())
                .observeOn(Schedulers.io())
                .flatMap(new Function<String, Observable<DouBanFMSongListDetail>>() {
                    @Override
                    public Observable<DouBanFMSongListDetail> apply(String songListId) throws Exception {
                        return DouBanFMApi.songListDetail(songListId);
                    }
                })
                .doOnNext(new Consumer<DouBanFMSongListDetail>() {
                    @Override
                    public void accept(DouBanFMSongListDetail detail) throws Exception {
                        CacheUtil.cacheJson(CACHE_KEY + songListId, detail);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(detailObserver);
    }

    private void initView() {
        viewWrapper = new DouBanFMSongListActivityViewWrapper(View.inflate(this, R.layout.activity_dou_ban_fm_song_list, null));
        setContentView(viewWrapper.rootView());

        setSupportActionBar(viewWrapper.toolbar());
        viewWrapper.toolbar().setTitle("");
        viewWrapper.toolbarLayout().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * 获取到了豆瓣歌单详情，刷新列表
     *
     * @param detail
     */
    private void initDetail(final DouBanFMSongListDetail detail) {
        viewWrapper.coverImageView().load(detail.getCover(), android.R.color.transparent, android.R.color.transparent);
        viewWrapper.creatorPictureImageView().load(detail.getCreator().getPicture()).circular();
        viewWrapper.titleTextView().setText(detail.getTitle());
        viewWrapper.creatorNameTextView().setText(detail.getCreator().getName());
        viewWrapper.collectedCountTextView().setText("" + detail.getCollected_count());
        viewWrapper.collectedCountTextView().setVisibility(View.VISIBLE);
        viewWrapper.containerRecycleView().setLayoutManager(new LinearLayoutManager(this));
        viewWrapper.containerRecycleView().setAdapter(new DouBanFMSongListDetailAdapter(this, detail));

        viewWrapper.toolbarLayout().setTitle(detail.getTitle());
        viewWrapper.toolbarLayout().setCollapsedTitleTextColor(Color.WHITE);
        viewWrapper.toolbarLayout().setExpandedTitleColor(Color.TRANSPARENT);
    }

    /**
     * 改变操作栏的状态 是否收藏，是否在播放本列表歌曲，是否已缓存
     *
     * @param songListDetail
     */
    private void initActionBar(final DouBanFMSongListDetail songListDetail) {
        /**
         * 初始化收藏按钮的状态
         */
        Observable.just(songListDetail)
                .compose(this.<DouBanFMSongListDetail>bindToLifecycle())
                .observeOn(Schedulers.io())
                .map(new Function<DouBanFMSongListDetail, Boolean>() {
                    @Override
                    public Boolean apply(DouBanFMSongListDetail detail) throws Exception {
                        return StarUtil.isStar(songListDetail);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isStar) throws Exception {
                        viewWrapper.starImageView().setImageResource(isStar ? R.drawable.ic_star_32dp : R.drawable.ic_star_32dp_default);
                    }
                });

        /**
         * 如果歌单已star，则unStar后设置checkBox check false
         * 反之亦然
         */
        RxView.clicks(viewWrapper.starImageView())
                .compose(this.bindToLifecycle())
                .doOnNext(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        viewWrapper.starImageView().setEnabled(false);
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<Object, Boolean>() {
                    @Override
                    public Boolean apply(Object o) throws Exception {
                        boolean isStar = StarUtil.isStar(songListDetail);
                        if (isStar) {
                            StarUtil.unStar(songListDetail);
                        } else {
                            StarUtil.star(songListDetail);
                        }
                        return !isStar;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isStar) throws Exception {
                        viewWrapper.starImageView().setEnabled(true);
                        viewWrapper.starImageView().setImageResource(isStar ? R.drawable.ic_star_32dp : R.drawable.ic_star_32dp_default);
                    }
                });

        /**
         * 加入歌单并播放，如果歌单是本歌单，那么不做任何动作
         *
         */
        RxView.clicks(viewWrapper.playImageView())
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(DouBanFMSongListActivity.this.bindToLifecycle())
                .observeOn(Schedulers.io())
                .flatMap(new Function<Object, ObservableSource<Intent>>() {

                    @Override
                    public ObservableSource<Intent> apply(Object o) throws Exception {
                        return new MusicPlayHelper()
                                .list()
                                .observe()
                                .compose(DouBanFMSongListActivity.this.<Intent>bindToLifecycle());
                    }
                })
                .map(new Function<Intent, Boolean>() {
                    @Override
                    public Boolean apply(Intent intent) throws Exception {

                        List<DouBanFMSongListDetail.Song> list = (List<DouBanFMSongListDetail.Song>) intent.getSerializableExtra("list");
                        if (list == null || list.size() == 0) {
                            return false;
                        }
                        List<DouBanFMSongListDetail.Song> localList = songListDetail.getSongs();
                        if (list.size() != localList.size()) {
                            return false;
                        }
                        for (int i = 0; i < list.size(); i++) {
                            boolean sameSong = list.get(i).getSid().equals(localList.get(i).getSid());
                            if (!sameSong) {
                                return false;
                            }
                        }
                        return true;
                    }
                })
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean isSameList) throws Exception {
                        LogUtil.e(isSameList + "");
                        return isSameList == null || !isSameList;
                    }
                })
                .flatMap(new Function<Object, ObservableSource<Intent>>() {

                    @Override
                    public ObservableSource<Intent> apply(Object o) throws Exception {
                        return new MusicPlayHelper()
                                .clear()
                                .add(songListDetail.getSongs())
                                .to(0)
                                .observe()
                                .compose(DouBanFMSongListActivity.this.<Intent>bindToLifecycle());
                    }
                })
                .filter(new Predicate<Intent>() {
                    @Override
                    public boolean test(Intent intent) throws Exception {
                        return intent.getAction().equals(MusicPlayService.ACTION_TO);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Intent>() {
                    @Override
                    public void accept(Intent intent) throws Exception {
                        ToastUtils.showShortToast("正在播放：" + songListDetail.getSongs().get(0).getTitle());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showShortToast(throwable.getMessage());
                    }
                });

        //@TODO 分享和下载的逻辑未做
    }

    /**
     * 请求豆瓣FM 歌单详情
     */
    private static class DouBanFMSongListDetailObserver extends DefaultObserver<DouBanFMSongListActivity, DouBanFMSongListDetail> {

        public DouBanFMSongListDetailObserver(DouBanFMSongListActivity activity) {
            super(activity);
        }

        @Override
        public void onError(Throwable e) {
            LogUtil.e(e);
            /**
             * 这里没有缓存的时候会有空指针异常，针对无缓存的异常不做提示
             */
            if (!e.getMessage().contains("null value")) {
                ToastUtils.showShortToast("获取歌单详情失败（" + e.getMessage() + "）");
            }
            super.onError(e);
        }

        @Override
        public void onNext(DouBanFMSongListDetail douBanFMSongListDetail) {
            if (ref() != null) {
                ref().initDetail(douBanFMSongListDetail);
                ref().initActionBar(douBanFMSongListDetail);
            }
        }
    }

    /**
     * 豆瓣歌单详情 adapter
     */
    private static class DouBanFMSongListDetailAdapter extends RecyclerView.Adapter<DouBanFMSongListDetailItemViewWrapper> {

        private final WeakReference<DouBanFMSongListActivity> activity;
        private final DouBanFMSongListDetail detail;

        public DouBanFMSongListDetailAdapter(DouBanFMSongListActivity activity, DouBanFMSongListDetail detail) {
            this.activity = new WeakReference<>(activity);
            this.detail = detail;
        }

        @Override
        public DouBanFMSongListDetailItemViewWrapper onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DouBanFMSongListDetailItemViewWrapper(LayoutInflater.from(Application.get()).inflate(R.layout.item_douban_fm_song_list_detail, parent, false));
        }

        @Override
        public void onBindViewHolder(final DouBanFMSongListDetailItemViewWrapper view, final int position) {
            DouBanFMSongListDetailItemViewModel viewModel = (DouBanFMSongListDetailItemViewModel) view.rootView().getTag();
            if (viewModel == null) {
                viewModel = new DouBanFMSongListDetailItemViewModel(view);
                view.rootView().setTag(viewModel);
            }
            viewModel.bind(detail.getSongs().get(position));

            /**
             * 点击条目将歌单加入并播放当前位置的歌曲
             */
            RxView.clicks(view.rootView())
                    .compose(activity.get().bindToLifecycle())
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .observeOn(Schedulers.io())
                    .flatMap(new Function<Object, ObservableSource<Intent>>() {
                        @Override
                        public ObservableSource<Intent> apply(Object o) throws Exception {
                            return new MusicPlayHelper()
                                    .clear()
                                    .add(detail.getSongs())
                                    .to(position)
                                    .observe()
                                    .compose(activity.get().<Intent>bindToLifecycle());
                        }
                    })
                    .filter(new Predicate<Intent>() {
                        @Override
                        public boolean test(Intent intent) throws Exception {
                            return intent.getAction().equals(MusicPlayService.ACTION_TO);
                        }
                    })
                    .map(new Function<Intent, DouBanFMSongListDetail.Song>() {
                        @Override
                        public DouBanFMSongListDetail.Song apply(Intent intent) throws Exception {
                            return (DouBanFMSongListDetail.Song) intent.getSerializableExtra("song");
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<DouBanFMSongListDetail.Song>() {
                        @Override
                        public void accept(DouBanFMSongListDetail.Song song) throws Exception {
                            LogUtil.e("正在播放：" + song.getTitle());
                            ToastUtils.showShortToast("正在播放：" + song.getTitle());
                        }
                    });

            /**
             * 点击更多弹出选项框
             */
            RxView.clicks(view.moreImageView())
                    .compose(activity.get().bindToLifecycle())
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            activity.get().showSongDetailPopupWindow(detail.getSongs().get(position));
                        }
                    });
        }

        @Override
        public int getItemCount() {
            return detail.getSongs().size();
        }
    }

    public void showSongDetailPopupWindow(final DouBanFMSongListDetail.Song song) {

        View rootView = View.inflate(this, R.layout.fragment_douban_fm_song_detail, null);
        final PopupWindow popupWindow = new PopupWindow(rootView, MATCH_PARENT, WRAP_CONTENT);
        final DouBanFMSongDetailPopupWindowViewWrapper viewWrapper = new DouBanFMSongDetailPopupWindowViewWrapper(rootView);

        RxView.clicks(viewWrapper.cancelTextView())
                .compose(this.bindToLifecycle())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                    }
                });

        /**
         * 如果歌单已star，则unStar后设置checkBox check false
         * 反之亦然
         */
        RxView.clicks(viewWrapper.starImageView())
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .observeOn(Schedulers.io())
                .map(new Function<Object, Boolean>() {
                    @Override
                    public Boolean apply(Object o) throws Exception {
                        boolean isStar = StarUtil.isStar(song);
                        boolean result = isStar ? StarUtil.unStar(song) : StarUtil.star(song);
                        return result ? !isStar : isStar;
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
                .compose(this.<DouBanFMSongListDetail.Song>bindToLifecycle())
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

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(viewWrapper.rootView(), Gravity.BOTTOM, 0, 0);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                final WindowManager.LayoutParams wlBackground = getWindow().getAttributes();
                wlBackground.alpha = 1f;      // 0.0 完全不透明,1.0完全透明
                getWindow().setAttributes(wlBackground);
            }
        });

        final WindowManager.LayoutParams wlBackground = getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getWindow().setAttributes(wlBackground);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static void launch(Activity activity, String songListId) {
        Intent intent = new Intent(activity, DouBanFMSongListActivity.class);
        intent.putExtra("songListId", songListId);
        activity.startActivity(intent);
    }

    public static void launch(String songListId) {
        Intent intent = new Intent(Application.get(), DouBanFMSongListActivity.class);
        intent.putExtra("songListId", songListId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Application.get().startActivity(intent);
    }
}
