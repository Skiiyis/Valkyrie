package sawa.android.reader.zhihu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import sawa.android.reader.common.DefaultObserver;
import sawa.android.reader.webview.WebViewActivity;
import sawa.android.reader.global.Application;
import sawa.android.reader.http.ZhiHuApi;
import sawa.android.reader.util.CacheUtil;
import sawa.android.common.util.LogUtil;
import sawa.android.reader.zhihu.bean.ZhiHuNewsDetailResponse;

/**
 * 知乎WebView
 */
public class ZhiHuWebViewActivity extends WebViewActivity {

    private static final String CACHE_KEY = "zhihu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String id = getIntent().getStringExtra("id");
        NewsDetailObserver observer = new NewsDetailObserver(this);

        Observable.just(id)
                .observeOn(Schedulers.io())
                .compose(this.<String>bindToLifecycle())
                .map(new Function<String, ZhiHuNewsDetailResponse>() {
                    @Override
                    public ZhiHuNewsDetailResponse apply(String id) throws Exception {
                        return CacheUtil.cache(CACHE_KEY + id, ZhiHuNewsDetailResponse.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        Observable.just(id)
                .observeOn(Schedulers.io())
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Function<String, Observable<ZhiHuNewsDetailResponse>>() {
                    @Override
                    public Observable<ZhiHuNewsDetailResponse> apply(String id) throws Exception {
                        return ZhiHuApi.newsDetail(id);
                    }
                })
                .doOnNext(new Consumer<ZhiHuNewsDetailResponse>() {
                    @Override
                    public void accept(ZhiHuNewsDetailResponse zhiHuNewsDetailResponse) throws Exception {
                        CacheUtil.cacheJson(CACHE_KEY + id, zhiHuNewsDetailResponse);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 请求返回
     *
     * @param response
     */
    private void response(ZhiHuNewsDetailResponse response) {
        view.toolbar().setTitle(response.getTitle());
        view.toolbarLayout().setTitle(Html.fromHtml("<font fontSize=12>" + response.getTitle() + "</font>"));
        view.headerImageView().load(response.getImage());
        String cssStr = "<link rel=\"stylesheet\" type=\"text/css\" href=\"%s\" />";
        String html = response.getBody();
        for (String cssLink : response.getCss()) {
            html = html + String.format(cssStr, cssLink);
        }
        html = html.replace("<div class=\"img-place-holder\"></div>", "");
        view.contentWebView().loadData(html, "text/html; charset=UTF-8", null);
    }

    /**
     * 请求知乎日报内容
     */
    private static class NewsDetailObserver extends DefaultObserver<ZhiHuWebViewActivity, ZhiHuNewsDetailResponse> {

        public NewsDetailObserver(ZhiHuWebViewActivity zhiHuWebViewActivity) {
            super(zhiHuWebViewActivity);
        }

        @Override
        public void onError(Throwable e) {
            LogUtil.e(e);
        }

        @Override
        public void onNext(ZhiHuNewsDetailResponse zhiHuNewsDetailResponse) {
            if (ref() != null) {
                ref().response(zhiHuNewsDetailResponse);
            }
        }
    }

    public static void launch(Activity activity, String id) {
        Intent intent = new Intent(activity, ZhiHuWebViewActivity.class);
        intent.putExtra("id", id);
        activity.startActivity(intent);
    }

    public static void launch(String id) {
        Context context = Application.get();
        Intent intent = new Intent(context, ZhiHuWebViewActivity.class);
        intent.putExtra("id", id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
