package com.honeywell.hch.airtouch.ui.emotion.interfaceFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 27/12/16.
 */

public interface IEmotionView {

    void dealSuccessCallBack();

    void dealErrorCallBack();

    void setRespectTargetNumBarChart(ArrayList<Float> respectTarget);

    void setInDoorLineChart(List<Float> inDoorTarget);

    void setOutDoorLineChart(List<Float> outDoorTarget);

    void setRespectNameBarChart(ArrayList<String> allDateList);

    void setRespectNameLineChart(ArrayList<String> allDateList);

    void setTotalBarNumBarChart(int size);

    void setTotalBarNumLineChart(int size);

    void setMaxScaleLineChart(int maxScale, String unit);

    void setMaxScaleBarChart(int maxScale);

    void setTotalDataBarChart(int max);

    void setTotalLineBarChart(int max);

    void setTotalData(String amount, String unit);

    void setDataIndexLineChart(int index);

    void setDataIndexBarChart(int index);

    void setIndoorWorseThanOutDoor();

    void setDataBreakPoint();

    void isShowIndoorErrTv(int visibility);

    void getAirDataFromServer(boolean isShowLoading);

    void getWaterDataFromServer(boolean isShowLoading);

    void showShareLayout();
}
