package com.honeywell.hch.airtouch.ui.main.ui.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.view.BottomIconViewItem;
import com.honeywell.hch.airtouch.library.util.DensityUtil;

import java.util.List;

/**
 * Created by h127856 on 7/18/16.
 */
public class BottomNavigationView extends RelativeLayout {

    private Context mContext;
    private RelativeLayout mRootView;

    private List<BottomIconViewItem> mBottomIconViewItemList;

    public BottomNavigationView(Context context) {
        super(context);
        mContext = context;
        initView();

    }

    public BottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }


    /**
     * bottomIconViewItemList did not include the first botton
     *
     * @param bottomIconViewItemList
     */
    public void addBtnToNavigationView(List<BottomIconViewItem> bottomIconViewItemList) {
        mRootView.removeAllViews();
        if (bottomIconViewItemList != null && bottomIconViewItemList.size() > 0) {
            mBottomIconViewItemList = bottomIconViewItemList;
            int itemWidth = DensityUtil.getScreenWidth() / bottomIconViewItemList.size();
            for (int i = 0; i < bottomIconViewItemList.size(); i++) {
                bottomIconViewItemList.get(i).setId(getBtnId(i));

                //第一个按钮默认是选中的状态
                int textcolor = i == 0 ? getResources().getColor(R.color.login_new_user_color) : getResources().getColor(R.color.group_edit_text);
                bottomIconViewItemList.get(i).isClickSelectedStatus(i == 0, textcolor);


                LayoutParams relativeLayoutParams = new LayoutParams(itemWidth, ViewGroup.LayoutParams.MATCH_PARENT);
                if (i != 0) {
                    int preId = bottomIconViewItemList.get(i - 1).getId();
                    relativeLayoutParams.addRule(RelativeLayout.RIGHT_OF, preId);
                }

                bottomIconViewItemList.get(i).setLayoutParams(relativeLayoutParams);
                mRootView.addView(bottomIconViewItemList.get(i));
            }
        }
    }


    /**
     * 创建Item
     * @param clickImage
     * @param unclickImageId
     * @param str
     * @param onClickListener
     * @return
     */
    public BottomIconViewItem addBottomViewItem(int clickImage, int unclickImageId, String str, OnClickListener onClickListener) {
        BottomIconViewItem bottomIconViewItem = new BottomIconViewItem(mContext, clickImage, unclickImageId);
        bottomIconViewItem.setTextString(str);
        bottomIconViewItem.setOnClickListener(onClickListener);
        return bottomIconViewItem;
    }


    public void setItemClick(String itemStr) {
        if (mBottomIconViewItemList != null) {
            for (BottomIconViewItem bottomIconViewItem : mBottomIconViewItemList) {
                if (bottomIconViewItem.getTextString().equals(itemStr)) {
                    bottomIconViewItem.isClickSelectedStatus(true, getResources().getColor(R.color.login_new_user_color));
                } else {
                    bottomIconViewItem.isClickSelectedStatus(false, getResources().getColor(R.color.group_edit_text));
                }
            }
        }
    }

    public void setAllDeviceNavigationRedDot(boolean isVisible){
        if (mBottomIconViewItemList != null) {
            for (BottomIconViewItem bottomIconViewItem : mBottomIconViewItemList) {
                if (bottomIconViewItem.getTextString().equals(mContext.getString(R.string.devices_btn_text))) {
                    bottomIconViewItem.setRedDotImageVisible(isVisible, R.drawable.small_alert_on_device_tab);
                }
            }
        }
    }


    public void setMessageNavigationRedDot(boolean isVisible){
        if (mBottomIconViewItemList != null) {
            for (BottomIconViewItem bottomIconViewItem : mBottomIconViewItemList) {
                if (bottomIconViewItem.getTextString().equals(mContext.getString(R.string.messages_btn_text))) {
                    bottomIconViewItem.setRedDotImageVisible(isVisible,R.drawable.small_red_dot);
                }
            }
        }
    }

    public void setMeNavigationRedDot(boolean isVisible){
        if (mBottomIconViewItemList != null) {
            for (BottomIconViewItem bottomIconViewItem : mBottomIconViewItemList) {
                if (bottomIconViewItem.getTextString().equals(mContext.getString(R.string.me_btn_text))) {
                    bottomIconViewItem.setRedDotImageVisible(isVisible,R.drawable.small_red_dot);
                }
            }
        }
    }


    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.dashboard_bottom_view, this);
        mRootView = (RelativeLayout) findViewById(R.id.botton_layout_id);
    }

    private int getBtnId(int position) {
        if (position == 0) {
            return R.id.ds_one_bottom_btn_id;
        } else if (position == 1) {
            return R.id.ds_two_bottom_btn_id;
        } else if (position == 2) {
            return R.id.ds_three_bottom_btn_id;
        } else {
            return R.id.ds_four_bottom_btn_id;
        }
    }


}
