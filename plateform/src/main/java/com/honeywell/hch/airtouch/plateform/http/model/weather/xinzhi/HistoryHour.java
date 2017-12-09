package com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi;

import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.util.DateTimeUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;

import java.util.Date;

/**
 * Created by lynnliu on 10/18/15.
 */
public class HistoryHour extends Hour {

    @SerializedName("time_stamp")
    protected String mTimeString;
    protected Date mDate;

    public String getTimeString() {
        return mTimeString;
    }

    public void setTimeString(String timeString) {
        mTimeString = timeString;
        if (!StringUtil.isEmpty(mTimeString)) {
            mDate = DateTimeUtil.getDateTimeFromString(DateTimeUtil.THINKPAGE_TIME_FORMAT,
                    mTimeString);
        }
    }

    public Date getDate() {
        if (!StringUtil.isEmpty(mTimeString)) {
            mDate = DateTimeUtil.getDateTimeFromString(DateTimeUtil.THINKPAGE_TIME_FORMAT,
                    mTimeString);
        }
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
