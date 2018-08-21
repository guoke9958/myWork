package com.cn.xa.qyw.ui.slide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.adapter.DiscoverAdapter;
import com.cn.xa.qyw.adapter.GodHistoryAdapter;
import com.cn.xa.qyw.adapter.MyGodAdapter;
import com.cn.xa.qyw.entiy.Capital;
import com.cn.xa.qyw.entiy.CapitalHistory;
import com.cn.xa.qyw.entiy.MyGodItem;
import com.cn.xa.qyw.entiy.UserCapital;
import com.cn.xa.qyw.entiy.UserPayPwd;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.ui.paypwd.DialogWidget;
import com.cn.xa.qyw.ui.paypwd.PayPasswordView;
import com.cn.xa.qyw.utils.DateUtils;
import com.cn.xa.qyw.utils.SHA1;
import com.cn.xa.qyw.utils.ToastUtils;

/**
 * Created by Administrator on 2016/7/26.
 */
public class MyGodActivity extends SlideBaseActivity {

    private View mChongZhi;
    private View mTiXian;
    private MyGodAdapter mAdapter;
    private ListView mListView;
    private DialogWidget mDialogWidget;
    private UserCapital mCapital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();
        mToolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserPayPwd();
            }
        });
    }

    private void getUserPayPwd() {
        showDialog();
        HttpUtils.postDataFromServer(HttpAddress.GET_USER_PAY_PWD, DoctorApplication.mUser.getUserId(), new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                dismissDialog();
                if ("不存在".equals(message)) {
                    mDialogWidget = new DialogWidget(MyGodActivity.this, getDecorViewDialog());
                    mDialogWidget.show();
                } else {
                    showToast("服务器连接异常");
                }
            }

            @Override
            public void onSuccess(String data) {
                dismissDialog();
                startActivity(getNewIntent(SelectPriceActivity.class));
            }
        });

    }

    protected View getDecorViewDialog() {

        // TODO Auto-generated method stub
        return PayPasswordView.getInstance("", "请先设置支付密码", true, "*支付密码用于打赏，提现等资金安全性操作，请您牢记", this, new PayPasswordView.OnPayListener() {

            @Override
            public void onSurePay(String password) {
                mDialogWidget.dismiss();
                mDialogWidget=null;
                addPayPwd(password);
            }

            @Override
            public void onCancelPay() {
                mDialogWidget.dismiss();
                mDialogWidget=null;
            }
        }).getView();
    }

    private void addPayPwd(String pwd) {
        showDialog();

        UserPayPwd payPwd = new UserPayPwd();
        payPwd.setUserId(DoctorApplication.mUser.getUserId());
        payPwd.setPayPwd(SHA1.encryptToSHA(pwd));
        payPwd.setUpdateTime(DateUtils.getCurrentTimestamp());

        HttpUtils.postDataFromServer(HttpAddress.ADD_AND_UPDATE_USER_PAY_PWD,JSONObject.toJSONString(payPwd) , new NetworkResponseHandler() {
            @Override
            public void onFail(String message) {
                dismissDialog();
                showToast("添加支付密码失败");
            }

            @Override
            public void onSuccess(String data) {
                dismissDialog();
                showLongToast("添加支付密码成功");
                startActivity(getNewIntent(SelectPriceActivity.class));
            }
        });
    }

    private void initView() {
        mChongZhi = findViewById(R.id.chongzhi);
        mTiXian = findViewById(R.id.tixian);
        mListView = (ListView) findViewById(R.id.my_account_listview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CapitalHistory history = new CapitalHistory();
        history.setUpdateTime(DateUtils.getCurrentTimestamp());
        history.setUserId(DoctorApplication.mUser.getUserId());

        HttpUtils.postDataFromServer(HttpAddress.GET_MY_CAPITAL, JSONObject.toJSONString(history), new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {

            }

            @Override
            public void onSuccess(String data) {

                mCapital = JSONObject.parseObject(data, UserCapital.class);
                mAdapter.setData(mCapital);
//                mRecharge.setText(""+capital.getRechargeCapital());
//                mExpand.setText("" + capital.getExpandCapital());
//                mYuCount.setText(""+capital.getCapitalTotal());
//                mAdapter.setData(capital.getListHistory());
            }
        });

    }

    private void initListener() {
        mChongZhi.setOnClickListener(this);
        mTiXian.setOnClickListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyGodItem item = mAdapter.getItem(position);
                Intent intent = getNewIntent(UserCapitalHistoryActivity.class);
                int type = item.getType();
                intent.putExtra("type", type);
                if (MyGodItem.GOD_TYPE_YU_E == type) {
                } else if (MyGodItem.GOD_TYPE_CHONG_ZHI == type) {
                    startActivity(intent);
                } else if (MyGodItem.GOD_TYPE_SHOU_RU == type) {
                    startActivity(intent);
                } else if (MyGodItem.GOD_TYPE_XIAO_FEI == type) {
                    startActivity(intent);
                }
            }
        });
    }

    private void initData() {
        mAdapter = new MyGodAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.chongzhi) {
            getUserPayPwd();
        } else if (id == R.id.tixian) {
            startActivity(getNewIntent(WithDrawAccountActivity.class));
        }
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.view_my_god_list_view_header;
    }
}
