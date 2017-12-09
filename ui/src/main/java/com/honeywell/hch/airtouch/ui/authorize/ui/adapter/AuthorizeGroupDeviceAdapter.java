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
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthBaseModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.AuthGroupDeviceListModel;

/**
 * Created by Vincent on 15/2/16.
 */
public class AuthorizeGroupDeviceAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private AuthGroupDeviceListModel mAuthGroupDeviceListModel;

    public AuthorizeGroupDeviceAdapter(Context context, AuthGroupDeviceListModel authGroupDeviceListModel) {
        mContext = context;
        mAuthGroupDeviceListModel = authGroupDeviceListModel;
        this.inflater = LayoutInflater.from(context);
    }

    public AuthorizeGroupDeviceAdapter(Context context) {
        mContext = context;
        this.inflater = LayoutInflater.from(context);
        setmGetAuthMessagesResponse(null);
    }

    @Override
    public int getCount() {
        return mAuthGroupDeviceListModel.getmAuthDevices() == null ?
                0 : mAuthGroupDeviceListModel.getmAuthDevices().size();
    }

    @Override
    public AuthBaseModel getItem(int position) {
        return mAuthGroupDeviceListModel.getmAuthDevices().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private void setmGetAuthMessagesResponse(AuthGroupDeviceListModel authGroupDeviceListModel) {
        if (authGroupDeviceListModel != null) {
            this.mAuthGroupDeviceListModel = authGroupDeviceListModel;
        } else {
            this.mAuthGroupDeviceListModel = new AuthGroupDeviceListModel();
        }
    }

    public void changeData(AuthGroupDeviceListModel authGroupDeviceListModel) {
        this.setmGetAuthMessagesResponse(authGroupDeviceListModel);
        this.notifyDataSetChanged();
    }

    public void changeData(int position) {
        if (position < mAuthGroupDeviceListModel.getmAuthDevices().size()) {
            mAuthGroupDeviceListModel.getmAuthDevices().remove(position);
            this.notifyDataSetChanged();
        }
    }

    public void append(AuthGroupDeviceListModel authGroupDeviceListModel) {
        this.mAuthGroupDeviceListModel.getmAuthDevices().addAll(authGroupDeviceListModel.getmAuthDevices());
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.auth_children_item, null);
        }
        TextView childDeviceNameTv = ViewHolderUtil.get(convertView, R.id.auth_children_device_name_tv);
        TextView childStatusTv = ViewHolderUtil.get(convertView, R.id.auth_children_device_status_tv);
        TextView childClickNameTv = ViewHolderUtil.get(convertView, R.id.auth_children_click_tv);
        ImageView childGroupDeviceIv = ViewHolderUtil.get(convertView, R.id.auth_children_icon_iv);
        ImageView childStatusIv = ViewHolderUtil.get(convertView, R.id.auth_children_device_status_iv);

        AuthBaseModel authBaseModel = getItem(position);
        childStatusTv.setVisibility(View.GONE);
        childStatusIv.setVisibility(View.GONE);
        if (mAuthGroupDeviceListModel.ismIsLocatonOwner()) {
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
            childClickNameTv.setText(mContext.getString(authBaseModel.getAuthRemoveAction()));
        }
        childGroupDeviceIv.setBackgroundResource(authBaseModel.getModelIcon());
        childDeviceNameTv.setText(authBaseModel.getModelName());

        return convertView;
    }

}
