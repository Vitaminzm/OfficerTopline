package com.education.officertopline.fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.education.officertopline.R;
import com.education.officertopline.utils.ToastUtils;
import com.education.officertopline.utils.Utils;
import com.shizhefei.fragment.LazyFragment;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.ArrayList;

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
    @Bind(R.id.image_add)ImageView image_add;

    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;
    private ArrayList<LazyFragment> viewPagerdatas;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public HomeFragment() {
    }

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ToastUtils.TOAST_WHAT:
                    ToastUtils.showtaostbyhandler(getActivity(), msg);
                    break;

                case 2:

                    break;

                default:
                    break;
            }
        };
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, mRootView);//绑定framgent
        initView(mRootView);
        return mRootView;
    }

    private void initView(View view) {
        image_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.sendtoastbyhandler(handler,"dianjiale ");
            }
        });
        inflate = LayoutInflater.from(getActivity().getApplicationContext());
        float unSelectSize = 12;
        float selectSize = unSelectSize * 1.3f;
        home_indicator.setOnTransitionListener(new OnTransitionTextListener().setColor(0xFF2196F3, Color.GRAY).setSize(selectSize, unSelectSize));
        home_indicator.setScrollBar(new ColorBar(getActivity().getApplicationContext(), 0xFF2196F3, 4));


        viewPagerdatas=new ArrayList<>();
        int i = 0;
        for (i= 0; i<10;i++){
            RecommendFragment mainFragment = new RecommendFragment();
            Bundle bundle = new Bundle();
            bundle.putString(RecommendFragment.INTENT_STRING_TABNAME, "haha");
            bundle.putInt(RecommendFragment.INTENT_INT_POSITION, i);
            mainFragment.setArguments(bundle);
            viewPagerdatas.add(mainFragment);
        }
        home_viewPager.setOffscreenPageLimit(viewPagerdatas.size());
        indicatorViewPager = new IndicatorViewPager(home_indicator, home_viewPager);
        indicatorViewPager.setAdapter(new MyAdapter(getChildFragmentManager(),viewPagerdatas));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);//解绑
    }


    private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        ArrayList<LazyFragment> dataList;
        private int currentPageIndex =0;
        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }
        public MyAdapter(FragmentManager fragmentManager, ArrayList<LazyFragment> datalist) {
            super(fragmentManager);
            dataList = datalist;
        }

        @Override
        public int getCount() {
            return dataList.size();
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
           // dataList.get(currentPageIndex).onPause(); // 调用切换前Fargment的onPause()
                     dataList.get(currentPageIndex).onStop(); // 调用切换前Fargment的onStop()
            if(dataList.get(position).isAdded()){
               dataList.get(position).onStart(); // 调用切换后Fargment的onStart()
           //     dataList.get(position).onResume(); // 调用切换后Fargment的onResume()
                 }
            currentPageIndex = position;
            LazyFragment mainFragment = dataList.get(position);
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
