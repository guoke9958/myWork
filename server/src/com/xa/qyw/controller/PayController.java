package com.xa.qyw.controller;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xa.qyw.BizCode;
import com.xa.qyw.entiy.CapitalHistory;
import com.xa.qyw.entiy.RechargeGift;
import com.xa.qyw.entiy.TradeNo;
import com.xa.qyw.entiy.UserCapital;
import com.xa.qyw.service.PayService;
import com.xa.qyw.service.UserCapitalService;
import com.xa.qyw.utils.DateUtils;
import com.xa.qyw.utils.PayCommonUtil;
import com.xa.qyw.utils.ResponseUtils;
import com.xa.qyw.utils.XMLUtil;

@Controller
@RequestMapping("/api/pay/")
public class PayController {

	int count = 100;

	@Autowired
	PayService payService;

	@Autowired
	UserCapitalService mUserCapitalService;

	@ResponseBody
	@RequestMapping("alipay_notify")
	public String alipayNotify(HttpServletRequest request) {

		String body = request.getParameter("body");
		String time = request.getParameter("notify_time");
		String tradeNo = request.getParameter("out_trade_no");
		String subject = request.getParameter("subject");
		String totalAmount = request.getParameter("total_amount");
		String tradeStatus = request.getParameter("trade_status");

		TradeNo trade = new TradeNo();
		trade.setTradeNo(tradeNo);
		trade.setNotifyTime(DateUtils.getCurrentTimestamp());

		System.out.println("alipayNotify==tradeNo==" + tradeNo
				+ "\ntradeStatus===" + tradeStatus);

		try {

			if ("TRADE_FINISHED".equals(tradeStatus)
					|| "TRADE_SUCCESS".equals(tradeStatus)) {
				trade.setTradeStatus(1);

				TradeNo trad = payService.getTradeByNo(trade);

				UserCapital capital = new UserCapital();
				capital.setUserId(trad.getUserId());
				capital.setChangeCapital(trad.getTotalAmount());
				mUserCapitalService.updateCapitalRecharge(capital);

				CapitalHistory history = new CapitalHistory();
				history.setOrderId(UUID.randomUUID().toString()
						.replace("-", ""));
				history.setId(0);
				history.setChange(trad.getTotalAmount());
				history.setUpdateTime(DateUtils.getCurrentTimestamp());
				history.setCapitalType(1);
				history.setUserId(trad.getUserId());
				history.setPayType("ali");
				mUserCapitalService.insertUserCapitalHistory(history);

				RechargeGift gift = new RechargeGift();
				gift.setUserId(trad.getUserId());
				gift.setChange(trad.getTotalAmount());
				mUserCapitalService.updateRechargeGift(gift);

			} else {
				trade.setTradeStatus(3);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		payService.updateTradeNoStatus(trade);

		return "success";
	}

	@ResponseBody
	@RequestMapping("wx_notify")
	public String wxNotify(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BufferedReader reader = null;

		reader = request.getReader();
		String line = "";
		String xmlString = null;
		StringBuffer inputString = new StringBuffer();

		while ((line = reader.readLine()) != null) {
			inputString.append(line);
		}
		xmlString = inputString.toString();
		request.getReader().close();
		System.out.println("----接收到的数据如下：---" + xmlString);
		Map<String, String> map = new HashMap<String, String>();
		String result_code = "";
		String return_code = "";
		String out_trade_no = "";
		map = XMLUtil.doXMLParse(xmlString);
		result_code = map.get("result_code");
		out_trade_no = map.get("out_trade_no");
		return_code = map.get("return_code");

		TradeNo trade = new TradeNo();
		trade.setTradeNo(out_trade_no);
		trade.setNotifyTime(DateUtils.getCurrentTimestamp());
		
		if (checkSign(xmlString)) {
			
			if("SUCCESS".equals(result_code)){
				
				trade.setTradeStatus(1);

				TradeNo trad = payService.getTradeByNo(trade);

				UserCapital capital = new UserCapital();
				capital.setUserId(trad.getUserId());
				capital.setChangeCapital(trad.getTotalAmount());
				mUserCapitalService.updateCapitalRecharge(capital);

				CapitalHistory history = new CapitalHistory();
				history.setOrderId(UUID.randomUUID().toString()
						.replace("-", ""));
				history.setId(0);
				history.setChange(trad.getTotalAmount());
				history.setUpdateTime(DateUtils.getCurrentTimestamp());
				history.setCapitalType(1);
				history.setUserId(trad.getUserId());
				history.setPayType("wx");
				mUserCapitalService.insertUserCapitalHistory(history);

				RechargeGift gift = new RechargeGift();
				gift.setUserId(trad.getUserId());
				gift.setChange(trad.getTotalAmount());
				mUserCapitalService.updateRechargeGift(gift);
				
			}else{
				trade.setTradeStatus(3);
			}
			
			payService.updateTradeNoStatus(trade);
			
			return returnXML(result_code);
		} else {
			trade.setTradeStatus(3);
			
			payService.updateTradeNoStatus(trade);
			
			return returnXML("FAIL");
		}

	}

	@ResponseBody
	@RequestMapping("create_trade_no")
	public String createTradeNo(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		TradeNo tradeNo = JSONObject.parseObject(data, TradeNo.class);
		try {
			payService.saveTradeNo(tradeNo);
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.CREATE_TRADE_NO, result,
				JSONObject.toJSONString(tradeNo));
	}

	private boolean checkSign(String xmlString) {

		Map<String, String> map = null;

		try {

			map = XMLUtil.doXMLParse(xmlString);

		} catch (Exception e) {
			e.printStackTrace();
		}

		String signFromAPIResponse = map.get("sign").toString();

		if (signFromAPIResponse == "" || signFromAPIResponse == null) {

			System.out.println("API返回的数据签名数据不存在，有可能被第三方篡改!!!");

			return false;

		}
		System.out.println("服务器回包里面的签名是:" + signFromAPIResponse);

		// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名

		map.put("sign", "");

		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较

		String signForAPIResponse = getSign(map);

		if (!signForAPIResponse.equals(signFromAPIResponse)) {

			// 签名验不过，表示这个API返回的数据有可能已经被篡改了

			System.out
					.println("API返回的数据签名验证不通过，有可能被第三方篡改!!! signForAPIResponse生成的签名为"
							+ signForAPIResponse);

			return false;

		}

		System.out.println("恭喜，API返回的数据签名验证通过!!!");

		return true;

	}

	private String returnXML(String return_code) {

		String result = "<xml><return_code><![CDATA["

		+ return_code

		+ "]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
		
		return result;
	}

	public String getSign(Map<String, String> map) {
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
			signParams.put(stringStringEntry.getKey(),
					stringStringEntry.getValue());
		}
		signParams.remove("sign");
		String sign = PayCommonUtil.createSign("UTF-8", signParams);
		return sign;
	}

}
