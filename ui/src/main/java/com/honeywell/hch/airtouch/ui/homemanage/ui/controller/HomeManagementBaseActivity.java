package com.honeywell.hch.airtouch.ui.homemanage.ui.controller;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseActivity;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.homemanage.manager.HomeManagerManager;
import com.honeywell.hch.airtouch.ui.homemanage.manager.HomeManagerUiManager;

/**
 * Created by Vincent on 13/7/16.
 */
public class HomeManagementBaseActivity extends BaseActivity {
    protected TextView mEndTextTip;
    protected TextView mTitleTextview;
    protected HomeManagerUiManager mHomeManagerUiManager;
    protected final int ADD_HOME_RESULT = 000001;


    protected void initTitleView() {
        mTitleTextview = (TextView) findViewById(R.id.title_textview_id);
        mEndTextTip = (TextView) findViewById(R.id.end_text_tip);
        mEndTextTip.setVisibility(View.VISIBLE);
    }

    protected HomeManagerManager.SuccessCallback mSuccessCallback = new HomeManagerManager.SuccessCallback() {
        @Override
        public void onSuccess(ResponseResult responseResult) {
            dealSuccessCallBack(responseResult);
        }
    };

    protected HomeManagerManager.ErrorCallback mErrorCallBack = new HomeManagerManager.ErrorCallback() {
        @Override
        public void onError(ResponseResult responseResult, int id) {
            dealErrorCallBack(responseResult, id);
        }
    };

    /*
        group control update group name need overide
     */
    protected void dealErrorCallBack(ResponseResult responseResult, int id) {
        super.dealErrorCallBack(responseResult, id);
        switch (responseResult.getRequestId()) {
            case DELETE_LOCATION:
                if (NetWorkUtil.isNetworkAvailable(mContext) && mNetWorkErrorLayout != null
                        && mNetWorkErrorLayout.getVisibility() != View.VISIBLE) {
                    MessageBox.createSimpleDialog((Activity) mContext, null,
                            getResources().getString(id), null, null);
                } else {
                    errorHandle(responseResult, getString(id));
                }
                break;
            case SWAP_LOCATION:
            case ADD_LOCATION:
                errorHandle(responseResult, getString(id));
                break;
        }
    }

    protected void startIntent(Class mClass) {
        Intent intent = new Intent(mContext, mClass);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    protected void initHomeManagerUiManager() {
        mHomeManagerUiManager = new HomeManagerUiManager();
        initManagerCallBack(mHomeManagerUiManager);
    }

    protected void initManagerCallBack(HomeManagerUiManager homeManagerUiManager) {
        homeManagerUiManager.setErrorCallback(mErrorCallBack);
        homeManagerUiManager.setSuccessCallback(mSuccessCallback);
    }

}
