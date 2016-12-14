package com.education.officertopline.fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.education.officertopline.R;
import com.education.officertopline.app.ConstantData;
import com.education.officertopline.db.dao.ChannelNewsListDao;
import com.education.officertopline.entity.ToplineChannelInfo;
import com.education.officertopline.http.HttpActionHandle;
import com.education.officertopline.http.HttpRequestUtil;
import com.education.officertopline.http.HttpStringClient;
import com.education.officertopline.log.LogUtil;
import com.education.officertopline.result.ToplineChannelListResult;
import com.education.officertopline.utils.ArithDouble;
import com.education.officertopline.utils.SpSaveUtils;
import com.education.officertopline.utils.ToastUtils;
import com.education.officertopline.utils.Utils;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.ArrayList;
import java.util.List;

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
    private MyIndicatorAdapter myIndicatorAdapter;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private ArrayList<ToplineChannelInfo> channelList;

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
                ToastUtils.sendtoastbyhandler(handler, "dianjiale ");
//                channel.remove(0);
//             //   home_viewPager.setOffscreenPageLimit(viewPagerdatas.size());
//                myIndicatorAdapter.notifyDataSetChanged();
//                myFragmentPagerAdapter.notifyDataSetChanged();

            }
        });
        inflate = LayoutInflater.from(getActivity().getApplicationContext());
        float unSelectSize = 12;
        float selectSize = unSelectSize * 1.3f;
        home_indicator.setOnTransitionListener(new OnTransitionTextListener().setColor(getResources().getColor(R.color.themeRed), getResources().getColor(R.color.deepgray)).setSize(selectSize, unSelectSize));
        home_indicator.setScrollBar(new ColorBar(getActivity().getApplicationContext(), getResources().getColor(R.color.themeRed), 4));


        channelList =new ArrayList<>();
        home_viewPager.setOffscreenPageLimit(3);
        myIndicatorAdapter = new MyIndicatorAdapter(channelList);
        home_indicator.setAdapter(myIndicatorAdapter);
        home_indicator.setOnItemSelectListener(new Indicator.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View selectItemView, int select, int preSelect) {
                home_viewPager.setCurrentItem(select);
            }
        });
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), channelList);
        home_viewPager.setAdapter(myFragmentPagerAdapter);
        home_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == home_indicator.getCurrentItem()) {
                    return;
                }
                home_indicator.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);//解绑
    }

    private class MyIndicatorAdapter extends Indicator.IndicatorAdapter {

        ArrayList<ToplineChannelInfo> dataList;
        public MyIndicatorAdapter(ArrayList<ToplineChannelInfo> datalist) {
            super();
            dataList = datalist;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.view_tab_top, container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(dataList.get(position).getChannelName());
            int witdh = getTextWidth(textView);
            int padding = Utils.dip2px(getActivity().getApplicationContext(), 8f);
            //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
            //1.3f是根据上面字体大小变化的倍数1.3f设置
            textView.setWidth((int) (witdh * 1.3f) + padding);
            return convertView;
        }
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter{
        List<ToplineChannelInfo> dataList;
        FragmentManager fm;
        Fragment currentFragment;
        public MyFragmentPagerAdapter(FragmentManager fm, List<ToplineChannelInfo> fragments){
            super(fm);
            this.fm = fm;
            dataList = fragments;
        }
        public int getItemPosition(Object object){
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            LogUtil.i("lgs","getItem:"+position);
//            // dataList.get(currentPageIndex).onPause(); // 调用切换前Fargment的onPause()
//            dataList.get(currentPageIndex).onStop(); // 调用切换前Fargment的onStop()
//            if(dataList.get(position).isAdded()){
//                dataList.get(position).onStart(); // 调用切换后Fargment的onStart()
//                //     dataList.get(position).onResume(); // 调用切换后Fargment的onResume()
//            }
//            currentPageIndex = position;
//            LazyFragment mainFragment = dataList.get(position);
            RecommendFragment mainFragment = new RecommendFragment();
            Bundle bundle = new Bundle();
            bundle.putString(RecommendFragment.INTENT_STRING_CHANNELCODE, dataList.get(position).getChannelCode());
            mainFragment.setArguments(bundle);
            return mainFragment;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return  ArithDouble.parseLong(dataList.get(position).getChannelCode()) + position;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            currentFragment = (Fragment) object;
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //得到缓存的fragment
            LogUtil.i("lgs","instantiateItem:"+position);
            Fragment fragment = (Fragment)super.instantiateItem(container, position);
////            //得到tag ❶
//            String fragmentTag = fragment.getTag();
//          //  if (fragmentsUpdateFlag[position %fragmentsUpdateFlag.length]) {
//                //如果这个fragment需要更新
//                FragmentTransaction ft =fm.beginTransaction();
//                //移除旧的fragment
//                ft.remove(fragment);
//                //换成新的fragment
//                fragment =fragments[position %fragments.length];
//                //添加新fragment时必须用前面获得的tag ❶
//                ft.add(container.getId(), fragment, fragmentTag);
//                ft.attach(fragment);
//                ft.commit();
//                //复位更新标志
//           //     fragmentsUpdateFlag[position %fragmentsUpdateFlag.length] =false;
//           // }
            return fragment;
        }
         public Fragment getCurrentFragment(){
             return currentFragment;
         }
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

    public void initData(){
        List<ToplineChannelInfo> dataList = (List<ToplineChannelInfo>) SpSaveUtils.getObject(getActivity().getApplicationContext(), ConstantData.TOPLINE_CHANNEL_LIST);
        if(dataList!= null && dataList.size() > 0){
            channelList.addAll(dataList);
            myIndicatorAdapter.notifyDataSetChanged();
            myFragmentPagerAdapter.notifyDataSetChanged();
        }else{
            ToplineChannelInfo i= new ToplineChannelInfo();
            i.setChannelCode("1");
            i.setChannelName("测试");
            channelList.add(i);
            ToplineChannelInfo i1= new ToplineChannelInfo();
            i1.setChannelCode("2");
            i1.setChannelName("测试2");
            channelList.add(i1);
            myIndicatorAdapter.notifyDataSetChanged();
            myFragmentPagerAdapter.notifyDataSetChanged();
//            HttpRequestUtil.getinstance().getToplineChannel(HTTP_TASK_KEY, null, ToplineChannelListResult.class, new HttpActionHandle<ToplineChannelListResult>() {
//                @Override
//                public void handleActionError(String actionName, String errmsg) {
//
//                }
//
//                @Override
//                public void handleActionSuccess(String actionName, ToplineChannelListResult result) {
//                    if(ConstantData.HTTP_RESPONSE_OK.equals(result.getCode())){
//                        if(result.getList()!= null && result.getList().size() > 0){
//                            channelList.addAll(result.getList());
//                            myIndicatorAdapter.notifyDataSetChanged();
//                            myFragmentPagerAdapter.notifyDataSetChanged();
//                        }
//                    }else{
//                        ToastUtils.sendtoastbyhandler(handler,result.getMsg());
//                    }
//                }
//            });
        }

    }
    public void refushData(){
        Fragment fragment =  myFragmentPagerAdapter.getCurrentFragment();
        if(fragment == null){
            LogUtil.i("lgs","refushData failed" );
        }else{
            ((RecommendFragment)fragment).refushData();
            LogUtil.i("lgs","refushData succeess" );
        }
    }
}
