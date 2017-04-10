package sawa.android.reader.main.view_model;

import sawa.android.reader.common.BaseViewModel;
import sawa.android.reader.main.bean.DouBanFMChannel;
import sawa.android.reader.main.view_wrapper.DouBanFMViewWrapper;

/**
 * Created by hasee on 2017/3/26.
 */
public class DouBanFMViewModel extends BaseViewModel<DouBanFMViewWrapper, DouBanFMChannel.Channel> {

    public DouBanFMViewModel(DouBanFMViewWrapper view) {
        super(view);
    }

    @Override
    public void bind(DouBanFMChannel.Channel model) {
        view.coverImageView().load(model.getCover());
        view.textTextView().setText(model.getName());
    }
}
