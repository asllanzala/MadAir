package com.honeywell.hch.airtouch.ui.emotion.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseRequestFragment;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.emotion.interfaceFile.IEmotionView;
import com.honeywell.hch.airtouch.ui.emotion.manager.EmotionPresenter;
import com.honeywell.hch.airtouch.ui.emotion.manager.TryDemoEmotionPresenter;
import com.honeywell.hch.airtouch.ui.emotion.ui.controller.EmotionActivity_new;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 12/8/16.
 */
public class EmotionFragment_new extends BaseRequestFragment implements IEmotionView {

    private ScrollView mScrollView;
    private int mRequestTimeParamer;
    private boolean isGetDataSuccess = false;
    private UserLocationData mUserLocationData;
    private EmotionPresenter mEmotionPresenter;
    private View emotionView;

    private TextView mLineChartTilteTv;
    private LineChartView mLineChartView;

    private TextView mBarChartTitleTv;
    private TextView mBarChartUnitTv;
    private BarChartView mBarChartView;
    private TextView mBarTotalUnitTv;
    private TextView mBarTotalNumTv;
    private TextView mTotalTv;
    private TextView mIndoorWorseTv;
    private final String TAG = "EmotionFragment_new";
    private LinearLayout mRootLl;
    private boolean isAbandon = false;
    private final int ALARMVALVE = 150;
    private final int EMPTY = 0;
    private TextView mIndoorTv;
    private TextView mOutDoorTv;
    private boolean isFromTryDemo = false;
    private LinearLayout mOutDoorLl;
    private TextView mApproxTv;
    private TextView mLineChartUnitTv;

    /*
        0: airTouch
        1: water
     */
    private int mDeviceType;


    public static EmotionFragment_new newInstance(UserLocationData userLocationData, Activity activity, int requestTimeParamer, int deviceType, boolean isFromTryDemo) {
        EmotionFragment_new fragment = new EmotionFragment_new();
        fragment.initUserLocation(userLocationData, activity, requestTimeParamer, deviceType, isFromTryDemo);
        return fragment;
    }

    private void initUserLocation(UserLocationData userLocationData, Activity activity, int requestTimeParamer, int deviceType, boolean isFromTryDemo) {
        this.isFromTryDemo = isFromTryDemo;
        mDeviceType = deviceType;
        mRequestTimeParamer = requestTimeParamer;
        mUserLocationData = userLocationData;
        mParentActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "onCreateView----");
        if (emotionView == null) {
            emotionView = inflater.inflate(R.layout.fragment_emotion_new, container, false);
            initView(emotionView);
            initEmotionUIManager();
            setOnRootViewTouchEvent();
            setOnclickListener();
        }
        initData();
        initOnFillingCallBack();
        return emotionView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(View view) {
        mScrollView = (ScrollView) view.findViewById(R.id.emotion_scrollview);
        mIndoorTv = (TextView) view.findViewById(R.id.emotion_indoor_tv);
        mOutDoorTv = (TextView) view.findViewById(R.id.emotion_outdoor_tv);
        mScrollView.smoothScrollTo(0, 0);
        mIndoorWorseTv = (TextView) view.findViewById(R.id.emotion_indoor_worse_tv);
        mTotalTv = (TextView) view.findViewById(R.id.tv_bar_tips);
        mTotalTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mApproxTv = (TextView) view.findViewById(R.id.tv_unit_top_approx);
        mLineChartUnitTv=(TextView)view.findViewById(R.id.tv_line_unit) ;
        //line chart
        mLineChartTilteTv = (TextView) view.findViewById(R.id.tv_line_chart_title);
        mLineChartView = (LineChartView) view.findViewById(R.id.line_chart);

        //bar chart
        mBarChartTitleTv = (TextView) view.findViewById(R.id.tv_bar_chart_title);
        mBarChartUnitTv = (TextView) view.findViewById(R.id.tv_bar_unit);
        mBarChartView = (BarChartView) view.findViewById(R.id.bar_chart);
        mBarTotalUnitTv = (TextView) view.findViewById(R.id.tv_unit_top_bar);
        mBarTotalNumTv = (TextView) view.findViewById(R.id.tv_bar_num);
        mOutDoorLl = (LinearLayout) view.findViewById(R.id.emotion_outdoor_ll);
        mRootLl = (LinearLayout) view.findViewById(R.id.emotion_root_ll);
    }

    private void initEmotionUIManager() {
        if (isFromTryDemo) {
            mEmotionPresenter = new TryDemoEmotionPresenter((Context) mParentActivity, this, 0);
        } else {
            mEmotionPresenter = new EmotionPresenter((Context) mParentActivity, this, mUserLocationData.getLocationID());
        }
    }

    private void setOnclickListener() {
        mTotalTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                if (mDeviceType == EmotionPresenter.AIR_TOUCH_TYPE) {
                    message = getString(R.string.emotion_air_total_message);
                } else {
                    message = getString(R.string.emotion_water_total_message);
                }
                MessageBox.createSimpleDialog(mParentActivity, "", message, getString(R.string.ok), null);
            }
        });
        mIndoorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDeviceType == EmotionPresenter.AIR_TOUCH_TYPE) {
                    MessageBox.createSimpleDialog(mParentActivity, "", getString(R.string.emotion_air_device_average_message), getString(R.string.ok), null);
                }

            }
        });
        mOutDoorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDeviceType == EmotionPresenter.AIR_TOUCH_TYPE) {
                    MessageBox.createSimpleDialog(mParentActivity, "", getString(R.string.emotion_air_city_average_message), getString(R.string.ok), null);
                }

            }
        });

    }

    private void setOnRootViewTouchEvent() {
        mRootLl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((EmotionActivity_new) mParentActivity).setViewPagerCanScroll(true);
                isAbandon = false;
                return true;
            }
        });
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isAbandon) {
                    return true;
                }
                return false;
            }
        });
    }

    private void initData() {
        if (mDeviceType == EmotionPresenter.AIR_TOUCH_TYPE) {
            mLineChartTilteTv.setText(getString(R.string.pm25_str));
            mBarChartTitleTv.setText(R.string.mad_air_dashboard_filtered_particulars);
            mIndoorTv.setText(R.string.emotion_indoor_air);
            mOutDoorTv.setText(R.string.emotion_outdoor_air);
            mIndoorTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            mOutDoorTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            mBarTotalUnitTv.setText(R.string.mad_air_dashboard_g_unit);
            mLineChartView.setAlarmNum(Integer.MAX_VALUE);
            mEmotionPresenter.getAirDataFromServer();
            mEmotionPresenter.getTotalDustByLocationID();
            mApproxTv.setVisibility(View.VISIBLE);
        } else if (mDeviceType == EmotionPresenter.AQUA_TOUCH_TYPE) {
            mLineChartTilteTv.setText(getString(R.string.emotion_line_chart_title_water));
            mBarChartTitleTv.setText(R.string.water_filtered_particulars);
            mIndoorTv.setText(R.string.emotion_outdoor_water);
            mOutDoorTv.setText(R.string.emotion_indoor_water);
            mBarTotalUnitTv.setText(getString(R.string.water_default_unit));
            mLineChartView.setAlarmNum(Integer.MAX_VALUE);
            mEmotionPresenter.getWaterDataFromServer();
            mEmotionPresenter.getTotalVolumeByLocationID();
            mApproxTv.setVisibility(View.GONE);
        }
        if (!AppManager.getLocalProtocol().canShowEmotionOutDoorLayout()) {
            mOutDoorLl.setVisibility(View.GONE);
        }
    }

    @Override
    public void getAirDataFromServer(boolean isShowLoading) {
        if (isShowLoading) {
            mDialog = LoadingProgressDialog.show(mParentActivity, getString(R.string.enroll_loading));
        }
        mEmotionPresenter.getDustAndPm25ByLocationID("\'\'", "\'\'");
    }

    @Override
    public void getWaterDataFromServer(boolean isShowLoading) {
        if (isShowLoading) {
            mDialog = LoadingProgressDialog.show(mParentActivity, getString(R.string.enroll_loading));
        }
        mEmotionPresenter.getVolumeAndTdsByLocationID("\'\'", "\'\'");
    }

    @Override
    public void showShareLayout() {
        isGetDataSuccess = true;
        ((EmotionActivity_new) getFragmentActivity()).
                isShowShareRl();
    }

    public boolean isGetDataSuccess() {
        return isGetDataSuccess;
    }

    @Override
    public void dealSuccessCallBack() {
        super.dealSuccessCallBack(null);
        isGetDataSuccess = true;
        ((EmotionActivity_new) getFragmentActivity()).
                isShowShareRl();
        //存储emotion数据的更新时间
        if (mParentActivity != null && ((EmotionActivity_new) mParentActivity).getLastUpdateTime() == HPlusConstants.DEFAULT_TIME) {
            ((EmotionActivity_new) mParentActivity).setLastUpdateTime(System.currentTimeMillis());
        }
    }

    @Override
    public void dealErrorCallBack() {
        super.dealErrorCallBack(null, 0);
    }

    @Override
    public void setRespectTargetNumBarChart(ArrayList<Float> respectTarget) {
        mBarChartView.setRespectTargetNum(respectTarget);
    }

    @Override
    public void setInDoorLineChart(List<Float> respectTarget) {
        mLineChartView.setIndoorTarget(respectTarget);
    }

    @Override
    public void setOutDoorLineChart(List<Float> respectTarget) {
        mLineChartView.setOutDoorTarget(respectTarget);
    }

    @Override
    public void setRespectNameBarChart(ArrayList<String> allDateList) {
        mBarChartView.setRespectName(allDateList);
    }

    @Override
    public void setRespectNameLineChart(ArrayList<String> allDateList) {
        mLineChartView.setRespectName(allDateList);
    }

    @Override
    public void setTotalBarNumBarChart(int size) {
        mBarChartView.setTotalBarNum(size);
    }

    @Override
    public void setTotalBarNumLineChart(int size) {
        mLineChartView.setTotalBarNum(size);
    }

    @Override
    public void setMaxScaleLineChart(int maxScale, String unit) {
        mLineChartView.setMaxScale(maxScale);
        mLineChartUnitTv.setText(R.string.emotion_air_unit);
    }

    @Override
    public void setMaxScaleBarChart(int maxScale) {
        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "maxScale: " + maxScale);
        mBarChartView.setMaxScale(maxScale);
        if (mDeviceType == EmotionPresenter.AIR_TOUCH_TYPE) {
            if (maxScale == 1000) {
                mBarChartUnitTv.setText(getString(R.string.mad_air_dashboard_kg_unit));
            } else {
                mBarChartUnitTv.setText(getString(R.string.mad_air_dashboard_g_unit));
            }
        } else {
            if (maxScale == 1000) {
                mBarChartUnitTv.setText(getString(R.string.water_m_unit));
            } else {
                mBarChartUnitTv.setText(getString(R.string.water_default_unit));
            }
        }
    }

    @Override
    public void setTotalDataBarChart(int max) {
        mBarChartView.setMax(max);

    }

    @Override
    public void setTotalData(String amount, String unit) {
        mBarTotalNumTv.setText(amount);
        mBarTotalUnitTv.setText(unit);
    }

    @Override
    public void setTotalLineBarChart(int max) {
        mLineChartView.setMax(max);
    }

    @Override
    public void setDataIndexLineChart(int index) {
        mLineChartView.setDataIndex(index);
    }

    @Override
    public void setDataIndexBarChart(int index) {
        mBarChartView.setDataIndex(index);
    }

    @Override
    public void setIndoorWorseThanOutDoor() {
        if (mDeviceType == EmotionPresenter.AIR_TOUCH_TYPE) {
            mIndoorWorseTv.setText(R.string.emotion_indoor_air_worse);
        } else if (mDeviceType == EmotionPresenter.AQUA_TOUCH_TYPE) {
            mIndoorWorseTv.setText(R.string.emotion_indoor_water_worse);
        }
    }

    @Override
    public void setDataBreakPoint() {
        if (mDeviceType == EmotionPresenter.AIR_TOUCH_TYPE) {
            mIndoorWorseTv.setText(R.string.emotion_data_break_point_air);
        } else if (mDeviceType == EmotionPresenter.AQUA_TOUCH_TYPE) {
            mIndoorWorseTv.setText(R.string.emotion_data_break_point_water);
        }
    }

    @Override
    public void isShowIndoorErrTv(int visibility) {
        mIndoorWorseTv.setVisibility(visibility);
    }

    private void initOnFillingCallBack() {
        mLineChartView.setOnScrollCallback(new LineChartView.OnScrollCallback() {
            @Override
            public void onScroll(int index) {
                mEmotionPresenter.refreshLineChartData(index);
            }
        });
        mBarChartView.setOnFillingCallback(new BarChartView.OnScrollCallback() {
            @Override
            public void onScroll(int index) {
                mEmotionPresenter.refreshBarChartData(index);
            }
        });
        mLineChartView.setOnTouchCallback(new LineChartView.OnTouchCallback() {
            @Override
            public void onTouch() {
                isAbandon = true;
                ((EmotionActivity_new) mParentActivity).setViewPagerCanScroll(false);
            }
        });
        mBarChartView.setOnTouchCallback(new BarChartView.OnTouchCallback() {
            @Override
            public void onTouch() {
                isAbandon = true;
                ((EmotionActivity_new) mParentActivity).setViewPagerCanScroll(false);
            }
        });
    }

}
