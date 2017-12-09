package com.honeywell.hch.airtouch.ui.authorize.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthMessageModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.GetAuthMessagesResponse;
import com.honeywell.hch.airtouch.ui.authorize.ui.view.AuthorizeMessageCardView;

/**
 * Created by Vincent on 15/2/16.
 */
public class AuthorizeMessageAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private GetAuthMessagesResponse mGetAuthMessagesResponse;
    private Callback mCallback;

    public AuthorizeMessageAdapter(Context context, GetAuthMessagesResponse getAuthMessagesResponse, Callback callback) {
        mContext = context;
        mGetAuthMessagesResponse = getAuthMessagesResponse;
        mCallback = callback;
        this.inflater = LayoutInflater.from(context);
    }

    public AuthorizeMessageAdapter(Context context, Callback callback) {
        mContext = context;
        mCallback = callback;
        this.inflater = LayoutInflater.from(context);
        setmGetAuthMessagesResponse(null);
    }

    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     */
    public interface Callback {
        public void click(View v, int action, AuthMessageModel authMessageModel, int position);
    }

    @Override
    public int getCount() {
        return mGetAuthMessagesResponse.getAuthMessages() == null ?
                0 : mGetAuthMessagesResponse.getAuthMessages().size();
    }

    @Override
    public AuthMessageModel getItem(int position) {
        if (mGetAuthMessagesResponse.getAuthMessages() != null) {
            return mGetAuthMessagesResponse.getAuthMessages().size() == 0 ?
                    null : mGetAuthMessagesResponse.getAuthMessages().get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return mGetAuthMessagesResponse.getAuthMessages().get(position).getMessageType();
    }

    private void setmGetAuthMessagesResponse(GetAuthMessagesResponse getAuthMessagesResponse) {
        if (getAuthMessagesResponse != null) {
            this.mGetAuthMessagesResponse = getAuthMessagesResponse;
        } else {
            this.mGetAuthMessagesResponse = new GetAuthMessagesResponse();
        }
    }

    public void changeData(GetAuthMessagesResponse getAuthMessagesResponse) {
        this.setmGetAuthMessagesResponse(getAuthMessagesResponse);
        this.notifyDataSetChanged();
    }

    public void changeData(int position) {
        if (position < mGetAuthMessagesResponse.getAuthMessages().size()) {
            mGetAuthMessagesResponse.getAuthMessages().remove(position);
            this.notifyDataSetChanged();
        }
    }

    public void append(GetAuthMessagesResponse getAuthMessagesResponse) {
        this.mGetAuthMessagesResponse.getAuthMessages().addAll(getAuthMessagesResponse.getAuthMessages());
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            AuthorizeMessageCardView cardView = new AuthorizeMessageCardView(mContext);
            convertView = cardView;
        }
        ((AuthorizeMessageCardView)convertView).initViewHolder(convertView, mGetAuthMessagesResponse.getAuthMessages().get(position), mCallback, position);
        return convertView;
    }

}
