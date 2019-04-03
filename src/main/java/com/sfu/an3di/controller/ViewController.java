package com.sfu.an3di.controller;

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
    public ModelAndView stock(Model m, @RequestParam(value="stockId", defaultValue="") String stockId) {
    	logger.info("enter stock");
//    	List<Stock> selectedStockList = new ArrayList<>();
//    	selectedStockList.add(new Stock("apple", "Apple Inc. Common Stock (AAPL)", "AAPL"));
//    	selectedStockList.add(new Stock("amazon", "Amazon.com, Inc. Common Stock (AMZN)", "AMZN"));
//    	selectedStockList.add(new Stock("alibaba", "Alibaba Group Holding Limited (BABA)", "BABA"));
//    	selectedStockList.add(new Stock("cisco", "Cisco Systems, Inc. Common Stock Common Stock (CSCO)", "CSCO"));
//    	selectedStockList.add(new Stock("ebay", " eBay Inc. Common Stock (EBAY)", "EBAY"));
//    	selectedStockList.add(new Stock("facebook", "Facebook, Inc. Class A Common Stock Common Stock (FB)", "FB"));
//    	selectedStockList.add(new Stock("google", "Alphabet Inc. Class A Common Stock (GOOGL)", "GOOGL"));
//    	selectedStockList.add(new Stock("microsoft", "Microsoft Corporation Common Stock Common Stock (MSFT)", "MSFT"));
//    	selectedStockList.add(new Stock("netflix", "Netflix, Inc. Common Stock Common Stock (NFLX)", "NFLX"));
//    	selectedStockList.add(new Stock("tesla", "Tesla, Inc. Common Stock Common Stock (TSLA)", "TSLA"));
    	
    	if (!"".equals(stockId) && stockId!=null) {
    		
    		
    		Map<String, String> stockValue = ApiInvoker.getInstance().getStockValue(stockId);
			
			m.addAttribute("appName", appConfig.getAppName());
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
    			
    			Map<String, String> stockValue = ApiInvoker.getInstance().getStockValue(stock.getId());
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
	    	m.addAttribute("selectedStockList", CommonUtils.selectedStockList);
	    	
	        return new ModelAndView("stock_list");
    	}
    }
}
