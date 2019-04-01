package com.sfu.an3di.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.sfu.an3di.ApplicationConfig;
import com.sfu.an3di.utils.CommonUtils;

@RestController
public class ViewController {

	@Autowired
	private ApplicationConfig appConfig;
	
	private Logger logger = CommonUtils.getLogger();

	@GetMapping("/")
    public ModelAndView root(Model m) {
    	logger.info("enter index");
    	m.addAttribute("appName",appConfig.getAppName());
    	
        return new ModelAndView("index");
    }
	
    @GetMapping("/index")
    public ModelAndView index(Model m) {
    	logger.info("enter index");
    	m.addAttribute("appName", appConfig.getAppName());
    	
        return new ModelAndView("index");
    }
    
    @GetMapping("/stock")
    public ModelAndView stock(Model m) {
    	logger.info("enter stock");
    	m.addAttribute("appName", appConfig.getAppName());
    	
        return new ModelAndView("work");
    }
}
