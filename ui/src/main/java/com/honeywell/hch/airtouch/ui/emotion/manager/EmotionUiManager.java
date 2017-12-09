package com.honeywell.hch.airtouch.ui.emotion.manager;


import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.EmotionBottleResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 15/8/16.
 */
public class EmotionUiManager implements IEmotionUiManager {

    private final String TAG = "HomeManagerUiManager";
    private final int EMPTY = 0;
    private EmotionManager managerManager;
    private int mLocationId;


    public EmotionUiManager() {
        managerManager = new EmotionManager();
    }

    public EmotionUiManager(int locationId) {
        mLocationId = locationId;
        managerManager = new EmotionManager();
    }


    /**
     * 设置success callBack
     *
     * @param successCallback
     */
    public void setSuccessCallback(EmotionManager.SuccessCallback successCallback) {
        managerManager.setSuccessCallback(successCallback);
    }

    /**
     * 设置error callBack
     *
     * @param errorCallback
     */
    public void setErrorCallback(EmotionManager.ErrorCallback errorCallback) {
        managerManager.setErrorCallback(errorCallback);
    }

    public void getPMLevelFromServer(int requestTimeParamer) {
        managerManager.getPMLevelFromServer(mLocationId, requestTimeParamer);
    }

    public List<EmotionBottleResponse> getEmotionBottleResponse(ResponseResult responseResult, UserLocationData userLocationData) {
        List<EmotionBottleResponse> emotionBottleResponseList = (List<EmotionBottleResponse>) responseResult.getResponseData().getSerializable(EmotionBottleResponse.EMOTION_RESP_DATA);
        List<EmotionBottleResponse> emotionBottleResponseTempleList = new ArrayList<>();
        if (userLocationData != null && userLocationData.isOwnAirTouchSeries()) {
            for (EmotionBottleResponse emotionBottleResponse : emotionBottleResponseList) {
                if (emotionBottleResponse.getEmtoionType() == HPlusConstants.EMOTION_TYPE_AIRTOUCH) {
                    emotionBottleResponseTempleList.add(emotionBottleResponse);
                }
            }
        }
        if (userLocationData != null && userLocationData.isOwnWaterSeries()) {
            for (EmotionBottleResponse emotionBottleResponse : emotionBottleResponseList) {
                if (emotionBottleResponse.getEmtoionType() == HPlusConstants.EMOTION_TYPE_WATER) {
                    emotionBottleResponseTempleList.add(emotionBottleResponse);
                }
            }
        }
        return emotionBottleResponseTempleList;
    }

    @Override
    public void getDustAndPm25ByLocationID(String fromDate, String toDate) {
        managerManager.getDustAndPm25ByLocationID(mLocationId, fromDate, toDate);
    }

    @Override
    public void getVolumeAndTdsByLocationID(String fromDate, String toDate) {
        managerManager.getVolumeAndTdsByLocationID(mLocationId, fromDate, toDate);
    }

    @Override
    public void getTotalDustByLocationID() {
        managerManager.getTotalDustByLocationID(mLocationId);
    }

    @Override
    public void getTotalVolumeByLocationID() {
        managerManager.getTotalVolumeByLocationID(mLocationId);
    }

    public int getmLocationId() {
        return mLocationId;
    }
}
