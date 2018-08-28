package com.cn.xa.qyw.ui.slide;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.cn.xa.qyw.DoctorApplication;
import com.cn.xa.qyw.R;
import com.cn.xa.qyw.adapter.MaterialSimpleListAdapter;
import com.cn.xa.qyw.adapter.SelectPriceAdapter;
import com.cn.xa.qyw.alipay.GlobalConst;
import com.cn.xa.qyw.alipay.Payment;
import com.cn.xa.qyw.entiy.MaterialSimpleListItem;
import com.cn.xa.qyw.entiy.SelectPriceItem;
import com.cn.xa.qyw.entiy.TradeNo;
import com.cn.xa.qyw.http.HttpAddress;
import com.cn.xa.qyw.http.HttpUtils;
import com.cn.xa.qyw.http.NetworkResponseHandler;
import com.cn.xa.qyw.utils.DateUtils;
import com.cn.xa.qyw.utils.StringUtils;
import com.cn.xa.qyw.utils.ToastUtils;

import java.util.UUID;

/**
 * Created by Administrator on 2016/8/21.
 */
public class SelectPriceActivity extends SlideBaseActivity {

    private ListView mListView;
    private SelectPriceAdapter mAdapter;
    private View footView;
    private ImageView mGift;
    private TextView mTvShow;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbarTitle.setText("资金充值");
        initView();
        initListener();
        initData();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.list_view);
        footView = getLayoutInflater().inflate(R.layout.layout_vip_pay_footer, null);
        View headView = getLayoutInflater().inflate(R.layout.layout_dq_banner, null);
        mListView.addFooterView(footView);
//        mListView.addHeaderView(headView);
        mGift = (ImageView) headView.findViewById(R.id.prize);
        mTvShow = (TextView) headView.findViewById(R.id.prize_tips);
        mProgress = (ProgressBar) headView.findViewById(R.id.pro);


        IntentFilter filter = new IntentFilter();
        filter.addAction(GlobalConst.BROADCAST_ACTION_WXPAY_RESULT);
        registerReceiver(myReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    private void initListener() {
        mAdapter = new SelectPriceAdapter(this);
        footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOtherDialog();
            }
        });

        mGift.setOnClickListener(this);
    }

    private void showOtherDialog() {
        new MaterialDialog.Builder(this)
                .title("充值金额")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .iconRes(R.mipmap.account_msg_yd)
                .positiveText("确定")
                .negativeText("取消")
                .input("输入金额", "10", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        String number = String.valueOf(input);

                        if (!StringUtils.isEmpty(number) && !"0".equals(number)) {

                            int dianquan = Integer.parseInt(number);
                            startPay(dianquan);
                        } else {
                            ToastUtils.showShortSnackbar(mToolbar, "请输入金额");
                        }

                    }
                }).show();
    }

    private void startPay(final int number) {

        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(new MaterialSimpleListAdapter.Callback() {
            @Override
            public void onMaterialListItemSelected(MaterialDialog dialog, int index) {
                dialog.dismiss();
                pay(number, index);
            }
        });
        adapter.add(new MaterialSimpleListItem.Builder(this)
                .content("微信付款")
                .icon(R.mipmap.pay_ico_weixin)
                .backgroundColor(Color.WHITE)
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(this)
                .content("支付宝付款")
                .icon(R.mipmap.pay_ico_alipay)
                .backgroundColor(Color.WHITE)
                .build());

        new MaterialDialog.Builder(this)
                .title("选择支付方式")
                .adapter(adapter, null)
                .negativeText("取消")
                .show();


    }

    private void pay(int number, final int type) {
        //付款订单号 随机生成 不重复 小于32位
        showDialog();
        String uuid = UUID.randomUUID().toString().replace("-", "");

        //订单金额
        int price = number;

        //交易结束时间

        //商品描述
        String body = "求医网资金充值";

        //商品详情
        String detail = "求医网资金充值" + number + "元";

        TradeNo tradeNo = new TradeNo();
        tradeNo.setTradeNo(uuid);
        tradeNo.setPayType(1);
        tradeNo.setCreateTime(DateUtils.getCurrentTimestamp());
        tradeNo.setId(0);

        if(type==0){
            detail = "求医网资金微信充值" + number + "元";
            tradeNo.setType("wx");
        }else if(type ==1){
            detail = "求医网资金支付宝充值" + number + "元";
            tradeNo.setType("ali");
        }else{
            showToast("支付失败");
            return ;
        }

        tradeNo.setBody(body);
        tradeNo.setSubject(detail);
        tradeNo.setTotalAmount(price);
        tradeNo.setUserId(DoctorApplication.mUser.getUserId());

        String url = HttpAddress.CREATE_TRADE_NO;

        HttpUtils.postDataFromServer(url, JSONObject.toJSONString(tradeNo), new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {
                dismissDialog();
                showToast("创建订单失败");
            }

            @Override
            public void onSuccess(String data) {
                TradeNo trade = JSONObject.parseObject(data, TradeNo.class);

                if (type == 0) {
                    payWx(trade);
                } else if (type == 1) {
                    payAli(trade);
                }

            }
        });


    }

    private void initData() {
        mListView.setAdapter(mAdapter);

        refresh();

    }

    private void refresh() {
        HttpUtils.postDataFromServer(HttpAddress.GET_RECHARGE_GIFT, DoctorApplication.mUser.getUserId(), new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {

            }

            @Override
            public void onSuccess(String data) {
//                RechargeGift gift = JSONObject.parseObject(data, RechargeGift.class);
//                mProgress.setProgress(gift.getCoupon());
//                if (gift.getCoupon() >= 500) {
//                    mTvShow.setText("您有200点券需要领取，请点击礼包领取！");
//                    mGift.setClickable(true);
//                    mGift.setImageResource(R.mipmap.user_task_sign);
//                } else {
//                    mTvShow.setText("再购买" + (500 - gift.getCoupon()) + "点券，可额外获得20点券");
//                    mGift.setClickable(false);
//                    mGift.setImageResource(R.mipmap.pay_prize_icon_normal);
//                }

            }
        });
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.activity_select_price;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v.getId() == R.id.prize) {
            showGiftDialog();
        } else {
            int position = (int) v.getTag();
            SelectPriceItem item = mAdapter.getItem(position);
            startPay(Integer.parseInt(item.getPrice()));

        }
    }

    private void showGiftDialog() {
        HttpUtils.postDataFromServer(HttpAddress.EXPAND_GIFT, DoctorApplication.mUser.getUserId(), new NetworkResponseHandler() {
            @Override
            public void onFail(String error) {

            }

            @Override
            public void onSuccess(String data) {
                refresh();

                final Dialog dialog = new Dialog(SelectPriceActivity.this, R.style.Transparent);
                dialog.setContentView(R.layout.dialog_fragment_prizes);
                dialog.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
                dialog.show();

                dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.findViewById(R.id.get_prizes_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });


    }

    protected void payAli(TradeNo trade) {
        long expireTime = System.currentTimeMillis() + 30 * 60 * 1000;
        dismissDialog();
        new Payment().alipay(this, trade.getBody(), trade.getSubject(), trade.getTotalAmount(), trade.getTradeNo(), expireTime, new Payment.IPayNotifyCallbk() {

            @Override
            public void onEvent(int type, Object param) {
                switch (type) {
                    case Payment.IPayNotifyCallbk.CALLBACK_TYPE_ALIPAY_SUCCESS:
                        finish();
                        Toast.makeText(SelectPriceActivity.this, "支付宝支付成功", Toast.LENGTH_SHORT).show();
                        break;
                    case Payment.IPayNotifyCallbk.CALLBACK_TYPE_ALIPAY_CONFIRMING:
                        Toast.makeText(SelectPriceActivity.this, "支付宝支付结果确认中", Toast.LENGTH_SHORT).show();
                        break;
                    case Payment.IPayNotifyCallbk.CALLBACK_TYPE_ALIPAY_FAIL:
                        Toast.makeText(SelectPriceActivity.this, "支付宝支付失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

    protected void payWx(TradeNo trade) {
        long expireTime = System.currentTimeMillis() + 30 * 60 * 1000;

        new Payment().wxpay(this, trade.getBody(), trade.getSubject(), trade.getTotalAmount(), trade.getTradeNo(), expireTime, new Payment.IPayNotifyCallbk() {

            @Override
            public void onEvent(int type, Object param) {
                switch (type) {
                    case Payment.IPayNotifyCallbk.CALLBACK_TYPE_WXPAY_NOT_INSTALLED:
                        dismissDialog();
                        Toast.makeText(SelectPriceActivity.this, "手机暂未安装微信不支持", Toast.LENGTH_SHORT).show();
                        break;
                    case Payment.IPayNotifyCallbk.CALLBACK_TYPE_WXPAY_NOT_SUPPORTED:
                        dismissDialog();
                        Toast.makeText(SelectPriceActivity.this, "微信版本过低暂不支持", Toast.LENGTH_SHORT).show();
                        break;
                    case Payment.IPayNotifyCallbk.CALLBACK_TYPE_WXPAY_GET_PREPAY_ID_FAIL:
                        dismissDialog();
                        Toast.makeText(SelectPriceActivity.this, "微信支付失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }


    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (GlobalConst.BROADCAST_ACTION_WXPAY_RESULT.equals(action)) {
                boolean payResult = intent.getBooleanExtra(GlobalConst.EXTRA_KEY_WXPAY_RESULT_BOOLEAN, false);
                if (payResult) {
                    dismissDialog();
                    showToast("微信支付成功");
                    finish();
                } else {
                    dismissDialog();
                    showToast("微信支付失败");
                }

            }
        }

    };
}
