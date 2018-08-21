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
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.ui.main.DoctorMainActivity;
import com.cn.xa.qyw.utils.DateUtils;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2016/9/19.
 */
public class HongBaoDetailActivity extends DoctorBaseActivity {

    private TextView mSendUserName;
    private TextView mPayTOUserName;
    private TextView mSendTime;
    private TextView mHongBaoCount;
    private SimpleDraweeView mPayUerPhoto;
    private SimpleDraweeView mPayToUserPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setVisibility(View.GONE);
        initView();
        initListener();
        initData();
    }

    private void initListener() {

    }

    private void initView() {
        mSendUserName = (TextView) findViewById(R.id.send_hongbao_user_name);
        mPayTOUserName = (TextView) findViewById(R.id.pay_to_user_name);
        mSendTime = (TextView) findViewById(R.id.send_time);
        mHongBaoCount = (TextView) findViewById(R.id.hongbao_count);
        mPayUerPhoto = (SimpleDraweeView) findViewById(R.id.pay_user_photo);
        mPayToUserPhoto = (SimpleDraweeView) findViewById(R.id.pay_to_user_photo);

        TextView toolbarTitle = (TextView) findViewById(R.id.hongbao_toolbar_title);
        toolbarTitle.setText("赏金详情");

        ImageView mBtnBack = (ImageView) findViewById(R.id.hongbao_img_btn_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        showDialog();
        String orderId = getIntent().getStringExtra("orderId");
        HttpUtils.postDataFromServer(HttpAddress.GET_HONGBAO_DETAIL, orderId, new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {
                dismissDialog();
                showToast("获取红包详情失败");
            }

            @Override
            public void onSuccess(String data) {
                dismissDialog();
                CapitalHistory history = JSONObject.parseObject(data, CapitalHistory.class);
                initViewData(history);
            }
        });
    }

    @Override
    public void initBaseView() {
        super.initBaseView();
        mBaseMain.setBackgroundResource(R.color.black_100);
    }

    private void initViewData(CapitalHistory history) {
        mSendUserName.setText(history.getUserName() + " 的打赏");
        mPayTOUserName.setText(history.getToUserName());
        mHongBaoCount.setText(history.getChange() + " 元");
        if (DoctorApplication.mUser != null) {
            mPayUerPhoto.setImageURI(HttpAddress.PHOTO_URL + DoctorMainActivity.mUserInfo.getPhoto());
        }

        mPayToUserPhoto.setImageURI(HttpAddress.PHOTO_URL + history.getToUserPhoto());

        mSendTime.setText(DateUtils.getTimeData(history.getUpdateTime()));
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_hongbao_detail;
    }
}
