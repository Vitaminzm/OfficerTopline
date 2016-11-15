package com.education.officertopline.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.education.officertopline.R;
import com.education.officertopline.adapter.MyAdapter;
import com.education.officertopline.animators.FadeInAnimator;
import com.education.officertopline.animators.FadeInDownAnimator;
import com.education.officertopline.animators.FadeInUpAnimator;
import com.education.officertopline.animators.LandingAnimator;
import com.education.officertopline.animators.SlideInLeftAnimator;
import com.education.officertopline.interfaces.MyItemClickListener;
import com.education.officertopline.log.LogUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shizhefei.fragment.LazyFragment;

import java.util.ArrayList;

public class RecommendFragment extends LazyFragment {
    public static final String INTENT_STRING_TABNAME = "intent_String_tabName";
    public static final String INTENT_INT_POSITION = "intent_int_position";
    private String tabName;
    private int position;
    private TextView mEmptyView;
    private XRecyclerView mRecyclerView;

    private ArrayList<String> listData;
    private MyAdapter mAdapter;
    private int refreshTime = 0;
    private int times = 0;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        tabName = getArguments().getString(INTENT_STRING_TABNAME);
        position = getArguments().getInt(INTENT_INT_POSITION);
        setContentView(R.layout.fragment_recommend);
        mEmptyView = (TextView) findViewById(R.id.text_empty);
        mEmptyView.setText(tabName + " " + position + " 界面加载完毕");
        mRecyclerView = (XRecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setEmptyView(mEmptyView);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        //mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        listData.clear();
                        for (int i = 0; i < 15; i++) {
                            listData.add("item" + i + "after " + refreshTime + " times of refresh");
                        }
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                if (times < 2) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            for (int i = 0; i < 15; i++) {
                                listData.add("item" + (1 + listData.size()));
                            }
                            mRecyclerView.loadMoreComplete();
                            mAdapter.notifyDataSetChanged();
                        }
                    }, 1000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            for (int i = 0; i < 9; i++) {
                                listData.add("item" + (1 + listData.size()));
                            }
                            mRecyclerView.setNoMore(true);
                            mAdapter.notifyDataSetChanged();
                        }
                    }, 1000);
                }
                times++;
            }
        });
        listData = new  ArrayList<String>();
        for(int i = 0; i < 15 ;i++){
            listData.add("item" + i);
        }
        mAdapter = new MyAdapter(listData);
        mAdapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                LogUtil.i("lgs", "position--:" + postion);
                mAdapter.remove(postion);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new FadeInDownAnimator(new OvershootInterpolator(1f)));
        mRecyclerView.getItemAnimator().setAddDuration(500);
        mRecyclerView.getItemAnimator().setRemoveDuration(500);
        mRecyclerView.setRefreshing(true);
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        handler.removeMessages(1);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
//            textView.setVisibility(View.VISIBLE);
//            progressBar.setVisibility(View.GONE);
        }

        ;
    };
}