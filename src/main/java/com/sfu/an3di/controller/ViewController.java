package com.sfu.an3di.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.sfu.an3di.ApplicationConfig;
import com.sfu.an3di.pojo.Stock;
import com.sfu.an3di.utils.ApiInvoker;
import com.sfu.an3di.utils.CommonUtils;

@RestController
public class ViewController {

	@Autowired
	private ApplicationConfig appConfig;
	
	@Autowired
	private ApiInvoker apiInvoker;
	
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
    
    @GetMapping("/about")
    public ModelAndView about(Model m) {
    	logger.info("enter index");
    	m.addAttribute("appName", appConfig.getAppName());
    	
        return new ModelAndView("about");
    }
    
    @GetMapping("/stock")
    public ModelAndView stock(Model m, @RequestParam(value="stockId", defaultValue="") String stockId) {
    	logger.info("enter stock");
    	
    	if (!"".equals(stockId) && stockId!=null) {
    		
    		//防止api call过多
    		if ("demo".equals(appConfig.getUnibitApiKey())) {
    			stockId = "AAPL";
    		}
    		
    		Map<String, String> stockValue = apiInvoker.getStockValue(stockId);
			Map<String, Object> companyProfile = apiInvoker.getStockCompanyProfile(stockId);
			
			m.addAttribute("appName", appConfig.getAppName());
			m.addAttribute("unibitApiKey", appConfig.getUnibitApiKey());
			m.addAttribute("stockId", stockId);
			m.addAttribute("companyName", companyProfile.get("company_name"));
			m.addAttribute("stockValue", Double.valueOf(stockValue.get("value")));
			m.addAttribute("stockChange", Double.valueOf(stockValue.get("change")));
			m.addAttribute("stockNet", Double.valueOf(stockValue.get("net"))*100);
			
    		return new ModelAndView("stock_detail");
    		
    	} else {
    		
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		String today = df.format(new Date());
    		for (Stock stock : CommonUtils.selectedStockList) {
    			if (today.equals(stock.getLastUpdateDate())) {
    				continue;
    			}
    			
    			stockId = stock.getId();
    			//防止api call过多
        		if ("demo".equals(appConfig.getUnibitApiKey())) {
        			stockId = "AAPL";
        		}
        		
    			Map<String, String> stockValue = apiInvoker.getStockValue(stockId);
    			stock.setLastUpdateDate(today);
    			stock.setValue(Double.valueOf(stockValue.get("value")));
    			stock.setChange(Double.valueOf(stockValue.get("change")));
    			stock.setNet(Double.valueOf(stockValue.get("net"))*100);
    			try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		
    		m.addAttribute("appName", appConfig.getAppName());
    		m.addAttribute("quandlApiKey", appConfig.getQuandlApiKey());
	    	m.addAttribute("selectedStockList", CommonUtils.selectedStockList);
	    	
	        return new ModelAndView("stock_list");
    	}
    }
}
