package com.sfu.an3di.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.sfu.an3di.pojo.Stock;

public class CommonUtils {
	
	public static List<Stock> selectedStockList = new ArrayList<>();
	
	public static Map<String, String> companyNameMap = new HashMap<>();
	
	static {
		selectedStockList.add(new Stock("AAPL", "Apple Inc. Common Stock (AAPL)"));
		selectedStockList.add(new Stock("AMZN", "Amazon.com, Inc. Common Stock (AMZN)"));
		selectedStockList.add(new Stock("BABA", "Alibaba Group Holding Limited (BABA)"));
		selectedStockList.add(new Stock("CSCO", "Cisco Systems, Inc. Common Stock Common Stock (CSCO)"));
		selectedStockList.add(new Stock("EBAY", " eBay Inc. Common Stock (EBAY)"));
		selectedStockList.add(new Stock("FB", "Facebook, Inc. Class A Common Stock Common Stock (FB)"));
		selectedStockList.add(new Stock("GOOGL", "Alphabet Inc. Class A Common Stock (GOOGL)"));
		selectedStockList.add(new Stock("MSFT", "Microsoft Corporation Common Stock Common Stock (MSFT)"));
		selectedStockList.add(new Stock("NFLX", "Netflix, Inc. Common Stock Common Stock (NFLX)"));
		selectedStockList.add(new Stock("TSLA", "Tesla, Inc. Common Stock Common Stock (TSLA)"));
		
		try {
			ClassPathResource resource = new ClassPathResource("static/mockdata/stock-symbol-full.json");
			StringWriter writer = new StringWriter();
			IOUtils.copy(resource.getInputStream(), writer, "UTF-8");
			String content = writer.toString();
			//File file = ResourceUtils.getFile("classpath:static/mockdata/stock-symbol-full.json");
			//String content = new String(Files.readAllBytes(file.toPath()));
			Map<String, Object> ret = convertJsonStrToMap(content);
			List<Map<String, Object>> list = (List<Map<String, Object>>) ret.get("data");
			for (Map<String, Object> map : list) {
				companyNameMap.put(map.get("stock_id").toString(), map.get("company_name").toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Logger getLogger() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String callersClassName = stackTrace[2].getClassName();
        return LoggerFactory.getLogger(callersClassName);
	}
	
	public static HashMap<String, Object> convertJsonStrToMap(String jsonStr){
		HashMap<String, Object> ret;
		
		try {
			ret = new Gson().fromJson(new JsonParser().parse(jsonStr), HashMap.class);
		}catch(Exception e) {
			ret = new HashMap<>();
		}
		return ret;
	}
}
