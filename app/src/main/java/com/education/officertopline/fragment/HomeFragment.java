package com.education.officertopline.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.education.officertopline.R;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import butterknife.Bind;
import butterknife.ButterKnife;

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
            return 10;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.view_tab_top, container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText("1" + " " + position);
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
    }
}
