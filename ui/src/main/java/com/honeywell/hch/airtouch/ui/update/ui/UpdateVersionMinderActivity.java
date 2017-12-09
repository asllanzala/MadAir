package com.honeywell.hch.airtouch.ui.update.ui;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.model.update.VersionCollector;
import com.honeywell.hch.airtouch.plateform.permission.Permission;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseActivity;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.plateform.update.UpdateManager;

import java.util.Locale;

/**
 * Created by Vincent on 28/10/15.
 */
public class UpdateVersionMinderActivity extends BaseActivity {

    public static final String IS_FROM_NOTIFICATION = "is_from_notification";
    private int mCurrentTimeHour;
    private AppConfig mAppConfig = AppConfig.shareInstance();
    private String mLocalLanguage;
    private VersionCollector mVersionCollector;
    private int mUpdateType;
    private AlertDialog mMessageBox;
    private boolean isPermission = false;
    private View mParentView;
    private boolean isFirstResume = true;
    private boolean isFromNotification = false;

    @Override
    public void onDestroy() {
        if (mMessageBox != null && mMessageBox.isShowing()) {
            mMessageBox.dismiss();
            mMessageBox = null;
        }
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verion_reminder);

        if (isTaskRoot()) {
            finish();
            return;
        }
        mParentView = findViewById(R.id.parent_id);

        mHPlusPermission.checkAndRequestPermission(Permission.PermissionCodes.STORAGE_REQUEST_CODE, this);

        Bundle bundle = getIntent().getExtras();
        mUpdateType = bundle.getInt(UpdateManager.UPDATE_TYPE, HPlusConstants.NORMAL_UPDATE);
        mVersionCollector = (VersionCollector) bundle.getSerializable(UpdateManager.VERSION_DATA);
        mCurrentTimeHour = (int) ((System.currentTimeMillis()) / (3600 * 1000 * 24));
        mLocalLanguage = Locale.getDefault().getLanguage();
    }

    private void showUpdateDialog(int updateType) {
        switch (updateType) {
            case HPlusConstants.NORMAL_UPDATE:
            case HPlusConstants.FORCE_UPDATE:
                forceUpdate(updateType == HPlusConstants.FORCE_UPDATE);
                break;
            case HPlusConstants.UNSUPPORT_MESSAGE_UPDATE:
                updateVersion(R.string.message_new_version);
                break;
            case HPlusConstants.NOTIFICATION_UPDATE:
                updateVersion(R.string.push_notification_update);
                break;
        }

    }

    private void forceUpdate(final boolean isForceUpdate) {
        String title = getString(R.string.version_title, mVersionCollector.mVersionName);
        String content = getTextValue();

        MessageBox.MyOnClick leftClick = new MessageBox.MyOnClick() {
            @Override
            public void onClick(View v) {
                if (!isForceUpdate) {
                    mAppConfig.setUpdateVersionTime(mCurrentTimeHour);
                    UpdateVersionMinderActivity.this.finish();

                } else {
                    updateNowClick();
                }
            }
        };

        MessageBox.MyOnClick rightClick = new MessageBox.MyOnClick() {
            @Override
            public void onClick(View v) {
                updateNowClick();
                UpdateVersionMinderActivity.this.finish();
            }
        };


        if (mMessageBox == null || !mMessageBox.isShowing()) {
            mMessageBox = MessageBox.createUpdateDialog(this, isForceUpdate, title, content, leftClick, rightClick);
        }
    }

    private void updateVersion(int contentId) {
        mMessageBox = MessageBox.createTwoButtonDialog(this, null, getString(contentId), getString(R.string.cancel), new MessageBox.MyOnClick() {
                    @Override
                    public void onClick(View v) {
                        UpdateVersionMinderActivity.this.finish();
                    }
                },
                getString(R.string.push_notification_update_btn), new MessageBox.MyOnClick() {
                    @Override
                    public void onClick(View v) {
                        updateNowClick();
                        UpdateVersionMinderActivity.this.finish();
                    }
                });
    }

    private void updateNowClick() {
        if (UpdateManager.getInstance().isDownLoading()) {
            Toast.makeText(this, getResources().getString(R.string.downloading_new_version_str), Toast.LENGTH_LONG).show();
            return;
        }
        //如果有读写sd卡的权限，就直接下载，否则跳转到应用宝升级,如果是从通知栏过来的，点击升级后，是不需要进行记录时间的
        UpdateManager.getInstance().starDownLoadAPK(UpdateVersionMinderActivity.this, isPermission, !isFromNotification);

    }

    private String getTextValue() {

        if ("zh".equals(mLocalLanguage)) {
            return mVersionCollector.getDescriptionCh();
        } else {
            return mVersionCollector.getDescriptionEn();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        mParentView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirstResume) {
                    showUpdateDialog(mUpdateType);
                    isFirstResume = false;
                }
            }
        }, 500);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // when the progress is finding the device , can not be back
        if (keyCode == KeyEvent.KEYCODE_BACK && mUpdateType == HPlusConstants.FORCE_UPDATE) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onPermissionGranted(int permissionCode) {
        if (permissionCode == Permission.PermissionCodes.STORAGE_REQUEST_CODE) {
            isPermission = true;
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
//        if (permissionCode == Permission.PermissionCodes.CALL_PHONE_REQUEST_CODE){
//            callPhonePermissionResult(false);
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case Permission.PermissionCodes.STORAGE_REQUEST_CODE:
                storagePermissionResult(mHPlusPermission.verifyPermissions(grantResults));
                break;
        }
    }

    private void storagePermissionResult(boolean verifyResult) {
        isPermission = verifyResult;
    }
}
