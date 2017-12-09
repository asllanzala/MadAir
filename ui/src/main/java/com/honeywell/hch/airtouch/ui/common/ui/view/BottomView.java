package com.honeywell.hch.airtouch.ui.common.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.util.DensityUtil;

import java.util.List;

/**
 * Created by h127856 on 6/2/16.
 */
public class BottomView extends LinearLayout implements View.OnTouchListener {

    private Context mContext;

    private View mRootView;

    private RelativeLayout mFirstSelectedBtnLayout;

    private BottomIconViewItem mFirstBottomIconViewItem;

    private RelativeLayout mOtherBtnLayout;

    private static final int IPHONE_6_WIDTH = 750;

    private static final int FIRST_BTN_WIDTH = 160;

    private int mOtherLayoutTotalWidth = 0;

    public static final float ALPHA_70 = 0.7f;
    public static final float ALPHA_100 = 1f;

    public BottomView(Context context) {
        super(context);
        mContext = context;
        initView();

    }

    public BottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = inflater.inflate(R.layout.device_bottom_view, this);
        mFirstSelectedBtnLayout = (RelativeLayout) findViewById(R.id.first_selectall_layout_id);
        mFirstSelectedBtnLayout.setOnTouchListener(this);

        int firstWidth = (FIRST_BTN_WIDTH * DensityUtil.getScreenWidth()) / IPHONE_6_WIDTH;
        mFirstSelectedBtnLayout.setLayoutParams(new RelativeLayout.LayoutParams(firstWidth, RelativeLayout.LayoutParams.MATCH_PARENT));

        mFirstBottomIconViewItem = (BottomIconViewItem)findViewById(R.id.first_btn_id);

        mOtherLayoutTotalWidth = DensityUtil.getScreenWidth() - firstWidth;
        mOtherBtnLayout = (RelativeLayout) findViewById(R.id.other_btn_layout);
    }

    /**
     * bottomIconViewItemList did not include the first botton
     *
     * @param bottomIconViewItemList
     */
    public void addBtnToThisView(List<BottomIconViewItem> bottomIconViewItemList) {
        mOtherBtnLayout.removeAllViews();
        if (bottomIconViewItemList != null && bottomIconViewItemList.size() > 0) {

            int itemWidth = mOtherLayoutTotalWidth / bottomIconViewItemList.size();
            for (int i = 0; i < bottomIconViewItemList.size(); i++) {
                bottomIconViewItemList.get(i).setId(getBtnId(i));
                RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.MATCH_PARENT);
                if (i != 0) {
                    int preId = bottomIconViewItemList.get(i - 1).getId();
                    relativeLayoutParams.addRule(RelativeLayout.RIGHT_OF, preId);
                }

                bottomIconViewItemList.get(i).setLayoutParams(relativeLayoutParams);
                mOtherBtnLayout.addView(bottomIconViewItemList.get(i));
            }
        }
    }

    public  void setFirstSelectBtnClickListen(OnClickListener onClickListener){
        mFirstSelectedBtnLayout.setOnClickListener(onClickListener);
    }


    private int getBtnId(int position) {
        if (position == 0) {
            return R.id.second_bottom_btn_id;
        } else if (position == 1) {
            return R.id.three_bottom_btn_id;
        } else {
            return R.id.four_bottom_btn_id;
        }
    }

    public boolean isAllSelectStrInFirstBtn(){
        if(mFirstBottomIconViewItem.getTextString().equalsIgnoreCase(mContext.getString(R.string.all_device_bottom_selectall))){
            return true;
        }
        return false;
    }

    public void setFirstBtnSelectStatusText(String str){
        mFirstBottomIconViewItem.setTextString(str);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // TODO Auto-generated method stub
        if (v.isClickable()){
            if (v.getId() == R.id.first_selectall_layout_id){
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    mFirstBottomIconViewItem.setImageAndTextAlpha(ALPHA_70);
                }else if(event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL){
                    mFirstBottomIconViewItem.setImageAndTextAlpha(ALPHA_100);
                }
            }
        }

        return false;
    }


    /**
     * 设置底部的所有按钮是否可点击
     * @param clickable
     */
    public void setAllBtnClickable(boolean clickable){
        float alpha =  clickable ? ALPHA_100 : ALPHA_70;
        mFirstSelectedBtnLayout.setClickable(clickable);
        mFirstBottomIconViewItem.setImageAndTextAlpha(alpha);
        if (mOtherBtnLayout != null){
           for (int i = 0; i < mOtherBtnLayout.getChildCount();i++){
               if(mOtherBtnLayout.getChildAt(i) != null){
                   mOtherBtnLayout.getChildAt(i).setClickable(clickable);
                   ((BottomIconViewItem)mOtherBtnLayout.getChildAt(i)).setImageAndTextAlpha(alpha);
               }
           }
        }
    }

    /**
     * 设置底部的除了select按钮之外的其他所有按钮是否可点击
     * @param clickable
     */
    public void setAllBtnExceptSelectClickable(boolean clickable){
        float alpha =  clickable ? ALPHA_100 : ALPHA_70;
        if (mOtherBtnLayout != null){
            for (int i = 0; i < mOtherBtnLayout.getChildCount();i++){
                if(mOtherBtnLayout.getChildAt(i) != null){
                    mOtherBtnLayout.getChildAt(i).setClickable(clickable);
                    ((BottomIconViewItem)mOtherBtnLayout.getChildAt(i)).setImageAndTextAlpha(alpha);
                }
            }
        }
    }
}
