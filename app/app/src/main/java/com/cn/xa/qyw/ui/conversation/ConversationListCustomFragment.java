package com.cn.xa.qyw.ui.conversation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.xa.qyw.R;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;

/**
 * Created by Administrator on 2016/8/17.
 */
public class ConversationListCustomFragment extends ConversationListFragment {


    private TextView mEmptyView;

    @Override
    public void onResume() {
        super.onResume();

        RongIMClient.ConnectionStatusListener.ConnectionStatus status = RongIM.getInstance().getCurrentConnectionStatus();
        if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            mEmptyView.setText(RongContext.getInstance().getResources().getString(io.rong.imkit.R.string.rc_conversation_list_not_connected));
        } else {
            mEmptyView.setText(RongContext.getInstance().getResources().getString(io.rong.imkit.R.string.rc_conversation_list_empty_prompt));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       mEmptyView = (TextView)this.findViewById(view, android.R.id.empty);
    }
}
