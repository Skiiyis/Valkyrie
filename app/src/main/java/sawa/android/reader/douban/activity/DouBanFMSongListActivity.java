package sawa.android.reader.douban.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observer;
import sawa.android.reader.R;
import sawa.android.reader.common.BaseActivity;
import sawa.android.reader.douban.bean.DouBanFMSongListDetail;
import sawa.android.reader.douban.view_model.DouBanFMSongListDetailItemViewModel;
import sawa.android.reader.douban.view_wrapper.DouBanFMSongListActivityViewWrapper;
import sawa.android.reader.douban.view_wrapper.DouBanFMSongListDetailItemViewWrapper;
import sawa.android.reader.global.Application;
import sawa.android.reader.http.DouBanFMApi;

public class DouBanFMSongListActivity extends BaseActivity implements View.OnClickListener {

    private DouBanFMSongListActivityViewWrapper activityViewWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityViewWrapper = new DouBanFMSongListActivityViewWrapper(View.inflate(this, R.layout.activity_dou_ban_fm_song_list, null));
        setContentView(activityViewWrapper.getRootView());

        setSupportActionBar(activityViewWrapper.toolbar());
        activityViewWrapper.toolbar().setTitle("");
        activityViewWrapper.toolbarLayout().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        DouBanFMApi.songListDetail(getIntent().getStringExtra("songListId"), new DouBanFMSongListDetailRequest(this));
    }

    /**
     * 获取到了豆瓣歌单详情
     *
     * @param detail
     */
    private void getDetail(DouBanFMSongListDetail detail) {
        activityViewWrapper.coverImageView().load(detail.getCover(), android.R.color.transparent, android.R.color.transparent);
        activityViewWrapper.creatorPictureImageView().load(detail.getCreator().getPicture()).circular();
        activityViewWrapper.titleTextView().setText(detail.getTitle());
        activityViewWrapper.creatorNameTextView().setText(detail.getCreator().getName());
        activityViewWrapper.collectedCountTextView().setText("" + detail.getCollected_count());
        activityViewWrapper.collectedCountTextView().setVisibility(View.VISIBLE);
        activityViewWrapper.containerRecycleView().setLayoutManager(new LinearLayoutManager(this));
        activityViewWrapper.containerRecycleView().setAdapter(new DouBanFMSongListDetailAdapter(this, detail.getSongs()));
        activityViewWrapper.starCheckBox().setOnClickListener(this);
        activityViewWrapper.shareCheckBox().setOnClickListener(this);
        activityViewWrapper.toolbar().setTitle(detail.getTitle());
        activityViewWrapper.downloadCheckBox().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 请求豆瓣FM 歌单详情
     */
    private static class DouBanFMSongListDetailRequest implements Observer<DouBanFMSongListDetail> {
        WeakReference<DouBanFMSongListActivity> activity;

        public DouBanFMSongListDetailRequest(DouBanFMSongListActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            ToastUtils.showShortToast("获取歌单详情失败（" + e.getMessage() + "）");
            LogUtils.e(e);
        }

        @Override
        public void onNext(DouBanFMSongListDetail douBanFMSongListDetail) {
            DouBanFMSongListActivity activity = this.activity.get();
            if (activity != null) {
                activity.getDetail(douBanFMSongListDetail);
            }
        }
    }

    /**
     * 豆瓣歌单详情 adapter
     */
    private static class DouBanFMSongListDetailAdapter extends RecyclerView.Adapter<DouBanFMSongListDetailItemViewWrapper> {
        private final List<DouBanFMSongListDetail.Song> songs;
        private WeakReference<DouBanFMSongListActivity> activity;

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
            DouBanFMSongListDetailItemViewModel viewModel = (DouBanFMSongListDetailItemViewModel) view.getRootView().getTag();
            if (viewModel == null) {
                viewModel = new DouBanFMSongListDetailItemViewModel(view);
                view.getRootView().setTag(viewModel);
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
