package com.xa.qyw.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xa.qyw.BizCode;
import com.xa.qyw.entiy.UserInfo;
import com.xa.qyw.entiy.app.DoctorDetailInfo;
import com.xa.qyw.entiy.app.SearchDoctorByDepartmentAndCity;
import com.xa.qyw.entiy.app.SimpleDoctor;
import com.xa.qyw.otherweb.rongyun.RongYunUtils;
import com.xa.qyw.service.DoctorService;
import com.xa.qyw.service.UserInfoService;
import com.xa.qyw.utils.ResponseUtils;

@Controller
@RequestMapping("/api/doctor/")
public class DoctorController {

	@Autowired
	DoctorService mDoctorService;
	
	@Autowired
	UserInfoService mUserInfoService;

	@ResponseBody
	@RequestMapping("all_by_department")
	public String getAllDoctor(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		List<SimpleDoctor> list = new ArrayList<SimpleDoctor>();

		try {

			SearchDoctorByDepartmentAndCity search = JSONObject.parseObject(
					data, SearchDoctorByDepartmentAndCity.class);
			
			String item = search.getGrade();
            String grade = "";
            if (item.contains("三")) {
                grade = "三";
            } else if (item.contains("二")) {
                grade = "二";
            } else if (item.contains("私立")) {
                grade = "私立";
            }else if(item.contains("卫生")){
            	grade = "卫";
            } else if(item.contains("医疗")){
                grade = "医疗";
            }else if(item.contains("null")){
            	grade = "";
            }
            
            search.setGrade("%"+grade+"%");
			
			if(item.contains("生活")||item.contains("学校")||item.contains("爱尔眼科")){
				list = mDoctorService.getUserByDepartmentsId(search);
			}else{
				list = mDoctorService.getDoctorByDepartmentsId(search);
			}
			
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.GET_ALL_BY_DEPARTMENT_ID,
				result, JSONObject.toJSONString(list));
	}

	@ResponseBody
	@RequestMapping("detail")
	public String getDoctorDetail(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		UserInfo doctor = new UserInfo();
		String error = "加载失败！";
		try {

			doctor = mUserInfoService.getUserInfoById(data);
			doctor.setState(RongYunUtils.getUserState(doctor.getUserId()));
			result = ResponseUtils.SUCCESS;
			error = "加载完成";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.DOCTOR_DETAIL, result,
				JSONObject.toJSONString(doctor),error);
	}

	@ResponseBody
	@RequestMapping("all")
	public String getAllDoctor() {
		int result = ResponseUtils.FAIL;
		List<DoctorDetailInfo> list = new ArrayList<DoctorDetailInfo>();
		try {

			list = mDoctorService.getAllDoctor();
			
			for(int i=0;i<list.size();i++){
				DoctorDetailInfo docInfo = list.get(i);
				mDoctorService.updateDoctorIntro(docInfo);
				System.out.println("成功");
			}
			
			
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.DOCTOR_DETAIL, result,
				JSONObject.toJSONString(list));
	}

}
