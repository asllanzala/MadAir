package com.honeywell.hch.airtouch.ui.main.ui.messagecenter;

import android.app.Activity;
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
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthMessageModel;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.authorize.manager.AuthorizeManager;
import com.honeywell.hch.airtouch.ui.authorize.manager.AuthorizeUiManager;
import com.honeywell.hch.airtouch.ui.authorize.ui.view.AuthorizeMessageCardView;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseRequestFragment;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;

/**
 * Created by Vincent on 23/9/16.
 */
public class MessageHandleAuthFragment extends BaseRequestFragment implements View.OnClickListener {

    private final String TAG = "MessageHandleActivity";
    private TextView mTitleTv;
    private TextView mMessageFromTv, mMessagePlaceTv, mMessageDeviceTv,
            mMessageRoleTv, mMessageSentTv, mMessageExpireTv;
    private TextView mAuthDeviceTv;
    private TextView mMessageRoleTitleTv;
//    private ImageView mMessageRoleIv;

    private AuthMessageModel mAuthMessageModel;
    protected int mAction;
    private AuthorizeUiManager mAuthorizeUiManager;
    private View mParentView = null;
    private static int mMessageId;
    private static int mMessageType;
    private static boolean mIsPushNotification;
    private TextView mExpireTv;
    private Button mButtonLeft;
    private Button mButtonRight;
    private Button mDeleteBtn;
    private FrameLayout mBackFl;
    private LinearLayout mRoleLl;
    private LinearLayout mExpireLl;
    private LinearLayout mInviteLl;
    private LinearLayout mDeleteLl;
    private RelativeLayout mInitRl;

    public static MessageHandleAuthFragment newInstance(int messageId, int messageType, boolean isPushNotification, Activity activity) {
        MessageHandleAuthFragment fragment = new MessageHandleAuthFragment();
        fragment.initActivity(activity);
        mMessageId = messageId;
        mMessageType = messageType;
        mIsPushNotification = isPushNotification;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAuthManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dealWithIntent();
        initLayout(inflater, container);
        initListener();
        return mParentView;
    }

    private void dealWithIntent() {
        mDialog = LoadingProgressDialog.show(mParentActivity, getString(R.string.footview_loading));
        getMessageByID();
    }

    private void initListener() {
        mButtonLeft.setOnClickListener(this);
        mButtonRight.setOnClickListener(this);
        mBackFl.setOnClickListener(this);
        mDeleteBtn.setOnClickListener(this);
    }

    protected void initAuthManager() {
        mAuthorizeUiManager = new AuthorizeUiManager();
        mAuthorizeUiManager.setErrorCallback(mErrorCallBack);
        mAuthorizeUiManager.setSuccessCallback(mSuccessCallback);
    }

    protected AuthorizeManager.SuccessCallback mSuccessCallback = new AuthorizeManager.SuccessCallback() {
        @Override
        public void onSuccess(ResponseResult responseResult) {
            dealSuccessCallBack(responseResult);
        }
    };

    protected AuthorizeManager.ErrorCallback mErrorCallBack = new AuthorizeManager.ErrorCallback() {
        @Override
        public void onError(ResponseResult responseResult, int id) {
            dealErrorCallBack(responseResult, id);
        }
    };

    private void initLayout(LayoutInflater inflater, ViewGroup container) {
        //4:设备授权消息 (设备接受者)
        mParentView = inflater.inflate(R.layout.activity_message_center_invitation, container, false);
        initView(mParentView);
    }


    private void getMessageByID() {
        mAuthorizeUiManager.getInvitationsById(mMessageId);
    }

    private void initView(View v) {
        initDragDownManager(v, R.id.root_view_id);
        initStatusBar(v);
        mMessageFromTv = (TextView) v.findViewById(R.id.auth_from_name_tv);
        mMessagePlaceTv = (TextView) v.findViewById(R.id.auth_place_name_tv);
        mMessageDeviceTv = (TextView) v.findViewById(R.id.auth_device_name_tv);
        mMessageRoleTv = (TextView) v.findViewById(R.id.auth_role_name_tv);
        mMessageSentTv = (TextView) v.findViewById(R.id.auth_sent_name_tv);
        mAuthDeviceTv = (TextView) v.findViewById(R.id.auth_device_tv);
        mMessageRoleTitleTv = (TextView) v.findViewById(R.id.auth_role_tv);
        mTitleTv = (TextView) v.findViewById(R.id.title_textview_id);
        mExpireTv = (TextView) v.findViewById(R.id.message_expired_tv);
        mMessageExpireTv = (TextView) v.findViewById(R.id.auth_expire_name_tv);
        mButtonLeft = (Button) v.findViewById(R.id.message_handle_left);
        mButtonRight = (Button) v.findViewById(R.id.message_handle_right);
        mDeleteBtn = (Button) v.findViewById(R.id.message_handle_delete_btn);
        mBackFl = (FrameLayout) v.findViewById(R.id.enroll_back_layout);
        mRoleLl = (LinearLayout) v.findViewById(R.id.auth_message_role_ll);
        mExpireLl = (LinearLayout) v.findViewById(R.id.auth_message_expire_ll);
        mInviteLl = (LinearLayout) v.findViewById(R.id.message_invite_ll);
        mDeleteLl = (LinearLayout) v.findViewById(R.id.message_delete_ll);
        mInitRl = (RelativeLayout) v.findViewById(R.id.message_handle_init_rl);
        mTitleTv.setText(getString(R.string.message_device_share));
    }

    private void initData() {
        mMessageFromTv.setText(mAuthMessageModel.getSenderName());
        mMessagePlaceTv.setText(mAuthMessageModel.getLocationName());
        mMessageDeviceTv.setText(mAuthMessageModel.getTargetName());
        mMessageRoleTv.setText(mAuthMessageModel.parseRole());
        //device as default, otherwise group
        if (mAuthMessageModel.getTargetType() == AuthMessageModel.GROUP) {
            mAuthDeviceTv.setText(R.string.authorize_send_invitation_group);
        }
        //revoke or remove, hide role column
        if (mAuthMessageModel.getMessageType() == 3 || mAuthMessageModel.getMessageType() == 6) {
            mRoleLl.setVisibility(View.GONE);
        }
        if (mAuthMessageModel.getMessageType() == 4) {
            mExpireLl.setVisibility(View.VISIBLE);
            mInviteLl.setVisibility(View.VISIBLE);
            mDeleteLl.setVisibility(View.GONE);
            //if invited message is expired the button function is delete.
            if (mAuthMessageModel.isExpired()) {
                mExpireTv.setVisibility(View.VISIBLE);
                mInviteLl.setVisibility(View.GONE);
                mDeleteLl.setVisibility(View.VISIBLE);
            }
            mMessageExpireTv.setText(DateTimeUtil.getDateTimeString(DateTimeUtil.AUTHORIZE_TIME_FORMAT, DateTimeUtil.AUTHORIZE_TIME_FORMAT2,
                    DateTimeUtil.AUTHORIZE_TIME_TO_FORMAT, mAuthMessageModel.getExpirationTime()));
        } else {
            mInviteLl.setVisibility(View.GONE);
            mDeleteLl.setVisibility(View.VISIBLE);
        }


        mMessageSentTv.setText(DateTimeUtil.getDateTimeString(DateTimeUtil.AUTHORIZE_TIME_FORMAT, DateTimeUtil.AUTHORIZE_TIME_FORMAT2,
                DateTimeUtil.AUTHORIZE_TIME_TO_FORMAT, mAuthMessageModel.getInvitationTime()));
        mInitRl.setVisibility(View.GONE);
    }

    @Override
    protected void dealSuccessCallBack(ResponseResult responseResult) {
        super.dealSuccessCallBack(responseResult);
        switch (responseResult.getRequestId()) {
            case GET_AUTH_MESSAGE_BY_ID:
                mAuthMessageModel = mAuthorizeUiManager.getAuthMessageResponse(responseResult);
                initData();
                break;
            case HANDLE_AUTH_MESSAGE:
                ((MessageHandleActivity) mParentActivity).backResultClick(mAction);
                break;
        }
    }

    @Override
    protected void dealErrorCallBack(ResponseResult responseResult, int id) {
        super.dealErrorCallBack(responseResult, id);
        switch (responseResult.getRequestId()) {
            case GET_AUTH_MESSAGE_BY_ID:
                if (mNetWorkErrorLayout != null && mNetWorkErrorLayout.getVisibility() == View.VISIBLE) {
                    return;
                } else {
                    ((MessageHandleActivity) mParentActivity).getMessageFail(true);
                }
                break;
            case HANDLE_AUTH_MESSAGE:
                String errorStr = getString(R.string.enroll_error);
                switch (mAction) {
                    case AuthorizeMessageCardView.GOTITACTION:
                        errorStr = getString(R.string.message_delete_fail);
                        break;
                    case AuthorizeMessageCardView.ACCEPTACTION:
                        errorStr = getString(R.string.message_acept_fail);
                        break;
                    case AuthorizeMessageCardView.DECLINEACTON:
                        errorStr = getString(R.string.message_decline_fail);
                        break;
                }
                errorHandle(responseResult, errorStr);
                break;
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.enroll_back_layout) {
            if (mIsPushNotification) {
                ((MessageHandleActivity) mParentActivity).getMessageFail(false);
            } else {
                ((MessageHandleActivity) mParentActivity).backResultRead();
            }

        } else {
            if (v.getId() == R.id.message_handle_left) {
                mAlertDialog = MessageBox.createTwoButtonDialog(mParentActivity, null, getString(R.string.authorize_decline_confirm, mAuthMessageModel.getSenderName()), getString(R.string.cancel),
                        null, getString(R.string.authorize_message_decline), declineMessageBoxBtn);
                mAction = AuthorizeMessageCardView.DECLINEACTON;
            } else if (v.getId() == R.id.message_handle_right) {
                mAction = AuthorizeMessageCardView.ACCEPTACTION;
                mDialog = LoadingProgressDialog.show(mParentActivity, getString(R.string.enroll_loading));
                mAuthorizeUiManager.handleInvitation(mAuthMessageModel.getMessageId(), AuthorizeMessageCardView.ACCEPTACTION);

            } else if (v.getId() == R.id.message_handle_delete_btn) {
                deleteMessage(mAuthMessageModel.getMessageId());
            }
        }
    }

    protected void deleteMessage(int messageId) {
        mAction = AuthorizeMessageCardView.GOTITACTION;
        mDialog = LoadingProgressDialog.show(mParentActivity, getString(R.string.enroll_loading));
        mAuthorizeUiManager.handleInvitation(messageId, AuthorizeMessageCardView.GOTITACTION);
    }

    private MessageBox.MyOnClick declineMessageBoxBtn = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            mAction = AuthorizeMessageCardView.DECLINEACTON;
            mDialog = LoadingProgressDialog.show(mParentActivity, getString(R.string.enroll_loading));
            mAuthorizeUiManager.handleInvitation(mAuthMessageModel.getMessageId(), AuthorizeMessageCardView.DECLINEACTON);
        }
    };

}
