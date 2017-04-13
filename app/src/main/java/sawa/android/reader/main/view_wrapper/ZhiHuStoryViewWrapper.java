package sawa.android.reader.main.view_wrapper;

/**
 * Created by mc100 on 2017/4/13.
 */

import android.view.View;
import android.widget.TextView;

import sawa.android.reader.R;
import sawa.android.reader.common.BaseViewWrapper;
import sawa.android.reader.common.PlusImageView;

/**
 * 知乎StoryViewHolder
 */
public class ZhiHuStoryViewWrapper extends BaseViewWrapper {

    private final TextView titleTextView;
    private final PlusImageView thumbnailImageView;

    public ZhiHuStoryViewWrapper(View itemView) {
        super(itemView);
        titleTextView = (TextView) itemView.findViewById(R.id.tv_title);
        thumbnailImageView = (PlusImageView) itemView.findViewById(R.id.iv_thumbnail);
    }

    public TextView titleTextView() {
        return titleTextView;
    }

    public PlusImageView thumbnailImageView() {
        return thumbnailImageView;
    }
}
