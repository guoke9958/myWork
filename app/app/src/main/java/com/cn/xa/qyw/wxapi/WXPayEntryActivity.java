package com.cn.xa.qyw.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cn.xa.qyw.alipay.GlobalConst;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, GlobalConst.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp instanceof PayResp) {
            Log.i(TAG, "onPayFinish, errCode = " + ((PayResp) resp).errCode);
            Log.i(TAG, "onPayFinish, errStr = " + ((PayResp) resp).errStr);
            Log.i(TAG, "onPayFinish, extData = " + ((PayResp) resp).extData);
            Log.i(TAG, "onPayFinish, prepayId = " + ((PayResp) resp).prepayId);
            Log.i(TAG, "onPayFinish, openId = " + ((PayResp) resp).openId);
            Log.i(TAG, "onPayFinish, returnKey = " + ((PayResp) resp).returnKey);
            Log.i(TAG, "onPayFinish, transaction = " + ((PayResp) resp).transaction);
        }

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Intent intent = new Intent(GlobalConst.BROADCAST_ACTION_WXPAY_RESULT);
            intent.putExtra(GlobalConst.EXTRA_KEY_WXPAY_PREPAYID_STRING, ((PayResp) resp).prepayId);
            if (resp.errCode == 0) {
                intent.putExtra(GlobalConst.EXTRA_KEY_WXPAY_RESULT_BOOLEAN, true);
            } else {
                intent.putExtra(GlobalConst.EXTRA_KEY_WXPAY_RESULT_BOOLEAN, false);
            }
            sendBroadcast(intent);
        }
        finish();
    }
}