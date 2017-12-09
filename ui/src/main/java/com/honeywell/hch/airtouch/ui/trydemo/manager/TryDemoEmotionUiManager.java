package com.honeywell.hch.airtouch.ui.trydemo.manager;


import android.os.Handler;
import android.os.Message;

import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.water.model.WaterDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.EmotionBottleResponse;
import com.honeywell.hch.airtouch.ui.emotion.manager.EmotionManager;
import com.honeywell.hch.airtouch.ui.emotion.manager.IEmotionUiManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 15/8/16.
 */
public class TryDemoEmotionUiManager implements IEmotionUiManager {

    private EmotionManager.SuccessCallback successCallback;


    private int mRequestTimeParamer = 0;

    public TryDemoEmotionUiManager() {

    }

    public TryDemoEmotionUiManager(int locationId) {
    }


    /**
     * 设置success callBack
     *
     * @param successCallback
     */
    public void setSuccessCallback(EmotionManager.SuccessCallback successCallback) {
        this.successCallback = successCallback;
    }

    /**
     * 设置error callBack
     *
     * @param errorCallback
     */
    public void setErrorCallback(EmotionManager.ErrorCallback errorCallback) {
//        managerManager.setErrorCallback(errorCallback);
    }

    public void getPMLevelFromServer(int requestTimeParamer) {
//        managerManager.getPMLevelFromServer(mLocationId, requestTimeParamer);
        mRequestTimeParamer = requestTimeParamer;

        ResponseResult responseResult = new ResponseResult();
        responseResult.setResult(true);
        responseResult.setRequestId(RequestID.EMOTION_BOTTLE);
        successCallback.onSuccess(responseResult);
    }

    public List<EmotionBottleResponse> getEmotionBottleResponse(ResponseResult responseResult, UserLocationData userLocationData) {
        List<EmotionBottleResponse> emotionBottleResponseList = (List<EmotionBottleResponse>) responseResult.getResponseData().getSerializable(EmotionBottleResponse.EMOTION_RESP_DATA);
        List<EmotionBottleResponse> emotionBottleResponseTempleList = new ArrayList<>();

        EmotionBottleResponse emotionBottleResponse = new EmotionBottleResponse();
        emotionBottleResponse.setEmtoionType(HPlusConstants.EMOTION_TYPE_AIRTOUCH);
        emotionBottleResponse.setCleanDust(getTotalDust());
        emotionBottleResponseTempleList.add(emotionBottleResponse);


        EmotionBottleResponse emotionBottleResponse2 = new EmotionBottleResponse();
        emotionBottleResponse2.setEmtoionType(HPlusConstants.EMOTION_TYPE_WATER);
        emotionBottleResponse2.setOutflowVolume(getTotalOutflowVolume());
        emotionBottleResponseTempleList.add(emotionBottleResponse2);

        return emotionBottleResponseTempleList;
    }

    @Override
    public void getDustAndPm25ByLocationID(String fromDate, String toDate) {

    }

    @Override
    public void getVolumeAndTdsByLocationID(String fromDate, String toDate) {

    }

    @Override
    public void getTotalDustByLocationID() {

    }

    @Override
    public void getTotalVolumeByLocationID() {

    }

    private float getTotalDust(){
        if (mRequestTimeParamer == 0){
            return 80;
        }
        else if (mRequestTimeParamer == 1){
            return 400;
        }else if (mRequestTimeParamer == 2){
            return 120 * 1000;
        }else {
            return 800 * 1000;
        }
    }

    private float getTotalOutflowVolume(){
        if (mRequestTimeParamer == 0){
            return 5;
        }
        else if (mRequestTimeParamer == 1){
            return 30;
        }else if (mRequestTimeParamer == 2){
            return 908;
        }else {
            return 20 * 1000;
        }
    }

}
