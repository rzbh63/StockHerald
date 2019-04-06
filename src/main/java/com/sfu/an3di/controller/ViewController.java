package com.sfu.an3di.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.conf.ConfigurationBuilder;

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
    		
    		Map<String, String> stockValue = apiInvoker.getStockValueFromAlphaVantage(stockId);
			Map<String, Object> companyProfile = apiInvoker.getStockCompanyProfile(stockId);
			List<String>[] tweetUrlsArr = apiInvoker.getSearchedTweetUrls((String) companyProfile.get("company_name"));
			List<String> allTweetUrls = tweetUrlsArr[0];
			List<String> allSortedTweetUrls = tweetUrlsArr[1];
			
			List<String> tweetUrls = new ArrayList<>();
			for (int i=0; i<5 && i<allSortedTweetUrls.size(); i++) {
				tweetUrls.add(allSortedTweetUrls.get(i));
			}
			m.addAttribute("appName", appConfig.getAppName());
			m.addAttribute("unibitApiKey", appConfig.getUnibitApiKey());
			m.addAttribute("stockId", stockId);
			m.addAttribute("tweetsNum", allTweetUrls.size());
			m.addAttribute("tweetUrls", tweetUrls);
			m.addAttribute("allTweetUrls", allTweetUrls);
			m.addAttribute("companyName", companyProfile.get("company_name"));
			m.addAttribute("stockValue", Double.valueOf(stockValue.get("value")));
			m.addAttribute("stockChange", Double.valueOf(stockValue.get("change")));
			m.addAttribute("stockNet", stockValue.get("net"));
			//m.addAttribute("stockNet", Double.valueOf(stockValue.get("net"))*100);
			m.addAttribute("stockHistoryData", apiInvoker.getHistoryStockDataFromAlphaVantage(stockId));
			
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
        		
    			Map<String, String> stockValue = apiInvoker.getStockValueByHistoryFromUnibit(stockId);
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
