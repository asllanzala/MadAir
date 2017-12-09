package com.honeywell.hch.airtouch.ui.main.ui.messagecenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.honeywell.hch.airtouch.plateform.http.model.message.MessageModel;
import com.honeywell.hch.airtouch.plateform.http.model.notification.PushMessageModel;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseFragmentActivity;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseRequestFragment;
import com.honeywell.hch.airtouch.ui.main.manager.Message.manager.MessageUiManager;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainActivity;

/**
 * Created by Jesse on 12/8/16.
 */
public class MessageHandleActivity extends BaseFragmentActivity {
    private final String TAG = "MessageHandleActivity";
    public static final String INTENTPARAMETEROBJECT = "intentParameterObject";
    public static final String INTENTPARAMETERCLICKTYPE = "intentParameterClickType"; //0 invitation 1 remove 2revoke

    private MessageModel mMessageModel;
    private PushMessageModel mPushMessageModel;
    private BaseRequestFragment mBaseRequestFragment;
    public static final int AUTHORIZE_TYPE = 1;
    public static final int REMOTE_CONTROL_TYPE = 2;
    public static final int WATER_ERROR_TYPE = 3;
    public static final int REGULAR_NOTICE_TYPE = 4;
    private MessageUiManager mMessageUiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_handle);
        initStatusBar();
        initMessageHandleFragment();
    }

    private void loadHandleAuthMessageFragment(int messageId, int messageType, boolean isPushNotification) {
        mBaseRequestFragment = MessageHandleAuthFragment.newInstance(messageId, messageType, isPushNotification, this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.message_handle_panel, mBaseRequestFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void loadHandleMessageFragment(int messageId, int messageType, boolean isPushNotification) {
        mBaseRequestFragment = MessageHandleFragment.newInstance(messageId, messageType, isPushNotification, this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.message_handle_panel, mBaseRequestFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void initMessageHandleFragment() {
        mMessageModel = (MessageModel) getIntent().getSerializableExtra("message");
        mPushMessageModel = (PushMessageModel) getIntent().getSerializableExtra(PushMessageModel.PUSHPARAMETER);
        mMessageUiManager = new MessageUiManager();
        if (mPushMessageModel == null) {
            int messageType = mMessageModel.getmMessageCategory();
            if (messageType == AUTHORIZE_TYPE) {
                loadHandleAuthMessageFragment(mMessageModel.getmMessageId(), messageType, false);
            } else {
                loadHandleMessageFragment(mMessageModel.getmMessageId(), messageType, false);
            }
        } else {
            //推送过来的
            int pushMessageType = mMessageUiManager.parseMessageType(mPushMessageModel.getmMessageType());
            switch (pushMessageType) {
                case AUTHORIZE_TYPE:
                    loadHandleAuthMessageFragment(mPushMessageModel.getmMessageId(), pushMessageType, true);
                    break;
                case REMOTE_CONTROL_TYPE:
                case WATER_ERROR_TYPE:
                    loadHandleMessageFragment(mPushMessageModel.getmMessageId(), pushMessageType, true);
                    break;
                default:
                    getMessageFail(false);
            }
        }
    }

    public void getMessageFail(boolean isFail) {
        Intent intent = new Intent(mContext, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(INTENTPARAMETEROBJECT, isFail);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        finish();
    }

    public void backResultClick(int action) {
        if (mPushMessageModel == null) {
            Intent intent = new Intent();
            intent.putExtra(DashBoadConstant.ARG_HANDLE_MESSAGE, action);
            setResult(RESULT_OK, intent);
            backIntent();
        } else {
            getMessageFail(false);
        }
    }

    public void backResultRead() {
        if (mPushMessageModel == null) {
            setResult(DashBoadConstant.RESULT_READ);
            backIntent();
        } else {
            getMessageFail(false);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // when the progress is finding the device , can not be back
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mPushMessageModel != null) {
                getMessageFail(false);
            } else {
                backResultRead();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void dealWithNoNetWork() {
        mBaseRequestFragment.setNoNetWorkView();
    }

    @Override
    public void dealWithNetworkError() {
        mBaseRequestFragment.setNetWorkErrorView();
    }

    @Override
    public void dealNetworkConnected() {
        mBaseRequestFragment.setNetWorkViewGone();
    }

}