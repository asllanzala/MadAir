package com.honeywell.hch.airtouch.ui.authorize.ui.controller.invite;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthBaseModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthDeviceModel;
import com.honeywell.hch.airtouch.ui.authorize.ui.controller.list.AuthorizeBaseActivity;
import com.honeywell.hch.airtouch.ui.common.ui.view.DropDownAnimationManager;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;

/**
 * Created by Vincent on 1/2/16.
 */
public class AuthorizeSendInvitationActivity extends AuthorizeBaseActivity implements AuthorizeBaseActivity.AuthorizeClick {

    private final String TAG = "AuthorizeSendInvitation";
    private TextView mTitleTv;
    private TextView mPlaceTv;
    private TextView mDevieTv;
    private TextView mAuthToTv;
    private TextView mRole;

    private Button mSendBtn;
    private AuthBaseModel mAuthBaseModel;
    private RelativeLayout mRootRl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_send_invitation);
        initStatusBar();
        init();
        dealWithIntent();
        initData();
    }

    private void init() {
        mRootRl = (RelativeLayout) findViewById(R.id.root_view_id);
        mDropDownAnimationManager = new DropDownAnimationManager(mRootRl, mContext);
        mSendBtn = (Button) findViewById(R.id.startConnectBtn);
        mTitleTv = (TextView) findViewById(R.id.title_textview_id);
        mPlaceTv = (TextView) findViewById(R.id.auth_send_invitation_place_name_tv);
        mDevieTv = (TextView) findViewById(R.id.auth_send_invitation_device_name_tv);
        mAuthToTv = (TextView) findViewById(R.id.auth_send_invitation_authorize_name_tv);
        mRole = (TextView) findViewById(R.id.auth_send_invitation_role_name_tv);
    }

    private void initData() {
        mSendBtn.setVisibility(View.VISIBLE);
        mSendBtn.setText(R.string.authorize_send_invitation);
        mTitleTv.setText(R.string.authorize_confirm_item);
        mPlaceTv.setText(mAuthBaseModel.getmLocationName());
        mDevieTv.setText(mAuthBaseModel.getModelName());
        mAuthToTv.setText(mAuthBaseModel.authToPhoneNameListToString());
        mRole.setText(getString(mAuthBaseModel.parseRole()));
    }

    private void dealWithIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAuthBaseModel = (AuthDeviceModel) bundle.get(INTENTPARAMETEROBJECT);
        }
    }

    @Override
    protected void dealSuccessCallBack(ResponseResult responseResult) {
        super.dealSuccessCallBack(responseResult);
        boolean isSuccess = mAuthorizeUiManager.
                grantAuthToDeviceInviteResponse(responseResult, mAuthBaseModel);
        if (isSuccess) {
            startIntentBack(mAuthBaseModel.getmClass(), mAuthBaseModel, ClickType.AUTHROIZE);
        } else {
            mDropDownAnimationManager.showDragDownLayout(getString(R.string.authorize_send_send_fail), true);
        }
    }

    public void doClick(View v) {
        super.doClick(v);
        if (v.getId() == R.id.startConnectBtn && !isNetworkOff()) {
            mDialog = LoadingProgressDialog.show(mContext, getString(R.string.enroll_loading));
            mAuthorizeUiManager.grantAuthToDevice(mAuthBaseModel.getModelId(), mAuthBaseModel.getRole(), mAuthBaseModel.getmGrantUserPhoneNumber());
        }
    }
}
