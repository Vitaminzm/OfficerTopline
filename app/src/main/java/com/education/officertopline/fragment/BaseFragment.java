package com.education.officertopline.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.education.officertopline.http.HttpStringClient;

/**
 * Fragment基类
 * Created by WuHang on 2015/9/17.
 */
public class BaseFragment extends Fragment {
    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        HttpStringClient.getinstance().cancleRequest(HTTP_TASK_KEY);
        super.onDestroy();
    }
}
