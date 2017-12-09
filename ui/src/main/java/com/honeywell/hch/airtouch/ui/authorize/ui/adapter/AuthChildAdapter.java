package com.honeywell.hch.airtouch.ui.authorize.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.util.ViewHolderUtil;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthBaseModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthDeviceModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.AuthHomeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 7/7/16.
 */
public class AuthChildAdapter extends BaseAdapter {
    private final String TAG = "AuthChildAdapter";
    private static final int TYPE_CATEGORY_ITEM = 0;
    private static final int TYPE_ITEM = 1;

    //    private ArrayList<Category> mListData;
    private LayoutInflater mInflater;
    private Context mContext;
    private final int EMPTY = 0;

    private List<AuthHomeModel> mChilds;


    public AuthChildAdapter(Context context, List<AuthHomeModel> childs) {
        mContext = context;
        mChilds = childs;
        mInflater = LayoutInflater.from(context);
    }

    public void changeData(List<AuthHomeModel> childs) {
        this.setCategoryList(childs);
        this.notifyDataSetChanged();
    }

    private void setCategoryList(List<AuthHomeModel> childs) {
        if (mChilds != null) {
            this.mChilds = childs;
        } else {
            this.mChilds = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        int count = EMPTY;

        if (null != mChilds) {
            //  所有分类中item的总和是ListVIew  Item的总个数
            for (AuthHomeModel authHomeModel : mChilds) {
                count += authHomeModel.getItemCount();
            }
        }
//        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "count: " + count);
        return count;
    }

    @Override
    public Object getItem(int position) {
//        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "position1111: " + position);
        // 异常情况处理
        if (null == mChilds || position < EMPTY || position > getCount()) {
            return null;
        }

        // 同一分类内，第一个元素的索引值
        int categroyFirstIndex = EMPTY;

        for (AuthHomeModel authHomeModel : mChilds) {
            int size = authHomeModel.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categroyFirstIndex;
            // item在当前分类内
            if (categoryIndex < size) {
                return authHomeModel.getItem(categoryIndex);
            }
            // 索引移动到当前分类结尾，即下一个分类第一个元素索引
            categroyFirstIndex += size;
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        // 异常情况处理
        if (null == mChilds || position < EMPTY || position > getCount()) {
            return TYPE_ITEM;
        }

        int categroyFirstIndex = EMPTY;
        for (AuthHomeModel authHomeModel : mChilds) {
            int size = authHomeModel.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categroyFirstIndex;
            if (categoryIndex == EMPTY) {
                return TYPE_CATEGORY_ITEM;
            }
            categroyFirstIndex += size;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int itemViewType = getItemViewType(position);
        Object object = getItem(position);
        switch (itemViewType) {
            case TYPE_CATEGORY_ITEM:
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.auth_children_title_item, null);
                }
                TextView childGroupTV = ViewHolderUtil.get(convertView, R.id.auth_children_title_tv);
                childGroupTV.setText((String) getItem(position));

                break;

            case TYPE_ITEM:
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.auth_children_item, null);
                }
                TextView childDeviceNameTv = ViewHolderUtil.get(convertView, R.id.auth_children_device_name_tv);
                TextView childStatusTv = ViewHolderUtil.get(convertView, R.id.auth_children_device_status_tv);
                TextView childClickNameTv = ViewHolderUtil.get(convertView, R.id.auth_children_click_tv);
                ImageView childGroupDeviceIv = ViewHolderUtil.get(convertView, R.id.auth_children_icon_iv);
                ImageView childStatusIv = ViewHolderUtil.get(convertView, R.id.auth_children_device_status_iv);
                AuthBaseModel authBaseModel = (AuthBaseModel) object;
                childStatusTv.setVisibility(View.GONE);
                childStatusIv.setVisibility(View.GONE);
                if (authBaseModel.ismIsLocationOwner()) {
                    if (authBaseModel.getmAuthorityToList() == null) {
                        childClickNameTv.setText(mContext.getString(authBaseModel.getAuthSendAction()));
                    } else {
                        if (authBaseModel.canShowDeviceStatus()) {
                            childStatusTv.setVisibility(View.VISIBLE);
                            childStatusTv.setText(authBaseModel.getmAuthorityToList().get(0).parseStatus());
                            childStatusIv.setVisibility(View.VISIBLE);
                            childStatusIv.setImageResource(authBaseModel.getmAuthorityToList().get(0).parseStatusImage());
                        }
                        childClickNameTv.setText(mContext.getString(authBaseModel.getAuthRevokeAction()));
                    }
                } else {
                    childStatusTv.setVisibility(View.VISIBLE);
                    childStatusTv.setText(authBaseModel.parseRole());
                    childStatusIv.setVisibility(View.VISIBLE);
                    childStatusIv.setImageResource(authBaseModel.getRemoveIcon());
                    childClickNameTv.setText(mContext.getString(authBaseModel.getAuthRemoveAction()));
                }
                childGroupDeviceIv.setBackgroundResource(authBaseModel.getModelIcon());
                childDeviceNameTv.setText(authBaseModel.getModelName());
                //set unknow device text color
                if (authBaseModel instanceof AuthDeviceModel && !DeviceType.isHplusSeries(((AuthDeviceModel) authBaseModel).getmDeviceType())) {
                    childDeviceNameTv.setTextColor(mContext.getResources().getColor(R.color.ds_clean_now));
                }
                break;
        }

        return convertView;
    }


    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != TYPE_CATEGORY_ITEM;
    }

}
