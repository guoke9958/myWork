package com.xa.qyw.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xa.qyw.BizCode;
import com.xa.qyw.entiy.CapitalHistory;
import com.xa.qyw.entiy.Order;
import com.xa.qyw.entiy.RechargeGift;
import com.xa.qyw.entiy.UserCapital;
import com.xa.qyw.service.UserCapitalService;
import com.xa.qyw.service.UserVoucherService;
import com.xa.qyw.utils.DateUtils;
import com.xa.qyw.utils.ResponseUtils;

@Controller
@RequestMapping("/api/capital/")
public class CapitalController {

	@Autowired
	UserCapitalService mUserCapitalService;
	
	@Autowired
	UserVoucherService mUserVoucherSrevice;

	@ResponseBody
	@RequestMapping("userid")
	public String getUserCapital(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		CapitalHistory his = JSONObject.parseObject(data, CapitalHistory.class);
		UserCapital userCapital = new UserCapital();
		try {
			userCapital.setUserId(his.getUserId());
			userCapital = mUserCapitalService.getUserCapital(userCapital);

			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.GET_USER_CAPITAL, result,
				JSONObject.toJSONString(userCapital));

	}

	@ResponseBody
	@RequestMapping("history")
	public String getCapitalHistory(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		CapitalHistory his = JSONObject.parseObject(data, CapitalHistory.class);
		List<CapitalHistory> list = new ArrayList<CapitalHistory>();
		try {
			if (his.getCapitalType() == 6) {
				list = mUserCapitalService.getCapitalHistoryInCome(his);
			} else {
				list = mUserCapitalService.getCapitalHistory(his);
			}
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.GET_USER_CAPITAL, result,
				JSONObject.toJSONString(list));

	}

	@ResponseBody
	@RequestMapping("history_income")
	public String getCapitalHistoryIncomeI(
			@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		CapitalHistory his = JSONObject.parseObject(data, CapitalHistory.class);
		List<CapitalHistory> list = new ArrayList<CapitalHistory>();
		try {
			list = mUserCapitalService.getCapitalHistoryInCome(his);
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.GET_USER_CAPITAL, result,
				JSONObject.toJSONString(list));

	}

	@ResponseBody
	@RequestMapping("get_recharge_gift")
	public String getRechargeGiftByUserId(
			@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		RechargeGift gift = new RechargeGift();
		try {
			gift.setUserId(data);
			gift = mUserCapitalService.getUserGift(gift);
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.GET_RECHARGE_GIFT, result,
				JSONObject.toJSONString(gift));
	}

	@ResponseBody
	@RequestMapping("expand_gift")
	public String getExpandGiftByUserId(
			@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		RechargeGift gift = new RechargeGift();
		try {
			gift.setUserId(data);
			gift.setChange(500);
			gift.setIsGet(0);
			mUserCapitalService.updateExpandGift(gift);

			UserCapital capital = new UserCapital();
			capital.setUserId(data);
			capital.setChangeCapital(2);
			mUserCapitalService.updateCapitalRecharge(capital);

			CapitalHistory history = new CapitalHistory();
			history.setOrderId(UUID.randomUUID().toString().replace("-", ""));
			history.setId(0);
			history.setChange(2);
			history.setUpdateTime(DateUtils.getCurrentTimestamp());
			history.setCapitalType(5);
			history.setUserId(data);
			history.setPayType("libao");
			mUserCapitalService.insertUserCapitalHistory(history);

			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.GET_RECHARGE_GIFT, result,
				JSONObject.toJSONString(gift));
	}

	@ResponseBody
	@RequestMapping("payToDoctor")
	public String payToDoctor(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;

		try {
			CapitalHistory his = JSONObject.parseObject(data,
					CapitalHistory.class);
			UserCapital userCapital = new UserCapital();
			userCapital.setUserId(his.getUserId());
			UserCapital capital = mUserCapitalService
					.getUserCapital(userCapital);

			if (capital.getCapitalTotal() >= his.getChange()) {

				// 扣掉自己的钱

				capital.setChangeCapital(his.getChange());
				capital.setUserId(his.getUserId());
				mUserCapitalService.updateCapitalExpand(capital);

				// 给别人充钱
				capital.setChangeCapital(his.getChange());
				capital.setUserId(his.getToUserId());
				mUserCapitalService.updateCapitalInCome(capital);

				mUserCapitalService.insertUserCapitalHistory(his);

				result = ResponseUtils.SUCCESS;
			} else {
				result = 3;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseUtils.createResponse(BizCode.GET_RECHARGE_GIFT, result,
				JSONObject.toJSONString(""));
	}

	@ResponseBody
	@RequestMapping("getHongBaoDetail")
	public String getHongBaoDetail(@RequestParam(value = "data") String data) {
		CapitalHistory his = new CapitalHistory();
		int result = ResponseUtils.FAIL;
		try {
			his = mUserCapitalService.getHongBaoDetail(data);
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseUtils.createResponse(BizCode.GET_RECHARGE_GIFT, result,
				JSONObject.toJSONString(his));
	}

	@ResponseBody
	@RequestMapping("get_order")
	public String getOrderById(@RequestParam(value = "data") String data) {
		Order order = new Order();
		int result = ResponseUtils.FAIL;

		try {
			order = mUserCapitalService.getOrderById(data);

			if (order == null) {
				mUserCapitalService.insertOrder(new Order(0, data, DateUtils
						.getCurrentTimestamp()));
			}

			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseUtils.createResponse(BizCode.GET_RECHARGE_GIFT, result,
				JSONObject.toJSONString(order));
	}
	
	
	@ResponseBody
	@RequestMapping("get_voucher_count")
	public String getVoucherTotalCount(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		int count = 0;
		try {
			count = mUserVoucherSrevice.getVoucherUserTotal(data);

			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseUtils.createResponse(BizCode.GET_RECHARGE_GIFT, result,
				JSONObject.toJSONString(count));
	}
	
}
