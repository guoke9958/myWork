package com.cn.xa.qyw.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.ui.conversation.ConversationActivity;
import com.cn.xa.qyw.utils.Lg;

import io.rong.imkit.RongIM;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.utils.AndroidEmoji;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * 红包消息处理器
 * Created by Administrator on 2016/9/18.
 */
@ProviderTag(messageContent = HongBaoMessage.class)
public class HongBaoMessageProvider extends IContainerItemProvider.MessageProvider<HongBaoMessage> {

    @Override
    public void bindView(View view, int i, HongBaoMessage data, UIMessage uiMessage) {
        HongBaoMessageProvider.ViewHolder holder = (ViewHolder) view.getTag();
        String content = data.getContent();

        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {
            holder.messageParent.setBackgroundResource(R.drawable.rc_ic_red_right);
        } else {
            holder.messageParent.setBackgroundResource(R.drawable.rc_ic_red_left);
        }

        if (content.contains("]")) {

            String[] arr = content.split("]");
            holder.message.setText(arr[1]);
        } else {
            holder.message.setText(content);
        }


    }

    @Override
    public Spannable getContentSummary(HongBaoMessage data) {
        if (data == null) {
            return null;
        } else {
            String content = data.getContent();
            if (content != null) {
                if (content.length() > 100) {
                    content = content.substring(0, 15);
                }

                return new SpannableString(AndroidEmoji.ensure(content));
            } else {
                return null;
            }
        }
    }

    @Override
    public void onItemClick(View view, int i, HongBaoMessage hongBaoMessage, UIMessage uiMessage) {
        Lg.e("content==="+hongBaoMessage.getContent());
        Lg.e("count==="+hongBaoMessage.getCount());
        Lg.e("orderId===" + hongBaoMessage.getOrderId());
        Lg.e("toUserId===" + hongBaoMessage.getToUserId());
        String content = hongBaoMessage.getContent();
        String message = "";
        if (content.contains("]")) {

            String[] arr = content.split("]");
            message = arr[1];
        } else {
            message = content;
        }

        ConversationActivity context = (ConversationActivity) view.getContext();
        String sendUserId =  context.getUserId();
        String tagName = context.getTagUserName();
        Lg.e("sendUserId===" + sendUserId);

        if(DoctorApplication.mUser!=null){
            String userId = DoctorApplication.mUser.getUserId();
            if(userId.equals(hongBaoMessage.getToUserId())){
                Lg.e("接收者打开红包===============");
                Intent intent = new Intent(context,OpenHongBaoActivity.class);
                intent.putExtra("orderId",hongBaoMessage.getOrderId());
                intent.putExtra("userName",tagName);
                intent.putExtra("content",message);
                intent.putExtra("count",hongBaoMessage.getCount());
                intent.putExtra("sendUserId",sendUserId);
                context.startActivity(intent);
            }else{
                Intent intent = new Intent(context,HongBaoDetailActivity.class);
                intent.putExtra("orderId",hongBaoMessage.getOrderId());
                context.startActivity(intent);

            }
        }






    }

    @Override
    public void onItemLongClick(View view, int i, HongBaoMessage hongBaoMessage, UIMessage uiMessage) {

    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        HongBaoMessageProvider.ViewHolder holder = new HongBaoMessageProvider.ViewHolder();
        View view = LayoutInflater.from(context).inflate(R.layout.hongbao_item_view, null);
        holder.message = (TextView) view.findViewById(R.id.content);
        holder.messageParent = view.findViewById(R.id.message_parent);
        view.setTag(holder);
        return view;
    }


    class ViewHolder {
        TextView message;
        View messageParent;
    }
}
