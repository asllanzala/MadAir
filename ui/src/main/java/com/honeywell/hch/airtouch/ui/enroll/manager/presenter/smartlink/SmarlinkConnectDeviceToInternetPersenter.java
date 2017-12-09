package com.honeywell.hch.airtouch.ui.enroll.manager.presenter.smartlink;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.task.CheckDeviceIsOnlineTask;
import com.honeywell.hch.airtouch.plateform.smartlink.ConnectAndFindDeviceManager;
import com.honeywell.hch.airtouch.plateform.umeng.UmengUtil;
import com.honeywell.hch.airtouch.ui.control.manager.umeng.UmengUiManager;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.IConnectDeviceToInternetPresenter;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.IConnectDeviceToInternetView;

/**
 * Created by h127856 on 16/10/18.
 */
public class SmarlinkConnectDeviceToInternetPersenter implements IConnectDeviceToInternetPresenter {

    private ConnectAndFindDeviceManager mConnectAndFindDeviceManager;

    private IConnectDeviceToInternetView iConnectDeviceToInternetView;
    private String mDeviceMacWithcolon;
    private String mDeviceMacNocolon;

    private String mSsid;

    private String mUserPassword;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConnectAndFindDeviceManager.CONNECTING_TIMEOUT:
                case ConnectAndFindDeviceManager.THREAD_ERROR:
                    iConnectDeviceToInternetView.sendDeviceWifiInfoError();
                    break;
                case ConnectAndFindDeviceManager.PROCESS_END:

                    //发现设备成功后，开始检查设备是否在线
                    checkDeviceIsOnline();
                    break;
                case ConnectAndFindDeviceManager.WIFI_CONNECTED_CHECK_END:
                    Bundle bundle = msg.getData();
                    boolean isConnecting = bundle.getBoolean(ConnectAndFindDeviceManager.IS_CONNECT);
                    if (isConnecting) {
                        startFindThread();
                    } else {
                        iConnectDeviceToInternetView.phoneNotConnectWifi();
                    }
                    break;


            }
        }
    };

    public SmarlinkConnectDeviceToInternetPersenter(IConnectDeviceToInternetView iConnectDeviceToInternetView,String ssid,String password) {
        this.iConnectDeviceToInternetView = iConnectDeviceToInternetView;
        this.mSsid = ssid;
        this.mUserPassword = password;

        mDeviceMacNocolon = EnrollScanEntity.getEntityInstance().getmMacID();

        mDeviceMacWithcolon = StringUtil.getStringWithColon(mDeviceMacNocolon, 2);

        mConnectAndFindDeviceManager = new ConnectAndFindDeviceManager(mHandler, mDeviceMacWithcolon, mDeviceMacNocolon);

    }

    @Override
    public void connectDeviceToInternet() {

        mConnectAndFindDeviceManager.startCheckNetworkConnectingThread();
    }

    private void startFindThread() {
        //send cooee and send udp
        mConnectAndFindDeviceManager.startConnectingAndFinding(mSsid, mUserPassword, NetWorkUtil.getNetworkIp(AppManager.getInstance().getApplication()));

    }

    IActivityReceive iActivityReceive = new IActivityReceive() {
        @Override
        public void onReceive(ResponseResult responseResult) {
            switch (responseResult.getRequestId()) {
                case CHECK_MAC:
                    if (responseResult.getResponseCode() == StatusCode.NETWORK_ERROR) {
                        iConnectDeviceToInternetView.phoneNotConnectWifi();
                    } else {
                        if (responseResult.getFlag() == HPlusConstants.CHECK_MAC_ALIVE) {
                            Log.e("Manager", "checkDeviceIsOnline  addHomeAndDevice");
                            afterCheckDeviceOnline();
                        } else {
                            iConnectDeviceToInternetView.deviceIsNotOnline();
                            UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(), UmengUtil.EnrollEventType.ENROLL_FAIL, "check_mac_error");
                        }
                    }

                    return;

                default:
                    break;
            }

        }
    };

    public void checkDeviceIsOnline() {

        String mMacId = EnrollScanEntity.getEntityInstance().getmMacID();
        CheckDeviceIsOnlineTask checkMacTask = new CheckDeviceIsOnlineTask(mMacId, null, iActivityReceive);
        AsyncTaskExecutorUtil.executeAsyncTask(checkMacTask);

    }

    private void afterCheckDeviceOnline(){
        if (DIYInstallationState.getIsDeviceAlreadyEnrolled()){
            iConnectDeviceToInternetView.updateDeviceWifiSuccess();
        }else {
            iConnectDeviceToInternetView.deviceHasConnectToInternet();
        }
    }


}
