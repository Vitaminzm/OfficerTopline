package com.education.officertopline.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.education.officertopline.R;
import com.education.officertopline.app.AppManager;
import com.education.officertopline.fragment.HomeFragment;
import com.education.officertopline.log.LogUtil;
import com.education.officertopline.utils.FragmentTabUtils;
import com.education.officertopline.utils.ToastUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity implements FragmentTabUtils.OnRgsExtraCheckedChangedListener {

    @Bind(R.id.fragment_container)FrameLayout fragment_container;
    @Bind(R.id.mian_rgs)RadioGroup mian_rgs;
    @Bind(R.id.main_home_r)RadioButton main_home_r;
    @Bind(R.id.view_stub)ViewStub view_stub;
    private boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    static class MyHandler extends Handler {
        WeakReference<FragmentActivity> mActivity;

        MyHandler(FragmentActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentActivity theActivity = mActivity.get();
            switch (msg.what) {
                case ToastUtils.TOAST_WHAT:
                    ToastUtils.showtaostbyhandler(theActivity,msg);
                    break;
            }
        }
    }

    MyHandler handler = new MyHandler(this);
    private long exitTime;

    private ArrayList<Fragment> fragments = new ArrayList<>();

    protected void initData() {
        fragments.add(HomeFragment.newInstance());
        fragments.add(HomeFragment.newInstance());
        fragments.add(HomeFragment.newInstance());
        new FragmentTabUtils(getSupportFragmentManager(), fragments, R.id.fragment_container, mian_rgs, this);
        main_home_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    ((HomeFragment)fragments.get(0)).refushData();
                    LogUtil.i("lgs","222222-----");
                }else {
                    flag = true;
                }
            }
        });
        flag = true;
    }

    protected void initView() {
        setContentView(R.layout.activity_main);
        AppManager.addActivity(this);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        AppManager.delActivity(this);
    }


    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtils.sendtoastbyhandler(handler, getString(R.string.quit_again));
            exitTime = System.currentTimeMillis();
        } else {//注销网络监听
            AppManager.getAppManager().AppExit();
        }
    }

    @Override
    public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
        LogUtil.i("lgs","11111-----");
        flag = false;
    }
}
