package com.honeywell.hch.airtouch.ui.notification.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.http.model.notification.PushMessageModel;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseActivity;
import com.honeywell.hch.airtouch.ui.control.ui.device.controller.DeviceControlActivity;

import java.io.Serializable;

/**
 * Created by Vincent on 26/4/16.
 */
public class LeakDialogActivity extends BaseActivity {

    private Button rightBtn;
    private Button leftBtn;
    private TextView titleTv;
    private TextView contentTv;
    private PushMessageModel mPushMessageModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_two_button);
//        initStatusBar();
        init();
        dealWithIntent();
        initListener();
        initData();
        initDragDownManager(R.id.root_view_id);
    }

    private void init() {
        titleTv = (TextView) findViewById(R.id.dialog_title_text);
        titleTv.setVisibility(View.GONE);
        contentTv = (TextView) findViewById(R.id.dialog_content_text);
        leftBtn = (Button) findViewById(R.id.dialog_left_button);
        rightBtn = (Button) findViewById(R.id.dialog_right_button);
    }

    private void initData() {
        leftBtn.setText(getString(R.string.see_detail));
        rightBtn.setText(getString(R.string.get_it));
        if (mPushMessageModel != null && mPushMessageModel.getmBaiduPushAlert() != null
                && !StringUtil.isEmpty(mPushMessageModel.getmBaiduPushAlert().getmAlert())) {
            contentTv.setText(mPushMessageModel.getmBaiduPushAlert().getmAlert());
        } else {
            contentTv.setText(getString(R.string.water_unknow_alarm));
        }
    }

    private void initListener() {
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent();
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void dealWithIntent() {
        mPushMessageModel = (PushMessageModel) getIntent().getSerializableExtra(PushMessageModel.PUSHPARAMETER);
    }

    private void startIntent() {
        if (mPushMessageModel != null) {
            Intent intent = new Intent(LeakDialogActivity.this, DeviceControlActivity.class);
            intent.putExtra(PushMessageModel.PUSHPARAMETER, (Serializable) mPushMessageModel);
            startActivity(intent);
        }
        finish();
    }

//    private void callPhone() {
//        Intent intent = null;
//        if (AppConfig.getInstance().isIndiaAccount()) {
//            intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + HPlusConstants.INDIA_CONTACT_PHONE_NUMBER));
//        } else {
//            intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + HPlusConstants.CONTACT_PHONE_NUMBER));
//        }
//        startActivity(intent);
//        finish();
//    }
}
