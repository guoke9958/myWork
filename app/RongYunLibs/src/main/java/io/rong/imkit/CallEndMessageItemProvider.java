package io.rong.imkit;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;
import io.rong.calllib.message.CallSTerminateMessage;
import io.rong.imkit.RongContext;
import io.rong.imkit.model.LinkTextView;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

@ProviderTag(messageContent = CallSTerminateMessage.class, showSummaryWithName = false, showProgress = false, showWarning = false)
public class CallEndMessageItemProvider extends IContainerItemProvider.MessageProvider<CallSTerminateMessage> {
    class ViewHolder {
        LinkTextView message;
    }


    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_text_message, null);

        ViewHolder holder = new ViewHolder();
        holder.message = (LinkTextView) view.findViewById(android.R.id.text1);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View v, int position, CallSTerminateMessage content, UIMessage data) {
        ViewHolder holder = (ViewHolder) v.getTag();

        if (data == null || content == null) {
            return;
        }
        if (data.getMessageDirection() == Message.MessageDirection.SEND) {
            holder.message.setBackgroundResource(R.drawable.rc_ic_bubble_right);
        } else {
            holder.message.setBackgroundResource(R.drawable.rc_ic_bubble_left);
        }

        RongCallCommon.CallMediaType mediaType = content.getMediaType();
        String direction = content.getDirection();
        Drawable drawable = null;
        holder.message.setText(content.getContent());
        holder.message.setCompoundDrawablePadding(15);

        int size = 54;
        if (mediaType.equals(RongCallCommon.CallMediaType.VIDEO)) {
            if (direction != null && direction.equals("MO")) {
                drawable = v.getResources().getDrawable(R.drawable.rc_voip_video_right);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                holder.message.setCompoundDrawables(null, null, drawable, null);
            } else {
                drawable = RongContext.getInstance().getResources().getDrawable(R.drawable.rc_voip_video_left);
                drawable.setBounds(0, 0,  drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                holder.message.setCompoundDrawables(drawable, null, null, null);
            }
        } else {
            if (direction != null && direction.equals("MO")) {
                if (content.getReason().equals(RongCallCommon.CallDisconnectedReason.HANGUP) ||
                        content.getReason().equals(RongCallCommon.CallDisconnectedReason.REMOTE_HANGUP)) {
                    drawable = RongContext.getInstance().getResources().getDrawable(R.drawable.rc_voip_audio_right_connected);
                } else {
                    drawable = RongContext.getInstance().getResources().getDrawable(R.drawable.rc_voip_audio_right_cancel);
                }
                drawable.setBounds(0, 0,  drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                holder.message.setCompoundDrawables(null, null, drawable, null);
            } else {
                if (content.getReason().equals(RongCallCommon.CallDisconnectedReason.HANGUP) ||
                        content.getReason().equals(RongCallCommon.CallDisconnectedReason.REMOTE_HANGUP)) {
                    drawable = RongContext.getInstance().getResources().getDrawable(R.drawable.rc_voip_audio_left_connected);
                } else {
                    drawable = RongContext.getInstance().getResources().getDrawable(R.drawable.rc_voip_audio_left_cancel);
                }
                drawable.setBounds(0, 0,  drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                holder.message.setCompoundDrawables(drawable, null, null, null);
            }
        }
    }

    @Override
    public Spannable getContentSummary(CallSTerminateMessage data) {

        RongCallCommon.CallMediaType mediaType = data.getMediaType();
        if (mediaType.equals(RongCallCommon.CallMediaType.AUDIO)) {
            return new SpannableString(RongContext.getInstance().getString(R.string.rc_voip_message_audio));
        } else {
            return new SpannableString(RongContext.getInstance().getString(R.string.rc_voip_message_video));
        }
    }

    @Override
    public void onItemClick(View view, int position, CallSTerminateMessage content, UIMessage message) {
        RongCallSession profile = RongCallClient.getInstance().getCallSession();
        if (profile != null && profile.getActiveTime() > 0) {
            Toast.makeText(view.getContext(), view.getContext().getString(R.string.rc_voip_call_start_fail), Toast.LENGTH_SHORT).show();
            return;
        }
        RongCallCommon.CallMediaType mediaType = content.getMediaType();
        String action = null;
        if (mediaType.equals(RongCallCommon.CallMediaType.VIDEO)) {
            action = RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEVIDEO;
        } else {
            action = RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEAUDIO;
        }
        Intent intent = new Intent(action);
        intent.putExtra("conversationType", message.getConversationType().getName().toLowerCase());
        intent.putExtra("targetId", message.getTargetId());
        intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
        view.getContext().startActivity(intent);
    }

    @Override
    public void onItemLongClick(final View view, int position, final CallSTerminateMessage content, final UIMessage message) {
//        String name = null;
//
//        if (message.getSenderUserId() != null) {
//            UserInfo userInfo = message.getUserInfo();
//            if (userInfo == null || userInfo.getName() == null)
//                userInfo = RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId());
//
//            if (userInfo != null)
//                name = userInfo.getName();
//        }
//
//        String[] items;
//
//        items = new String[]{view.getContext().getResources().getString(R.string.rc_dialog_item_message_delete)};
//
//        ArraysDialogFragment.newInstance(name, items).setArraysDialogItemListener(new ArraysDialogFragment.OnArraysDialogItemListener() {
//            @Override
//            public void OnArraysDialogItemClick(DialogInterface dialog, int which) {
//                if (which == 0) {
//                    @SuppressWarnings("deprecation")
//                    ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
//                    clipboard.setText(((CallSTerminateMessage) content).getContent());
//                } else if (which == 1) {
//                    RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, null);
//                }
//
//            }
//        }).show(((FragmentActivity) view.getContext()).getSupportFragmentManager());
    }
}
