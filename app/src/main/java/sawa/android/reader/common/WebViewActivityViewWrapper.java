package sawa.android.reader.common;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import sawa.android.reader.R;

/**
 * Created by mc100 on 2017/4/13.
 */

public class WebViewActivityViewWrapper extends BaseViewWrapper {

    public WebViewActivityViewWrapper(View rootView) {
        super(rootView);
    }

    public WebView contentWebView() {
        return (WebView) rootView().findViewById(R.id.wv_content);
    }

    public PlusImageView headerImageView() {
        return (PlusImageView) rootView().findViewById(R.id.pv_header);
    }

    public CollapsingToolbarLayout toolbarLayout() {
        return (CollapsingToolbarLayout) rootView().findViewById(R.id.toolbar_layout);
    }

    public Toolbar toolbar() {
        return (Toolbar) rootView().findViewById(R.id.toolbar);
    }
}
