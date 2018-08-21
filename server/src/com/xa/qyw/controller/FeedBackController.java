package com.xa.qyw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xa.qyw.BizCode;
import com.xa.qyw.entiy.FeedBack;
import com.xa.qyw.service.FeedBackService;
import com.xa.qyw.utils.ResponseUtils;

@Controller
@RequestMapping("/api/feedback/")
public class FeedBackController {

	@Autowired
	FeedBackService mFeedBackService;

	@ResponseBody
	@RequestMapping("add")
	public String addFeedBack(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;

		try {
			FeedBack feedback = JSONObject.parseObject(data, FeedBack.class);
			int id = mFeedBackService.addFeedBack(feedback);
			if (id == 1) {
				result = ResponseUtils.SUCCESS;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.GET_RECHARGE_GIFT, result,
				"");
	}

}
