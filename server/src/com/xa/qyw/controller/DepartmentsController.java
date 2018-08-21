package com.xa.qyw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xa.qyw.BizCode;
import com.xa.qyw.entiy.AddDepartments;
import com.xa.qyw.entiy.DepartmentInfo;
import com.xa.qyw.entiy.app.SimpleDoctor;
import com.xa.qyw.service.DoctorService;
import com.xa.qyw.service.NewDepartmentsService;
import com.xa.qyw.utils.ResponseUtils;

@Controller
@RequestMapping("/api/departments/")
public class DepartmentsController {

	@Autowired
	NewDepartmentsService newDepartmentsService;
	
	@Autowired
	DoctorService mDoctorService;

	@ResponseBody
	@RequestMapping("all")
	public String getAllDepartments() {
		int result = ResponseUtils.FAIL;
		List<AddDepartments> list = null;
		try {
			list = newDepartmentsService.getAllDepartments();
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseUtils.createResponse(BizCode.GET_ALL_DEPARTMENT, result,
				JSONObject.toJSONString(list));
	}
	
	@ResponseBody
	@RequestMapping("allByGrade")
	public String getAllDepartmentsByGrade(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		List<AddDepartments> list = null;
		try {
			
			if(data.contains("null")){
				list = newDepartmentsService.getAllDepartments();
			}else{
				data = data.replace("%", "");
				list = newDepartmentsService.getDepartmentsByGrade(data);
			}
			
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseUtils.createResponse(BizCode.GET_ALL_DEPARTMENT, result,
				JSONObject.toJSONString(list));
	}
	
	@ResponseBody
	@RequestMapping("detail")
	public String getDepartmentDetail(@RequestParam(value = "data") String data){
		int result = ResponseUtils.FAIL;
		DepartmentInfo departInfo = null;
		try {
//			DepartmentInfo info = JSONObject.parseObject(data, DepartmentInfo.class);
			
			DepartmentInfo info = new DepartmentInfo();
			info.setDepartmentId(182);
			info.setHospitalId(1);
			
			departInfo = newDepartmentsService.getDepartmentInfoByHosptailIdAndDepartmentId(info);
			
			List<SimpleDoctor> list = mDoctorService.getDoctorByDepartmentsIdAndHospitalId(info);
			departInfo.setListDoctor(list);
			
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ResponseUtils.createResponse(BizCode.GET_DEPARTMENT_DETAIL, result,
				JSONObject.toJSONString(departInfo));
	}
	

}
