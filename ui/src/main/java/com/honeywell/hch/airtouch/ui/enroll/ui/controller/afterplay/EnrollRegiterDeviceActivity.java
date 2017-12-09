package com.honeywell.hch.airtouch.ui.enroll.ui.controller.afterplay;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.enroll.constant.EnrollConstants;
import com.honeywell.hch.airtouch.ui.enroll.manager.presenter.RegisterDevicePresenter;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;
import com.honeywell.hch.airtouch.ui.enroll.interfacefile.IRegisterDeviceView;

/**
 * Created by Qian Jin on 9/21/15.
 */
public class EnrollRegiterDeviceActivity extends EnrollBaseActivity implements IRegisterDeviceView {
    private static final String TAG = "AirTouchEnrollLoading";
    private ImageView mLoadingImageView;
    private AnimationDrawable mAnimationDrawable;

    private RegisterDevicePresenter mRegisterDevicePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddevice_loading);
        initStatusBar();
        initView();
        mRegisterDevicePresenter = new RegisterDevicePresenter(this);
        mRegisterDevicePresenter.addDevice();
    }

    @Override
    public void onStart() {
        super.onStart();
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void registerTimeoutError() {
        goToResultActivity(EnrollConstants.ENROLL_RESULT_ADDDEVICE_TIMEOUT);

    }

    @Override
    public void registerByOtherError() {
        goToResultActivity(EnrollConstants.ENROLL_RESULT_REGISTER_BY_OTHER);

    }

    @Override
    public void commNotGetResultFailed() {
        goToResultActivity(EnrollConstants.ENROLL_RESULT_NOT_GET_COMMTASK_RESULT_FAILED);

    }

    @Override
    public void otherFailed() {
        goToResultActivity(EnrollConstants.ENROLL_RESULT_OTHER_FAILED);

    }

    @Override
    public void addDeviceSuccess() {
        goToResultActivity(EnrollConstants.ENROLL_RESULT_ADDDEVICE_SUCCESS);
    }

    private void initView() {
        super.initTitleView(false, getString(R.string.register_device_title_msg), EnrollConstants.TOTAL_FOUR_STEP,
                EnrollConstants.STEP_FOUR, getString(R.string.register_device_center_msg),false);

        mLoadingImageView = (ImageView) findViewById(R.id.enroll_loading_iv);
        mAnimationDrawable = (AnimationDrawable) mLoadingImageView.getDrawable();
        mAnimationDrawable.start();
    }


    private void goToResultActivity(int enrollResultType) {

        Intent intent = new Intent();
        intent.putExtra(EnrollConstants.ENROLL_RESULT,enrollResultType);
        intent.setClass(this, EnrollResultActivity.class);
        gotoActivityWithIntent(intent, false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
