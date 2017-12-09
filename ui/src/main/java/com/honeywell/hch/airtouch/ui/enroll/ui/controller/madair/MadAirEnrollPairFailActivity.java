package com.honeywell.hch.airtouch.ui.enroll.ui.controller.madair;

import android.os.Bundle;
import android.view.View;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.manager.CloseActivityUtil;
import com.honeywell.hch.airtouch.ui.enroll.constant.EnrollConstants;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;

/**
 * Created by Jin on 11/23/16.
 */
public class MadAirEnrollPairFailActivity extends EnrollBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enroll_mad_air_pair_fail);
        initStatusBar();
        initView();
        initDragDownManager(R.id.root_view_id);
    }

    private void initView() {
        super.initTitleView(true, getString(R.string.mad_air_enroll_pair_fail_title), EnrollConstants.TOTAL_THREE_STEP,
                EnrollConstants.STEP_ONE,  getString(R.string.mad_air_enroll_pair_fail_desc), false);

    }

    public void doClick(View v) {
        if (v.getId() == R.id.mad_air_exit_btn) {
            CloseActivityUtil.exitEnrollClient(mContext);
            backIntent();
        } else if (v.getId() == R.id.mad_air_retry_btn) {
            backIntent();
        }
    }

}
