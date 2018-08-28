//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imkit.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import io.rong.common.RLog;
import io.rong.imkit.R.bool;
import io.rong.imkit.R.id;
import io.rong.imkit.R.integer;
import io.rong.imkit.R.layout;
import io.rong.imkit.R.string;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.SendImageManager;
import io.rong.imkit.model.ConversationInfo;
import io.rong.imkit.widget.InputView.IInputBoardListener;
import io.rong.imkit.widget.InputView.OnInfoButtonClick;
import io.rong.imlib.CustomServiceConfig;
import io.rong.imlib.ICustomServiceListener;
import io.rong.imlib.IRongCallback.ISendMessageCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.OperationCallback;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.CSGroupItem;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.CustomServiceMode;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.PublicServiceMenu.PublicServiceMenuItemType;
import io.rong.message.PublicServiceCommandMessage;
import io.rong.push.RongPushClient;

public class CustomFragment extends DispatchResultFragment implements OnScrollListener {
    private static final String TAG = "CustomFragment";
    CustomMessageListFragment mListFragment;
    CustomMessageInputFragment mInputFragment;
    ConversationType mConversationType;
    String mTargetId;
    private CSCustomServiceInfo mCustomUserInfo;
    ConversationInfo mCurrentConversationInfo;
    private OnInfoButtonClick onInfoButtonClick;
    private IInputBoardListener inputBoardListener;
    private boolean robotType = true;
    private int source = 0;
    private boolean resolved = true;
    private boolean committing = false;
    private long enterTime;
    private boolean evaluate = true;
    ICustomServiceListener customServiceListener = new ICustomServiceListener() {
        public void onSuccess(CustomServiceConfig config) {
            if(config.isBlack) {
                CustomFragment.this.onCustomServiceWarning(CustomFragment.this.getString(string.rc_blacklist_prompt), false);
            }

            if(config.robotSessionNoEva) {
                CustomFragment.this.evaluate = false;
                if(CustomFragment.this.mListFragment != null) {
                    CustomFragment.this.mListFragment.setNeedEvaluateForRobot(true);
                }
            }

        }

        public void onError(int code, String msg) {
            CustomFragment.this.onCustomServiceWarning(msg, false);
        }

        public void onModeChanged(CustomServiceMode mode) {
            CustomFragment.this.mInputFragment.setInputProviderType(mode);
            if(!mode.equals(CustomServiceMode.CUSTOM_SERVICE_MODE_HUMAN) && !mode.equals(CustomServiceMode.CUSTOM_SERVICE_MODE_HUMAN_FIRST)) {
                if(mode.equals(CustomServiceMode.CUSTOM_SERVICE_MODE_NO_SERVICE)) {
                    CustomFragment.this.evaluate = false;
                }
            } else {
                CustomFragment.this.robotType = false;
                CustomFragment.this.evaluate = true;
            }

            if(CustomFragment.this.mListFragment != null) {
                CustomFragment.this.mListFragment.setRobotMode(CustomFragment.this.robotType);
            }

        }

        public void onQuit(String msg) {
            if(!CustomFragment.this.committing) {
                CustomFragment.this.onCustomServiceWarning(msg, true);
            }

        }

        public void onPullEvaluation(String dialogId) {
            if(!CustomFragment.this.committing) {
                CustomFragment.this.onCustomServiceEvaluation(true, dialogId, CustomFragment.this.robotType, CustomFragment.this.evaluate);
            }

        }

        @Override
        public void onSelectGroup(List<CSGroupItem> list) {

        }
    };
    private String mRightImageUrl;
    private String mLeftImageUrl;

    public CustomFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RongPushClient.clearAllPushNotifications(RongContext.getInstance());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout.rc_fr_conversation, container, false);
        return view;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    public void onResume() {
        RongPushClient.clearAllPushNotifications(RongContext.getInstance());
        super.onResume();
    }

    public void setOnInfoButtonClick(OnInfoButtonClick onInfoButtonClick) {
        this.onInfoButtonClick = onInfoButtonClick;
        if(this.mInputFragment != null) {
            this.mInputFragment.setOnInfoButtonClick(onInfoButtonClick);
        }

    }

    public void setInputBoardListener(IInputBoardListener inputBoardListener) {
        this.inputBoardListener = inputBoardListener;
        if(this.mInputFragment != null) {
            this.mInputFragment.setInputBoardListener(inputBoardListener);
        }

    }

    protected void initFragment(Uri uri) {
        RLog.d("CustomFragment", "initFragment : " + uri);
        if(uri != null) {
            String typeStr = uri.getLastPathSegment().toUpperCase();
            this.mConversationType = ConversationType.valueOf(typeStr);
            this.mTargetId = uri.getQueryParameter("targetId");
            if(this.mConversationType.equals(ConversationType.CUSTOMER_SERVICE) && this.getActivity() != null && this.getActivity().getIntent() != null && this.getActivity().getIntent().getData() != null) {
                this.mCustomUserInfo = (CSCustomServiceInfo)this.getActivity().getIntent().getParcelableExtra("customServiceInfo");
            }

            this.mCurrentConversationInfo = ConversationInfo.obtain(this.mConversationType, this.mTargetId);
            RongContext.getInstance().registerConversationInfo(this.mCurrentConversationInfo);
            this.mListFragment = (CustomMessageListFragment)this.getChildFragmentManager().findFragmentById(android.R.id.list);
            this.mInputFragment = (CustomMessageInputFragment)this.getChildFragmentManager().findFragmentById(android.R.id.toggle);
            if(this.mListFragment == null) {
                this.mListFragment = new CustomMessageListFragment();
            }

            if(this.mInputFragment == null) {
                this.mInputFragment = new CustomMessageInputFragment();
            }

            if(this.mListFragment.getUri() == null) {
                this.mListFragment.setUri(uri);
            }

            if(this.mInputFragment.getUri() == null) {
                this.mInputFragment.setUri(uri);
            }

            this.mListFragment.setOnScrollListener(this);
            if(this.mConversationType.equals(ConversationType.CHATROOM)) {
                boolean msg = this.getActivity() != null && this.getActivity().getIntent().getBooleanExtra("createIfNotExist", true);
                int message = this.getResources().getInteger(integer.rc_chatroom_first_pull_message_count);
                if(msg) {
                    RongIMClient.getInstance().joinChatRoom(this.mTargetId, message, new OperationCallback() {
                        public void onSuccess() {
                            RLog.i("CustomFragment", "joinChatRoom onSuccess : " + CustomFragment.this.mTargetId);
                        }

                        public void onError(ErrorCode errorCode) {
                            RLog.e("CustomFragment", "joinChatRoom onError : " + errorCode);
                            CustomFragment.this.csWarning(CustomFragment.this.getString(string.rc_join_chatroom_failure));
                        }
                    });
                } else {
                    RongIMClient.getInstance().joinExistChatRoom(this.mTargetId, message, new OperationCallback() {
                        public void onSuccess() {
                            RLog.i("CustomFragment", "joinExistChatRoom onSuccess : " + CustomFragment.this.mTargetId);
                        }

                        public void onError(ErrorCode errorCode) {
                            RLog.e("CustomFragment", "joinExistChatRoom onError : " + errorCode);
                            CustomFragment.this.csWarning(CustomFragment.this.getString(string.rc_join_chatroom_failure));
                        }
                    });
                }
            } else if(this.mConversationType != ConversationType.APP_PUBLIC_SERVICE && this.mConversationType != ConversationType.PUBLIC_SERVICE) {
                if(this.mConversationType.equals(ConversationType.CUSTOMER_SERVICE)) {
                    this.enterTime = System.currentTimeMillis();
                    this.mInputFragment.setOnRobotSwitcherListener(new OnClickListener() {
                        public void onClick(View v) {
                            RongIMClient.getInstance().switchToHumanMode(CustomFragment.this.mTargetId);
                        }
                    });
                    RongIMClient.getInstance().startCustomService(this.mTargetId, this.customServiceListener, this.mCustomUserInfo);
                }
            } else {
                PublicServiceCommandMessage msg1 = new PublicServiceCommandMessage();
                msg1.setCommand(PublicServiceMenuItemType.Entry.getMessage());
                Message message1 = Message.obtain(this.mTargetId, this.mConversationType, msg1);
                RongIMClient.getInstance().sendMessage(message1, (String)null, (String)null, new ISendMessageCallback() {
                    public void onAttached(Message message) {
                    }

                    public void onSuccess(Message message) {
                    }

                    public void onError(Message message, ErrorCode errorCode) {
                    }
                });
            }
        }

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mInputFragment = (CustomMessageInputFragment)this.getChildFragmentManager().findFragmentById(android.R.id.toggle);
        if(this.mInputFragment != null) {
            this.mInputFragment.setOnInfoButtonClick(this.onInfoButtonClick);
            this.mInputFragment.setInputBoardListener(this.inputBoardListener);
        }

    }

    public void onDestroyView() {
        RongContext.getInstance().unregisterConversationInfo(this.mCurrentConversationInfo);
        super.onDestroyView();
    }

    public void onDestroy() {
        RongContext.getInstance().getEventBus().unregister(this);
        if(this.mConversationType != null) {
            if(this.mConversationType.equals(ConversationType.CHATROOM)) {
                SendImageManager.getInstance().cancelSendingImages(this.mConversationType, this.mTargetId);
            }

            RongContext.getInstance().executorBackground(new Runnable() {
                public void run() {
                    RongIM.getInstance().quitChatRoom(CustomFragment.this.mTargetId, new OperationCallback() {
                        public void onSuccess() {
                        }

                        public void onError(ErrorCode errorCode) {
                        }
                    });
                }
            });
            if(this.mConversationType.equals(ConversationType.CUSTOMER_SERVICE)) {
                boolean needToQuit = true;

                try {
                    needToQuit = RongContext.getInstance().getResources().getBoolean(bool.rc_stop_custom_service_when_quit);
                } catch (NotFoundException var3) {
                    var3.printStackTrace();
                }

                if(needToQuit) {
                    RongIMClient.getInstance().stopCustomService(this.mTargetId);
                }
            }
        }

        super.onDestroy();
    }

    public boolean handleMessage(android.os.Message msg) {
        return false;
    }

    private void csWarning(String msg) {
        if(this.getActivity() != null) {
            Builder builder = new Builder(this.getActivity());
            builder.setCancelable(false);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            Window window = alertDialog.getWindow();
            window.setContentView(layout.rc_cs_alert_warning);
            TextView tv = (TextView)window.findViewById(id.rc_cs_msg);
            tv.setText(msg);
            window.findViewById(id.rc_btn_ok).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    alertDialog.dismiss();
                    FragmentManager fm = CustomFragment.this.getChildFragmentManager();
                    if(fm.getBackStackEntryCount() > 0) {
                        fm.popBackStack();
                    } else {
                        CustomFragment.this.getActivity().finish();
                    }

                }
            });
        }
    }

    public void onCustomServiceWarning(String msg, final boolean evaluate) {
        if(this.getActivity() != null) {
            Builder builder = new Builder(this.getActivity());
            builder.setCancelable(false);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            Window window = alertDialog.getWindow();
            window.setContentView(layout.rc_cs_alert_warning);
            TextView tv = (TextView)window.findViewById(id.rc_cs_msg);
            tv.setText(msg);
            window.findViewById(id.rc_btn_ok).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    alertDialog.dismiss();
                    if(evaluate) {
                        CustomFragment.this.onCustomServiceEvaluation(false, "", CustomFragment.this.robotType, evaluate);
                    } else {
                        FragmentManager fm = CustomFragment.this.getChildFragmentManager();
                        if(fm.getBackStackEntryCount() > 0) {
                            fm.popBackStack();
                        } else {
                            CustomFragment.this.getActivity().finish();
                        }
                    }

                }
            });
        }
    }

    public boolean onCustomServiceEvaluation(boolean isPullEva, final String dialogId, final boolean robotType, boolean evaluate) {
        if(!evaluate) {
            return false;
        } else {
            long currentTime = System.currentTimeMillis();
            int interval = 60;

            try {
                interval = RongContext.getInstance().getResources().getInteger(integer.rc_custom_service_evaluation_interval);
            } catch (NotFoundException var14) {
                var14.printStackTrace();
            }

            if(currentTime - this.enterTime < (long)(interval * 1000) && !isPullEva) {
                return false;
            } else {
                this.committing = true;
                Builder builder = new Builder(this.getActivity());
                builder.setCancelable(false);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Window window = alertDialog.getWindow();
                final LinearLayout linearLayout;
                int i;
                View child;
                if(robotType) {
                    window.setContentView(layout.rc_cs_alert_robot_evaluation);
                    linearLayout = (LinearLayout)window.findViewById(id.rc_cs_yes_no);
                    if(this.resolved) {
                        linearLayout.getChildAt(0).setSelected(true);
                        linearLayout.getChildAt(1).setSelected(false);
                    } else {
                        linearLayout.getChildAt(0).setSelected(false);
                        linearLayout.getChildAt(1).setSelected(true);
                    }

                    for(i = 0; i < linearLayout.getChildCount(); ++i) {
                        child = linearLayout.getChildAt(i);
                        child.setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                v.setSelected(true);
                                int index = linearLayout.indexOfChild(v);
                                if(index == 0) {
                                    linearLayout.getChildAt(1).setSelected(false);
                                    CustomFragment.this.resolved = true;
                                } else {
                                    CustomFragment.this.resolved = false;
                                    linearLayout.getChildAt(0).setSelected(false);
                                }

                            }
                        });
                    }
                } else {
                    window.setContentView(layout.rc_cs_alert_human_evaluation);
                    linearLayout = (LinearLayout)window.findViewById(id.rc_cs_stars);

                    for(i = 0; i < linearLayout.getChildCount(); ++i) {
                        child = linearLayout.getChildAt(i);
                        if(i < this.source) {
                            child.setSelected(true);
                        }

                        child.setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                int index = linearLayout.indexOfChild(v);
                                int count = linearLayout.getChildCount();
                                CustomFragment.this.source = index + 1;
                                if(!v.isSelected()) {
                                    while(index >= 0) {
                                        linearLayout.getChildAt(index).setSelected(true);
                                        --index;
                                    }
                                } else {
                                    ++index;

                                    while(index < count) {
                                        linearLayout.getChildAt(index).setSelected(false);
                                        ++index;
                                    }
                                }

                            }
                        });
                    }
                }

                window.findViewById(id.rc_btn_cancel).setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        CustomFragment.this.committing = false;
                        alertDialog.dismiss();
                        FragmentManager fm = CustomFragment.this.getChildFragmentManager();
                        if(fm.getBackStackEntryCount() > 0) {
                            fm.popBackStack();
                        } else {
                            CustomFragment.this.getActivity().finish();
                        }

                    }
                });
                window.findViewById(id.rc_btn_ok).setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if(robotType) {
                            RongIMClient.getInstance().evaluateCustomService(CustomFragment.this.mTargetId, CustomFragment.this.resolved, "");
                        } else if(CustomFragment.this.source > 0) {
                            RongIMClient.getInstance().evaluateCustomService(CustomFragment.this.mTargetId, CustomFragment.this.source, (String)null, dialogId);
                        }

                        alertDialog.dismiss();
                        CustomFragment.this.committing = false;
                        FragmentManager fm = CustomFragment.this.getChildFragmentManager();
                        if(fm.getBackStackEntryCount() > 0) {
                            fm.popBackStack();
                        } else {
                            CustomFragment.this.getActivity().finish();
                        }

                    }
                });
                return true;
            }
        }
    }

    public void setRightImageUrl(String rightImageUrl) {
        this.mRightImageUrl = rightImageUrl;
        if(CustomFragment.this.mListFragment != null) {
            mListFragment.setRightImageUrl(rightImageUrl);
        }

    }

    public void setLeftImageUrl(String leftImageUrl) {
        this.mLeftImageUrl = leftImageUrl;
        if(CustomFragment.this.mListFragment != null) {
            mListFragment.setLeftImageUrl(leftImageUrl);
        }
    }

    public CustomMessageInputFragment getmInputFragment(){
        return mInputFragment;
    }

}
