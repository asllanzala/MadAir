package com.honeywell.hch.airtouch.ui.enroll.ui.controller.beforeplay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.Result;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.library.util.StatusBarUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.permission.Permission;
import com.honeywell.hch.airtouch.plateform.umeng.UmengUtil;
import com.honeywell.hch.airtouch.plateform.update.UpdateManager;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.Zxing.camera.CameraManager;
import com.honeywell.hch.airtouch.ui.Zxing.decoding.CaptureActivityHandler;
import com.honeywell.hch.airtouch.ui.Zxing.decoding.InactivityTimer;
import com.honeywell.hch.airtouch.ui.Zxing.view.ViewfinderView;
import com.honeywell.hch.airtouch.ui.common.ui.view.HplusNetworkErrorLayout;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.control.manager.umeng.UmengUiManager;
import com.honeywell.hch.airtouch.ui.enroll.interfacefile.IEnrollScanView;
import com.honeywell.hch.airtouch.ui.enroll.manager.presenter.EnrollScanPresenter;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.ap.ApEnrollChoiceActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;

import java.io.IOException;

/**
 * 拍照的Activity
 *
 * @author Vincent
 */
public class EnrollScanActivity extends EnrollBaseActivity implements Callback, IEnrollScanView {
    private static final String TAG = "EnrollScanActivity";
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    private RelativeLayout mContentView;
    private Activity mActivity;
    private CameraManager cameraManager;
    SurfaceView surfaceView;
    private boolean isPermission = false;
    private TextView mTitleTextview;
    private Button mSettingButton;
    private int isCheckPermission = 0;
    private SurfaceHolder mSurfaceHolder;
    private FrameLayout mCameraLightFl;
    private EnrollScanPresenter mEnrollScanPresenter;
    private ImageView mCameraLithIv;
    private boolean isClickNoQrCode = false;

    /**
     * mEnrollScanEntity
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.enroll_before_play_scan);

        Intent intent = new Intent(HPlusConstants.ENTER_ENROLL_PROCESS);
        sendBroadcast(intent);
        initStatusBar();
        initView();
        downLoadEdviceType();
        UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(), UmengUtil.EnrollEventType.ENROLL_START, "");
        initCameraPermission(true);
    }

    private void initView() {
        initDragDownManager(R.id.root_view_id);
        mActivity = this;
        cameraManager = CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.mo_scanner_viewfinder_view);
        mTitleTextview = (TextView) findViewById(R.id.title_textview_id);
        mSettingButton = (Button) findViewById(R.id.enroll_choose_nextBtn);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        mContentView = (RelativeLayout) findViewById(R.id.deny_scanner_rl);
        surfaceView = (SurfaceView) findViewById(R.id.mo_scanner_preview_view);
        mCameraLightFl = (FrameLayout) findViewById(R.id.camera_light_fl);
        mCameraLithIv = (ImageView) findViewById(R.id.camera_light_iv);
        mEnrollScanPresenter = new EnrollScanPresenter(mContext, this);
    }

    private void downLoadEdviceType() {
        mEnrollScanPresenter.downLoadEdviceType();
    }

    private void initSmartEnrollScanEntity() {
        mEnrollScanPresenter.initSmartEnrollScanEntity();
    }

    private void checkHasCameraLight() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            mCameraLightFl.setVisibility(View.GONE);
        } else {
            mCameraLightFl.setVisibility(View.VISIBLE);
        }
    }

    private void checkPermission() {
        mHPlusPermission.checkAndRequestPermission(Permission.PermissionCodes.STORAGE_AND_CAMERA_CODE, this);
    }

    private void initCameraPermission(boolean isPermission) {
        if (isPermission) {
            mSurfaceHolder = surfaceView.getHolder();
            if (hasSurface) {
                checkCamera();
            } else {
                mSurfaceHolder.addCallback(this);
                mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            }
            StatusBarUtil.initMarginTopWithStatusBarHeight(findViewById(R.id.actionbar_tile_bg), this);
            StatusBarUtil.changeStatusBarTextColor(findViewById(R.id.root_view_id), View.SYSTEM_UI_FLAG_VISIBLE);
            mContentView.setVisibility(View.GONE);
            surfaceView.setVisibility(View.VISIBLE);
            checkHasCameraLight();
        } else {
            StatusBarUtil.initMarginTopWithStatusBarHeight(findViewById(R.id.actionbar_tile_bg), this);
            StatusBarUtil.changeStatusBarTextColor(findViewById(R.id.root_view_id), View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            mContentView.setVisibility(View.VISIBLE);
            surfaceView.setVisibility(View.GONE);
            mTitleTextview.setText(getResources().getString(R.string.enroll_scan_title));
            mSettingButton.setText(getResources().getString(R.string.enroll_setting_btn));
            mCameraLightFl.setVisibility(View.GONE);
        }
        //初始化网络的layout
        initNetWorkLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraLithIv.setImageResource(R.drawable.camera_light_open);
        initSmartEnrollScanEntity();
        checkPermission();
        isClickNoQrCode = false;
    }


    private void checkCamera() {
        initCamera(mSurfaceHolder);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopScanQrcode();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        inactivityTimer.shutdown();
        disMissDialog();
        if (mEnrollScanPresenter != null) {
            mEnrollScanPresenter.restManagerCallBack();
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            cameraManager.openDriver(surfaceHolder);
        } catch (IOException ioe) {
            initCameraPermission(false);
            ioe.printStackTrace();
            return;
        } catch (RuntimeException e) {
            e.printStackTrace();
            initCameraPermission(false);
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            initCameraPermission(false);
            return;
        }
        if (handler == null) {
            try {
                handler = new CaptureActivityHandler(this, null,
                        null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void stopScanQrcode() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
    }

    MessageBox.MyOnClick checkDeviceTypeClick = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            starDownLoad(mContext);
            disMissDialog();
        }
    };

    @Override
    public void disMissDialog() {
        super.disMissDialog();
        if (handler != null) {
            handler.restartPreviewAndDecode();
        }
    }


    public void handleDecode(final Result result, Bitmap barcode) {
        String resultURL = result.getText();
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "resultURL: " + resultURL);
        if ((mAlertDialog != null && mAlertDialog.isShowing())
                || !NetWorkUtil.isNetworkAvailable(mContext)
                || StringUtil.isNumeric(resultURL)) {
            return;
        }
        mDialog = LoadingProgressDialog.show(mContext, getString(R.string.enroll_scanning_welcome));
        inactivityTimer.onActivity();
        mEnrollScanPresenter.paresURL(resultURL);
    }

    protected MessageBox.MyOnClick wifiSetting = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            startActivity(new
                    Intent(Settings.ACTION_WIFI_SETTINGS));//直接进入手机中的蓝牙设置界面
        }
    };

    public void doClick(View v) {
        if (v.getId() == R.id.enroll_back_layout) {
            backIntent();
        } else if (v.getId() == R.id.enroll_choose_nextBtn) {
            goToPermissionSetting();
        } else if (v.getId() == R.id.ap_mode_entrance) {
            isClickNoQrCode = true;
            jumpToSelectModel();
        } else if (v.getId() == R.id.camera_light_fl) {
            cameraManager.openOrShutCameraLigh(mCameraLithIv);
        } else if (v.getId() == R.id.camera_help_fl) {
            startIntent(EnrollHelpActivity.class);
        }
    }

    private void jumpToSelectModel() {
        EnrollScanEntity.getEntityInstance().setNoQRcode(true);
        startIntent(ApEnrollChoiceActivity.class);
    }

    protected MessageBox.MyOnClick downLoadTypeAgain = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            disMissDialog();
            downLoadEdviceType();
        }
    };

    protected MessageBox.MyOnClick update = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            startIntent(EnrollDeviceInfoActivity.class);
        }
    };

    private void starDownLoad(final Context context) {
        UpdateManager.getInstance().starDownLoadAPK(context, isPermission, false);
    }

    @Override
    public void onPermissionGranted(int permissionCode) {
        if (permissionCode == Permission.PermissionCodes.STORAGE_AND_CAMERA_CODE) {
            isPermission = true;
            initCameraPermission(true);
        }
        if (permissionCode == Permission.PermissionCodes.STORAGE_REQUEST_CODE) {
            isPermission = true;
        } else if (permissionCode == Permission.PermissionCodes.CAMERA_REQUEST_CODE) {
            initCameraPermission(true);
        }
    }

    @Override
    public void onPermissionNotGranted(String[] permission, int permissionCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isCheckPermission == 0) {
                this.requestPermissions(permission, permissionCode);
                isCheckPermission++;
            }
        }
    }

    @Override
    public void onPermissionDenied(int permissionCode) {
        if (permissionCode == Permission.PermissionCodes.CAMERA_REQUEST_CODE) {
            LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "onPermissionDenied camera");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "onRequestPermissionsResult: " + requestCode);
        switch (requestCode) {
            case Permission.PermissionCodes.STORAGE_AND_CAMERA_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initCameraPermission(true);
                } else {
                    initCameraPermission(false);
                }
                storagePermissionResult(mHPlusPermission.verifyPermissions(grantResults));
                break;
            case Permission.PermissionCodes.CAMERA_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initCameraPermission(true);
                } else {
                    initCameraPermission(false);
                }
                break;
            case Permission.PermissionCodes.STORAGE_REQUEST_CODE:
                storagePermissionResult(mHPlusPermission.verifyPermissions(grantResults));
                break;
        }
    }

    /**
     * enroll除了第一个界面外，其他的界面都不需要对网络和networkerro进行显示
     */
    @Override
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


    private void storagePermissionResult(boolean verifyResult) {
        isPermission = verifyResult;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void unKnowDevice() {
        mAlertDialog = MessageBox.createSimpleDialog(mActivity, "", getString(R.string.no_data_device), getString(R.string.ok), messageBoxClick);
    }

    @Override
    public void dealSuccessCallBack() {
        super.dealSuccessCallBack(null);
        if (isClickNoQrCode) {
            jumpToSelectModel();
        }
    }

    @Override
    public void deviceModelErrorl() {
        mAlertDialog = MessageBox.createSimpleDialog(mActivity, "", getString(R.string.enroll_device_model_error), getString(R.string.ok), messageBoxClick);
    }

    @Override
    public void unSupportSmartLinkDevice() {
        mAlertDialog = MessageBox.createSimpleDialog(mActivity, "", getString(R.string.no_support_smart_link), getString(R.string.ok), messageBoxClick);
    }

    @Override
    public void updateWifi() {
        mAlertDialog = MessageBox.createTwoButtonDialog(this, null,
                getString(R.string.eroll_device_update_wi_fi), getString(R.string.enroll_exit),
                quitEnroll, getString(R.string.enroll_update), update);
    }

    @Override
    public void registedAuthDevice() {
        mAlertDialog = MessageBox.createSimpleDialog((Activity) mContext, null,
                getString(R.string.device_already_registered_authorize), getString(R.string.ok),
                quitEnroll);
    }

    @Override
    public void showNoNetWorkDialog() {
        mAlertDialog = MessageBox.createTwoButtonDialog(this, null,
                getString(R.string.enroll_network_unavaiable), getString(R.string.enroll_exit),
                quitEnroll, getString(R.string.go_to_setting), new MessageBox.MyOnClick() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void startIntent(Class mClass) {
        super.startIntent(mClass);
    }

    @Override
    public void updateVersion() {
        mAlertDialog = MessageBox.createTwoButtonDialog(mActivity, "", getString(R.string.enroll_device_model_update), getString(R.string.enroll_exit), quitEnroll, getString(R.string.push_notification_update_btn), checkDeviceTypeClick);
    }

    @Override
    public void invalidDevice() {
        mAlertDialog = MessageBox.createSimpleDialog(mActivity, "", getString(R.string.enroll_invalid_qr_code), getString(R.string.ok), messageBoxClick);

    }

    @Override
    public void madAirDeviceAlreadyEnrolled() {
        mAlertDialog = MessageBox.createSimpleDialog(mActivity, "", getString(R.string.mad_ari_enrolled_already), getString(R.string.ok), quitEnroll);
    }

    @Override
    public void errorHandle(ResponseResult responseResult, String errorMsg) {
        // do not quitAPEnroll if wifi issue.
        if (responseResult == null || responseResult.getFlag() == HPlusConstants.CHECK_MAC_OFFLINE) {
            MessageBox.createSimpleDialog(this, null, errorMsg, null, messageBoxClick);
            return;
        }

        if (!NetWorkUtil.isNetworkAvailable(this)) {
            dealWithNetWorkResponse();
            return;
        }

        if (responseResult.getResponseCode() == StatusCode.NETWORK_ERROR || responseResult.getResponseCode() == StatusCode.NETWORK_TIMEOUT) {
            dealWithNetWorkResponse();
            return;
        }

        if (responseResult.getExeptionMsg() != null) {
            LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "ExceptionMsg：" + responseResult.getExeptionMsg());
            LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "ErrorCode：" + responseResult.getResponseCode());
        }

        MessageBox.createSimpleDialog(this, null, errorMsg, null, messageBoxClick);

    }


}