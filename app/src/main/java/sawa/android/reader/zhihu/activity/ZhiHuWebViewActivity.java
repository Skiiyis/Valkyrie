package sawa.android.reader.zhihu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.text.Html;
import android.webkit.WebView;

import com.blankj.utilcode.utils.LogUtils;

import java.lang.ref.WeakReference;

import rx.Subscriber;
import sawa.android.reader.R;
import sawa.android.reader.common.PlusImageView;
import sawa.android.reader.common.WebViewActivity;
import sawa.android.reader.global.Application;
import sawa.android.reader.http.ZhiHuApi;
import sawa.android.reader.zhihu.bean.ZhiHuNewsDetailResponse;

/**
 * 知乎WebView
 */
public class ZhiHuWebViewActivity extends WebViewActivity {

    private WebView contentWebView;
    private CollapsingToolbarLayout toolbarLayout;
    private PlusImageView pvHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentWebView = (WebView) findViewById(R.id.wv_content);

        String id = getIntent().getStringExtra("id");
        ZhiHuApi.newsDetail(new NewsDetailRequest(this), id);

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        pvHeader = (PlusImageView) findViewById(R.id.pv_header);
    }

    /**
     * 请求返回
     *
     * @param response
     */
    private void response(ZhiHuNewsDetailResponse response) {
        toolbar.setTitle(response.getTitle());
        toolbarLayout.setTitle(Html.fromHtml("<font fontSize=12>" + response.getTitle() + "</font>"));
        pvHeader.load(response.getImage());
        String cssStr = "<link rel=\"stylesheet\" type=\"text/css\" href=\"%s\" />";
        String html = response.getBody();
        for (String cssLink : response.getCss()) {
            html = html + String.format(cssStr, cssLink);
        }
        html = html.replace("<div class=\"img-place-holder\"></div>", "");
        contentWebView.loadData(html, "text/html; charset=UTF-8", null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contentWebView.destroy();
    }

    /**
     * 请求知乎日报内容
     */
    private static class NewsDetailRequest extends Subscriber<ZhiHuNewsDetailResponse> {

        private final WeakReference<ZhiHuWebViewActivity> webViewActivity;

        public NewsDetailRequest(ZhiHuWebViewActivity webViewActivity) {
            this.webViewActivity = new WeakReference<>(webViewActivity);
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            LogUtils.e("error", "has an error", e);
        }

        @Override
        public void onNext(ZhiHuNewsDetailResponse zhiHuNewsDetailResponse) {
            if (webViewActivity.get() != null) {
                webViewActivity.get().response(zhiHuNewsDetailResponse);
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
