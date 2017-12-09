package com.honeywell.hch.airtouch.ui.common.ui.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.library.util.StatusBarUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.permission.HPlusPermission;
import com.honeywell.hch.airtouch.plateform.permission.Permission;
import com.honeywell.hch.airtouch.plateform.permission.PermissionListener;
import com.honeywell.hch.airtouch.plateform.umeng.UmengUtil;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.view.DropDownAnimationManager;
import com.honeywell.hch.airtouch.ui.common.ui.view.HplusNetworkErrorLayout;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.beforeplay.EnrollScanActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;

/**
 * Base request fragment, implement some common request function
 * Created by nan.liu on 4/8/15.
 */
public class BaseRequestFragment extends BaseFragment implements PermissionListener {
    protected Activity mParentActivity;
    protected Dialog mDialog;
    protected DropDownAnimationManager mDropDownAnimationManager;
    protected ViewGroup mRootRl;
    protected HPlusPermission mHPlusPermission;
    protected AlertDialog mAlertDialog;
    protected HplusNetworkErrorLayout mNetWorkErrorLayout;

    public Activity getFragmentActivity() {
        return mParentActivity;
    }

    protected void initActivity(Activity activity) {
        mParentActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHPlusPermission = new HPlusPermission(this);
//        registerNetworkReceiver();
    }

    @Override
    public void onPermissionGranted(int permissionCode) {
        if (permissionCode == Permission.PermissionCodes.CALL_PHONE_REQUEST_CODE) {
            callPhonePermissionResult(true);
        }
    }

    @Override
    public void onPermissionNotGranted(String[] permission, int permissionCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(permission, permissionCode);
        }
    }

    @Override
    public void onPermissionDenied(int permissionCode) {

    }

    private void callPhonePermissionResult(boolean verifyResult) {
        if (verifyResult) {
            Intent intent;
            if (AppConfig.shareInstance().isIndiaAccount()) {
                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + HPlusConstants.INDIA_CONTACT_PHONE_NUMBER));
            } else {
                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + HPlusConstants.CONTACT_PHONE_NUMBER));
            }
            startActivity(intent);
        } else {
            MessageBox.createSimpleDialog(getActivity(), "",
                    getResources().getString(R.string.phone_call_permission_deny),
                    getResources().getString(R.string.ok), null);
        }
    }

    protected void initDragDownManager(View v, int id) {
        mRootRl = (ViewGroup) v.findViewById(id);
        mDropDownAnimationManager = new DropDownAnimationManager(mRootRl, mParentActivity);
    }

    public void errorHandle(ResponseResult responseResult, String errorMsg) {
        if (!NetWorkUtil.isNetworkAvailable(mParentActivity)) {
            dealWithNetWorkResponse();
            return;
        }

        if (responseResult.getResponseCode() == StatusCode.NETWORK_ERROR || responseResult.getResponseCode() == StatusCode.NETWORK_TIMEOUT ||
                UserAllDataContainer.shareInstance().isHasNetWorkError()) {
            dealWithNetWorkResponse();
            return;
        }

        if (responseResult.getResponseCode() == StatusCode.BAD_REQUEST || !StringUtil.isEmpty(errorMsg)) {
            mDropDownAnimationManager.showDragDownLayout(errorMsg, true);
            return;
        }

        mDropDownAnimationManager.showDragDownLayout(getString(R.string.enroll_error), true);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        unRegisterNetworkReceiver();
        disMissDialog();
    }

    protected void disMissDialog() {
        if (mDialog != null) {
            mDialog.cancel();
        }
        if (mAlertDialog != null) {
            mAlertDialog.cancel();
        }
    }

    protected void intentStartActivity(Class mClass) {
        Intent intent = new Intent(mParentActivity, mClass);
        startActivity(intent);
        mParentActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        EnrollBaseActivity.mAliveFlag = 1;
    }

    public void backClick() {
        mParentActivity.finish();
        mParentActivity.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengUtil.onFragmentActivityStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtil.onFragmentActivityEnd();
    }

    protected void initStatusBar(View view) {
        StatusBarUtil.initMarginTopWithStatusBarHeight(view.findViewById(R.id.actionbar_tile_bg), mParentActivity);
        initNetWorkLayout(view);
    }

    protected void initNetWorkLayout(View view) {
        mNetWorkErrorLayout = (HplusNetworkErrorLayout) view.findViewById(R.id.network_error_layout);
        if (!NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication())) {
            mNetWorkErrorLayout.setVisibility(View.VISIBLE);
            mNetWorkErrorLayout.setErrorMsg(HplusNetworkErrorLayout.NETWORK_OFF);
        } else if (UserAllDataContainer.shareInstance().isHasNetWorkError()) {
            mNetWorkErrorLayout.setVisibility(View.VISIBLE);
            mNetWorkErrorLayout.setErrorMsg(HplusNetworkErrorLayout.CONNECT_SERVER_ERROR);
        } else {
            mNetWorkErrorLayout.setVisibility(View.GONE);
        }
    }

    public HplusNetworkErrorLayout getNetWorkErrorLayout() {
        return mNetWorkErrorLayout;
    }


    public void setNoNetWorkView() {
        if (mNetWorkErrorLayout != null) {
            mNetWorkErrorLayout.setVisibility(View.VISIBLE);
            mNetWorkErrorLayout.setErrorMsg(HplusNetworkErrorLayout.NETWORK_OFF);
        }
    }

    public void setNetWorkErrorView() {
        if (mNetWorkErrorLayout != null) {
            mNetWorkErrorLayout.setVisibility(View.VISIBLE);
            mNetWorkErrorLayout.setErrorMsg(HplusNetworkErrorLayout.CONNECT_SERVER_ERROR);
        }
    }

    public void setNetWorkViewGone() {
        if (UserAllDataContainer.shareInstance().isHasNetWorkError()) {
            setNetWorkErrorView();
        } else if (mNetWorkErrorLayout != null) {
            mNetWorkErrorLayout.setVisibility(View.GONE);
        }
    }

    protected void dealWithNetWorkResponse() {
        if (mNetWorkErrorLayout != null) {
            if (mNetWorkErrorLayout.getVisibility() == View.VISIBLE) {
                startFlick(mNetWorkErrorLayout);
            } else {
                mNetWorkErrorLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    //闪烁
    protected void startFlick(View view) {
        if (null == view) {
            return;
        }
        Animation alphaAnimation = new AlphaAnimation(1, 0.2f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        view.startAnimation(alphaAnimation);
        view.setClickable(false);
    }

    protected void dealErrorCallBack(ResponseResult responseResult, int id) {
        disMissDialog();
    }

    /**
     * need override this method
     * 需要子类进行重写
     *
     * @param responseResult
     */
    protected void dealSuccessCallBack(ResponseResult responseResult) {
        disMissDialog();
        if (mNetWorkErrorLayout != null) {
            mNetWorkErrorLayout.setVisibility(View.GONE);
        }
    }

    protected boolean isNetworkOff() {
        if (!NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication())) {
            dealWithNetWorkResponse();
            return true;
        }
        return false;
    }

    /**
     * 处理activity过来的广播，进行界面处理
     *
     * @param locationData
     * @param index
     */
    public void refreshFragment(UserLocationData locationData, int index) {

    }

}
