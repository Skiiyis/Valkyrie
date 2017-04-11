package sawa.android.reader.main.view_model;

import sawa.android.reader.common.BaseViewModel;
import sawa.android.reader.main.bean.DouBanFMSongList;
import sawa.android.reader.main.view_wrapper.DouBanFMViewWrapper;

/**
 * Created by hasee on 2017/3/26.
 */
public class DouBanFMViewModel extends BaseViewModel<DouBanFMViewWrapper, DouBanFMSongList> {

    public DouBanFMViewModel(DouBanFMViewWrapper view) {
        super(view);
    }

    @Override
    public void bind(DouBanFMSongList model) {
        view.coverImageView().load(model.getCover());
        view.titleTextView().setText(model.getTitle());
        view.collectedCountTextView().setText(model.getCollected_count() + "人已收藏");
    }
}
