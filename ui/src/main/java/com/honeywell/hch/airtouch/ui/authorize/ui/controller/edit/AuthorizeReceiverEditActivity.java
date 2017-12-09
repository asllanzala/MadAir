package com.honeywell.hch.airtouch.ui.authorize.ui.controller.edit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthBaseModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthDeviceModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.RemoveAuthResponse;
import com.honeywell.hch.airtouch.ui.authorize.ui.controller.list.AuthorizeBaseActivity;
import com.honeywell.hch.airtouch.ui.common.ui.view.DropDownAnimationManager;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;

/**
 * Created by Vincent on 1/2/16.
 */
public class AuthorizeReceiverEditActivity extends AuthorizeBaseActivity implements AuthorizeBaseActivity.AuthorizeClick {

    private TextView mTitleTv;
    private Button mUnauthorizeBtn;
    private AuthBaseModel mAuthBaseModel;
    private TextView mFromTv;
    private TextView mPlaceTv;
    private TextView mDeviceTv;
    private TextView mRoleTv;
    private TextView mGroupDeviceNameTv;
    private RelativeLayout mRootRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_receive_edit);
        initStatusBar();
        init();
        dealWithIntent();
        initData();
    }

    private void init() {
        mRootRl = (RelativeLayout) findViewById(R.id.root_view_id);
        mUnauthorizeBtn = (Button) findViewById(R.id.startConnectBtn);
        mTitleTv = (TextView) findViewById(R.id.title_textview_id);
        mFromTv = (TextView) findViewById(R.id.auth_receive_edit_from_name_tv);
        mPlaceTv = (TextView) findViewById(R.id.auth_receive_edit_place_name_tv);
        mDeviceTv = (TextView) findViewById(R.id.auth_receive_edit_device_name_tv);
        mRoleTv = (TextView) findViewById(R.id.auth_receive_edit_role_name_tv);
        mGroupDeviceNameTv = (TextView) findViewById(R.id.auth_owner_receive_device_tv);
        mDropDownAnimationManager = new DropDownAnimationManager(mRootRl, mContext);
    }

    private void initData() {
        mUnauthorizeBtn.setVisibility(View.VISIBLE);
        mUnauthorizeBtn.setText(R.string.authorize_remove);
        mTitleTv.setText(R.string.authorize_auth_detail);
        mFromTv.setText(mAuthBaseModel.getOwnerName());
        mPlaceTv.setText(mAuthBaseModel.getmLocationName());
        mDeviceTv.setText(mAuthBaseModel.getModelName());
        mRoleTv.setText(getString(mAuthBaseModel.parseRole()));
        mGroupDeviceNameTv.setText(getString(mAuthBaseModel.parseGroupDeviceName()));
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
        RemoveAuthResponse removeAuthResponse = (RemoveAuthResponse) responseResult.getResponseData()
                .getSerializable(RemoveAuthResponse.REMOVE_AUTH_DATA);
        if (!String.valueOf(StatusCode.OK).equals(removeAuthResponse.getCode())) {
            mDropDownAnimationManager.showDragDownLayout(getString(R.string.authorize_send_remove_fail), true);
        } else {
            startIntentBack(mAuthBaseModel.getmClass(), mAuthBaseModel, ClickType.REMOVE);
        }
    }

    @Override
    public void doClick(View v) {
        super.doClick(v);
        if (v.getId() == R.id.startConnectBtn) {
            showRemoveMessage();
        }
    }

    private void showRemoveMessage() {
        showDoubleDialog(getString(R.string.authorize_remove_remind, mAuthBaseModel.getModelName()), getString(R.string.cancel), null,
                getString(R.string.authorize_remove), removeMessageBoxBtn);
    }

    private MessageBox.MyOnClick removeMessageBoxBtn = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            mDialog = LoadingProgressDialog.show(mContext, getString(R.string.enroll_loading));
            if (mAuthBaseModel instanceof AuthDeviceModel) {
                mAuthorizeUiManager.removeAuthDevice(mAuthBaseModel.getModelId());
            } else {
                mAuthorizeUiManager.removeAuthGroup(mAuthBaseModel.getModelId());
            }
        }
    };

}
