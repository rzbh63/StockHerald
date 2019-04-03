package com.sfu.an3di.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;

public class ApiInvoker {
	
	private static ApiInvoker instance = null;
	
	private final String USER_AGENT = "Mozilla/5.0";
	private Logger logger = CommonUtils.getLogger();
	
	private ApiInvoker() {
		// TODO Auto-generated constructor stub
	}
	
	public static ApiInvoker getInstance() {
		if (instance == null) {
			instance = new ApiInvoker();
		}
		return instance;
	}
	
	public Map<String, String> getStockValue(String stockId) {
		
		String url = "https://api.unibit.ai/historicalstockprice/AAPL?range=1m&interval=1&AccessKey=demo";
		
		String ret = "";
		try {
			ret = this.sendGet(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Map<String, String> stockValue = new HashMap<>();
		
		try {
			HashMap<String, Object> retMap = CommonUtils.convertJsonStrToMap(ret);
			List<Map<String, Object>> list = (List) retMap.get("Stock price");
			Map<String, Object> day1 = list.get(0);
			Map<String, Object> day2 = list.get(0);
			
			String date = (String) day1.get("date");
			Double value1 = (Double) day1.get("open");
			Double value2 = (Double) day2.get("close");
			Double change = value1 - value2;
			Double net = change / value2;
			
			stockValue.put("date", date);
			stockValue.put("value", value1.toString());
			stockValue.put("change", change.toString());
			stockValue.put("net", net.toString());
		}catch(Exception e) {
			stockValue.put("date", "");
			stockValue.put("value", "0");
			stockValue.put("change", "0");
			stockValue.put("net", "0");
		}
		return stockValue;
	}
	
	// HTTP GET request
	public String sendGet(String url) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");
		con.setReadTimeout(5000);
		con.setConnectTimeout(5000);
		
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		logger.info("\nSending 'GET' request to URL : " + url);
		logger.info("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		String ret = response.toString();
		logger.info(ret);
		return ret;
	}
	
	// HTTP POST request
	public void sendPost() throws Exception {

		String url = "https://selfsolve.apple.com/wcResults.do";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//print result
		System.out.println(response.toString());

	}

}
