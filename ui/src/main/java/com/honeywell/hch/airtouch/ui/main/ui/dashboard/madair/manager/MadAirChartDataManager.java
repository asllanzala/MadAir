package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.manager;

import android.content.Context;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirHistoryRecord;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.view.PercentageBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by zhujunyu on 2016/11/24.
 */

public class MadAirChartDataManager {
    private final String TAG = "MadAirChartDataManager";
    private static final int MG_UNIT_SCALE = 1;
    private static final int G_UNIT_SCALE = 1000;

    private PercentageBar mPercentageBar;
    private ArrayList<Float> respectTarget;
    private ArrayList<String> respName;
    private ArrayList<String> allDateList;
    private MadAirDeviceModel mMadAirDeviceModel;
    private TextView mTextViewNum;
    private TextView mTextViewUnit;
    private TextView mTextViewNumUnit;
    private float mPMNum;
    private Context mContext;

    public MadAirChartDataManager(Context context) {
        mContext = context;
    }

    public void setPercentageBar(MadAirDeviceModel madAirDeviceModel, PercentageBar percentageBar, TextView mTextViewNum, TextView mTextViewNumUnit, TextView textViewUnit) {
        this.mPercentageBar = percentageBar;
        this.mMadAirDeviceModel = madAirDeviceModel;
        this.mTextViewNum = mTextViewNum;
        this.mTextViewUnit = textViewUnit;
        this.mTextViewNumUnit = mTextViewNumUnit;
        initPercentageBar();
    }

    public void initPercentageBar() {
        respectTarget = new ArrayList<Float>();
        respName = new ArrayList<String>();
        refactorData();
    }

    public void refleshView() {
        mPercentageBar.invalidate();
    }


    public void refactorData() {
        allDateList = new ArrayList<String>();

        Calendar firstData = Calendar.getInstance();
        firstData.add(Calendar.DATE, -30);

        Calendar theCa = Calendar.getInstance();
        theCa.setTime(new Date());

        //第一天
        theCa.add(theCa.DATE, -30);
        String month = theCa.get(Calendar.MONTH) < 9 ? "0" + (theCa.get(Calendar.MONTH) + 1) : String.valueOf(theCa.get(Calendar.MONTH) + 1);
        String day = theCa.get(Calendar.DAY_OF_MONTH) < 10 ? "/0" + theCa.get(Calendar.DAY_OF_MONTH) : "/" + theCa.get(Calendar.DAY_OF_MONTH);
        allDateList.add(month + day);
        for (int i = 0; i < 29; i++) {
            theCa.add(theCa.DATE, +1);
            String monthFor = theCa.get(Calendar.MONTH) < 9 ? "0" + (theCa.get(Calendar.MONTH) + 1) : String.valueOf(theCa.get(Calendar.MONTH) + 1);
            String dayFor = theCa.get(Calendar.DAY_OF_MONTH) < 10 ? "/0" + theCa.get(Calendar.DAY_OF_MONTH) : "/" + theCa.get(Calendar.DAY_OF_MONTH);
            allDateList.add(monthFor + dayFor);
        }

        Map<String, Float> mapdata = new HashMap<>();
        MadAirHistoryRecord record = mMadAirDeviceModel.getMadAirHistoryRecord();
        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "record: " + record);
        if (record == null) {
            System.out.println("初始化。。。" + "record为空");
            return;
        }

        mapdata = sortMapByKey(record.getParticleMap());
        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "mapdata: " + mapdata);
        if (mapdata == null) {
            return;
        }

        mPMNum = 0;
        for (Map.Entry<String, Float> map : mapdata.entrySet()) {
            System.out.println(map.getKey() + "\t" + map.getValue());
            Calendar cal = StrToDate(map.getKey());

            if (compareCalendar(cal, firstData) > 0 && compareCalendar(Calendar.getInstance(), cal) > 0) {
                mPMNum = mPMNum + map.getValue();
                respectTarget.add(0.0f + map.getValue());
                String monthResp = cal.get(Calendar.MONTH) < 9 ? "0" + (cal.get(Calendar.MONTH) + 1) : String.valueOf(cal.get(Calendar.MONTH) + 1);
                String dayResp = cal.get(Calendar.DAY_OF_MONTH) < 10 ? "/0" + cal.get(Calendar.DAY_OF_MONTH) : "/" + cal.get(Calendar.DAY_OF_MONTH);
                respName.add(monthResp + dayResp);
            }
        }
        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "allDateList.size(): " + allDateList.size());
        for (int k = 0; k < allDateList.size(); k++) {
            System.out.println("————————————" + allDateList.get(k));
            if (respName.contains(allDateList.get(k))) {
            } else {
                respectTarget.add(k, 0f);
            }
        }

        mPercentageBar.setRespectTargetNum(respectTarget);
        mPercentageBar.setRespectName(allDateList);
        mPercentageBar.setTotalBarNum(respectTarget.size());

        int newMax = 0;
        float maxOrginalData = 0;
        if (respectTarget.size() > 0) {
            maxOrginalData = Collections.max(respectTarget);
        }
        //如果 数值 > 1000 单位改为克，所有的数据都除以1000

        if (maxOrginalData >= 1000) {
            maxOrginalData = maxOrginalData / 1000;
//            for (int m = 0; m < respectTarget.size(); m++) {
//                respectTarget.set(m, respectTarget.get(m) / 1000);
//            }
            mPercentageBar.setMaxScale(G_UNIT_SCALE);
            mTextViewUnit.setText(mContext.getResources().getString(R.string.mad_air_dashboard_g_unit));
        } else {
            mPercentageBar.setMaxScale(MG_UNIT_SCALE);
            mTextViewUnit.setText(mContext.getResources().getString(R.string.mad_air_dashboard_default_unit));
        }

        newMax = (int) Math.ceil(maxOrginalData);

        if (newMax % 2 != 0) {
            newMax = newMax + 1;
        }
        mPercentageBar.setMax(newMax);
        mTextViewNum.setText(getValue(mPMNum));
        mTextViewNumUnit.setText(getUnit(mPMNum));
    }


    public int compareCalendar(Calendar calendarOne, Calendar calendarTwo) {
        if (calendarOne.get(Calendar.YEAR) > calendarTwo.get(Calendar.YEAR)) {
            return 1;
        } else if (calendarOne.get(Calendar.YEAR) == calendarTwo.get(Calendar.YEAR) && calendarOne.get(Calendar.MONTH) > calendarTwo.get(Calendar.MONTH)) {
            return 1;
        } else if (calendarOne.get(Calendar.YEAR) == calendarTwo.get(Calendar.YEAR) && calendarOne.get(Calendar.MONTH) == calendarTwo.get(Calendar.MONTH)
                && calendarOne.get(Calendar.DAY_OF_MONTH) >= calendarTwo.get(Calendar.DAY_OF_MONTH)) {
            return 1;
        }
        return 0;
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, Float> sortMapByKey(Map<String, Float> map) {
        if (map == null) {
            return null;
        }

        Map<String, Float> sortMap = new TreeMap<String, Float>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }

    /**
     * 日期转换成字符串
     *
     * @return str
     */
    public static Calendar StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        Calendar cal = Calendar.getInstance();
        try {
            date = format.parse(str);
            cal = Calendar.getInstance();
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    static class MapKeyComparator implements Comparator<String> {

        @Override
        public int compare(String str1, String str2) {

            return str1.compareTo(str2);
        }
    }

    static int level1 = 100;
    static int level1Devide = 1000;
    static int level2 = 100000;
    static int level2Devide = 1000000;

    private String getValue(float num) {
        if (num < level1) {
            return String.format("%.2f", num);
        } else if (num < level2) {
            return String.format("%.2f", num / level1Devide);
        }
        return String.format("%.2f", num / level2Devide);
    }


    private String getUnit(float num) {
        if (num < level1) {
            return mContext.getResources().getString(R.string.mad_air_dashboard_default_unit);
        } else if (num < level2) {
            return mContext.getResources().getString(R.string.mad_air_dashboard_g_unit);
        }
        return mContext.getResources().getString(R.string.mad_air_dashboard_kg_unit);
    }
}
