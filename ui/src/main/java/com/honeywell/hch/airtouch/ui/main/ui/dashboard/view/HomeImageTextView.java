package com.honeywell.hch.airtouch.ui.main.ui.dashboard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.util.DensityUtil;

import java.util.List;

/**
 * Created by h127856 on 7/18/16.
 */
public class HomeImageTextView extends RelativeLayout {

    //左右边距的dip数
    private static final int LAYOUT_LEFT_RIGHT_MARGIN = 40;
    private Context mContext;
    private LinearLayout mRootView;

    public HomeImageTextView(Context context) {
        super(context);
        mContext = context;
        initView();

    }

    public HomeImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }


    /**
     * bottomIconViewItemList did not include the first botton
     *
     * @param bottomIconViewItemList
     */
    public void addHomeImageTextViewLayout(List<HomeItemTextItemView> bottomIconViewItemList) {
        mRootView.removeAllViews();
        if (bottomIconViewItemList != null && bottomIconViewItemList.size() > 0) {
            int itemWidth = (DensityUtil.getScreenWidth() - DensityUtil.dip2px(LAYOUT_LEFT_RIGHT_MARGIN)) / bottomIconViewItemList.size();
            for (int i = 0; i < bottomIconViewItemList.size(); i++) {

                LinearLayout.LayoutParams relativeLayoutParams = new LinearLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                relativeLayoutParams.gravity= Gravity.CENTER_VERTICAL;
                bottomIconViewItemList.get(i).setLayoutParams(relativeLayoutParams);
                mRootView.addView(bottomIconViewItemList.get(i));
            }
        }
    }


    /**
     *
     * @param imageSrc
     * @param nameStr
     * @param isValueGone
     * @param onClickListener
     * @return
     */
    public HomeItemTextItemView newHomeImageTextViewItem(int imageSrc,String nameStr,boolean isValueGone, OnClickListener onClickListener) {
        HomeItemTextItemView homeItemTextItemView = new HomeItemTextItemView(mContext, imageSrc, nameStr,isValueGone);
        homeItemTextItemView.setOnClickListener(onClickListener);
        return homeItemTextItemView;
    }


    public HomeItemTextItemView newHomeImageTextViewItemOnlyText(String nameStr) {
        HomeItemTextItemView homeItemTextItemView = new HomeItemTextItemView(mContext, nameStr);
        return homeItemTextItemView;
    }



    /**
     * 获取第index个子view
     * @param index
     * @return
     */
    public HomeItemTextItemView getHomeItemTextView(int index){
        return (HomeItemTextItemView)mRootView.getChildAt(index);
    }

    public int getTopViewChildViewCount(){
        return mRootView.getChildCount();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.image_text_view_layout, this);
        mRootView = (LinearLayout) findViewById(R.id.botton_layout_id);
    }





}
