package com.dub.spring.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Only one web page in this application
 * */

@Controller
public class DefaultController {
	
	@Value("${startContainerUrl}")
	private String startContainerUrl;
	
	@Value("${stopContainerUrl}")
	private String stopContainerUrl;
	
	@RequestMapping({"/", "/index"})
	public String home(ModelMap model) {
		
		model.addAttribute("startContainerUrl", startContainerUrl);
		model.addAttribute("stopContainerUrl", stopContainerUrl);
		
		return "dashboard";
	}
}
