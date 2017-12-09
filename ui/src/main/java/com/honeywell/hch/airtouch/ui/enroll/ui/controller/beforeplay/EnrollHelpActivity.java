package com.honeywell.hch.airtouch.ui.enroll.ui.controller.beforeplay;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.ap.ApEnrollChoiceActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;

/**
 * Created by Vincent on 17/10/16.
 */
public class EnrollHelpActivity extends EnrollBaseActivity {

    private TextView mTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enroll_before_play_help);
        initStatusBar();
        initView();
        initDragDownManager(R.id.root_view_id);
    }

    private void initView() {
        mTitleTv = (TextView) findViewById(R.id.title_textview_id);
        mTitleTv.setText(R.string.enroll_before_play_title);
    }

    public void doClick(View v) {
        if (v.getId() == R.id.ap_mode_entrance) {
            EnrollScanEntity.getEntityInstance().setNoQRcode(true);
            startIntent(ApEnrollChoiceActivity.class);
        } else if (v.getId() == R.id.enroll_back_layout) {
            backIntent();
        }
    }
}
