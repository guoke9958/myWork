package com.cn.xa.qyw.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.alipay.sdk.app.PayTask;
import com.cn.xa.qyw.base.DoctorBaseActivity;
import com.cn.xa.qyw.utils.Lg;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.rong.imageloader.utils.L;


public class Payment {
	
	 public static interface IPayNotifyCallbk {

	        /**
	         * 支付宝支付成功(param = null )
	         */
	        int CALLBACK_TYPE_ALIPAY_SUCCESS = 1;
	        /**
	         * 支付宝支付确认中(param = null )
	         */
	        int CALLBACK_TYPE_ALIPAY_CONFIRMING = 2;
	        /**
	         * 支付宝支付失败(param = null )
	         */
	        int CALLBACK_TYPE_ALIPAY_FAIL = 3;

	        /**
	         * 获取预订单id成功(param = PayReq )
	         */
	        int CALLBACK_TYPE_WXPAY_GET_PREPAY_ID_SUCCESS = 4;

	        /**
	         * 未安装微信(param = null )
	         */
	        int CALLBACK_TYPE_WXPAY_NOT_INSTALLED = 5;
	        /**
	         * 您安装的微信不支持支付功能(param = null )
	         */
	        int CALLBACK_TYPE_WXPAY_NOT_SUPPORTED = 6;
	        /**
	         * 获取预订单id失败(param = Exception )
	         */
	        int CALLBACK_TYPE_WXPAY_GET_PREPAY_ID_FAIL = 7;

	        void onEvent(int type, Object param);
	    }
	
	
	
	 public void alipay(final Activity activity, final String subject, final String body, final double price, final String outTradeNo, final long expireTime, final IPayNotifyCallbk payNotifyCallbk) {
	        new Thread() {
	            public void run() {
	                try {
	                    DecimalFormat df = new DecimalFormat("0.00");
	                    String priceStr = df.format(price);
	                    String orderInfo = getAliPayOrderInfo(subject, body, priceStr, outTradeNo, expireTime);
	                    // 对订单做RSA 签名
	                    String sign = SignUtils.sign(orderInfo, GlobalConst.ALIPAY_RSA_PRIVATE);
	                    try {
	                        // 仅需对sign 做URL编码
	                        sign = URLEncoder.encode(sign, "UTF-8");
	                    } catch (UnsupportedEncodingException e) {
	                        e.printStackTrace();
	                    }

	                    // 完整的符合支付宝参数规范的订单信息
	                    final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + "sign_type=\"RSA\"";
	                    PayTask alipay = new PayTask(activity);
	                    // 调用支付接口，获取支付结果
	                    String result = alipay.pay(payInfo);
	                    AliPayResult payResult = new AliPayResult(result);
	                    payResult.parseResult();
	                    
	                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
	                    if ("操作成功(9000)".equals(payResult.resultStatus)) {
	                        // Toast.makeText(PayDemoActivity.this, "支付成功",
	                        // Toast.LENGTH_SHORT).show();
	                        notifyUpLayer(payNotifyCallbk, IPayNotifyCallbk.CALLBACK_TYPE_ALIPAY_SUCCESS, null);
	                    } else {
	                        // 判断resultStatus 为非“9000”则代表可能支付失败
	                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
	                        if ("支付宝支付结果确认中(8000)".equals(payResult.resultStatus)) {
	                            // Toast.makeText(PayDemoActivity.this, "支付结果确认中",
	                            // Toast.LENGTH_SHORT).show();
	                            notifyUpLayer(payNotifyCallbk, IPayNotifyCallbk.CALLBACK_TYPE_ALIPAY_CONFIRMING, null);
	                        } else {
	                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
	                            // Toast.makeText(PayDemoActivity.this, "支付失败",
	                            // Toast.LENGTH_SHORT).show();
	                            notifyUpLayer(payNotifyCallbk, IPayNotifyCallbk.CALLBACK_TYPE_ALIPAY_FAIL, null);
	                        }
	                    }
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            };
	        }.start();
	    }

	    private void notifyUpLayer(final IPayNotifyCallbk payNotifyCallbk, final int type, final Object obj) {
	        new Handler(Looper.getMainLooper()).post(new Runnable() {
	            public void run() {
	                payNotifyCallbk.onEvent(type, obj);
	            }
	        });
	    }

	    /**
	     * create the order info. 创建订单信息
	     * 
	     */
	    public String getAliPayOrderInfo(String subject, String body, String price, String outTradeNo, long expireTime) {

	        // 签约合作者身份ID
	        String orderInfo = "partner=" + "\"" + GlobalConst.ALIPAY_PARTNER + "\"";

	        // 签约卖家支付宝账号
	        orderInfo += "&seller_id=" + "\"" + GlobalConst.ALIPAY_SELLER + "\"";

	        // 商户网站唯一订单号
	        orderInfo += "&out_trade_no=" + "\"" + outTradeNo + "\"";

	        // 商品名称
	        orderInfo += "&subject=" + "\"" + subject + "\"";

	        // 商品详情
	        orderInfo += "&body=" + "\"" + body + "\"";

	        // 商品金额
	        orderInfo += "&total_fee=" + "\"" + price + "\"";

	        // 服务器异步通知页面路径
	        orderInfo += "&notify_url=" + "\"" + "http://www.qiuyiwang.com:8081/YHT/api/pay/alipay_notify.do?" + "\"";

	        // 服务接口名称， 固定值
	        orderInfo += "&service=\"mobile.securitypay.pay\"";

	        // 支付类型， 固定值
	        orderInfo += "&payment_type=\"1\"";

	        // 参数编码， 固定值
	        orderInfo += "&_input_charset=\"utf-8\"";

	        // 设置未付款交易的超时时间
	        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
	        // 取值范围：1m～15d。
	        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
	        // 该参数数值不接受小数点，如1.5h，可转换为90m。
	        String time = null;
	        try {
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            Date date = new Date(expireTime);
	            time = sdf.format(date);
	        } catch (Exception e) {
	        }
	        if (time != null) {
	            orderInfo += "&it_b_pay=\"" + time + "\"";
	        }
	        // orderInfo += "&smart360_user_id=\"" +
	        // UserEntity.getInstance().getUserId() + "\"";
	        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
	        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

	        return orderInfo;
	    }

	    /***
	     * 生成随机字条串
	     * 
	     * @return
	     */
	    public static String getRandomString(int length) {
	        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
	        Random random = new Random();
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < length; i++) {
	            int number = random.nextInt(base.length());
	            sb.append(base.charAt(number));
	        }
	        return sb.toString();
	    }

	public void wxpay(final DoctorBaseActivity activity, final String subject, final String body, final double price, final String outTradeNo, final long expireTime, final IPayNotifyCallbk payNotifyCallbk) {
		new Thread() {
			public void run() {
				try {
					IWXAPI msgApi = WXAPIFactory.createWXAPI(activity, null);
					msgApi.registerApp(GlobalConst.WX_APP_ID);
					boolean isInstalled = msgApi.isWXAppInstalled();
					if (!isInstalled) {
						notifyUpLayer(payNotifyCallbk, IPayNotifyCallbk.CALLBACK_TYPE_WXPAY_NOT_INSTALLED, null);
						activity.dismissDialog();
						return;
					}
					if (msgApi.getWXAppSupportAPI() < Build.PAY_SUPPORTED_SDK_INT) {
						notifyUpLayer(payNotifyCallbk, IPayNotifyCallbk.CALLBACK_TYPE_WXPAY_NOT_SUPPORTED, null);
						activity.dismissDialog();
						return;
					}
					String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
					String productArgs = getWXPayProductArgs(subject, body, price, outTradeNo, expireTime);
					byte[] buf = Util.httpPost(url, productArgs);
					String content = new String(buf);

					Lg.e(content);

					Map<String, String> xml = decodeXml(content);
					String resultCode = xml.get("result_code");
					String perpayId = xml.get("prepay_id");

					if ("SUCCESS".equals(resultCode) && !TextUtils.isEmpty(perpayId)) {
						PayReq req = genPayReq(xml);
						notifyUpLayer(payNotifyCallbk, IPayNotifyCallbk.CALLBACK_TYPE_WXPAY_GET_PREPAY_ID_SUCCESS, req);
						msgApi.sendReq(req);
					} else {
						activity.dismissDialog();
						notifyUpLayer(payNotifyCallbk, IPayNotifyCallbk.CALLBACK_TYPE_WXPAY_GET_PREPAY_ID_FAIL, null);
					}
				} catch (Exception e) {
					e.printStackTrace();
					activity.dismissDialog();
					notifyUpLayer(payNotifyCallbk, IPayNotifyCallbk.CALLBACK_TYPE_WXPAY_GET_PREPAY_ID_FAIL, null);
				}

			};
		}.start();
	}

	private PayReq genPayReq(Map<String, String> result) {

		PayReq req = new PayReq();
		req.appId = GlobalConst.WX_APP_ID;
		req.partnerId = GlobalConst.WX_MCH_ID;
		req.prepayId = result.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(System.currentTimeMillis() / 1000);

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
		req.sign = genAppSign(signParams);
		return req;
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(GlobalConst.WX_API_KEY);
		String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		return appSign;
	}

	public Map<String, String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();
				switch (event) {
					case XmlPullParser.START_DOCUMENT:

						break;
					case XmlPullParser.START_TAG:

						if ("xml".equals(nodeName) == false) {
							// 实例化student对象
							xml.put(nodeName, parser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion", e.toString());
		}
		return null;

	}


	private String getWXPayProductArgs(final String subject, String detail, double price, final String outTradeNo, long expireTime) {
		price = price * 100;
		DecimalFormat df = new DecimalFormat("0");
		String priceStr = df.format(price);
		StringBuffer xml = new StringBuffer();

		String nonceStr = genNonceStr();

		xml.append("</xml>");
		List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
		String expire_time = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = new Date(expireTime);
			expire_time = sdf.format(date);
		} catch (Exception e) {
		}
		packageParams.add(new BasicNameValuePair("appid", GlobalConst.WX_APP_ID));
		packageParams.add(new BasicNameValuePair("body", subject));
		packageParams.add(new BasicNameValuePair("detail", detail));
		packageParams.add(new BasicNameValuePair("mch_id", GlobalConst.WX_MCH_ID));
		packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
		packageParams.add(new BasicNameValuePair("notify_url", "http://www.qiuyiwang.com:8081/YHT/api/pay/wx_notify"));
		packageParams.add(new BasicNameValuePair("out_trade_no", outTradeNo));
		packageParams.add(new BasicNameValuePair("spbill_create_ip", "115.230.124.88"));
		if (expire_time != null) {
			packageParams.add(new BasicNameValuePair("time_expire", expire_time));
		}
		packageParams.add(new BasicNameValuePair("total_fee", priceStr));
		packageParams.add(new BasicNameValuePair("trade_type", "APP"));
		String sign = genPackageSign(packageParams);
		packageParams.add(new BasicNameValuePair("sign", sign));
		String xmlstring = toXml(packageParams);
		try {
			return new String(xmlstring.toString().getBytes(), "ISO8859-1");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");

		return sb.toString();
	}

	/**
	 * 生成签名
	 */

	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(GlobalConst.WX_API_KEY);

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		return packageSign;
	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}


}
