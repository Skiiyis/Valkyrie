package sawa.android.reader.main.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;

import rx.Subscriber;
import sawa.android.reader.R;
import sawa.android.reader.common.BaseView;
import sawa.android.reader.http.ZhiHuApi;
import sawa.android.reader.main.view_wrapper.ViewRecycleViewWrapper;
import sawa.android.reader.main.view_model.ZhiHuViewModel;
import sawa.android.reader.main.bean.ZhiHuNewsLatestResponse;

/**
 * Created by hasee on 2017/3/11.
 */
public class ZhiHuView extends BaseView {

    private ViewRecycleViewWrapper viewRecycleViewWrapper;
    private ZhiHuViewModel zhiHuViewModel;

    public ZhiHuView(Context context) {
        super(context);
    }

    public ZhiHuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZhiHuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onInflate(View contentView) {
        viewRecycleViewWrapper = new ViewRecycleViewWrapper(contentView);
        zhiHuViewModel = new ZhiHuViewModel(viewRecycleViewWrapper);
        ZhiHuApi.newsLatest(new NewsLatestRequest(this));
    }

    public void response(ZhiHuNewsLatestResponse response) {
        zhiHuViewModel.bind(response);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_recycle_view;
    }

    /**
     * 请求知乎日报最新内容列表
     */
    private static class NewsLatestRequest extends Subscriber<ZhiHuNewsLatestResponse> {

        private final WeakReference<ZhiHuView> zhiHuView;

        public NewsLatestRequest(ZhiHuView zhiHuView) {
            this.zhiHuView = new WeakReference<>(zhiHuView);
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(ZhiHuNewsLatestResponse zhiHuNewsLatestResponse) {
            if (zhiHuView.get() != null) {
                zhiHuView.get().response(zhiHuNewsLatestResponse);
            }
        }
    }
}
