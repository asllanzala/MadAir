package com.honeywell.hch.airtouch.ui.main.ui.messagecenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.model.message.MessageModel;
import com.honeywell.hch.airtouch.plateform.http.task.GetAuthMessagesTask;
import com.honeywell.hch.airtouch.plateform.http.task.GetMessagesPerPageTask;
import com.honeywell.hch.airtouch.plateform.update.UpdateManager;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.authorize.ui.view.AuthorizeMessageCardView;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseRequestFragment;
import com.honeywell.hch.airtouch.ui.common.ui.controller.HomeDeviceInfoBaseFragment.ButtomType;
import com.honeywell.hch.airtouch.ui.common.ui.view.HplusNetworkErrorLayout;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.main.manager.Message.manager.MessageManager;
import com.honeywell.hch.airtouch.ui.main.manager.Message.manager.MessageUiManager;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainActivity;
import com.honeywell.hch.airtouch.ui.main.ui.messagecenter.adapter.AuthorizeMessageAdapter;
import com.honeywell.hch.airtouch.ui.main.ui.messagecenter.view.MessageListItemView;
import com.honeywell.hch.airtouch.ui.main.ui.messagecenter.view.PullToRefreshListView;
import com.honeywell.hch.airtouch.ui.update.ui.UpdateVersionMinderActivity;

import java.util.ArrayList;

/**
 * Created by Vincent Chen on 2/13/15.
 */
public class MessagesFragment extends BaseRequestFragment implements View.OnClickListener {
    private final String TAG = "MessagesFragment";
    private View mTopView;
    private RelativeLayout mLoadingRl;
    private TextView mMessageTitleTv;
    protected PullToRefreshListView mMessageLv;
    public AuthorizeMessageAdapter mMessageAdapter;
    protected final int EMPTY = 0;
    private int pageIndex = EMPTY;
    private int mVisibleLastIndex = EMPTY;
    private RelativeLayout loadingLayout;
    private final int PAGESIZE = 50;
    private ArrayList<MessageModel> loadMessageResponseList;
    private int TWO = 2;
    protected MessageUiManager mMessageUiManager;
    public static final int MESSAGE_REQUEST_CODE = 1;
    private int mHandledItemIndex = 0;
    private final int mRefreshIndex = 0;
    private FrameLayout mBackRl;
    private boolean mFirstLoading = true;
    public static final int AUTO_REFRESH = 0;
    public static final int MANUAL_REFRESH = 1;
    public static final int MANUAL_LOADING = 2;
    public static int mLoadMode = 0;
    private TextView mEndTextTip;
    //是否处于编辑状态,alldevice 和deviceControl公用
    private static boolean isEditStatus = false;
    private RelativeLayout mBottomView;
    private String mLanguage;
    private final int mCompareMsgPoolId = -1;
    protected Dialog mMessageBoxDialog;
    private TextView mClearAllTv;
    private TextView mCLearTv;
    public static final float ALPHA_70 = 0.7f;
    public static final float ALPHA_100 = 1f;
    public ButtomType mButtomType;
    private LinearLayout mLoadingCacheLl;
    private LinearLayout mLoadingFailRl;
    private final int FRIST_LOADING_TITLE = 1;
    private final int FRIST_LOADING_FAIL_TITLE = 2;
    private final int NORMAL_TITLE = 3;
    private TextView mNoMessageTv;
    private ImageView mNoMessageIv;
    //如果是清空所有消息， pageindex 要重置为0
    private boolean isClearAll = false;
    private ImageView mLoadingImageView;
    private ImageView mFootViewLoadingIv;
    private Animation hyperspaceJumpAnimation;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeCellFragment.
     */
    public static MessagesFragment newInstance(Activity activity) {
        MessagesFragment fragment = new MessagesFragment();
        fragment.initActivity(activity);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "onCreate----");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "onCreateView----");
        if (mTopView == null) {
            mTopView = inflater.inflate(R.layout.fragment_message, container, false);
            initStatusBar(mTopView);
            initView();
            initData();
            initMessageUIManager();
            initListener();
            initItemClickListener();
        }


        return mTopView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getMessagesFromServer();
    }

    private void initView() {
        initDragDownManager(mTopView, R.id.filter_item_root);
        mLoadingRl = (RelativeLayout) mTopView.findViewById(R.id.message_loading_rl);
        mMessageLv = (PullToRefreshListView) mTopView.findViewById(R.id.fragment_message_listView);
        loadingLayout = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.auth_load_more_white, null);
        mFootViewLoadingIv = (ImageView) loadingLayout.findViewById(R.id.auth_load_more_iv);
        mBackRl = (FrameLayout) mTopView.findViewById(R.id.enroll_back_layout);
        mEndTextTip = (TextView) mTopView.findViewById(R.id.end_text_tip);
        mEndTextTip.setText(mParentActivity.getResources().getString(R.string.message_clear));
        mBottomView = (RelativeLayout) mTopView.findViewById(R.id.this_bottom_layout_id);
        mClearAllTv = (TextView) mTopView.findViewById(R.id.fragment_message_clear_all_tv);
        mCLearTv = (TextView) mTopView.findViewById(R.id.fragment_message_clear_tv);
        mMessageTitleTv = (TextView) mTopView.findViewById(R.id.message_title_text_id);
        mLoadingCacheLl = (LinearLayout) mTopView.findViewById(R.id.loading_cache_id);
        mLoadingFailRl = (LinearLayout) mTopView.findViewById(R.id.loading_fail_title);
        mNoMessageTv = (TextView) mTopView.findViewById(R.id.message_loading_tv);
        mNoMessageIv = (ImageView) mTopView.findViewById(R.id.message_loading_iv);
        mLoadingImageView = (ImageView) mTopView.findViewById(R.id.loading_img);
    }

    private void initData() {
        mBackRl.setVisibility(View.GONE);
        //load cach message Data
        hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                mParentActivity, R.anim.loading_animation);
        LinearInterpolator lir = new LinearInterpolator();
        hyperspaceJumpAnimation.setInterpolator(lir);
        mLoadingImageView.startAnimation(hyperspaceJumpAnimation);
        mButtomType = ButtomType.NORMAL;
        loadMessageResponseList = UserAllDataContainer.shareInstance().getLoadMessageResponseList();
        mMessageAdapter = new AuthorizeMessageAdapter(getContext(), mButtomType, loadMessageResponseList);
        mMessageLv.setAdapter(mMessageAdapter);
        mLanguage = AppConfig.shareInstance().getLanguage();
        setTitleView(FRIST_LOADING_TITLE);
        showNoMessageView();
    }

    protected void initMessageUIManager() {
        mMessageUiManager = new MessageUiManager();
        initManagerCallBack();
    }

    protected void initManagerCallBack() {
        mMessageUiManager.setErrorCallback(mErrorCallBack);
        mMessageUiManager.setSuccessCallback(mSuccessCallback);
    }

    protected MessageManager.SuccessCallback mSuccessCallback = new MessageManager.SuccessCallback() {
        @Override
        public void onSuccess(ResponseResult responseResult) {
            dealSuccessCallBack(responseResult);
        }
    };

    protected MessageManager.ErrorCallback mErrorCallBack = new MessageManager.ErrorCallback() {
        @Override
        public void onError(ResponseResult responseResult, int id) {
            dealErrorCallBack(responseResult, id);
        }
    };

    @Override
    protected void dealSuccessCallBack(ResponseResult responseResult) {
        switch (responseResult.getRequestId()) {
            case GET_MESSAGES_PER_PAGE:
                super.dealSuccessCallBack(responseResult); //after clear messages and get messages_success dismiss dialog
                mFirstLoading = false;
                setTitleView(NORMAL_TITLE);
                loadMessageResponseList = mMessageUiManager.getMessagesResponse(responseResult);
                mLoadMode = responseResult.getResponseData().getInt(GetAuthMessagesTask.ISREFLASH, AUTO_REFRESH);
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "loadMessageResponseList.size: " + loadMessageResponseList.size());
                loadUiDatas();
                mMessageLv.onRefreshComplete();
                break;
            case DELETE_MESSAGES:
                if (isClearAll) {
                    pageIndex = EMPTY;
                }
                getMessages(mRefreshIndex, (pageIndex + 1) * PAGESIZE, AUTO_REFRESH);
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "DELETE_MESSAGES success ");
                normalStatusView();
                break;
        }
    }

    @Override
    protected void dealErrorCallBack(ResponseResult responseResult, int id) {
        super.dealErrorCallBack(responseResult, id);
        switch (responseResult.getRequestId()) {
            case GET_MESSAGES_PER_PAGE:
                mMessageLv.onRefreshComplete();
                if (mFirstLoading) {
                    HplusNetworkErrorLayout mNetWorkErrorLayout = getNetWorkErrorLayout();
                    if (mNetWorkErrorLayout != null && mNetWorkErrorLayout.getVisibility() == View.VISIBLE) {
                        setTitleView(NORMAL_TITLE);
                    } else {
                        setTitleView(FRIST_LOADING_FAIL_TITLE);
                    }
                    mFootViewLoadingIv.clearAnimation();
                    mMessageLv.removeFooterView(loadingLayout);
                    mFirstLoading = false;
                    return;
                }
                mLoadMode = responseResult.getResponseData().getInt(GetMessagesPerPageTask.ISREFLASH, AUTO_REFRESH);
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "mLoadMode: " + mLoadMode);
                //断网加载更多时要加上footview
                if (mLoadMode != AUTO_REFRESH) {
                    if (mLoadMode == MANUAL_LOADING) {
                        addListViewFootView();
                    }
                    errorHandle(responseResult, getString(R.string.message_center_failed));
                }
                break;
            case DELETE_MESSAGES:
                errorHandle(responseResult, getString(R.string.message_clear_fail));
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "DELETE_MESSAGES fail ");
                //错误返回时，取消edit状态，变为normal状态
                normalStatusView();
                break;
        }
    }

    private void addListViewFootView() {
        if (mMessageLv.getFooterViewsCount() == EMPTY) {
            mFootViewLoadingIv.startAnimation(hyperspaceJumpAnimation);
            mMessageLv.addFooterView(loadingLayout);
        }
    }

    public void getMessagesFromServer() {
        if (mFirstLoading) {
            getMessages(pageIndex, PAGESIZE, MANUAL_LOADING);
        } else {
            getMessages(mRefreshIndex, (pageIndex + 1) * PAGESIZE, AUTO_REFRESH);
        }

    }

    private void getMessages(int index, int pagesize, int loadMode) {
        mMessageUiManager.getMessagesPerPage(index * pagesize + 1, pagesize, mLanguage, mCompareMsgPoolId, loadMode);
    }

    /*
        1: 第一次 loading..
        2: loading fail
        3: normal message
     */
    private void setTitleView(int titleType) {
        switch (titleType) {
            case FRIST_LOADING_TITLE:
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "FRIST_LOADING_TITLE");
                mMessageTitleTv.setVisibility(View.GONE);
                mLoadingCacheLl.setVisibility(View.VISIBLE);
                mLoadingFailRl.setVisibility(View.GONE);
                break;
            case FRIST_LOADING_FAIL_TITLE:
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "FRIST_LOADING_FAIL_TITLE");
                mMessageTitleTv.setVisibility(View.GONE);
                mLoadingCacheLl.setVisibility(View.GONE);
                mLoadingFailRl.setVisibility(View.VISIBLE);
                break;
            case NORMAL_TITLE:
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "NORMAL_TITLE");
                mMessageTitleTv.setVisibility(View.VISIBLE);
                mLoadingCacheLl.setVisibility(View.GONE);
                mLoadingFailRl.setVisibility(View.GONE);
                break;
        }
    }

    private void showNoMessageView() {
        if (mMessageAdapter.getCount() == EMPTY) {
            mLoadingRl.setVisibility(View.VISIBLE);
            setEndTextVisible(View.GONE);
            ((MainActivity) mParentActivity).clearRedDot();
            hideBottomView();
            ((MainActivity) mParentActivity).setMessageEditStatusFromIndiacator(false);
            if (!mFirstLoading) {
                mNoMessageTv.setText(getString(R.string.no_message));
                mNoMessageIv.setVisibility(View.VISIBLE);
            }
            pageIndex = EMPTY;
        } else {
            if (mFirstLoading) {
                addListViewFootView();
            }
            mLoadingRl.setVisibility(View.GONE);
            setEndTextVisible(View.VISIBLE);
        }
    }

    private void loadUiDatas() {
        if (mLoadMode == MANUAL_LOADING) {
            addListViewFootView();
        }
        if (loadMessageResponseList != null &&
                loadMessageResponseList.size() < PAGESIZE) {
            mFootViewLoadingIv.clearAnimation();
            mMessageLv.removeFooterView(loadingLayout);
        }
        if (pageIndex == EMPTY || mLoadMode != MANUAL_LOADING) {
            if (loadMessageResponseList != null &&
                    loadMessageResponseList.size() == PAGESIZE) {
                addListViewFootView();
            }
            mMessageAdapter.removeMessage(loadMessageResponseList);
        } else {
            mMessageUiManager.reflashGetAuthMessagesResponse(mMessageAdapter.getMessageModelArrayList(), loadMessageResponseList);
            mMessageAdapter.append(loadMessageResponseList);
        }
        showNoMessageView();
        loadMessageResponseList = null;
    }

    private void initListener() {

        mMessageLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && mVisibleLastIndex == mMessageAdapter.getCount()) {
                    LogUtil.log(LogUtil.LogLevel.INFO, TAG, "onScrollStateChanged");
                    pageIndex++;
                    mFootViewLoadingIv.clearAnimation();
                    mMessageLv.removeFooterView(loadingLayout);
                    getMessages(pageIndex, PAGESIZE, MANUAL_LOADING);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mVisibleLastIndex = firstVisibleItem + visibleItemCount - TWO;

            }
        });
        mEndTextTip.setOnClickListener(this);
        mClearAllTv.setOnClickListener(this);
        mCLearTv.setOnClickListener(this);
    }

    private void initItemClickListener() {
        mMessageLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageModel messageModel = mMessageAdapter.getItem(position - 1);
                if (messageModel == null) {
                    return;
                }
                if (mMessageAdapter.getmButtomType() == ButtomType.NORMAL) {
                    if (!mMessageUiManager.isSupportMessageType(messageModel.getmMessageCategory())) {
                        unSupportVersionUpdate();
                    } else {
                        goToMessageHandleActivity(position, messageModel);
                    }
                    //select mode
                } else {
                    messageModel.setIsSelected(!messageModel.isSelected());
                    ((MessageListItemView) view).initViewHolder(view, messageModel, mMessageAdapter.getmButtomType());
                    setClearBtnClickable(mMessageAdapter.isSelectOneDevice());
                }
            }
        });

        mMessageLv.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            public void onRefresh() {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "onRefresh---");
                getMessages(mRefreshIndex, (pageIndex + 1) * PAGESIZE, MANUAL_REFRESH);
            }
        });

    }

    private void goToMessageHandleActivity(int position, MessageModel messageModel) {
        mHandledItemIndex = position - 1;
        Intent intent = new Intent(mParentActivity, MessageHandleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("message", messageModel);
        intent.putExtras(bundle);
        startActivityForResult(intent, MESSAGE_REQUEST_CODE);
        mParentActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    private void unSupportVersionUpdate() {
        Intent intent = new Intent(mParentActivity, UpdateVersionMinderActivity.class);
        intent.putExtra(UpdateManager.UPDATE_TYPE, HPlusConstants.UNSUPPORT_MESSAGE_UPDATE);
        startActivity(intent);
        mParentActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    private void setClearBtnClickable(boolean clickable) {
        float alpha = clickable ? ALPHA_100 : ALPHA_70;
        mCLearTv.setClickable(clickable);
        mCLearTv.setAlpha(alpha);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (resultCode) {
            case Activity.RESULT_OK:
                dealActivityForResult(intent);
                break;
            case Activity.RESULT_FIRST_USER:
                if (intent != null) {
                    if (intent.getIntExtra(DashBoadConstant.ARG_HANDLE_MESSAGE, 1) == AuthorizeMessageCardView.ERRORCALLPUSH) {
                        MessageBox.createSimpleDialog(mParentActivity, null, getString(R.string.authorization_deleted), getString(R.string.ok), null);
                    }
                }
                //already read message
            case DashBoadConstant.RESULT_READ:
                dealWithReadMessageResult();
                break;
            default:
                break;
        }
    }

    private void dealWithReadMessageResult() {
        mMessageAdapter.changeMessageReadStatus(mHandledItemIndex);
    }

    private void dealActivityForResult(Intent intent) {
        int action = intent.getIntExtra(DashBoadConstant.ARG_HANDLE_MESSAGE, 1);
        mDropDownAnimationManager.showDragDownLayout(mMessageUiManager.remindNotificationAction(action), false);
        mMessageAdapter.removeMessage(mHandledItemIndex);
        showNoMessageView();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.end_text_tip) {
            setEndTip();
            //clear all
        } else if (v.getId() == R.id.fragment_message_clear_all_tv) {
            if (!isNetworkOff()) {
                clearMessages(R.string.message_clear_select_all_message, clearAllMessages);
            }
            //clear
        } else if (v.getId() == R.id.fragment_message_clear_tv) {
            if (!isNetworkOff()) {
                clearMessages(R.string.message_clear_select_message, clearMessages);
            }
        }
    }

    private void clearMessages(int stringId, MessageBox.MyOnClick onClick) {
        mMessageBoxDialog = MessageBox.createTwoButtonDialog(mParentActivity, null,
                getString(stringId), getString(R.string.cancel),
                null, getString(R.string.message_clear), onClick);
    }

    protected MessageBox.MyOnClick clearAllMessages = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "clear all messages");
            mDialog = LoadingProgressDialog.show(mParentActivity, getString(R.string.message_clearing));
            mMessageUiManager.deleteMessagesList(mMessageAdapter.getMessageModelArrayList(), true);
            isClearAll = true;
        }
    };

    protected MessageBox.MyOnClick clearMessages = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "clear selected messages");
            mDialog = LoadingProgressDialog.show(mParentActivity, getString(R.string.message_clearing));
            mMessageUiManager.deleteMessagesList(mMessageAdapter.getMessageModelArrayList(), false);
            isClearAll = false;
        }
    };

    protected void showBottomStatusView() {
        isEditStatus = true;
        showBottomView();
        //没有选中任何设备的时候底部按钮不可点击且设置为灰
        mMessageAdapter.setmButtomType(ButtomType.SELECT);
        mButtomType = ButtomType.SELECT;
        mMessageAdapter.notifyDataSetChanged();
        mEndTextTip.setText(getActivity().getResources().getString(R.string.all_device_title_cancel));
        ((MainActivity) mParentActivity).setMessageEditStatusFromIndiacator(true);
    }

    private void normalStatusView() {
        isEditStatus = false;
        hideBottomView();
        mMessageAdapter.setmButtomType(ButtomType.NORMAL);
        mButtomType = ButtomType.NORMAL;
        resetSelectAllBtn();
        mEndTextTip.setText(mParentActivity.getResources().getString(R.string.message_clear));
        mMessageAdapter.notifyDataSetChanged();
        //最底部view 和 选择的view互斥，一个显示一个隐藏
        ((MainActivity) mParentActivity).setMessageEditStatusFromIndiacator(false);
    }

    public void setEndTextVisible(int visible) {
        if (mEndTextTip != null) {
            mEndTextTip.setVisibility(visible);
        }
    }

    private void setEndTip() {
        if (isEditStatus) {
            normalStatusView();
        } else {
            showBottomStatusView();
        }
    }

    public boolean onkeyDownBack() {
        if (isEditStatus) {
            normalStatusView();

            return true;
        } else {
            return false;
        }
    }

    public void showBottomView() {
        Animation showFromBottom = AnimationUtils.loadAnimation(mParentActivity, R.anim.in_from_bottom);
        setClearBtnClickable(mMessageAdapter.isSelectOneDevice());
        showFromBottom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBottomView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBottomView.startAnimation(showFromBottom);

    }

    /**
     * 隐藏bottom view
     */
    public void hideBottomView() {
        if (mBottomView.getVisibility() != View.GONE) {
            Animation showFromBottom = AnimationUtils.loadAnimation(mParentActivity, R.anim.out_to_bottom);
            mBottomView.setVisibility(View.GONE);
            mBottomView.startAnimation(showFromBottom);
        }
    }

    private void resetSelectAllBtn() {
        mMessageAdapter.setAllDeviceSelectStatus(false);
    }

}