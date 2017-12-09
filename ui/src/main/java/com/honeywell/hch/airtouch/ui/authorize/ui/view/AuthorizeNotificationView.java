package com.honeywell.hch.airtouch.ui.authorize.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.GetAuthUnreadMsgResponse;

/**
 * Created by Vincent on 1/2/16.
 */
public class AuthorizeNotificationView extends RelativeLayout {
    private Context mContext;
    private ImageView mSingleIv;
    private ImageView mMultiIv;
    private TextView mFromNameTv;
    private TextView mAuthStatus;
    private RelativeLayout mRootRl;

    public AuthorizeNotificationView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public AuthorizeNotificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.auth_notification_view, this);
        mRootRl = (RelativeLayout) findViewById(R.id.auth_notify_info_layout);
        mSingleIv = (ImageView) findViewById(R.id.auth_notify_single_info);
        mMultiIv = (ImageView) findViewById(R.id.auth_notify_multi_info);
        mFromNameTv = (TextView) findViewById(R.id.auth_notify_from);
        mAuthStatus = (TextView) findViewById(R.id.auth_notify_status);
    }

    public void updateView(GetAuthUnreadMsgResponse mGetAuthUnreadMsg) {
        switch (mGetAuthUnreadMsg.getUnreadMsgCount()) {
            case 0:
                mRootRl.setVisibility(GONE);
                break;
            case 1:
                mRootRl.setVisibility(VISIBLE);
                mSingleIv.setVisibility(VISIBLE);
                break;
            case 2:
                mRootRl.setVisibility(VISIBLE);
                mSingleIv.setVisibility(GONE);
                mMultiIv.setVisibility(VISIBLE);
                break;
            default:
                mRootRl.setVisibility(VISIBLE);
                mMultiIv.setVisibility(VISIBLE);
                break;
        }
        if (mGetAuthUnreadMsg.getUnreadMsgCount() != 0 && mGetAuthUnreadMsg.getLatestAuthMsgs() != null) {
            mFromNameTv.setText(mContext.getString(R.string.authorize_notification_from, mGetAuthUnreadMsg.getLatestAuthMsgs().getmSenderName()));
            mAuthStatus.setText(mContext.getString(mGetAuthUnreadMsg.getLatestAuthMsgs().parseMessageType()));
        }
    }

    public void setInVisible() {
        mRootRl.setVisibility(GONE);
    }

}
