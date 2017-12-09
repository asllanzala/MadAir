package com.honeywell.hch.airtouch.ui.main.manager.devices.manager;

import android.os.Bundle;

import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.plateform.http.model.group.GroupResponse;
import com.honeywell.hch.airtouch.plateform.http.model.multicommtask.MultiCommTaskListResponse;
import com.honeywell.hch.airtouch.plateform.http.model.multicommtask.MultiCommTaskResponse;
import com.honeywell.hch.airtouch.plateform.http.task.UpdateGroupNameTask;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 12/1/16.
 */
public class GroupManagerResponseTest {
    private GroupManager mGroupManager;
    private boolean result = false;
    private RequestID[] requestIDs = new RequestID[]{RequestID.CREATE_GROUP, RequestID.DELETE_GROUP, RequestID.ADD_DEVICE_TO_GROUP, RequestID.DELETE_DEVICE_FROM_GROUP,
            RequestID.GET_GROUP_BY_GROUP_ID, RequestID.GET_GROUP_BY_LOCATION_ID, RequestID.IS_DEVICE_MASTER, RequestID.GET_HOME_PM25, RequestID.UPDATE_GROUP_NAME};

    public GroupManagerResponseTest(GroupManager groupManager) {
        mGroupManager = groupManager;
    }

    public void testGroupResponse() {
        for (RequestID requestID : requestIDs) {
            resetResult();
            ResponseResult responseResult = constructResponse(false, requestID, StatusCode.OK);
            setError();
            mGroupManager.getmResponse().onReceive(responseResult);
            Assert.assertEquals(result, true);

            resetResult();
            responseResult = constructResponse(true, requestID, StatusCode.BAD_REQUEST);
            setError();
            mGroupManager.getmResponse().onReceive(responseResult);
            Assert.assertEquals(result, true);


            resetResult();
            responseResult = constructResponse(true, requestID, StatusCode.OK);
            setSuccess();
            mGroupManager.getmResponse().onReceive(responseResult);
            Assert.assertEquals(result, true);

            testUpdateGroupNameResponse(requestID);
        }
    }

    private void testUpdateGroupNameResponse(RequestID requestID) {
        if (requestID.equals(RequestID.UPDATE_GROUP_NAME)) {
            resetResult();
            ResponseResult responseResult = constructResponse(true, RequestID.UPDATE_GROUP_NAME, StatusCode.OK);
            Bundle bundle = new Bundle();
            bundle.putInt(GroupResponse.CODE_ID, UpdateGroupNameTask
                    .CODE_GROUP_NAME_ALREADY_EXIST);
            responseResult.setResponseData(bundle);
            mGroupManager.getmResponse().onReceive(responseResult);
            Assert.assertEquals(result, true);
        }

    }

    public void testGetSucceedDevice() {
        resetResult();
        ResponseResult responseResult = constructResponse(true, RequestID.UPDATE_GROUP_NAME, StatusCode.OK);
        responseResult.setResponseData(new Bundle());
//        mGroupManager.getSucceedDevice(responseResult);
        Assert.assertEquals(result, false);

        resetResult();
        responseResult = constructResponse(true, RequestID.UPDATE_GROUP_NAME, StatusCode.OK);
        Bundle bundle = new Bundle();
        mockMultiCommTask(bundle);
        responseResult.setResponseData(bundle);
//        mGroupManager.getSucceedDevice(responseResult);
        setSuccess();
        Assert.assertEquals(result, false);

    }

    private void mockMultiCommTask(Bundle bundle) {
        MultiCommTaskListResponse taskListResponse = new MultiCommTaskListResponse();
        List<MultiCommTaskResponse> listMultiCommTaskListResponse = new ArrayList<MultiCommTaskResponse>();
        taskListResponse.setmMultiCommTaskResponses(listMultiCommTaskListResponse);
        for (int i = 0; i < 3; i++) {
            MultiCommTaskResponse taskResponse = new MultiCommTaskResponse("Succeeded", "", i);
            taskListResponse.putMultiCommTaskResponses(taskResponse);
        }
        bundle.putSerializable(MultiCommTaskListResponse.MUTLICOMMTASK, taskListResponse);
    }

    private void setError() {
        mGroupManager.setErrorCallback(new GroupManager.ErrorCallback() {
            @Override
            public void onError(ResponseResult responseResult, int id) {
                result = true;
            }
        });
    }

    private void setSuccess() {
        mGroupManager.setSuccessCallback(new GroupManager.SuccessCallback() {
            @Override
            public void onSuccess(ResponseResult responseResult) {
                result = true;
            }
        });
    }

    private void resetResult() {
        result = false;
    }

    private ResponseResult constructResponse(boolean isSuccess, RequestID requestId, int statusCode) {
        ResponseResult responseResult = new ResponseResult(isSuccess, requestId);
        responseResult.setResponseCode(statusCode);
        return responseResult;
    }

    private ResponseResult constructResponse() {
        ResponseResult responseResult = new ResponseResult();
        return responseResult;
    }
}
