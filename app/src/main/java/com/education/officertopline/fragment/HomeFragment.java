package com.education.officertopline.fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.education.officertopline.R;
import com.education.officertopline.utils.Utils;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.internal.Util;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment {

    @Bind(R.id.home_indicator)ScrollIndicatorView home_indicator;
    @Bind(R.id.home_viewPager)ViewPager home_viewPager;

    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, mRootView);//绑定framgent
        initView(mRootView);
        return mRootView;
    }

    private void initView(View view) {
        inflate = LayoutInflater.from(getActivity().getApplicationContext());
        float unSelectSize = 12;
        float selectSize = unSelectSize * 1.3f;
        home_indicator.setOnTransitionListener(new OnTransitionTextListener().setColor(0xFF2196F3, Color.GRAY).setSize(selectSize, unSelectSize));
        home_indicator.setScrollBar(new ColorBar(getActivity().getApplicationContext(), 0xFF2196F3, 4));

        home_viewPager.setOffscreenPageLimit(3);
        indicatorViewPager = new IndicatorViewPager(home_indicator, home_viewPager);
        indicatorViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);//解绑
    }

    private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.view_tab_top, container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText("热门" + " " + position);
            int witdh = getTextWidth(textView);
            int padding = Utils.dip2px(getActivity().getApplicationContext(), 8f);
            //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
            //1.3f是根据上面字体大小变化的倍数1.3f设置
            textView.setWidth((int) (witdh * 1.3f) + padding);
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            RecommendFragment mainFragment = new RecommendFragment();
            Bundle bundle = new Bundle();
            bundle.putString(RecommendFragment.INTENT_STRING_TABNAME, "haha");
            bundle.putInt(RecommendFragment.INTENT_INT_POSITION, position);
            mainFragment.setArguments(bundle);
            return mainFragment;
        }

        private int getTextWidth(TextView textView) {
            if (textView == null) {
                return 0;
            }
            Rect bounds = new Rect();
            String text = textView.getText().toString();
            Paint paint = textView.getPaint();
            paint.getTextBounds(text, 0, text.length(), bounds);
            int width = bounds.left + bounds.width();
            return width;
        }
    }
}
