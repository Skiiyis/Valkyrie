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
import sawa.android.reader.global.Application;
import sawa.android.reader.http.DouBanFMApi;
import sawa.android.reader.main.bean.DouBanFMChannel;
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
        DouBanFMApi.channelList(new DouBanFMChannelsRequest(this));
    }

    private void getChannelsResponse(List<DouBanFMChannel.Channel> douBanFMChannels) {
        RecyclerView recyclerView = viewRecycleViewWrapper.contentRecycleView();
        recyclerView.setLayoutManager(new LinearLayoutManager(Application.get()));
        recyclerView.setAdapter(new DouBanFMViewAdapter(douBanFMChannels));
    }

    /**
     * 切换音乐播放的状态
     */
    private void toggleMusicStatus() {

    }

    /**
     * 豆瓣fm recycleview adapter
     */
    private class DouBanFMViewAdapter extends RecyclerView.Adapter {
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
                        toggleMusicStatus();
                    }
                });
            }
            viewModel.bind(douBanFMChannels.get(position));
        }

        @Override
        public int getItemCount() {
            return douBanFMChannels.size();
        }
    }

    /**
     * 请求豆瓣FM频道列表
     */
    private class DouBanFMChannelsRequest implements Observer<List<DouBanFMChannel>> {

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
