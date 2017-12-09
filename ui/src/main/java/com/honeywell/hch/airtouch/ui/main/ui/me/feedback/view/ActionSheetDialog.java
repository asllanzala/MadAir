package com.honeywell.hch.airtouch.ui.main.ui.me.feedback.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.ui.R;

import java.util.ArrayList;
import java.util.List;

public class ActionSheetDialog {
    private Context context;
    private Dialog dialog;
    private TextView txt_title;
    private TextView txt_cancel;
    private LinearLayout lLayout_content;
    private ScrollView sLayout_content;
    private boolean showTitle = false;
    private List<SheetItem> sheetItemList;
    private Display display;

    public ActionSheetDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public ActionSheetDialog builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_actionsheet, null);

        view.setMinimumWidth(display.getWidth());

        sLayout_content = (ScrollView) view.findViewById(R.id.sLayout_content);
        lLayout_content = (LinearLayout) view
                .findViewById(R.id.lLayout_content);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
        txt_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        return this;
    }

    public ActionSheetDialog setTitle(String title) {
        showTitle = true;
        txt_title.setVisibility(View.VISIBLE);
        txt_title.setText(title);
        return this;
    }

    public ActionSheetDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public ActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * @param strItem  ��Ŀ���
     * @param color    ��Ŀ������ɫ������null��Ĭ����ɫ
     * @param listener
     * @return
     */
    public ActionSheetDialog addSheetItem(String strItem, SheetItemColor color, int drawable,
                                          OnSheetItemClickListener listener) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<SheetItem>();
        }
        sheetItemList.add(new SheetItem(strItem, color, drawable, listener));
        return this;
    }

    private void setSheetItems() {
        if (sheetItemList == null || sheetItemList.size() <= 0) {
            return;
        }

        int size = sheetItemList.size();
        if (size >= 7) {
            LayoutParams params = (LayoutParams) sLayout_content
                    .getLayoutParams();
            params.height = display.getHeight() / 2;
            sLayout_content.setLayoutParams(params);
        }
        int height = DensityUtil.dip2px(30);
        for (int i = 1; i <= size; i++) {
            final int index = i;
            SheetItem sheetItem = sheetItemList.get(i - 1);
            String strItem = sheetItem.name;
            SheetItemColor color = sheetItem.color;
            final OnSheetItemClickListener listener = (OnSheetItemClickListener) sheetItem.itemClickListener;

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, DensityUtil.dip2px(45));
            llParams.gravity = Gravity.CENTER_VERTICAL;
            linearLayout.setLayoutParams(llParams);

            ImageView imageView = new ImageView(context);
            imageView.setImageResource(sheetItem.drawable);
            TextView textView = new TextView(context);
            textView.setText(strItem);
            textView.setTextSize(15);

            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(0, height, 1);
            LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            if (size == 1) {
                if (showTitle) {
                    linearLayout.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                } else {
                    linearLayout.setBackgroundResource(R.drawable.actionsheet_single_selector);
                }
            } else {
                if (showTitle) {
                    if (i >= 1 && i < size) {
                        linearLayout.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    } else {
                        linearLayout.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                    }
                } else {
                    if (i == 1) {
                        linearLayout.setBackgroundResource(R.drawable.actionsheet_top_selector);
                        tvParams.setMargins(DensityUtil.dip2px(25), 0, DensityUtil.dip2px(10), 0);
                        ivParams.setMargins(0, 0, DensityUtil.dip2px(22), 0);
                    } else if (i < size) {
                        linearLayout.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    } else {
                        linearLayout.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                        tvParams.setMargins(DensityUtil.dip2px(20), 0, DensityUtil.dip2px(10), 0);
                        ivParams.setMargins(0, 0, DensityUtil.dip2px(18), 0);
                    }
                }
            }

            if (color == null) {
                textView.setTextColor(Color.parseColor(SheetItemColor.Blue
                        .getName()));
            } else {
                textView.setTextColor(Color.parseColor(color.getName()));
            }

//            tvParams.setMargins(DensityUtil.dip2px(25), 0, DensityUtil.dip2px(10), 0);
            //设置textview垂直居中
            tvParams.gravity = Gravity.CENTER_VERTICAL;
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setLayoutParams(tvParams);


//            ivParams.setMargins(0, 0, DensityUtil.dip2px(22), 0);
            ivParams.gravity = Gravity.CENTER_VERTICAL;
            imageView.setLayoutParams(ivParams);

            linearLayout.addView(textView);
            linearLayout.addView(imageView);
            linearLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(index);
                    dialog.dismiss();
                }
            });
            lLayout_content.addView(linearLayout);
        }
    }

    public void show() {
        setSheetItems();
        dialog.show();
    }

    public interface OnSheetItemClickListener {
        void onClick(int which);
    }

    public class SheetItem {
        String name;
        OnSheetItemClickListener itemClickListener;
        SheetItemColor color;
        int drawable;


        public SheetItem(String name, SheetItemColor color, int drawable,
                         OnSheetItemClickListener itemClickListener) {
            this.name = name;
            this.color = color;
            this.itemClickListener = itemClickListener;
            this.drawable = drawable;
        }
    }

    public enum SheetItemColor {
        Blue("#037BFF"), Red("#FD4A2E"),
        Black("#222222");

        private String name;

        private SheetItemColor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
