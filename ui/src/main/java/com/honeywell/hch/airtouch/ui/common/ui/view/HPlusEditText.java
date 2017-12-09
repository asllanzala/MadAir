package com.honeywell.hch.airtouch.ui.common.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.honeywell.hch.airtouch.ui.R;

/**
 * Created by h127856 on 5/29/16.
 */
public class HPlusEditText extends RelativeLayout {


    private Context mContext;

    private EditText mEditTextView;

    private ImageView mEditTextImageView;

    private boolean mIsPasswordEyeOn = false;

    private IChangeEyeStatus mChangeEyeStatus;

    public HPlusEditText(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public HPlusEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.customer_edit_textview, this);

        mEditTextView = (EditText) findViewById(R.id.edit_text_id);
        mEditTextImageView = (ImageView) findViewById(R.id.edit_icon_id);

        mEditTextView.addTextChangedListener(mTextWatch);
    }

    private OnFocusChangeListener mEditorFocusChangedListener = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            setSelected(hasFocus);
            showImageButton(hasFocus &&
                    !(mEditTextView.getText().toString().isEmpty()));

        }
    };

    private OnClickListener mPasswordListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            clickEyeBtn();
        }
    };

    private OnClickListener mCleanImageListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            mEditTextView.setText(null);
            showImageButton(false);
        }
    };

    private TextWatcher mTextWatch = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public EditText getEditText() {
        return mEditTextView;
    }

    public ImageView getImageView() {
        return mEditTextImageView;
    }

    public void setEditorText(String text) {
        mEditTextView.setText(text);
        if (null != text) {
            showImageButton(mEditTextView.hasFocus() && !(text.toString().isEmpty()));
        }
    }


    public void showImageButton(boolean isShow) {
        mEditTextImageView.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 设置editor的hint文本
     *
     * @param text hint文本
     */
    public void setEditorHint(String text) {
        mEditTextView.setHint(text);
    }


    /**
     * @return 获取当前editor的输入text
     */
    public String getEditorText() {
        return mEditTextView.getText().toString();
    }


    /**
     * when click the password listen
     */
    public void clickEyeBtn() {
        if (!mIsPasswordEyeOn) {
            mIsPasswordEyeOn = true;
            mEditTextView.setInputType(InputType.TYPE_CLASS_TEXT);
            mEditTextView.setSelection(mEditTextView.getText().length());
            mEditTextImageView.setImageResource(R.drawable.eye_on);
        } else {
            mIsPasswordEyeOn = false;
            mEditTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mEditTextView.setSelection(mEditTextView.getText().length());
            mEditTextView.setTypeface(Typeface.DEFAULT);
            mEditTextImageView.setImageResource(R.drawable.eye_off);
        }
        if (mChangeEyeStatus != null){
            mChangeEyeStatus.afterChangeEyeStatus(mIsPasswordEyeOn);
        }
    }


    public void setPasswordImage(boolean isEyeOn) {
        mIsPasswordEyeOn = isEyeOn;

        int resId = mIsPasswordEyeOn ? R.drawable.eye_on : R.drawable.eye_off;

        mEditTextImageView.setImageResource(resId);
        int inputType = mIsPasswordEyeOn ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
        mEditTextView.setInputType(inputType);
        mEditTextView.setTypeface(Typeface.DEFAULT);
//        setTouchFocusChangeListener();
        mEditTextImageView.setOnClickListener(mPasswordListener);
    }


    public void setCleanImage() {
        mEditTextImageView.setImageResource(R.drawable.ic_delete);
        mEditTextView.setInputType(InputType.TYPE_CLASS_TEXT);
        mEditTextImageView.setOnClickListener(mCleanImageListener);
        setTouchFocusChangeListener();
        mEditTextImageView.setVisibility(View.INVISIBLE);
    }

    /**
     * 确认密码框
     */
    public void setPasswordCleanImage() {
        mEditTextImageView.setImageResource(R.drawable.ic_delete);
        mEditTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mEditTextImageView.setOnClickListener(mCleanImageListener);
        setTouchFocusChangeListener();
        mEditTextView.setTypeface(Typeface.DEFAULT);
        mEditTextImageView.setVisibility(View.INVISIBLE);
    }

    /*
    搜索框
     */
    public void setAddHomeSearchImage(){
        mEditTextImageView.setImageResource(R.drawable.add_home_search_icon);
        mEditTextView.setInputType(InputType.TYPE_CLASS_TEXT);
        mEditTextView.setTypeface(Typeface.DEFAULT);
        mEditTextImageView.setVisibility(View.VISIBLE);
    }

    public void setEditTextImageViewVisible(int isVisible) {
        mEditTextImageView.setVisibility(isVisible);
    }

    public void setEditTextGravity(int gravity) {
        mEditTextView.setGravity(gravity);
    }


    public void setTextChangedListener(TextWatcher watcher) {
        mEditTextView.addTextChangedListener(watcher);
    }

    public void setTextMaxLength(int length) {
        mEditTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
    }


    public void setTouchFocusChangeListener(){
      mEditTextView.setOnFocusChangeListener(mEditorFocusChangedListener);

    }

    public void setChangeEyeStatusInterface(IChangeEyeStatus iChangeEyeStatus){
        mChangeEyeStatus = iChangeEyeStatus;
    }

//    public void setEnabled()

    public interface IChangeEyeStatus{
        public void afterChangeEyeStatus(boolean isEyeOn);
    }

}
