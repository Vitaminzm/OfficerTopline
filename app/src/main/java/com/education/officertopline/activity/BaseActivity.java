package com.education.officertopline.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.education.officertopline.http.HttpStringClient;

/**
 * Activity基类
 * Created by WuHang on 2015/9/17.
 */
public abstract class BaseActivity extends Activity{
    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getApplicationContext();
        initView();
        initData();
    }

    /**
     *
     * 初始化数据
     */
    protected abstract void initData();

    /**
     *
     *  初始化UI
     */
    protected abstract void initView();

    /**
     *
     *  用于资源回收
     */
    protected abstract void recycleMemery();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recycleMemery();
        HttpStringClient.getinstance().cancleRequest(HTTP_TASK_KEY);
    }
}
