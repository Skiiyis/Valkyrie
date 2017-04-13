package sawa.android.reader.douban.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.utils.ToastUtils;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import sawa.android.reader.R;
import sawa.android.reader.common.BaseActivity;
import sawa.android.reader.common.DefaultObserver;
import sawa.android.reader.db.CacheManager;
import sawa.android.reader.douban.bean.DouBanFMSongListDetail;
import sawa.android.reader.douban.view_model.DouBanFMSongListDetailItemViewModel;
import sawa.android.reader.douban.view_wrapper.DouBanFMSongListActivityViewWrapper;
import sawa.android.reader.douban.view_wrapper.DouBanFMSongListDetailItemViewWrapper;
import sawa.android.reader.global.Application;
import sawa.android.reader.http.DouBanFMApi;
import sawa.android.reader.util.LogUtil;

/**
 * 豆瓣歌单详情
 */
public class DouBanFMSongListActivity extends BaseActivity implements View.OnClickListener {

    private DouBanFMSongListActivityViewWrapper viewWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Observable<DouBanFMSongListDetail> observable = Observable.just(getIntent().getStringExtra("songListId"))
                .observeOn(Schedulers.io())
                .flatMap(new Function<String, Observable<DouBanFMSongListDetail>>() {
                    @Override
                    public Observable<DouBanFMSongListDetail> apply(String s) throws Exception {
                        final String cache = CacheManager.INSTANCE.cache("songListId" + s);
                        if (!TextUtils.isEmpty(cache)) {
                            return Observable.create(new ObservableOnSubscribe<DouBanFMSongListDetail>() {
                                @Override
                                public void subscribe(ObservableEmitter<DouBanFMSongListDetail> e) throws Exception {
                                    DouBanFMSongListDetail detail = new Gson().fromJson(cache, DouBanFMSongListDetail.class);
                                    e.onNext(detail);
                                }
                            });
                        }
                        return null;
                    }
                });

        if (observable != null) {
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DouBanFMSongListDetailObserver(this));
        }

        Observable.just(getIntent().getStringExtra("songListId"))
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String songListId) throws Exception {
                        initView();
                    }
                })
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
                        CacheManager.INSTANCE.cache("songListId" + detail.getId(), new Gson().toJson(detail));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DouBanFMSongListDetailObserver(this));
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
     * 获取到了豆瓣歌单详情
     *
     * @param detail
     */
    private void getDetail(DouBanFMSongListDetail detail) {
        viewWrapper.coverImageView().load(detail.getCover(), android.R.color.transparent, android.R.color.transparent);
        viewWrapper.creatorPictureImageView().load(detail.getCreator().getPicture()).circular();
        viewWrapper.titleTextView().setText(detail.getTitle());
        viewWrapper.creatorNameTextView().setText(detail.getCreator().getName());
        viewWrapper.collectedCountTextView().setText("" + detail.getCollected_count());
        viewWrapper.collectedCountTextView().setVisibility(View.VISIBLE);
        viewWrapper.containerRecycleView().setLayoutManager(new LinearLayoutManager(this));
        viewWrapper.containerRecycleView().setAdapter(new DouBanFMSongListDetailAdapter(this, detail.getSongs()));
        viewWrapper.starCheckBox().setOnClickListener(this);
        viewWrapper.shareCheckBox().setOnClickListener(this);
        viewWrapper.downloadCheckBox().setOnClickListener(this);
        viewWrapper.playCheckBox().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == viewWrapper.starCheckBox()) {

        } else if (v == viewWrapper.shareCheckBox()) {

        } else if (v == viewWrapper.downloadCheckBox()) {

        }
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
            ToastUtils.showShortToast("获取歌单详情失败（" + e.getMessage() + "）");
            LogUtil.e(e);
        }

        @Override
        public void onNext(DouBanFMSongListDetail douBanFMSongListDetail) {
            if (ref() != null) {
                ref().getDetail(douBanFMSongListDetail);
            }
        }
    }

    /**
     * 豆瓣歌单详情 adapter
     */
    private static class DouBanFMSongListDetailAdapter extends RecyclerView.Adapter<DouBanFMSongListDetailItemViewWrapper> {
        private final List<DouBanFMSongListDetail.Song> songs;
        private final WeakReference<DouBanFMSongListActivity> activity;

        public DouBanFMSongListDetailAdapter(DouBanFMSongListActivity activity, List<DouBanFMSongListDetail.Song> songs) {
            this.activity = new WeakReference<>(activity);
            this.songs = songs;
        }

        @Override
        public DouBanFMSongListDetailItemViewWrapper onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DouBanFMSongListDetailItemViewWrapper(LayoutInflater.from(Application.get()).inflate(R.layout.item_douban_fm_song_list_detail, parent, false));
        }

        @Override
        public void onBindViewHolder(DouBanFMSongListDetailItemViewWrapper view, int position) {
            DouBanFMSongListDetailItemViewModel viewModel = (DouBanFMSongListDetailItemViewModel) view.rootView().getTag();
            if (viewModel == null) {
                viewModel = new DouBanFMSongListDetailItemViewModel(view);
                view.rootView().setTag(viewModel);
            }
            viewModel.bind(songs.get(position));
        }

        @Override
        public int getItemCount() {
            return songs.size();
        }
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
