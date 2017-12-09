package com.honeywell.hch.airtouch.ui.enroll.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.enroll.constant.EnrollConstants;

/**
 * Created by h127856 on 16/10/11.
 */
public class IndicatorItemView extends RelativeLayout {

    private Context mContext;

    private RelativeLayout mTextBgLayout;
    private TextView mTextView;

    public IndicatorItemView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public IndicatorItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();

    }

    public IndicatorItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView(){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.enroll_indicator_item, this);

        mTextBgLayout = (RelativeLayout)findViewById(R.id.dot_bg_id);
        mTextView = (TextView)findViewById(R.id.text_id);
    }

    public void intItemView(int stepStatus,int stepNumber){
        int backgroundId = R.drawable.enroll_undo;
        if (stepStatus == EnrollConstants.ENROLL_DONE_STEP){
            backgroundId = R.drawable.enroll_done;
        }else if (stepStatus == EnrollConstants.ENROLL_DOING_STEP){
            backgroundId = R.drawable.enroll_doing;
        }

        mTextBgLayout.setBackgroundResource(backgroundId);
        mTextView.setText(String.valueOf(stepNumber));
    }
}
