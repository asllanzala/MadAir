package com.honeywell.hch.airtouch.ui.enroll.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.view.TypeTextView;

/**
 * Created by Vincent on 10/10/16.
 */
public class EnrollLoadingButton extends RelativeLayout {
    private Context mContext;
    private TextView mLeftTv;
    private TypeTextView mRightTv;
    private RelativeLayout mRootRl;
    private String mButtonStr;
    private String mLoadingStr;

    public EnrollLoadingButton(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public EnrollLoadingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.loading_button, this);
        mLeftTv = (TextView) findViewById(R.id.loading_left_tv);
        mRightTv = (TypeTextView) findViewById(R.id.loading_right_tv);
        mRootRl = (RelativeLayout) findViewById(R.id.loading_root_rl);
    }

    public void init(String leftStr, String rightstr) {
        mLeftTv.setText(leftStr);
        mRightTv.setText(rightstr);
    }

    public void loading() {
        mRightTv.setVisibility(VISIBLE);
        mLeftTv.setText(mLoadingStr);
        mRightTv.startLoop();
    }

    public void stoploading() {
        mRightTv.setVisibility(GONE);
        mRightTv.stop();
        mLeftTv.setText(mButtonStr);
    }


    public void initLoadingText(String buttonStr, String loadingStr) {
        mButtonStr = buttonStr;
        mLoadingStr = loadingStr;
        mLeftTv.setText(buttonStr);
    }

    public RelativeLayout getmRootRl() {
        return mRootRl;
    }

    public void setButtonStatus(boolean isNormal, boolean isLoading) {
        if (isLoading) {
            mRootRl.setBackgroundResource(R.drawable.enroll_big_btn_disable);
            mRootRl.setClickable(false);
            mRootRl.setEnabled(false);
            loading();
        } else {
            if (isNormal) {
                mRootRl.setBackgroundResource(R.drawable.enroll_big_btn);
                stoploading();
                mRootRl.setClickable(true);
                mRootRl.setEnabled(true);
            } else {
                mRootRl.setBackgroundResource(R.drawable.enroll_big_btn_disable);
                stoploading();
                mRootRl.setClickable(false);
                mRootRl.setEnabled(false);
            }
        }
    }

}
