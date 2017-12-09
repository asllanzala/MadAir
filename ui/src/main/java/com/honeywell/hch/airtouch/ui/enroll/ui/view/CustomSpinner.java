package com.honeywell.hch.airtouch.ui.enroll.ui.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by wuyuan on 2/24/16.
 */
public class CustomSpinner extends Spinner {

    private Activity mActivity;

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setActivity(Activity activity){
        mActivity = activity;
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
//        ((EnrollConnectDeviceBaseActivity)mParentActivity).setSelectItem(position);
    }
}
