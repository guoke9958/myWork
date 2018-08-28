package com.cn.xa.qyw.ui.conversation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.entiy.User;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.preference.PreferenceKeys;
import com.cn.xa.qyw.preference.PreferenceUtils;
import com.cn.xa.qyw.ui.chat.HongBaoMessage;
import com.cn.xa.qyw.ui.chat.SendHongBaoActivity;
import com.cn.xa.qyw.ui.main.DoctorMainActivity;
import com.cn.xa.qyw.utils.StringUtils;

import java.util.Locale;

import io.rong.imageloader.utils.L;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.CustomFragment;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

//CallKit start 1
//CallKit end 1

/**
 * Created by Bob on 15/11/3.
 * 会话页面
 * 1，设置 ActionBar title
 * 2，加载会话页面
 * 3，push 和 通知 判断
 */
public class ConversationActivity extends DoctorBaseActivity {

    private String mTargetId;

    public static String hongbao = "start_hongbao";

    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;
    private View mInput;
    private String mTitle;
    private MessageBroadcastReceiver receiveBroadCast;
    private com.cn.xa.qyw.entiy.UserInfo mUser;
    private CustomFragment mFragment;
    private String mToTargetId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        getIntentDate(intent);

        isReconnect(intent);
        initView();

        receiveBroadCast = new MessageBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(hongbao);    //只有持有相同的action的接受者才能接收此广播
        registerReceiver(receiveBroadCast, filter);

    }

    private void initView() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiveBroadCast);
    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {

        mTargetId = intent.getData().getQueryParameter("targetId");
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        mTitle = intent.getData().getQueryParameter("title");

        if (mTitle.contains(";")) {
            mToTargetId = mTitle.split(";")[0];
            mTitle = mTitle.split(";")[1];
        }

        //intent.getData().getLastPathSegment();//获得当前会话类型
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
        enterFragment(mConversationType, mTargetId);

        mToolbarTitle.setText(mTitle);
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_conversation;
    }

    /**
     * 重连
     *
     * @param token
     */
    private void reconnect(String token) {

        if (getApplicationInfo().packageName.equals(DoctorApplication.getCurProcessName(getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {

                }

                @Override
                public void onSuccess(String s) {

                    enterFragment(mConversationType, mTargetId);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }

    /**
     * 判断消息是否是 push 消息
     */
    private void isReconnect(Intent intent) {


        String token = null;
        token = PreferenceUtils.getPrefString(PreferenceKeys.RONG_YUN_TOKEN, "default");
        //push或通知过来
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {

            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("push") != null
                    && intent.getData().getQueryParameter("push").equals("true")) {

                reconnect(token);
            } else {
                //程序切到后台，收到消息后点击进入,会执行这里
                if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {

                    reconnect(token);
                } else {
                    enterFragment(mConversationType, mTargetId);
                }
            }
        }
    }

    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType
     * @param mTargetId
     */
    private void enterFragment(final Conversation.ConversationType mConversationType, final String mTargetId) {

        HttpUtils.postDataFromServer(HttpAddress.GET_USER_INFO, mToTargetId + ";" + mTargetId, new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {

            }

            @Override
            public void onSuccess(String data) {
                if (!StringUtils.isEmpty(data)) {
                    mUser = JSONObject.parseObject(data, com.cn.xa.qyw.entiy.UserInfo.class);
                    if (mUser != null) {
                        ConversationActivity.this.mTargetId = mUser.getUserId();

                        mFragment = (CustomFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);
                        Uri.Builder builder = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                                .appendQueryParameter("targetId", mTargetId);

                        Uri uri = builder.build();
                        mFragment.setUri(uri);
                        if (DoctorMainActivity.mUserInfo != null) {
                            mFragment.setRightImageUrl(HttpAddress.PHOTO_URL + DoctorMainActivity.mUserInfo.getPhoto());
                        }


                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(mUser.getUserId(),
                                StringUtils.getUserName(mUser), Uri.parse(HttpAddress.PHOTO_URL + mUser.getPhoto())));

                        mToolbarTitle.setText(StringUtils.getUserName(mUser));
                        mFragment.setLeftImageUrl(HttpAddress.PHOTO_URL + mUser.getPhoto());
                    }
                }

            }
        });

    }

    public String getUserId() {
        return mTargetId;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {

            if (requestCode == 13) {
                L.e("红包发送成功....");
                mFragment.getmInputFragment().onBackPressed();
                sendMesssage(data.getIntExtra("count", 20), data.getStringExtra("title"), data.getStringExtra("orderId"));
            }
        } else {
            L.e("红包发送失败...");
        }

    }

    public String getTagUserName() {
        if (mUser != null) {
            return StringUtils.getUserName(mUser);
        }
        return "";
    }

    class MessageBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();
            L.e("MessageBroadcastReceiver=========action==" + action);
            if ("start_hongbao".equals(action)) {
                Intent intent1 = new Intent(context, SendHongBaoActivity.class);
                intent1.putExtra("user_id", mTargetId);
                startActivityForResult(intent1, 13);
            }
        }
    }

    private void sendMesssage(int count, String title, String orderId) {
        HongBaoMessage message = HongBaoMessage.obtain("[医患通赏金] " + title, count, orderId, mTargetId);

        Message myMessage = Message.obtain(mTargetId, Conversation.ConversationType.PRIVATE, message);

        RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //消息本地数据库存储成功的回调
            }

            @Override
            public void onSuccess(Message message) {
                //消息通过网络发送成功的回调
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回调
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null && mFragment.getmInputFragment().onBackPressed()) {
            return;
        } else {
            super.onBackPressed();
        }
    }
}
