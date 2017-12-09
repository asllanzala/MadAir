package com.honeywell.hch.airtouch.plateform.http.task;


import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by Vincent on 24/11/15.
 */
public class SmartLinkTask extends BaseRequestTask {
    private String mSessionId;
    private int mUserType;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;
    private String mEnrollmentType;
    private static final String TAG = "SmartLinkEnroll";

    public SmartLinkTask(String enrollmentType, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        this.mEnrollmentType = enrollmentType;
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
        this.mUserType = AppManager.isEnterPriseVersion() ?
                HPlusConstants.ENTERPRISE_ACCOUNT : HPlusConstants.PERSONAL_ACCOUNT;
        mSessionId = UserInfoSharePreference.getSessionId();
    }


    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.USER_LOGIN);
        if (reLoginResult.isResult()) {
            ResponseResult enrollmentStyle = HttpProxy.getInstance().getWebService()
                    .checkEnrollmentStyle(mEnrollmentType, mUserType, mSessionId,
                            mRequestParams, mIReceiveResponse);

            return enrollmentStyle;
        }
        return reLoginResult;
    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {

        if (mIReceiveResponse != null) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }

//    public static SmartEnrollScanEntity parseCheckTypeResponse(String result, SmartEnrollScanEntity scanEntity) {
//        JSONArray jsonArray = null;
//        try {
//            jsonArray = new JSONArray(result);
//            Log.i(TAG, "jsonArry: " + jsonArray.toString());
//            String[] enrollType = new String[jsonArray.length()];
//            for (int i = 0; i < jsonArray.length(); i++) {
//                enrollType[i] = jsonArray.getString(i);
//                Log.i("SmartLinkEnroll", "macID" + i + ": " + enrollType[i]);
//            }
//            scanEntity.setmEnrollType(enrollType);
//        } catch (JSONException ex) {
//            ex.printStackTrace();
//            scanEntity.setmEnrollType(null);
//            return scanEntity;
//        }
//        return scanEntity;
//    }
//
//    public static boolean parseURL(String recode, SmartEnrollScanEntity mSmartEnrollScanEntity) {
//        final int STARTINDEX = 0;
//        final int ENDINDEX = 6;
//        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "recode: " + recode);
//        if (recode.contains(EnrollmentConstant.JDBASEURL)) {
//            String[] JD = recode.split("\\?");
//            Log.i(TAG, "JD.length: " + JD.length);
//            if (JD.length >= 2) {
//                String jdBase64 = JD[1];
//                String[] jdValue = jdBase64.split("\\=");
//                if ("g".equals(jdValue[0])) {
//                    String base64url = "";
//                    try {
//                        base64url = StringUtil.decodeURL(jdValue[1]);
//                    } catch (UnsupportedEncodingException ex) {
//                        return false;
//                    }
//                    String mProductUUID = StringUtil.parseJDURL(base64url, STARTINDEX, ENDINDEX);
//                    String mMacID = StringUtil.parseJDURL(base64url, ENDINDEX, base64url.length());
//                    Log.i(TAG, "mProductUUID: " + mProductUUID);
//                    Log.i(TAG, "mMacID: " + mMacID);
//                    mSmartEnrollScanEntity.setData(mProductUUID, mMacID, mProductUUID, null, "", false);
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        } else if (recode.contains(EnrollmentConstant.HONEYURLURL)) {
//            String[] honeyCode = recode.split("\\?");
//            if (honeyCode.length == 1) {
//                // http://hch.honeywell.com.cn/landingpage/air-touch-install.html
//                mSmartEnrollScanEntity.setData("", "", HPlusConstants.AIR_TOUCH_S_MODEL, null, "",false);
//                Log.i(TAG, "model: " + HPlusConstants.AIR_TOUCH_S_MODEL);
//                return true;
//            } else {
//                String valueCode = honeyCode[1];
//                String[] macValue = valueCode.split("\\&");
//                Log.i(TAG, "honeyValue.lenght: " + macValue.length);
//                if (macValue.length == 1) {
//                    //http://hch.honeywell.com.cn/landingpage/air-touch-install.html?model=KJ700G-PAC2127W
//                    String[] modelString = valueCode.split("\\=");
//                    Log.i(TAG, "model: " + modelString[1]);
//                    mSmartEnrollScanEntity.setData("", "", modelString[1], null, "", false);
//                    return true;
//                } else {
//                    String model = macValue[0].split("\\=")[1];
//                    Log.i(TAG, "model: " + model);
//                    String[] isSmart = macValue[1].split("\\=");
//                    if ("h".equals(isSmart[0])) {
//                        //http://hch.honeywell.com.cn/landingpage/air-touch-install.html?model=XXXXXXXXXXX&h=XXXXXXXXXXX
//                        String base64url = "";
//                        try {
//                            base64url = StringUtil.decodeURL(isSmart[1]);
//                        } catch (UnsupportedEncodingException ex) {
//                            return false;
//                        }
////                        String mProductUUID = StringUtil.parseJDURL(base64url, STARTINDEX, ENDINDEX);
//                        String mMacID = StringUtil.parseJDURL(base64url, STARTINDEX, base64url.length());
//                        String mProductUUID = mMacID;
//                        mSmartEnrollScanEntity.setData(mProductUUID, mMacID, model, null, "", false);
//                        Log.i(TAG, "mProductUUID: " + mProductUUID);
//                        Log.i(TAG, "mMacID: " + mMacID);
//                        return true;
//                    } else {
//                        //http://hch.honeywell.com.cn/landingpage/air-touch-install.html?model=XXXXXXXXXXX&country=India
//                        if ("country".equals(isSmart[0])) {
//                            Log.i(TAG, "country: " + isSmart[1]);
//                            mSmartEnrollScanEntity.setData("", "", model, null, isSmart[1], false);
//                            return true;
//                        }
//                    }
//                }
//            }
//
//        } else if (HPlusConstants.AIR_TOUCH_P_MODEL.equals(recode) || HPlusConstants.AIR_TOUCH_S_MODEL.equals
//                (recode)) {
//            //PAC45M1022W
//            Log.i(TAG, "TOUCH_P_MODEL: " + recode);
//            mSmartEnrollScanEntity.setData("", "", HPlusConstants.AIR_TOUCH_P_MODEL, null, "",false);
//            return true;
//        } else if (recode.contains(EnrollmentConstant.AIRTOUCHBASEURL)) {
//            //http://eccap.honeywell.cn/eacmobileinstall.html?model=A-1
//            String modle = (recode.split("\\?")[1].split("\\="))[1];
//            Log.i(TAG, "model: " + modle);
//            mSmartEnrollScanEntity.setData("", "", modle, null, "", false);
//            return true;
//        }
//        return false;
//    }

}
