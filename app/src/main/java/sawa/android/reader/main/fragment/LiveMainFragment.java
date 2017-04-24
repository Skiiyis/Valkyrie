package sawa.android.reader.main.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import sawa.android.reader.R;
import sawa.android.reader.common.BaseFragment;
import sawa.android.reader.common.DefaultObserver;
import sawa.android.reader.global.Application;
import sawa.android.reader.http.MaxJiaLiveApi;
import sawa.android.reader.main.bean.LiveListItem;
import sawa.android.reader.main.view_wrapper.LiveMainViewWrapper;
import sawa.android.reader.main.view_wrapper.ViewRecycleViewWrapper;
import sawa.android.reader.maxjia.activity.LiveDetailActivity;

/**
 * Created by mc100 on 2017/4/19.
 */
public class LiveMainFragment extends BaseFragment {

    private ViewRecycleViewWrapper viewRecycleViewWrapper;

    @Override
    protected int layoutResId() {
        return R.layout.view_recycle_view;
    }

    @Override
    protected void onInflated(View contentView) {
        viewRecycleViewWrapper = new ViewRecycleViewWrapper(contentView);
        RecyclerView recycleView = viewRecycleViewWrapper.contentRecycleView();
        recycleView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        Observable.just(0)
                .compose(this.<Integer>bindToLifecycle())
                .observeOn(Schedulers.io())
                .flatMap(new Function<Integer, Observable<List<LiveListItem>>>() {
                    @Override
                    public Observable<List<LiveListItem>> apply(Integer integer) throws Exception {
                        return MaxJiaLiveApi.liveList(0, 20, "lol");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LiveListDateObserver(this));
    }

    /**
     * 获取到直播列表
     *
     * @param value
     */
    private void getLiveListDate(List<LiveListItem> value) {
        viewRecycleViewWrapper.contentRecycleView().setAdapter(new LiveMainAdapter(value));
    }

    private static class LiveListDateObserver extends DefaultObserver<LiveMainFragment, List<LiveListItem>> {

        public LiveListDateObserver(LiveMainFragment liveMainFragment) {
            super(liveMainFragment);
        }

        @Override
        public void onNext(List<LiveListItem> value) {
            if (ref() != null) {
                ref().getLiveListDate(value);
            }
        }
    }

    /**
     * 直播间列表
     */
    private static class LiveMainAdapter extends RecyclerView.Adapter<LiveMainViewWrapper> {

        private final List<LiveListItem> value;

        public LiveMainAdapter(List<LiveListItem> value) {
            this.value = value;
        }

        @Override
        public LiveMainViewWrapper onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LiveMainViewWrapper(LayoutInflater.from(Application.get()).inflate(R.layout.item_live_main, parent, false));
        }

        @Override
        public void onBindViewHolder(LiveMainViewWrapper view, int position) {
            LiveListItem item = value.get(position);
            view.audienceTextView().setText("观看人数" + item.getLive_online());
            view.roomThumbPlusImageView().load(item.getLive_img());
            view.liverPlusImageView().load(item.getLive_userimg(), R.drawable.ic_person);
            view.liverPlusImageView().circular();
            view.roomNameTextView().setText(item.getLive_title());
            view.liverTextView().setText(item.getLive_nickname());

            view.rootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LiveDetailActivity.launch("rtmp://live.hkstv.hk.lxdns.com/live/hks");
                }
            });
        }

        @Override
        public int getItemCount() {
            return value.size();
        }
    }
}
