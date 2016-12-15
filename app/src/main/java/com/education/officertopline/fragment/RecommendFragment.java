package com.education.officertopline.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.education.officertopline.R;
import com.education.officertopline.adapter.NewsListInfoAdapter;
import com.education.officertopline.animators.FadeInDownAnimator;
import com.education.officertopline.app.ConstantData;
import com.education.officertopline.db.dao.ChannelNewsListDao;
import com.education.officertopline.entity.ToplineNewsListInfo;
import com.education.officertopline.http.HttpActionHandle;
import com.education.officertopline.http.HttpRequestUtil;
import com.education.officertopline.http.HttpStringClient;
import com.education.officertopline.http.ImageLoaderUtils;
import com.education.officertopline.interfaces.MyItemClickListener;
import com.education.officertopline.log.LogUtil;
import com.education.officertopline.result.ToplineNewsListResult;
import com.education.officertopline.utils.NewsInfoComparator;
import com.education.officertopline.utils.ToastUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shizhefei.fragment.LazyFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RecommendFragment extends LazyFragment {

    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();
    public static final String INTENT_STRING_CHANNELCODE = "intent_string_channelcode";
    private String tabName;
    private String channelCode;
    private TextView mEmptyView;
    private TextView text_msg;
    private XRecyclerView mRecyclerView;

    private ArrayList<ToplineNewsListInfo> listData;
    private NewsListInfoAdapter mAdapter;
    private int refreshTime = 0;
    private int times = 0;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        channelCode = getArguments().getString(INTENT_STRING_CHANNELCODE);
        setContentView(R.layout.fragment_recommend);
        text_msg = (TextView) findViewById(R.id.text_msg);
        mEmptyView = (TextView) findViewById(R.id.text_empty);
        mEmptyView.setText(tabName + " " + channelCode + " 界面加载完毕");
        mRecyclerView = (XRecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);


        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        //mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if(listData.size() > 0)
                    getNewsListFromHttp(listData.get(0).getId());   //refresh data here
                else
                    getNewsListFromHttp("0");   //refresh data here
            }

            @Override
            public void onLoadMore() {
                if (times < 2) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            for (int i = 0; i < 15; i++) {
                                ToplineNewsListInfo info = new ToplineNewsListInfo();
                                info.setFirstTime("2016-12-12 09:09");
                                info.setTitle("台湾回归 "+i);
                                info.setNewsType(i+"");
                                info.setCommentNum(i+"评论");
                                listData.add(info);
                            }
                            mRecyclerView.loadMoreComplete();
                            mAdapter.notifyDataSetChanged();
                        }
                    }, 1000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            for (int i = 0; i < 9; i++) {
                                ToplineNewsListInfo info = new ToplineNewsListInfo();
                                info.setFirstTime("2016-12-12 09:09");
                                info.setTitle("台湾回归 "+i);
                                info.setNewsType(i+"");
                                info.setCommentNum(i+"评论");
                                listData.add(info);
                            }
                            mRecyclerView.setNoMore(true);
                            mAdapter.notifyDataSetChanged();
                        }
                    }, 1000);
                }
                times++;
            }
        });
        listData = new  ArrayList<ToplineNewsListInfo>();
//        for(int i = 0; i < 15 ;i++){
//            ToplineNewsListInfo info = new ToplineNewsListInfo();
//            info.setFirstTime("2016-12-12 09:09");
//            info.setTitle("台湾回归 " + i);
//            info.setNewsType(i + "");
//            info.setCommentNum(i + "评论");
//            info.setPic("https://ss0.baidu.com/73F1bjeh1BF3odCf/it/u=3490816059,3779854493&fm=96&s=B3A7FC04CEB11882160624DC03001099");
//            listData.add(info);
//        }
        mAdapter = new NewsListInfoAdapter(getApplicationContext(), listData, new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LogUtil.i("lgs", "onItemClickposition--:" + position + "getHeaderViewCount:" + mRecyclerView.getHeaderViewCount());
                // mAdapter.remove(position- mRecyclerView.getHeaderViewCount(), mRecyclerView.getHeaderViewCount());
            }

            @Override
            public void onItemInClick(View view, int position) {
                LogUtil.i("lgs", "onItemInClickposition--:" + position + "getHeaderViewCount:"+ mRecyclerView.getHeaderViewCount());
            }
        });

        //View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item,null);
        //mRecyclerView.addHeaderView(view);
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new FadeInDownAnimator(new OvershootInterpolator(1f)));
        mRecyclerView.getItemAnimator().setAddDuration(500);
        mRecyclerView.getItemAnimator().setRemoveDuration(500);
        LogUtil.d("lgs", "Fragment 将要创建View " + channelCode);
        onRefresh();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(RecyclerView.SCROLL_STATE_SETTLING == newState){
                    ImageLoaderUtils.getInstance().pauseLoad(getApplicationContext());
                }else{
                    ImageLoaderUtils.getInstance().resumeLoad(getApplicationContext());
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void onRefresh() {
        ChannelNewsListDao dao = new ChannelNewsListDao(getApplicationContext());
        List<ToplineNewsListInfo> datas = dao.getOfflineOrderInfo(channelCode, ConstantData.PAGESIZE);
        if(datas.size() > 0){
            listData.addAll(datas);
            mAdapter.notifyDataSetChanged();
        }else{
            mRecyclerView.setRefreshing(true);
        }
    }

    public void getNewsListFromHttp(String index){
        Map<String, String> map = new TreeMap<>();
        map.put("channelCode", channelCode);
        map.put("pageStartIndex", index);
        map.put("pageSize", ConstantData.PAGESIZE + "");
        HttpRequestUtil.getinstance().getToplineNewsList(HTTP_TASK_KEY, map, ToplineNewsListResult.class, new HttpActionHandle<ToplineNewsListResult>() {

            @Override
            public void handleActionFinish() {
                mRecyclerView.refreshComplete();
            }

            @Override
            public void handleActionError(String actionName, String errmsg) {
                ToastUtils.sendtoastbyhandler(handler, errmsg);
            }

            @Override
            public void handleActionSuccess(String actionName, ToplineNewsListResult result) {
                if(ConstantData.HTTP_RESPONSE_OK.equals(result.getCode())){
                    if(result.getList()!= null && result.getList().size() > 0){
                        text_msg.setVisibility(View.VISIBLE);
                        text_msg.setText("更新了"+result.getList().size()+"条新闻");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                text_msg.setVisibility(View.GONE);
                            }
                        }, 1000);
                        ChannelNewsListDao dao =  new ChannelNewsListDao(getApplicationContext());
                        dao.addOrderGoodsInfo(channelCode, result.getList());
                        Collections.sort(result.getList(), new NewsInfoComparator());
                        listData.addAll(0,result.getList());
                        mAdapter.notifyDataSetChanged();
                    }else{
                        text_msg.setVisibility(View.VISIBLE);
                        text_msg.setText("没有更多新闻了");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                text_msg.setVisibility(View.GONE);
                            }
                        }, 1000);
                    }
                }else{
                    ToastUtils.sendtoastbyhandler(handler,result.getMsg());
                }
            }
        });
    }
    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        LogUtil.d("lgs", "Fragment所在的Activity onResume, onResumeLazy " + channelCode);
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        LogUtil.d("lgs", "Fragment 显示 " + channelCode);
    }

    @Override
    protected void onFragmentStopLazy() {
        super.onFragmentStopLazy();
        LogUtil.d("lgs", "Fragment 掩藏 " + channelCode);
    }

    @Override
    protected void onPauseLazy() {
        super.onPauseLazy();
        LogUtil.d("lgs", "Fragment所在的Activity onPause, onPauseLazy " + channelCode);
    }


    @Override
    public void onDestroy() {
      //  HttpStringClient.getinstance().cancleRequest(HTTP_TASK_KEY);
        super.onDestroy();
        LogUtil.d("lgs", "Fragment 所在的Activity onDestroy " + channelCode);
    }
    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        LogUtil.d("lgs", "Fragment View将被销毁 " + channelCode);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ToastUtils.TOAST_WHAT:
                    ToastUtils.showtaostbyhandler(getActivity(), msg);
                    break;

                case 2:

                    break;

                default:
                    break;
            }
        }
    };

    public void refushData(){
        mRecyclerView.scrollToPosition(0);
        mRecyclerView.setRefreshing(true);
    }
}