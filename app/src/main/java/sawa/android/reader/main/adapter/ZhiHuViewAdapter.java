package sawa.android.reader.main.adapter;

/**
 * Created by mc100 on 2017/4/13.
 */

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConvertUtils;

import java.util.List;

import sawa.android.reader.R;
import sawa.android.reader.global.Application;
import sawa.android.reader.main.bean.ZhiHuNewsLatestResponse;
import sawa.android.reader.main.view_wrapper.ZhiHuStoryViewWrapper;
import sawa.android.reader.zhihu.activity.ZhiHuWebViewActivity;

/**
 * 首页知乎recycle的Adapter
 */
public final class ZhiHuViewAdapter extends RecyclerView.Adapter {

    private final ZhiHuNewsLatestResponse response;

    public ZhiHuViewAdapter(ZhiHuNewsLatestResponse response) {
        this.response = response;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            ViewPager itemView = new ViewPager(Application.get());
            itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtils.dp2px(240f)));
            return new ViewHolder(itemView) {
            };
        }

        if (viewType == 1) {
            TextView itemView = new TextView(Application.get());
            itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtils.dp2px(40f)));
            return new ViewHolder(itemView) {
            };
        }

        /**
         * 使用这种inflate方法可以match_parent
         */
        View itemView = LayoutInflater.from(Application.get()).inflate(R.layout.item_story__view_zhihu__recycleview, parent, false);
        return new ZhiHuStoryViewWrapper(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {
        if (position == 0) {
            ((ViewPager) vh.itemView).setAdapter(new ZhiHuBannerViewPagerAdapter(response.getTop_stories()));
            return;
        }

        if (position == 1) {
            TextView itemView = (TextView) vh.itemView;
            itemView.setGravity(Gravity.CENTER_VERTICAL);
            itemView.setTextColor(Color.parseColor("#666666"));
            itemView.setPadding(ConvertUtils.dp2px(12f), 0, 0, 0);
            itemView.setText("今日热闻");
            return;
        }

        final ZhiHuStoryViewWrapper view = ((ZhiHuStoryViewWrapper) vh);
        final ZhiHuNewsLatestResponse.Story story = response.getStories().get(position - 1);
        view.titleTextView().setText(story.getTitle());
        List<String> images = story.getImages();
        if (images != null && images.size() != 0) {
            view.thumbnailImageView().load(images.get(0));
        }
        view.titleTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZhiHuWebViewActivity.launch(story.getId().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return response.getStories().size() + 1;
    }
}
