package com.honeywell.hch.airtouch.ui.enroll.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.enroll.constant.EnrollConstants;

/**
 * Created by h127856 on 16/10/11.
 */
public class EnrollIndicatorView extends RelativeLayout{



    private Context mContext;

    /**
     *enroll 总的步数
     */
    private int mTotalStep;
    /**
     * 当前enroll到了第几步
     */
    private int mCurrentStep;

    private LinearLayout mTopView;

    public EnrollIndicatorView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public EnrollIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();

    }

    public EnrollIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    public void setEnrollStepParams(int totalStep, int currentStep){
        mTotalStep = totalStep;
        mCurrentStep = currentStep;
        setVisibility(View.VISIBLE);
        initIndicatorView();
    }

    /**
     * 所有的流程都成功结束时候，调用这个方法
     * @param totalStep
     */
    public void setEnrollStepAllSuccess(int totalStep){
        setVisibility(View.VISIBLE);
        for (int i = 0 ; i < totalStep; i++){
            addDotView(EnrollConstants.ENROLL_DONE_STEP,i + 1);
            if (i < totalStep - 1){
                addConnectedDot();
            }
        }
    }

    private void initIndicatorView(){
        for (int i = 0 ; i < mTotalStep; i++){
            if ( i < mCurrentStep - 1 && i <= mTotalStep - 1){
                addDotView(EnrollConstants.ENROLL_DONE_STEP,i + 1);

            }else if (i == mCurrentStep - 1 && i <= mTotalStep - 1){
                addDotView(EnrollConstants.ENROLL_DOING_STEP,i + 1);
            }else if (i > mCurrentStep - 1 && i <= mTotalStep - 1){
                addDotView(EnrollConstants.ENROLL_UNDO_STEP,i + 1);
            }

            if (i < mTotalStep - 1){
                addConnectedDot();
            }
        }
    }

    private void initView(){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.enroll_indicator, this);
        mTopView = (LinearLayout)findViewById(R.id.enroll_indicator_parent_id);
    }

    private void addDotView(int stepStatus,int stepNumber){
        IndicatorItemView indicatorItemView = new IndicatorItemView(mContext);
        indicatorItemView.intItemView(stepStatus,stepNumber);
        mTopView.addView(indicatorItemView);
    }

    private void addConnectedDot(){
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(R.drawable.connect_dot);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        layoutParams.leftMargin = DensityUtil.dip2px(10);
        layoutParams.rightMargin = DensityUtil.dip2px(10);
        imageView.setLayoutParams(layoutParams);
        mTopView.addView(imageView);
    }
}
