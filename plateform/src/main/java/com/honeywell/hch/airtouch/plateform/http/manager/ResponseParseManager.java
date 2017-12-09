package com.honeywell.hch.airtouch.plateform.http.manager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.honeywell.hch.airtouch.library.http.model.HTTPRequestResponse;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.library.util.DateTimeUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthMessageModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.AuthGroupDeviceListModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.AuthHomeModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.CheckAuthUserResponse;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.GetAuthMessagesResponse;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.GetAuthUnreadMsgResponse;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.GrantAuthToDeviceResponse;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.HandleAuthMessageResponse;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.RemoveAuthResponse;
import com.honeywell.hch.airtouch.plateform.http.model.device.CapabilityResponse;
import com.honeywell.hch.airtouch.plateform.http.model.device.CommTaskResponse;
import com.honeywell.hch.airtouch.plateform.http.model.emotion.GetDustAndPm25Response;
import com.honeywell.hch.airtouch.plateform.http.model.emotion.GetVolumeAndTdsResponse;
import com.honeywell.hch.airtouch.plateform.http.model.group.CreateGroupResponse;
import com.honeywell.hch.airtouch.plateform.http.model.group.GroupData;
import com.honeywell.hch.airtouch.plateform.http.model.group.GroupResponse;
import com.honeywell.hch.airtouch.plateform.http.model.group.SingleGroupItem;
import com.honeywell.hch.airtouch.plateform.http.model.message.GetUnReadResponse;
import com.honeywell.hch.airtouch.plateform.http.model.message.MessageDetailResponse;
import com.honeywell.hch.airtouch.plateform.http.model.message.MessageModel;
import com.honeywell.hch.airtouch.plateform.http.model.multi.ScenarioMultiResponse;
import com.honeywell.hch.airtouch.plateform.http.model.multicommtask.MultiCommTaskListResponse;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.EmotionBottleResponse;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.GatewayAliveResponse;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.RecordCreatedResponse;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.SmsSendResponse;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.SmsValidResponse;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.UserLoginResponse;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.UserValidateResponse;
import com.honeywell.hch.airtouch.plateform.http.model.weather.WeatherPageData;
import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.Hour;
import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.HourlyFuture;
import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.HourlyHistory;
import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.Weather;
import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.WeatherData;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wuyuan on 15/5/20.
 */
public class ResponseParseManager {
    private final static String TAG = "AirTouchResponseParse";

    /**
     * parse Response of Check Mac on Tcc
     *
     * @param response http response
     * @return
     */
    public static ResponseResult parseCheckMacResponse(HTTPRequestResponse response, RequestID requestID) {
        ResponseResult result = new ResponseResult(true, response.getStatusCode(), "", response.getRequestID());
        result.setFlag(HPlusConstants.CHECK_MAC_AGAIN);
        try {
            if (!StringUtil.isEmpty(response.getData())) {
                GatewayAliveResponse gatewayAliveResponse = new Gson().fromJson(response.getData(),
                        GatewayAliveResponse.class);

                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "CHECK_MAC:" + gatewayAliveResponse.isAlive());
                if (gatewayAliveResponse.isAlive()) {
                    result.setFlag(HPlusConstants.CHECK_MAC_ALIVE);
                } else {
                    result.setFlag(HPlusConstants.CHECK_MAC_AGAIN);
                }
            } else {
                result.setFlag(HPlusConstants.CHECK_MAC_AGAIN);
            }
        } catch (Exception e) {

        }


        return result;
    }


    /**
     * parse User login Response
     *
     * @param response  http response
     * @param requestID requestID request id
     * @return
     */
    public static ResponseResult parseUserLoginResponse(HTTPRequestResponse response, RequestID requestID) {

        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", response.getRequestID());
            if (!StringUtil.isEmpty(response.getData())) {
                UserLoginResponse userLoginResponse = new Gson().fromJson(response.getData(), UserLoginResponse.class);

                if (userLoginResponse.getUserInfo() != null) {
                    if (!userLoginResponse.getUserInfo().getIsActivated()) {
                        result.setResult(false);
                        result.setResponseCode(StatusCode.UNACTIVATED_ACCOUNT);
                        return result;
                    }

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(HPlusConstants.USER_LOGIN_INFO, userLoginResponse);
                    result.setResponseData(bundle);
                    return result;
                }
            } else {
                result.setResult(false);
                result.setResponseCode(StatusCode.NO_RESPONSE_DATA);
                return result;
            }
        }
        return getErrorResponse(response, requestID);
    }


    public static ResponseResult parseRefreshSessionResponse(HTTPRequestResponse response, RequestID requestID) {

        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", response.getRequestID());
            if (!StringUtil.isEmpty(response.getData())) {
                UserLoginResponse userLoginResponse = new Gson().fromJson(response.getData(), UserLoginResponse.class);

                if (!StringUtil.isEmpty(userLoginResponse.getSessionId())) {
                    UserInfoSharePreference.saveLoginSession(userLoginResponse.getSessionId());
                    UserInfoSharePreference.saveLastUpdateSession(System.currentTimeMillis());
                    return result;
                }
            }
        } else if (response != null && response.getStatusCode() == StatusCode.UNAUTHORIZED) {
            Intent logOutIntent = new Intent(HPlusConstants.LOGOUT_ACTION);
            AppManager.getInstance().getApplication().getApplicationContext().sendOrderedBroadcast(logOutIntent, null);
            ResponseResult result = new ResponseResult(false, StatusCode.UPDATE_SESSION_PASSWORD_CHANGE, "", response.getRequestID());
            return result;
        }

        if (response != null) {
            return new ResponseResult(false, response.getStatusCode(), "", response.getRequestID());
        }
        return new ResponseResult(false, StatusCode.RETURN_RESPONSE_NULL, "", response.getRequestID());
    }

    /**
     * get bottle response
     *
     * @param response
     * @return
     */
    public static ResponseResult parseBottleResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

//            if (!StringUtil.isEmpty(response.getDeviceList())) {
//                EmotionBottleResponse emotionBottleResponse = new Gson().fromJson(response.getDeviceList(), EmotionBottleResponse.class);
//                Bundle bundle = new Bundle();
//                bundle.putFloat("clean_dust", emotionBottleResponse.getCleanDust());
//                bundle.putFloat("heavy_metal", emotionBottleResponse.getHeavyMetal());
//                bundle.putFloat("PAHs", emotionBottleResponse.getPahs());
//                result.setResponseData(bundle);
//                return result;
//            }

            if (!StringUtil.isEmpty(response.getData())) {
                Log.i(TAG, "response.getDeviceList()： " + response.getData());

                try {
                    JSONArray responseArray = new JSONArray(response.getData());

                    List<EmotionBottleResponse> emotionBottleResponseList = new ArrayList<>();
                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject responseJSON = responseArray.getJSONObject(i);
                        EmotionBottleResponse emotionBottleResponse
                                = new Gson().fromJson(responseJSON.toString(), EmotionBottleResponse.class);
                        emotionBottleResponseList.add(emotionBottleResponse);
                    }

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(EmotionBottleResponse.EMOTION_RESP_DATA, (Serializable) emotionBottleResponseList);
                    result.setResponseData(bundle);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return result;
            }
        }

        return getErrorResponse(response, requestID);

    }


    public static ResponseResult parseGetGroupByLocationIdResponse(HTTPRequestResponse response, RequestID requestID, int locationId) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

            if (!StringUtil.isEmpty(response.getData())) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "response.getDeviceList()： " + response.getData());
                UserInfoSharePreference.saveGroupCachesData(response.getData(), locationId);

                LocationAndDeviceParseManager.getInstance().parseJsonDataToGroupData(locationId, response.getData(), false);

                return result;
            }
        }

        return getErrorResponse(response, requestID);
    }


    /**
     * get register response
     *
     * @param response
     * @return
     */
    public static ResponseResult getRegsterResponse(HTTPRequestResponse response) {
        ResponseResult result = new ResponseResult(true, StatusCode.OK, "", response.getRequestID());

        if (response != null && !StringUtil.isEmpty(response.getData()) &&
                response.getStatusCode() == StatusCode.CREATE_OK) {
            return result;
        }
        return getErrorResponse(response, response.getRequestID());
    }

    /**
     * get location response and set it to UserLocations
     *
     * @param response
     * @return
     */
    public static ResponseResult parseGetLocationResponse(HTTPRequestResponse response, RequestID requestID) {
        ResponseResult result = new ResponseResult(true, StatusCode.OK, "", response.getRequestID());
        if (!StringUtil.isEmpty(response.getData()) && response.getStatusCode() == StatusCode.OK) {
            try {
                UserInfoSharePreference.saveLocationCachesData(response.getData());
                LocationAndDeviceParseManager.getInstance().parseJsonDataToUserLocationObject(response.getData());
                UserAllDataContainer.shareInstance().setDashboardLoadSuccess();
            } catch (JSONException e) {
                e.printStackTrace();
                UserAllDataContainer.shareInstance().setDashboardLoadFailed();
            }

            return result;
        } else {
            result.setResponseCode(response.getStatusCode());
            result.setExceptionMsg(response.getData());
        }
        UserAllDataContainer.shareInstance().setDashboardLoadFailed();
        return getErrorResponse(response, requestID);
    }


    public static ResponseResult parseAddLocationResponse(HTTPRequestResponse response, RequestID requestID) {
        ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

        if (response != null) {
            if (response.getStatusCode() == StatusCode.CREATE_OK) {
                RecordCreatedResponse recordCreatedResponse = new Gson().fromJson(response.getData(),
                        RecordCreatedResponse.class);
                Bundle bundle = new Bundle();
                bundle.putInt(HPlusConstants.LOCATION_ID_BUNDLE_KEY, recordCreatedResponse.getId());
                result.setResponseData(bundle);

                return result;
            } else {
                return getErrorResponse(response, requestID);
            }
        }

        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseAddDeviceResponse(HTTPRequestResponse response, RequestID requestID) {
        ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

        if (response != null) {
            if (response.getStatusCode() == StatusCode.CREATE_OK) {
                RecordCreatedResponse recordCreatedResponse = new Gson().fromJson(response.getData(),
                        RecordCreatedResponse.class);
                Bundle bundle = new Bundle();
                bundle.putInt(HPlusConstants.COMM_TASK_BUNDLE_KEY, recordCreatedResponse.getId());
                result.setResponseData(bundle);

                return result;
            }
        }

        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseCreateGroupResponse(HTTPRequestResponse response, RequestID requestID) {
        ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

        if (response != null) {
            if (response.getStatusCode() == StatusCode.OK) {

                CreateGroupResponse createGroupResponse = new Gson().fromJson(response.getData(),
                        CreateGroupResponse.class);
                Bundle bundle = new Bundle();
                bundle.putInt(CreateGroupResponse.GROUP_ID, createGroupResponse.getGroupId());
                bundle.putInt(CreateGroupResponse.CODE_ID, createGroupResponse.getCode());
                result.setResponseData(bundle);

                return result;
            } else {
                return getErrorResponse(response, requestID);
            }
        }
        return null;
    }

    public static ResponseResult parseControlDeviceResponse(HTTPRequestResponse response, RequestID requestID) {
        ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

        if (response != null) {
            if (response.getStatusCode() == StatusCode.CREATE_OK) {
                RecordCreatedResponse recordCreatedResponse = new Gson().fromJson(response.getData(),
                        RecordCreatedResponse.class);
                Bundle bundle = new Bundle();
                bundle.putInt(HPlusConstants.COMM_TASK_BUNDLE_KEY, recordCreatedResponse.getId());
                result.setResponseData(bundle);

                return result;
            } else {
                return getErrorResponse(response, requestID);
            }
        }

        return null;
    }

    public static ResponseResult parseGroupResponse(HTTPRequestResponse response, RequestID requestID) {
        ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

        if (response != null) {
            if (response.getStatusCode() == StatusCode.OK) {
                GroupResponse groupResponse = new Gson().fromJson(response.getData(),
                        GroupResponse.class);
                Bundle bundle = new Bundle();
                bundle.putInt(GroupResponse.CODE_ID, groupResponse.getCode());
                result.setResponseData(bundle);
                return result;
            } else if (response.getStatusCode() == 401) { // 401 - group does not exist
                Bundle bundle = new Bundle();
                bundle.putInt(GroupResponse.CODE_ID, response.getStatusCode());
                result.setResponseData(bundle);
                return result;
            } else {
                return getErrorResponse(response, requestID);
            }
        }
        return null;
    }

    public static ResponseResult parseIsMasterResponse(HTTPRequestResponse response, RequestID requestID) {
        ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

        if (response != null) {
            if (response.getStatusCode() == StatusCode.OK) {

                return result;
            } else {
                return getErrorResponse(response, requestID);
            }
        }
        return null;
    }

    public static ResponseResult parseScenarioResponse(HTTPRequestResponse response, RequestID requestID) {
        ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

        if (response != null) {
            if (response.getStatusCode() == StatusCode.OK) {

                ScenarioMultiResponse res = new Gson().fromJson(response.getData(),
                        ScenarioMultiResponse.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ScenarioMultiResponse.SCENARIO_DATA, res);
                result.setResponseData(bundle);

                return result;
            } else {
                return getErrorResponse(response, requestID);
            }
        }
        return null;
    }

    public static ResponseResult parseMultiCommTaskResponse(HTTPRequestResponse response, RequestID requestID) {
        ResponseResult result = new ResponseResult(true, StatusCode.OK, "", response.getRequestID());
        if (response.getStatusCode() == StatusCode.OK) {
            if (!StringUtil.isEmpty(response.getData())) {
                if (!StringUtil.isEmpty(response.getData())) {
                    MultiCommTaskListResponse commTaskData
                            = new Gson().fromJson(response.getData(), MultiCommTaskListResponse.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(MultiCommTaskListResponse.MUTLICOMMTASK, commTaskData);
                    result.setResponseData(bundle);
                }
            }
            return result;
        } else {
            return getErrorResponse(response, requestID);
        }
    }

    public static ResponseResult parseCommTaskResponse(HTTPRequestResponse response, RequestID requestID) {
        ResponseResult result = new ResponseResult(true, StatusCode.OK, "", response.getRequestID());
        if (response.getStatusCode() == StatusCode.OK) {
            if (!StringUtil.isEmpty(response.getData())) {
                CommTaskResponse commTaskResponse = new Gson().fromJson(response.getData(),
                        CommTaskResponse.class);
                if (commTaskResponse.getState().equals("Succeeded")) {
                    result.setFlag(HPlusConstants.COMM_TASK_SUCCEED);
                } else if (commTaskResponse.getState().equals("Failed")) {
                    result.setFlag(HPlusConstants.COMM_TASK_FAILED);
                } else {
                    result.setFlag(HPlusConstants.COMM_TASK_RUNNING);
                }
            }
            return result;
        } else {
            return getErrorResponse(response, requestID);
        }
    }

    public static ResponseResult parseCommonResponse(HTTPRequestResponse response, RequestID requestID) {
        ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

        if (response != null) {
            if (response.getStatusCode() >= StatusCode.OK
                    && (response.getStatusCode() <= StatusCode.SMS_OK)) {
                return result;
            } else {
                return getErrorResponse(response, requestID);
            }
        }
        return null;
    }


    private static ResponseResult getErrorResponse(HTTPRequestResponse response, RequestID requestID) {
        ResponseResult result = new ResponseResult(false, response.getStatusCode(), "", requestID);

        if (response == null) {
            return result;
        }

        result.setResponseCode(response.getStatusCode());

        if (response.getException() != null) {
            result.setExceptionMsg(response.getException().toString());
        }
        return result;
    }

    /**
     * get device capability and parse the response
     *
     * @param response
     * @return
     */
    public static ResponseResult parseGetDeviceCapabilityResponse(HTTPRequestResponse response, RequestID requestID) {
        ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

        if (response != null) {
            if (response.getStatusCode() == StatusCode.OK) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "capability: " + response.getData());
                CapabilityResponse capabilityResponse = new Gson().fromJson(response.getData(),
                        CapabilityResponse.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable(HPlusConstants.DEVICE_CAPABILITY_KEY, capabilityResponse);
                result.setResponseData(bundle);

                return result;
            } else {
                return getErrorResponse(response, requestID);
            }
        }

        return null;
    }

    public static ResponseResult parseCleanTime(HTTPRequestResponse response, RequestID requestID) {
        ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

        if (response != null) {
            if (response.getStatusCode() == StatusCode.OK) {
                return result;
            } else {
                return getErrorResponse(response, requestID);
            }
        }
        return null;
    }

    public static ResponseResult parseAllWeatherResponse(HTTPRequestResponse baseWeatherData,
                                                         RequestID requestID) {
        ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);
        HashMap<String, WeatherPageData> weatherPageDataMap = new HashMap<>();
        if (baseWeatherData != null) {
            if (baseWeatherData.getStatusCode() == StatusCode.OK) {
                WeatherData weatherData = new Gson().fromJson(baseWeatherData.getData(), WeatherData
                        .class);
                if (weatherData == null) {
                    result = getErrorResponse(baseWeatherData, requestID);
                } else if (weatherData.getWeather() != null) {
                    for (Weather weather : weatherData.getWeather()) {
                        WeatherPageData weatherPageData = new WeatherPageData();
                        weatherPageData.setWeather(weather);
                        weatherPageDataMap.put(weather.getCityID(), weatherPageData);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(HPlusConstants.WEATHER_DATA_KEY, weatherPageDataMap);
                    result.setResponseData(bundle);
                }
            } else {
                result = getErrorResponse(baseWeatherData, requestID);
            }
        }
        return result;
    }

    public static Hour[] parseHourlyData(HourlyFuture
                                                 hourlyFuture) {
        Hour[] hours = new Hour[hourlyFuture.getHours().length];
        // TODO now data need to be added in the array

        for (int i = 0; i < hourlyFuture.getHours().length; i++) {
            Date date = hourlyFuture.getHours()[i].getDate();
            String te = DateTimeUtil.getDateTimeString(date, DateTimeUtil.WEATHER_CHART_TIME_FORMAT);
            hours[i] = hourlyFuture.getHours()[i];
        }
        return hours;
    }

    public static HourlyFuture parseHourlyFutureResponse(HTTPRequestResponse hourResponse) {
        HourlyFuture hourlyFuture = null;
        if (hourResponse != null && hourResponse.getStatusCode() == StatusCode.OK) {
            hourlyFuture = new Gson().fromJson(hourResponse.getData(), HourlyFuture.class);
        }
        return hourlyFuture;
    }

    public static HourlyHistory parseHourlyHistoryResponse(HTTPRequestResponse hourResponse) {
        HourlyHistory hourlyHistory = null;
        if (hourResponse != null && hourResponse.getStatusCode() == StatusCode.OK) {
            hourlyHistory = new Gson().fromJson(hourResponse.getData(), HourlyHistory.class);
        }
        return hourlyHistory;
    }

    public static ResponseResult parseTurnOnDevie(HTTPRequestResponse response, RequestID requestID) {
        ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

        if (response != null) {
            if (response.getStatusCode() == StatusCode.OK) {
                return result;
            } else {
                return getErrorResponse(response, requestID);
            }
        }
        return null;
    }

    public static ResponseResult parseGetEnrollTypeResponse(HTTPRequestResponse response, RequestID requestID) {
        Log.i("SmartLinkEnroll", "response: " + response);
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            if (!StringUtil.isEmpty(response.getData())) {
                ResponseResult result = new ResponseResult(true, StatusCode.OK, response.getData(), requestID);

                Log.i("SmartLinkEnroll", "response.getDeviceList()： " + response.getData());

                return result;
            }
        }
        Log.i("SmartLinkEnroll", "response == null");

        return getErrorResponse(response, requestID);
    }


    public static ResponseResult parseGetAuthUnreadMsgResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

            if (!StringUtil.isEmpty(response.getData())) {
                GetAuthUnreadMsgResponse getAuthUnreadMsgResponse
                        = new Gson().fromJson(response.getData(), GetAuthUnreadMsgResponse.class);

                UserAllDataContainer.shareInstance().setHasUnReadMessage(getAuthUnreadMsgResponse.getUnreadMsgCount() > 0);
                Bundle bundle = new Bundle();
                bundle.putSerializable(GetAuthUnreadMsgResponse.AUTH_UNREAD_MSG_DATA, getAuthUnreadMsgResponse);
                result.setResponseData(bundle);
                return result;
            }
        }

        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseCheckAuthUserResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

            if (!StringUtil.isEmpty(response.getData())) {
                Log.i(TAG, "response.getDeviceList()： " + response.getData());

                try {
                    JSONArray responseArray = new JSONArray(response.getData());

                    List<CheckAuthUserResponse> tempList = new ArrayList<>();
                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject responseJSON = responseArray.getJSONObject(i);
                        CheckAuthUserResponse checkAuthUserResponse
                                = new Gson().fromJson(responseJSON.toString(), CheckAuthUserResponse.class);
                        tempList.add(checkAuthUserResponse);
                    }

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(CheckAuthUserResponse.AUTH_USER_DATA, (Serializable) tempList);
                    result.setResponseData(bundle);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return result;
            }
        }

        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseGrantAuthToDeviceResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

            if (!StringUtil.isEmpty(response.getData())) {
                GrantAuthToDeviceResponse grantAuthToDeviceResponse
                        = new Gson().fromJson(response.getData(), GrantAuthToDeviceResponse.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(GrantAuthToDeviceResponse.GRANT_AUTH_DATA, grantAuthToDeviceResponse);
                result.setResponseData(bundle);
                return result;
            }
        }

        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseRemoveDeviceAuthResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

            if (!StringUtil.isEmpty(response.getData())) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "response.getDeviceList(): " + response.getData());
                RemoveAuthResponse removeAuthResponse
                        = new Gson().fromJson(response.getData(), RemoveAuthResponse.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(RemoveAuthResponse.REMOVE_AUTH_DATA, removeAuthResponse);
                result.setResponseData(bundle);
                return result;
            }
        }

        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseGetAuthMessagesResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

            if (!StringUtil.isEmpty(response.getData())) {

                try {
                    LogUtil.log(LogUtil.LogLevel.INFO, TAG, "response.getDeviceList(): " + response.getData());
                    JSONObject responseObject = new JSONObject(response.getData());
                    GetAuthMessagesResponse getAuthMessagesResponse
                            = new Gson().fromJson(responseObject.toString(), GetAuthMessagesResponse.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(GetAuthMessagesResponse.AUTH_MESSAGE_DATA, (Serializable) getAuthMessagesResponse);
                    result.setResponseData(bundle);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return result;
            }

        }

        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseGetAuthMessageByIdResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

            if (!StringUtil.isEmpty(response.getData())) {

                try {
                    LogUtil.log(LogUtil.LogLevel.INFO, TAG, "response.getDeviceList(): " + response.getData());
                    JSONObject responseObject = new JSONObject(response.getData());
                    AuthMessageModel getAuthMessageResponse
                            = new Gson().fromJson(responseObject.toString(), AuthMessageModel.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AuthMessageModel.AUTH_MESSAGE_DATA_BY_ID, (Serializable) getAuthMessageResponse);
                    result.setResponseData(bundle);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return result;
            }
        }
        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseHandleAuthMessageResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

            if (!StringUtil.isEmpty(response.getData())) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "response.getDeviceList(): " + response.getData());
                HandleAuthMessageResponse handleAuthMessageResponse
                        = new Gson().fromJson(response.getData(), HandleAuthMessageResponse.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(HandleAuthMessageResponse.AUTH_HANDLE_MESSAGE_DATA, handleAuthMessageResponse);
                result.setResponseData(bundle);
                return result;
            }
        }

        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseGetAuthDevicesResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);
            if (!StringUtil.isEmpty(response.getData())) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "data: " + response.getData());

                try {
                    JSONArray responseArray = new JSONArray(response.getData());
                    List<AuthHomeModel> tempList = new ArrayList<>();
                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject responseJSON = responseArray.getJSONObject(i);
                        AuthHomeModel authHomeModel
                                = new Gson().fromJson(responseJSON.toString(), AuthHomeModel.class);
                        tempList.add(authHomeModel);
                    }

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AuthHomeModel.AUTH_HOME_DATA, (Serializable) tempList);
                    result.setResponseData(bundle);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return result;
            }
        }
        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseHandleUserClientInfoResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.SMS_OK) {
//            if (!StringUtil.isEmpty(response.getDeviceList())) {
            ResponseResult result = new ResponseResult(true, StatusCode.SMS_OK, response.getData(), requestID);
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "data: " + response.getData());
            return result;
//            }
        }
        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseRemoveGroupAuthResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

            if (!StringUtil.isEmpty(response.getData())) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "response.getDeviceList(): " + response.getData());
                RemoveAuthResponse removeAuthResponse
                        = new Gson().fromJson(response.getData(), RemoveAuthResponse.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(RemoveAuthResponse.REMOVE_AUTH_DATA, removeAuthResponse);
                result.setResponseData(bundle);
                return result;
            }
        }

        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseDeviceListByGroupIdResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);
            if (!StringUtil.isEmpty(response.getData())) {
                Log.i(TAG, "response.getDeviceList()： " + response.getData());
                try {
                    JSONArray responseArray = new JSONArray(response.getData());
                    List<AuthGroupDeviceListModel> tempList = new ArrayList<>();
                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject responseJSON = responseArray.getJSONObject(i);
                        AuthGroupDeviceListModel authGroupDeviceListModel
                                = new Gson().fromJson(responseJSON.toString(), AuthGroupDeviceListModel.class);
                        tempList.add(authGroupDeviceListModel);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AuthGroupDeviceListModel.AUTH_GROUP_DEVICE_LIST_DATA, (Serializable) tempList);
                    result.setResponseData(bundle);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return result;
            }
        }
        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseUpdatePasswordResponse(HTTPRequestResponse response, RequestID requestID, String newPassword) {
        if (response != null && response.getStatusCode() == StatusCode.SMS_OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);
            return result;
        } else if (response != null && response.getStatusCode() == StatusCode.BAD_REQUEST) {
            ResponseResult result = new ResponseResult(false, StatusCode.BAD_REQUEST, "", requestID);
            if (response.getData() != null) {
                result.setExceptionMsg(response.getData());
            }
            return result;
        }
        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseFeedbackResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.SMS_OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);
            return result;
        } else if (response != null && response.getStatusCode() == StatusCode.BAD_REQUEST) {
            ResponseResult result = new ResponseResult(false, StatusCode.BAD_REQUEST, "", requestID);
            if (response.getData() != null) {
                result.setExceptionMsg(response.getData());
            }
            return result;
        }
        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseFeedbackImgResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);
            Bundle bundle = new Bundle();
            bundle.putString(HPlusConstants.IMG_UPLOAD_CALLBACK, response.getData());
            result.setResponseData(bundle);

//            Bundle bundle =responseResult.getResponseData();
//            String str = bundle.getString(HPlusConstants.IMG_UPLOAD_CALLBACK);
            return result;
        } else if (response != null && response.getStatusCode() == StatusCode.BAD_REQUEST) {
            ResponseResult result = new ResponseResult(false, StatusCode.BAD_REQUEST, "", requestID);
            if (response.getData() != null) {
                result.setExceptionMsg(response.getData());
            }
            return result;
        }
        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseGetDustAndPm25ByLocationIDResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);
            if (!StringUtil.isEmpty(response.getData())) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "response.getDeviceList()： " + response.getData());
                try {
                    JSONObject responseObject = new JSONObject(response.getData());
                    GetDustAndPm25Response getDustAndPm25Response
                            = new Gson().fromJson(responseObject.toString(), GetDustAndPm25Response.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(GetDustAndPm25Response.GET_DUST_AND_PM25_PARAMETER, (Serializable) getDustAndPm25Response);
                    result.setResponseData(bundle);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return result;
            }
        }
        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseGetVolumeAndTdsByLocationIDResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);
            if (!StringUtil.isEmpty(response.getData())) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "response.getDeviceList()： " + response.getData());
                try {
                    JSONArray responseArray = new JSONArray(response.getData());
                    List<GetVolumeAndTdsResponse> tempList = new ArrayList<>();
                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject responseJSON = responseArray.getJSONObject(i);

                        //解析最外层
                        GetVolumeAndTdsResponse getVolumeAndTdsResponse
                                = new Gson().fromJson(responseJSON.toString(), GetVolumeAndTdsResponse.class);
                        tempList.add(getVolumeAndTdsResponse);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(GetVolumeAndTdsResponse.GET_VOLUME_AND_TDS_PARAMETER, (Serializable) tempList);
                    result.setResponseData(bundle);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return result;
            }
        }
        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseGetTotalDustResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);
            if (!StringUtil.isEmpty(response.getData())) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "response.getDeviceList()： " + response.getData());
                Bundle bundle = new Bundle();
                bundle.putString(HPlusConstants.GET_TOTAL_DUST_PARAMETER, response.getData());
                result.setResponseData(bundle);
                return result;
            }
        }
        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseGetTotalVolumeResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);
            if (!StringUtil.isEmpty(response.getData())) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "response.getDeviceList()： " + response.getData());
                Bundle bundle = new Bundle();
                bundle.putString(HPlusConstants.GET_TOTAL_VOLUME_PARAMETER, response.getData());
                result.setResponseData(bundle);
                return result;
            }
        }
        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseChangePasswordResponse(HTTPRequestResponse response, RequestID requestID, String newPassword) {
        if (response != null && (response.getStatusCode() == StatusCode.SMS_OK
                || response.getStatusCode() == StatusCode.OK
                || response.getStatusCode() == StatusCode.CREATE_OK)) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);
            return result;
        }


        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseGetSmsCodeResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);
            if (!StringUtil.isEmpty(response.getData())) {
                Log.i(TAG, "parseGetSmsCodeResponse response.getDeviceList()： " + response.getData());

                SmsSendResponse smsSendResponse = new Gson().fromJson(response.getData(),
                        SmsSendResponse.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean(HPlusConstants.GET_SMS_RESULT, smsSendResponse.isSend());
                result.setResponseData(bundle);

                result.setResult(smsSendResponse.isSend());
                if (!smsSendResponse.isSend()) {
                    result.setResponseCode(StatusCode.SEND_VERIFY_CODE_ERROR);
                }
                return result;
            }
        }
        return getErrorResponse(response, requestID);
    }


    public static ResponseResult parseValidSmsCodeResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);
            if (!StringUtil.isEmpty(response.getData())) {
                Log.i(TAG, "parseValidSmsCodeResponse response.getDeviceList()： " + response.getData());

                SmsValidResponse smsValidResponse = new Gson().fromJson(response.getData(),
                        SmsValidResponse.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean(HPlusConstants.SMS_CODE_VALID_RESULT, smsValidResponse.isValid());

                result.setResponseData(bundle);

                result.setResult(smsValidResponse.isValid());
                if (!smsValidResponse.isValid()) {
                    result.setResponseCode(StatusCode.VERIFY_CODE_ERROR);
                }

                return result;
            }
        }
        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseWaterControlDeviceResponse(HTTPRequestResponse response, RequestID requestID) {
        ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

        if (response != null) {
            if (response.getStatusCode() == StatusCode.CREATE_OK) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "data: " + response.getData());
                RecordCreatedResponse recordCreatedResponse = new Gson().fromJson(response.getData(),
                        RecordCreatedResponse.class);
                Bundle bundle = new Bundle();
                bundle.putInt(HPlusConstants.COMM_TASK_BUNDLE_KEY, recordCreatedResponse.getId());
                result.setResponseData(bundle);

                return result;
            } else {
                return getErrorResponse(response, requestID);
            }
        }

        return null;
    }

    public static ResponseResult parseAllLocationsAndDevicesRunstatus(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);
            if (!StringUtil.isEmpty(response.getData())) {

                try {
                    LogUtil.log(LogUtil.LogLevel.INFO, TAG, "locationDetail: " + response.getData());

                    UserInfoSharePreference.saveDeviceRunstatusCachesData(response.getData());

                    LocationAndDeviceParseManager.getInstance().parseJsonDataToRunstatusObject(response.getData());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return result;
            }
        }
        return getErrorResponse(response, requestID);
    }


    public static ResponseResult parseAllDeviceTypeConfig(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);
            if (!StringUtil.isEmpty(response.getData())) {

                try {
                    LogUtil.log(LogUtil.LogLevel.INFO, TAG, "ConfigData： " + response.getData());
                    UserInfoSharePreference.saveDeviceConfigCachesData(response.getData());
                    LocationAndDeviceParseManager.getInstance().parseJsonToDeviceConfig(response.getData());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return result;
            }
        }
        return getErrorResponse(response, requestID);
    }


    public static ResponseResult parseGetGroupByGroupIdResponse(HTTPRequestResponse response, RequestID requestID) {

        /**
         * 当这个组被删除后，调用该接口会返回401，所以如果返回的401有认为是成功
         */
        if (response != null && (response.getStatusCode() == StatusCode.OK || response.getStatusCode() == StatusCode.UNAUTHORIZED)) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

            if (!StringUtil.isEmpty(response.getData()) && response.getStatusCode() == StatusCode.OK) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "response.getDeviceList()： " + response.getData());
                SingleGroupItem groupData = new Gson().fromJson(response.getData(), SingleGroupItem.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(GroupData.GROUP_DATA, groupData);
                result.setResponseData(bundle);
            }
            return result;

        }

        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseGetMessagesPerPageResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

            if (!StringUtil.isEmpty(response.getData())) {

                try {
                    LogUtil.log(LogUtil.LogLevel.INFO, TAG, "response.getDeviceList(): " + response.getData());

                    UserInfoSharePreference.saveMessageCachesData(response.getData());
                    List<MessageModel> tempList = LocationAndDeviceParseManager.getInstance().parseJsonDataToMessageSObject(response.getData());

//                    JSONArray responseArray = new JSONArray(response.getDeviceList());
//                    List<MessageModel> tempList = new ArrayList<>();
//                    for (int i = 0; i < responseArray.length(); i++) {
//                        JSONObject responseJSON = responseArray.getJSONObject(i);
//
//                        //解析最外层
//                        MessageModel messageModel
//                                = new Gson().fromJson(responseJSON.toString(), MessageModel.class);
//                        tempList.add(messageModel);
//                    }
//
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(MessageModel.GET_MESSAGES_PER_PAGE_PARAMETER, (Serializable) tempList);
                    result.setResponseData(bundle);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return result;
            }

        }

        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseGetMessageByIdResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

            if (!StringUtil.isEmpty(response.getData())) {

                try {
                    LogUtil.log(LogUtil.LogLevel.INFO, TAG, "response.getDeviceList(): " + response.getData());
                    JSONObject responseObject = new JSONObject(response.getData());
                    MessageDetailResponse messageDetailResponse
                            = new Gson().fromJson(responseObject.toString(), MessageDetailResponse.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(MessageDetailResponse.GET_MESSAGES_BY_ID_PARAMETER, (Serializable) messageDetailResponse);
                    result.setResponseData(bundle);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return result;
            }
        }
        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseGetUnreadMsgResponse(HTTPRequestResponse response, RequestID requestID) {
        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", requestID);

            if (!StringUtil.isEmpty(response.getData())) {
                GetUnReadResponse getUnreadMsgResponse
                        = new Gson().fromJson(response.getData(), GetUnReadResponse.class);

                UserAllDataContainer.shareInstance().setHasUnReadMessage(getUnreadMsgResponse.getmUnreadMessages() > 0);
                Bundle bundle = new Bundle();
                bundle.putSerializable(GetUnReadResponse.UNREAD_MSG_DATA, getUnreadMsgResponse);
                result.setResponseData(bundle);
                return result;
            }
        }

        return getErrorResponse(response, requestID);
    }

    public static ResponseResult parseUserValidateResponse(HTTPRequestResponse response, RequestID requestID) {

        if (response != null && response.getStatusCode() == StatusCode.OK) {
            ResponseResult result = new ResponseResult(true, StatusCode.OK, "", response.getRequestID());
            if (!StringUtil.isEmpty(response.getData())) {
                UserValidateResponse userValidateResponse = new Gson().fromJson(response.getData(), UserValidateResponse.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(UserValidateResponse.USER_VALIDATE_DATA, userValidateResponse);
                result.setResponseData(bundle);
                return result;
            }
        }
        return getErrorResponse(response, requestID);
    }
}
