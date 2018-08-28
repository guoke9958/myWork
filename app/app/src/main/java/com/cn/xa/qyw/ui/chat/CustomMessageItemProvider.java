package com.cn.xa.qyw.ui.chat;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.utils.StringUtils;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.utils.AndroidEmoji;
import io.rong.imkit.widget.provider.IContainerItemProvider;

/**
 * Created by Administrator on 2016/9/22.
 */
@ProviderTag(messageContent = CustomMessage.class, showPortrait = false, centerInHorizontal = true)
public class CustomMessageItemProvider extends IContainerItemProvider.MessageProvider<CustomMessage> {

    @Override
    public void bindView(View view, int i, CustomMessage customMessage, UIMessage uiMessage) {
        TextView tv = (TextView) view;

        if (DoctorApplication.mUser.getUserId().equals(customMessage.getToUserId())) {
            tv.setText(customMessage.getSendContent());
        } else {
            tv.setText(customMessage.getContent());
        }


    }

    @Override
    public Spannable getContentSummary(CustomMessage data) {
        if (data == null) {
            return null;
        } else {
            String content = "";
            if (DoctorApplication.mUser != null) {
                if (DoctorApplication.mUser.getUserId().equals(data.getToUserId())) {
                    content = data.getSendContent();
                } else {
                    content = data.getContent();
                }
            } else {
                content = data.getContent();
            }


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
    public void onItemClick(View view, int i, CustomMessage customMessage, UIMessage uiMessage) {

    }

    @Override
    public void onItemLongClick(View view, int i, CustomMessage customMessage, UIMessage uiMessage) {

    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        return new TextView(context);
    }
}
