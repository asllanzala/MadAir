package com.honeywell.hch.airtouch.ui.authorize.ui.controller.edit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthBaseModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.AuthTo;
import com.honeywell.hch.airtouch.ui.authorize.ui.controller.list.AuthorizeBaseActivity;
import com.honeywell.hch.airtouch.ui.common.ui.view.DropDownAnimationManager;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;

import java.util.List;

/**
 * Created by Vincent on 1/2/16.
 */
public class AuthorizeOwnerEditActivity extends AuthorizeBaseActivity implements AuthorizeBaseActivity.AuthorizeClick {

    private TextView mTitleTv;
    private Button mUnauthorizeBtn;
    private AuthBaseModel mAuthBaseModel;
    private TextView mPlaceTv;
    private TextView mDeviceTv;
    private TextView mAuthorizeTo;
    private TextView mRoleStatus;
    private RelativeLayout mRootRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_owner_edit);
        initStatusBar();
        init();
        dealWithIntent();
        initData();
    }

    private void init() {
        mRootRl = (RelativeLayout) findViewById(R.id.root_view_id);
        mUnauthorizeBtn = (Button) findViewById(R.id.startConnectBtn);
        mTitleTv = (TextView) findViewById(R.id.title_textview_id);
        mPlaceTv = (TextView) findViewById(R.id.auth_owner_edit_place_name_tv);
        mDeviceTv = (TextView) findViewById(R.id.auth_owner_edit_device_name_tv);
        mAuthorizeTo = (TextView) findViewById(R.id.auth_owner_edit_authorize_name_tv);
        mRoleStatus = (TextView) findViewById(R.id.auth_owner_edit_authorize_status_tv);
        mDropDownAnimationManager = new DropDownAnimationManager(mRootRl, mContext);
    }

    private void initData() {
        mUnauthorizeBtn.setVisibility(View.VISIBLE);
        mUnauthorizeBtn.setText(R.string.authorize_revoke);
        mTitleTv.setText(R.string.authorize_auth_detail);
        mPlaceTv.setText(mAuthBaseModel.getmLocationName());
        mDeviceTv.setText(mAuthBaseModel.getModelName());
        mAuthorizeTo.setText(mAuthBaseModel.getmAuthorityToList().get(0).getGrantToUserName());
        if (mAuthBaseModel.getmAuthorityToList().get(0).getStatus() == AuthTo.WAIT) {
            mRoleStatus.setText(getString(mAuthBaseModel.getmAuthorityToList().get(0).parseRole()) + ", " +
                    getString(mAuthBaseModel.getmAuthorityToList().get(0).parseStatus()));
        } else {
            mRoleStatus.setText(getString(mAuthBaseModel.getmAuthorityToList().get(0).parseRole()));
        }
    }

    private void dealWithIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAuthBaseModel = (AuthBaseModel) bundle.get(INTENTPARAMETEROBJECT);
        }
    }

    @Override
    protected void dealSuccessCallBack(ResponseResult responseResult) {
        super.dealSuccessCallBack(responseResult);
        if(responseResult == null) {
            return;
        }
        boolean isSuccess = mAuthorizeUiManager.
                parseRevokeResponse(responseResult, mAuthBaseModel);
        if (isSuccess) {
            startIntentBack(mAuthBaseModel.getmClass(), mAuthBaseModel, ClickType.REVOKE);
        } else {
            mDropDownAnimationManager.showDragDownLayout(getString(R.string.authorize_send_revoke_fail), true);
        }
    }

    public void doClick(View v) {
        super.doClick(v);
        if (R.id.startConnectBtn == v.getId()) {
            if (isNetworkOff()){
                return;
            }
            showRemoveMessage();
        }
    }

    private void showRemoveMessage() {
        showDoubleDialog(getString(R.string.authorize_revoke_remind, mAuthBaseModel.getModelName(), mAuthBaseModel.getmAuthorityToList().get(0).getGrantToUserName()),
                getString(R.string.cancel), null, getString(R.string.authorize_revoke), revokeMessageBoxBtn);
    }

    private MessageBox.MyOnClick revokeMessageBoxBtn = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            mDialog = LoadingProgressDialog.show(mContext, getString(R.string.enroll_loading));
            List<String> phoneNumberList = mAuthorizeUiManager.parseRevokePhoneNumberList(mAuthBaseModel);
            mAuthorizeUiManager.grantAuthToDevice(mAuthBaseModel.getModelId(), mAuthBaseModel.REVOKEASSIGNROLE, phoneNumberList);
        }
    };
}
