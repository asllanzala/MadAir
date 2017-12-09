package com.honeywell.hch.airtouch.plateform.http.task;


import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by Jin Qian on 15/7/2.
 */
public class EmotionalBottleTask extends BaseRequestTask {
    private int mLocationId;
    private int mPeriodType;
    private String mSessionId;
    private IActivityReceive mIReceiveResponse;

    public EmotionalBottleTask(int locationId, int periodType, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        this.mLocationId = locationId;
        this.mPeriodType = periodType;
        mSessionId = UserInfoSharePreference.getSessionId();
        this.mIReceiveResponse = iReceiveResponse;
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reloginResult = reloginSuccessOrNot(RequestID.EMOTION_BOTTLE);
        if (reloginResult.isResult()) {
            ResponseResult result = HttpProxy.getInstance().getWebService()
                    .emotionBottle(mLocationId, mPeriodType, mSessionId, mIReceiveResponse);
//            Bundle response = result.getResponseData();
//            if (response != null) {
//                float pmvalue = response.getFloat("clean_dust");
//                float pahsValue = response.getFloat("PAHs");
//                float leadValue = pmvalue * 0.015f * 0.35f;
//                //cigeratte value
//                double cigerate = Math.round(pmvalue * 1000 * 0.0004 / 60);
//
//                float carfumeScond = Math.round((leadValue / 7.78f) * 1000);
//
//                Bundle bundle2 = new Bundle();
//                bundle2.putFloat("pm25_value", pmvalue);
//                bundle2.putFloat("lead_value", leadValue);
//                bundle2.putDouble("cigerate_value", cigerate);
//                bundle2.putFloat("PAHs", pahsValue);
//                bundle2.putFloat("fume_second", carfumeScond);
//                bundle2.putFloat("water_volume", response.getFloat("water_volume"));
//                result.setResponseData(bundle2);
//            }
            return result;
        }
        return reloginResult;

    }

//    private void setEmotionalBottleData(){
//
//        List<UserLocationData> userLocationDataList = UserAllDataContainer.getInstance().getUserLocationDataList();
//        if (userLocationDataList != null && userLocationDataList.size() > 0){
//            for (UserLocationData userLocationData : userLocationDataList){
//                if (userLocationData.getLocationID() == mLocationId){
//                    List<EmotionalData> emotionalDataList = userLocationData.getEmotionalData();
//                    if (emotionalDataList != null && emotionalDataList.size() > 0){
//                        for (EmotionalData emotionalData : emotionalDataList){
//
//                        }
//                    }
//
//                }
//            }
//        }
//    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {

        if (mIReceiveResponse != null) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }
}
