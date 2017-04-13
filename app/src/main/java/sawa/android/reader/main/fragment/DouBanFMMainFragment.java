package sawa.android.reader.main.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.utils.LogUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import sawa.android.reader.R;
import sawa.android.reader.common.BaseFragment;
import sawa.android.reader.common.DefaultObserver;
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
public class DouBanFMMainFragment extends BaseFragment {

    private ViewRecycleViewWrapper viewRecycleViewWrapper;

    @Override
    protected void onInflated(final View contentView) {
        Observable.just(0)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        viewRecycleViewWrapper = new ViewRecycleViewWrapper(contentView);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<Integer, Observable<List<DouBanFMSongList>>>() {
                    @Override
                    public Observable<List<DouBanFMSongList>> apply(Integer integer) throws Exception {
                        return DouBanFMApi.songList();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DouBanFMSongListObserver(this));
    }

    private void getChannelsResponse(List<DouBanFMChannel.Channel> douBanFMChannels) {
        viewRecycleViewWrapper.contentRecycleView().setLayoutManager(new LinearLayoutManager(Application.get()));
    }

    private void getSongListResponse(List<DouBanFMSongList> douBanFMSongLists) {
        viewRecycleViewWrapper.contentRecycleView().setLayoutManager(new LinearLayoutManager(Application.get()));
        viewRecycleViewWrapper.contentRecycleView().setAdapter(new DouBanFMSongListAdapter(douBanFMSongLists));
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
            DouBanFMViewModel viewModel = (DouBanFMViewModel) view.rootView().getTag();
            if (viewModel == null) {
                viewModel = new DouBanFMViewModel(view);
                view.rootView().setTag(viewModel);
                view.rootView().setOnClickListener(new View.OnClickListener() {
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
     * 请求豆瓣FM歌单列表
     */
    private static class DouBanFMSongListObserver extends DefaultObserver<DouBanFMMainFragment, List<DouBanFMSongList>> {

        public DouBanFMSongListObserver(DouBanFMMainFragment douBanFMMainFragment) {
            super(douBanFMMainFragment);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }

        @Override
        public void onNext(List<DouBanFMSongList> douBanFMSongLists) {
            if (ref() != null) {
                ref().getSongListResponse(douBanFMSongLists);
            }
        }
    }

    /**
     * 请求豆瓣FM频道列表
     */
    private static class DouBanFMChannelsObserver extends DefaultObserver<DouBanFMMainFragment, List<DouBanFMChannel>> {

        private WeakReference<DouBanFMMainFragment> view;

        public DouBanFMChannelsObserver(DouBanFMMainFragment douBanFMMainFragment) {
            super(douBanFMMainFragment);
        }

        @Override
        public void onError(Throwable e) {
            LogUtils.e(e.getMessage());
        }

        @Override
        public void onNext(List<DouBanFMChannel> douBanFMChannels) {
            DouBanFMMainFragment douBanFMMainFragment = view.get();
            if (douBanFMMainFragment != null) {
                ArrayList<DouBanFMChannel.Channel> channels = new ArrayList<>();
                for (DouBanFMChannel response : douBanFMChannels) {
                    channels.addAll(response.getChls());
                }
                douBanFMMainFragment.getChannelsResponse(channels);
            }
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.view_recycle_view;
    }
}
