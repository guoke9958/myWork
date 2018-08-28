//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imkit.fragment;

import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import io.rong.common.RLog;
import io.rong.common.SystemUtils;
import io.rong.eventbus.EventBus;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.R.bool;
import io.rong.imkit.R.id;
import io.rong.imkit.R.layout;
import io.rong.imkit.R.string;
import io.rong.imkit.fragment.UriFragment;
import io.rong.imkit.manager.AudioPlayManager;
import io.rong.imkit.model.EmojiMessageAdapter;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.model.Event.ConnectEvent;
import io.rong.imkit.model.Event.GroupUserInfoEvent;
import io.rong.imkit.model.Event.InputViewEvent;
import io.rong.imkit.model.Event.MessageDeleteEvent;
import io.rong.imkit.model.Event.MessageRecallEvent;
import io.rong.imkit.model.Event.MessagesClearEvent;
import io.rong.imkit.model.Event.OnMessageSendErrorEvent;
import io.rong.imkit.model.Event.OnReceiveMessageEvent;
import io.rong.imkit.model.Event.OnReceiveMessageProgressEvent;
import io.rong.imkit.model.Event.PlayAudioEvent;
import io.rong.imkit.model.Event.PublicServiceFollowableEvent;
import io.rong.imkit.model.Event.ReadReceiptEvent;
import io.rong.imkit.model.Event.ReadReceiptRequestEvent;
import io.rong.imkit.model.Event.ReadReceiptResponseEvent;
import io.rong.imkit.model.Event.RemoteMessageRecallEvent;
import io.rong.imkit.widget.InputView.Event;
import io.rong.imkit.widget.adapter.CustomMessageListAdapter;
import io.rong.imkit.widget.adapter.MessageListAdapter;
import io.rong.imkit.widget.adapter.MessageListAdapter.OnItemHandlerListener;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.IRongCallback.ISendMediaMessageCallback;
import io.rong.imlib.IRongCallback.ISendMessageCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.OperationCallback;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.RongIMClient.SendImageMessageCallback;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.PublicServiceProfile;
import io.rong.imlib.model.ReadReceiptInfo;
import io.rong.imlib.model.UserInfo;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message.MessageDirection;
import io.rong.imlib.model.Message.ReceivedStatus;
import io.rong.imlib.model.Message.SentStatus;
import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.ReadReceiptMessage;
import io.rong.message.RecallNotificationMessage;
import io.rong.message.VoiceMessage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomMessageListFragment extends UriFragment implements OnScrollListener {
    private static final String TAG = "CustomMessageListFragment";
    CustomMessageListAdapter mAdapter;
    GestureDetector mGestureDetector;
    ListView mList;
    Conversation mConversation;
    int mUnreadCount;
    int mNewMessageCount;
    int mLastVisiblePosition;
    Button mUnreadBtn;
    ImageButton mNewMessageBtn;
    TextView mNewMessageTextView;
    boolean isShowUnreadMessageState;
    boolean isShowNewMessageState;
    int mMessageleft = -1;
    boolean needEvaluateForRobot = false;
    boolean robotMode = true;
    static final int REQ_LIST = 1;
    static final int RENDER_LIST = 2;
    static final int REFRESH_LIST_WHILE_RECEIVE_MESSAGE = 3;
    static final int REFRESH_ITEM = 4;
    static final int REQ_HISTORY = 5;
    static final int RENDER_HISTORY = 6;
    static final int REFRESH_ITEM_READ_RECEIPT = 7;
    static final int REQ_REMOTE_HISTORY = View.GONE;
    static final int NOTIFY_LIST = 9;
    static final int RESET_LIST_STACK = 10;
    static final int DELETE_MESSAGE = 11;
    static final int REQ_UNREAD = 12;
    private static final int LISTVIEW_SHOW_COUNT = 5;
    View mHeaderView;
    private boolean isOnClickBtn;
    private boolean isShowWithoutConnected = false;
    private List<Message> mUnreadMentionMessages;
    private boolean mHasReceivedMessage;
    OnScrollListener onScrollListener;
    boolean mHasMoreLocalMessages = true;
    boolean mHasMoreRemoteMessages = true;
    long mLastRemoteMessageTime = 0L;
    boolean isLoading = false;
    private String mRightImageUrl;
    private String mLeftImageUrl;

    public CustomMessageListFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RongContext.getInstance().getEventBus().register(this);
        this.isShowUnreadMessageState = RongContext.getInstance().getUnreadMessageState();
        this.isShowNewMessageState = RongContext.getInstance().getNewMessageState();
        if(EmojiMessageAdapter.getInstance() == null) {
            EmojiMessageAdapter.init(RongContext.getInstance());
        }

        this.mAdapter = new CustomMessageListAdapter(this.getActivity(),mRightImageUrl,mLeftImageUrl);
        this.mGestureDetector = new GestureDetector(new SimpleOnGestureListener() {
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if(distanceY > 0.0F && CustomMessageListFragment.this.mNewMessageCount >= 0 && CustomMessageListFragment.this.mList.getLastVisiblePosition() >= CustomMessageListFragment.this.mList.getCount() - CustomMessageListFragment.this.mNewMessageCount) {
                    CustomMessageListFragment.this.mNewMessageTextView.setText(CustomMessageListFragment.this.mList.getCount() - CustomMessageListFragment.this.mList.getLastVisiblePosition() + "");
                    CustomMessageListFragment.this.mNewMessageCount = CustomMessageListFragment.this.mList.getCount() - CustomMessageListFragment.this.mList.getLastVisiblePosition() - 1;
                    if(CustomMessageListFragment.this.mNewMessageCount > 99) {
                        CustomMessageListFragment.this.mNewMessageTextView.setText("99+");
                    } else {
                        CustomMessageListFragment.this.mNewMessageTextView.setText(CustomMessageListFragment.this.mNewMessageCount + "");
                    }
                }

                if(CustomMessageListFragment.this.mNewMessageCount == 0) {
                    CustomMessageListFragment.this.mNewMessageBtn.setVisibility(View.GONE);
                    CustomMessageListFragment.this.mNewMessageTextView.setVisibility(View.GONE);
                }

                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    protected void initFragment(Uri uri) {
        RLog.d("CustomMessageListFragment", "initFragment " + uri);
        String typeStr = uri.getLastPathSegment().toUpperCase();
        ConversationType type = ConversationType.valueOf(typeStr);
        String targetId = uri.getQueryParameter("targetId");
        String title = uri.getQueryParameter("title");
        if(!TextUtils.isEmpty(targetId) && type != null) {
            this.mConversation = Conversation.obtain(type, targetId, title);
            if(this.mAdapter != null) {
                this.getHandler().post(new Runnable() {
                    public void run() {
                        CustomMessageListFragment.this.mAdapter.clear();
                        CustomMessageListFragment.this.mAdapter.notifyDataSetChanged();
                    }
                });
            }

            this.mNewMessageBtn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    CustomMessageListFragment.this.getHandler().postDelayed(CustomMessageListFragment.this.new ScrollRunnable(), 500L);
                    CustomMessageListFragment.this.mList.smoothScrollToPosition(CustomMessageListFragment.this.mList.getCount() + 1);
                    CustomMessageListFragment.this.mNewMessageCount = 0;
                    CustomMessageListFragment.this.mNewMessageBtn.setVisibility(View.GONE);
                    CustomMessageListFragment.this.mNewMessageTextView.setVisibility(View.GONE);
                }
            });
            if(RongIM.getInstance().getCurrentConnectionStatus().equals(ConnectionStatus.DISCONNECTED)) {
                RLog.e("CustomMessageListFragment", "initFragment Not connected yet.");
                this.isShowWithoutConnected = true;
            } else {
                this.getConversation();
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout.rc_fr_messagelist, container, false);
        this.mUnreadBtn = (Button)this.findViewById(view, id.rc_unread_message_count);
        this.mNewMessageBtn = (ImageButton)this.findViewById(view, id.rc_new_message_count);
        this.mNewMessageTextView = (TextView)this.findViewById(view, id.rc_new_message_number);
        this.mList = (ListView)this.findViewById(view, id.rc_list);
        this.mHeaderView = inflater.inflate(layout.rc_item_progress, (ViewGroup)null);
        this.mList.addHeaderView(this.mHeaderView);
        this.mList.setOnScrollListener(this);
        this.mList.setSelectionAfterHeaderView();
        this.mAdapter.setOnItemHandlerListener(new OnItemHandlerListener() {
            public void onWarningViewClick(int position, final Message data, View v) {
                RongIM.getInstance().deleteMessages(new int[]{data.getMessageId()}, new ResultCallback<Boolean>() {
                    public void onSuccess(Boolean aBoolean) {
                        if(aBoolean.booleanValue()) {
                            data.setMessageId(0);
                            if(data.getContent() instanceof ImageMessage) {
                                RongIM.getInstance().sendImageMessage(data, "", "", new SendImageMessageCallback() {
                                    public void onAttached(Message message) {
                                    }

                                    public void onError(Message message, ErrorCode code) {
                                    }

                                    public void onSuccess(Message message) {
                                    }

                                    public void onProgress(Message message, int progress) {
                                    }
                                });
                            } else if(data.getContent() instanceof LocationMessage) {
                                RongIM.getInstance().sendLocationMessage(data, (String)null, (String)null, (ISendMessageCallback)null);
                            } else if(data.getContent() instanceof FileMessage) {
                                RongIM.getInstance().sendMediaMessage(data, (String)null, (String)null, (ISendMediaMessageCallback)null);
                            } else {
                                RongIM.getInstance().sendMessage(data, (String)null, (String)null, new ISendMessageCallback() {
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

                    public void onError(ErrorCode e) {
                    }
                });
            }
        });
        return view;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch(scrollState) {
            case 0:
                if(view.getFirstVisiblePosition() == 0 && this.mAdapter.getCount() > 0 && this.mHasMoreLocalMessages && !this.isLoading) {
                    this.isLoading = true;
                    this.getHandler().sendEmptyMessage(5);
                } else if(view.getFirstVisiblePosition() == 0 && !this.mHasMoreLocalMessages && this.mHasMoreRemoteMessages && !this.isLoading && this.mConversation.getConversationType() != ConversationType.CHATROOM && this.mConversation.getConversationType() != ConversationType.APP_PUBLIC_SERVICE && this.mConversation.getConversationType() != ConversationType.PUBLIC_SERVICE) {
                    this.isLoading = true;
                    this.mLastRemoteMessageTime = ((UIMessage)this.mAdapter.getItem(0)).getSentTime();
                    this.getHandler().sendEmptyMessage(View.GONE);
                }
            default:
                if(this.onScrollListener != null) {
                    this.onScrollListener.onScrollStateChanged(view, scrollState);
                }

        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(this.onScrollListener != null) {
            this.onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        if(firstVisibleItem + visibleItemCount >= totalItemCount - this.mNewMessageCount) {
            this.mNewMessageCount = 0;
            this.mNewMessageBtn.setVisibility(View.GONE);
            this.mNewMessageTextView.setVisibility(View.GONE);
        }

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        if(this.getActionBarHandler() != null) {
            this.getActionBarHandler().onTitleChanged(this.mConversation.getConversationTitle());
        }

        this.mList.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 2 || event.getAction() == 0) {
                    EventBus.getDefault().post(InputViewEvent.obtain(false));
                    if (event.getAction() == 2 && CustomMessageListFragment.this.mList.getCount() == 0 && CustomMessageListFragment.this.mHasMoreRemoteMessages && CustomMessageListFragment.this.mConversation.getConversationType() != ConversationType.CHATROOM && CustomMessageListFragment.this.mConversation.getConversationType() != ConversationType.APP_PUBLIC_SERVICE && CustomMessageListFragment.this.mConversation.getConversationType() != ConversationType.PUBLIC_SERVICE) {
                        CustomMessageListFragment.this.isLoading = true;
                        CustomMessageListFragment.this.getHandler().sendEmptyMessage(View.GONE);
                    }
                }

                CustomMessageListFragment.this.mGestureDetector.onTouchEvent(event);
                return false;
            }
        });
        this.mList.setAdapter(this.mAdapter);
        this.mList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RongContext.getInstance().getPrimaryInputProvider().onInactive(view.getContext());
                RongContext.getInstance().getSecondaryInputProvider().onInactive(view.getContext());
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public boolean onBackPressed() {
        return false;
    }

    private List<UIMessage> filterMessage(List<UIMessage> srcList) {
        Object destList = null;
        if(this.mAdapter.getCount() > 0) {
            destList = new ArrayList();

            for(int i = 0; i < this.mAdapter.getCount(); ++i) {
                Iterator i$ = srcList.iterator();

                while(i$.hasNext()) {
                    UIMessage msg = (UIMessage)i$.next();
                    if(!((List)destList).contains(msg) && msg.getMessageId() != ((UIMessage)this.mAdapter.getItem(i)).getMessageId()) {
                        ((List)destList).add(msg);
                    }
                }
            }
        } else {
            destList = srcList;
        }

        return (List)destList;
    }

    public boolean handleMessage(android.os.Message msg) {
        RLog.d("CustomMessageListFragment", "CustomMessageListFragment msg : " + msg.what);
        List position;
        int pos;
        UIMessage message;
        switch(msg.what) {
            case 1:
                this.mAdapter.clear();
                this.mAdapter.notifyDataSetChanged();
                EmojiMessageAdapter.getInstance().getLatestMessages(this.mConversation.getConversationType(), this.mConversation.getTargetId(), 30, new ResultCallback<List<UIMessage>>() {
                    public void onSuccess(List<UIMessage> messages) {
                        RLog.d("CustomMessageListFragment", "getLatestMessages, onSuccess " + messages.size());
                        CustomMessageListFragment.this.mHasMoreLocalMessages = messages.size() == 30;
                        CustomMessageListFragment.this.mList.removeHeaderView(CustomMessageListFragment.this.mHeaderView);
                        CustomMessageListFragment.this.isLoading = false;
                        CustomMessageListFragment.this.getHandler().obtainMessage(2, messages).sendToTarget();
                    }

                    public void onError(ErrorCode e) {
                        RLog.e("CustomMessageListFragment", "getLatestMessages, " + e.toString());
                        CustomMessageListFragment.this.mHasMoreLocalMessages = false;
                        CustomMessageListFragment.this.isLoading = false;
                        CustomMessageListFragment.this.mList.removeHeaderView(CustomMessageListFragment.this.mHeaderView);
                    }
                });
                break;
            case 2:
                if(msg.obj != null && msg.obj instanceof List) {
                    position = (List)msg.obj;
                    this.mAdapter.clear();
                    this.mAdapter.addCollection(this.filterMessage(position));
                    if(position.size() <= 5) {
                        this.mList.setStackFromBottom(false);
                        this.mList.setTranscriptMode(2);
                    } else {
                        this.mList.setStackFromBottom(true);
                        this.mList.setTranscriptMode(2);
                    }

                    this.mAdapter.notifyDataSetChanged();
                    this.getHandler().sendEmptyMessage(10);
                }

                if(this.mUnreadMentionMessages != null && this.mUnreadMentionMessages.size() > 0) {
                    UIMessage position2 = UIMessage.obtain((Message)this.mUnreadMentionMessages.get(0));
                    pos = this.mAdapter.findPosition(position2);
                    this.mList.smoothScrollToPosition(pos);
                }

                if(this.mUnreadCount >= 10) {
                    TranslateAnimation position3 = new TranslateAnimation(300.0F, 0.0F, 0.0F, 0.0F);
                    AlphaAnimation pos2 = new AlphaAnimation(0.0F, 1.0F);
                    position3.setDuration(1000L);
                    pos2.setDuration(2000L);
                    AnimationSet message1 = new AnimationSet(true);
                    message1.addAnimation(position3);
                    message1.addAnimation(pos2);
                    this.mUnreadBtn.setVisibility(View.VISIBLE);
                    this.mUnreadBtn.startAnimation(message1);
                    message1.setAnimationListener(new AnimationListener() {
                        public void onAnimationStart(Animation animation) {
                        }

                        public void onAnimationEnd(Animation animation) {
                            CustomMessageListFragment.this.getHandler().postDelayed(new Runnable() {
                                public void run() {
                                    if(!CustomMessageListFragment.this.isOnClickBtn) {
                                        TranslateAnimation animation = new TranslateAnimation(0.0F, 700.0F, 0.0F, 0.0F);
                                        animation.setDuration(700L);
                                        animation.setFillAfter(true);
                                        CustomMessageListFragment.this.mUnreadBtn.startAnimation(animation);
                                    }

                                }
                            }, 4000L);
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }
            case 3:
            default:
                break;
            case 4:
                int position1 = ((Integer)msg.obj).intValue();
                if(position1 >= this.mList.getFirstVisiblePosition() && position1 <= this.mList.getLastVisiblePosition()) {
                    RLog.d("CustomMessageListFragment", "REFRESH_ITEM Index:" + position1);
                    this.mAdapter.getView(position1, this.mList.getChildAt(position1 - this.mList.getFirstVisiblePosition() + this.mList.getHeaderViewsCount()), this.mList);
                }
                break;
            case 5:
                message = (UIMessage)this.mAdapter.getItem(0);
                this.mList.addHeaderView(this.mHeaderView);
                EmojiMessageAdapter.getInstance().getHistoryMessages(this.mConversation.getConversationType(), this.mConversation.getTargetId(), message.getMessageId(), 30, new ResultCallback<List<UIMessage>>() {
                    public void onSuccess(List<UIMessage> messages) {
                        RLog.d("CustomMessageListFragment", "getHistoryMessages, onSuccess " + messages.size());
                        CustomMessageListFragment.this.mHasMoreLocalMessages = messages.size() == 30;
                        CustomMessageListFragment.this.mList.removeHeaderView(CustomMessageListFragment.this.mHeaderView);
                        CustomMessageListFragment.this.isLoading = false;
                        CustomMessageListFragment.this.getHandler().obtainMessage(6, messages).sendToTarget();
                    }

                    public void onError(ErrorCode e) {
                        CustomMessageListFragment.this.mHasMoreLocalMessages = false;
                        CustomMessageListFragment.this.mList.removeHeaderView(CustomMessageListFragment.this.mHeaderView);
                        CustomMessageListFragment.this.isLoading = false;
                        RLog.e("CustomMessageListFragment", "getHistoryMessages, " + e.toString());
                    }
                });
                break;
            case 6:
                if(msg.obj instanceof List) {
                    position = (List)msg.obj;
                    Iterator pos1 = position.iterator();

                    while(pos1.hasNext()) {
                        message = (UIMessage)pos1.next();
                        this.mAdapter.add(message, 0);
                    }

                    this.mList.setTranscriptMode(0);
                    this.mList.setStackFromBottom(false);
                    pos = this.mList.getFirstVisiblePosition();
                    this.mAdapter.notifyDataSetChanged();
                    if(pos == 0) {
                        this.mList.setSelection(position.size());
                    }
                }
                break;
            case 7:
                pos = ((Integer)msg.obj).intValue();
                if(pos >= this.mList.getFirstVisiblePosition() && pos <= this.mList.getLastVisiblePosition()) {
                    RLog.d("CustomMessageListFragment", "REFRESH_ITEM Index:" + pos);
                    this.mAdapter.getView(pos, this.mList.getChildAt(pos - this.mList.getFirstVisiblePosition() + this.mList.getHeaderViewsCount()), this.mList);
                }
                break;
            case View.GONE:
                this.mList.addHeaderView(this.mHeaderView);
                EmojiMessageAdapter.getInstance().getRemoteHistoryMessages(this.mConversation.getConversationType(), this.mConversation.getTargetId(), this.mLastRemoteMessageTime, 10, new ResultCallback<List<UIMessage>>() {
                    public void onSuccess(List<UIMessage> uiMessages) {
                        CustomMessageListFragment.this.mList.removeHeaderView(CustomMessageListFragment.this.mHeaderView);
                        if(uiMessages != null && uiMessages.size() != 0) {
                            RLog.d("CustomMessageListFragment", "getRemoteHistoryMessages, onSuccess " + uiMessages.size());
                            CustomMessageListFragment.this.mHasMoreRemoteMessages = uiMessages.size() >= 10;
                            ArrayList filterMsg = new ArrayList();
                            Iterator i$ = uiMessages.iterator();

                            while(i$.hasNext()) {
                                UIMessage m = (UIMessage)i$.next();
                                String uid = m.getUId();
                                int count = CustomMessageListFragment.this.mAdapter.getCount();
                                boolean result = true;

                                for(int i = 0; i < count; ++i) {
                                    UIMessage item = (UIMessage)CustomMessageListFragment.this.mAdapter.getItem(i);
                                    String targetUid = item.getUId();
                                    if(uid != null && targetUid != null && uid.equals(targetUid)) {
                                        result = false;
                                        break;
                                    }
                                }

                                if(result) {
                                    filterMsg.add(m);
                                }
                            }

                            RLog.d("CustomMessageListFragment", "getRemoteHistoryMessages, src: " + uiMessages.size() + " dest: " + filterMsg.size());
                            CustomMessageListFragment.this.getHandler().obtainMessage(6, filterMsg).sendToTarget();
                        } else {
                            CustomMessageListFragment.this.mHasMoreRemoteMessages = false;
                        }

                        CustomMessageListFragment.this.isLoading = false;
                    }

                    public void onError(ErrorCode e) {
                        CustomMessageListFragment.this.mHasMoreRemoteMessages = false;
                        CustomMessageListFragment.this.mList.removeHeaderView(CustomMessageListFragment.this.mHeaderView);
                        CustomMessageListFragment.this.isLoading = false;
                        RLog.e("CustomMessageListFragment", "getRemoteHistoryMessages, " + e.toString());
                    }
                });
                break;
            case 9:
                if(this.mAdapter != null) {
                    this.mAdapter.notifyDataSetChanged();
                }
                break;
            case 10:
                this.resetListViewStack();
                this.mAdapter.notifyDataSetChanged();
                break;
            case 11:
                this.mAdapter.notifyDataSetChanged();
                this.getHandler().post(new Runnable() {
                    public void run() {
                        if(CustomMessageListFragment.this.mList.getCount() > 0) {
                            View firstView = CustomMessageListFragment.this.mList.getChildAt(CustomMessageListFragment.this.mList.getFirstVisiblePosition());
                            View lastView = CustomMessageListFragment.this.mList.getChildAt(CustomMessageListFragment.this.mList.getLastVisiblePosition());
                            if(firstView != null && lastView != null) {
                                int listViewPadding = CustomMessageListFragment.this.mList.getListPaddingBottom() + CustomMessageListFragment.this.mList.getListPaddingTop();
                                int childViewsHeight = lastView.getBottom() - (firstView.getTop() == -1?0:firstView.getTop());
                                int listViewHeight = CustomMessageListFragment.this.mList.getBottom() - listViewPadding;
                                if(childViewsHeight < listViewHeight) {
                                    CustomMessageListFragment.this.mList.setTranscriptMode(2);
                                    CustomMessageListFragment.this.mList.setStackFromBottom(false);
                                } else {
                                    CustomMessageListFragment.this.mList.setTranscriptMode(1);
                                }

                                CustomMessageListFragment.this.mAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                });
                break;
            case 12:
                message = (UIMessage)this.mAdapter.getItem(0);
                EmojiMessageAdapter.getInstance().getHistoryMessages(this.mConversation.getConversationType(), this.mConversation.getTargetId(), message.getMessageId(), this.mUnreadCount - 29, new ResultCallback<List<UIMessage>>() {
                    public void onSuccess(List<UIMessage> messages) {
                        RLog.d("CustomMessageListFragment", "getHistoryMessages unread, onSuccess " + messages.size());
                        CustomMessageListFragment.this.mHasMoreLocalMessages = messages.size() == CustomMessageListFragment.this.mUnreadCount - 29;
                        CustomMessageListFragment.this.mList.removeHeaderView(CustomMessageListFragment.this.mHeaderView);
                        Iterator i$ = messages.iterator();

                        while(i$.hasNext()) {
                            UIMessage item = (UIMessage)i$.next();
                            CustomMessageListFragment.this.mAdapter.add(item, 0);
                        }

                        CustomMessageListFragment.this.mAdapter.notifyDataSetChanged();
                        CustomMessageListFragment.this.mList.setStackFromBottom(false);
                        CustomMessageListFragment.this.mList.smoothScrollToPosition(0);
                        CustomMessageListFragment.this.isLoading = false;
                    }

                    public void onError(ErrorCode e) {
                        RLog.e("CustomMessageListFragment", "getHistoryMessages, " + e.toString());
                        CustomMessageListFragment.this.mHasMoreLocalMessages = false;
                        CustomMessageListFragment.this.mList.removeHeaderView(CustomMessageListFragment.this.mHeaderView);
                        CustomMessageListFragment.this.isLoading = false;
                    }
                });
        }

        return false;
    }

    private void resetListViewStack() {
        int count = this.mList.getChildCount();
        View firstView = this.mList.getChildAt(0);
        View lastView = this.mList.getChildAt(count - 1);
        if(firstView != null && lastView != null) {
            int listViewPadding = this.mList.getListPaddingBottom() + this.mList.getListPaddingTop();
            int childViewsHeight = lastView.getBottom() - (firstView.getTop() == -1?0:firstView.getTop());
            int listViewHeight = this.mList.getBottom() - listViewPadding;
            if(childViewsHeight < listViewHeight) {
                this.mList.setTranscriptMode(2);
                this.mList.setStackFromBottom(false);
            } else {
                this.mList.setTranscriptMode(2);
            }
        }

    }

    public void onEventMainThread(ReadReceiptEvent event) {
        if(RongContext.getInstance().isReadReceiptConversationType(event.getMessage().getConversationType())) {
            if(this.mConversation != null && this.mConversation.getTargetId().equals(event.getMessage().getTargetId()) && this.mConversation.getConversationType() == event.getMessage().getConversationType() && event.getMessage().getMessageDirection().equals(MessageDirection.RECEIVE)) {
                if(!this.mConversation.getConversationType().equals(ConversationType.PRIVATE)) {
                    return;
                }

                ReadReceiptMessage content = (ReadReceiptMessage)event.getMessage().getContent();
                long ntfTime = content.getLastMessageSendTime();

                for(int i = this.mAdapter.getCount() - 1; i >= 0 && ((UIMessage)this.mAdapter.getItem(i)).getSentStatus() != SentStatus.READ; --i) {
                    if(((UIMessage)this.mAdapter.getItem(i)).getSentStatus() == SentStatus.SENT && ((UIMessage)this.mAdapter.getItem(i)).getMessageDirection().equals(MessageDirection.SEND) && ntfTime >= ((UIMessage)this.mAdapter.getItem(i)).getSentTime()) {
                        ((UIMessage)this.mAdapter.getItem(i)).setSentStatus(SentStatus.READ);
                        this.getHandler().obtainMessage(7, Integer.valueOf(i)).sendToTarget();
                    }
                }
            }

        }
    }

    private void refreshListWhileReceiveMessage(UIMessage model) {
        model.setIsHistoryMessage(false);
        this.mAdapter.setEvaluateForRobot(this.needEvaluateForRobot);
        this.mAdapter.setRobotMode(this.robotMode);
        this.mAdapter.add(model);
        if(this.isShowNewMessageState && this.mList.getLastVisiblePosition() < this.mList.getCount() - 1 && MessageDirection.SEND != model.getMessageDirection()
                && SystemUtils.isAppRunningOnTop(RongContext.getInstance(), RongContext.getInstance().getPackageName()) && model.getConversationType() != ConversationType.CHATROOM && model.getConversationType() != ConversationType.CUSTOMER_SERVICE && model.getConversationType() != ConversationType.APP_PUBLIC_SERVICE && model.getConversationType() != ConversationType.PUBLIC_SERVICE) {
            ++this.mNewMessageCount;
            if(this.mNewMessageCount > 0) {
                this.mNewMessageBtn.setVisibility(View.VISIBLE);
                this.mNewMessageTextView.setVisibility(View.VISIBLE);
            }

            if(this.mNewMessageCount > 99) {
                this.mNewMessageTextView.setText("99+");
            } else {
                this.mNewMessageTextView.setText(this.mNewMessageCount + "");
            }
        }

        int last = this.mList.getLastVisiblePosition();
        int count = this.mList.getCount();
        if(last == count - 1) {
            this.mList.setTranscriptMode(2);
        } else if(last < this.mList.getCount() - 1) {
            this.mList.setTranscriptMode(1);
        }

        this.mAdapter.notifyDataSetChanged();
        if(last == count - 1) {
            this.mNewMessageBtn.setVisibility(View.GONE);
            this.mNewMessageTextView.setVisibility(View.GONE);
        }

    }

    public void onEventMainThread(Message msg) {
        UIMessage message = UIMessage.obtain(msg);
        boolean readRec = false;
        boolean syncReadStatus = false;

        try {
            readRec = this.getResources().getBoolean(bool.rc_read_receipt);
            syncReadStatus = this.getResources().getBoolean(bool.rc_enable_sync_read_status);
        } catch (NotFoundException var7) {
            RLog.e("CustomMessageListFragment", "rc_read_receipt not configure in rc_config.xml");
            var7.printStackTrace();
        }

        RLog.d("CustomMessageListFragment", "onEventMainThread message : " + message.getMessageId() + " " + message.getSentStatus());
        if(this.mConversation != null && this.mConversation.getTargetId().equals(message.getTargetId()) && this.mConversation.getConversationType() == message.getConversationType()) {
            int position = this.mAdapter.findPosition((long)message.getMessageId());
            if(message.getMessageId() > 0) {
                ReceivedStatus status = message.getReceivedStatus();
                status.setRead();
                message.setReceivedStatus(status);
                RongIMClient.getInstance().setMessageReceivedStatus(msg.getMessageId(), status, (ResultCallback)null);
            }

            if(position == -1) {
                if(this.mMessageleft <= 0 && message.getConversationType() == ConversationType.PRIVATE && RongContext.getInstance().isReadReceiptConversationType(ConversationType.PRIVATE) && message.getMessageDirection().equals(MessageDirection.RECEIVE)) {
                    if(readRec) {
                        RongIMClient.getInstance().sendReadReceiptMessage(message.getConversationType(), message.getTargetId(), message.getSentTime());
                    } else if(syncReadStatus) {
                        RongIMClient.getInstance().syncConversationReadStatus(message.getConversationType(), message.getTargetId(), message.getSentTime(), (OperationCallback)null);
                    }
                }

                this.mHasReceivedMessage = true;
                this.mConversation.setSentTime(message.getSentTime());
                this.mConversation.setSenderUserId(message.getSenderUserId());
                this.refreshListWhileReceiveMessage(message);
            } else {
                ((UIMessage)this.mAdapter.getItem(position)).setSentStatus(message.getSentStatus());
                ((UIMessage)this.mAdapter.getItem(position)).setExtra(message.getExtra());
                ((UIMessage)this.mAdapter.getItem(position)).setSentTime(message.getSentTime());
                ((UIMessage)this.mAdapter.getItem(position)).setUId(message.getUId());
                ((UIMessage)this.mAdapter.getItem(position)).setContent(message.getContent());
                this.getHandler().obtainMessage(4, Integer.valueOf(position)).sendToTarget();
            }
        }

    }

    public void onEventMainThread(GroupUserInfoEvent event) {
        GroupUserInfo userInfo = event.getUserInfo();
        if(userInfo != null && userInfo.getNickname() != null) {
            if(this.mList != null && this.isResumed()) {
                int first = this.mList.getFirstVisiblePosition() - this.mList.getHeaderViewsCount();
                int last = this.mList.getLastVisiblePosition() - this.mList.getHeaderViewsCount();
                int index = first - 1;

                while(true) {
                    Message message;
                    do {
                        do {
                            ++index;
                            if(index > last || index < 0 || index >= this.mAdapter.getCount()) {
                                return;
                            }

                            message = (Message)this.mAdapter.getItem(index);
                        } while(message == null);
                    } while(!TextUtils.isEmpty(message.getSenderUserId()) && !userInfo.getUserId().equals(message.getSenderUserId()));

                    this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition() + this.mList.getHeaderViewsCount()), this.mList);
                }
            }
        }
    }

    public void onEventMainThread(OnMessageSendErrorEvent event) {
        Message msg = event.getMessage();
        this.onEventMainThread(msg);
    }

    public void onEventMainThread(OnReceiveMessageEvent event) {
        this.mMessageleft = event.getLeft();
        this.onEventMainThread(event.getMessage());
    }

    public void onEventMainThread(MessageContent messageContent) {
        if(this.mList != null && this.isResumed()) {
            int first = this.mList.getFirstVisiblePosition() - this.mList.getHeaderViewsCount();
            int last = this.mList.getLastVisiblePosition() - this.mList.getHeaderViewsCount();
            int index = first - 1;

            while(true) {
                ++index;
                if(index > last || index < 0 || index >= this.mAdapter.getCount()) {
                    break;
                }

                if(((UIMessage)this.mAdapter.getItem(index)).getContent().equals(messageContent)) {
                    this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition() + this.mList.getHeaderViewsCount()), this.mList);
                    break;
                }
            }
        }

    }

    public void onEventMainThread(PlayAudioEvent event) {
        if(this.mList != null && this.isResumed()) {
            int first = this.mList.getFirstVisiblePosition() - this.mList.getHeaderViewsCount();
            int last = this.mList.getLastVisiblePosition() - this.mList.getHeaderViewsCount();

            int index;
            UIMessage uiMessage;
            for(index = first; index <= last && index >= 0 && index < this.mAdapter.getCount(); ++index) {
                uiMessage = (UIMessage)this.mAdapter.getItem(index);
                if(uiMessage.getContent().equals(event.content)) {
                    uiMessage.continuePlayAudio = false;
                    break;
                }
            }

            ++index;
            if(event.continuously) {
                while(index <= last && index >= 0 && index < this.mAdapter.getCount()) {
                    uiMessage = (UIMessage)this.mAdapter.getItem(index);
                    if(uiMessage.getContent() instanceof VoiceMessage && uiMessage.getMessageDirection().equals(MessageDirection.RECEIVE) && !uiMessage.getReceivedStatus().isListened()) {
                        uiMessage.continuePlayAudio = true;
                        this.mAdapter.getView(index, this.mList.getChildAt(index - first), this.mList);
                        break;
                    }

                    ++index;
                }
            }
        }

    }

    public void onEventMainThread(OnReceiveMessageProgressEvent event) {
        if(this.mList != null && this.isResumed()) {
            int first = this.mList.getFirstVisiblePosition() - this.mList.getHeaderViewsCount();
            int last = this.mList.getLastVisiblePosition() - this.mList.getHeaderViewsCount();
            int index = first - 1;

            while(true) {
                ++index;
                if(index > last || index < 0 || index >= this.mAdapter.getCount()) {
                    break;
                }

                UIMessage uiMessage = (UIMessage)this.mAdapter.getItem(index);
                if(uiMessage.getMessageId() == event.getMessage().getMessageId()) {
                    uiMessage.setProgress(event.getProgress());
                    this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition() + this.mList.getHeaderViewsCount()), this.mList);
                    break;
                }
            }
        }

    }

    public void onEventMainThread(Event event) {
        if(this.mAdapter != null) {
            if(event == Event.ACTION) {
                this.getHandler().sendEmptyMessage(10);
            }

        }
    }

    public void onEventMainThread(UserInfo userInfo) {
        if(this.mList != null) {
            int first = this.mList.getFirstVisiblePosition() - this.mList.getHeaderViewsCount();
            int last = this.mList.getLastVisiblePosition() - this.mList.getHeaderViewsCount();
            int index = first - 1;

            while(true) {
                UIMessage uiMessage;
                do {
                    do {
                        ++index;
                        if(index > last || index < 0 || index >= this.mAdapter.getCount()) {
                            return;
                        }

                        uiMessage = (UIMessage)this.mAdapter.getItem(index);
                    } while(uiMessage == null);
                } while(!TextUtils.isEmpty(uiMessage.getSenderUserId()) && !userInfo.getUserId().equals(uiMessage.getSenderUserId()));

                uiMessage.setUserInfo(userInfo);
                this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition() + this.mList.getHeaderViewsCount()), this.mList);
            }
        }
    }

    public void onEventMainThread(PublicServiceProfile publicServiceProfile) {
        if(this.mList != null && this.isResumed() && this.mAdapter != null) {
            int first = this.mList.getFirstVisiblePosition() - this.mList.getHeaderViewsCount();
            int last = this.mList.getLastVisiblePosition() - this.mList.getHeaderViewsCount();
            int index = first - 1;

            while(true) {
                Message message;
                do {
                    do {
                        ++index;
                        if(index > last || index < 0 || index >= this.mAdapter.getCount()) {
                            return;
                        }

                        message = (Message)this.mAdapter.getItem(index);
                    } while(message == null);
                } while(!TextUtils.isEmpty(message.getTargetId()) && !publicServiceProfile.getTargetId().equals(message.getTargetId()));

                this.mAdapter.getView(index, this.mList.getChildAt(index - this.mList.getFirstVisiblePosition() + this.mList.getHeaderViewsCount()), this.mList);
            }
        }
    }

    private void getConversation() {
        RongIM.getInstance().getConversation(this.mConversation.getConversationType(), this.mConversation.getTargetId(), new ResultCallback<Conversation>() {
            public void onSuccess(Conversation conversation) {
                if(conversation != null) {
                    if(!TextUtils.isEmpty(CustomMessageListFragment.this.mConversation.getConversationTitle())) {
                        conversation.setConversationTitle(CustomMessageListFragment.this.mConversation.getConversationTitle());
                    }

                    CustomMessageListFragment.this.mConversation = conversation;
                    if(CustomMessageListFragment.this.isShowUnreadMessageState && conversation.getConversationType() != ConversationType.APP_PUBLIC_SERVICE && conversation.getConversationType() != ConversationType.PUBLIC_SERVICE && conversation.getConversationType() != ConversationType.CUSTOMER_SERVICE && conversation.getConversationType() != ConversationType.CHATROOM) {
                        CustomMessageListFragment.this.mUnreadCount = CustomMessageListFragment.this.mConversation.getUnreadMessageCount();
                    }

                    if(CustomMessageListFragment.this.mUnreadCount > 150) {
                        CustomMessageListFragment.this.mUnreadBtn.setText("150+" + CustomMessageListFragment.this.getResources().getString(string.rc_new_messages));
                    } else {
                        CustomMessageListFragment.this.mUnreadBtn.setText(CustomMessageListFragment.this.mUnreadCount + CustomMessageListFragment.this.getResources().getString(string.rc_new_messages));
                    }

                    CustomMessageListFragment.this.sendReadReceiptAndSyncUnreadStatus(conversation.getConversationType(), conversation.getTargetId(), conversation.getSentTime());
                    CustomMessageListFragment.this.mHasReceivedMessage = false;
                    CustomMessageListFragment.this.mUnreadBtn.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            CustomMessageListFragment.this.isOnClickBtn = true;
                            CustomMessageListFragment.this.mUnreadBtn.setClickable(false);
                            TranslateAnimation animation = new TranslateAnimation(0.0F, 500.0F, 0.0F, 0.0F);
                            animation.setDuration(500L);
                            CustomMessageListFragment.this.mUnreadBtn.startAnimation(animation);
                            animation.setFillAfter(true);
                            animation.setAnimationListener(new AnimationListener() {
                                public void onAnimationStart(Animation animation) {
                                }

                                public void onAnimationEnd(Animation animation) {
                                    CustomMessageListFragment.this.mUnreadBtn.setVisibility(View.GONE);
                                    if(CustomMessageListFragment.this.mUnreadCount <= 30) {
                                        if(CustomMessageListFragment.this.mList.getCount() < 30) {
                                            CustomMessageListFragment.this.mList.smoothScrollToPosition(CustomMessageListFragment.this.mList.getCount() - CustomMessageListFragment.this.mUnreadCount);
                                        } else {
                                            CustomMessageListFragment.this.mList.smoothScrollToPosition(30 - CustomMessageListFragment.this.mUnreadCount);
                                        }
                                    } else if(CustomMessageListFragment.this.mUnreadCount >= 30) {
                                        CustomMessageListFragment.this.getHandler().sendEmptyMessage(12);
                                    }

                                }

                                public void onAnimationRepeat(Animation animation) {
                                }
                            });
                        }
                    });
                    if(CustomMessageListFragment.this.mConversation.getMentionedCount() > 0) {
                        RongIMClient.getInstance().getUnreadMentionedMessages(CustomMessageListFragment.this.mConversation.getConversationType(), CustomMessageListFragment.this.mConversation.getTargetId(), new ResultCallback<List<Message>>() {
                            public void onSuccess(List<Message> messages) {
                                CustomMessageListFragment.this.mUnreadMentionMessages = messages;
                            }

                            public void onError(ErrorCode e) {
                            }
                        });
                    }

                    if(CustomMessageListFragment.this.mConversation != null && CustomMessageListFragment.this.mConversation.getConversationType() != ConversationType.CHATROOM) {
                        RongIM.getInstance().clearMessagesUnreadStatus(CustomMessageListFragment.this.mConversation.getConversationType(), CustomMessageListFragment.this.mConversation.getTargetId(), (ResultCallback)null);
                    }
                }

                CustomMessageListFragment.this.getHandler().sendEmptyMessage(1);
            }

            public void onError(ErrorCode e) {
                RLog.e("CustomMessageListFragment", "fail, " + e.toString());
            }
        });
    }

    public void onEventMainThread(ConnectEvent event) {
        RLog.d("CustomMessageListFragment", "onEventMainThread Event.ConnectEvent: isListRetrieved = " + this.isShowWithoutConnected);
        if(this.isShowWithoutConnected) {
            this.getConversation();
            if(this.mConversation.getConversationType() != ConversationType.CHATROOM) {
                RongIM.getInstance().clearMessagesUnreadStatus(this.mConversation.getConversationType(), this.mConversation.getTargetId(), (ResultCallback)null);
            }

            this.isShowWithoutConnected = false;
        }
    }

    public void onEventMainThread(ReadReceiptRequestEvent event) {
        if(this.mConversation.getConversationType().equals(ConversationType.GROUP) || this.mConversation.getConversationType().equals(ConversationType.DISCUSSION)) {
            if(RongContext.getInstance().isReadReceiptConversationType(event.getConversationType())) {
                if(event.getConversationType().equals(this.mConversation.getConversationType()) && event.getTargetId().equals(this.mConversation.getTargetId())) {
                    for(int i = 0; i < this.mAdapter.getCount(); ++i) {
                        if(((UIMessage)this.mAdapter.getItem(i)).getUId().equals(event.getMessageUId())) {
                            final UIMessage uiMessage = (UIMessage)this.mAdapter.getItem(i);
                            ReadReceiptInfo readReceiptInfo = uiMessage.getReadReceiptInfo();
                            if(readReceiptInfo == null) {
                                readReceiptInfo = new ReadReceiptInfo();
                                uiMessage.setReadReceiptInfo(readReceiptInfo);
                            }

                            if(readReceiptInfo.isReadReceiptMessage() && readReceiptInfo.hasRespond()) {
                                return;
                            }

                            readReceiptInfo.setIsReadReceiptMessage(true);
                            readReceiptInfo.setHasRespond(false);
                            ArrayList messageList = new ArrayList();
                            messageList.add(((UIMessage)this.mAdapter.getItem(i)).getMessage());
                            RongIMClient.getInstance().sendReadReceiptResponse(event.getConversationType(), event.getTargetId(), messageList, new OperationCallback() {
                                public void onSuccess() {
                                    uiMessage.getReadReceiptInfo().setHasRespond(true);
                                }

                                public void onError(ErrorCode errorCode) {
                                    RLog.e("CustomMessageListFragment", "sendReadReceiptResponse failed, errorCode = " + errorCode);
                                }
                            });
                            return;
                        }
                    }
                }

            }
        }
    }

    public void onEventMainThread(ReadReceiptResponseEvent event) {
        if(this.mConversation.getConversationType().equals(ConversationType.GROUP) || this.mConversation.getConversationType().equals(ConversationType.DISCUSSION)) {
            if(RongContext.getInstance().isReadReceiptConversationType(event.getConversationType())) {
                if(event.getConversationType().equals(this.mConversation.getConversationType()) && event.getTargetId().equals(this.mConversation.getTargetId())) {
                    for(int i = 0; i < this.mAdapter.getCount(); ++i) {
                        if(((UIMessage)this.mAdapter.getItem(i)).getUId().equals(event.getMessageUId())) {
                            UIMessage uiMessage = (UIMessage)this.mAdapter.getItem(i);
                            ReadReceiptInfo readReceiptInfo = uiMessage.getReadReceiptInfo();
                            if(readReceiptInfo == null) {
                                readReceiptInfo = new ReadReceiptInfo();
                                readReceiptInfo.setIsReadReceiptMessage(true);
                                uiMessage.setReadReceiptInfo(readReceiptInfo);
                            }

                            readReceiptInfo.setRespondUserIdList(event.getResponseUserIdList());
                            this.getHandler().obtainMessage(4, Integer.valueOf(i)).sendToTarget();
                            return;
                        }
                    }
                }

            }
        }
    }

    public void onPause() {
        super.onPause();
        RongContext.getInstance().getEventBus().post(Event.DESTROY);
    }

    public void onResume() {
        super.onResume();
        if(RongIM.getInstance().getCurrentConnectionStatus().equals(ConnectionStatus.DISCONNECTED)) {
            this.isShowWithoutConnected = true;
            RLog.e("CustomMessageListFragment", "onResume Not connected yet.");
        }

        if(this.mList.getLastVisiblePosition() == this.mList.getCount() - 1) {
            this.mNewMessageCount = 0;
            this.mNewMessageTextView.setVisibility(View.GONE);
            this.mNewMessageBtn.setVisibility(View.GONE);
        }

    }

    public void onEventMainThread(MessageDeleteEvent deleteEvent) {
        if(deleteEvent.getMessageIds() != null) {
            boolean hasChanged = false;
            boolean position = false;
            Iterator i$ = deleteEvent.getMessageIds().iterator();

            while(i$.hasNext()) {
                long item = (long)((Integer)i$.next()).intValue();
                int position1 = this.mAdapter.findPosition(item);
                if(position1 >= 0) {
                    this.mAdapter.remove(position1);
                    hasChanged = true;
                }
            }

            if(hasChanged) {
                this.mAdapter.notifyDataSetChanged();
                this.getHandler().obtainMessage(11).sendToTarget();
            }
        }

    }

    public void onEventMainThread(PublicServiceFollowableEvent event) {
        if(event != null && !event.isFollow()) {
            this.getActivity().finish();
        }

    }

    public void onEventMainThread(MessagesClearEvent clearEvent) {
        if(clearEvent.getTargetId().equals(this.mConversation.getTargetId()) && clearEvent.getType().equals(this.mConversation.getConversationType())) {
            this.mAdapter.removeAll();
            this.getHandler().post(new Runnable() {
                public void run() {
                    CustomMessageListFragment.this.mList.setTranscriptMode(1);
                    CustomMessageListFragment.this.mList.setStackFromBottom(false);
                    CustomMessageListFragment.this.mAdapter.notifyDataSetChanged();
                }
            });
            this.mAdapter.notifyDataSetChanged();
        }

    }

    public void onEventMainThread(MessageRecallEvent event) {
        if(event.isRecallSuccess()) {
            RecallNotificationMessage recallNotificationMessage = event.getRecallNotificationMessage();
            int position = this.mAdapter.findPosition((long)event.getMessageId());
            if(position != -1) {
                ((UIMessage)this.mAdapter.getItem(position)).setContent(recallNotificationMessage);
                this.getHandler().obtainMessage(4, Integer.valueOf(position)).sendToTarget();
            }
        } else {
            Toast.makeText(this.getActivity(), string.rc_recall_failed, View.VISIBLE).show();
        }

    }

    public void onEventMainThread(RemoteMessageRecallEvent event) {
        if(event.isRecallSuccess()) {
            RecallNotificationMessage recallNotificationMessage = event.getRecallNotificationMessage();
            if(AudioPlayManager.getInstance().getPlayingUri() != null) {
                String position = AudioPlayManager.getInstance().getPlayingUri().toString();
                int i = position.lastIndexOf(47);
                int j = position.lastIndexOf(46);
                String sub = null;
                boolean matchId = false;

                try {
                    sub = position.substring(i + 1, j);
                    int matchId1 = Integer.parseInt(sub);
                    if(matchId1 == event.getMessageId()) {
                        AudioPlayManager.getInstance().stopPlay();
                    }
                } catch (Exception var9) {
                    var9.printStackTrace();
                }
            }

            int position1 = this.mAdapter.findPosition((long)event.getMessageId());
            if(position1 != -1) {
                ((UIMessage)this.mAdapter.getItem(position1)).setContent(recallNotificationMessage);
                this.getHandler().obtainMessage(4, Integer.valueOf(position1)).sendToTarget();
            }
        }

    }

    public void onDestroy() {
        RongContext.getInstance().getEventBus().unregister(this);
        if(this.mConversation.getConversationType() != ConversationType.CHATROOM) {
            RongIM.getInstance().clearMessagesUnreadStatus(this.mConversation.getConversationType(), this.mConversation.getTargetId(), (ResultCallback)null);
        }

        if(this.mConversation != null) {
            boolean syncReadStatus = false;

            try {
                syncReadStatus = this.getResources().getBoolean(bool.rc_enable_sync_read_status);
            } catch (NotFoundException var3) {
                RLog.e("CustomMessageListFragment", "rc_enable_sync_unread_status not configure in rc_config.xml");
                var3.printStackTrace();
            }

            if(syncReadStatus && (this.mConversation.getConversationType() == ConversationType.GROUP || this.mConversation.getConversationType() == ConversationType.DISCUSSION) && this.mHasReceivedMessage) {
                RongIMClient.getInstance().syncConversationReadStatus(this.mConversation.getConversationType(), this.mConversation.getTargetId(), this.mConversation.getSentTime(), (OperationCallback)null);
            }

            this.mHasReceivedMessage = false;
        }

        super.onDestroy();
    }

    public void setAdapter(CustomMessageListAdapter adapter) {
        if(this.mAdapter != null) {
            this.mAdapter.clear();
        }

        this.mAdapter = adapter;
        if(this.mList != null && this.getUri() != null) {
            this.mList.setAdapter(adapter);
            this.initFragment(this.getUri());
        }

    }

    public MessageListAdapter getAdapter() {
        return this.mAdapter;
    }

    public void setNeedEvaluateForRobot(boolean needEvaluate) {
        this.needEvaluateForRobot = needEvaluate;
    }

    public void setRobotMode(boolean robotMode) {
        this.robotMode = robotMode;
    }

    private void sendReadReceiptAndSyncUnreadStatus(ConversationType type, String targetId, long timeStamp) {
        boolean readRec = false;
        boolean syncReadStatus = false;

        try {
            readRec = this.getResources().getBoolean(bool.rc_read_receipt);
            syncReadStatus = this.getResources().getBoolean(bool.rc_enable_sync_read_status);
        } catch (NotFoundException var8) {
            RLog.e("CustomMessageListFragment", "rc_read_receipt not configure in rc_config.xml");
            var8.printStackTrace();
        }

        if(type == ConversationType.PRIVATE) {
            if(readRec && RongContext.getInstance().isReadReceiptConversationType(ConversationType.PRIVATE)) {
                if(this.mConversation.getUnreadMessageCount() > 0) {
                    RongIMClient.getInstance().sendReadReceiptMessage(type, targetId, timeStamp);
                }
            } else if(syncReadStatus && this.mConversation.getUnreadMessageCount() > 0) {
                RongIMClient.getInstance().syncConversationReadStatus(type, targetId, timeStamp, (OperationCallback)null);
            }
        } else if((type == ConversationType.GROUP || type == ConversationType.DISCUSSION) && syncReadStatus && this.mConversation.getUnreadMessageCount() > 0) {
            RongIMClient.getInstance().syncConversationReadStatus(type, targetId, timeStamp, (OperationCallback)null);
        }

    }

    public static class Builder {
        private ConversationType conversationType;
        private String targetId;
        private Uri uri;

        public Builder() {
        }

        public ConversationType getConversationType() {
            return this.conversationType;
        }

        public void setConversationType(ConversationType conversationType) {
            this.conversationType = conversationType;
        }

        public String getTargetId() {
            return this.targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }
    }

    public class ScrollRunnable implements Runnable {
        public ScrollRunnable() {
        }

        public void run() {
            if(CustomMessageListFragment.this.mList.getLastVisiblePosition() < CustomMessageListFragment.this.mList.getCount() - 1) {
                CustomMessageListFragment.this.mList.setSelection(CustomMessageListFragment.this.mList.getLastVisiblePosition() + 10);
                CustomMessageListFragment.this.getHandler().postDelayed(CustomMessageListFragment.this.new ScrollRunnable(), 100L);
            }

        }
    }


    public void setRightImageUrl(String rightImageUrl) {
        this.mRightImageUrl = rightImageUrl;
        if(mAdapter!=null){
            mAdapter.setRightImageUrl(rightImageUrl);
        }
    }

    public void setLeftImageUrl(String leftImageUrl) {
        this.mLeftImageUrl = leftImageUrl;
        if(mAdapter!=null){
            mAdapter.setLeftImageUrl(leftImageUrl);
        }
    }
}
