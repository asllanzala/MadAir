package com.honeywell.hch.airtouch.ui.emotion.ui.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.EmotionBottleResponse;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseRequestFragment;
import com.honeywell.hch.airtouch.ui.common.ui.view.GroupContolListView;
import com.honeywell.hch.airtouch.ui.emotion.manager.EmotionManager;
import com.honeywell.hch.airtouch.ui.emotion.manager.EmotionUiManager;
import com.honeywell.hch.airtouch.ui.emotion.manager.IEmotionUiManager;
import com.honeywell.hch.airtouch.ui.emotion.ui.adapter.EmotionItemAdapter;
import com.honeywell.hch.airtouch.ui.emotion.ui.controller.EmotionActivity;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoEmotionUiManager;

import java.util.List;

/**
 * Created by Vincent on 12/8/16.
 */
public class EmotionFragment extends BaseRequestFragment {

    private RelativeLayout mLoadingRl;
    private ImageView mLoadingIv;
    private TextView mLoadingTv;
    private int mUserLocationId;
    private ScrollView mScrollView;
    private IEmotionUiManager mEmotionUiManager;
    private int mRequestTimeParamer;
    private List<EmotionBottleResponse> mEmotionBottleResponseList;
    private GroupContolListView mEmotionLv;
    private EmotionItemAdapter mEmotionItemAdapter;
    private boolean isGetDataSuccess = false;
    private UserLocationData mUserLocationData;

    private boolean isFromTryDemo = false;


    public static EmotionFragment newInstance(int locationId, Activity activity, int requestTimeParamer, boolean isFromTryDemo) {
        EmotionFragment fragment = new EmotionFragment();
        fragment.initUserLocation(locationId, activity, requestTimeParamer, isFromTryDemo);
        return fragment;
    }

    private void initUserLocation(int locationId, Activity activity, int requestTimeParamer, boolean isFromTryDemo) {
        this.isFromTryDemo = isFromTryDemo;
        mRequestTimeParamer = requestTimeParamer;
        mUserLocationId = locationId;
        mUserLocationData = UserDataOperator.getUserLocationByID(mUserLocationId, UserAllDataContainer.shareInstance().getUserLocationDataList());
        mParentActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "onCreateView----");
        initEmotionUIManager();
        View emotionView = inflater.inflate(R.layout.fragment_emotion, container, false);
        initView(emotionView);
        getPMLevelFromServer();
        return emotionView;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(View view) {
        mLoadingRl = (RelativeLayout) view.findViewById(R.id.emotion_loading_rl);
        mLoadingIv = (ImageView) view.findViewById(R.id.emotion_loading_iv);
        mLoadingTv = (TextView) view.findViewById(R.id.emotion_loading_tv);
        mScrollView = (ScrollView) view.findViewById(R.id.emotion_scrollview);
        mScrollView.smoothScrollTo(0, 0);
        mEmotionLv = (GroupContolListView) view.findViewById(R.id.emoton_listView);

    }

    private void getPMLevelFromServer() {
        setLoadingVew(true, true);
        mEmotionUiManager.getPMLevelFromServer(mRequestTimeParamer);
    }

    private void loadListViewData() {
        mEmotionItemAdapter = new EmotionItemAdapter(getContext(), mEmotionBottleResponseList);
        mEmotionLv.setAdapter(mEmotionItemAdapter);
    }

    private EmotionManager.SuccessCallback mSuccessCallback = new EmotionManager.SuccessCallback() {
        @Override
        public void onSuccess(ResponseResult responseResult) {
            switch (responseResult.getRequestId()) {
                case EMOTION_BOTTLE:
                    isGetDataSuccess = true;
                    setLoadingVew(false, false);
                    mEmotionBottleResponseList = mEmotionUiManager.getEmotionBottleResponse(responseResult, mUserLocationData);
                    loadListViewData();
                    ((EmotionActivity) getFragmentActivity()).isShowShareRl();

                    //存储emotion数据的更新时间
                    if (mParentActivity != null && ((EmotionActivity) mParentActivity).getLastUpdateTime() == HPlusConstants.DEFAULT_TIME) {
                        ((EmotionActivity) mParentActivity).setLastUpdateTime(System.currentTimeMillis());
                    }
                    break;
            }
        }
    };

    private EmotionManager.ErrorCallback mErrorCallBack = new EmotionManager.ErrorCallback() {
        @Override
        public void onError(ResponseResult responseResult, int id) {
            switch (responseResult.getRequestId()) {
                case EMOTION_BOTTLE:
                    isGetDataSuccess = false;
                    setLoadingVew(true, false);
                    ((EmotionActivity) getFragmentActivity()).isShowShareRl();
                    break;
            }
        }
    };

    private void initEmotionUIManager() {

        if (isFromTryDemo) {
            mEmotionUiManager = new TryDemoEmotionUiManager();

        } else {
            mEmotionUiManager = new EmotionUiManager(mUserLocationId);
        }
        initManagerCallBack();
    }

    private void initManagerCallBack() {
        mEmotionUiManager.setErrorCallback(mErrorCallBack);
        mEmotionUiManager.setSuccessCallback(mSuccessCallback);
    }

    private void setLoadingVew(boolean isNeedShown, boolean isLoading) {
        if (isNeedShown) {
            mLoadingRl.setVisibility(View.VISIBLE);
            int loadingIc = isLoading ? R.drawable.loading_white_icon : R.drawable.loading_error_white_icon;
            mLoadingIv.setImageResource(loadingIc);
            int loadingTv = isLoading ? R.string.all_device_loading : R.string.all_device_loading_failed;
            mLoadingTv.setText(loadingTv);
        } else {
            mLoadingRl.setVisibility(View.GONE);
        }
    }

    public boolean isGetDataSuccess() {
        return isGetDataSuccess;
    }
}
