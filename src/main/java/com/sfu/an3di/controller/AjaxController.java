package com.sfu.an3di.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sfu.an3di.ApplicationConfig;
import com.sfu.an3di.utils.ApiInvoker;
import com.sfu.an3di.utils.CommonUtils;

@RestController
public class AjaxController {
	
	@Autowired
	private ApplicationConfig appConfig;
	
	@Autowired
	private ApiInvoker apiInvoker;
	
	private Logger logger = CommonUtils.getLogger();
	
	@RequestMapping("/predict")
    public Map<String, Object> predict(Model m, 
    							@RequestParam(value = "stockId", defaultValue = "") String stockId,
    							@RequestParam(value = "companyName", defaultValue = "") String companyName,
                                @RequestParam(value = "predictDate",defaultValue = "") String predictDate) throws Exception {
		
        Map<String, Object> retData = new HashMap<>();
        
        try {
        	
        	// compute last 15 days price list
        	Map<String, Object> stockHistoryData = (Map<String, Object>) apiInvoker.getHistoryStockDataFromAlphaVantage(stockId, "compact").get("Time Series (Daily)");
            
            int cnt = 0;
            StringBuilder priceListStr = new StringBuilder();
            List<String> priceList = new ArrayList<>();
            
            for (String key : stockHistoryData.keySet()) {
            	Map<String, Object> price = (Map<String, Object>) stockHistoryData.get(key);
            	try {
            		priceList.add(price.get("4. close").toString());
            	} catch(Exception e) {
            		continue;
            	}
            	cnt++;
            	if (cnt == 15) {
            		break;
            	}
            }
            for (int i=priceList.size()-1; i>=0; i--) {
            	priceListStr.append(priceList.get(i));
            	if (i > 0) {
            		priceListStr.append("|");
            	}
            }
            logger.info(priceListStr.toString());
            
            // compute last 15 days news title list
            
            
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            cal.setTime(new Date());
            cnt = 0;
            List<String> newsTitleList = new ArrayList<>();
            StringBuilder newsTitleListStr = new StringBuilder();
            for (int i=0; i<30; i++) {
            	if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            		cal.add(Calendar.DATE, -1);
            		continue;
            	}
            	
            	int timespan = 3;
            	String[] datespan = new String[timespan];
            	for (int j=0; j<timespan; j++) {
            		datespan[j] = sdf.format(cal.getTime());
            		cal.add(Calendar.DATE, -1);
            	}
            	
            	List<Map<String, Object>> articleList = (List<Map<String, Object>>) apiInvoker.getRecentNews(stockId, companyName, datespan[timespan-1], datespan[0]).get("articles");
    			Map<String, List<String>> dailyNewsTitles = new HashMap<>();
    			
                for (Map<String, Object> article : articleList) {
                	try {
                		String date = article.get("publishedAt").toString().substring(0, 10);
                		List<String> tmp = dailyNewsTitles.getOrDefault(date, new ArrayList<>());
                		tmp.add(article.get("title").toString());
                		dailyNewsTitles.put(date, tmp);
                	}catch(Exception e) {
                		continue;
                	}
    			}
                
                for (int j=0; j<timespan; j++) {
	            	List<String> newsTitles = dailyNewsTitles.get(datespan[j]);
					if (newsTitles != null){
						StringBuilder sb = new StringBuilder();
						int max = newsTitles.size() < 10 ? newsTitles.size() : 10;
						for (int k=0; k<max; k++) {
							sb.append(newsTitles.get(k));
							if (k<max-1) {
								sb.append("$@$");
							}
						}
						newsTitleList.add(sb.toString());
					} else {
						newsTitleList.add("");
					}
                }
				
				cnt += timespan;
				if (cnt >= 15) {
					break;
				}
            }
            
            for (int i=newsTitleList.size()-1; i>=0; i--) {
            	newsTitleListStr.append(newsTitleList.get(i));
            	if (i>0) {
            		newsTitleListStr.append("|||");
            	}
            }
            logger.info(newsTitleListStr.toString());

            
    		String s = "";
    		
			//Process p = Runtime.getRuntime().exec("python3 test.py + ");
    		
    		long noOfDaysBetween = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(predictDate));
    		
			ProcessBuilder pb = new ProcessBuilder("python3", appConfig.getModelScriptPath(), priceListStr.toString(), newsTitleListStr.toString(), Long.toString(noOfDaysBetween));
			Process p = pb.start();
			
			BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));

            // read the output from the command
            logger.info("Here is the standard output of the command:\n");
            String pre = "";
            while ((s = stdInput.readLine()) != null) {
            	if ("prediction finished".equals(pre)) {
            		retData.put("data", s);
            	}
            	logger.info(s);
            	pre = s;
            }
            
            // read any errors from the attempted command
            logger.info("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
            	logger.info(s);
            }

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return retData;
    }
}
