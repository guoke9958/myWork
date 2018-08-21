package com.xa.qyw.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xa.qyw.BizCode;
import com.xa.qyw.entiy.HospitalDetailInfo;
import com.xa.qyw.entiy.HospitalGrade;
import com.xa.qyw.entiy.app.SimpleDepartment;
import com.xa.qyw.service.HospitalService;
import com.xa.qyw.service.NewDepartmentsService;
import com.xa.qyw.tools.mail.SendMail;
import com.xa.qyw.utils.ResponseUtils;
import com.xa.qyw.utils.StringUtils;

@Controller
@RequestMapping("/api/hospital/")
public class HospitalController {

	@Autowired
	HospitalService mHospitalService;

	@Autowired
	NewDepartmentsService newDepartmentsService;

	@ResponseBody
	@RequestMapping("detail")
	public String getSingleHospital(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		HospitalDetailInfo hospital = null;
		try {
			hospital = mHospitalService.getHospitalById(Integer.parseInt(data));
			List<SimpleDepartment> list = newDepartmentsService
					.getListDepartment(Integer.parseInt(data));
			hospital.setListDepart(list);
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.DEPARTMENT_DETAIL_INFO,
				result, JSONObject.toJSONString(hospital));
	}

	@ResponseBody
	@RequestMapping("all")
	public String getAllHospital() {
		int result = ResponseUtils.FAIL;
		List<HospitalDetailInfo> hospitals = null;
		try {
			hospitals = mHospitalService.getAllHospital();
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.DEPARTMENT_DETAIL_INFO,
				result, JSONObject.toJSONString(hospitals));
	}

	@ResponseBody
	@RequestMapping("querylike")
	public String getAllHospital(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		List<HospitalDetailInfo> hospitals = null;
		try {
			hospitals = mHospitalService.queryLikeHospitalByName(data);
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.DEPARTMENT_DETAIL_INFO,
				result, JSONObject.toJSONString(hospitals));
	}

	@ResponseBody
	@RequestMapping("queryHospitalCity")
	public String queryHospitalByCity(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		List<HospitalDetailInfo> hospitals = null;
		try {
			HospitalDetailInfo info = JSONObject.parseObject(data,
					HospitalDetailInfo.class);
			
			if("医疗服务".equals(info.getHospital_grade())){
				info.setHospital_type(2);
			}else{
				info.setHospital_type(1);
			}
			
			String item = info.getHospital_grade();
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
            }
            
		    info.setHospital_grade("%"+grade+"%");
			
			
			hospitals = mHospitalService.queryHospitalByCity(info);
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.DEPARTMENT_DETAIL_INFO,
				result, JSONObject.toJSONString(hospitals));
	}

	@ResponseBody
	@RequestMapping("add")
	public String userAddHospital(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		String des = "添加失败";
		try {
			HospitalDetailInfo info = JSONObject.parseObject(data,
					HospitalDetailInfo.class);

			if (mHospitalService.queryHospitalByName(info) == null) {
				
				if("其它".equals(info.getHospital_grade())){
					info.setHospital_type(2);
				}
				
				mHospitalService.insertHospital(info);
				result = ResponseUtils.SUCCESS;

				SendMail.addHospital(data);

			} else {
				des = "该医院已经存在";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.DEPARTMENT_DETAIL_INFO,
				result, "", des);
	}

	@ResponseBody
	@RequestMapping("addByText")
	public String addHospital(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		try {
			HospitalDetailInfo info = JSONObject.parseObject(data,
					HospitalDetailInfo.class);
			mHospitalService.insertHospital(info);
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.DEPARTMENT_DETAIL_INFO,
				result, "");
	}

	@ResponseBody
	@RequestMapping("addHospitalText")
	public String addHospital() {

		List<String> list = new ArrayList<String>();

		String filePath = "D:/全国总医院数.txt";

		try {

			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {

					if (!StringUtils.isEmpty(lineTxt)) {
						list.add(lineTxt);
					}

				}
				read.close();
			}

			for (String str : list) {
				String hosInfo = str.trim().replace("\n", "");

				HospitalDetailInfo info = new HospitalDetailInfo();

				String[] arr = hosInfo.split("#");
				String province = arr[0];
				String city = arr[1];
				String name = arr[2];
				String grade = arr[3];

				info.setHospital_province(province);
				info.setHospital_city(city);
				info.setHospital_grade(grade);
				info.setHospital_name(name);

				if (mHospitalService.queryHospitalByName(info) == null) {
					System.err.println(hosInfo);
					mHospitalService.insertHospital(info);
				} else {
					System.err.println(hosInfo + "==========");
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.DEPARTMENT_DETAIL_INFO, 0,
				"");
	}

	@ResponseBody
	@RequestMapping("getAllHospitalGrade")
	public String getHospitalGrade() {
		int result = ResponseUtils.FAIL;
		List<HospitalGrade> list = null;
		try {
			list = mHospitalService.getAllHospitalGrade();
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.DEPARTMENT_DETAIL_INFO,
				result, JSONObject.toJSONString(list));
	}

}
