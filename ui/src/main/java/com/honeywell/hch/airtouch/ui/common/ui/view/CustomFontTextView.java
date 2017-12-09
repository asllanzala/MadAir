package com.honeywell.hch.airtouch.ui.common.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;

import java.util.Locale;

/**
 * Created by h127856 on 16/9/1.
 */
public class CustomFontTextView extends TextView {

    private static final String CHINESE_STRING = "zh";
    private Context mContext;

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.customtextview);
            String mLocalLanguage = Locale.getDefault().getLanguage();
            if (!CHINESE_STRING.equals(mLocalLanguage)){
                boolean isBold = ta.getBoolean(R.styleable.customtextview_textboldCustom, false);
                setTypefaceName(isBold);
            }
            ta.recycle();
        }
    }

    public CustomFontTextView(Context context) {
        super(context);
        this.mContext = context;
    }

    private void setTypefaceName(boolean  isBold) {
        String fontName = isBold ? "HoneywellSans_Bold.otf" : "HoneywellSans_Book.otf";
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), fontName);
        this.setTypeface(typeface);
        System.gc();
    }


    public void setCustomText(CharSequence text) {
        if (HPlusConstants.DATA_LOADING_STATUS.equals(text)){
            setTypefaceName(true);
        }
        super.setText(text);
    }

}
