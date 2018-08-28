package io.rong.imkit.widget.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import io.rong.common.RLog;
import io.rong.eventbus.EventBus;
import io.rong.imkit.R;
import io.rong.imkit.RongContext;
import io.rong.imkit.model.ConversationKey;
import io.rong.imkit.model.Event;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.utils.RongDateUtils;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imkit.widget.ProviderContainerView;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.PublicServiceProfile;
import io.rong.imlib.model.UnknownMessage;
import io.rong.imlib.model.UserInfo;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.TextMessage;

/**
 * Created by Administrator on 2016/9/1View.GONE.
 */
public class CustomMessageListAdapter extends MessageListAdapter {

    private boolean timeGone = false;
    private String mRightImageUrl;
    private String mLeftImageUrl;

    public CustomMessageListAdapter(Context context,String rightImageUrl,String leftImageUrl) {
        super(context);
        this.mRightImageUrl = rightImageUrl;
        this.mLeftImageUrl = leftImageUrl;
    }


    protected View newView(Context context, int position, ViewGroup group) {
        View result = this.mInflater.inflate(R.layout.rc_item_message, (ViewGroup)null);
        MessageListAdapter.ViewHolder holder = new MessageListAdapter.ViewHolder();
        holder.leftIconView = (AsyncImageView)this.findViewById(result, R.id.rc_left);
        holder.rightIconView = (AsyncImageView)this.findViewById(result, R.id.rc_right);
        holder.nameView = (TextView)this.findViewById(result, R.id.rc_title);
        holder.contentView = (ProviderContainerView)this.findViewById(result, R.id.rc_content);
        holder.layout = (ViewGroup)this.findViewById(result, R.id.rc_layout);
        holder.progressBar = (ProgressBar)this.findViewById(result, R.id.rc_progress);
        holder.warning = (ImageView)this.findViewById(result, R.id.rc_warning);
        holder.readReceipt = (ImageView)this.findViewById(result, R.id.rc_read_receipt);
        holder.time = (TextView)this.findViewById(result, R.id.rc_time);
        holder.sentStatus = (TextView)this.findViewById(result, R.id.rc_sent_status);
        if(holder.time.getVisibility() == View.GONE) {
            this.timeGone = true;
        } else {
            this.timeGone = false;
        }

        result.setTag(holder);
        return result;
    }


    protected void bindView(View v, final int position, final UIMessage data) {
        MessageListAdapter.ViewHolder holder = (MessageListAdapter.ViewHolder)v.getTag();
        Object provider = null;
        ProviderTag tag = null;
        if(this.getNeedEvaluate(data)) {
            provider = RongContext.getInstance().getEvaluateProvider();
            tag = RongContext.getInstance().getMessageProviderTag(data.getContent().getClass());
        } else {
            if(RongContext.getInstance() == null || data == null || data.getContent() == null) {
                RLog.e("MessageListAdapter", "Message is null !");
                return;
            }

            provider = RongContext.getInstance().getMessageTemplate(data.getContent().getClass());
            if(provider == null) {
                provider = RongContext.getInstance().getMessageTemplate(UnknownMessage.class);
                tag = RongContext.getInstance().getMessageProviderTag(UnknownMessage.class);
            } else {
                tag = RongContext.getInstance().getMessageProviderTag(data.getContent().getClass());
            }

            if(provider == null) {
                RLog.e("MessageListAdapter", data.getObjectName() + " message provider not found !");
                return;
            }
        }

        View view = holder.contentView.inflate((IContainerItemProvider)provider);
        ((IContainerItemProvider)provider).bindView(view, position, data);
        this.subView = view;
        if(tag == null) {
            RLog.e("MessageListAdapter", "Can not find ProviderTag for " + data.getObjectName());
        } else {
            if(tag.hide()) {
                holder.contentView.setVisibility(View.GONE);
                holder.time.setVisibility(View.GONE);
                holder.nameView.setVisibility(View.GONE);
                holder.leftIconView.setVisibility(View.GONE);
                holder.rightIconView.setVisibility(View.GONE);
            } else {
                holder.contentView.setVisibility(View.VISIBLE);
            }

            UserInfo time1;
            if(data.getMessageDirection() == Message.MessageDirection.SEND) {
                if(tag.showPortrait()) {
                    holder.rightIconView.setVisibility(View.VISIBLE);
                    holder.leftIconView.setVisibility(View.GONE);
                } else {
                    holder.leftIconView.setVisibility(View.GONE);
                    holder.rightIconView.setVisibility(View.GONE);
                }

                if(!tag.centerInHorizontal()) {
                    this.setGravity(holder.layout, 5);
                    holder.contentView.containerViewRight();
                    holder.nameView.setGravity(5);
                } else {
                    this.setGravity(holder.layout, 17);
                    holder.contentView.containerViewCenter();
                    holder.nameView.setGravity(1);
                    holder.contentView.setBackgroundColor(0);
                }

                boolean time = false;

                try {
                    time = this.mContext.getResources().getBoolean(R.bool.rc_read_receipt);
                } catch (Resources.NotFoundException var12) {
                    RLog.e("MessageListAdapter", "rc_read_receipt not configure in rc_config.xml");
                    var12.printStackTrace();
                }

                if(data.getSentStatus() == Message.SentStatus.SENDING) {
                    if(tag.showProgress()) {
                        holder.progressBar.setVisibility(View.VISIBLE);
                    } else {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    holder.warning.setVisibility(View.GONE);
                    holder.readReceipt.setVisibility(View.GONE);
                } else if(data.getSentStatus() == Message.SentStatus.FAILED) {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.warning.setVisibility(View.VISIBLE);
                    holder.readReceipt.setVisibility(View.GONE);
                } else if(data.getSentStatus() == Message.SentStatus.SENT) {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.warning.setVisibility(View.GONE);
                    holder.readReceipt.setVisibility(View.GONE);
                } else if(time && data.getSentStatus() == Message.SentStatus.READ) {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.warning.setVisibility(View.GONE);
                    MessageContent pre = data.getMessage().getContent();
                    if(!(pre instanceof InformationNotificationMessage)) {
                        holder.readReceipt.setVisibility(View.VISIBLE);
                    } else {
                        holder.readReceipt.setVisibility(View.GONE);
                    }
                } else {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.warning.setVisibility(View.GONE);
                    holder.readReceipt.setVisibility(View.GONE);
                }

                if(data.getObjectName().equals("RC:VSTMsg")) {
                    holder.readReceipt.setVisibility(View.GONE);
                }

                holder.nameView.setVisibility(View.GONE);
                holder.rightIconView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if(RongContext.getInstance().getConversationBehaviorListener() != null) {
                            UserInfo userInfo = null;
                            if(!TextUtils.isEmpty(data.getSenderUserId())) {
                                userInfo = RongUserInfoManager.getInstance().getUserInfo(data.getSenderUserId());
                                userInfo = userInfo == null?new UserInfo(data.getSenderUserId(), (String)null, (Uri)null):userInfo;
                            }

                            RongContext.getInstance().getConversationBehaviorListener().onUserPortraitClick(CustomMessageListAdapter.this.mContext, data.getConversationType(), userInfo);
                        }

                    }
                });
                holder.rightIconView.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        if(RongContext.getInstance().getConversationBehaviorListener() != null) {
                            UserInfo userInfo = null;
                            if(!TextUtils.isEmpty(data.getSenderUserId())) {
                                userInfo = RongUserInfoManager.getInstance().getUserInfo(data.getSenderUserId());
                                userInfo = userInfo == null?new UserInfo(data.getSenderUserId(), (String)null, (Uri)null):userInfo;
                            }

                            return RongContext.getInstance().getConversationBehaviorListener().onUserPortraitLongClick(CustomMessageListAdapter.this.mContext, data.getConversationType(), userInfo);
                        } else {
                            return true;
                        }
                    }
                });
                if(!tag.showWarning()) {
                    holder.warning.setVisibility(View.GONE);
                }
            } else {
                if(tag.showPortrait()) {
                    holder.rightIconView.setVisibility(View.GONE);
                    holder.leftIconView.setVisibility(View.VISIBLE);
                } else {
                    holder.leftIconView.setVisibility(View.GONE);
                    holder.rightIconView.setVisibility(View.GONE);
                }

                if(!tag.centerInHorizontal()) {
                    this.setGravity(holder.layout, 3);
                    holder.contentView.containerViewLeft();
                    holder.nameView.setGravity(3);
                } else {
                    this.setGravity(holder.layout, 17);
                    holder.contentView.containerViewCenter();
                    holder.nameView.setGravity(1);
                    holder.contentView.setBackgroundColor(0);
                }

                holder.progressBar.setVisibility(View.GONE);
                holder.warning.setVisibility(View.GONE);
                holder.readReceipt.setVisibility(View.GONE);
                holder.nameView.setVisibility(View.VISIBLE);
                if(data.getConversationType() != Conversation.ConversationType.PRIVATE && tag.showPortrait() && data.getConversationType() != Conversation.ConversationType.PUBLIC_SERVICE && data.getConversationType() != Conversation.ConversationType.APP_PUBLIC_SERVICE) {
                    if(data.getConversationType().equals(Conversation.ConversationType.CUSTOMER_SERVICE) && data.getUserInfo() != null && data.getMessageDirection().equals(Message.MessageDirection.RECEIVE)) {
                        time1 = data.getUserInfo();
                        holder.nameView.setText(time1.getName());
                    } else if(data.getConversationType() == Conversation.ConversationType.GROUP) {
                        GroupUserInfo pre1 = RongUserInfoManager.getInstance().getGroupUserInfo(data.getTargetId(), data.getSenderUserId());
                        if(pre1 != null) {
                            holder.nameView.setText(pre1.getNickname());
                        } else {
                            time1 = RongUserInfoManager.getInstance().getUserInfo(data.getSenderUserId());
                            if(time1 == null) {
                                holder.nameView.setText(data.getSenderUserId());
                            } else {
                                holder.nameView.setText(time1.getName());
                            }
                        }
                    } else {
                        time1 = RongUserInfoManager.getInstance().getUserInfo(data.getSenderUserId());
                        if(time1 == null) {
                            holder.nameView.setText(data.getSenderUserId());
                        } else {
                            holder.nameView.setText(time1.getName());
                        }
                    }
                } else {
                    holder.nameView.setVisibility(View.GONE);
                }

                holder.leftIconView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if(RongContext.getInstance().getConversationBehaviorListener() != null) {
                            UserInfo userInfo = null;
                            if(!TextUtils.isEmpty(data.getSenderUserId())) {
                                userInfo = RongUserInfoManager.getInstance().getUserInfo(data.getSenderUserId());
                                userInfo = userInfo == null?new UserInfo(data.getSenderUserId(), (String)null, (Uri)null):userInfo;
                            }

                            RongContext.getInstance().getConversationBehaviorListener().onUserPortraitClick(CustomMessageListAdapter.this.mContext, data.getConversationType(), userInfo);
                        }

                        EventBus.getDefault().post(Event.InputViewEvent.obtain(false));
                    }
                });
            }

            holder.leftIconView.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    if(RongContext.getInstance().getConversationBehaviorListener() != null) {
                        UserInfo userInfo = null;
                        if(!TextUtils.isEmpty(data.getSenderUserId())) {
                            userInfo = RongUserInfoManager.getInstance().getUserInfo(data.getSenderUserId());
                            userInfo = userInfo == null?new UserInfo(data.getSenderUserId(), (String)null, (Uri)null):userInfo;
                        }

                        return RongContext.getInstance().getConversationBehaviorListener().onUserPortraitLongClick(CustomMessageListAdapter.this.mContext, data.getConversationType(), userInfo);
                    } else {
                        return false;
                    }
                }
            });
            PublicServiceProfile publicServiceProfile;
            ConversationKey mKey;
            Uri pre2;
            if(holder.rightIconView.getVisibility() == View.VISIBLE) {
                if(data.getConversationType().equals(Conversation.ConversationType.CUSTOMER_SERVICE) && data.getUserInfo() != null && data.getMessageDirection().equals(Message.MessageDirection.RECEIVE)) {
                    time1 = data.getUserInfo();
                    pre2 = time1.getPortraitUri();
                    if(mRightImageUrl != null) {
                        holder.rightIconView.setAvatar(mRightImageUrl, 0);
                    }
                } else if((data.getConversationType().equals(Conversation.ConversationType.PUBLIC_SERVICE) || data.getConversationType().equals(Conversation.ConversationType.APP_PUBLIC_SERVICE)) && data.getMessageDirection().equals(Message.MessageDirection.RECEIVE)) {
                    time1 = data.getUserInfo();
                    if(time1 != null) {
                        pre2 = time1.getPortraitUri();
                        if(mRightImageUrl != null) {
                            holder.leftIconView.setAvatar(mRightImageUrl, 0);
                        }
                    } else {
                        mKey = ConversationKey.obtain(data.getTargetId(), data.getConversationType());
                        publicServiceProfile = RongContext.getInstance().getPublicServiceInfoFromCache(mKey.getKey());
                        pre2 = publicServiceProfile.getPortraitUri();
                        if(mRightImageUrl != null) {
                            holder.rightIconView.setAvatar(mRightImageUrl, 0);
                        }
                    }
                } else if(!TextUtils.isEmpty(data.getSenderUserId())) {
                    time1 = RongUserInfoManager.getInstance().getUserInfo(data.getSenderUserId());
                    if(time1 != null && mRightImageUrl != null) {
                        holder.rightIconView.setAvatar(mRightImageUrl, 0);
                    }
                }
            } else if(holder.leftIconView.getVisibility() == View.VISIBLE) {
                if(data.getConversationType().equals(Conversation.ConversationType.CUSTOMER_SERVICE) && data.getUserInfo() != null && data.getMessageDirection().equals(Message.MessageDirection.RECEIVE)) {
                    time1 = data.getUserInfo();
                    pre2 = time1.getPortraitUri();
                    if(mLeftImageUrl != null) {
                        holder.leftIconView.setAvatar(mLeftImageUrl, 0);
                    }
                } else if((data.getConversationType().equals(Conversation.ConversationType.PUBLIC_SERVICE) || data.getConversationType().equals(Conversation.ConversationType.APP_PUBLIC_SERVICE)) && data.getMessageDirection().equals(Message.MessageDirection.RECEIVE)) {
                    time1 = data.getUserInfo();
                    if(time1 != null) {
                        pre2 = time1.getPortraitUri();
                        if(mLeftImageUrl != null) {
                            holder.leftIconView.setAvatar(mLeftImageUrl, 0);
                        }
                    } else {
                        mKey = ConversationKey.obtain(data.getTargetId(), data.getConversationType());
                        publicServiceProfile = RongContext.getInstance().getPublicServiceInfoFromCache(mKey.getKey());
                        if(publicServiceProfile != null && mLeftImageUrl != null) {
                            holder.leftIconView.setAvatar(mLeftImageUrl, 0);
                        }
                    }
                } else if(!TextUtils.isEmpty(data.getSenderUserId())) {
                    time1 = RongUserInfoManager.getInstance().getUserInfo(data.getSenderUserId());
                    if(time1 != null && mLeftImageUrl != null) {
                        holder.leftIconView.setAvatar(mLeftImageUrl, 0);
                    }
                }
            }

            if(view != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if(RongContext.getInstance().getConversationBehaviorListener() == null || !RongContext.getInstance().getConversationBehaviorListener().onMessageClick(CustomMessageListAdapter.this.mContext, v, data)) {
                            Object provider;
                            if(CustomMessageListAdapter.this.getNeedEvaluate(data)) {
                                provider = RongContext.getInstance().getEvaluateProvider();
                            } else {
                                provider = RongContext.getInstance().getMessageTemplate(data.getContent().getClass());
                            }

                            if(provider != null) {
                                ((IContainerItemProvider.MessageProvider)provider).onItemClick(v, position, data.getContent(), data);
                            }

                        }
                    }
                });
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        if(RongContext.getInstance().getConversationBehaviorListener() != null && RongContext.getInstance().getConversationBehaviorListener().onMessageLongClick(CustomMessageListAdapter.this.mContext, v, data)) {
                            return true;
                        } else {
                            Object provider;
                            if(CustomMessageListAdapter.this.getNeedEvaluate(data)) {
                                provider = RongContext.getInstance().getEvaluateProvider();
                            } else {
                                provider = RongContext.getInstance().getMessageTemplate(data.getContent().getClass());
                            }

                            if(provider != null) {
                                ((IContainerItemProvider.MessageProvider)provider).onItemLongClick(v, position, data.getContent(), data);
                            }

                            return true;
                        }
                    }
                });
            }

            holder.warning.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (CustomMessageListAdapter.this.mOnItemHandlerListener != null) {
                        CustomMessageListAdapter.this.mOnItemHandlerListener.onWarningViewClick(position, data, v);
                    }

                }
            });
            if(tag.hide()) {
                holder.time.setVisibility(View.GONE);
            } else {
                if(!this.timeGone) {
                    String time2 = RongDateUtils.getConversationFormatDate(data.getSentTime(),mContext);
                    holder.time.setText(time2);
                    if(position == 0) {
                        holder.time.setVisibility(View.VISIBLE);
                    } else {
                        Message pre3 = (Message)this.getItem(position - 1);
                        if(data.getSentTime() - pre3.getSentTime() > 60000L) {
                            holder.time.setVisibility(View.VISIBLE);
                        } else {
                            holder.time.setVisibility(View.GONE);
                        }
                    }
                }

            }
        }

        holder.readReceipt.setVisibility(View.GONE);

    }

    private final void setGravity(View view, int gravity) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = gravity;
    }

    private boolean getNeedEvaluate(UIMessage data) {
        String extra = "";
        String robotEva = "";
        String sid = "";
        if(data != null && data.getConversationType() != null && data.getConversationType().equals(Conversation.ConversationType.CUSTOMER_SERVICE)) {
            if(data.getContent() instanceof TextMessage) {
                extra = ((TextMessage)data.getContent()).getExtra();
                if(TextUtils.isEmpty(extra)) {
                    return false;
                }

                try {
                    JSONObject e = new JSONObject(extra);
                    robotEva = e.optString("robotEva");
                    sid = e.optString("sid");
                } catch (JSONException var6) {
                    ;
                }
            }

            if(data.getMessageDirection() == Message.MessageDirection.RECEIVE && data.getContent() instanceof TextMessage && this.evaForRobot && this.robotMode && !TextUtils.isEmpty(robotEva) && !TextUtils.isEmpty(sid) && !data.getIsHistoryMessage()) {
                return true;
            }
        }

        return false;
    }

    public void setRightImageUrl(String rightImageUrl) {
        this.mRightImageUrl = rightImageUrl;
        Log.e("chengnan","setRightImageUrl======="+rightImageUrl);
        notifyDataSetChanged();
    }

    public void setLeftImageUrl(String leftImageUrl) {
        this.mLeftImageUrl = leftImageUrl;
        Log.e("chengnan","setLeftImageUrl======="+leftImageUrl);
        notifyDataSetChanged();
    }
}
