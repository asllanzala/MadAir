package com.honeywell.hch.airtouch.plateform.http.model.emotion;

import java.io.Serializable;

/**
 * Created by Vincent on 10/2/17.
 */

public class EmotionDataModel implements Serializable {
    private String date = "";

    private float indoorData = 0;

    private float outData = 0;

    private float barChartData = 0;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getIndoorData() {
        return indoorData;
    }

    public void setIndoorData(float indoorData) {
        this.indoorData = indoorData;
    }

    public float getOutData() {
        return outData;
    }

    public void setOutData(float outData) {
        this.outData = outData;
    }

    public float getBarChartData() {
        return barChartData;
    }

    public void setBarChartData(float barChartData) {
        this.barChartData = barChartData;
    }

    public EmotionDataModel(float outData, float indoorData, float barChartData) {
        this.indoorData = indoorData;
        this.outData = outData;
        this.barChartData = barChartData;
    }

    public EmotionDataModel() {
    }
}
