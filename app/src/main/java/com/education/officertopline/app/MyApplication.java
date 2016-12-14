package com.education.officertopline.app;

import android.app.Application;
import android.content.Context;

import com.education.officertopline.BuildConfig;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by symbol on 2016/3/8.
 */
public class MyApplication extends Application {

    {
        //微信 wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
        PlatformConfig.setWeixin("wx19c3ec55fbabdd31", "34da45782b5785a3fac88ef0127e7683");
        // PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        //新浪微博
        PlatformConfig.setSinaWeibo("1243125757", "39760d0355770c7ef2122434c40bb8ec");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        Config.REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
    }
    /**
     * APP context
     */
    public static Context context;
    public static boolean isDebug = true;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        UMShareAPI.get(this);
        //初始化成功自身捕获异常
    }

    /**
     * protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
     Log.d("lgs","onActivityResult");
     }

     private UMShareListener umShareListener = new UMShareListener() {
    @Override
    public void onResult(SHARE_MEDIA platform) {
    Log.d("lgs","platform"+platform+ " 分享成功啦");

    Toast.makeText(MainActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onError(SHARE_MEDIA platform, Throwable t) {
    Log.d("lgs","platform"+platform+ " 分享失败啦");
    Toast.makeText(MainActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
    if(t!=null){
    Log.d("throw", "throw:" + t.getMessage());
    }
    }

    @Override
    public void onCancel(SHARE_MEDIA platform) {
    Log.d("lgs","platform"+platform+ " 分享取消了");
    Toast.makeText(MainActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
    }
    };

     public static String url = "https://wsq.umeng.com/";
     public static String text = "友盟微社区sdk，多终端一社区，为您的app添加社区就是这么简单";
     public static String title = "友盟微社区";
     public static String imageurl = "http://dev.umeng.com/images/tab2_1.png";
     public static String videourl = "http://video.sina.com.cn/p/sports/cba/v/2013-10-22/144463050817.html";
     public static String musicurl = "http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3";
     public static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();

     @Override
     public void onClick(View v) {
     int id = v.getId();
     Log.d("lgs","platform------------------");
     if(id == R.id.btn1){
     new ShareAction(MainActivity.this).withText("hello")
     .setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA,SHARE_MEDIA.WEIXIN_FAVORITE)
     .setCallback(umShareListener).open();
     //            new ShareAction(MainActivity.this)
     //                    .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
     //                    .withTitle("aaa")
     //                    .withTargetUrl(url)
     //                    .withText("hello1")
     //                    .setCallback(umShareListener)
     //                    .share();
     }else if(id == R.id.btn2) {
     new ShareAction(MainActivity.this)
     .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
     .withTargetUrl(url)
     .withText("hello2")
     .setCallback(umShareListener)
     .share();
     }
     }
     */
}