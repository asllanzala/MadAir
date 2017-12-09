package com.honeywell.hch.airtouch.ui.common.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;

/**
 * Created by wuyuan on 10/16/15.
 */
public class TypeTextView extends TextView {

    private static final int MESSAGE_SET_TEXT = 10001;
    private Context mContext = null;

    private String mShowTextString = null;

    private static final int TYPE_TIME_DELAY = 500;

    private boolean isRunning = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SET_TEXT:
                    if (getText().toString().length() < mShowTextString.length()) {
                        setText(mShowTextString.substring(0, getText().toString().length() + 1));
                    } else {
                        setText("");
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public TypeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initTypeTextView(context);
    }

    public TypeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initTypeTextView(context);
    }

    public TypeTextView(Context context) {
        super(context);
        mContext = context;
        initTypeTextView(context);
    }


    public void startLoop() {
        if (isRunning) {
            return;
        }
        isRunning = true;
        mShowTextString = mContext.getString(R.string.enroll_loading_type);
        setText("");
        startTypeLoopTimer();
    }

    public void stop() {
        stopTypeTimer();
    }

    private void initTypeTextView(Context context) {
        mContext = context;
    }


    private void stopTypeTimer() {
        isRunning = false;
    }

    public final void setTypeText(CharSequence text) {
        stopTypeTimer();
        setText(text);
    }

    private void startTypeLoopTimer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    Message message = Message.obtain();
                    message.what = MESSAGE_SET_TEXT;
                    mHandler.sendMessage(message);

                    try {
                        Thread.sleep(TYPE_TIME_DELAY);
                    } catch (Exception e) {

                    }
                }
            }
        }).start();
    }

}
