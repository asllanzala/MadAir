package com.honeywell.hch.airtouch.plateform.http.task;


import android.os.Bundle;

import com.google.gson.Gson;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.download.DownloadUtils;
import com.honeywell.hch.airtouch.plateform.http.model.enroll.EnrollDeviceTypeModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 10/3/16.
 */
public class GetEnrollDeviceTypeTask extends BaseRequestTask {
    private final String TAG = "GetEnrollDeviceTypeTask";
    private IActivityReceive mIReceiveResponse;

    public GetEnrollDeviceTypeTask(IActivityReceive iReceiveResponse) {
        this.mIReceiveResponse = iReceiveResponse;
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {
        ResponseResult result = new ResponseResult(true, StatusCode.OK, "", RequestID.ENROLL_DEVICE_TYPE);
        try {

            // Check upgrade
            List<EnrollDeviceTypeModel> tempList = new ArrayList<>();
            String jsonData = DownloadUtils.getInstance().downloadJsonData(HPlusConstants.downLoadUrl());
            LogUtil.log(LogUtil.LogLevel.INFO, "SmartLinkEnroll", "model: " + jsonData);
            JSONArray responseArray = new JSONArray(jsonData);
            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject responseJSONObj = responseArray.getJSONObject(i);
                EnrollDeviceTypeModel enrollDeviceTypeModel = new Gson().fromJson(responseJSONObj.toString(), EnrollDeviceTypeModel.class);
                tempList.add(enrollDeviceTypeModel);
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(EnrollDeviceTypeModel.ENROLL_DEVICE_TYPE_DATA, (Serializable) tempList);
            result.setResponseData(bundle);
            return result;
        } catch (Exception e) {
            result.setResponseCode(StatusCode.DOWN_LOAD_FAULT);
            e.printStackTrace();
            LogUtil.log(LogUtil.LogLevel.ERROR, "SmartLinkEnroll", "Download and parse error! " + e.getMessage());
        }
        result.setResponseCode(StatusCode.DOWN_LOAD_FAULT);
        return result;

    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {
        if (mIReceiveResponse != null) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }
}
