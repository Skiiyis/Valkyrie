package sawa.android.reader.main.ViewModel;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConvertUtils;

import java.util.List;

import sawa.android.reader.R;
import sawa.android.reader.common.BaseViewModel;
import sawa.android.reader.common.PlusImageView;
import sawa.android.reader.global.Application;
import sawa.android.reader.main.ViewDelegate.ZhiHuViewDelegate;
import sawa.android.reader.main.bean.ZhiHuNewsLatestResponse;
import sawa.android.reader.zhihu.activity.ZhiHuWebViewActivity;

/**
 * Created by hasee on 2017/3/11.
 */
public class ZhiHuViewModel extends BaseViewModel<ZhiHuViewDelegate, ZhiHuNewsLatestResponse> {

    public ZhiHuViewModel(ZhiHuViewDelegate view) {
        super(view);
    }

    @Override
    public void bind(ZhiHuNewsLatestResponse model) {
        view.contentRecycleView().setLayoutManager(new LinearLayoutManager(Application.get()));
        view.contentRecycleView().setAdapter(new ZhiHuViewAdapter(model));
    }

    /**
     * 首页知乎recycle的Adapter
     */
    private static final class ZhiHuViewAdapter extends RecyclerView.Adapter {

        private final ZhiHuNewsLatestResponse response;

        public ZhiHuViewAdapter(ZhiHuNewsLatestResponse response) {
            this.response = response;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                ViewPager itemView = new ViewPager(Application.get());
                itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtils.dp2px(240f)));
                return new ZhiHuContainerViewHolder(itemView);
            }

            if (viewType == 1) {
                TextView itemView = new TextView(Application.get());
                itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtils.dp2px(40f)));
                return new ZhiHuContainerViewHolder(itemView);
            }

            /**
             * 使用这种inflate方法可以match_parent
             */
            View itemView = LayoutInflater.from(Application.get()).inflate(R.layout.item_story__view_zhihu__recycleview, parent, false);
            return new ZhiHuStoryViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder vh, int position) {
            if (position == 0) {
                final ZhiHuContainerViewHolder holder = ((ZhiHuContainerViewHolder) vh);
                ViewPager itemView = (ViewPager) holder.itemView;
                itemView.setAdapter(new ZhihuBannerViewPagerAdapter(response.getTop_stories()));
                return;
            }

            if (position == 1) {
                final ZhiHuContainerViewHolder holder = ((ZhiHuContainerViewHolder) vh);
                TextView itemView = (TextView) holder.itemView;
                itemView.setGravity(Gravity.CENTER_VERTICAL);
                itemView.setTextColor(Color.parseColor("#666666"));
                itemView.setPadding(ConvertUtils.dp2px(12f), 0, 0, 0);
                itemView.setText("今日热闻");
                return;
            }

            final ZhiHuStoryViewHolder holder = ((ZhiHuStoryViewHolder) vh);
            final ZhiHuNewsLatestResponse.Story story = response.getStories().get(position - 1);
            holder.titleTextView.setText(story.getTitle());
            List<String> images = story.getImages();
            if (images != null && images.size() != 0) {
                holder.thumbailImageView.load(images.get(0));
            }
            holder.titleTextView.setOnClickListener(new View.OnClickListener() {
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

    /**
     * 知乎banner的adapter
     */
    private static final class ZhihuBannerViewPagerAdapter extends PagerAdapter {

        private final List<ZhiHuNewsLatestResponse.Story> topStories;

        public ZhihuBannerViewPagerAdapter(List<ZhiHuNewsLatestResponse.Story> topStories) {
            this.topStories = topStories;
        }

        @Override
        public int getCount() {
            return topStories.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = LayoutInflater.from(Application.get()).inflate(R.layout.item_banner__view_zhihu__recycleview, container, false);
            final ZhiHuNewsLatestResponse.Story story = topStories.get(position);
            ((TextView) itemView.findViewById(R.id.tv_title)).setText(story.getTitle());
            ((PlusImageView) itemView.findViewById(R.id.iv_background)).load(story.getImage());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ZhiHuWebViewActivity.launch(topStories.get(position).getId().toString());
                }
            });
            container.addView(itemView);
            return itemView;
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

    /**
     * 知乎ViewHolder
     */
    private static final class ZhiHuContainerViewHolder extends RecyclerView.ViewHolder {
        public ZhiHuContainerViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 知乎ViewHolder
     */
    private static final class ZhiHuStoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleTextView;
        private final PlusImageView thumbailImageView;

        public ZhiHuStoryViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.tv_title);
            thumbailImageView = (PlusImageView) itemView.findViewById(R.id.iv_thumbnail);
        }
    }
}
