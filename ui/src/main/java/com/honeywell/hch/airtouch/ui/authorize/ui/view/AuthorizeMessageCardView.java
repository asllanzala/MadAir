package com.honeywell.hch.airtouch.ui.authorize.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.util.DateTimeUtil;
import com.honeywell.hch.airtouch.library.util.ViewHolderUtil;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthMessageModel;
import com.honeywell.hch.airtouch.ui.authorize.ui.adapter.AuthorizeMessageAdapter;

/**
 * Created by Vincent on 1/2/16.
 */
public class AuthorizeMessageCardView extends RelativeLayout {
    private final String TAG = "AuthorizeMessageCardView";
    private Context mContext;

    public static final int ERRORCALLPUSH = -3;
    public static final int ERRORCALL = -1;
    public static final int DECLINEACTON = 0;
    public static final int ACCEPTACTION = 1;
    public static final int GOTITACTION = 3;

    /*
    type:1 对方接受消息
    2   对方拒绝消息
    3   对方解除消息             remove
    4   设备授权消息
    5   设备所有者解除授权消息     revoke

     */

    public AuthorizeMessageCardView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public AuthorizeMessageCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.auth_message_item, this);
    }

    public void initViewHolder(View view, AuthMessageModel authMessageModel, AuthorizeMessageAdapter.Callback callback, int position) {
        TextView titleTv = ViewHolderUtil.get(view, R.id.auth_message_title_tv);
        ImageView titleIv = ViewHolderUtil.get(view, R.id.auth_message_title_iv);

        TextView nameKeyTv = ViewHolderUtil.get(view, R.id.auth_message_name_key_tv);
        TextView nameTv = ViewHolderUtil.get(view, R.id.auth_message_name_value_tv);

        TextView placeKeyTv = ViewHolderUtil.get(view, R.id.auth_message_place_key_tv);
        TextView placeTv = ViewHolderUtil.get(view, R.id.auth_message_place_value_tv);

        TextView deviceKeyTv = ViewHolderUtil.get(view, R.id.auth_message_device_key_tv);
        TextView deviceTv = ViewHolderUtil.get(view, R.id.auth_message_device_value_tv);

        LinearLayout authLy = ViewHolderUtil.get(view, R.id.auth_message_authority_ly);
        TextView authKeyTv = ViewHolderUtil.get(view, R.id.auth_message_authority_key_tv);
        TextView authTv = ViewHolderUtil.get(view, R.id.auth_message_authority_value_tv);

        TextView sendKeyTv = ViewHolderUtil.get(view, R.id.auth_message_send_key_tv);
        TextView sendTv = ViewHolderUtil.get(view, R.id.auth_message_send_value_tv);

        LinearLayout timeLy = ViewHolderUtil.get(view, R.id.auth_message_expire_ly);
        TextView expireKeyTv = ViewHolderUtil.get(view, R.id.auth_message_expire_key_tv);
        TextView expireTv = ViewHolderUtil.get(view, R.id.auth_message_expire_value_tv);

        LinearLayout remindLy = ViewHolderUtil.get(view, R.id.auth_message_remind_ly);

        Button leftBtn = ViewHolderUtil.get(view, R.id.auth_message_left_btn);
        Button rightBtn = ViewHolderUtil.get(view, R.id.auth_message_right_btn);

        rightBtn.setVisibility(View.GONE);
        timeLy.setVisibility(View.GONE);
        remindLy.setVisibility(View.GONE);
        authLy.setVisibility(VISIBLE);

        switch (authMessageModel.getMessageType()) {
            case 1:   //对方接受消息
                titleIv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.request_accepted_icon));
                leftBtn.setText(mContext.getString(R.string.authorize_message_get));
                sendKeyTv.setText(mContext.getString(R.string.authorize_message_accept_time));
                nameKeyTv.setText(mContext.getString(R.string.authorize_message_accept_by));
                leftBtn.setOnClickListener(new MyBtnClickListener(GOTITACTION, authMessageModel, callback, position));
                break;
            case 2:   //对方拒绝消息
                titleIv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.request_declined_icon));
                leftBtn.setText(mContext.getString(R.string.authorize_message_get));
                nameKeyTv.setText(mContext.getString(R.string.authorize_message_decline_by));
                sendKeyTv.setText(mContext.getString(R.string.authorize_message_decline_time));
                leftBtn.setOnClickListener(new MyBtnClickListener(GOTITACTION, authMessageModel, callback, position));
                break;
            case 3:   //对方解除消息
                authLy.setVisibility(GONE);
                titleIv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.auth_revoked_or_removed));
                sendKeyTv.setText(mContext.getString(R.string.authorize_message_remove_time));
                leftBtn.setText(mContext.getString(R.string.authorize_message_get));
                nameKeyTv.setText(mContext.getString(R.string.authorize_message_remove_by));
                leftBtn.setOnClickListener(new MyBtnClickListener(GOTITACTION, authMessageModel, callback, position));
                break;
            case 4:  //设备授权消息 (设备接受者)
                timeLy.setVisibility(View.VISIBLE);
                nameKeyTv.setText(mContext.getString(R.string.authorize_message_from));
                sendKeyTv.setText(mContext.getString(R.string.authorize_message_send));
                expireTv.setText(DateTimeUtil.getDateTimeString(DateTimeUtil.AUTHORIZE_TIME_FORMAT, DateTimeUtil.AUTHORIZE_TIME_FORMAT2,
                        DateTimeUtil.AUTHORIZE_TIME_TO_FORMAT, authMessageModel.getExpirationTime()));
                if (authMessageModel.isExpired()) {
                    titleIv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.new_request_expired));
                    remindLy.setVisibility(View.VISIBLE);
                    leftBtn.setText(mContext.getString(R.string.authorize_message_get));
                    leftBtn.setOnClickListener(new MyBtnClickListener(GOTITACTION, authMessageModel, callback, position));
                } else {
                    titleIv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.message_auth_icon));
                    rightBtn.setVisibility(View.VISIBLE);
                    leftBtn.setText(mContext.getString(R.string.authorize_message_accept));
                    rightBtn.setText(mContext.getString(R.string.authorize_message_decline));
                    leftBtn.setOnClickListener(new MyBtnClickListener(ACCEPTACTION, authMessageModel, callback, position));
                    rightBtn.setOnClickListener(new MyBtnClickListener(DECLINEACTON, authMessageModel, callback, position));
                }
                break;
            case 5:  //授权降级
                titleIv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.message_auth_icon));
                leftBtn.setText(mContext.getString(R.string.authorize_message_get));
                sendKeyTv.setText(mContext.getString(R.string.authorize_message_send));
                nameKeyTv.setText(mContext.getString(R.string.authorize_message_from));
                leftBtn.setOnClickListener(new MyBtnClickListener(GOTITACTION, authMessageModel, callback, position));
                break;
            case 6: //设备所有者解除授权消息 （设备接受者）
                authLy.setVisibility(GONE);
                titleIv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.auth_revoked_or_removed));
                sendKeyTv.setText(mContext.getString(R.string.authorize_message_revoke_time));
                leftBtn.setText(mContext.getString(R.string.authorize_message_get));
                nameKeyTv.setText(mContext.getString(R.string.authorize_message_revoke_by));
                leftBtn.setOnClickListener(new MyBtnClickListener(GOTITACTION, authMessageModel, callback, position));
                break;
        }
        titleTv.setText(mContext.getString(authMessageModel.parseMessageType()));
        nameTv.setText(authMessageModel.getSenderName());
        sendTv.setText(DateTimeUtil.getDateTimeString(DateTimeUtil.AUTHORIZE_TIME_FORMAT, DateTimeUtil.AUTHORIZE_TIME_FORMAT2,
                DateTimeUtil.AUTHORIZE_TIME_TO_FORMAT, authMessageModel.getInvitationTime()));
        placeTv.setText(authMessageModel.getLocationName());
        deviceKeyTv.setText(authMessageModel.parseTargetType());
        deviceTv.setText(authMessageModel.getTargetName());
        authTv.setText(authMessageModel.parseRole());

    }

    class MyBtnClickListener implements OnClickListener {

        private int mAction;
        private AuthorizeMessageAdapter.Callback mCallback;
        private AuthMessageModel mAuthMessageModel;
        private int mPosition;

        public MyBtnClickListener(int action, AuthMessageModel authMessageModel, AuthorizeMessageAdapter.Callback callback, int position) {
            mAction = action;
            mAuthMessageModel = authMessageModel;
            mCallback = callback;
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            mCallback.click(v, mAction, mAuthMessageModel, mPosition);
        }
    }
}
