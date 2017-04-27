package sawa.android.reader.maxjia.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import sawa.android.live.inter.IRenderView;
import sawa.android.live.view.IjkVideoView;
import sawa.android.reader.R;
import sawa.android.reader.global.Application;

public class LiveDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        IjkVideoView liveView = (IjkVideoView) findViewById(R.id.live_view);
        liveView.setAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT);
        liveView.setVideoURI(Uri.parse(getIntent().getStringExtra("url")));
        liveView.start();
    }

    public static void launch(String url) {
        Context context = Application.get();
        Intent intent = new Intent(context, LiveDetailActivity.class);
        intent.putExtra("url", url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
