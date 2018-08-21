package com.xa.qyw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/admin/")
public class AdminController {

	@RequestMapping("add")
	public String getAddView(){
		return "/add";
	}
	
}
