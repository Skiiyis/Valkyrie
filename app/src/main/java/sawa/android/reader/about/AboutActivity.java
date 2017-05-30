package sawa.android.reader.about;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import sawa.android.reader.R;
import sawa.android.reader.common.BaseActivity;
import sawa.android.reader.common.PlusImageView;

public class AboutActivity extends BaseActivity {

    private PlusImageView pvHeader;
    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarLayout;
    private AppBarLayout appBar;
    private TextView tvName;
    private TextView tvEmail;
    private TextView tvGithub;
    private PlusImageView pvPoint;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
        setSupportActionBar(toolbar);
        toolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        toolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        toolbarLayout.setTitle("关于作者");

        RxView.clicks(fab)
                .compose(this.bindToLifecycle())
                .flatMap(new Function<Object, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(Object o) throws Exception {
                        return crateDialog();
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer witch) throws Exception {
                        switch (witch) {
                            case -2:
                                ToastUtils.showShortToast("去视频");
                                break;
                            case -1:
                                ToastUtils.showShortToast("去拍照");
                                break;
                            default:
                                break;
                        }
                    }
                });
    }

    private Observable crateDialog() {
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(final ObservableEmitter<Object> e) throws Exception {
                AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
                builder.setMessage("如何获取内容？");
                builder.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        e.onNext(which);
                        e.onComplete();
                    }
                });
                builder.setNegativeButton("视频", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        e.onNext(which);
                        e.onComplete();
                    }
                });
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        e.onNext(0);
                        e.onComplete();
                    }
                });
                builder.show();
            }
        });
    }

    private void initView() {
        pvHeader = (PlusImageView) findViewById(R.id.pv_header);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvGithub = (TextView) findViewById(R.id.tv_github);
        pvPoint = (PlusImageView) findViewById(R.id.pv_point);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, AboutActivity.class);
        activity.startActivity(intent);
    }

}
