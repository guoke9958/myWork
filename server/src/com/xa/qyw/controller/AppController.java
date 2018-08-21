package com.xa.qyw.controller;

import java.io.File;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.xa.qyw.entiy.App;
import com.xa.qyw.service.AppService;
import com.xa.qyw.utils.FileUpLoadUtil;
import com.xa.qyw.utils.ResponseUtils;
import com.xa.qyw.utils.StringUtils;
import com.xa.qyw.utils.UrlUtils;

@Controller
@RequestMapping("/api/app")
public class AppController {

	@Autowired
	AppService mAppService;

	@SuppressWarnings("finally")
	@RequestMapping(value = "/uploadview")
	public String getMaxAppJsp(HttpServletRequest request, Model model) {
		int code = 0;
		try {
			code = mAppService.getMaxAppVersionCode();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			model.addAttribute("versionCode", code);
			return "/upload";
		}
	}

	@RequestMapping(value = "/create")
	public String create(HttpServletRequest request, Model model,
			@RequestParam(value = "file") MultipartFile[] files)
			throws Exception {
		String result = "添加失败";
		App app = new App();
		String httpPath = null;
		try {
			app.setId(0);
			app.setVersionName(request.getParameter("versionName"));
			app.setVersionCode(Integer.parseInt(request
					.getParameter("newversion")));
			app.setType(request.getParameter("type"));
			app.setUpdateContext(request.getParameter("updatecontent"));
			app.setIsFlag(Integer.parseInt(request.getParameter("forces")));

			for (MultipartFile mf : files) {
				if (!mf.isEmpty()) {
					String path = FileUpLoadUtil.getAppBaseUpLoadRoot(request);
					httpPath = FileUpLoadUtil.fileAppUpload(path, mf);
					app.setDowndloadUrl(UrlUtils.DOWNLOAD_APP_FILE + httpPath);
				}
			}

			if (httpPath != null) {
				app.setCreateTime(new Timestamp(System.currentTimeMillis()));
				mAppService.addApp(app);
				result = "添加成功";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("result", result);
		int code = mAppService.getMaxAppVersionCode();
		model.addAttribute("versionCode", code);

		if ("添加失败".equals(result) && !StringUtils.isEmpty(httpPath)) {
			String path = FileUpLoadUtil.getAppBaseUpLoadRoot(request);
			new File(path + httpPath).delete();
		}

		return "/upload";

	}

	@RequestMapping("/version/info")
	@ResponseBody
	public String getMaxAppVersion(HttpServletRequest request) {
		Integer result = ResponseUtils.FAIL;
		App app = null;
		try {
			int code = mAppService.getMaxAppVersionCode();
			app = mAppService.getMaxApp(code);
			result = ResponseUtils.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseUtils.createResponse(1000, result,
				JSONObject.toJSONString(app));
	}

}
