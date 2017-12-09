package com.honeywell.hch.airtouch.ui.main.manager.devices.manager;

import android.content.Intent;
import android.os.Bundle;

import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.SharePreferenceUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.ap.model.PhoneNameRequest;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.ble.manager.BLEManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModelSharedPreference;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.device.DeviceListRequest;
import com.honeywell.hch.airtouch.plateform.http.model.device.LocationIdListRequest;
import com.honeywell.hch.airtouch.plateform.http.model.group.GroupResponse;
import com.honeywell.hch.airtouch.plateform.http.model.group.ScenarioGroupRequest;
import com.honeywell.hch.airtouch.plateform.http.model.multi.ScenarioMultiCommTaskResponse;
import com.honeywell.hch.airtouch.plateform.http.model.multi.ScenarioMultiResponse;
import com.honeywell.hch.airtouch.plateform.http.model.multicommtask.MultiCommTaskRequest;
import com.honeywell.hch.airtouch.plateform.http.task.AddDeviceToGroupTask;
import com.honeywell.hch.airtouch.plateform.http.task.CommTask;
import com.honeywell.hch.airtouch.plateform.http.task.CreateGroupTask;
import com.honeywell.hch.airtouch.plateform.http.task.DeleteDeviceListTask;
import com.honeywell.hch.airtouch.plateform.http.task.DeleteDeviceTask;
import com.honeywell.hch.airtouch.plateform.http.task.DeleteGroupTask;
import com.honeywell.hch.airtouch.plateform.http.task.GetGroupByLocationIdTask;
import com.honeywell.hch.airtouch.plateform.http.task.IsDeviceMasterTask;
import com.honeywell.hch.airtouch.plateform.http.task.MoveOutDeviceFromGroupTask;
import com.honeywell.hch.airtouch.plateform.http.task.QuickActionTask;
import com.honeywell.hch.airtouch.plateform.http.task.SendScenarioToGroupTask;
import com.honeywell.hch.airtouch.plateform.http.task.UpdateGroupNameTask;
import com.honeywell.hch.airtouch.plateform.umeng.UmengUtil;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.control.manager.device.ControlBaseManager;
import com.honeywell.hch.airtouch.ui.control.manager.model.DeviceMode;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Qian Jin on 10/13/15.
 */
public class GroupManager extends ControlBaseManager {

    private static int mSendScenarioCount;
    public static final String BUNDLE_DEVICE_ID = "device_id";
    protected Map<Integer, Integer> mCommTaskMap = new HashMap<>();
    public static final int COMM_TASK_SUCCESS = 0;
    public static final int COMM_TASK_ERROR = -1;

    private int mCommTaskReturnCount = 0;

    private final int GROUPCACHEID = 100;
    private int mScenarioMode = DeviceMode.MODE_HOME; //成功保存的

    public GroupManager() {

    }

    public void setmGroupId(int mGroupId) {
        this.mGroupId = mGroupId;
    }


    public void createGroup(String groupName, int masterDeviceId,
                            int locationId, DeviceListRequest request) {
        CreateGroupTask requestTask
                = new CreateGroupTask(toURLEncoded(groupName), masterDeviceId, locationId, request, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }


    public void deleteGroup(int groupId) {
        // temp - server issue
        PhoneNameRequest request = new PhoneNameRequest("");
        DeleteGroupTask requestTask
                = new DeleteGroupTask(groupId, request, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void addDeviceToGroup(int groupId, DeviceListRequest request) {
        AddDeviceToGroupTask requestTask
                = new AddDeviceToGroupTask(groupId, request, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void moveOutDeviceFromGroup(int groupId, DeviceListRequest request) {
        MoveOutDeviceFromGroupTask requestTask
                = new MoveOutDeviceFromGroupTask(groupId, request, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void updateGroupName(String groupName, int groupId) {
        // temp - server issue
        PhoneNameRequest request = new PhoneNameRequest("");
        UpdateGroupNameTask requestTask
                = new UpdateGroupNameTask(toURLEncoded(groupName), groupId, request, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void getGroupByGroupId(boolean isRefreshOpr, int groupId) {
//        GetGroupByGroupIdTask requestTask
//                = new GetGroupByGroupIdTask(isRefreshOpr, groupId, null, mResponse);
//        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void getGroupByLocationId(boolean isRefreshOpr, int locationId) {
        GetGroupByLocationIdTask requestTask
                = new GetGroupByLocationIdTask(isRefreshOpr, locationId, null, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void isMasterDevice(int deviceId) {
        IsDeviceMasterTask requestTask
                = new IsDeviceMasterTask(deviceId, null, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void deleteDevice(int deviceId, UserLocationData userLocationDataItem) {
        DeleteDeviceTask requestTask
                = new DeleteDeviceTask(deviceId, userLocationDataItem, null, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void deleteDeviceList(Integer[] deviceId, UserLocationData userLocationDataItem) {
        DeleteDeviceListTask requestTask
                = new DeleteDeviceListTask(deviceId, userLocationDataItem, null, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    //口罩删除
    public void deleteMadAirDeviceList(String[] deviceId) {

        for (int i = 0; i < deviceId.length; i++) {
            deleteMadAirDevice(deviceId[i]);
        }
        UserAllDataContainer.shareInstance().updateMadAirData();
        Intent boradIntent = new Intent(HPlusConstants.SHORTTIME_REFRESH_END_ACTION);
        AppManager.getInstance().getApplication().getApplicationContext().sendBroadcast(boradIntent);
        boolean mResult = true;
        int mResponseCode = 200;
        RequestID mRequestId = RequestID.DELETE_MADAIR_DEVICE;
        ResponseResult mResponseResult = new ResponseResult(mResult, mResponseCode, "", mRequestId);
        mResponse.onReceive(mResponseResult);
    }

    public void deleteMadAirDevice(String macAddress) {

        BLEManager.getInstance().disconnectBle(macAddress);

        MadAirDeviceModelSharedPreference.deleteDevice(macAddress);

        UserAllDataContainer.shareInstance().deleteMadAirDevice(macAddress);
    }

    public void quickAction(int[] deviceIds) {
//        deviceIds = new int[]{28491, 28574, 28504, 28501};
        LocationIdListRequest locationIdListRequest = new LocationIdListRequest(deviceIds);
        QuickActionTask requestTask = new QuickActionTask(locationIdListRequest, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);

    }

    private void runCommTaskForDeleteDevice(HashMap<Integer, Integer> taskIdMap) {

        final HashMap<Integer, Integer> result = new HashMap<>();
        for (Integer deviceId : taskIdMap.keySet()) {
            result.put(deviceId, COMM_TASK_ERROR);
        }

        for (Map.Entry<Integer, Integer> taskItem : taskIdMap.entrySet()) {
            final int deviceId = taskItem.getKey();
            int taskId = taskItem.getValue();
            final IActivityReceive runCommTaskResponse = new IActivityReceive() {
                @Override
                public void onReceive(ResponseResult responseResult) {
                    if (responseResult.isResult()) {
                        switch (responseResult.getRequestId()) {
                            case COMM_TASK:
                                switch (responseResult.getFlag()) {

                                    case HPlusConstants.COMM_TASK_SUCCEED:
                                        // PO said that regard TIMEOUT as SUCCESS.... >_<
                                    case HPlusConstants.COMM_TASK_TIMEOUT:
                                        result.put(deviceId, COMM_TASK_SUCCESS);
                                    case HPlusConstants.COMM_TASK_FAILED:
                                        dealWithAllCommTaskReturn(result);
                                        break;

                                }
                                break;

                            default:
                                break;
                        }
                    } else {
                        dealWithAllCommTaskReturn(result);
//                        mErrorCallback.onError(responseResult, R.string.enroll_error);
                    }
                }
            };

            CommTask commTask = new CommTask(taskId, null, runCommTaskResponse, true);
            AsyncTaskExecutorUtil.executeAsyncTask(commTask);
        }

    }

    private void dealWithAllCommTaskReturn(HashMap<Integer, Integer> result) {
        mCommTaskReturnCount++;
        if (mCommTaskReturnCount >= result.size()) {
            mCommTaskReturnCount = 0;
            ResponseResult responseResult = new ResponseResult(false, RequestID.DELETE_DEVICE);
            //according to the new desgin
            for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
                if (entry.getValue() == COMM_TASK_ERROR) {
                    sendErrorCallBack(responseResult, R.string.delete_device_fail);
                    return;
                }
            }
            responseResult.setResult(true);
            sendSuccessCallBack(responseResult);
        }
    }


    public void sendScenarioToGroup(int groupId, ScenarioGroupRequest request) {
        setGroupScenarioIsFlashing(true);
        setSendGroupMode(groupId, request.getmGroupScenario());
        mScenarioMode = request.getmGroupScenario();
        SendScenarioToGroupTask requestTask
                = new SendScenarioToGroupTask(groupId, request, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
        mLastTaskId = requestTask.getmTaskUuid();
    }


    public static String toURLEncoded(String paramString) {
        if (paramString == null ||
                paramString.equals("")) {
            return "";
        }
        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void dealMultiCommTaskResult(ResponseResult result, boolean isSuccess) {
        LogUtil.log(LogUtil.LogLevel.INFO, "GroupControlFragment", "isSuccess: " + isSuccess);
        String controlTaskId = result.getResponseData().getString(DashBoadConstant.ARG_CONTROL_TASK_UUID);
        if (controlTaskId.equals(mLastTaskId)) {
            mLastTaskId = "";
            if (isSuccess) {
                getSucceedDevice(result);
                sendSuccessCallBack(result);
                handleCallBack(true);

                UmengUtil.GroupControlType groupCtrlStatus = result.getFlag() == HPlusConstants.COMM_TASK_ALL_FAILED ? UmengUtil.GroupControlType.GROUP_CONTROL_FAIL : UmengUtil.GroupControlType.GROUP_CONTROL_SUCCESS;
                UmengUtil.groupControlEvent(mGroupId, mScenarioMode, groupCtrlStatus, String.valueOf(result.getFlag()));
                return;
            }
            sendErrorCallBack(result, R.string.enroll_error);
            handleCallBack(false);
        }
    }

    private void handleCallBack(boolean isSuccess) {
        LogUtil.log(LogUtil.LogLevel.INFO, "GroupControlFragment", "handleCallBack----- ");
        setGroupScenarioIsFlashing(false);
        if (isSuccess) {
            setSuccessGroupMode(mGroupId, mScenarioMode);
        }
        sendStopFlashTask();

        if (!isSuccess) {
            UmengUtil.groupControlEvent(mGroupId, mScenarioMode, UmengUtil.GroupControlType.GROUP_CONTROL_FAIL, "");
        }
    }

    public IActivityReceive getmResponse() {
        return mResponse;
    }

    @Override
    public void handleResponseReceive(ResponseResult responseResult) {
        switch (responseResult.getRequestId()) {
            case CREATE_GROUP:
            case DELETE_GROUP:
            case ADD_DEVICE_TO_GROUP:
            case DELETE_DEVICE_FROM_GROUP:
            case GET_GROUP_BY_GROUP_ID:
            case GET_GROUP_BY_LOCATION_ID:
            case IS_DEVICE_MASTER:
            case DELETE_MADAIR_DEVICE:
                if (responseResult.isResult()
                        && (responseResult.getResponseCode() == StatusCode.OK)) {
                    sendSuccessCallBack(responseResult);
                } else {
                    sendErrorCallBack(responseResult, R.string.enroll_error);
                }
                break;

            case UPDATE_GROUP_NAME:
                Bundle bundle = responseResult.getResponseData();
                if (bundle != null) {
                    if (bundle.getInt(GroupResponse.CODE_ID) == UpdateGroupNameTask
                            .CODE_GROUP_NAME_ALREADY_EXIST) {
                        responseResult.setResponseCode(StatusCode.BAD_REQUEST);
                        sendErrorCallBack(responseResult, R.string.group_name_exist);
                        break;
                    }
                }
                if (responseResult.isResult()
                        && (responseResult.getResponseCode() == StatusCode.OK)) {
                    sendSuccessCallBack(responseResult);
                } else {
                    sendErrorCallBack(responseResult, R.string.enroll_error);
                }
                break;


            case DELETE_DEVICE:
                if (responseResult.isResult() && responseResult.getResponseCode() == StatusCode.OK) {
                    if (responseResult.getResponseData() != null) {
                        HashMap<Integer, Integer> taskMap = (HashMap<Integer, Integer>) responseResult.getResponseData()
                                .getSerializable(HPlusConstants.COMM_TASK_MAP_BUNDLE_KEY);

                        runCommTaskForDeleteDevice(taskMap);
                    } else {
                        sendErrorCallBack(responseResult, R.string.enroll_error);
                    }
                } else {
                    sendErrorCallBack(responseResult, R.string.enroll_error);
                }
                break;
            case SEND_SCENARIO_TO_GROUP:
            case QUICK_ACTION:
                String homeControlTaskId = responseResult.getResponseData().getString(DashBoadConstant.ARG_CONTROL_TASK_UUID);
                if (!StringUtil.isEmpty(mLastTaskId) && mLastTaskId.equals(homeControlTaskId)) {
                    startMultiCommTaskAfterControl(responseResult, homeControlTaskId);
                }
                break;

            default:
                sendErrorCallBack(responseResult, R.string.enroll_error);
                break;
        }
    }

    /**
     * 停止闪烁
     */
    public void sendStopFlashTask() {
        Intent intent = new Intent(HPlusConstants.BROADCAST_ACTION_STOP_FLASHINGTASK);
        AppManager.getInstance().getApplication().getApplicationContext().sendBroadcast(intent);
    }


    private void setGroupScenarioIsFlashing(boolean isFlashing) {
        SharePreferenceUtil.setPrefBoolean(HPlusConstants.PREFERENCE_GROUP_CONTROL_FLASH,
                Integer.toString(mGroupId + GROUPCACHEID), isFlashing);
    }

    private void setSuccessGroupMode(int groupID, int groupMode) {
        SharePreferenceUtil.setPrefInt(HPlusConstants.PREFERENCE_SUCCESS_GROUP_CONTROL_MODE,
                Integer.toString(groupID), groupMode);

    }

    public void setSendGroupMode(int groupID, int groupMode) {
        SharePreferenceUtil.setPrefInt(HPlusConstants.PREFERENCE_SEND_GROUP_CONTROL_MODE,
                Integer.toString(groupID), groupMode);

    }

    public void startMultiCommTaskAfterControl(ResponseResult responseResult, String controlTaskId) {
        if (responseResult.getResponseCode() == StatusCode.OK) {
            // Return success result to UI thread.
            ScenarioMultiResponse response = (ScenarioMultiResponse) responseResult.getResponseData()
                    .getSerializable(ScenarioMultiResponse.SCENARIO_DATA);
            if (response == null) {
                mLastTaskId = "";
                sendErrorCallBack(responseResult, R.string.group_control_all_fail);
                handleCallBack(false);
                return;
            }

            mCommTaskMap.clear();
            List<Integer> taskIds = new ArrayList<>();
            int taskCount = 0;
            for (ScenarioMultiCommTaskResponse res : response.getGroupCommTaskResponse()) {
                if (res.getCommTaskId() != 0) {
                    taskCount += res.getCommTaskId();
                    mCommTaskMap.put(res.getCommTaskId(), res.getDeviceId());
                    taskIds.add(res.getCommTaskId());
                }
                //TODO: 如果taskId ＝0，说明这个设备不可以被控制，所以也不需要发到云端去轮询查询
//                taskIds.add(res.getCommTaskId());

            }

            // If all task is 0, do not run multi-task.
            if (taskCount == 0) {
                mLastTaskId = "";
                sendErrorCallBack(responseResult, R.string.group_control_all_fail);
                handleCallBack(false);
            } else {
                runMultiCommTask(new MultiCommTaskRequest(taskIds), controlTaskId, HPlusConstants.FROM_GROUP_SCENARIO_CONTROL);
            }
        } else {
            mLastTaskId = "";
            sendErrorCallBack(responseResult, R.string.group_control_all_fail);
            handleCallBack(false);
        }
    }

    private void getSucceedDevice(ResponseResult result) {
        if (result == null || result.getResponseData() == null) {
            return;
        }

        ArrayList<Integer> successCmdIdList = result.getResponseData().getIntegerArrayList(HPlusConstants.BUNDLE_DEVICES_IDS);
        if (successCmdIdList == null || successCmdIdList.size() == 0) {
            return;
        }
        ArrayList<Integer> deviceIds = new ArrayList<>();

        for (Integer cmdId : successCmdIdList) {
            deviceIds.add(mCommTaskMap.get(cmdId));
        }
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList(HPlusConstants.BUNDLE_DEVICES_IDS, deviceIds);
        result.setResponseData(bundle);
    }

}
