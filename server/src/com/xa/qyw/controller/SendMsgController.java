package com.xa.qyw.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xa.qyw.entiy.MsgState;
import com.xa.qyw.entiy.OrderMsg;
import com.xa.qyw.entiy.Push;
import com.xa.qyw.entiy.SimpUser;
import com.xa.qyw.entiy.User;
import com.xa.qyw.entiy.UserInfo;
import com.xa.qyw.entiy.Voucher;
import com.xa.qyw.otherweb.note.MsgSend;
import com.xa.qyw.otherweb.rongyun.RongYunUtils;
import com.xa.qyw.otherweb.xinge.XinGeUtils;
import com.xa.qyw.service.MsgService;
import com.xa.qyw.service.OrderMsgService;
import com.xa.qyw.service.UserInfoService;
import com.xa.qyw.service.UserService;
import com.xa.qyw.service.UserVoucherService;
import com.xa.qyw.utils.BadWord;
import com.xa.qyw.utils.DateUtils;
import com.xa.qyw.utils.ResponseUtils;
import com.xa.qyw.utils.StringUtils;

@Controller
@RequestMapping("/api/send_msg/")
public class SendMsgController {

	@Autowired
	UserService mUserService;

	@Autowired
	OrderMsgService mOrderMsgService;

	@Autowired
	MsgService mMsgService;

	@Autowired
	UserInfoService mUserInfoService;

	@Autowired
	UserVoucherService mUserVoucherService;

	@ResponseBody
	@RequestMapping("singleUser")
	public String getAllUser(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		try {

			Push push = JSONObject.parseObject(data, Push.class);
			List<String> mListStr = new ArrayList<String>();
			mListStr.add(push.getUserId());
			RongYunUtils.publishWelcomeSystemMessage(mListStr,
					push.getContent());
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(1000, result, "");
	}

	private void sendMessage(final int length, final List<SimpUser> list,
			final String data) {
		new Thread() {

			public void run() {
				try {
					List<String> mListStr = new ArrayList<String>();
					for (int j = 0; j < length; j++) {
						mListStr.clear();
						for (int i = 0; i < 100; i++) {
							SimpUser user = list.get(i);
							mListStr.add(user.getUserId());
						}
						RongYunUtils
								.publishWelcomeSystemMessage(mListStr, data);
						sleep(1100);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			};

		}.start();

	}

	@ResponseBody
	@RequestMapping("sendDoctor")
	public String sendToUser(@RequestParam(value = "data") String data) {

		int result = ResponseUtils.FAIL;
		String des = "发送失败";

		try {
			OrderMsg orderMsg = JSONObject.parseObject(data, OrderMsg.class);
			UserInfo user = mUserInfoService.getUserInfoById(orderMsg
					.getDoctorId());
			String isBad = BadWord.replace(orderMsg.getOrderInfo(), "*");
			if (isBad.contains("*")) {
				des = "文字信息含有敏感词";
			} else {
				long code = 0;
				if ("2".equals(user.getType())) {
					code = MsgSend.sendNoteOrderMsg(orderMsg.getDoctorName(),
							orderMsg.getUserName(), orderMsg.getOrderInfo(),
							orderMsg.getDoctorPhone(), orderMsg.getSex(),
							orderMsg.getUserAge(), orderMsg.getUserAddress());
				} else {

					code = MsgSend.sendNoteOrderMsg(user.getNickName(),
							orderMsg.getUserName(), orderMsg.getOrderInfo(),
							orderMsg.getDoctorPhone(),
							orderMsg.getUserAddress());
				}

				if (code > 0) {
					des = "发送成功";
					orderMsg.setCreateTime(DateUtils.getCurrentTimestamp());
					mOrderMsgService.addOrderMsg(orderMsg);

					if ("2".equals(user.getType())) {
						RongYunUtils.publishMessage(orderMsg.getDoctorId(),
								orderMsg.getOrderInfo() + "\n" + "出生日期："
										+ orderMsg.getUserAge() + "\n" + "地址："
										+ orderMsg.getUserAddress(),
								orderMsg.getUserId());
					} else {
						RongYunUtils.publishMessage(orderMsg.getDoctorId(),
								orderMsg.getOrderInfo(), orderMsg.getUserId());
					}

					result = ResponseUtils.SUCCESS;

					String info = MsgSend.getMsgSendState(code);

					MsgState state = new MsgState();
					state.setId(0);
					state.setOrderId(code + "");
					state.setPhoneNumber(orderMsg.getDoctorPhone());
					state.setRandomCode(orderMsg.getOrderInfo());
					state.setSendState(info);
					state.setIsUsed(1);
					state.setSendTime(DateUtils.getCurrentTimestamp());
					mMsgService.insertMsgSendState(state);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(1000, result, "", des);
	}

	@ResponseBody
	@RequestMapping("push")
	public String pushToAllUser(@RequestParam(value = "data") String data) {

		int result = ResponseUtils.FAIL;
		String des = "发送失败";

		try {
			Push push = JSONObject.parseObject(data, Push.class);

			if (!push.getUserId().equals(push.getToUserId())) {

				if (!StringUtils.isEmpty(push.getImgPath())) {
					RongYunUtils.publishImageMessage(push.getToUserId(),
							push.getContent(), push.getImgPath(),
							push.getUserId());
				} else {

					String ret = RongYunUtils.publishImageMessage(
							push.getToUserId(), push.getContent(),
							push.getImgPath(), push.getUserId());

				}

			}

			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(1000, result, "", des);
	}

	@ResponseBody
	@RequestMapping("pushAll")
	public String pushAllUser(@RequestParam(value = "data") String data) {

		int result = ResponseUtils.FAIL;
		String des = "发送失败";

		try {
			Push push = JSONObject.parseObject(data, Push.class);

			int total = mUserVoucherService.getVoucherUserTotal(push
					.getUserId());

			if (total > 1) {
				org.json.JSONObject re = XinGeUtils.sendPushXinGe(push);

				des = re.toString();

				String ret = re.optString("ret_code");

				if ("0".equals(ret)) {

					Voucher voucher = new Voucher();
					voucher.setUserId(push.getUserId());
					voucher.setUserVoucher(total - 2);
					mUserVoucherService.updateVoucher(voucher);

					if (!StringUtils.isEmpty(push.getImgPath())) {
						RongYunUtils.publishWelcomeSystemImageMessage(
								push.getUserId(), push.getContent(),
								push.getImgPath());
					} else {

						RongYunUtils.publishWelcomeSystemMessage(
								push.getUserId(), push.getContent());
					}

					result = ResponseUtils.SUCCESS;

				} else {
					des = "账户余额不足";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(1000, result, "", des);
	}

}
