package com.education.officertopline.app;

import android.app.Application;
import android.content.Context;

import com.education.officertopline.BuildConfig;
import com.socks.library.KLog;

/**
 * Created by symbol on 2016/3/8.
 */
public class MyApplication extends Application {

    /**
     * APP context
     */
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        KLog.init(BuildConfig.LOG_DEBUG);
        //初始化成功自身捕获异常
    }
}