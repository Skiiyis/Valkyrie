package sawa.android.reader.main.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import sawa.android.reader.R;
import sawa.android.reader.common.BaseActivity;
import sawa.android.reader.main.fragment.DouBanFMMainFragment;
import sawa.android.reader.main.fragment.LiveMainFragment;
import sawa.android.reader.main.fragment.MineMainFragment;
import sawa.android.reader.main.fragment.ZhiHuMainFragment;
import sawa.android.reader.main.view_wrapper.MainActivityViewWrapper;

public class MainActivity extends BaseActivity {

    private int currentChecked = 0;

    /*
    int[][] states = new int[][]{{-android.R.attr.state_checked, android.R.attr.state_checked}};
    private int[] colors = new int[]{ContextCompat.getColor(Application.get(), R.color.gray_D8D8D8), ContextCompat.getColor(Application.get(), R.color.color_primary)};
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final MainActivityViewWrapper mainActivityViewWrapper = new MainActivityViewWrapper(View.inflate(this, R.layout.activity_main, null));
        setContentView(mainActivityViewWrapper.rootView());

        mainActivityViewWrapper.containerViewPager().setAdapter(new MainFragmentAdapter(getSupportFragmentManager()));
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
        mainActivityViewWrapper.containerViewPager().setOffscreenPageLimit(3);
    }

    /**
     * 首页Adapter
     */
    public static final class MainFragmentAdapter extends FragmentPagerAdapter {

        public MainFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ZhiHuMainFragment();
                case 1:
                    return new DouBanFMMainFragment();
                case 2:
                    return new LiveMainFragment();
                default:
                    return new MineMainFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
