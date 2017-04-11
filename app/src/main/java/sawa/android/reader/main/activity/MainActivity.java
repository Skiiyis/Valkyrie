package sawa.android.reader.main.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import sawa.android.reader.R;
import sawa.android.reader.common.BaseActivity;
import sawa.android.reader.common.BaseView;
import sawa.android.reader.common.LifeCycleViewHelper;
import sawa.android.reader.global.Application;
import sawa.android.reader.main.view.DouBanFMView;
import sawa.android.reader.main.view.ZhiHuView;
import sawa.android.reader.main.view_wrapper.MainActivityViewWrapper;

public class MainActivity extends BaseActivity {

    private LifeCycleViewHelper helper = new LifeCycleViewHelper();
    private int currentChecked = 0;

    /*
    int[][] states = new int[][]{{-android.R.attr.state_checked, android.R.attr.state_checked}};
    private int[] colors = new int[]{ContextCompat.getColor(Application.get(), R.color.gray_D8D8D8), ContextCompat.getColor(Application.get(), R.color.color_primary)};
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final MainActivityViewWrapper mainActivityViewWrapper = new MainActivityViewWrapper(View.inflate(this, R.layout.activity_main, null));
        setContentView(mainActivityViewWrapper.getRootView());

        mainActivityViewWrapper.containerViewPager().setAdapter(new MainFragmentAdapter(this));
        mainActivityViewWrapper.buttonRadioGroup().setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                for (int j = 0; j < radioGroup.getChildCount(); j++) {
                    if (((RadioButton) radioGroup.getChildAt(j)).isChecked()) {
                        mainActivityViewWrapper.containerViewPager().setCurrentItem(j);
                        currentChecked = j;
                        break;
                    }
                }
            }
        });
        ((RadioButton) mainActivityViewWrapper.buttonRadioGroup().getChildAt(currentChecked)).setChecked(true);
        mainActivityViewWrapper.containerViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /*
                int[] color = new int[]{colors[0], ColorUtil.centerColor(colors[0], colors[1], positionOffsetPixels)};
                ColorStateList colorStateList = new ColorStateList(states, color);
                ((RadioButton) mainActivityViewWrapper.buttonRadioGroup().getChildAt(currentChecked)).setCompoundDrawableTintList(colorStateList);
                */
            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton) mainActivityViewWrapper.buttonRadioGroup().getChildAt(position)).setChecked(true);
                currentChecked = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 首页Adapter
     */
    public static final class MainFragmentAdapter extends PagerAdapter {

        private WeakReference<MainActivity> activityRef;

        public MainFragmentAdapter(MainActivity activity) {
            activityRef = new WeakReference<>(activity);
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
            BaseView childView = null;
            switch (position) {
                case 0:
                    childView = new ZhiHuView(Application.get());
                    break;
                case 1:
                    childView = new DouBanFMView(Application.get());
                    break;
                default:
                    TextView child = new TextView(Application.get());
                    container.addView(child);
                    return child;
            }
            if (activityRef.get() != null) {
                activityRef.get().helper.addLifeCycleView(childView);
            }
            container.addView(childView);
            return childView;
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
