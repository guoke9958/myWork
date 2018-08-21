package com.xa.qyw.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.xa.qyw.BizCode;
import com.xa.qyw.entiy.VideoDetail;
import com.xa.qyw.entiy.VideoType;
import com.xa.qyw.service.VideoService;
import com.xa.qyw.utils.DateUtils;
import com.xa.qyw.utils.FileUpLoadUtil;
import com.xa.qyw.utils.ResponseUtils;
import com.xa.qyw.utils.UrlUtils;

@Controller
@RequestMapping("/api/video/")
public class VideoController {

	@Autowired
	VideoService mVideoService;

	@RequestMapping(value = "/uploadview")
	public String getMaxAppJsp(HttpServletRequest request, Model model) {
		return "/videoupload";
	}
	
	@RequestMapping(value = "/addWeb")
	public String getUploadView(HttpServletRequest request, Model model) {
		return "/uploadWeb";
	}
	
	@RequestMapping(value = "/create")
	public String create(HttpServletRequest request, Model model,
			@RequestParam(value = "videofile") MultipartFile[] files,@RequestParam(value = "videoimage") MultipartFile[] file)
			throws Exception {
		String result = "添加失败";
		VideoDetail detail = new VideoDetail();
		String httpPath = null;
		try {
			detail.setId(0);
			detail.setVideoName(request.getParameter("name"));
			detail.setTypeId(Integer.parseInt(request
					.getParameter("videotype")));
			detail.setCreateTime(DateUtils.getCurrentTimestamp());
			detail.setIsWeb(0);

			for (MultipartFile mf : files) {
				if (!mf.isEmpty()) {
					String path = FileUpLoadUtil.getVideoBaseUpLoadRoot(request);
					httpPath = FileUpLoadUtil.fileVideoUpload(path, mf);
					detail.setPlayUrl(UrlUtils.DOWNLOAD_VIDEO_FILE + httpPath);
				}
			}
			
			for (MultipartFile mf : file) {
				if (!mf.isEmpty()) {
					String path = FileUpLoadUtil.getVideoImageBaseUpLoadRoot(request);
					httpPath = FileUpLoadUtil.fileVideoImageUpload(path, mf);
					detail.setVideoImage(UrlUtils.DOWNLOAD_VIDEO_IMAGE_FILE + httpPath);
				}
			}
			

			if (httpPath != null) {
				mVideoService.insertVideDetail(detail);
				result = "添加成功";
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("result", result);


		return "/videoupload";

	}
	
	
	@RequestMapping(value = "/insertWeb")
	public String addDianShiJu(HttpServletRequest request, Model model,
			@RequestParam(value = "videoimage") MultipartFile[] file)
			throws Exception {
		String result = "添加失败";
		VideoDetail detail = new VideoDetail();
		String httpPath = null;
		try {
			detail.setId(0);
			detail.setVideoName(request.getParameter("name"));
			detail.setTypeId(Integer.parseInt(request
					.getParameter("videotype")));
			detail.setCreateTime(DateUtils.getCurrentTimestamp());
			detail.setIsWeb(1);
			detail.setPlayUrl(request.getParameter("webUrl"));
			detail.setSource(request.getParameter("source"));

			for (MultipartFile mf : file) {
				if (!mf.isEmpty()) {
					String path = FileUpLoadUtil.getVideoImageBaseUpLoadRoot(request);
					httpPath = FileUpLoadUtil.fileVideoImageUpload(path, mf);
					detail.setVideoImage(UrlUtils.DOWNLOAD_VIDEO_IMAGE_FILE + httpPath);
				}
			}
			

			if (httpPath != null) {
				mVideoService.insertVideDetail(detail);
				result = "添加成功";
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("result", result);


		return "/uploadWeb";

	}
	
	

	@ResponseBody
	@RequestMapping("allType")
	public String getUserPayPwd() {
		int result = ResponseUtils.FAIL;
		List<VideoType> listType = new ArrayList<VideoType>();
		try {

			listType = mVideoService.getAllVideoType();
			result = ResponseUtils.SUCCESS;

		} catch (Exception e) {
			System.err.println(e);
		}

		return ResponseUtils.createResponse(BizCode.GET_RECHARGE_GIFT, result,
				JSONObject.toJSONString(listType));
	}

	@ResponseBody
	@RequestMapping("videosByType")
	public String getVideoByType(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		List<VideoDetail> listType = new ArrayList<VideoDetail>();
		try {

			listType = mVideoService.getVideoByType(Integer.parseInt(data));
			result = ResponseUtils.SUCCESS;

		} catch (Exception e) {
			System.err.println(e);
		}

		return ResponseUtils.createResponse(BizCode.GET_RECHARGE_GIFT, result,
				JSONObject.toJSONString(listType));
	}

	@ResponseBody
	@RequestMapping("videoDetail")
	public String getVideoDetail(@RequestParam(value = "data") String data) {
		int result = ResponseUtils.FAIL;
		VideoDetail listType = new VideoDetail();
		try {

			listType = mVideoService.getVideoDetail(Integer.parseInt(data));
			result = ResponseUtils.SUCCESS;

		} catch (Exception e) {
			System.err.println(e);
		}

		return ResponseUtils.createResponse(BizCode.GET_RECHARGE_GIFT, result,
				JSONObject.toJSONString(listType));
	}

}
