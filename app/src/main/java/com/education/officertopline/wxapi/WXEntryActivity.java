package com.education.officertopline.wxapi;


import android.content.Intent;
import android.util.Log;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import java.util.Map;


public class WXEntryActivity extends WXCallbackActivity {

    @Override
    protected void handleIntent(Intent intent){

        mWxHandler.setAuthListener(new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                Log.e("lgs","UMWXHandler fsdfsdfs");
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                Log.e("lgs","UMWXHandler error");
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                Log.e("lgs","UMWXHandler cancle");
            }
        });
        super.handleIntent(intent);
    }
}
