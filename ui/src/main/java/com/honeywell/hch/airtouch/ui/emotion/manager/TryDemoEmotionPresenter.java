package com.honeywell.hch.airtouch.ui.emotion.manager;

import android.content.Context;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.http.model.emotion.EmotionDataModel;
import com.honeywell.hch.airtouch.ui.emotion.interfaceFile.IEmotionView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Vincent on 27/12/16.
 */

public class TryDemoEmotionPresenter extends EmotionPresenter {
    private final String TAG = "TryDemoEmotionPresenter";
    private EmotionUiManager mEmotionUiManager;
    private final float AIRBARTOTAL = 8310f;
    private final float WATERBARTOTAL = 12960f;
    
    public TryDemoEmotionPresenter(Context context, IEmotionView emotionView, int locationId) {
        super(context, emotionView, locationId);
    }

    public void initEmotionUiManager() {
        mEmotionUiManager = new EmotionUiManager();
    }

    public void getDustAndPm25ByLocationID(String fromDate, String toDate) {
        mEmotionUiManager.getDustAndPm25ByLocationID(fromDate, toDate);
    }

    public void getVolumeAndTdsByLocationID(String fromDate, String toDate) {
        mEmotionUiManager.getVolumeAndTdsByLocationID(fromDate, toDate);
    }

    public void getTotalDustByLocationID() {
        mEmotionChartDataManager.setTotalAirDataBarChart(AIRBARTOTAL);
    }

    public void getTotalVolumeByLocationID() {
        mEmotionChartDataManager.setTotalWaterDataBarChart(WATERBARTOTAL);
    }

    public void refreshLineChartData(int index) {
        mEmotionChartDataManager.refreshLineChartData(index);
    }

    public void refreshBarChartData(int index) {
        mEmotionChartDataManager.refreshBarChartData(index);
    }

    private List<String> initDateArray() {
        List<String> resultArray = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.add(Calendar.DATE, -60);
        String fromDateString = format.format(fromCalendar.getTime());

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
        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "resultArray: " + resultArray);
        return resultArray;
    }

    public void getAirDataFromServer() {
        List<String> dateArray = initDateArray();
        List<EmotionDataModel> emotionDataModelList = mockAirData();
        setEmotionData(dateArray, emotionDataModelList);
        mIEmotionView.showShareLayout();
        mEmotionChartDataManager.setEmotionDataModelList(emotionDataModelList);
    }

    public void getWaterDataFromServer() {
        List<String> dateArray = initDateArray();
        List<EmotionDataModel> emotionDataModelList = mockWaterData();
        setEmotionData(dateArray, emotionDataModelList);
        mIEmotionView.showShareLayout();
        mEmotionChartDataManager.setEmotionDataModelList(emotionDataModelList);
    }

    //初始化一个 emotion折线和柱状图数据数组
    private List setEmotionData(List<String> dateArray, List<EmotionDataModel> emotionDataModelList) {
        for (int i = 0; i < emotionDataModelList.size(); i++) {
            emotionDataModelList.get(i).setDate(dateArray.get(i));
            if(AppConfig.shareInstance().isIndiaAccount()){
                emotionDataModelList.get(i).setOutData(0);
            }
        }
        return emotionDataModelList;
    }

    private List<EmotionDataModel> mockAirData() {
        List<EmotionDataModel> emotionDataModelList = new ArrayList<>();

        emotionDataModelList.add(new EmotionDataModel(285, 75, 210));
        emotionDataModelList.add(new EmotionDataModel(300, 60, 240));
        emotionDataModelList.add(new EmotionDataModel(266, 100, 166));
        emotionDataModelList.add(new EmotionDataModel(240, 66, 174));
        emotionDataModelList.add(new EmotionDataModel(231, 40, 191));
        emotionDataModelList.add(new EmotionDataModel(180, 75, 105));
        emotionDataModelList.add(new EmotionDataModel(125, 60, 65));
        emotionDataModelList.add(new EmotionDataModel(150, 50, 100));
        emotionDataModelList.add(new EmotionDataModel(180, 66, 114));
        emotionDataModelList.add(new EmotionDataModel(285, 75, 210));

        emotionDataModelList.add(new EmotionDataModel(300, 70, 230));
        emotionDataModelList.add(new EmotionDataModel(266, 30, 236));
        emotionDataModelList.add(new EmotionDataModel(240, 40, 200));
        emotionDataModelList.add(new EmotionDataModel(231, 46, 185));
        emotionDataModelList.add(new EmotionDataModel(250, 50, 200));
        emotionDataModelList.add(new EmotionDataModel(260, 42, 218));
        emotionDataModelList.add(new EmotionDataModel(270, 28, 242));
        emotionDataModelList.add(new EmotionDataModel(220, 32, 188));
        emotionDataModelList.add(new EmotionDataModel(285, 35, 250));
        emotionDataModelList.add(new EmotionDataModel(350, 20, 330));

        emotionDataModelList.add(new EmotionDataModel(330, 49, 281));
        emotionDataModelList.add(new EmotionDataModel(320, 44, 276));
        emotionDataModelList.add(new EmotionDataModel(290, 66, 224));
        emotionDataModelList.add(new EmotionDataModel(190, 75, 115));
        emotionDataModelList.add(new EmotionDataModel(110, 86, 24));
        emotionDataModelList.add(new EmotionDataModel(155, 50, 105));
        emotionDataModelList.add(new EmotionDataModel(177, 75, 102));
        emotionDataModelList.add(new EmotionDataModel(196, 75, 121));
        emotionDataModelList.add(new EmotionDataModel(220, 80, 140));
        emotionDataModelList.add(new EmotionDataModel(200, 90, 110));

        emotionDataModelList.add(new EmotionDataModel(171, 50, 121));
        emotionDataModelList.add(new EmotionDataModel(160, 80, 80));
        emotionDataModelList.add(new EmotionDataModel(176, 75, 101));
        emotionDataModelList.add(new EmotionDataModel(180, 60, 120));
        emotionDataModelList.add(new EmotionDataModel(160, 70, 90));
        emotionDataModelList.add(new EmotionDataModel(144, 75, 69));
        emotionDataModelList.add(new EmotionDataModel(123, 75, 48));
        emotionDataModelList.add(new EmotionDataModel(133, 80, 53));
        emotionDataModelList.add(new EmotionDataModel(144, 67, 77));
        emotionDataModelList.add(new EmotionDataModel(156, 50, 106));

        emotionDataModelList.add(new EmotionDataModel(178, 90, 88));
        emotionDataModelList.add(new EmotionDataModel(190, 75, 115));
        emotionDataModelList.add(new EmotionDataModel(110, 60, 50));
        emotionDataModelList.add(new EmotionDataModel(140, 50, 90));
        emotionDataModelList.add(new EmotionDataModel(130, 75, 55));
        emotionDataModelList.add(new EmotionDataModel(133, 75, 58));
        emotionDataModelList.add(new EmotionDataModel(150, 80, 70));
        emotionDataModelList.add(new EmotionDataModel(140, 100, 40));
        emotionDataModelList.add(new EmotionDataModel(180, 50, 130));
        emotionDataModelList.add(new EmotionDataModel(176, 90, 86));

        emotionDataModelList.add(new EmotionDataModel(180, 75, 105));
        emotionDataModelList.add(new EmotionDataModel(125, 60, 65));
        emotionDataModelList.add(new EmotionDataModel(150, 50, 100));
        emotionDataModelList.add(new EmotionDataModel(180, 75, 105));
        emotionDataModelList.add(new EmotionDataModel(285, 75, 210));
        emotionDataModelList.add(new EmotionDataModel(300, 80, 220));
        emotionDataModelList.add(new EmotionDataModel(266, 120, 146));
        emotionDataModelList.add(new EmotionDataModel(240, 130, 110));
        emotionDataModelList.add(new EmotionDataModel(231, 90, 141));
        emotionDataModelList.add(new EmotionDataModel(180, 75, 105));
        return emotionDataModelList;
    }

    private List<EmotionDataModel> mockWaterData() {
        List<EmotionDataModel> emotionDataModelList = new ArrayList<>();

        emotionDataModelList.add(new EmotionDataModel(285, 48, 237));
        emotionDataModelList.add(new EmotionDataModel(300, 50, 250));
        emotionDataModelList.add(new EmotionDataModel(266, 32, 234));
        emotionDataModelList.add(new EmotionDataModel(240, 44, 196));
        emotionDataModelList.add(new EmotionDataModel(231, 40, 191));
        emotionDataModelList.add(new EmotionDataModel(256, 39, 217));
        emotionDataModelList.add(new EmotionDataModel(248, 30, 218));
        emotionDataModelList.add(new EmotionDataModel(232, 36, 196));
        emotionDataModelList.add(new EmotionDataModel(258, 48, 210));
        emotionDataModelList.add(new EmotionDataModel(285, 40, 245));

        emotionDataModelList.add(new EmotionDataModel(300, 33, 267));
        emotionDataModelList.add(new EmotionDataModel(266, 30, 236));
        emotionDataModelList.add(new EmotionDataModel(240, 40, 200));
        emotionDataModelList.add(new EmotionDataModel(231, 46, 185));
        emotionDataModelList.add(new EmotionDataModel(250, 50, 200));
        emotionDataModelList.add(new EmotionDataModel(260, 42, 218));
        emotionDataModelList.add(new EmotionDataModel(270, 28, 242));
        emotionDataModelList.add(new EmotionDataModel(220, 32, 188));
        emotionDataModelList.add(new EmotionDataModel(290, 35, 255));
        emotionDataModelList.add(new EmotionDataModel(305, 20, 285));

        emotionDataModelList.add(new EmotionDataModel(270, 49, 221));
        emotionDataModelList.add(new EmotionDataModel(280, 44, 236));
        emotionDataModelList.add(new EmotionDataModel(296, 36, 260));
        emotionDataModelList.add(new EmotionDataModel(288, 32, 256));
        emotionDataModelList.add(new EmotionDataModel(265, 29, 236));
        emotionDataModelList.add(new EmotionDataModel(252, 40, 212));
        emotionDataModelList.add(new EmotionDataModel(248, 48, 200));
        emotionDataModelList.add(new EmotionDataModel(259, 50, 209));
        emotionDataModelList.add(new EmotionDataModel(266, 42, 224));
        emotionDataModelList.add(new EmotionDataModel(241, 33, 208));

        emotionDataModelList.add(new EmotionDataModel(250, 36, 214));
        emotionDataModelList.add(new EmotionDataModel(253, 38, 215));
        emotionDataModelList.add(new EmotionDataModel(255, 50, 205));
        emotionDataModelList.add(new EmotionDataModel(238, 48, 190));
        emotionDataModelList.add(new EmotionDataModel(260, 46, 214));
        emotionDataModelList.add(new EmotionDataModel(277, 45, 232));
        emotionDataModelList.add(new EmotionDataModel(273, 38, 235));
        emotionDataModelList.add(new EmotionDataModel(239, 32, 207));
        emotionDataModelList.add(new EmotionDataModel(258, 26, 232));
        emotionDataModelList.add(new EmotionDataModel(250, 37, 213));

        emotionDataModelList.add(new EmotionDataModel(244, 46, 198));
        emotionDataModelList.add(new EmotionDataModel(220, 35, 185));
        emotionDataModelList.add(new EmotionDataModel(257, 40, 217));
        emotionDataModelList.add(new EmotionDataModel(243, 48, 195));
        emotionDataModelList.add(new EmotionDataModel(252, 36, 216));
        emotionDataModelList.add(new EmotionDataModel(243, 44, 199));
        emotionDataModelList.add(new EmotionDataModel(269, 47, 222));
        emotionDataModelList.add(new EmotionDataModel(244, 34, 210));
        emotionDataModelList.add(new EmotionDataModel(239, 50, 189));
        emotionDataModelList.add(new EmotionDataModel(226, 45, 181));

        emotionDataModelList.add(new EmotionDataModel(235, 45, 190));
        emotionDataModelList.add(new EmotionDataModel(256, 43, 213));
        emotionDataModelList.add(new EmotionDataModel(247, 49, 198));
        emotionDataModelList.add(new EmotionDataModel(238, 46, 192));
        emotionDataModelList.add(new EmotionDataModel(272, 38, 234));
        emotionDataModelList.add(new EmotionDataModel(260, 30, 230));
        emotionDataModelList.add(new EmotionDataModel(256, 25, 231));
        emotionDataModelList.add(new EmotionDataModel(240, 32, 208));
        emotionDataModelList.add(new EmotionDataModel(231, 44, 187));
        emotionDataModelList.add(new EmotionDataModel(220, 48, 172));
        return emotionDataModelList;
    }

}
