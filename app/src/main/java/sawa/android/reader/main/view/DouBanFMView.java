package sawa.android.reader.main.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.utils.LogUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import sawa.android.reader.R;
import sawa.android.reader.common.BaseView;
import sawa.android.reader.douban.activity.DouBanFMSongListActivity;
import sawa.android.reader.global.Application;
import sawa.android.reader.http.DouBanFMApi;
import sawa.android.reader.main.bean.DouBanFMChannel;
import sawa.android.reader.main.bean.DouBanFMSongList;
import sawa.android.reader.main.view_model.DouBanFMViewModel;
import sawa.android.reader.main.view_wrapper.DouBanFMViewWrapper;
import sawa.android.reader.main.view_wrapper.ViewRecycleViewWrapper;

/**
 * Created by hasee on 2017/3/26.
 */
public class DouBanFMView extends BaseView {

    private ViewRecycleViewWrapper viewRecycleViewWrapper;

    public DouBanFMView(Context context) {
        super(context);
    }

    public DouBanFMView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DouBanFMView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onInflate(View contentView) {
        viewRecycleViewWrapper = new ViewRecycleViewWrapper(contentView);
        DouBanFMApi.songList(new DouBanFMSongListRequest(this));
    }

    private void getChannelsResponse(List<DouBanFMChannel.Channel> douBanFMChannels) {
        RecyclerView recyclerView = viewRecycleViewWrapper.contentRecycleView();
        recyclerView.setLayoutManager(new LinearLayoutManager(Application.get()));
        //recyclerView.setAdapter(new DouBanFMViewAdapter(douBanFMChannels));
    }

    private void getSongListResponse(List<DouBanFMSongList> douBanFMSongLists) {
        RecyclerView recyclerView = viewRecycleViewWrapper.contentRecycleView();
        recyclerView.setLayoutManager(new LinearLayoutManager(Application.get()));
        recyclerView.setAdapter(new DouBanFMSongListAdapter(douBanFMSongLists));
    }

    /**
     * 豆瓣fm recycleview adapter
     */
    private static class DouBanFMSongListAdapter extends RecyclerView.Adapter<DouBanFMViewWrapper> {

        private final List<DouBanFMSongList> douBanFMSongLists;

        public DouBanFMSongListAdapter(List<DouBanFMSongList> douBanFMSongLists) {
            this.douBanFMSongLists = douBanFMSongLists;
        }

        @Override
        public DouBanFMViewWrapper onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DouBanFMViewWrapper(LayoutInflater.from(Application.get()).inflate(R.layout.item_main_douban_fm, parent, false));
        }

        @Override
        public void onBindViewHolder(DouBanFMViewWrapper view, final int position) {
            DouBanFMViewModel viewModel = (DouBanFMViewModel) view.getRootView().getTag();
            if (viewModel == null) {
                viewModel = new DouBanFMViewModel(view);
                view.getRootView().setTag(viewModel);
                view.getRootView().setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DouBanFMSongListActivity.launch("" + douBanFMSongLists.get(position).getId());
                    }
                });
            }
            viewModel.bind(douBanFMSongLists.get(position));
        }

        @Override
        public int getItemCount() {
            return douBanFMSongLists.size();
        }
    }

    /**
     * 豆瓣fm recycleview adapter
     */
    /*private static class DouBanFMViewAdapter extends RecyclerView.Adapter {
        private final List<DouBanFMChannel.Channel> douBanFMChannels;

        public DouBanFMViewAdapter(List<DouBanFMChannel.Channel> douBanFMChannels) {
            this.douBanFMChannels = douBanFMChannels;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DouBanFMViewWrapper(LayoutInflater.from(Application.get()).inflate(R.layout.item_main_douban_fm, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            DouBanFMViewWrapper viewWrapper = (DouBanFMViewWrapper) holder;
            DouBanFMViewModel viewModel = viewWrapper.getViewModel();
            if (viewModel == null) {
                viewModel = new DouBanFMViewModel(viewWrapper);
                viewWrapper.getRootView().setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
            viewModel.bind(douBanFMChannels.get(position));
        }

        @Override
        public int getItemCount() {
            return douBanFMChannels.size();
        }
    }*/

    /**
     * 请求豆瓣FM歌单列表
     */
    private static class DouBanFMSongListRequest implements Observer<List<DouBanFMSongList>> {

        private WeakReference<DouBanFMView> view;

        public DouBanFMSongListRequest(DouBanFMView douBanFMView) {
            view = new WeakReference<>(douBanFMView);
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }

        @Override
        public void onNext(List<DouBanFMSongList> douBanFMSongLists) {
            DouBanFMView douBanFMView = view.get();
            if (douBanFMView != null) {
                douBanFMView.getSongListResponse(douBanFMSongLists);
            }
        }
    }

    /**
     * 请求豆瓣FM频道列表
     */
    private static class DouBanFMChannelsRequest implements Observer<List<DouBanFMChannel>> {

        private WeakReference<DouBanFMView> view;

        public DouBanFMChannelsRequest(DouBanFMView douBanFMView) {
            view = new WeakReference<>(douBanFMView);
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            LogUtils.e(e.getMessage());
        }

        @Override
        public void onNext(List<DouBanFMChannel> douBanFMChannels) {
            DouBanFMView douBanFMView = view.get();
            if (douBanFMView != null) {
                ArrayList<DouBanFMChannel.Channel> channels = new ArrayList<>();
                for (DouBanFMChannel response : douBanFMChannels) {
                    channels.addAll(response.getChls());
                }
                douBanFMView.getChannelsResponse(channels);
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_recycle_view;
    }
}
