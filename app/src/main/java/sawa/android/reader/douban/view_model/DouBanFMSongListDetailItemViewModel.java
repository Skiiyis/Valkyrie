package sawa.android.reader.douban.view_model;

import sawa.android.reader.common.BaseViewModel;
import sawa.android.reader.douban.bean.DouBanFMSongListDetail;
import sawa.android.reader.douban.view_wrapper.DouBanFMSongListDetailItemViewWrapper;

/**
 * Created by mc100 on 2017/4/12.
 */
public class DouBanFMSongListDetailItemViewModel extends BaseViewModel<DouBanFMSongListDetailItemViewWrapper, DouBanFMSongListDetail.Song> {

    public DouBanFMSongListDetailItemViewModel(DouBanFMSongListDetailItemViewWrapper view) {
        super(view);
    }

    @Override
    public void bind(DouBanFMSongListDetail.Song model) {
        view.coverImageView().load(model.getPicture());
        view.singerNameTextView().setText(model.getArtist());
        view.songNameTextView().setText(model.getTitle());
    }
}
