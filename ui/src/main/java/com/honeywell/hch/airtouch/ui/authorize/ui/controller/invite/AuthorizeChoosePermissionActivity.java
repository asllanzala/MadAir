package com.honeywell.hch.airtouch.ui.authorize.ui.controller.invite;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthBaseModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthDeviceModel;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.authorize.ui.controller.list.AuthorizeBaseActivity;

/**
 * Created by Vincent on 1/2/16.
 */
public class AuthorizeChoosePermissionActivity extends AuthorizeBaseActivity implements AuthorizeBaseActivity.AuthorizeClick {

    private final String TAG = "AuthorizeChoosePermission";
    private Button mChooseBtn;
    private TextView mTitleTv;
    private AuthBaseModel authBaseModel;
    private RadioGroup mRadioGroup;
    private RadioButton mControlRb;
    private TextView mControlTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_choose_permission);
        dealWithIntent();
        initStatusBar();
        init();
        initData();
        initRadioGroup();
    }

    private void init() {
        mChooseBtn = (Button) findViewById(R.id.startConnectBtn);
        mRadioGroup = (RadioGroup) findViewById(R.id.auth_permission_rg);
        mTitleTv = (TextView) findViewById(R.id.title_textview_id);
        mControlRb = (RadioButton) findViewById(R.id.auth_permission_control_rb);
        mControlTv = (TextView) findViewById(R.id.auth_permission_control_tv);
    }

    private void initData() {
        mChooseBtn.setVisibility(View.VISIBLE);
        mChooseBtn.setText(R.string.enroll_next);
        mTitleTv.setText(R.string.authorize_select_item);

    }

    private void initRadioGroup() {
        if (DeviceType.isAirTouch100GSeries(((AuthDeviceModel) authBaseModel).getmDeviceType())) {
            mControlRb.setVisibility(View.GONE);
            mControlTv.setVisibility(View.GONE);
            mRadioGroup.check(R.id.auth_permission_read_rb);
        }
    }

    private void dealWithIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            authBaseModel = (AuthBaseModel) bundle.get(INTENTPARAMETEROBJECT);
        }
    }

    public void doClick(View v) {
        super.doClick(v);
        if (v.getId() == R.id.startConnectBtn) {
            getTheSelectedRole();
        }
    }

    private void getTheSelectedRole() {
        mAuthorizeUiManager.encloseChoosePermission(authBaseModel, mRadioGroup);
        startIntent(AuthorizeSendInvitationActivity.class, authBaseModel, ClickType.AUTHROIZE);

    }
}
