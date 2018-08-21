//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imkit.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import io.rong.common.RLog;
import io.rong.eventbus.EventBus;
import io.rong.imkit.R;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.R.id;
import io.rong.imkit.R.layout;
import io.rong.imkit.fragment.DispatchResultFragment;
import io.rong.imkit.fragment.UriFragment;
import io.rong.imkit.model.ConversationKey;
import io.rong.imkit.model.Event.InputViewEvent;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.InputView;
import io.rong.imkit.widget.InputView.IInputBoardListener;
import io.rong.imkit.widget.InputView.OnInfoButtonClick;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.InputProvider.ExtendProvider;
import io.rong.imkit.widget.provider.VoiceInputProvider;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.CustomServiceMode;
import io.rong.imlib.model.PublicServiceMenu;
import io.rong.imlib.model.PublicServiceProfile;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Conversation.PublicServiceType;

import java.util.Iterator;

public class CustomMessageInputFragment extends MessageInputFragment implements OnClickListener {

    private View mViewPager;
    private LinearLayout mPluginsLayout;
    private FrameLayout mExtendLayout;

    public CustomMessageInputFragment() {
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mPluginsLayout = (LinearLayout)view.findViewById(id.rc_plugins);
        this.mExtendLayout = (FrameLayout)view.findViewById(id.rc_ext);

        if (mViewPager == null) {
            Log.e("chengnan", "为空======");
        } else {
            Log.e("chengnan", "不为空======");
        }

        if (this.getUri() != null) {
            String path = this.getUri().getPath().toLowerCase();

            if (path != null && !"".equals(path)) {
                if (path.contains("system")) {
                    mInput.setVisibility(View.GONE);
                } else {
                    mInput.setVisibility(View.VISIBLE);
                }
            }

        }

    }

    @Override
    public boolean onBackPressed() {

        if(this.mPluginsLayout.getVisibility() != View.GONE && this.mExtendLayout.getVisibility() != View.VISIBLE) {
            if(this.mPluginsLayout.getVisibility() == View.VISIBLE) {
                mInput.onProviderInactive(getActivity());
                RongContext.getInstance().getEventBus().post(InputView.Event.CLICK);
                return true;
            }
        }

        return false;


    }
}