package com.honeywell.hch.airtouch.ui.common.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;

/**
 * Created by h127856 on 6/8/16.
 */
public class CenterTextView extends TextView {

    private static final int CENTER = 1;
    private static final int NORMAL = 0;
    private StaticLayout myStaticLayout;
    private TextPaint tp;

    private int mShowModel = NORMAL;
    private boolean isBold = true;

    public CenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CenterTextView);
            mShowModel = ta.getInt(R.styleable.CenterTextView_showModel, 0);

            isBold = ta.getBoolean(R.styleable.CenterTextView_textbold, true);
            ta.recycle();
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initView();
    }

    private void initView() {
        tp = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        tp.setTextSize(getTextSize());
        tp.setColor(getCurrentTextColor());

        Layout.Alignment align = Layout.Alignment.ALIGN_NORMAL;
        if (mShowModel == CENTER) {
            align = Layout.Alignment.ALIGN_CENTER;
        }
        myStaticLayout = new StaticLayout(getText(), tp, getWidth(), align, 1.0f, 0.0f, false);
        if (isBold) {
            tp.setFakeBoldText(true);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initView();
        myStaticLayout.draw(canvas);
    }
}
