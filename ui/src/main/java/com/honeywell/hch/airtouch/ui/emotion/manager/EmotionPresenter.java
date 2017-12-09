package com.honeywell.hch.airtouch.ui.emotion.manager;

import android.content.Context;

import com.google.gson.Gson;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.model.emotion.EmotionDataModel;
import com.honeywell.hch.airtouch.plateform.http.model.emotion.GetDustAndPm25Response;
import com.honeywell.hch.airtouch.plateform.http.model.emotion.GetVolumeAndTdsResponse;
import com.honeywell.hch.airtouch.plateform.http.model.emotion.IndoorPm25Model;
import com.honeywell.hch.airtouch.plateform.http.model.emotion.OutDoorPm25Model;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.ui.emotion.interfaceFile.IEmotionView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Vincent on 27/12/16.
 */

public class EmotionPresenter {
    private final String TAG = "EmotionPresenter";
    protected IEmotionView mIEmotionView;
    private Context mContext;
    protected EmotionChartDataManager mEmotionChartDataManager;
    public static final int AIR_TOUCH_TYPE = 0;
    public static final int AQUA_TOUCH_TYPE = 1;
    private EmotionUiManager mEmotionUiManager;
    private int mUserLocationId;

    public EmotionPresenter(Context context, IEmotionView emotionView, int locationId) {
        mContext = context;
        mIEmotionView = emotionView;
        mUserLocationId = locationId;
        mEmotionChartDataManager = new EmotionChartDataManager(mContext, mIEmotionView);
        initEmotionUiManager();
    }

    public void initEmotionUiManager() {
        mEmotionUiManager = new EmotionUiManager(mUserLocationId);
        setErrorCallback(mErrorCallBack);
        setSuccessCallback(mSuccessCallback);
    }

    private EmotionManager.SuccessCallback mSuccessCallback = new EmotionManager.SuccessCallback() {
        @Override
        public void onSuccess(ResponseResult responseResult) {
            dealSuccessCallBack(responseResult);
        }
    };

    private EmotionManager.ErrorCallback mErrorCallBack = new EmotionManager.ErrorCallback() {
        @Override
        public void onError(ResponseResult responseResult, int id) {
            dealErrorCallBack(responseResult, id);
        }
    };

    private void setSuccessCallback(EmotionManager.SuccessCallback successCallback) {
        mEmotionUiManager.setSuccessCallback(successCallback);
    }

    private void setErrorCallback(EmotionManager.ErrorCallback errorCallback) {
        mEmotionUiManager.setErrorCallback(errorCallback);
    }

    private void dealSuccessCallBack(ResponseResult responseResult) {
        switch (responseResult.getRequestId()) {
            case GET_DUST_AND_PM25:
                GetDustAndPm25Response getDustAndPm25Response = (GetDustAndPm25Response) responseResult.getResponseData()
                        .getSerializable(GetDustAndPm25Response.GET_DUST_AND_PM25_PARAMETER);
                List dateArrayAir = getDateStringsAir(getDustAndPm25Response);
                List<EmotionDataModel> emotionDataModelListAir = setAirEmotionData(dateArrayAir, getDustAndPm25Response);
                UserInfoSharePreference.saveEmotionAirCachesData(String.valueOf(mUserLocationId), new Gson().toJson(emotionDataModelListAir));
                mEmotionChartDataManager.setEmotionDataModelList(emotionDataModelListAir);
                mIEmotionView.dealSuccessCallBack();
                break;

            case GET_VOLUME_AND_TDS:
                List<GetVolumeAndTdsResponse> getVolumeAndTdsResponseList = (ArrayList<GetVolumeAndTdsResponse>) responseResult.getResponseData()
                        .getSerializable(GetVolumeAndTdsResponse.GET_VOLUME_AND_TDS_PARAMETER);
                List dateArrayWater = getDateStringsWater(getVolumeAndTdsResponseList);
                List<EmotionDataModel> emotionDataModelListWater = setWaterEmotionData(dateArrayWater, getVolumeAndTdsResponseList);
                UserInfoSharePreference.saveEmotionWaterCachesData(String.valueOf(mUserLocationId), new Gson().toJson(emotionDataModelListWater));
                mEmotionChartDataManager.setEmotionDataModelList(emotionDataModelListWater);
                mIEmotionView.dealSuccessCallBack();
                break;

            case GET_TOTAL_DUST:
                float totalDust = Float.valueOf(responseResult.getResponseData()
                        .getString(HPlusConstants.GET_TOTAL_DUST_PARAMETER));
                UserInfoSharePreference.saveEmotionAirCachesTotalData(String.valueOf(mUserLocationId), totalDust);
                mEmotionChartDataManager.setTotalAirDataBarChart(totalDust);
                break;

            case GET_TOTAL_VOLUME:
                float totalVolume = Float.valueOf(responseResult.getResponseData()
                        .getString(HPlusConstants.GET_TOTAL_VOLUME_PARAMETER));
                UserInfoSharePreference.saveEmotionWaterCachesTotalData(String.valueOf(mUserLocationId), totalVolume);
                mEmotionChartDataManager.setTotalWaterDataBarChart(totalVolume);
                break;
        }
    }

    private void dealErrorCallBack(ResponseResult responseResult, int id) {
        switch (responseResult.getRequestId()) {
            case GET_DUST_AND_PM25:
            case GET_VOLUME_AND_TDS:
            case GET_TOTAL_DUST:
            case GET_TOTAL_VOLUME:
                mIEmotionView.dealErrorCallBack();
                break;

        }
    }

    //将cache emotion data 转换成对象
    public List<EmotionDataModel> parseJsonDataToEmotionDataModel(String responseData) throws JSONException {
        JSONArray responseArray = new JSONArray(responseData);
        List<EmotionDataModel> tempList = new ArrayList<>();
        for (int i = 0; i < responseArray.length(); i++) {
            JSONObject responseJSON = responseArray.getJSONObject(i);

            //解析最外层
            EmotionDataModel emotionDataModel
                    = new Gson().fromJson(responseJSON.toString(), EmotionDataModel.class);
            tempList.add(emotionDataModel);
        }
        return tempList;
    }

    public void getDustAndPm25ByLocationID(String fromDate, String toDate) {
        mEmotionUiManager.getDustAndPm25ByLocationID(fromDate, toDate);
    }

    public void getVolumeAndTdsByLocationID(String fromDate, String toDate) {
        mEmotionUiManager.getVolumeAndTdsByLocationID(fromDate, toDate);
    }

    public void getTotalDustByLocationID() {
        mEmotionUiManager.getTotalDustByLocationID();
    }

    public void getTotalVolumeByLocationID() {
        mEmotionUiManager.getTotalVolumeByLocationID();
    }

    public void refreshLineChartData(int index) {
        mEmotionChartDataManager.refreshLineChartData(index);
    }

    public void refreshBarChartData(int index) {
        mEmotionChartDataManager.refreshBarChartData(index);
    }

    //检查服务器传来的时间值，拼成连续的时间数组,空气
    private List<String> getDateStringsAir(GetDustAndPm25Response getDustAndPm25Response) {
        List<String> resultArray = new ArrayList<>();
        String fromDateString;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.add(Calendar.DATE, -30);
        //起始点
        String fromDateStringTemple = format.format(fromCalendar.getTime());
        Date fromDateFromServe = null;
        Calendar calFromServe = Calendar.getInstance();
        try {
            fromDateFromServe = format.parse(getDustAndPm25Response.getmInDoorPm25ModelList().get(0).getmDate());
            calFromServe.setTime(fromDateFromServe);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (fromDateFromServe == null || fromCalendar.before(calFromServe)) {
            fromDateString = fromDateStringTemple;
        } else {
            fromDateString = getDustAndPm25Response.getmInDoorPm25ModelList().get(0).getmDate();
        }
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.add(Calendar.DATE, -1);
        //yesterday
        String toDateString = format.format(currentCalendar.getTime());
        resultArray.add(fromDateString);
        Date fromDate = new Date();
        try {
            fromDate = format.parse(fromDateString);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            currentCalendar.setTime(fromDate);
            currentCalendar.add(Calendar.DATE, i);//把日期往后增加一天.整数往后推,负数往前移动
            Date date = currentCalendar.getTime();   //这个时间就是日期往后推一天的结果

            String dateString = format.format(date);
            resultArray.add(dateString);

            if (dateString.equals(toDateString)) {
                break;
            }
        }
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "toDateString air: " + toDateString);
        return resultArray;
    }

    //检查服务器传来的时间值，拼成连续的时间数组,水
    public List<String> getDateStringsWater(List<GetVolumeAndTdsResponse> getVolumeAndTdsResponseList) {
        List<String> resultArray = new ArrayList<>();
        String fromDateString;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.add(Calendar.DATE, -30);
        //起始点
        String fromDateStringTemple = format.format(fromCalendar.getTime());
        Date fromDateFromServe = null;
        Calendar calFromServe = Calendar.getInstance();
        try {
            fromDateFromServe = format.parse(getVolumeAndTdsResponseList.get(0).getmDate());
            calFromServe.setTime(fromDateFromServe);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (fromDateFromServe == null || fromCalendar.before(calFromServe)) {
            fromDateString = fromDateStringTemple;
        } else {
            fromDateString = getVolumeAndTdsResponseList.get(0).getmDate();
        }

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.add(Calendar.DATE, -1);
        //yesterday
        String toDateString = format.format(currentCalendar.getTime());
        resultArray.add(fromDateString);
        Date fromDate = new Date();
        try {
            fromDate = format.parse(fromDateString);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            currentCalendar.setTime(fromDate);
            currentCalendar.add(Calendar.DATE, i);//把日期往后增加一天.整数往后推,负数往前移动
            Date date = currentCalendar.getTime();   //这个时间就是日期往后推一天的结果

            String dateString = format.format(date);
            resultArray.add(dateString);

            if (dateString.equals(toDateString)) {
                break;
            }
        }
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "toDateString water: " + toDateString);
//        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "dateString water: " + resultArray.toString());
        return resultArray;
    }

    //初始化一个 emotion折线和柱状图数据数组
    private List setAirEmotionData(List<String> dateArray, GetDustAndPm25Response getDustAndPm25Response) {
        List<EmotionDataModel> emotionDataModelList = new ArrayList<>();
        for (String date : dateArray) {
            EmotionDataModel emotionDataModel = new EmotionDataModel();
            List<IndoorPm25Model> inDoorPm25ModelList = getDustAndPm25Response.getmInDoorPm25ModelList();
            List<OutDoorPm25Model> outDoorPm25ModelList = getDustAndPm25Response.getmOutDoorPm25ModelList();
            for (int i = 0; i < inDoorPm25ModelList.size(); i++) {
                if (date.equals(inDoorPm25ModelList.get(i).getmDate())) {
                    emotionDataModel.setIndoorData(inDoorPm25ModelList.get(i).getmAvgInPM25());
                    emotionDataModel.setBarChartData(inDoorPm25ModelList.get(i).getmCleanDust());
                }
            }

            for (int i = 0; i < outDoorPm25ModelList.size(); i++) {
                if (date.equals(outDoorPm25ModelList.get(i).getmDate())) {
                    emotionDataModel.setOutData(outDoorPm25ModelList.get(i).getmAvgOutPM25());
                }
            }
            emotionDataModel.setDate(date.replaceAll("-","/"));
            emotionDataModelList.add(emotionDataModel);

        }
        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "emotionDataAirList: " + emotionDataModelList.toString());
        return emotionDataModelList;
    }

    //初始化一个 emotion折线和柱状图数据数组
    private List setWaterEmotionData(List<String> dateArray, List<GetVolumeAndTdsResponse> getVolumeAndTdsResponseList) {
        List<EmotionDataModel> emotionDataModelList = new ArrayList<>();
        for (String date : dateArray) {
            EmotionDataModel emotionDataModel = new EmotionDataModel();
            for (GetVolumeAndTdsResponse getVolumeAndTdsResponse : getVolumeAndTdsResponseList) {
                if (date.equals(getVolumeAndTdsResponse.getmDate())) {
                    emotionDataModel.setIndoorData(getVolumeAndTdsResponse.getmAvgOutflowTDS());
                    emotionDataModel.setBarChartData(getVolumeAndTdsResponse.getmOutflowVolume());
                    emotionDataModel.setOutData(getVolumeAndTdsResponse.getmAvgInflowTDS());
                }
            }
            emotionDataModel.setDate(date.replaceAll("-","/"));
            emotionDataModelList.add(emotionDataModel);

        }
        return emotionDataModelList;
    }

    public void getAirDataFromServer() {
        List<EmotionDataModel> emotionDataModelListAir = new ArrayList<>();
        try {
            emotionDataModelListAir = parseJsonDataToEmotionDataModel
                    (UserInfoSharePreference.getEmotionAirCachesData(String.valueOf(mUserLocationId)));
        } catch (JSONException ex) {
            ex.printStackTrace();
            mIEmotionView.getAirDataFromServer(true);
            return;
        }
        mEmotionChartDataManager.setTotalAirDataBarChart(UserInfoSharePreference.getEmotionAirCachesTotalData(String.valueOf(mUserLocationId)));
        mEmotionChartDataManager.setEmotionDataModelList(emotionDataModelListAir);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.add(Calendar.DATE, -1);
        //yesterday
        String toDateString = format.format(currentCalendar.getTime());
        mIEmotionView.showShareLayout();
        if (emotionDataModelListAir.size() > 0 && !toDateString.equals(emotionDataModelListAir.get(emotionDataModelListAir.size() - 1).getDate())) {
            mIEmotionView.getAirDataFromServer(false);
        }
    }

    public void getWaterDataFromServer() {
        List<EmotionDataModel> emotionDataModelListWater = new ArrayList<>();
        try {
            emotionDataModelListWater = parseJsonDataToEmotionDataModel
                    (UserInfoSharePreference.getEmotionWaterCachesData(String.valueOf(mUserLocationId)));
        } catch (JSONException ex) {
            ex.printStackTrace();
            mIEmotionView.getWaterDataFromServer(true);
            return;
        }
        mEmotionChartDataManager.setEmotionDataModelList(emotionDataModelListWater);
        mEmotionChartDataManager.setTotalWaterDataBarChart(UserInfoSharePreference.getEmotionWaterCachesTotalData(String.valueOf(mUserLocationId)));
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.add(Calendar.DATE, -1);
        //yesterday
        String toDateString = format.format(currentCalendar.getTime());
        mIEmotionView.showShareLayout();
        if (emotionDataModelListWater.size() > 0 && !toDateString.equals(emotionDataModelListWater.get(emotionDataModelListWater.size() - 1).getDate())) {
            mIEmotionView.getWaterDataFromServer(false);
        }
    }
}
