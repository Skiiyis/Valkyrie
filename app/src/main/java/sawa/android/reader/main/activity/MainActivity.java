package sawa.android.reader.main.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import sawa.android.reader.R;
import sawa.android.reader.common.BaseActivity;
import sawa.android.reader.common.LifeCycleViewHelper;
import sawa.android.reader.global.Application;
import sawa.android.reader.main.view.ZhiHuView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LifeCycleViewHelper helper = new LifeCycleViewHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();*/

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TabLayout mainTabLayout = findView(R.id.tl_tablayout);
        ViewPager mainViewPager = findView(R.id.vp_main_container);

        mainTabLayout.setupWithViewPager(mainViewPager);
        mainViewPager.setAdapter(new MainFragmentAdapter(this));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_fav) {
        } else if (id == R.id.nav_setting) {
        } else if (id == R.id.nav_night_mode) {
        } else if (id == R.id.nav_author) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 首页Adapter
     */
    public static final class MainFragmentAdapter extends PagerAdapter {

        private WeakReference<MainActivity> activityRef;

        public MainFragmentAdapter(MainActivity activity) {
            activityRef = new WeakReference<MainActivity>(activity);
        }

        private static final String[] title = new String[]{"知乎", "FM", "乐子", "Android"};

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ZhiHuView zhiHuView = new ZhiHuView(Application.get());
            if (activityRef.get() != null) {
                activityRef.get().helper.addLifeCycleView(zhiHuView);
            }
            container.addView(zhiHuView);
            return zhiHuView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        helper.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        helper.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        helper.onStop();
    }
}
