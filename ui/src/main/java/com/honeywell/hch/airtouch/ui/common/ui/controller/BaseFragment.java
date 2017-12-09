package com.honeywell.hch.airtouch.ui.common.ui.controller;


import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

/**
 * Base fragment, implement some common function
 * Created by nan.liu on 1/19/15.
 */
public class BaseFragment extends Fragment {
    protected String TAG = "BaseFragment";

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }
}
