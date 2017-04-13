package sawa.android.reader.main.adapter;

/**
 * Created by mc100 on 2017/4/13.
 */

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import sawa.android.reader.R;
import sawa.android.reader.global.Application;
import sawa.android.reader.main.bean.ZhiHuNewsLatestResponse;
import sawa.android.reader.main.view_wrapper.ZhiHuMainBannerItemViewWrapper;
import sawa.android.reader.zhihu.activity.ZhiHuWebViewActivity;

/**
 * 知乎banner的adapter
 */
public final class ZhiHuBannerViewPagerAdapter extends PagerAdapter {

    private final List<ZhiHuNewsLatestResponse.Story> topStories;

    public ZhiHuBannerViewPagerAdapter(List<ZhiHuNewsLatestResponse.Story> topStories) {
        this.topStories = topStories;
    }

    @Override
    public int getCount() {
        return topStories.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ZhiHuMainBannerItemViewWrapper view = new ZhiHuMainBannerItemViewWrapper(LayoutInflater.from(Application.get()).inflate(R.layout.item_banner__view_zhihu__recycleview, container, false));
        ZhiHuNewsLatestResponse.Story story = topStories.get(position);
        view.titleTextView().setText(story.getTitle());
        view.thumbnailPlusImageView().load(story.getImage());
        view.rootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZhiHuWebViewActivity.launch(topStories.get(position).getId().toString());
            }
        });
        container.addView(view.rootView());
        return view.rootView();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
