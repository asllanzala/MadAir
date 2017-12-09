package com.honeywell.hch.airtouch.ui.enroll.ui.controller.ap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.enroll.interfacefile.IEnrollScanView;
import com.honeywell.hch.airtouch.ui.enroll.manager.presenter.EnrollScanPresenter;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollChoiceModel;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;
import com.honeywell.hch.airtouch.ui.enroll.ui.adapter.MyGridAdapter;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.beforeplay.EnrollDeviceInfoActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 07/06/16.
 */
public class ApEnrollChoiceActivity extends EnrollBaseActivity implements IEnrollScanView {
    private Button mConfirmButton;

    private GridView mGridview;
    private MyGridAdapter myGridAdapter;
    private List<EnrollChoiceModel> mEnrollChoiceModelList = new ArrayList<>();
    private TextView mTitleTextView;
    private TextView mFromApTextView;
    private EnrollScanPresenter mEnrollScanPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollchoice);
        initStatusBar();
        initView();
        initData();
        disableConnectButton();
        initDragDownManager(R.id.root_view_id);
        initItemClickListener();
    }

    private void initView() {
        mTitleTextView = (TextView) findViewById(R.id.title_textview_id);
        mFromApTextView = (TextView) findViewById(R.id.input_tip_id);
        mConfirmButton = (Button) findViewById(R.id.confirmBtn);
        mGridview = (GridView) findViewById(R.id.enroll_choose_grid_v);
        mEnrollScanPresenter = new EnrollScanPresenter(mContext, this);
        mEnrollScanPresenter.setmEnrollScanEntity();
    }

    private void initData() {
        mTitleTextView.setText(R.string.enroll_choice_title);
        mFromApTextView.setText(getString(R.string.enroll_choice_title_content));
        mFromApTextView.setVisibility(View.VISIBLE);
        mSmartLinkUiManager.parseEnrollChoiceModle(mEnrollChoiceModelList);
        myGridAdapter = new MyGridAdapter(mContext, mEnrollChoiceModelList);
        mGridview.setAdapter(myGridAdapter);
    }

    private void initItemClickListener() {
        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EnrollChoiceModel enrollChoiceModel = (EnrollChoiceModel) parent.getItemAtPosition(position);
                mSmartLinkUiManager.enrollChoiceImageClick(enrollChoiceModel, mEnrollChoiceModelList);
                myGridAdapter.notifyDataSetChanged();
                enableConnectButton();
            }
        });
    }

    private void disableConnectButton() {
        mConfirmButton.setClickable(false);
        mConfirmButton.setEnabled(false);
    }

    private void enableConnectButton() {
        mConfirmButton.setClickable(true);
        mConfirmButton.setEnabled(true);
    }

    public void doClick(View v) {
        if (v.getId() == R.id.enroll_back_layout) {
            backIntent();
        } else if (v.getId() == R.id.confirmBtn) {
            if (!AppConfig.shareInstance().isIndiaAccount() && (
                    DeviceType.isAirTouchS(EnrollScanEntity.getEntityInstance().getmDeviceType()) ||
                            DeviceType.isAirTouchSUpdate(EnrollScanEntity.getEntityInstance().getmDeviceType()))) {
                if (EnrollScanEntity.getEntityInstance().getmEnrollDeviceTypeModelList() == null) {
                    mAlertDialog = MessageBox.createSimpleDialog((Activity) mContext, "", getString(R.string.enroll_device_model_error), getString(R.string.ok), downLoadTypeAgain);
                } else {
                    startIntent(ApEnrollModelChoiceActivity.class);
                }
            } else {
                startIntent(EnrollDeviceInfoActivity.class);
            }

        }
    }

    protected MessageBox.MyOnClick downLoadTypeAgain = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            disMissDialog();
            downLoadEdviceType();
        }
    };

    private void downLoadEdviceType() {
        mEnrollScanPresenter.downLoadEdviceType();
    }

    @Override
    public void unKnowDevice() {

    }

    @Override
    public void disMissDialog() {

    }

    @Override
    public void dealSuccessCallBack() {

    }

    @Override
    public void deviceModelErrorl() {

    }

    @Override
    public void unSupportSmartLinkDevice() {

    }

    @Override
    public void updateWifi() {

    }

    @Override
    public void registedAuthDevice() {

    }

    @Override
    public void showNoNetWorkDialog() {

    }

    @Override
    public void startIntent(Class mClass) {
        super.startIntent(mClass);
    }

    @Override
    public void updateVersion() {

    }

    @Override
    public void invalidDevice() {

    }

    @Override
    public void madAirDeviceAlreadyEnrolled() {

    }
}
