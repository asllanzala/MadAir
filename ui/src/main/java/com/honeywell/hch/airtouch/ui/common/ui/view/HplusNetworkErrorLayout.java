package com.honeywell.hch.airtouch.ui.common.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;

/**
 * Created by h127856 on 7/18/16.
 */
public class HplusNetworkErrorLayout extends RelativeLayout {

    public static final int NETWORK_OFF = 0;
    public static final int CONNECT_SERVER_ERROR = 1;

    private Context mContext;
    private TextView mContextView;
    private TextView mLastUpdateTimeView;

    public HplusNetworkErrorLayout(Context context) {
        super(context);
        mContext = context;
        initView();

    }

    public HplusNetworkErrorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }




    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.network_layout, this);

        mContextView = (TextView)findViewById(R.id.network_content_tv);
        mLastUpdateTimeView = (TextView)findViewById(R.id.network_time_tv);
    }

    public void setErrorMsg(int errorType){
        if (errorType == NETWORK_OFF){
            mContextView.setText(mContext.getString(R.string.network_off_msg));
        }else if (errorType == CONNECT_SERVER_ERROR){
            mContextView.setText(mContext.getString(R.string.network_error_msg));
        }
        invalidate();
    }

//    public void setLastUpdateTime(){
//        String lastTime = UserCacheDataManager.getInstance().getLastUpdateTimeOfCacheDataStr();
//        if (!StringUtil.isEmpty(lastTime)){
//            mLastUpdateTimeView.setVisibility(View.VISIBLE);
//            mLastUpdateTimeView.setText(lastTime);
//        }else{
//            mLastUpdateTimeView.setVisibility(View.GONE);
//        }
//    }


}
