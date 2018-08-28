package com.cn.xa.qyw.ui.main.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.xa.qyw.adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import io.rong.common.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.UriFragment;
import io.rong.imkit.model.ConversationKey;
import io.rong.imkit.model.Draft;
import io.rong.imkit.model.Event;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.utils.ConversationListUtils;
import io.rong.imkit.widget.ArraysDialogFragment;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.MentionedInfo;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.PublicServiceProfile;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ReadReceiptMessage;
import io.rong.message.RecallNotificationMessage;
import io.rong.message.VoiceMessage;
import io.rong.push.RongPushClient;

/**
 * Created by Administrator on 2016/5/26.
 */
public class MessageFragment extends UriFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, ConversationListAdapter.OnPortraitItemClick {
    private static String TAG = "MessageFragment";
    private View.OnClickListener mListener;
    private MessageAdapter mAdapter;
    private ListView mList;
    private LinearLayout mNotificationBar;
    private ImageView mNotificationBarImage;
    private TextView mNotificationBarText;
    private boolean isShowWithoutConnected = false;
    private ArrayList<Conversation.ConversationType> mSupportConversationList = new ArrayList();
    private ArrayList<Message> mMessageCache = new ArrayList();
    private Set<ConversationKey> mConversationKeyList;
    private RongIMClient.ResultCallback<List<Conversation>> mCallback = new RongIMClient.ResultCallback<List<Conversation>>() {
        public void onSuccess(List<Conversation> conversations) {
            RLog.d(MessageFragment.TAG, "MessageFragment initFragment onSuccess callback : list = " + (conversations != null ? Integer.valueOf(conversations.size()) : "null"));
            if(MessageFragment.this.mAdapter != null && MessageFragment.this.mAdapter.getCount() != 0) {
                MessageFragment.this.mAdapter.clear();
            }

            if(conversations != null && conversations.size() != 0) {
                if(MessageFragment.this.mAdapter == null) {
                    MessageFragment.this.mAdapter = new MessageAdapter(RongContext.getInstance());
                }

                MessageFragment.this.makeUiConversationList(conversations);
                if(MessageFragment.this.mList != null && MessageFragment.this.mList.getAdapter() != null) {
                    MessageFragment.this.mAdapter.notifyDataSetChanged();
                }

            } else {
                if(MessageFragment.this.mAdapter != null) {
                    MessageFragment.this.mAdapter.notifyDataSetChanged();
                }

            }
        }

        public void onError(RongIMClient.ErrorCode e) {
            RLog.d(MessageFragment.TAG, "MessageFragment initFragment onError callback, e=" + e);
            if(e.equals(RongIMClient.ErrorCode.IPC_DISCONNECT)) {
                MessageFragment.this.isShowWithoutConnected = true;
            }

        }
    };
    private String mContent;
    private TextView textView;

    public MessageFragment(){}

    public static MessageFragment getInstance() {
        return new MessageFragment();
    }

    public void setOnClickListener(View.OnClickListener listener){
        mListener = listener;

        if(textView!=null){
            textView.setOnClickListener(listener);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        RLog.d(TAG, "MessageFragment onCreate");
        super.onCreate(savedInstanceState);
        RongPushClient.clearAllPushNotifications(this.getActivity());
        this.mSupportConversationList.clear();
        this.mConversationKeyList = new HashSet();
        RongContext.getInstance().getEventBus().register(this);
    }

    public void onAttach(Activity activity) {
        RLog.d(TAG, "MessageFragment onAttach");
        super.onAttach(activity);
    }

    public void initFragment(Uri uri) {
        Conversation.ConversationType[] conversationType = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP, Conversation.ConversationType.DISCUSSION, Conversation.ConversationType.SYSTEM, Conversation.ConversationType.CUSTOMER_SERVICE, Conversation.ConversationType.CHATROOM, Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE};
        RLog.d(TAG, "MessageFragment initFragment");
        if(uri == null) {
            RongIM.getInstance().getConversationList(this.mCallback);
        } else {
            Conversation.ConversationType[] arr$ = conversationType;
            int len$ = conversationType.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Conversation.ConversationType type = arr$[i$];
                if(uri.getQueryParameter(type.getName()) != null) {
                    this.mSupportConversationList.add(type);
                    if("true".equals(uri.getQueryParameter(type.getName()))) {
                        RongContext.getInstance().setConversationGatherState(type.getName(), Boolean.valueOf(true));
                    } else if("false".equals(uri.getQueryParameter(type.getName()))) {
                        RongContext.getInstance().setConversationGatherState(type.getName(), Boolean.valueOf(false));
                    }
                }
            }

            if(RongIMClient.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                RLog.d(TAG, "RongCloud haven\'t been connected yet, so the conversation list display blank !!!");
                this.isShowWithoutConnected = true;
            }

            if(this.mSupportConversationList.size() > 0) {
                RongIM.getInstance().getConversationList(this.mCallback, (Conversation.ConversationType[])this.mSupportConversationList.toArray(new Conversation.ConversationType[this.mSupportConversationList.size()]));
            } else {
                RongIM.getInstance().getConversationList(this.mCallback);
            }

        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(TAG, "onCreateView");
        View view = inflater.inflate(io.rong.imkit.R.layout.rc_fr_conversationlist, container, false);
        this.mNotificationBar = (LinearLayout)this.findViewById(view, io.rong.imkit.R.id.rc_status_bar);
        this.mNotificationBar.setVisibility(View.GONE);
        this.mNotificationBarImage = (ImageView)this.findViewById(view, io.rong.imkit.R.id.rc_status_bar_image);
        this.mNotificationBarText = (TextView)this.findViewById(view, io.rong.imkit.R.id.rc_status_bar_text);
        this.mList = (ListView)this.findViewById(view, io.rong.imkit.R.id.rc_list);
        LinearLayout emptyView = (LinearLayout)this.findViewById(view, io.rong.imkit.R.id.rc_conversation_list_empty_layout);
        textView = (TextView)this.findViewById(view, io.rong.imkit.R.id.rc_empty_tv);
        RongIMClient.ConnectionStatusListener.ConnectionStatus status = RongIM.getInstance().getCurrentConnectionStatus();

        if(mListener!=null){
            textView.setOnClickListener(mListener);
        }

        this.setNotificationBarVisibility(status);
        this.mList.setEmptyView(emptyView);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        if(this.mAdapter == null) {
            this.mAdapter = new MessageAdapter(RongContext.getInstance());
        }

        this.mList.setAdapter(this.mAdapter);
        this.mList.setOnItemClickListener(this);
        this.mList.setOnItemLongClickListener(this);
        this.mAdapter.setOnPortraitItemClick(this);
        super.onViewCreated(view, savedInstanceState);
    }

    public void onResume() {
        super.onResume();
        if(!RongIMClient.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
            RLog.d(TAG, "RongCloud haven\'t been connected yet, so the conversation list display blank !!!");
            this.isShowWithoutConnected = true;
        } else {
            RLog.d(TAG, "onResume current connect status is:" + RongIM.getInstance().getCurrentConnectionStatus());
            RongPushClient.clearAllPushNotifications(this.getActivity());
            RongIMClient.ConnectionStatusListener.ConnectionStatus status = RongIM.getInstance().getCurrentConnectionStatus();
            this.setNotificationBarVisibility(status);
        }

        RongIMClient.ConnectionStatusListener.ConnectionStatus status = RongIM.getInstance().getCurrentConnectionStatus();
        if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            textView.setText(RongContext.getInstance().getResources().getString(io.rong.imkit.R.string.rc_conversation_list_not_connected));
        } else {
            textView.setText(RongContext.getInstance().getResources().getString(io.rong.imkit.R.string.rc_conversation_list_empty_prompt));
        }

    }

    private void setNotificationBarVisibility(RongIMClient.ConnectionStatusListener.ConnectionStatus status) {
        if(!this.getResources().getBoolean(io.rong.imkit.R.bool.rc_is_show_warning_notification)) {
            RLog.e(TAG, "rc_is_show_warning_notification is disabled.");
        } else {
            mContent = null;
            if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE)) {
                mContent = this.getResources().getString(io.rong.imkit.R.string.rc_notice_network_unavailable);
            } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
                mContent = this.getResources().getString(io.rong.imkit.R.string.rc_notice_tick);
            } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                this.mNotificationBar.setVisibility(View.GONE);
            } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                mContent = this.getResources().getString(io.rong.imkit.R.string.rc_notice_disconnect);
            } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING)) {
                mContent = this.getResources().getString(io.rong.imkit.R.string.rc_notice_connecting);
            }

            if(mContent != null) {
                if(this.mNotificationBar.getVisibility() == View.GONE) {
                    this.getHandler().postDelayed(new Runnable() {
                        public void run() {
                            if(!RongIMClient.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                                MessageFragment.this.mNotificationBar.setVisibility(View.VISIBLE);
                                MessageFragment.this.mNotificationBarText.setText(mContent);
                                if(RongIMClient.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING)) {
                                    MessageFragment.this.mNotificationBarImage.setImageDrawable(MessageFragment.this.getResources().getDrawable(io.rong.imkit.R.drawable.rc_notification_connecting_animated));
                                } else {
                                    MessageFragment.this.mNotificationBarImage.setImageDrawable(MessageFragment.this.getResources().getDrawable(io.rong.imkit.R.drawable.rc_notification_network_available));
                                }
                            }
                            mNotificationBar.setVisibility(View.GONE);

                        }
                    }, 4000L);
                } else {
                    this.mNotificationBarText.setText(mContent);
                    if(RongIMClient.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING)) {
                        this.mNotificationBarImage.setImageDrawable(this.getResources().getDrawable(io.rong.imkit.R.drawable.rc_notification_connecting_animated));
                    } else {
                        this.mNotificationBarImage.setImageDrawable(this.getResources().getDrawable(io.rong.imkit.R.drawable.rc_notification_network_available));
                    }
                }
            }

        }
        mNotificationBar.setVisibility(View.GONE);
    }

    public void onDestroy() {
        RLog.d(TAG, "onDestroy");
        RongContext.getInstance().getEventBus().unregister(this);
        this.getHandler().removeCallbacksAndMessages((Object)null);
        super.onDestroy();
    }

    public void onPause() {
        RLog.d(TAG, "onPause");
        super.onPause();
    }

    public boolean onBackPressed() {
        return false;
    }

    public void setAdapter(MessageAdapter adapter) {
        if(this.mAdapter != null) {
            this.mAdapter.clear();
        }

        this.mAdapter = adapter;
        if(this.mList != null) {
            this.mList.setAdapter(adapter);
        }

    }

    public MessageAdapter getAdapter() {
        return this.mAdapter;
    }

    public void onEventMainThread(Event.ConnectEvent event) {
        RLog.d(TAG, "onEventMainThread Event.ConnectEvent: isListRetrieved = " + this.isShowWithoutConnected);
        if(this.isShowWithoutConnected) {
            if(this.mSupportConversationList.size() > 0) {
                RongIM.getInstance().getConversationList(this.mCallback, (Conversation.ConversationType[])this.mSupportConversationList.toArray(new Conversation.ConversationType[this.mSupportConversationList.size()]));
            } else {
                RongIM.getInstance().getConversationList(this.mCallback);
            }

            LinearLayout emptyView = (LinearLayout)this.mList.getEmptyView();
            TextView textView = (TextView)emptyView.findViewById(io.rong.imkit.R.id.rc_empty_tv);
            textView.setText(RongContext.getInstance().getResources().getString(io.rong.imkit.R.string.rc_conversation_list_empty_prompt));
            this.isShowWithoutConnected = false;
        }
    }

    public void onEventMainThread(Event.SyncReadStatusEvent event) {
        this.refreshUnreadCount(event.getConversationType(), event.getTargetId());
        int originalIndex = this.mAdapter.findPosition(event.getConversationType(), event.getTargetId());
        UIConversation conversation = (UIConversation)this.mAdapter.getItem(originalIndex);
        conversation.setMentionedFlag(false);
        this.mAdapter.getView(originalIndex, this.mList.getChildAt(originalIndex - this.mList.getFirstVisiblePosition()), this.mList);
    }

    public void onEventMainThread(Event.ReadReceiptEvent event) {
        if(this.mAdapter == null) {
            RLog.d(TAG, "the conversation list adapter is null.");
        } else {
            int originalIndex = this.mAdapter.findPosition(event.getMessage().getConversationType(), event.getMessage().getTargetId());
            boolean gatherState = RongContext.getInstance().getConversationGatherState(event.getMessage().getConversationType().getName()).booleanValue();
            if(!gatherState && originalIndex >= 0) {
                UIConversation conversation = (UIConversation)this.mAdapter.getItem(originalIndex);
                ReadReceiptMessage content = (ReadReceiptMessage)event.getMessage().getContent();
                if(content.getLastMessageSendTime() >= conversation.getUIConversationTime() && conversation.getConversationSenderId().equals(RongIMClient.getInstance().getCurrentUserId())) {
                    conversation.setSentStatus(Message.SentStatus.READ);
                    this.mAdapter.getView(originalIndex, this.mList.getChildAt(originalIndex - this.mList.getFirstVisiblePosition()), this.mList);
                }
            }

        }
    }

    public void onEventMainThread(final Event.OnReceiveMessageEvent event) {
        RLog.d(TAG, "Receive MessageEvent: id=" + event.getMessage().getTargetId() + ", type=" + event.getMessage().getConversationType());
        if((this.mSupportConversationList.size() == 0 || this.mSupportConversationList.contains(event.getMessage().getConversationType())) && (this.mSupportConversationList.size() != 0 || event.getMessage().getConversationType() != Conversation.ConversationType.CHATROOM && event.getMessage().getConversationType() != Conversation.ConversationType.CUSTOMER_SERVICE)) {
            if(this.mAdapter == null) {
                RLog.d(TAG, "the conversation list adapter is null. Cache the received message firstly!!!");
                this.mMessageCache.add(event.getMessage());
            } else {
                int originalIndex = this.mAdapter.findPosition(event.getMessage().getConversationType(), event.getMessage().getTargetId());
                UIConversation uiConversation = this.makeUiConversation(event.getMessage(), originalIndex);
                int newPosition = ConversationListUtils.findPositionForNewConversation(uiConversation, this.mAdapter);
                if(originalIndex < 0) {
                    this.mAdapter.add(uiConversation, newPosition);
                } else if(originalIndex != newPosition) {
                    this.mAdapter.remove(originalIndex);
                    this.mAdapter.add(uiConversation, newPosition);
                }

                this.mAdapter.notifyDataSetChanged();
                if(event.getMessage().getMessageId() > 0) {
                    if(event.getLeft() > 0) {
                        this.mConversationKeyList.add(ConversationKey.obtain(event.getMessage().getTargetId(), event.getMessage().getConversationType()));
                    } else {
                        if(this.mConversationKeyList.size() > 0) {
                            Iterator i$ = this.mConversationKeyList.iterator();

                            while(i$.hasNext()) {
                                ConversationKey key = (ConversationKey)i$.next();
                                this.refreshUnreadCount(key.getType(), key.getTargetId());
                            }

                            this.mConversationKeyList.clear();
                        }

                        this.refreshUnreadCount(event.getMessage().getConversationType(), event.getMessage().getTargetId());
                    }
                }

                if(RongContext.getInstance().getConversationGatherState(event.getMessage().getConversationType().getName()).booleanValue()) {
                    RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                        public void onSuccess(List<Conversation> conversations) {
                            if(conversations != null && conversations.size() != 0) {
                                Iterator i$ = conversations.iterator();

                                while(i$.hasNext()) {
                                    Conversation conv = (Conversation)i$.next();
                                    if(conv.getConversationType().equals(event.getMessage().getConversationType()) && conv.getTargetId().equals(event.getMessage().getTargetId())) {
                                        int pos = MessageFragment.this.mAdapter.findPosition(conv.getConversationType(), conv.getTargetId());
                                        if(pos >= 0) {
                                            ((UIConversation)MessageFragment.this.mAdapter.getItem(pos)).setDraft(conv.getDraft());
                                            if(TextUtils.isEmpty(conv.getDraft())) {
                                                ((UIConversation)MessageFragment.this.mAdapter.getItem(pos)).setSentStatus((Message.SentStatus)null);
                                            } else {
                                                ((UIConversation)MessageFragment.this.mAdapter.getItem(pos)).setSentStatus(conv.getSentStatus());
                                            }

                                            MessageFragment.this.mAdapter.getView(pos, MessageFragment.this.mList.getChildAt(pos - MessageFragment.this.mList.getFirstVisiblePosition()), MessageFragment.this.mList);
                                        }
                                        break;
                                    }
                                }

                            }
                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    }, new Conversation.ConversationType[]{event.getMessage().getConversationType()});
                }

            }
        } else {
            RLog.e(TAG, "Not included in conversation list. Return directly!");
        }
    }

    public void onEventMainThread(Event.MessageRecallEvent event) {
        int count = this.mAdapter.getCount();

        for(int i = 0; i < count; ++i) {
            if(event.getMessageId() == ((UIConversation)this.mAdapter.getItem(i)).getLatestMessageId()) {
                boolean gatherState = ((UIConversation)this.mAdapter.getItem(i)).getConversationGatherState();
                if(gatherState) {
                    RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                        public void onSuccess(List<Conversation> conversationList) {
                            if(conversationList != null && conversationList.size() != 0) {
                                UIConversation uiConversation = MessageFragment.this.makeUIConversationFromList(conversationList);
                                int oldPos = MessageFragment.this.mAdapter.findPosition(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
                                if(oldPos >= 0) {
                                    MessageFragment.this.mAdapter.remove(oldPos);
                                }

                                int newIndex = ConversationListUtils.findPositionForNewConversation(uiConversation, MessageFragment.this.mAdapter);
                                MessageFragment.this.mAdapter.add(uiConversation, newIndex);
                                MessageFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    }, new Conversation.ConversationType[]{((UIConversation)this.mAdapter.getItem(i)).getConversationType()});
                } else {
                    RongIM.getInstance().getConversation(((UIConversation)this.mAdapter.getItem(i)).getConversationType(), ((UIConversation)this.mAdapter.getItem(i)).getConversationTargetId(), new RongIMClient.ResultCallback<Conversation>() {
                        public void onSuccess(Conversation conversation) {
                            if(conversation == null) {
                                RLog.d(MessageFragment.TAG, "onEventMainThread getConversation : onSuccess, conversation = null");
                            } else {
                                UIConversation temp = UIConversation.obtain(conversation, false);
                                int pos = MessageFragment.this.mAdapter.findPosition(conversation.getConversationType(), conversation.getTargetId());
                                if(pos >= 0) {
                                    MessageFragment.this.mAdapter.remove(pos);
                                }

                                int newPosition = ConversationListUtils.findPositionForNewConversation(temp, MessageFragment.this.mAdapter);
                                MessageFragment.this.mAdapter.add(temp, newPosition);
                                MessageFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    });
                }
                break;
            }
        }

    }

    public void onEventMainThread(Event.RemoteMessageRecallEvent event) {
        int count = this.mAdapter.getCount();

        for(int i = 0; i < count; ++i) {
            if(event.getMessageId() == ((UIConversation)this.mAdapter.getItem(i)).getLatestMessageId()) {
                boolean gatherState = ((UIConversation)this.mAdapter.getItem(i)).getConversationGatherState();
                if(gatherState) {
                    RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                        public void onSuccess(List<Conversation> conversationList) {
                            if(conversationList != null && conversationList.size() != 0) {
                                UIConversation uiConversation = MessageFragment.this.makeUIConversationFromList(conversationList);
                                int oldPos = MessageFragment.this.mAdapter.findPosition(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
                                if(oldPos >= 0) {
                                    MessageFragment.this.mAdapter.remove(oldPos);
                                }

                                int newIndex = ConversationListUtils.findPositionForNewConversation(uiConversation, MessageFragment.this.mAdapter);
                                MessageFragment.this.mAdapter.add(uiConversation, newIndex);
                                MessageFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    }, new Conversation.ConversationType[]{((UIConversation)this.mAdapter.getItem(i)).getConversationType()});
                } else {
                    RongIM.getInstance().getConversation(((UIConversation)this.mAdapter.getItem(i)).getConversationType(), ((UIConversation)this.mAdapter.getItem(i)).getConversationTargetId(), new RongIMClient.ResultCallback<Conversation>() {
                        public void onSuccess(Conversation conversation) {
                            if(conversation == null) {
                                RLog.d(MessageFragment.TAG, "onEventMainThread getConversation : onSuccess, conversation = null");
                            } else {
                                UIConversation temp = UIConversation.obtain(conversation, false);
                                int pos = MessageFragment.this.mAdapter.findPosition(conversation.getConversationType(), conversation.getTargetId());
                                if(pos >= 0) {
                                    MessageFragment.this.mAdapter.remove(pos);
                                }

                                int newPosition = ConversationListUtils.findPositionForNewConversation(temp, MessageFragment.this.mAdapter);
                                MessageFragment.this.mAdapter.add(temp, newPosition);
                                MessageFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    });
                }
                break;
            }
        }

    }

    public void onEventMainThread(Message message) {
        RLog.d(TAG, "onEventMainThread Receive Message: name=" + message.getObjectName() + ", type=" + message.getConversationType());
        if(this.mSupportConversationList.size() != 0 && !this.mSupportConversationList.contains(message.getConversationType()) || this.mSupportConversationList.size() == 0 && (message.getConversationType() == Conversation.ConversationType.CHATROOM || message.getConversationType() == Conversation.ConversationType.CUSTOMER_SERVICE)) {
            RLog.d(TAG, "onEventBackgroundThread Not included in conversation list. Return directly!");
        } else {
            int originalIndex = this.mAdapter.findPosition(message.getConversationType(), message.getTargetId());
            UIConversation uiConversation = this.makeUiConversation(message, originalIndex);
            int newPosition = ConversationListUtils.findPositionForNewConversation(uiConversation, this.mAdapter);
            if(originalIndex >= 0) {
                if(newPosition == originalIndex) {
                    this.mAdapter.getView(newPosition, this.mList.getChildAt(newPosition - this.mList.getFirstVisiblePosition()), this.mList);
                } else {
                    this.mAdapter.remove(originalIndex);
                    this.mAdapter.add(uiConversation, newPosition);
                    this.mAdapter.notifyDataSetChanged();
                }
            } else {
                this.mAdapter.add(uiConversation, newPosition);
                this.mAdapter.notifyDataSetChanged();
            }

        }
    }

    public void onEventMainThread(MessageContent content) {
        RLog.d(TAG, "onEventMainThread: MessageContent");

        for(int index = 0; index < this.mAdapter.getCount(); ++index) {
            UIConversation tempUIConversation = (UIConversation)this.mAdapter.getItem(index);
            if(content != null && tempUIConversation.getMessageContent() != null && tempUIConversation.getMessageContent() == content) {
                tempUIConversation.setMessageContent(content);
                tempUIConversation.setConversationContent(tempUIConversation.buildConversationContent(tempUIConversation));
                if(index >= this.mList.getFirstVisiblePosition()) {
                    this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition()), this.mList);
                }
            } else {
                RLog.e(TAG, "onEventMainThread MessageContent is null");
            }
        }

    }

    public void onEventMainThread(RongIMClient.ConnectionStatusListener.ConnectionStatus status) {
        RLog.d(TAG, "ConnectionStatus, " + status.toString());
        this.setNotificationBarVisibility(status);
        if(this.isShowWithoutConnected && status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
            this.isShowWithoutConnected = false;
        }

    }

    public void onEventMainThread(Event.CreateDiscussionEvent createDiscussionEvent) {
        RLog.d(TAG, "onEventBackgroundThread: createDiscussionEvent");
        UIConversation conversation = new UIConversation();
        conversation.setConversationType(Conversation.ConversationType.DISCUSSION);
        if(createDiscussionEvent.getDiscussionName() != null) {
            conversation.setUIConversationTitle(createDiscussionEvent.getDiscussionName());
        } else {
            conversation.setUIConversationTitle("");
        }

        conversation.setConversationTargetId(createDiscussionEvent.getDiscussionId());
        conversation.setUIConversationTime(System.currentTimeMillis());
        boolean isGather = RongContext.getInstance().getConversationGatherState(Conversation.ConversationType.DISCUSSION.getName()).booleanValue();
        conversation.setConversationGatherState(isGather);
        if(isGather) {
            String gatherPosition = RongContext.getInstance().getGatheredConversationTitle(conversation.getConversationType());
            conversation.setUIConversationTitle(gatherPosition);
        }

        int gatherPosition1 = this.mAdapter.findGatherPosition(Conversation.ConversationType.DISCUSSION);
        if(gatherPosition1 == -1) {
            this.mAdapter.add(conversation, ConversationListUtils.findPositionForNewConversation(conversation, this.mAdapter));
            this.mAdapter.notifyDataSetChanged();
        }

    }

    public void onEventMainThread(Draft draft) {
        if(draft != null) {
            Conversation.ConversationType curType = Conversation.ConversationType.setValue(draft.getType().intValue());
            if(curType != null && this.mSupportConversationList.contains(curType)) {
                RLog.i(TAG, "Draft ConversationType : " + curType.getName());
                int position = this.mAdapter.findPosition(curType, draft.getId());
                UIConversation temp;
                if(position >= 0) {
                    temp = (UIConversation)this.mAdapter.getItem(position);
                    if(temp.getConversationTargetId().equals(draft.getId())) {
                        temp.setDraft(draft.getContent());
                        if(!TextUtils.isEmpty(draft.getContent())) {
                            temp.setSentStatus((Message.SentStatus)null);
                        }

                        this.mAdapter.getView(position, this.mList.getChildAt(position - this.mList.getFirstVisiblePosition()), this.mList);
                    }
                } else if(!TextUtils.isEmpty(draft.getContent())) {
                    temp = new UIConversation();
                    temp.setConversationType(Conversation.ConversationType.setValue(draft.getType().intValue()));
                    temp.setConversationTargetId(draft.getId());
                    UserInfo curUserInfo = RongContext.getInstance().getUserInfoFromCache(draft.getId());
                    if(curUserInfo != null) {
                        temp.setUIConversationTitle(curUserInfo.getName());
                        if(curUserInfo.getPortraitUri() != null) {
                            temp.setIconUrl(curUserInfo.getPortraitUri());
                        }
                    }

                    temp.setUIConversationTime(System.currentTimeMillis());
                    temp.setConversationSenderId(RongIMClient.getInstance().getCurrentUserId());
                    temp.setDraft(draft.getContent());
                    int newPosition = ConversationListUtils.findPositionForNewConversation(temp, this.mAdapter);
                    if(newPosition >= 0) {
                        this.mAdapter.add(temp, newPosition);
                        this.mAdapter.notifyDataSetChanged();
                    }
                }

            } else {
                RLog.w(TAG, curType + " should not show in conversation list.");
            }
        }
    }

    public void onEventMainThread(Group groupInfo) {
        int count = this.mAdapter.getCount();
        RLog.d(TAG, "onEventMainThread Group: name=" + groupInfo.getName() + ", id=" + groupInfo.getId());
        if(groupInfo.getName() != null) {
            for(int i = 0; i < count; ++i) {
                UIConversation item = (UIConversation)this.mAdapter.getItem(i);
                if(item != null && item.getConversationType().equals(Conversation.ConversationType.GROUP) && item.getConversationTargetId().equals(groupInfo.getId())) {
                    boolean gatherState = RongContext.getInstance().getConversationGatherState(item.getConversationType().getName()).booleanValue();
                    if(gatherState) {
                        SpannableStringBuilder builder = new SpannableStringBuilder();
                        Spannable messageData = RongContext.getInstance().getMessageTemplate(item.getMessageContent().getClass()).getContentSummary(item.getMessageContent());
                        if(item.getMessageContent() instanceof VoiceMessage) {
                            boolean isListened = RongIM.getInstance().getConversation(item.getConversationType(), item.getConversationTargetId()).getReceivedStatus().isListened();
                            if(!item.getConversationSenderId().equals(RongIMClient.getInstance().getCurrentUserId()) && !isListened) {
                                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_voice_color)), 0, messageData.length(), 33);
                            } else {
                                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_text_color_secondary)), 0, messageData.length(), 33);
                            }
                        }

                        builder.append(groupInfo.getName()).append(" : ").append(messageData);
                        item.setConversationContent(builder);
                        if(groupInfo.getPortraitUri() != null) {
                            item.setIconUrl(groupInfo.getPortraitUri());
                        }
                    } else {
                        item.setUIConversationTitle(groupInfo.getName());
                        if(groupInfo.getPortraitUri() != null) {
                            item.setIconUrl(groupInfo.getPortraitUri());
                        }
                    }

                    this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                }
            }

        }
    }

    public void onEventMainThread(Discussion discussion) {
        int count = this.mAdapter.getCount();
        RLog.d(TAG, "onEventMainThread Discussion: name=" + discussion.getName() + ", id=" + discussion.getId());

        for(int i = 0; i < count; ++i) {
            UIConversation item = (UIConversation)this.mAdapter.getItem(i);
            if(item != null && item.getConversationType().equals(Conversation.ConversationType.DISCUSSION) && item.getConversationTargetId().equals(discussion.getId())) {
                boolean gatherState = RongContext.getInstance().getConversationGatherState(item.getConversationType().getName()).booleanValue();
                if(gatherState) {
                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    Spannable messageData = RongContext.getInstance().getMessageTemplate(item.getMessageContent().getClass()).getContentSummary(item.getMessageContent());
                    if(messageData == null) {
                        builder.append(discussion.getName());
                    } else {
                        if(item.getMessageContent() instanceof VoiceMessage) {
                            boolean isListened = RongIM.getInstance().getConversation(item.getConversationType(), item.getConversationTargetId()).getReceivedStatus().isListened();
                            if(!item.getConversationSenderId().equals(RongIMClient.getInstance().getCurrentUserId()) && !isListened) {
                                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_voice_color)), 0, messageData.length(), 33);
                            } else {
                                messageData.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_text_color_secondary)), 0, messageData.length(), 33);
                            }
                        }

                        builder.append(discussion.getName()).append(" : ").append(messageData);
                    }

                    item.setConversationContent(builder);
                } else {
                    item.setUIConversationTitle(discussion.getName());
                }

                this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
            }
        }

    }

    public void onEventMainThread(Event.GroupUserInfoEvent event) {
        int count = this.mAdapter.getCount();
        GroupUserInfo userInfo = event.getUserInfo();
        if(userInfo != null && userInfo.getNickname() != null) {
            for(int i = 0; i < count; ++i) {
                UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(i);
                Conversation.ConversationType type = uiConversation.getConversationType();
                boolean gatherState = RongContext.getInstance().getConversationGatherState(uiConversation.getConversationType().getName()).booleanValue();
                boolean isShowName;
                if(uiConversation.getMessageContent() == null) {
                    isShowName = false;
                } else {
                    ProviderTag messageData = RongContext.getInstance().getMessageProviderTag(uiConversation.getMessageContent().getClass());
                    isShowName = messageData != null?messageData.showSummaryWithName():false;
                }

                if(!gatherState && isShowName && type.equals(Conversation.ConversationType.GROUP) && uiConversation.getConversationSenderId().equals(userInfo.getUserId())) {
                    Spannable var12 = RongContext.getInstance().getMessageTemplate(uiConversation.getMessageContent().getClass()).getContentSummary(uiConversation.getMessageContent());
                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    if(uiConversation.getMessageContent() instanceof VoiceMessage) {
                        boolean isListened = RongIM.getInstance().getConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId()).getReceivedStatus().isListened();
                        if(!uiConversation.getConversationSenderId().equals(RongIMClient.getInstance().getCurrentUserId()) && !isListened) {
                            var12.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_voice_color)), 0, var12.length(), 33);
                        } else {
                            var12.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_text_color_secondary)), 0, var12.length(), 33);
                        }
                    }

                    if(uiConversation.getConversationTargetId().equals(userInfo.getGroupId())) {
                        uiConversation.addNickname(userInfo.getUserId());
                        builder.append(userInfo.getNickname()).append(" : ").append(var12);
                        uiConversation.setConversationContent(builder);
                    }

                    this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                }
            }

        }
    }

    public void onEventMainThread(UserInfo userInfo) {
        int count = this.mAdapter.getCount();
        if(userInfo.getName() != null) {
            for(int i = 0; i < count; ++i) {
                UIConversation temp = (UIConversation)this.mAdapter.getItem(i);
                String type = temp.getConversationType().getName();
                boolean gatherState = RongContext.getInstance().getConversationGatherState(temp.getConversationType().getName()).booleanValue();
                if(!temp.hasNickname(userInfo.getUserId())) {
                    boolean isShowName;
                    if(temp.getMessageContent() != null && !userInfo.getUserId().equals(RongIM.getInstance().getCurrentUserId())) {
                        ProviderTag messageData = RongContext.getInstance().getMessageProviderTag(temp.getMessageContent().getClass());
                        isShowName = messageData != null?messageData.showSummaryWithName():false;
                    } else {
                        isShowName = false;
                    }

                    SpannableStringBuilder builder;
                    Spannable var11;
                    if(temp.getMessageContent() instanceof RecallNotificationMessage && temp.getConversationSenderId().equals(userInfo.getUserId())) {
                        var11 = RongContext.getInstance().getMessageTemplate(temp.getMessageContent().getClass()).getContentSummary(temp.getMessageContent());
                        builder = new SpannableStringBuilder();
                        builder.append(var11);
                        temp.setConversationContent(builder);
                        this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                    }

                    boolean isListened;
                    if(!gatherState && isShowName && (type.equals("group") || type.equals("discussion")) && temp.getConversationSenderId().equals(userInfo.getUserId())) {
                        var11 = RongContext.getInstance().getMessageTemplate(temp.getMessageContent().getClass()).getContentSummary(temp.getMessageContent());
                        builder = new SpannableStringBuilder();
                        if(temp.getMessageContent() instanceof VoiceMessage) {
                            isListened = RongIM.getInstance().getConversation(temp.getConversationType(), temp.getConversationTargetId()).getReceivedStatus().isListened();
                            if(!temp.getConversationSenderId().equals(RongIMClient.getInstance().getCurrentUserId()) && !isListened) {
                                var11.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_voice_color)), 0, var11.length(), 33);
                            } else {
                                var11.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_text_color_secondary)), 0, var11.length(), 33);
                            }
                        }

                        builder.append(userInfo.getName()).append(" : ").append(var11);
                        temp.setConversationContent(builder);
                        this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                    } else if((type.equals("private") || type.equals("system") || type.equals("customer_service")) && temp.getConversationTargetId().equals(userInfo.getUserId())) {
                        if(!gatherState) {
                            temp.setUIConversationTitle(userInfo.getName());
                            temp.setIconUrl(userInfo.getPortraitUri());
                        } else if(gatherState && isShowName) {
                            var11 = RongContext.getInstance().getMessageTemplate(temp.getMessageContent().getClass()).getContentSummary(temp.getMessageContent());
                            builder = new SpannableStringBuilder();
                            if(var11 == null) {
                                builder.append(userInfo.getName());
                            } else {
                                if(temp.getMessageContent() instanceof VoiceMessage) {
                                    isListened = RongIM.getInstance().getConversation(temp.getConversationType(), temp.getConversationTargetId()).getReceivedStatus().isListened();
                                    if(!temp.getConversationSenderId().equals(RongIMClient.getInstance().getCurrentUserId()) && !isListened) {
                                        var11.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_voice_color)), 0, var11.length(), 33);
                                    } else {
                                        var11.setSpan(new ForegroundColorSpan(RongContext.getInstance().getResources().getColor(io.rong.imkit.R.color.rc_text_color_secondary)), 0, var11.length(), 33);
                                    }
                                }

                                builder.append(userInfo.getName()).append(" : ").append(var11);
                            }

                            temp.setConversationContent(builder);
                        }

                        this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                    }
                }
            }

        }
    }

    public void onEventMainThread(PublicServiceProfile accountInfo) {
        int count = this.mAdapter.getCount();
        boolean gatherState = RongContext.getInstance().getConversationGatherState(accountInfo.getConversationType().getName()).booleanValue();

        for(int i = 0; i < count; ++i) {
            if(((UIConversation)this.mAdapter.getItem(i)).getConversationType().equals(accountInfo.getConversationType()) && ((UIConversation)this.mAdapter.getItem(i)).getConversationTargetId().equals(accountInfo.getTargetId()) && !gatherState) {
                ((UIConversation)this.mAdapter.getItem(i)).setUIConversationTitle(accountInfo.getName());
                ((UIConversation)this.mAdapter.getItem(i)).setIconUrl(accountInfo.getPortraitUri());
                this.mAdapter.getView(i, this.mList.getChildAt(i - this.mList.getFirstVisiblePosition()), this.mList);
                break;
            }
        }

    }

    public void onEventMainThread(Event.PublicServiceFollowableEvent event) {
        if(event != null && !event.isFollow()) {
            int originalIndex = this.mAdapter.findPosition(event.getConversationType(), event.getTargetId());
            if(originalIndex >= 0) {
                this.mAdapter.remove(originalIndex);
                this.mAdapter.notifyDataSetChanged();
            }
        }

    }

    public void onEventMainThread(final Event.ConversationUnreadEvent unreadEvent) {
        int targetIndex = this.mAdapter.findPosition(unreadEvent.getType(), unreadEvent.getTargetId());
        if(targetIndex >= 0) {
            UIConversation temp = (UIConversation)this.mAdapter.getItem(targetIndex);
            boolean gatherState = temp.getConversationGatherState();
            if(gatherState) {
                RongIM.getInstance().getUnreadCount(new RongIMClient.ResultCallback<Integer>() {
                    public void onSuccess(Integer count) {
                        int pos = MessageFragment.this.mAdapter.findPosition(unreadEvent.getType(), unreadEvent.getTargetId());
                        if(pos >= 0) {
                            ((UIConversation)MessageFragment.this.mAdapter.getItem(pos)).setUnReadMessageCount(count.intValue());
                            MessageFragment.this.mAdapter.getView(pos, MessageFragment.this.mList.getChildAt(pos - MessageFragment.this.mList.getFirstVisiblePosition()), MessageFragment.this.mList);
                        }

                    }

                    public void onError(RongIMClient.ErrorCode e) {
                        System.err.print("Throw exception when get unread message count from ipc remote side!");
                    }
                }, new Conversation.ConversationType[]{unreadEvent.getType()});
                RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                    public void onSuccess(List<Conversation> conversations) {
                        boolean mentionedFlag = false;
                        Iterator pos = conversations.iterator();

                        while(pos.hasNext()) {
                            Conversation conversation = (Conversation)pos.next();
                            if(conversation.getMentionedCount() > 0) {
                                mentionedFlag = true;
                                break;
                            }
                        }

                        int pos1 = MessageFragment.this.mAdapter.findPosition(unreadEvent.getType(), unreadEvent.getTargetId());
                        if(pos1 >= 0) {
                            ((UIConversation)MessageFragment.this.mAdapter.getItem(pos1)).setMentionedFlag(mentionedFlag);
                            MessageFragment.this.mAdapter.getView(pos1, MessageFragment.this.mList.getChildAt(pos1 - MessageFragment.this.mList.getFirstVisiblePosition()), MessageFragment.this.mList);
                        }

                    }

                    public void onError(RongIMClient.ErrorCode e) {
                    }
                }, new Conversation.ConversationType[]{unreadEvent.getType()});
            } else {
                temp.setUnReadMessageCount(0);
                temp.setMentionedFlag(false);
                RLog.d(TAG, "onEventMainThread ConversationUnreadEvent: set unRead count to be 0");
                this.mAdapter.getView(targetIndex, this.mList.getChildAt(targetIndex - this.mList.getFirstVisiblePosition()), this.mList);
            }
        }

    }

    public void onEventMainThread(final Event.ConversationTopEvent setTopEvent) throws IllegalAccessException {
        int originalIndex = this.mAdapter.findPosition(setTopEvent.getConversationType(), setTopEvent.getTargetId());
        if(originalIndex >= 0) {
            UIConversation temp = (UIConversation)this.mAdapter.getItem(originalIndex);
            boolean originalValue = temp.isTop();
            if(originalValue != setTopEvent.isTop()) {
                if(temp.getConversationGatherState()) {
                    RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                        public void onSuccess(List<Conversation> conversations) {
                            if(conversations != null && conversations.size() != 0) {
                                UIConversation newConversation = MessageFragment.this.makeUIConversationFromList(conversations);
                                int pos = MessageFragment.this.mAdapter.findPosition(setTopEvent.getConversationType(), setTopEvent.getTargetId());
                                if(pos >= 0) {
                                    MessageFragment.this.mAdapter.remove(pos);
                                }

                                int newIndex = ConversationListUtils.findPositionForNewConversation(newConversation, MessageFragment.this.mAdapter);
                                MessageFragment.this.mAdapter.add(newConversation, newIndex);
                                MessageFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    }, new Conversation.ConversationType[]{temp.getConversationType()});
                } else {
                    int newIndex;
                    if(originalValue) {
                        temp.setTop(false);
                        newIndex = ConversationListUtils.findPositionForCancleTop(originalIndex, this.mAdapter);
                    } else {
                        temp.setTop(true);
                        newIndex = ConversationListUtils.findPositionForSetTop(temp, this.mAdapter);
                    }

                    if(originalIndex == newIndex) {
                        this.mAdapter.getView(newIndex, this.mList.getChildAt(newIndex - this.mList.getFirstVisiblePosition()), this.mList);
                    } else {
                        this.mAdapter.remove(originalIndex);
                        this.mAdapter.add(temp, newIndex);
                        this.mAdapter.notifyDataSetChanged();
                    }
                }

            }
        } else {
            throw new IllegalAccessException("the item has already been deleted!");
        }
    }

    public void onEventMainThread(final Event.ConversationRemoveEvent removeEvent) {
        int removedIndex = this.mAdapter.findPosition(removeEvent.getType(), removeEvent.getTargetId());
        boolean gatherState = RongContext.getInstance().getConversationGatherState(removeEvent.getType().getName()).booleanValue();
        if(!gatherState) {
            if(removedIndex >= 0) {
                this.mAdapter.remove(removedIndex);
                this.mAdapter.notifyDataSetChanged();
            }
        } else if(removedIndex >= 0) {
            RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                public void onSuccess(List<Conversation> conversationList) {
                    int oldPos = MessageFragment.this.mAdapter.findPosition(removeEvent.getType(), removeEvent.getTargetId());
                    if(conversationList != null && conversationList.size() != 0) {
                        UIConversation newConversation = MessageFragment.this.makeUIConversationFromList(conversationList);
                        if(oldPos >= 0) {
                            MessageFragment.this.mAdapter.remove(oldPos);
                        }

                        int newIndex = ConversationListUtils.findPositionForNewConversation(newConversation, MessageFragment.this.mAdapter);
                        MessageFragment.this.mAdapter.add(newConversation, newIndex);
                        MessageFragment.this.mAdapter.notifyDataSetChanged();
                    } else {
                        if(oldPos >= 0) {
                            MessageFragment.this.mAdapter.remove(oldPos);
                        }

                        MessageFragment.this.mAdapter.notifyDataSetChanged();
                    }
                }

                public void onError(RongIMClient.ErrorCode e) {
                }
            }, new Conversation.ConversationType[]{removeEvent.getType()});
        }

    }

    public void onEventMainThread(Event.ClearConversationEvent clearConversationEvent) {
        List typeList = clearConversationEvent.getTypes();

        for(int i = this.mAdapter.getCount() - 1; i >= 0; --i) {
            if(typeList.indexOf(((UIConversation)this.mAdapter.getItem(i)).getConversationType()) >= 0) {
                this.mAdapter.remove(i);
            }
        }

        this.mAdapter.notifyDataSetChanged();
    }

    public void onEventMainThread(Event.MessageDeleteEvent event) {
        int count = this.mAdapter.getCount();

        for(int i = 0; i < count; ++i) {
            if(event.getMessageIds().contains(Integer.valueOf(((UIConversation)this.mAdapter.getItem(i)).getLatestMessageId()))) {
                boolean gatherState = ((UIConversation)this.mAdapter.getItem(i)).getConversationGatherState();
                if(gatherState) {
                    RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                        public void onSuccess(List<Conversation> conversationList) {
                            if(conversationList != null && conversationList.size() != 0) {
                                UIConversation uiConversation = MessageFragment.this.makeUIConversationFromList(conversationList);
                                int oldPos = MessageFragment.this.mAdapter.findPosition(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
                                if(oldPos >= 0) {
                                    MessageFragment.this.mAdapter.remove(oldPos);
                                }

                                int newIndex = ConversationListUtils.findPositionForNewConversation(uiConversation, MessageFragment.this.mAdapter);
                                MessageFragment.this.mAdapter.add(uiConversation, newIndex);
                                MessageFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    }, new Conversation.ConversationType[]{((UIConversation)this.mAdapter.getItem(i)).getConversationType()});
                } else {
                    RongIM.getInstance().getConversation(((UIConversation)this.mAdapter.getItem(i)).getConversationType(), ((UIConversation)this.mAdapter.getItem(i)).getConversationTargetId(), new RongIMClient.ResultCallback<Conversation>() {
                        public void onSuccess(Conversation conversation) {
                            if(conversation == null) {
                                RLog.d(MessageFragment.TAG, "onEventMainThread getConversation : onSuccess, conversation = null");
                            } else {
                                UIConversation temp = UIConversation.obtain(conversation, false);
                                int pos = MessageFragment.this.mAdapter.findPosition(conversation.getConversationType(), conversation.getTargetId());
                                if(pos >= 0) {
                                    MessageFragment.this.mAdapter.remove(pos);
                                }

                                int newPosition = ConversationListUtils.findPositionForNewConversation(temp, MessageFragment.this.mAdapter);
                                MessageFragment.this.mAdapter.add(temp, newPosition);
                                MessageFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    });
                }
                break;
            }
        }

    }

    public void onEventMainThread(Event.ConversationNotificationEvent notificationEvent) {
        int originalIndex = this.mAdapter.findPosition(notificationEvent.getConversationType(), notificationEvent.getTargetId());
        if(originalIndex >= 0) {
            this.mAdapter.getView(originalIndex, this.mList.getChildAt(originalIndex - this.mList.getFirstVisiblePosition()), this.mList);
        }

    }

    public void onEventMainThread(Event.MessagesClearEvent clearMessagesEvent) {
        int originalIndex = this.mAdapter.findPosition(clearMessagesEvent.getType(), clearMessagesEvent.getTargetId());
        if(originalIndex >= 0) {
            boolean gatherState = RongContext.getInstance().getConversationGatherState(clearMessagesEvent.getType().getName()).booleanValue();
            if(gatherState) {
                RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                    public void onSuccess(List<Conversation> conversationList) {
                        if(conversationList != null && conversationList.size() != 0) {
                            UIConversation uiConversation = MessageFragment.this.makeUIConversationFromList(conversationList);
                            int pos = MessageFragment.this.mAdapter.findPosition(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
                            if(pos >= 0) {
                                MessageFragment.this.mAdapter.remove(pos);
                            }

                            int newIndex = ConversationListUtils.findPositionForNewConversation(uiConversation, MessageFragment.this.mAdapter);
                            MessageFragment.this.mAdapter.add(uiConversation, newIndex);
                            MessageFragment.this.mAdapter.notifyDataSetChanged();
                        }
                    }

                    public void onError(RongIMClient.ErrorCode e) {
                    }
                }, new Conversation.ConversationType[]{Conversation.ConversationType.GROUP});
            } else {
                RongIMClient.getInstance().getConversation(clearMessagesEvent.getType(), clearMessagesEvent.getTargetId(), new RongIMClient.ResultCallback<Conversation>() {
                    public void onSuccess(Conversation conversation) {
                        if(conversation != null) {
                            UIConversation uiConversation = UIConversation.obtain(conversation, false);
                            int pos = MessageFragment.this.mAdapter.findPosition(conversation.getConversationType(), conversation.getTargetId());
                            if(pos >= 0) {
                                MessageFragment.this.mAdapter.remove(pos);
                            }

                            int newPos = ConversationListUtils.findPositionForNewConversation(uiConversation, MessageFragment.this.mAdapter);
                            MessageFragment.this.mAdapter.add(uiConversation, newPos);
                            MessageFragment.this.mAdapter.notifyDataSetChanged();
                        }
                    }

                    public void onError(RongIMClient.ErrorCode e) {
                    }
                });
            }
        }

    }

    public void onEventMainThread(Event.OnMessageSendErrorEvent sendErrorEvent) {
        int index = this.mAdapter.findPosition(sendErrorEvent.getMessage().getConversationType(), sendErrorEvent.getMessage().getTargetId());
        if(index >= 0) {
            UIConversation temp = (UIConversation)this.mAdapter.getItem(index);
            temp.setUIConversationTime(sendErrorEvent.getMessage().getSentTime());
            temp.setMessageContent(sendErrorEvent.getMessage().getContent());
            temp.setConversationContent(temp.buildConversationContent(temp));
            temp.setSentStatus(Message.SentStatus.FAILED);
            this.mAdapter.remove(index);
            int newPosition = ConversationListUtils.findPositionForNewConversation(temp, this.mAdapter);
            this.mAdapter.add(temp, newPosition);
            this.mAdapter.notifyDataSetChanged();
        }

    }

    public void onEventMainThread(Event.QuitDiscussionEvent event) {
        int index = this.mAdapter.findPosition(Conversation.ConversationType.DISCUSSION, event.getDiscussionId());
        if(index >= 0) {
            if(!RongContext.getInstance().getConversationGatherState(Conversation.ConversationType.DISCUSSION.getName()).booleanValue()) {
                this.mAdapter.remove(index);
            }

            this.mAdapter.notifyDataSetChanged();
        }

    }

    public void onEventMainThread(Event.QuitGroupEvent event) {
        final int index = this.mAdapter.findPosition(Conversation.ConversationType.GROUP, event.getGroupId());
        boolean gatherState = RongContext.getInstance().getConversationGatherState(Conversation.ConversationType.GROUP.getName()).booleanValue();
        if(index >= 0 && gatherState) {
            RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                public void onSuccess(List<Conversation> conversationList) {
                    if (conversationList != null && conversationList.size() != 0) {
                        UIConversation uiConversation = MessageFragment.this.makeUIConversationFromList(conversationList);
                        int pos = MessageFragment.this.mAdapter.findPosition(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
                        if (pos >= 0) {
                            MessageFragment.this.mAdapter.remove(pos);
                        }

                        int newIndex = ConversationListUtils.findPositionForNewConversation(uiConversation, MessageFragment.this.mAdapter);
                        MessageFragment.this.mAdapter.add(uiConversation, newIndex);
                        MessageFragment.this.mAdapter.notifyDataSetChanged();
                    } else {
                        if (index >= 0) {
                            MessageFragment.this.mAdapter.remove(index);
                        }

                        MessageFragment.this.mAdapter.notifyDataSetChanged();
                    }
                }

                public void onError(RongIMClient.ErrorCode e) {
                }
            }, new Conversation.ConversationType[]{Conversation.ConversationType.GROUP});
        } else if(index >= 0) {
            this.mAdapter.remove(index);
            this.mAdapter.notifyDataSetChanged();
        }

    }

    public void onEventMainThread(Event.AudioListenedEvent event) {
        int originalIndex = this.mAdapter.findPosition(event.getConversationType(), event.getTargetId());
        if(originalIndex >= 0) {
            UIConversation temp = (UIConversation)this.mAdapter.getItem(originalIndex);
            if(temp.getLatestMessageId() == event.getLatestMessageId()) {
                temp.setConversationContent(temp.buildConversationContent(temp));
            }

            this.mAdapter.getView(originalIndex, this.mList.getChildAt(originalIndex - this.mList.getFirstVisiblePosition()), this.mList);
        }

    }

    public void onPortraitItemClick(View v, UIConversation data) {
        Conversation.ConversationType type = data.getConversationType();
        if(RongContext.getInstance().getConversationGatherState(type.getName()).booleanValue()) {
            RongIM.getInstance().startSubConversationList(this.getActivity(), type);
        } else {
            if(RongContext.getInstance().getConversationListBehaviorListener() != null) {
                boolean isDefault = RongContext.getInstance().getConversationListBehaviorListener().onConversationPortraitClick(this.getActivity(), type, data.getConversationTargetId());
                if(isDefault) {
                    return;
                }
            }

            data.setUnReadMessageCount(0);
            RongIM.getInstance().startConversation(this.getActivity(), type, data.getConversationTargetId(), data.getUIConversationTitle());
        }

    }

    public boolean onPortraitItemLongClick(View v, UIConversation data) {
        Conversation.ConversationType type = data.getConversationType();
        if(RongContext.getInstance().getConversationListBehaviorListener() != null) {
            boolean isDealt = RongContext.getInstance().getConversationListBehaviorListener().onConversationPortraitLongClick(this.getActivity(), type, data.getConversationTargetId());
            if(isDealt) {
                return true;
            }
        }

        if(!RongContext.getInstance().getConversationGatherState(type.getName()).booleanValue()) {
            this.buildMultiDialog(data);
            return true;
        } else {
            this.buildSingleDialog(data);
            return true;
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UIConversation uiconversation = (UIConversation)this.mAdapter.getItem(position);
        Conversation.ConversationType type = uiconversation.getConversationType();
        if(RongContext.getInstance().getConversationGatherState(type.getName()).booleanValue()) {
            RongIM.getInstance().startSubConversationList(this.getActivity(), type);
        } else {
            if(RongContext.getInstance().getConversationListBehaviorListener() != null) {
                boolean isDefault = RongContext.getInstance().getConversationListBehaviorListener().onConversationClick(this.getActivity(), view, uiconversation);
                if(isDefault) {
                    return;
                }
            }

            uiconversation.setUnReadMessageCount(0);
            RongIM.getInstance().startConversation(this.getActivity(), type, uiconversation.getConversationTargetId(), uiconversation.getUIConversationTitle());
        }

    }

    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        UIConversation uiConversation = (UIConversation)this.mAdapter.getItem(position);
        String type = uiConversation.getConversationType().getName();
        if(RongContext.getInstance().getConversationListBehaviorListener() != null) {
            boolean isDealt = RongContext.getInstance().getConversationListBehaviorListener().onConversationLongClick(this.getActivity(), view, uiConversation);
            if(isDealt) {
                return true;
            }
        }

        if(!RongContext.getInstance().getConversationGatherState(type).booleanValue()) {
            this.buildMultiDialog(uiConversation);
            return true;
        } else {
            this.buildSingleDialog(uiConversation);
            return true;
        }
    }

    private void buildMultiDialog(final UIConversation uiConversation) {
        String[] items = new String[2];
        if(uiConversation.isTop()) {
            items[0] = RongContext.getInstance().getString(io.rong.imkit.R.string.rc_conversation_list_dialog_cancel_top);
        } else {
            items[0] = RongContext.getInstance().getString(io.rong.imkit.R.string.rc_conversation_list_dialog_set_top);
        }

        items[1] = RongContext.getInstance().getString(io.rong.imkit.R.string.rc_conversation_list_dialog_remove);
        ArraysDialogFragment.newInstance(uiConversation.getUIConversationTitle(), items).setArraysDialogItemListener(new ArraysDialogFragment.OnArraysDialogItemListener() {
            public void OnArraysDialogItemClick(DialogInterface dialog, int which) {
                if(which == 0) {
                    RongIM.getInstance().setConversationToTop(uiConversation.getConversationType(), uiConversation.getConversationTargetId(), !uiConversation.isTop(), new RongIMClient.ResultCallback<Boolean>() {
                        public void onSuccess(Boolean aBoolean) {
                            if(uiConversation.isTop()) {
                                Toast.makeText(RongContext.getInstance(), MessageFragment.this.getString(io.rong.imkit.R.string.rc_conversation_list_popup_cancel_top), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RongContext.getInstance(), MessageFragment.this.getString(io.rong.imkit.R.string.rc_conversation_list_dialog_set_top), Toast.LENGTH_SHORT).show();
                            }

                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    });
                } else if(which == 1) {
                    RongIM.getInstance().removeConversation(uiConversation.getConversationType(), uiConversation.getConversationTargetId());
                }

            }
        }).show(this.getFragmentManager());
    }

    private void buildSingleDialog(final UIConversation uiConversation) {
        String[] items = new String[]{RongContext.getInstance().getString(io.rong.imkit.R.string.rc_conversation_list_dialog_remove)};
        ArraysDialogFragment.newInstance(uiConversation.getUIConversationTitle(), items).setArraysDialogItemListener(new ArraysDialogFragment.OnArraysDialogItemListener() {
            public void OnArraysDialogItemClick(DialogInterface dialog, int which) {
                RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                    public void onSuccess(List<Conversation> conversations) {
                        if(conversations != null && conversations.size() != 0) {
                            Iterator i$ = conversations.iterator();

                            while(i$.hasNext()) {
                                Conversation conversation = (Conversation)i$.next();
                                RongIM.getInstance().removeConversation(conversation.getConversationType(), conversation.getTargetId());
                            }

                        }
                    }

                    public void onError(RongIMClient.ErrorCode errorCode) {
                    }
                }, new Conversation.ConversationType[]{uiConversation.getConversationType()});
            }
        }).show(this.getFragmentManager());
    }

    private void makeUiConversationList(List<Conversation> conversationList) {
        UIConversation uiCon;
        for(Iterator i$ = conversationList.iterator(); i$.hasNext(); this.refreshUnreadCount(uiCon.getConversationType(), uiCon.getConversationTargetId())) {
            Conversation conversation = (Conversation)i$.next();
            Conversation.ConversationType conversationType = conversation.getConversationType();
            boolean gatherState = RongContext.getInstance().getConversationGatherState(conversationType.getName()).booleanValue();
            int originalIndex = this.mAdapter.findPosition(conversationType, conversation.getTargetId());
            if(originalIndex >= 0) {
                uiCon = (UIConversation)this.mAdapter.getItem(originalIndex);
                if(!uiCon.getMentionedFlag()) {
                    uiCon.setMentionedFlag(conversation.getMentionedCount() > 0);
                }
            } else {
                uiCon = UIConversation.obtain(conversation, gatherState);
                this.mAdapter.add(uiCon);
            }
        }

    }

    private UIConversation makeUiConversation(Message message, int pos) {
        UIConversation uiConversation;
        if(pos >= 0) {
            uiConversation = (UIConversation)this.mAdapter.getItem(pos);
            if(uiConversation != null) {
                uiConversation.setMessageContent(message.getContent());
                if(message.getMessageDirection() == Message.MessageDirection.SEND) {
                    uiConversation.setUIConversationTime(message.getSentTime());
                    uiConversation.setConversationSenderId(RongIM.getInstance().getCurrentUserId());
                } else {
                    uiConversation.setUIConversationTime(message.getSentTime());
                    uiConversation.setConversationSenderId(message.getSenderUserId());
                }

                uiConversation.setConversationTargetId(message.getTargetId());
                uiConversation.setConversationContent(uiConversation.buildConversationContent(uiConversation));
                uiConversation.setSentStatus(message.getSentStatus());
                uiConversation.setLatestMessageId(message.getMessageId());
                if(!uiConversation.getMentionedFlag()) {
                    MentionedInfo title = message.getContent().getMentionedInfo();
                    if(title != null && (title.getType().equals(MentionedInfo.MentionedType.PART) && title.getMentionedUserIdList().contains(RongIMClient.getInstance().getCurrentUserId()) || title.getType().equals(MentionedInfo.MentionedType.ALL))) {
                        uiConversation.setMentionedFlag(true);
                    }
                }

                String title1 = "";
                Uri iconUri = null;
                UserInfo userInfo = message.getContent().getUserInfo();
                Conversation.ConversationType conversationType = message.getConversationType();
                if(userInfo != null && message.getTargetId().equals(userInfo.getUserId()) && (conversationType.equals(Conversation.ConversationType.PRIVATE) || conversationType.equals(Conversation.ConversationType.SYSTEM)) && (uiConversation.getUIConversationTitle() != null && userInfo.getName() != null && !userInfo.getName().equals(uiConversation.getUIConversationTitle()) || uiConversation.getIconUrl() != null && userInfo.getPortraitUri() != null && !userInfo.getPortraitUri().equals(uiConversation.getIconUrl()))) {
                    iconUri = userInfo.getPortraitUri();
                    title1 = userInfo.getName();
                    RongIMClient.getInstance().updateConversationInfo(message.getConversationType(), message.getTargetId(), title1, iconUri != null?iconUri.toString():"", (RongIMClient.ResultCallback)null);
                }
            }
        } else {
            uiConversation = UIConversation.obtain(message, RongContext.getInstance().getConversationGatherState(message.getConversationType().getName()).booleanValue());
        }

        return uiConversation;
    }

    private UIConversation makeUIConversationFromList(List<Conversation> conversations) {
        int unreadCount = 0;
        boolean topFlag = false;
        boolean isMentioned = false;
        Conversation newest = (Conversation)conversations.get(0);

        Conversation conversation;
        for(Iterator uiConversation = conversations.iterator(); uiConversation.hasNext(); unreadCount += conversation.getUnreadMessageCount()) {
            conversation = (Conversation)uiConversation.next();
            if(newest.isTop()) {
                if(conversation.isTop() && conversation.getSentTime() > newest.getSentTime()) {
                    newest = conversation;
                }
            } else if(conversation.isTop() || conversation.getSentTime() > newest.getSentTime()) {
                newest = conversation;
            }

            if(conversation.isTop()) {
                topFlag = true;
            }

            if(conversation.getMentionedCount() > 0) {
                isMentioned = true;
            }
        }

        UIConversation uiConversation1 = UIConversation.obtain(newest, RongContext.getInstance().getConversationGatherState(newest.getConversationType().getName()).booleanValue());
        uiConversation1.setUnReadMessageCount(unreadCount);
        uiConversation1.setTop(topFlag);
        uiConversation1.setMentionedFlag(isMentioned);
        return uiConversation1;
    }

    private void refreshUnreadCount(final Conversation.ConversationType type, final String targetId) {
        if(this.mAdapter == null) {
            RLog.d(TAG, "the conversation list adapter is null.");
        } else {
            if(RongContext.getInstance().getConversationGatherState(type.getName()).booleanValue()) {
                RongIM.getInstance().getUnreadCount(new RongIMClient.ResultCallback<Integer>() {
                    public void onSuccess(Integer count) {
                        int curPos = MessageFragment.this.mAdapter.findPosition(type, targetId);
                        if(curPos >= 0) {
                            ((UIConversation)MessageFragment.this.mAdapter.getItem(curPos)).setUnReadMessageCount(count.intValue());
                            MessageFragment.this.mAdapter.getView(curPos, MessageFragment.this.mList.getChildAt(curPos - MessageFragment.this.mList.getFirstVisiblePosition()), MessageFragment.this.mList);
                        }

                    }

                    public void onError(RongIMClient.ErrorCode e) {
                        System.err.print("Throw exception when get unread message count from ipc remote side!");
                    }
                }, new Conversation.ConversationType[]{type});
            } else {
                RongIM.getInstance().getUnreadCount(type, targetId, new RongIMClient.ResultCallback<Integer>() {
                    public void onSuccess(Integer integer) {
                        int curPos = MessageFragment.this.mAdapter.findPosition(type, targetId);
                        if(curPos >= 0) {
                            ((UIConversation)MessageFragment.this.mAdapter.getItem(curPos)).setUnReadMessageCount(integer.intValue());
                            MessageFragment.this.mAdapter.getView(curPos, MessageFragment.this.mList.getChildAt(curPos - MessageFragment.this.mList.getFirstVisiblePosition()), MessageFragment.this.mList);
                        }

                    }

                    public void onError(RongIMClient.ErrorCode e) {
                    }
                });
            }

        }
    }

    public void logout() {
        mAdapter = new MessageAdapter(RongContext.getInstance());
        mList.setAdapter(mAdapter);
    }
}
