package com.sfu.an3di.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    	logger.info("enter about");
    	m.addAttribute("appName", appConfig.getAppName());
    	m.addAttribute("videoUrl", appConfig.getAboutVideoUrl());
        return new ModelAndView("about");
    }
    
    @GetMapping("/lda")
    public ModelAndView lda(Model m) {
    	logger.info("enter lda");
    	m.addAttribute("appName", appConfig.getAppName());
    	
        return new ModelAndView("lda");
    }
    
    @GetMapping("/stock")
    public ModelAndView stock(Model m, @RequestParam(value="stockId", defaultValue="") String stockId) {
    	logger.info("enter stock");
    	
    	if (!"".equals(stockId) && stockId!=null) {
    		
    		Map<String, String> stockValue = apiInvoker.getStockValueFromAlphaVantage(stockId);
			//Map<String, Object> companyProfile = apiInvoker.getStockCompanyProfile(stockId);
			List<String>[] tweetUrlsArr = apiInvoker.getSearchedTweetUrls(CommonUtils.companyNameMap.get(stockId));
			List<String> allTweetUrls = tweetUrlsArr[0];
			List<String> allSortedTweetUrls = tweetUrlsArr[1];
			
			List<String> tweetUrls = new ArrayList<>();
			for (int i=0; i<5 && i<allSortedTweetUrls.size(); i++) {
				tweetUrls.add(allSortedTweetUrls.get(i));
			}
			m.addAttribute("appName", appConfig.getAppName());
			m.addAttribute("unibitApiKey", appConfig.getUnibitApiKey());
			m.addAttribute("newsApiKey", appConfig.getNewsApiKey());
			m.addAttribute("stockId", stockId);
			m.addAttribute("tweetsNum", allTweetUrls.size());
			m.addAttribute("tweetUrls", tweetUrls);
			m.addAttribute("allTweetUrls", allTweetUrls);
			m.addAttribute("companyName", CommonUtils.companyNameMap.get(stockId));
			m.addAttribute("stockValue", Double.valueOf(stockValue.get("value")));
			m.addAttribute("stockChange", Double.valueOf(stockValue.get("change")));
			m.addAttribute("stockNet", stockValue.get("net"));
			//m.addAttribute("stockNet", Double.valueOf(stockValue.get("net"))*100);
			m.addAttribute("stockHistoryData", apiInvoker.getHistoryStockDataFromAlphaVantage(stockId, "full"));
			
    		return new ModelAndView("stock_detail");
    	} else {
    		
    		m.addAttribute("appName", appConfig.getAppName());
    		m.addAttribute("quandlApiKey", appConfig.getQuandlApiKey());
	    	m.addAttribute("selectedStockList", CommonUtils.selectedStockList);
	    	
	        return new ModelAndView("stock_list");
    	}
    }
    
    @Scheduled(fixedRate = 1000*3600*8)
    public void updateSelectedStockList() throws InterruptedException {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(String.format("---update selected stock list：%s", dateFormat.format(new Date())));
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String today = df.format(new Date());
		for (Stock stock : CommonUtils.selectedStockList) {
			if (today.equals(stock.getLastUpdateDate())) {
				continue;
			}
			
			String stockId = stock.getId();
			//防止api call过多
    		if ("demo".equals(appConfig.getUnibitApiKey())) {
    			stockId = "AAPL";
    		}
    		
			//Map<String, String> stockValue = apiInvoker.getStockValueByHistoryFromUnibit(stockId);
    		Map<String, String> stockValue = apiInvoker.getStockValueFromAlphaVantage(stockId);
			stock.setLastUpdateDate(today);
			stock.setValue(Double.valueOf(stockValue.get("value")));
			stock.setChange(Double.valueOf(stockValue.get("change")));
			stock.setNet(stockValue.get("net"));
			//stock.setNet(Double.valueOf(stockValue.get("net"))*100);
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
}
