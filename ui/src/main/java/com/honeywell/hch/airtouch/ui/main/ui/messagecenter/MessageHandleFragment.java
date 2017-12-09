package com.honeywell.hch.airtouch.ui.main.ui.messagecenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.DateTimeUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.model.message.MessageDetailResponse;
import com.honeywell.hch.airtouch.plateform.update.UpdateManager;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.control.manager.model.ControlConstant;
import com.honeywell.hch.airtouch.ui.control.ui.device.controller.DeviceControlActivity;
import com.honeywell.hch.airtouch.ui.main.manager.Message.manager.MessageManager;
import com.honeywell.hch.airtouch.ui.main.manager.Message.manager.MessageUiManager;
import com.honeywell.hch.airtouch.ui.update.ui.UpdateVersionMinderActivity;

/**
 * Created by Vincent on 23/9/16.
 */
public class MessageHandleFragment extends MessageHandleAuthFragment implements View.OnClickListener {
    private final String TAG = "MessageHandleActivity";
    private TextView mTitleTv;
    private TextView mMessagePlaceTv, mMessageDeviceTv,
            mMessageTimeTv, mMessageDescriptionTv;
    private MessageUiManager mMessageUiManager;
    private View mParentView = null;
    private static int mMessageId;
    private static boolean mIsPushNotification;
    private static int mMessageType;
    private String mLanguage;
    private MessageDetailResponse mMessageDetailResponse;
    private Button mButton;
    private FrameLayout mBackFl;
    private RelativeLayout mInitRl;
    private LinearLayout mPlaceDeviceLl;
    private Button mDeleteBtn;

    public static MessageHandleFragment newInstance(int messageId, int messageType, boolean isPushNotification, Activity activity) {
        MessageHandleFragment fragment = new MessageHandleFragment();
        fragment.initActivity(activity);
        mMessageId = messageId;
        mMessageType = messageType;
        mIsPushNotification = isPushNotification;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLanguage = AppConfig.shareInstance().getLanguage();
        initMessageManager();
        initAuthManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        dealWithIntent();
        mParentView = inflater.inflate(R.layout.activity_message_center_invitation_check, container, false);
        initView(mParentView);
        initListener();
        return mParentView;
    }

    private void dealWithIntent() {
        mDialog = LoadingProgressDialog.show(mParentActivity, getString(R.string.footview_loading));
        getMessageByID();
    }

    private void initListener() {
        mButton.setOnClickListener(this);
        mBackFl.setOnClickListener(this);
        mDeleteBtn.setOnClickListener(this);
    }

    protected void initMessageManager() {
        mMessageUiManager = new MessageUiManager();
        mMessageUiManager.setErrorCallback(mErrorCallBack);
        mMessageUiManager.setSuccessCallback(mSuccessCallback);
    }

    protected MessageManager.SuccessCallback mSuccessCallback = new MessageManager.SuccessCallback() {
        @Override
        public void onSuccess(ResponseResult responseResult) {
            dealSuccessCallBack(responseResult);
        }
    };

    protected MessageManager.ErrorCallback mErrorCallBack = new MessageManager.ErrorCallback() {
        @Override
        public void onError(ResponseResult responseResult, int id) {
            dealErrorCallBack(responseResult, id);
        }
    };

    private void getMessageByID() {
        mMessageUiManager.getMessageByIdTask(mMessageId, mLanguage);
    }

    private void initView(View v) {
        initDragDownManager(v, R.id.root_view_id);
        initStatusBar(v);
        mMessagePlaceTv = (TextView) v.findViewById(R.id.auth_place_name_tv);
        mMessageDeviceTv = (TextView) v.findViewById(R.id.auth_device_name_tv);
        mMessageTimeTv = (TextView) v.findViewById(R.id.auth_time_name_tv);
        mMessageDescriptionTv = (TextView) v.findViewById(R.id.auth_description_name_tv);
        mTitleTv = (TextView) v.findViewById(R.id.title_textview_id);
        mButton = (Button) v.findViewById(R.id.message_handle_check_btn);
        mBackFl = (FrameLayout) v.findViewById(R.id.enroll_back_layout);
        mInitRl = (RelativeLayout) v.findViewById(R.id.message_handle_init_rl);
        mPlaceDeviceLl = (LinearLayout) v.findViewById(R.id.message_handle_place_device_ll);
        mDeleteBtn = (Button) v.findViewById(R.id.message_handle_delete_btn);
    }

    private void initData() {
        switch (mMessageType) {
            case MessageHandleActivity.REMOTE_CONTROL_TYPE:
                mTitleTv.setText(getString(R.string.message_remote_title));
                break;
            case MessageHandleActivity.WATER_ERROR_TYPE:
                mTitleTv.setText(getString(R.string.message_device_error));
                mMessageDescriptionTv.setTextColor(getResources().getColor(R.color.pm_25_worst));
                break;
            case MessageHandleActivity.REGULAR_NOTICE_TYPE:
                mTitleTv.setText(getString(R.string.message_handle_refular_title));
                mPlaceDeviceLl.setVisibility(View.GONE);
                mDeleteBtn.setVisibility(View.VISIBLE);
                mButton.setVisibility(View.GONE);
                break;
        }
        mMessagePlaceTv.setText(mMessageDetailResponse.getmLocationName());
        mMessageDeviceTv.setText(mMessageDetailResponse.getmTargetName());
        mMessageTimeTv.setText(DateTimeUtil.getDateTimeString(DateTimeUtil.AUTHORIZE_TIME_FORMAT, DateTimeUtil.AUTHORIZE_TIME_FORMAT2,
                DateTimeUtil.AUTHORIZE_TIME_TO_FORMAT, mMessageDetailResponse.getmMessageTime()));
        mMessageDescriptionTv.setText(mMessageDetailResponse.getmMessageContent());
        mInitRl.setVisibility(View.GONE);
    }

    @Override
    protected void dealSuccessCallBack(ResponseResult responseResult) {
        super.dealSuccessCallBack(responseResult);
        switch (responseResult.getRequestId()) {
            case GET_MESSAGE_BY_ID:
                mMessageDetailResponse = mMessageUiManager.getMessageDetailResponse(responseResult);
                initData();
                break;
        }
    }

    @Override
    protected void dealErrorCallBack(ResponseResult responseResult, int id) {
        super.dealErrorCallBack(responseResult, id);
        switch (responseResult.getRequestId()) {
            case GET_MESSAGE_BY_ID:
                if (mNetWorkErrorLayout != null && mNetWorkErrorLayout.getVisibility() == View.VISIBLE) {
                    return;
                } else {
                    ((MessageHandleActivity) mParentActivity).getMessageFail(true);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mLanguage = AppConfig.shareInstance().getLanguage();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.enroll_back_layout) {
            if (mIsPushNotification) {
                ((MessageHandleActivity) mParentActivity).getMessageFail(false);
            } else {
                ((MessageHandleActivity) mParentActivity).backResultRead();
            }
        } else if (v.getId() == R.id.message_handle_check_btn) {
            HomeDevice homeDevice = UserDataOperator.getDeviceWithDeviceId(mMessageDetailResponse.getmTargetId(),UserAllDataContainer.shareInstance().getUserLocationDataList(),
                    UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList());
            if (homeDevice == null) {
                MessageBox.createSimpleDialog(mParentActivity, null, getString(R.string.message_not_find_device), getString(R.string.ok), null);
            } else if (!DeviceType.isHplusSeries(homeDevice.getDeviceType())) {
                unSupportVersionUpdate();
            } else {
                goToDeviceControlActivity();
            }
        } else if (v.getId() == R.id.message_handle_delete_btn) {
            deleteMessage(mMessageId);
        }
    }

    private void unSupportVersionUpdate() {
        Intent intent = new Intent(mParentActivity, UpdateVersionMinderActivity.class);
        intent.putExtra(UpdateManager.UPDATE_TYPE, HPlusConstants.UNSUPPORT_MESSAGE_UPDATE);
        startActivity(intent);
        mParentActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    private void goToDeviceControlActivity() {
        Intent intent = new Intent(mParentActivity, DeviceControlActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(DeviceControlActivity.ARG_LOCATION, mMessageDetailResponse.getmLocationId());
        bundle.putInt(DeviceControlActivity.ARG_FROM_TYPE, ControlConstant.FROM_NORMAL_CONTROL);

        bundle.putInt(DeviceControlActivity.ARG_DEVICE_ID, mMessageDetailResponse.getmTargetId());
        intent.putExtras(bundle);
        startActivity(intent);
        mParentActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }
}
