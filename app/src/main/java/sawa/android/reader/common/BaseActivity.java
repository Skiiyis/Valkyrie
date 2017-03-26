package sawa.android.reader.common;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.blankj.utilcode.utils.ToastUtils;

/**
 * Created by hasee on 2017/3/8.
 */
public class BaseActivity extends AppCompatActivity {

    public void showToast(String msg) {
        ToastUtils.showShortToast(msg);
    }

    public <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }
}
