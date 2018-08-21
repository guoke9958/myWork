package com.xa.qyw.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xa.qyw.BizCode;
import com.xa.qyw.service.UserInfoService;
import com.xa.qyw.utils.FileUpLoadUtil;
import com.xa.qyw.utils.ResponseUtils;


@Controller
@RequestMapping("/api/file/")
public class FileUploadController {

	@Autowired
	UserInfoService mUserInfoService;
	
	
	@ResponseBody
	@RequestMapping("upload_user_photo")
	public String upLoadUserPhoto(HttpServletRequest request,
			@RequestParam(value = "file") MultipartFile[] files) {
		int result = ResponseUtils.FAIL;
		String httpPath = "";
		try {

			for (MultipartFile mf : files) {
				if (!mf.isEmpty()) {
					String path = FileUpLoadUtil
							.getUserPhotoBaseUpLoadRoot(request);
					httpPath = "userphoto/"
							+ FileUpLoadUtil.fileupload(path, mf);
				}
			}
			
			
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(BizCode.ADD_USER_INFO, result,
				httpPath);
	}
	
}
