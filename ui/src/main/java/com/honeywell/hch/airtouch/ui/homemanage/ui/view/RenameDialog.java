package com.honeywell.hch.airtouch.ui.homemanage.ui.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeAndCity;
import com.honeywell.hch.airtouch.ui.common.ui.view.HPlusEditText;
import com.honeywell.hch.airtouch.ui.homemanage.manager.HomeManagerUiManager;

/**
 * Created by Vincent on 15/7/16.
 */
public class RenameDialog {
    private static RenameDialog mRenameDialog;

    private AlertDialog mAlerDialog;

    private HomeManagerUiManager mHomeManagerUiManager;

    public RenameHomeNameCallBack mCreatCallBack;

    private TextView mErrorText;

    private HPlusEditText mDeviceNameEditText;

    private Activity mActivity;
    private TextView mTitleV;
    private HomeAndCity mHomeAndCity;


    private TextWatcher mEditTextWatch = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            setErrorTextStatus();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            StringUtil.maxCharacterFilter(mDeviceNameEditText.getEditText());
            StringUtil.addOrEditHomeFilter(mDeviceNameEditText.getEditText());
        }
    };

    public static RenameDialog getInstance() {
        if (mRenameDialog == null) {
            mRenameDialog = new RenameDialog();
        }
        return mRenameDialog;
    }


    public AlertDialog showAlerDialog(Activity activity, HomeAndCity homeAndCity, RenameHomeNameCallBack callBack) {
        mActivity = activity;
        mHomeAndCity = homeAndCity;
        mCreatCallBack = callBack;
        if (mAlerDialog == null || !mAlerDialog.isShowing()) {
            mAlerDialog = createTwoButtonWithEditTextDialog(activity);
        }
        return mAlerDialog;
    }

    private void inflatView(AlertDialog dialog) {
        mTitleV = (TextView) dialog.findViewById(R.id.dialog_tip);
        mTitleV.setText(R.string.home_manager_input);
        mDeviceNameEditText = (HPlusEditText) dialog.findViewById(R.id.new_group_name);
        mDeviceNameEditText.setEditTextImageViewVisible(View.GONE);
        mDeviceNameEditText.setEditorHint(mActivity.getString(R.string.my_home));
        mDeviceNameEditText.setEditorText(mHomeAndCity.getHomeName());
        mDeviceNameEditText.setTextChangedListener(mEditTextWatch);

        Button cancelBtn = (Button) dialog.findViewById(R.id.dialog_left_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });

        Button doneBtn = (Button) dialog.findViewById(R.id.dialog_right_button);
        doneBtn.setText(R.string.home_manager_rename);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCreatCallBack.doneBtnCallBack(mDeviceNameEditText.getEditorText(), mHomeAndCity);
            }
        });

        mErrorText = (TextView) dialog.findViewById(R.id.error_tip);
        mErrorText.setVisibility(View.GONE);
    }

    public void dismissDialog() {
        if (mAlerDialog != null) {
            mAlerDialog.cancel();
            mAlerDialog = null;
        }
    }


    public interface RenameHomeNameCallBack {
        public void doneBtnCallBack(String homeName, HomeAndCity homeAndCity);
    }

    public void setErrorText(String str) {
        mErrorText.setVisibility(View.VISIBLE);
        mErrorText.setText(str);
    }

    private void setErrorTextStatus() {
        if (mDeviceNameEditText != null && StringUtil.isEmpty(mDeviceNameEditText.getEditorText())) {
            if (mErrorText != null && mErrorText.getVisibility() == View.VISIBLE) {
                mErrorText.setVisibility(View.GONE);
            }
        } else if (mDeviceNameEditText != null) {
            if (mErrorText.getText().equals(mActivity.getString(R.string.home_name_empty))) {
                mErrorText.setVisibility(View.GONE);
            }
        }

    }

    /**
     * @param activity referenced activity
     */
    private AlertDialog createTwoButtonWithEditTextDialog(final Activity activity) {
        if (activity == null || activity.isFinishing())
            return null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (activity.isDestroyed())
                return null;
        }
        return showDialog(activity);
    }


    /**
     * create dialog with customer view
     *
     * @param context
     * @return
     */
    private AlertDialog showDialog(Context context) {
        final AlertDialog myDialog = new AlertDialog.Builder(context).create();

        myDialog.show();
        myDialog.getWindow().setContentView(R.layout.dialog_with_input_text);

        inflatView(myDialog);

        myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        myDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        myDialog.setCancelable(false);
        return myDialog;
    }
}
