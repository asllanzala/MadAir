package com.honeywell.hch.airtouch.ui.enroll.manager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.http.model.HTTPRequestResponse;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IReceiveResponse;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.library.util.WifiUtil;
import com.honeywell.hch.airtouch.plateform.ap.EnrollmentClient;
import com.honeywell.hch.airtouch.plateform.ap.model.WAPIDeviceResponse;
import com.honeywell.hch.airtouch.plateform.ap.model.WAPIErrorCodeResponse;
import com.honeywell.hch.airtouch.plateform.ap.model.WAPIKeyResponse;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.task.CheckDeviceIsOnlineTask;
import com.honeywell.hch.airtouch.plateform.umeng.UmengUtil;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.control.manager.umeng.UmengUiManager;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;

import java.util.List;


/**
 * Created by Qian Jin on 8/26/15.
 * This class is to handle 3 things:
 * 1) Phone's wifi handling
 * 2) Phone connect to Device
 * 3) Phone connect to TCC
 */
public class EnrollDeviceManager implements WifiUtil.IWifiOpen {
    public static final String MACID_ARG = "macid_arg";
    public static final String CRCID_ARG = "crcid_arg";
    public static final String DEVICENAME_ARG = "devicename_arg";
    public static final String HOMENAME_ARG = "homename_arg";
    public static final String CITYCODE_ARG = "citycode_arg";

    private static final int RE_CONNECTED_WIFI = 8000;

    private String mMacId;


    // Data for device connect
    private WAPIDeviceResponse mWAPIDeviceResponse;
    private WAPIKeyResponse mWAPIKeyResponse;
    private WAPIErrorCodeResponse mWAPIErrorCodeResponse;
    // Data for Wifi
    private WifiManager mWifi;
    private boolean mConnectionAttempted;
    private boolean isHasAutoConnectWifi = false;
    private boolean isRegistered = false;

    private FinishCallback mFinishCallback;
    private ErrorCallback mErrorCallback;
    private LoadingCallback mLoadingCallback;
    private Context mContext;
    private int mReconnectWifiCount = 0;

    private static final String TAG = "AirTouchEnrollConnectDevice";


    public EnrollDeviceManager(Context context) {
        mContext = context;
        mWifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if (DIYInstallationState.getWAPIDeviceResponse() != null) {
            mMacId = DIYInstallationState.getWAPIDeviceResponse().getMacID();
        }
    }


    public EnrollDeviceManager() {
    }


    public void setRegisteredInfo(String macId, String macCrc, String deviceName, String homeName, String cityCode) {
        mMacId = macId;
    }

    public interface FinishCallback {
        void onFinish(boolean isUpdateWifiSuccess);
    }

    public interface ErrorCallback {
        void onError(ResponseResult responseResult, String errorMsg);
    }

    public interface LoadingCallback {
        void onLoad(String msg);
    }

    public void setFinishCallback(FinishCallback finishCallback) {
        mFinishCallback = finishCallback;
    }

    public void setErrorCallback(ErrorCallback errorCallback) {
        mErrorCallback = errorCallback;
    }

    public void setLoadingCallback(LoadingCallback loadingCallback) {
        mLoadingCallback = loadingCallback;
    }


    /******************
     * Wifi handling
     ******************/
    public void reconnectHomeWifi() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                mWifi == null) {
            return;
        }

        mWifi.disconnect();
        mWifi.startScan();

        isRegistered = true;
        IntentFilter scanResultsFilter = new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mContext.registerReceiver(mScanResultsReceiver, scanResultsFilter);

        WifiUtil.openWifi(mContext, this);

    }

    private BroadcastReceiver mScanResultsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return;
            }

            if (!isHasAutoConnectWifi) {
                isHasAutoConnectWifi = true;
                List<ScanResult> scanResults = mWifi.getScanResults();
                for (ScanResult scanResult : scanResults) {
                    if (DIYInstallationState.getmHomeConnectedSsid() != null) {
                        if (DIYInstallationState.getmHomeConnectedSsid().contains(scanResult.SSID)) {
                            connectAp(scanResult);
                            return;
                        }
                    }
                }
            }
        }
    };

    private void connectAp(ScanResult mResult) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        List<WifiConfiguration> mList = WifiUtil.getConfigurations(mContext);
        if (mList == null || mList.isEmpty()) {
            if (WifiUtil.getEncryptString(mResult.capabilities).equals("OPEN")) {
                WifiUtil.addNetWork(WifiUtil.createWifiConfig(mResult.SSID, "",
                        WifiUtil.getWifiCipher(mResult.capabilities)), mContext);
            }
        } else {
            boolean flag = false;
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).SSID.equals("\"" + mResult.SSID + "\"")) {
                    WifiUtil.addNetWork(mList.get(i), mContext);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if (WifiUtil.getEncryptString(mResult.capabilities).equals("OPEN")) {
                    WifiUtil.addNetWork(WifiUtil.createWifiConfig(mResult.SSID, "",
                            WifiUtil.getWifiCipher(mResult.capabilities)), mContext);
                }
            }
        }
    }

    @Override
    public void onWifiOpen(int state) {

    }


    /*************************************
     * Methods of Phone connect to Device
     *************************************/

    public boolean isConnectionAttempted() {
        return mConnectionAttempted;
    }

    public void setConnectionAttempted(boolean connectionAttempted) {
        mConnectionAttempted = connectionAttempted;
    }

    public BroadcastReceiver getScanResultsReceiver() {
        return mScanResultsReceiver;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    final IReceiveResponse getWAPIResponse = new IReceiveResponse() {
        @Override
        public void onReceive(HTTPRequestResponse httpRequestResponse) {
            if (httpRequestResponse.getStatusCode() == StatusCode.OK) {
                switch (httpRequestResponse.getRequestID()) {
                    case GET_MAC_CRC:
                        if (!StringUtil.isEmpty(httpRequestResponse.getData())) {
                            mWAPIDeviceResponse = new Gson().fromJson(httpRequestResponse
                                            .getData(),
                                    WAPIDeviceResponse.class);
                        }
                        mConnectionAttempted = true;
                        DIYInstallationState.setWAPIDeviceResponse(mWAPIDeviceResponse);
                        mReconnectWifiCount = 0;
                        mFinishCallback.onFinish(false);

                        break;

                    case GET_KEY:
                        if (!StringUtil.isEmpty(httpRequestResponse.getData())) {
                            mWAPIKeyResponse = new Gson().fromJson(httpRequestResponse.getData(),
                                    WAPIKeyResponse.class);
                        }
                        DIYInstallationState.setWAPIKeyResponse(mWAPIKeyResponse);
                        EnrollmentClient.sharedInstance()
                                .getDeviceInfo(RequestID.GET_MAC_CRC, getWAPIResponse);
                        break;

                    case SEND_PHONE_NAME:
                        EnrollmentClient.sharedInstance()
                                .getWAPIKey(RequestID.GET_KEY, getWAPIResponse);
                        break;

                    default:
                        break;
                } // end of switch
            } else {
                if (mReconnectWifiCount < 1) {
                    reConnectHomeWifiWhenSendPhoneName();
                    mReconnectWifiCount++;
                } else {
                    mReconnectWifiCount = 0;
                    ResponseResult responseResult = new ResponseResult();

                    if (NetWorkUtil.updateWifiInfo2(mContext).contains(getDeviceWifiStr()) && NetWorkUtil.isConnectMobileNetwork(mContext)) {
                        responseResult.setResponseCode(StatusCode.CONNECT_WIFI_BUT_CANNOT_COMMUNICATION);
                        UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(), UmengUtil.EnrollEventType.ENROLL_FAIL, "CONNECT_WIFI_BUT_CANNOT_COMMUNICATION");
                    } else if (NetWorkUtil.updateWifiInfo2(mContext).contains(getDeviceWifiStr())) {
                        responseResult.setResponseCode(StatusCode.CONNECT_WIFI_BUT_SOCKETTIMEOUT);
                        UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(), UmengUtil.EnrollEventType.ENROLL_FAIL, "CONNECT_WIFI_BUT_SOCKETTIMEOUT");
                    } else {
                        responseResult.setResponseCode(StatusCode.RE_CONNECT_DEVICE_WIFI_ERROR);
                        UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(), UmengUtil.EnrollEventType.ENROLL_FAIL, "RE_CONNECT_DEVICE_WIFI_ERROR");
                    }

                    mErrorCallback.onError(responseResult, null);
                }

            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RE_CONNECTED_WIFI:
                    connectDevice();
                    break;
            }
            super.handleMessage(msg);
        }

    };

    private void reConnectHomeWifiWhenSendPhoneName() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ScanResult> scanResults = mWifi.getScanResults();
                for (ScanResult scanResult : scanResults) {
                    if (scanResult.SSID.contains(getDeviceWifiStr())) {
                        connectAp(scanResult);

                        try {
                            Thread.sleep(4000);
                        } catch (Exception e) {

                        }
                        Message message = Message.obtain();
                        message.what = RE_CONNECTED_WIFI;
                        mHandler.sendMessage(message);
                        return;
                    }
                }
            }
        }).start();


    }

    public String getDeviceWifiStr() {

        if (EnrollScanEntity.getEntityInstance().getDeviceName() == R.string.airtouch_ffac_str) {
            if (AppManager.isEnterPriseVersion()) {
                return HPlusConstants.AIR_TOUCH_FFAC_SSID;
            }
        }
        else {
            return EnrollScanEntity.getEntityInstance().getDevicePrefixWifiName();
        }
        return HPlusConstants.AIR_TOUCH_X_SSID;
    }

    public void connectDevice() {
        EnrollmentClient.sharedInstance().sendPhoneName(Build.MODEL,
                RequestID.SEND_PHONE_NAME, getWAPIResponse);
    }

    public void processErrorCode() {
        final IReceiveResponse getErrorCodeResponse = new IReceiveResponse() {
            @Override
            public void onReceive(HTTPRequestResponse httpRequestResponse) {
                if (httpRequestResponse.getException() != null) {
                    LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "exception："
                            + httpRequestResponse.getException());
                    return;
                }

                if (httpRequestResponse.getStatusCode() != StatusCode.OK) {
                    return;
                }

                switch (httpRequestResponse.getRequestID()) {
                    case GET_ERROR:
                        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "response code："
                                + httpRequestResponse.getStatusCode());

                        if (!StringUtil.isEmpty(httpRequestResponse.getData())) {
                            mWAPIErrorCodeResponse = new Gson().fromJson(httpRequestResponse.getData(),
                                    WAPIErrorCodeResponse.class);
                            DIYInstallationState.setErrorCode(mWAPIErrorCodeResponse.getError());
                        }
                        break;

                    default:
                        break;
                }
            }
        };

        EnrollmentClient.sharedInstance()
                .getErrorCode(RequestID.GET_ERROR, getErrorCodeResponse);
    }


    /*************************************
     * Methods of Phone connect to TCC
     * Step 1 - Check mac
     * Step 2 - Add home and device
     *************************************/

    public void startConnectServer() {
//        mLoadingCallback.onLoad(mContext.getString(R.string.checking_device));
//        checkDeviceIsOnline();
    }


    public void checkDeviceIsOnline(IActivityReceive getMacResponse) {

        CheckDeviceIsOnlineTask checkMacTask = new CheckDeviceIsOnlineTask(mMacId, null, getMacResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(checkMacTask);

    }

}