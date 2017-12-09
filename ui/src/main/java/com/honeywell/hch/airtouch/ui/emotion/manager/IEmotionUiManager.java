package com.honeywell.hch.airtouch.ui.emotion.manager;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.EmotionBottleResponse;

import java.util.List;

/**
 * Created by honeywell on 26/12/2016.
 */

public interface IEmotionUiManager {

    void setSuccessCallback(EmotionManager.SuccessCallback successCallback);

    void setErrorCallback(EmotionManager.ErrorCallback errorCallback);

    void getPMLevelFromServer(int requestTimeParamer);

    List<EmotionBottleResponse> getEmotionBottleResponse(ResponseResult responseResult, UserLocationData userLocationData);


    void getDustAndPm25ByLocationID(String fromDate, String toDate);

    void getVolumeAndTdsByLocationID(String fromDate, String toDate);

    void getTotalDustByLocationID();

    void getTotalVolumeByLocationID();
}
