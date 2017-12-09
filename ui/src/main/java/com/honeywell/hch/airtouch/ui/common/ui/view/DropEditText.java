package com.honeywell.hch.airtouch.ui.common.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.manager.model.DropTextModel;

public class DropEditText extends FrameLayout implements OnItemClickListener, View.OnClickListener {

    /**
     * 两次点击事件相隔的事件
     */
    private static final long MIN_DELAY_MS = 400;
    private long mLastClickTime = 0;

    private EditText mEditText;  // 输入框
    private ImageView mDropImage; // 右边的图片按钮
    private PopupWindow mPopup; // 点击图片弹出popupwindow
    private WrapListView mPopView; // popupwindow的布局
    private LinearLayout mLinearLy;
    private int mDropMode;
    private String mHit;

    private ImageView mImageView;

    private IAfterSelectResult mAfterSelectResult;

    private Context mContext;

    /**
     * 记录上一次的选择的国家码
     */
    private String mLastChooseCode = "";

    public DropEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public DropEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.edit_layout, this);
        mPopView = (WrapListView) LayoutInflater.from(context).inflate(R.layout.pop_view, null);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DropEditText, defStyle, 0);
        mDropMode = ta.getInt(R.styleable.DropEditText_dropMode, 0);
        mHit = ta.getString(R.styleable.DropEditText_hint);
        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mEditText = (EditText) findViewById(R.id.dropview_edit);
        mDropImage = (ImageView) findViewById(R.id.dropview_image);
        mLinearLy = (LinearLayout) findViewById(R.id.dropview_ly);
        mImageView = (ImageView) findViewById(R.id.image_id);

        mEditText.setSelectAllOnFocus(true);
        if (!TextUtils.isEmpty(mHit)) {
            mEditText.setHint(mHit);
        }
        mEditText.setSingleLine(true);
        mDropImage.setOnClickListener(this);
        mPopView.setOnItemClickListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 如果布局发生改
        // 并且dropMode是flower_parent
        // 则设置ListView的宽度
        if (changed && 0 == mDropMode) {
            mPopView.setListWidth(getMeasuredWidth());
        }
    }

    /**
     * 设置Adapter
     *
     * @param adapter ListView的Adapter
     */
    public void setAdapter(BaseAdapter adapter, int width) {
        mPopView.setAdapter(adapter);
        if (adapter.getCount() > 6) {
            mPopup = new PopupWindow(mPopView, width, DensityUtil.dip2px(160));
        } else {
            mPopup = new PopupWindow(mPopView, width, LayoutParams.WRAP_CONTENT);
        }
        mPopup.setBackgroundDrawable(getResources().getDrawable(R.color.white));// 设置背景色
        checkVersion();

    }

    /**
     *
     */
    public void checkVersion() {
        //if version < 4.4, then set focus
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mPopup.setOutsideTouchable(true);
            mPopup.setFocusable(true); // 让popwin获取焦点
        }
    }

    /**
     * 获取输入框内的内容
     *
     * @return String content
     */
    public String getText() {
        return mEditText.getText().toString();
    }

    public EditText getmEditText() {
        return mEditText;
    }

    public ImageView getmDropImage() {
        return mDropImage;
    }

    public LinearLayout getmLinearLy() {
        return mLinearLy;
    }

    public PopupWindow getmPopup() {
        return mPopup;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dropview_image) {
            dealWithClickEvent();
        }
    }

    public void dismissPopup() {
        if (mPopup != null && mPopup.isShowing()) {
            mPopup.dismiss();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        DropTextModel dropTextModel = (DropTextModel) mPopView.getAdapter().getItem(position);
        mEditText.setText(dropTextModel.getTextViewString());

        if (dropTextModel.getLeftImageId() != 0) {
            mImageView.setBackgroundResource(dropTextModel.getLeftImageId());
            mImageView.setVisibility(View.VISIBLE);
        }

        closeDropPopWindow();

        if (mAfterSelectResult != null) {
            String cityCode = mContext.getString(R.string.china_str).equals(dropTextModel.getTextViewString())
                    ? HPlusConstants.CHINA_CODE : HPlusConstants.INDIA_CODE;
            if (!cityCode.equals(mLastChooseCode)) {
                mAfterSelectResult.setSelectResult(cityCode);
            }
            mAfterSelectResult.setSelectResult(dropTextModel);
            mLastChooseCode = cityCode;
        }
    }

    public void dealWithClickEvent() {
        //防止多次连续点击，导致dropImageClickEvent这个方法调用多次，会出现AIRQA-1640 crash
        long lastClickTime = mLastClickTime;
        long now = System.currentTimeMillis();
        mLastClickTime = now;
        if (now - lastClickTime > MIN_DELAY_MS) {
            //hide the input keyboard
            ((InputMethodManager) getContext().getSystemService(
                    getContext().INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mEditText.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            dropImageClickEvent();
        }
    }

    public boolean isTouchInThisDropEditText(float x, float y) {

        //获取mDropImage在屏幕中的坐标
        int[] location = new int[2];
        mLinearLy.getLocationOnScreen(location);
        float topX = location[0];
        float topY = location[1];

        //判断触摸点是否在mDropImage内
        boolean isIn = x >= topX && x <= topX + mLinearLy.getWidth() && y >= topY && y <= topY + mLinearLy.getHeight();
        if (isIn) {
            return true;
        }
        return false;
    }

    public void dropImageClickEvent() {
        if (mPopup != null && mPopup.isShowing()) {
            mPopup.dismiss();
            mLinearLy.setBackgroundResource(R.drawable.input_normal);
            mDropImage.setBackgroundResource(R.drawable.drop_down_btn_normal);
            return;
        }
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.requestFocus();
        mLinearLy.setBackgroundResource(R.drawable.input_active);
        mDropImage.setBackgroundResource(R.drawable.drop_up_btn_active);
        mPopup.showAsDropDown(this, 0, 1);

    }

    public void closeDropPopWindow() {
        if (mPopup != null && mPopup.isShowing()) {
            mPopup.dismiss();
        }

        int resId = mLinearLy.isEnabled() ? R.drawable.input_normal : R.drawable.input_disable;
        mLinearLy.setBackgroundResource(resId);

        int dropSrcId = mDropImage.isClickable() ? R.drawable.drop_down_btn_normal : R.drawable.drop_btn_disabled;

        mDropImage.setBackgroundResource(dropSrcId);
    }

    public void setDropTextItem(String cityName, int countrySrcId) {
        mEditText.setText(cityName);
        mImageView.setBackgroundResource(countrySrcId);
        mImageView.setVisibility(View.VISIBLE);
        if (countrySrcId == R.drawable.china_flag) {
            mLastChooseCode = HPlusConstants.CHINA_CODE;
        } else {
            mLastChooseCode = HPlusConstants.INDIA_CODE;
        }
    }


    public void setfterSelectResultInterface(IAfterSelectResult afterSelectResult) {
        mAfterSelectResult = afterSelectResult;
    }

    public interface IAfterSelectResult {
        /**
         * 用户选择了对应的落下列表后，需要在界面进行的操作
         *
         * @param cityCode
         */
        void setSelectResult(String cityCode);


        void setSelectResult(DropTextModel dropTextModel);
    }

    public void setViewEnable(boolean isEnable) {
        mDropImage.setClickable(isEnable);
        mEditText.setEnabled(isEnable);
        mLinearLy.setEnabled(isEnable);
        int dropSrcId = isEnable ? R.drawable.drop_down_btn_normal : R.drawable.drop_btn_disabled;
        mDropImage.setBackgroundResource(dropSrcId);
        int srcId = isEnable ? R.drawable.input_normal : R.drawable.input_disable;
        mLinearLy.setBackgroundResource(srcId);
    }

}
