package com.honeywell.hch.airtouch.ui.emotion.manager;

import android.content.Context;
import android.view.View;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.http.model.emotion.EmotionDataModel;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.emotion.interfaceFile.IEmotionView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Vincent on 21/12/16.
 */

public class EmotionChartDataManager {

    private static final int MG_UNIT_SCALE = 1;
    private static final int G_UNIT_SCALE = 1000;

    private Context mContext;
    private final int defaultIndex = -30; //默认前30天数据
    private int lineChartIndexTemp = -30;
    private int barChatIndexTemp = -30;
    private final int constantIndex = 30;
    private IEmotionView mIEmotionView;
    private List<EmotionDataModel> mEmotionDataModelList;
    private final int EMPTY = 0;

    public EmotionChartDataManager(Context context, IEmotionView emotionView) {
        mContext = context;
        mIEmotionView = emotionView;
    }

    public void setEmotionDataModelList(List<EmotionDataModel> emotionDataModelList) {
        this.mEmotionDataModelList = emotionDataModelList;
        initBarChart();
        initLineChart();
    }

    private void initLineChart() {
        refreshLineChartData(EMPTY);
    }

    public void initBarChart() {
        refreshBarChartData(EMPTY);
    }

    public void refreshBarChartData(int index) {

        if (mEmotionDataModelList == null || mEmotionDataModelList.size() == EMPTY) {
            return;
        }
        ArrayList<Float> respectTarget = new ArrayList<>();
        ArrayList<String> allDateList = new ArrayList<>();
        barChatIndexTemp += index;
        if (barChatIndexTemp > defaultIndex) {
            barChatIndexTemp = defaultIndex;
        } else if (barChatIndexTemp < (-mEmotionDataModelList.size())) {
            barChatIndexTemp = -mEmotionDataModelList.size();
        }
        int indexFor = mEmotionDataModelList.size() + barChatIndexTemp;
        for (int i = indexFor; i < indexFor + constantIndex; i++) {
            respectTarget.add(mEmotionDataModelList.get(i).getBarChartData());
            allDateList.add(mEmotionDataModelList.get(i).getDate().substring(5));
        }
        mIEmotionView.setRespectTargetNumBarChart(respectTarget);
        mIEmotionView.setRespectNameBarChart(allDateList);
        mIEmotionView.setTotalBarNumBarChart(constantIndex);
        mIEmotionView.setDataIndexBarChart(barChatIndexTemp + respectTarget.size());

        int newMax = 0;
        float maxOrginalData = EMPTY;
        if (respectTarget.size() > EMPTY) {
            maxOrginalData = Collections.max(respectTarget);
        }
        //如果 数值 > 1000 单位改为克，所有的数据都除以1000

        if (maxOrginalData >= 1000) {
            maxOrginalData = maxOrginalData / 1000;
            mIEmotionView.setMaxScaleBarChart(G_UNIT_SCALE);
        } else {
            mIEmotionView.setMaxScaleBarChart(MG_UNIT_SCALE);
        }

        newMax = (int) Math.ceil(maxOrginalData);
        if (newMax % 2 != EMPTY) {
            newMax = newMax + 1;
        }
        mIEmotionView.setTotalDataBarChart(newMax);
    }

    public void setTotalAirDataBarChart(float totalData) {
        mIEmotionView.setTotalData(getValue(totalData), getAirUnit(totalData));
    }

    public void setTotalWaterDataBarChart(float totalData) {
        mIEmotionView.setTotalData(getValue(totalData), getWaterUnit(totalData));
    }


    public void refreshLineChartData(int index) {
        if (mEmotionDataModelList == null || mEmotionDataModelList.size() == EMPTY) {
            return;
        }
        List<Float> inDoorArray = new ArrayList<>();
        List<Float> outDoorArray = new ArrayList<>();
        ArrayList<String> allDateList = new ArrayList<>();

        lineChartIndexTemp += index;
        if (lineChartIndexTemp > defaultIndex) {
            lineChartIndexTemp = defaultIndex;
        } else if (lineChartIndexTemp < (-mEmotionDataModelList.size())) {
            lineChartIndexTemp = -mEmotionDataModelList.size();
        }
        int indexFor = mEmotionDataModelList.size() + lineChartIndexTemp;
        boolean isIndoorWorseThanOutDoorFlag = false;
        for (int i = indexFor; i < indexFor + constantIndex; i++) {
            float inDoorData = mEmotionDataModelList.get(i).getIndoorData();
            float outDoorData = mEmotionDataModelList.get(i).getOutData();
            inDoorArray.add(inDoorData);
            outDoorArray.add(outDoorData);
            allDateList.add(mEmotionDataModelList.get(i).getDate().substring(5));
            if(outDoorData!=0f&& inDoorData >outDoorData){
                isIndoorWorseThanOutDoorFlag = true;
            }
        }
        mIEmotionView.setInDoorLineChart(inDoorArray);
        mIEmotionView.setOutDoorLineChart(outDoorArray);
        mIEmotionView.setRespectNameLineChart(allDateList);
        mIEmotionView.setTotalBarNumLineChart(allDateList.size());
        mIEmotionView.setDataIndexLineChart(lineChartIndexTemp + allDateList.size());


        int newMax = EMPTY;
        float maxIndoorlData = 0f;
        float maxOutdoorData = 0f;
        float maxData = EMPTY;
        if (inDoorArray.size() > EMPTY) {
            maxIndoorlData = Collections.max(inDoorArray);
        }
        if (outDoorArray.size() > EMPTY) {
            maxOutdoorData = Collections.max(outDoorArray);
        }
        if (maxIndoorlData > maxOutdoorData) {
            maxData = maxIndoorlData;
        } else {
            maxData = maxOutdoorData;
        }
        mIEmotionView.isShowIndoorErrTv(View.INVISIBLE);
        if (inDoorArray.contains(0f)) {
            mIEmotionView.isShowIndoorErrTv(View.VISIBLE);
            mIEmotionView.setDataBreakPoint();
        }
        if (isIndoorWorseThanOutDoorFlag) {
            mIEmotionView.isShowIndoorErrTv(View.VISIBLE);
            mIEmotionView.setIndoorWorseThanOutDoor();
        }
        //如果 数值 > 1000 单位改为克，所有的数据都除以1000

        if (maxData >= 1000) {
            maxData = maxData / 1000;
            mIEmotionView.setMaxScaleLineChart(G_UNIT_SCALE, mContext.getResources().getString(R.string.mad_air_dashboard_kg_unit));

        } else {
            mIEmotionView.setMaxScaleLineChart(MG_UNIT_SCALE, mContext.getResources().getString(R.string.mad_air_dashboard_g_unit));
        }

        newMax = (int) (Math.ceil(maxData / 100) * 100);

        if (newMax % 2 != EMPTY) {
            newMax = newMax + 1;
        }
        mIEmotionView.setTotalLineBarChart(newMax);
    }

    final int level1 = 100;
    final int level1Devide = 1000;
    final int level2 = 100000;
    final int level2Devide = 1000000;

    private String getValue(float num) {
        if (num < level1) {
            return String.format("%.2f", num);
        } else if (num < level2) {
            return String.format("%.2f", num / level1Devide);
        }
        return String.format("%.2f", num / level2Devide);
    }

    //空气默认 是mg
    private String getAirUnit(float num) {
        if (num < level1) {
            return mContext.getResources().getString(R.string.mad_air_dashboard_g_unit);
        } else if (num < level2) {
            return mContext.getResources().getString(R.string.mad_air_dashboard_kg_unit);
        }
        return mContext.getResources().getString(R.string.mad_air_dashboard_kgg_unit);
    }

    //水默认 是L
    private String getWaterUnit(float num) {
        if (num < level1) {
            return mContext.getResources().getString(R.string.water_default_unit);
        }
        return mContext.getResources().getString(R.string.water_m_unit);
    }


}
