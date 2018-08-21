package com.cn.xa.qyw.ui.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.entiy.CapitalHistory;
import com.cn.xa.qyw.entiy.Order;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.ui.main.DoctorMainActivity;
import com.cn.xa.qyw.utils.Lg;
import com.cn.xa.qyw.utils.StringUtils;
import com.cn.xa.qyw.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by Administrator on 2016/9/22.
 */
public class OpenHongBaoActivity extends DoctorBaseActivity {

    private SimpleDraweeView mPayUerPhoto;
    private TextView mSendUserName;
    private TextView mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setVisibility(View.GONE);
        initView();
        insertLocalData();
    }

    @Override
    public void initBaseView() {
        super.initBaseView();
        mBaseMain.setBackgroundResource(R.color.black_100);
    }

    private void initView() {

        mPayUerPhoto = (SimpleDraweeView) findViewById(R.id.pay_user_photo);
        mSendUserName = (TextView) findViewById(R.id.send_hongbao_user_name);
        TextView mLiuYan = (TextView) findViewById(R.id.send_hongbao_user_liuyan);
        mLiuYan.setText(getIntent().getStringExtra("content"));
        mCount = (TextView) findViewById(R.id.hongbao_count);
        mCount.setText(getIntent().getIntExtra("count", 0) + "");

        TextView toolbarTitle = (TextView) findViewById(R.id.hongbao_toolbar_title);
        toolbarTitle.setText("打赏详情");

        ImageView mBtnBack = (ImageView) findViewById(R.id.hongbao_img_btn_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void insertLocalData() {
        String orderId = getIntent().getStringExtra("orderId");
        showDialog();
        HttpUtils.postDataFromServer(HttpAddress.GET_ORDER, orderId, new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {
                ToastUtils.showShortSnackbar(mToolbar, "打开赏金失败");
            }

            @Override
            public void onSuccess(String data) {
                if (StringUtils.isEmpty(data)) {
                    sendToMessage();
                }
            }
        });


        HttpUtils.postDataFromServer(HttpAddress.GET_HONGBAO_DETAIL, orderId, new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {
                dismissDialog();
                showToast("获取赏金详情失败");
            }

            @Override
            public void onSuccess(String data) {
                dismissDialog();
                CapitalHistory history = JSONObject.parseObject(data, CapitalHistory.class);
                initViewData(history);
            }
        });


    }

    private void initViewData(CapitalHistory history) {
        mSendUserName.setText(history.getUserName() + " 的打赏");
        mPayUerPhoto.setImageURI(HttpAddress.PHOTO_URL + history.getUserPhoto());
    }

    private void sendToMessage() {
        String userName = getIntent().getStringExtra("userName");
        String myName = StringUtils.getUserName(DoctorMainActivity.mUserInfo);
        String mTagId = getIntent().getStringExtra("sendUserId");

        CustomMessage message = CustomMessage.obtain("您领取了" + userName + "的打赏", myName + "领取了您的打赏", mTagId);


        Message myMessage = Message.obtain(mTagId, Conversation.ConversationType.PRIVATE, message);

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
    public int getChildLayoutId() {
        return R.layout.activity_open_hongbao;
    }
}
