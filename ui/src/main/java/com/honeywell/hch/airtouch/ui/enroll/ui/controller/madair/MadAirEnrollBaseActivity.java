package com.honeywell.hch.airtouch.ui.enroll.ui.controller.madair;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.ble.manager.BleScanManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.location.manager.GpsUtil;
import com.honeywell.hch.airtouch.plateform.permission.Permission;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.manager.MadAirBleDataManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by honeywell on 10/12/2016.
 */

public class MadAirEnrollBaseActivity extends EnrollBaseActivity {

    protected int showDialogIndex = 0;

    protected BleScanManager mBleScanManager;


    protected void stopScan() {
        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "stopScan--");
        mBleScanManager.stopBleScan();
        mBleScanManager.stopScanTimeoutTimer();
        mBleScanManager.setScanTimeoutListener(null);
        MadAirBleDataManager.getInstance().setDeviceStatusListener(null);
    }

    @Override
    public void onPermissionGranted(int permissionCode) {
        if (permissionCode == Permission.PermissionCodes.LOCATION_SERVICE_REQUEST_CODE){
            startScan();
//            startGpsService();
        }
    }

    @Override
    public void onPermissionNotGranted(String[] permission, int permissionCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && showDialogIndex < 1) {
            this.requestPermissions(permission, permissionCode);
            showDialogIndex++;
        }
    }

    @Override
    public void onPermissionDenied(int permissionCode) {
//        noLocatedPermission();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case Permission.PermissionCodes.LOCATION_SERVICE_REQUEST_CODE:

                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        startScan();
                        String cityCode = UserInfoSharePreference.getGpsCityCode();
                        if (StringUtil.isEmpty(cityCode) || AppConfig.LOCATION_FAIL.equals(cityCode)) {
                            startGpsService(GpsUtil.FROM_ENROLL_PROCESS);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    noLocatedPermission();
                }
                break;
        }
    }

    /**
     * 手动选择scan和扫码后scan不一样
     */
    protected void startScan() {

    }

    private void noLocatedPermission() {
        MessageBox.createTwoButtonDialog((Activity) mContext, null, getString(R.string.no_located_permission_scantxt),
                getString(R.string.cancel), leftButton, getString(R.string.go_to_setting), goToSetting);

    }

    MessageBox.MyOnClick leftButton = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            backIntent();
        }
    };


    protected MessageBox.MyOnClick goToSetting = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            goToPermissionSetting();
            showDialogIndex = 0;
        }
    };
}
