package com.cn.xa.qyw.meesage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources.NotFoundException;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.rong.common.RLog;
import io.rong.imkit.R.bool;
import io.rong.imkit.R.drawable;
import io.rong.imkit.R.id;
import io.rong.imkit.R.integer;
import io.rong.imkit.R.layout;
import io.rong.imkit.R.string;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.ArraysDialogFragment;
import io.rong.imkit.widget.ArraysDialogFragment.OnArraysDialogItemListener;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imkit.widget.provider.IContainerItemProvider.MessageProvider;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Conversation.PublicServiceType;
import io.rong.imlib.model.Message.MessageDirection;
import io.rong.imlib.model.Message.SentStatus;
import io.rong.imlib.model.PublicServiceProfile;
import io.rong.imlib.model.UserInfo;


/**
 * Created by Administrator on 2017/9/3.
 */


@ProviderTag(
        messageContent = TextImageMessage.class
)

public class TextImageMessageProvider extends MessageProvider<TextImageMessage> {

    private static final String TAG = "TextImageMessageProvider";

    public TextImageMessageProvider() {
    }

    @Override
    public void bindView(View v, int i, TextImageMessage content, UIMessage message) {
        TextImageMessageProvider.ViewHolder holder = (TextImageMessageProvider.ViewHolder)v.getTag();
        holder.title.setText(content.getTitle());
        holder.content.setText(content.getContent());
        if(content.getImgUrl() != null) {
            holder.img.setResource(content.getImgUrl(), 0);
        }

        if(message.getMessageDirection() == MessageDirection.SEND) {
            holder.mLayout.setBackgroundResource(drawable.rc_ic_bubble_right_file);
        } else {
            holder.mLayout.setBackgroundResource(drawable.rc_ic_bubble_left_file);
        }
    }

    @Override
    public Spannable getContentSummary(TextImageMessage textImageMessage) {
        return new SpannableString(RongContext.getInstance().getResources().getString(string.rc_message_content_rich_text));
    }

    @Override
    public void onItemClick(View view, int i, TextImageMessage textImageMessage, UIMessage uiMessage) {

    }

    @Override
    public void onItemLongClick(View view, int i, TextImageMessage textImageMessage, final UIMessage message) {
        String name = null;
        if(!message.getConversationType().getName().equals(ConversationType.APP_PUBLIC_SERVICE.getName()) && !message.getConversationType().getName().equals(ConversationType.PUBLIC_SERVICE.getName())) {
            UserInfo items1 = message.getUserInfo();
            if(items1 == null) {
                items1 = RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId());
            }

            if(items1 != null) {
                name = items1.getName();
            }
        } else if(message.getUserInfo() != null) {
            name = message.getUserInfo().getName();
        } else {
            PublicServiceType items = PublicServiceType.setValue(message.getConversationType().getValue());
            PublicServiceProfile deltaTime = RongUserInfoManager.getInstance().getPublicServiceProfile(items, message.getTargetId());
            if(deltaTime != null) {
                name = deltaTime.getName();
            }
        }

        long deltaTime1 = RongIM.getInstance().getDeltaTime();
        long normalTime = System.currentTimeMillis() - deltaTime1;
        boolean enableMessageRecall = false;
        int messageRecallInterval = -1;
        boolean hasSent = !message.getSentStatus().equals(SentStatus.SENDING) && !message.getSentStatus().equals(SentStatus.FAILED);

        try {
            enableMessageRecall = RongContext.getInstance().getResources().getBoolean(bool.rc_enable_message_recall);
            messageRecallInterval = RongContext.getInstance().getResources().getInteger(integer.rc_message_recall_interval);
        } catch (NotFoundException var15) {
            RLog.e("TextImageMessageProvider", "rc_message_recall_interval not configure in rc_config.xml");
            var15.printStackTrace();
        }

        String[] items2;
        if(hasSent && enableMessageRecall && normalTime - message.getSentTime() <= (long)(messageRecallInterval * 1000) && message.getSenderUserId().equals(RongIM.getInstance().getCurrentUserId())) {
            items2 = new String[]{view.getContext().getResources().getString(string.rc_dialog_item_message_delete), view.getContext().getResources().getString(string.rc_dialog_item_message_recall)};
        } else {
            items2 = new String[]{view.getContext().getResources().getString(string.rc_dialog_item_message_delete)};
        }

        ArraysDialogFragment.newInstance(name, items2).setArraysDialogItemListener(new OnArraysDialogItemListener() {
            public void OnArraysDialogItemClick(DialogInterface dialog, int which) {
                if(which == 0) {
                    RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, (ResultCallback)null);
                } else if(which == 1) {
                    RongIM.getInstance().recallMessage(message);
                }

            }
        }).show(((FragmentActivity)view.getContext()).getSupportFragmentManager());
    }

    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(layout.rc_item_rich_content_message, (ViewGroup)null);
        TextImageMessageProvider.ViewHolder holder = new TextImageMessageProvider.ViewHolder();
        holder.title = (TextView)view.findViewById(id.rc_title);
        holder.content = (TextView)view.findViewById(id.rc_content);
        holder.img = (AsyncImageView)view.findViewById(id.rc_img);
        holder.mLayout = (RelativeLayout)view.findViewById(id.rc_layout);
        view.setTag(holder);
        return view;
    }

    class ViewHolder {
        AsyncImageView img;
        TextView title;
        TextView content;
        RelativeLayout mLayout;

        ViewHolder() {
        }
    }
}
