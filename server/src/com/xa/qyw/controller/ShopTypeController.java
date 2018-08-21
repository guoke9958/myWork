package com.xa.qyw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xa.qyw.BizCode;
import com.xa.qyw.entiy.Shop;
import com.xa.qyw.entiy.ShopType;
import com.xa.qyw.service.ShopTypeService;
import com.xa.qyw.utils.ResponseUtils;

@Controller
@RequestMapping("/api/shoptype/")
public class ShopTypeController {

	@Autowired
	ShopTypeService shopTypeService;

	@ResponseBody
	@RequestMapping("all")
	public String getAllShopType(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		List<ShopType> list = null;
		Shop shop = new Shop();
		try {
			list = shopTypeService.getAllType();
			
			shop.setListType(list);
			
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseUtils.createResponse(BizCode.GET_ALL_SHOP_TYPE, result,
				JSONObject.toJSONString(shop));
	}

}
