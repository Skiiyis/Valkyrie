package sawa.android.reader.main.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import sawa.android.reader.R;
import sawa.android.reader.common.BaseFragment;
import sawa.android.reader.common.DefaultObserver;
import sawa.android.reader.global.Application;
import sawa.android.reader.http.ZhiHuApi;
import sawa.android.reader.main.adapter.ZhiHuViewAdapter;
import sawa.android.reader.main.bean.ZhiHuNewsLatestResponse;
import sawa.android.reader.main.view_wrapper.ViewRecycleViewWrapper;
import sawa.android.reader.util.CacheUtil;
import sawa.android.reader.util.LogUtil;

/**
 * Created by hasee on 2017/3/11.
 */
public class ZhiHuMainFragment extends BaseFragment {

    private ViewRecycleViewWrapper viewRecycleViewWrapper;
    private static final String CACHE_KEY = ZhiHuMainFragment.class.getSimpleName();

    @Override
    protected void onInflated(final View contentView) {
        viewRecycleViewWrapper = new ViewRecycleViewWrapper(contentView);
        NewsLatestZhiHuViewObserver observer = new NewsLatestZhiHuViewObserver(ZhiHuMainFragment.this);

        Observable.just(CACHE_KEY)
                .observeOn(Schedulers.io())
                .compose(this.<String>bindToLifecycle())
                .map(new Function<String, ZhiHuNewsLatestResponse>() {
                    @Override
                    public ZhiHuNewsLatestResponse apply(String key) throws Exception {
                        return CacheUtil.cache(CACHE_KEY, ZhiHuNewsLatestResponse.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        Observable.just(0)
                .observeOn(Schedulers.io())
                .compose(this.<Integer>bindToLifecycle())
                .flatMap(new Function<Integer, Observable<ZhiHuNewsLatestResponse>>() {
                    @Override
                    public Observable<ZhiHuNewsLatestResponse> apply(Integer integer) throws Exception {
                        return new ZhiHuApi().newsLatest();
                    }
                })
                .doOnNext(new Consumer<ZhiHuNewsLatestResponse>() {
                    @Override
                    public void accept(ZhiHuNewsLatestResponse response) throws Exception {
                        CacheUtil.cacheJson(CACHE_KEY, response);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void response(ZhiHuNewsLatestResponse response) {
        viewRecycleViewWrapper.contentRecycleView().setLayoutManager(new LinearLayoutManager(Application.get()));
        viewRecycleViewWrapper.contentRecycleView().setAdapter(new ZhiHuViewAdapter(response));
    }

    /**
     * 请求知乎日报最新内容列表
     */
    private static class NewsLatestZhiHuViewObserver extends DefaultObserver<ZhiHuMainFragment, ZhiHuNewsLatestResponse> {

        public NewsLatestZhiHuViewObserver(ZhiHuMainFragment zhiHuMainFragment) {
            super(zhiHuMainFragment);
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            LogUtil.e(e);
        }

        @Override
        public void onNext(ZhiHuNewsLatestResponse zhiHuNewsLatestResponse) {
            if (ref() != null) {
                ref().response(zhiHuNewsLatestResponse);
            }
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.view_recycle_view;
    }
}
