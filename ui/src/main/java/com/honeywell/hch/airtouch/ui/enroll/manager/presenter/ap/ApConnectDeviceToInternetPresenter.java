package com.honeywell.hch.airtouch.ui.enroll.manager.presenter.ap;

import android.util.Log;

import com.honeywell.hch.airtouch.library.http.model.HTTPRequestResponse;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IReceiveResponse;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.plateform.ap.EnrollmentClient;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.umeng.UmengUtil;
import com.honeywell.hch.airtouch.ui.control.manager.umeng.UmengUiManager;
import com.honeywell.hch.airtouch.ui.enroll.manager.EnrollDeviceManager;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.IConnectDeviceToInternetPresenter;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.IConnectDeviceToInternetView;

/**
 * Created by h127856 on 16/10/14.
 */
public class ApConnectDeviceToInternetPresenter implements IConnectDeviceToInternetPresenter {

    private IConnectDeviceToInternetView iApConnectDeviceToInternetView;

    private EnrollDeviceManager mEnrollDeviceManager;

    public ApConnectDeviceToInternetPresenter(IConnectDeviceToInternetView apConnectDeviceToInternetView) {
        iApConnectDeviceToInternetView = apConnectDeviceToInternetView;
        mEnrollDeviceManager = new EnrollDeviceManager(AppManager.getInstance().getApplication());
        initCallBack();
    }

    private void initCallBack() {
        mEnrollDeviceManager.setFinishCallback(new EnrollDeviceManager.FinishCallback() {
            @Override
            public void onFinish(boolean isUpdateWifiSuccess) {

            }
        });

        mEnrollDeviceManager.setErrorCallback(new EnrollDeviceManager.ErrorCallback() {
            @Override
            public void onError(ResponseResult responseResult, String errorMsg) {

            }
        });
    }

    IReceiveResponse receiveResponse = new IReceiveResponse() {

        @Override
        public void onReceive(HTTPRequestResponse httpRequestResponse) {

            if (httpRequestResponse != null && httpRequestResponse.getException() != null) {
                /**
                 * 手机wifi的切换（从设备wifi切换到原来wifi）
                 */
                mEnrollDeviceManager.reconnectHomeWifi();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //sleep 7秒（时间是写的一个大概值），让手机连上可用的wifi
                        try {
                            Thread.sleep(10000);
                        } catch (Exception e) {

                        }

                        /**
                         * 检查设备是否在线
                         */
                        mEnrollDeviceManager.checkDeviceIsOnline(iActivityReceive);
                    }
                }).start();
            } else {
                iApConnectDeviceToInternetView.sendDeviceWifiInfoError();
            }


        }
    };

    IActivityReceive iActivityReceive = new IActivityReceive() {
        @Override
        public void onReceive(ResponseResult responseResult) {
            switch (responseResult.getRequestId()) {
                case CHECK_MAC:
                    if (responseResult.getResponseCode() == StatusCode.NETWORK_ERROR) {
                        iApConnectDeviceToInternetView.phoneNotConnectWifi();
                    } else {
                        if (responseResult.getFlag() == HPlusConstants.CHECK_MAC_ALIVE) {
                            Log.e("Manager", "checkDeviceIsOnline  addHomeAndDevice");
                            afterCheckDeviceOnline();
                        } else {
                            iApConnectDeviceToInternetView.deviceIsNotOnline();
                            UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(), UmengUtil.EnrollEventType.ENROLL_FAIL, "check_mac_error");
                        }
                    }

                    return;

                default:
                    break;
            }

        }
    };


    /**
     * 包括3个步骤：
     * 1.发送wifi密码到设备
     * 2.手机wifi的切换（从设备wifi切换到原来wifi）
     * 3.检查设备是否在线
     */
    @Override
    public void connectDeviceToInternet() {

        /**
         * 发送wifi密码到设备
         */
        EnrollmentClient.sharedInstance()
                .connectStat(DIYInstallationState.getWAPIRouter(), RequestID.CONNECT_ROUTER, receiveResponse);

    }


    private void afterCheckDeviceOnline(){
        if (DIYInstallationState.getIsDeviceAlreadyEnrolled()){
            iApConnectDeviceToInternetView.updateDeviceWifiSuccess();
        }else {
            iApConnectDeviceToInternetView.deviceHasConnectToInternet();
        }
    }


}
