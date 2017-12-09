package com.honeywell.hch.airtouch.ui.common.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;

/**
 * Created by h127856 on 6/2/16.
 */
public class BottomIconViewItem extends RelativeLayout implements View.OnTouchListener  {

    private Context mContext;

    private ImageView mImageView;

    private TextView mTextView;

    private int mSelectedImageSrcId;

    private int mUnSelectedImageSrcId;

    private ImageView mRedDotImageView;

    public BottomIconViewItem(Context context) {
        super(context);
        mContext = context;
        initView();

    }

    public BottomIconViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public BottomIconViewItem(Context context,int selectedId,int unSelectedId) {
        super(context);
        mContext = context;
        initView();
        mSelectedImageSrcId = selectedId;
        mUnSelectedImageSrcId = unSelectedId;
    }

    private void initView(){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.device_bottom_icon_view_item, this);

        mImageView = (ImageView)findViewById(R.id.btn_icon);
        mTextView = (TextView)findViewById(R.id.btn_txt_id);

        mImageView.setImageResource(R.drawable.bottom_select_all);
        mTextView.setText(mContext.getString(R.string.all_device_bottom_selectall));
        this.setOnTouchListener(this);

        mRedDotImageView = (ImageView)findViewById(R.id.error_flag);

    }

    public void setElementOfTheView(int imageId,int strId){
        mImageView.setImageResource(imageId);
        mTextView.setText(mContext.getString(strId));
    }

    public void setRedDotImageVisible(boolean isVisible,int resourceId){
        int visibleStatus = isVisible ? View.VISIBLE : View.GONE;
        mRedDotImageView.setVisibility(visibleStatus);
        mRedDotImageView.setImageResource(resourceId);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.isClickable()){
            if (v.getId() != R.id.first_btn_id){
                // TODO Auto-generated method stub
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    setImageAndTextAlpha(BottomView.ALPHA_70);
                }else if(event.getAction()==MotionEvent.ACTION_UP  || event.getAction()==MotionEvent.ACTION_CANCEL){
                    setImageAndTextAlpha(BottomView.ALPHA_100);
                }
            }
        }
        return false;
    }

    public String getTextString(){
        return mTextView.getText().toString();
    }

    public void setTextString(String str){
        mTextView.setText(str);
    }


    public void setImageAndTextAlpha(float alpha){
        mImageView.setAlpha(alpha);
        mTextView.setAlpha(alpha);
    }


    public void isClickSelectedStatus(boolean isSelected,int textColor){
        int srcId = isSelected ? mSelectedImageSrcId : mUnSelectedImageSrcId;
        mImageView.setImageResource(srcId);
        mTextView.setTextColor(textColor);
    }
}
