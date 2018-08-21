package com.xa.qyw.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xa.qyw.BizCode;
import com.xa.qyw.entiy.MsgState;
import com.xa.qyw.service.MsgService;
import com.xa.qyw.utils.DateUtils;
import com.xa.qyw.utils.ResponseUtils;
import com.xa.qyw.utils.StringUtils;

@Controller
@RequestMapping("/api/auth/")
public class MessageAuthCodeController {

	@Autowired
	MsgService mMsgService;

	@ResponseBody
	@RequestMapping("report")
	public String notifyMessageReport(HttpServletRequest request) {
		String report = request.getParameter("report");

		if (!StringUtils.isEmpty(report)) {
			if (report.contains(";")) {

				String[] arr = report.split(";");

				for (String message : arr) {
					updateMsgReport(message);
				}

			} else {
				updateMsgReport(report);
			}
		}

		System.out.println("report=====" + report);
		return "0";
	}

	private void updateMsgReport(String report) {
		try {
			String[] arr = report.split("\\|");
			String phoneNumber = arr[0];
			String state = getState(arr[1]);
			String orderId = arr[2];

			MsgState msg = new MsgState();
			msg.setPhoneNumber(phoneNumber);
			msg.setOrderId(orderId);
			msg.setReportState(state);
			msg.setReportTime(DateUtils.getCurrentTimestamp());

			mMsgService.updateMsgState(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getState(String state) {
		String message = "";
		if ("DELIVRD".equals(state)) {
			message = "状态成功";
		} else if ("UNDELIV".equals(state)) {
			message = "状态失败";
		} else if ("EXPIRED".equals(state)) {
			message = "因为用户长时间关机或者不在服务区等导致的短消息超时没有递交到用户手机上";
		} else if ("REJECTD".equals(state)) {
			message = "消息因为某些原因被拒绝";
		} else if ("MBBLACK".equals(state)) {
			message = "黑号";
		}
		return message;
	}

	@ResponseBody
	@RequestMapping("query")
	public String authCodeQuery(HttpServletRequest request) {
		int result = ResponseUtils.FAIL;
		String phoneNumber = request.getParameter("phone");
		String code = request.getParameter("code");

		try {
			MsgState msg = new MsgState();
			msg.setPhoneNumber(phoneNumber);
			msg.setRandomCode(code);
			MsgState localMsg = mMsgService.getMsgState(msg);

			if (localMsg != null) {
				if (localMsg.getRandomCode().equals(code)) {
					mMsgService.updateIsUsed(localMsg.getId());
					result = ResponseUtils.SUCCESS;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseUtils.createResponse(BizCode.LOGIN, result, "");
	}

}
