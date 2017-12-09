package com.honeywell.hch.airtouch.ui.common.ui.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.library.util.StatusBarUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.location.manager.GpsUtil;
import com.honeywell.hch.airtouch.plateform.permission.HPlusPermission;
import com.honeywell.hch.airtouch.plateform.permission.PermissionListener;
import com.honeywell.hch.airtouch.plateform.umeng.UmengUtil;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.manager.CloseActivityUtil;
import com.honeywell.hch.airtouch.ui.common.ui.view.DropDownAnimationManager;
import com.honeywell.hch.airtouch.ui.common.ui.view.HplusNetworkErrorLayout;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;

/**
 * Base activity, implement some common function
 * Created by nan.liu on 1/19/15.
 */
public class BaseActivity extends Activity implements PermissionListener {
    protected String TAG = "AirTouchBaseActivity";


    protected HPlusPermission mHPlusPermission;
    protected Context mContext;
    protected DropDownAnimationManager mDropDownAnimationManager;
    protected ViewGroup mRootRl;
    protected Dialog mDialog;
    protected AlertDialog mAlertDialog;
    protected BroadcastReceiver networkBroadcast;
    protected HplusNetworkErrorLayout mNetWorkErrorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        UmengUtil.visitPageEvent(this.getLocalClassName());
        registerNetworkReceiver();
        mHPlusPermission = new HPlusPermission(this);
        CloseActivityUtil.activityList.add(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
            // 状态栏字体设置为深色，SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 为SDK23增加
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

//            // 部分机型的statusbar会有半透明的黑色背景
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);// SDK21
        }


    }

    protected void initStatusBar() {
        StatusBarUtil.initMarginTopWithStatusBarHeight(findViewById(R.id.actionbar_tile_bg), this);
        StatusBarUtil.changeStatusBarTextColor(findViewById(R.id.root_view_id), View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        initNetWorkLayout();
    }

    protected void initNetWorkLayout() {
        mNetWorkErrorLayout = (HplusNetworkErrorLayout) findViewById(R.id.network_error_layout);
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

    @Override
    protected void onResume() {
        super.onResume();
        UmengUtil.onActivityResume(mContext, TAG);
    }

    protected void initDragDownManager(int id) {
        mRootRl = (ViewGroup) findViewById(id);
        mDropDownAnimationManager = new DropDownAnimationManager(mRootRl, mContext);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtil.onActivityPause(mContext, TAG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterNetworkReceiver();
        disMissDialog();
        CloseActivityUtil.activityList.remove(this);
    }

    protected void showToast(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT)
                .show();
    }

    public void errorHandle(ResponseResult responseResult, String errorMsg) {
        if (!NetWorkUtil.isNetworkAvailable(this)) {
            dealWithNetWorkResponse();
            return;
        }

        /**
         * 只要有网络错误的情况，都不给予其他提示
         */
        if (responseResult.getResponseCode() == StatusCode.NETWORK_ERROR || responseResult.getResponseCode() == StatusCode.NETWORK_TIMEOUT
                || UserAllDataContainer.shareInstance().isHasNetWorkError()) {
            dealWithNetWorkResponse();
            return;
        }

//        if (responseResult.getResponseCode() == StatusCode.BAD_REQUEST || !StringUtil.isEmpty(errorMsg)) {
//            mDropDownAnimationManager.showDragDownLayout(errorMsg, true);
//            return;
//        }

        if (responseResult.getResponseCode() == StatusCode.UPDATE_SESSION_PASSWORD_CHANGE) {
            mDropDownAnimationManager.showDragDownLayout(getString(R.string.login_password_invalid), true);
            return;
        }

//
//        if (responseResult.getExeptionMsg() != null
//                && responseResult.getResponseCode() == StatusCode.RETURN_RESPONSE_NULL && responseResult.getResponseCode() != StatusCode.REQUEST_ERROR) {
//            mDropDownAnimationManager.showDragDownLayout(responseResult.getExeptionMsg(), true);
//            return;
//
//        }
        mDropDownAnimationManager.showDragDownLayout(getString(R.string.enroll_error), true);

    }

    @Override
    public void onPermissionGranted(int permissionCode) {
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


    protected void gotoActivityWithIntent(Intent intent, boolean isBack) {
        startActivity(intent);
        if (isBack) {
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        } else {
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }
        finish();
    }

    protected void startIntent(Class<?> cls) {
        Intent intent = new Intent(mContext, cls);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    protected void BackActivityWithNoFinishIntent(Class<?> cls) {
        Intent intent = new Intent(mContext, cls);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    /**
     * 设置activity实现点击 非editTextView得其他区域，可以把键盘消失的方法
     *
     * @param view
     */
    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    if (v.getId() != R.id.edit_icon_id) {
                        hideInputKeyBoard();
                    }
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

    public void hideInputKeyBoard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    protected void disMissDialog() {
        if (mDialog != null) {
            mDialog.cancel();
        }
        if (mAlertDialog != null) {
            mAlertDialog.cancel();
        }
    }

    /**
     * 键盘显示的时界面上推
     *
     * @param resId 要上推的view id
     * @param view
     */
    public void setListenerToRootView(int resId, final View view) {
        final View rootView = getWindow().getDecorView().findViewById(resId);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int navigationBarHeight = 0;
                int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    navigationBarHeight = getResources().getDimensionPixelSize(resourceId);
                }

                // status bar height
                int statusBarHeight = 0;
                resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    statusBarHeight = getResources().getDimensionPixelSize(resourceId);
                }

                // display window size for the app layout
                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

                // screen height - (user app height + status + nav) ..... if non-zero, then there is a soft keyboard
                int keyboardHeight = rootView.getRootView().getHeight() - (statusBarHeight + navigationBarHeight + rect.height());
                ViewPropertyAnimator viewPropertyAnimator = view.animate();

                if (keyboardHeight >= DensityUtil.dip2px(50)) {
                    view.setVisibility(View.GONE);
                    viewPropertyAnimator.translationY(keyboardHeight).setDuration(10).start();
                } else {
                    view.setVisibility(View.VISIBLE);
                    viewPropertyAnimator.translationY(0).setDuration(10).start();

                }

            }
        });
    }


    protected void backIntent() {
        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    protected void showSimpleDialog(String message, String button, MessageBox.MyOnClick click) {
        if (!mAlertDialog.isShowing())
            mAlertDialog = MessageBox.createSimpleDialog((Activity) mContext, null, message, button, click);
    }

    protected void showDoubleDialog(String message, String leftBtn, MessageBox.MyOnClick leftClick, String rightBtn, MessageBox.MyOnClick rightClick) {
        if (!mAlertDialog.isShowing())
            mAlertDialog = MessageBox.createTwoButtonDialog((Activity) mContext, null, message, leftBtn, leftClick, rightBtn, rightClick);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // when the progress is finding the device , can not be back
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            backIntent();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void registerNetworkReceiver() {
        networkBroadcast = new NetWorkBroadcastReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(HPlusConstants.NET_WORK_ERROR);
        filter.addAction(HPlusConstants.NETWORK_CONNECT_SERVER_WELL);
        this.registerReceiver(networkBroadcast, filter);
    }

    private void unRegisterNetworkReceiver() {
        if (networkBroadcast != null) {
            this.unregisterReceiver(networkBroadcast);
            networkBroadcast = null;
        }
    }

    private class NetWorkBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "action: " + action);
            if (HPlusConstants.NET_WORK_ERROR.equals(action)) {
                if (mNetWorkErrorLayout != null) {
                    mNetWorkErrorLayout.setVisibility(View.VISIBLE);
                    mNetWorkErrorLayout.setErrorMsg(HplusNetworkErrorLayout.CONNECT_SERVER_ERROR);
                }

            } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action) || HPlusConstants.NETWORK_CONNECT_SERVER_WELL.equals(action)) {

                if (NetWorkUtil.isNetworkAvailable(BaseActivity.this)) {
                    LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "手机有网络------");
                    if (mNetWorkErrorLayout != null && !UserAllDataContainer.shareInstance().isHasNetWorkError()) {
                        mNetWorkErrorLayout.setVisibility(View.GONE);
                    }
                } else {
                    LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "手机没有任何网络....");
                    if (mNetWorkErrorLayout != null) {
                        mNetWorkErrorLayout.setVisibility(View.VISIBLE);
                        mNetWorkErrorLayout.setErrorMsg(HplusNetworkErrorLayout.NETWORK_OFF);
                    }
                    processNetworkDisconnected();
                }
            }

        }
    }


    protected void processNetworkConnected() {
    }

    protected void processNetworkDisconnected() {
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

    /**
     * 如果没有网络，点击的时候要进行闪烁
     *
     * @return
     */
    protected boolean isNetworkOff() {
        if (!NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication())) {
            dealWithNetWorkResponse();
            return true;
        }
        return false;
    }

    //闪烁
    private void startFlick(View view) {
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

    protected void dealSuccessCallBack(ResponseResult responseResult) {
        disMissDialog();
        if (mNetWorkErrorLayout != null) {
            mNetWorkErrorLayout.setVisibility(View.GONE);
        }
    }

    protected void dealErrorCallBack(ResponseResult responseResult, int id) {
        disMissDialog();
    }

    public SpannableString getSpanable(int msgId, int[] spanableStrId) {
        String str = getString(msgId);
        SpannableString ssTitle = new SpannableString(str);

        for (int spanItem : spanableStrId) {
            String spanItemStr = getString(spanItem);
            ssTitle.setSpan(new StyleSpan(Typeface.BOLD), str.indexOf(spanItemStr), str.indexOf(spanItemStr) + spanItemStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssTitle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_one)), str.indexOf(spanItemStr), str.indexOf(spanItemStr) + spanItemStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

        return ssTitle;
    }

    protected void goToPermissionSetting() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(localIntent);
    }

    protected void startGpsService(int fromWhere) {
        GpsUtil mGpsUtil = new GpsUtil(AppConfig.shareInstance().getCityChinaDBService(), AppConfig.shareInstance().getCityIndiaDBService());
        try {
            mGpsUtil.initGps(fromWhere);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
