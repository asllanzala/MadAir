package com.honeywell.hch.airtouch.ui.main.ui.dashboard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.ui.common.ui.view.BottomView;
import com.honeywell.hch.airtouch.ui.common.ui.view.CustomFontTextView;

/**
 * Created by h127856 on 6/2/16.
 */
public class HomeItemTextItemView extends RelativeLayout implements View.OnTouchListener  {

    private static final String DEFAULT_VALUE = HPlusConstants.DATA_LOADING_STATUS;

    private Context mContext;

    private ImageView mImageView;

    private TextView mNameTextView;

    private CustomFontTextView mValueTextView;
//
//    private TextView mCleanNowTextView;

    private TextView mDetailTextView;

    private int mResourceId;

    public HomeItemTextItemView(Context context) {
        super(context);
        mContext = context;
        initView();

    }

    public HomeItemTextItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public HomeItemTextItemView(Context context,int imageSrc,String nameStr,boolean isValueGone) {
        super(context);
        mContext = context;
        initView();
        mResourceId = imageSrc;
        mImageView.setImageResource(imageSrc);
        mNameTextView.setText(nameStr);

        int showStatus = isValueGone ? View.GONE : View.VISIBLE;
        mValueTextView.setVisibility(showStatus);
        mValueTextView.setCustomText(DEFAULT_VALUE);
    }

    public HomeItemTextItemView(Context context,String nameStr) {
        super(context);
        mContext = context;
        initView();
        mImageView.setVisibility(View.GONE);
        mNameTextView.setText(nameStr);

        mValueTextView.setVisibility(View.GONE);
        mDetailTextView.setVisibility(View.GONE);
    }

    private void initView(){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.home_image_text_view_item, this);

        mImageView = (ImageView)findViewById(R.id.image_flag_icon);
         mNameTextView = (TextView)findViewById(R.id.name_textview);
        mValueTextView = (CustomFontTextView)findViewById(R.id.value_textview);
//        mCleanNowTextView = (TextView)findViewById(R.id.clean_btn_id);
        mDetailTextView = (TextView)findViewById(R.id.detail_btn_id) ;


        this.setOnTouchListener(this);
    }


    public void setTextColor(int color){
         mNameTextView.setTextColor(color);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.isClickable()){
            if (v.getId() != R.id.first_btn_id){
                // TODO Auto-generated method stub
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    setImageAndTextAlpha(BottomView.ALPHA_70);
                }else if(event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL){
                    setImageAndTextAlpha(BottomView.ALPHA_100);
                }
            }
        }
        return false;
    }

    public String getNameTextString(){
        return  mNameTextView.getText().toString();
    }

    public void setNameTextString(String str){
         mNameTextView.setText(str);
    }

    public String getValueTextString(){
        return  mValueTextView.getText().toString();
    }

    public void setValueTextString(String str){
        mValueTextView.setCustomText(str);
    }

    public void setValueTextColor(int color){
        mValueTextView.setTextColor(color);
    }

    public void setImageAndTextAlpha(float alpha){
        mImageView.setAlpha(alpha);
        mNameTextView.setAlpha(alpha);
        mValueTextView.setAlpha(alpha);
    }

    /**
     * 处理button的显示逻辑，是显示“Detail”还是 “Clean Now”
     * @param isVisible
     * @param isVisible
     */
    public void setDealBadTextViewVisible(boolean isVisible){
        int showStatus = false ? View.VISIBLE : View.GONE;
        mDetailTextView.setVisibility(showStatus);
        mDetailTextView.setText(mContext.getString(R.string.clean_now_str));

    }


    public int getResourceId() {
        return mResourceId;
    }


    public void setImageSrc(int resourcId,int textColor){
        mImageView.setImageResource(resourcId);
        mNameTextView.setTextColor(textColor);
    }
}
