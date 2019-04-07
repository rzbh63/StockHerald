package com.sfu.an3di.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sfu.an3di.ApplicationConfig;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

@Component
public class ApiInvoker {
	
	//private static ApiInvoker instance = null;
	
	@Autowired
	private ApplicationConfig appConfig;
	
	private final String USER_AGENT = "Mozilla/5.0";
	private Logger logger = CommonUtils.getLogger();
	
	private ApiInvoker() {
		// TODO Auto-generated constructor stub
	}
	
	public Map<String, String> getStockValueFromAlphaVantage(String stockId) {
		
		String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + stockId + "&apikey=";
		
		String ret = "";
		try {
			boolean allKeyFails = true;
			for (String apikey : appConfig.getAlphaVantageApiKeySet()) {
				ret = this.sendGet(url + apikey);
				HashMap<String, Object> retMap = CommonUtils.convertJsonStrToMap(ret);
				if (retMap.get("Global Quote") == null) {
					continue;
				} else {
					allKeyFails = false;
					break;
				}
			}
			
			if (allKeyFails) {
				url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=MSFT&apikey=demo";
				ret = this.sendGet(url);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Map<String, String> stockValue = new HashMap<>();
		
		try {
			HashMap<String, Object> retMap = CommonUtils.convertJsonStrToMap(ret);
			Map<String, Object> quote = (Map) retMap.get("Global Quote");
			
			stockValue.put("date", quote.get("07. latest trading day").toString());
			stockValue.put("value", quote.get("05. price").toString());
			stockValue.put("change", quote.get("09. change").toString());
			stockValue.put("net", quote.get("10. change percent").toString());
		}catch(Exception e) {
			stockValue.put("date", "");
			stockValue.put("value", "0");
			stockValue.put("change", "0");
			stockValue.put("net", "0");
		}
		
		return stockValue;
	}
	
	public Map<String, Object> getHistoryStockDataFromAlphaVantage(String stockId) {
		String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + stockId + "&outputsize=full&apikey=";
		String ret = "";
		try {
			boolean allKeyFails = true;
			for (String apikey : appConfig.getAlphaVantageApiKeySet()) {
				ret = this.sendGet(url + apikey);
				HashMap<String, Object> retMap = CommonUtils.convertJsonStrToMap(ret);
				if (retMap.get("Time Series (Daily)") == null) {
					continue;
				} else {
					allKeyFails = false;
					break;
				}
			}
			
			if (allKeyFails) {
				url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=MSFT&apikey=demo";
				ret = this.sendGet(url);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HashMap<String, Object> retMap = new HashMap<>();
		try {
			retMap = CommonUtils.convertJsonStrToMap(ret);
		}catch(Exception e) {}
		
		return retMap;
	}
	
	public Map<String, String> getStockValueByHistoryFromUnibit(String stockId) {
		
		String ret = "";
		int idx=0;
		boolean allKeyFails = true;
		for (String apikey : appConfig.getUnibitApiKeySet()) {
			String url = "https://api.unibit.ai/historicalstockprice/" + stockId + "?range=1m&interval=1&AccessKey=" + apikey;
			try {
				ret = this.sendGet(url);
				appConfig.setAvailableUnibitApiKeyIndex(idx);
				allKeyFails = false;
				break;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			idx++;
		}
		
		if (allKeyFails) {
			String url = "https://api.unibit.ai/historicalstockprice/AAPL?range=1m&interval=1&AccessKey=demo";
			appConfig.setAvailableUnibitApiKeyIndex(-1);
			try {
				ret = this.sendGet(url);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	public Map<String, Object> getStockCompanyProfile(String stockId) {
		
		String ret = "";
		int idx=0;
		boolean allKeyFails = true;
		for (String apikey : appConfig.getUnibitApiKeySet()) {
			String url = "https://api.unibit.ai/companyprofile/" + stockId + "?AccessKey=" + apikey;
			try {
				ret = this.sendGet(url);
				appConfig.setAvailableUnibitApiKeyIndex(idx);
				allKeyFails = false;
				break;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			idx++;
		}
		
		if (allKeyFails) {
			String url = "https://api.unibit.ai/companyprofile/AAPL?AccessKey=demo";
			appConfig.setAvailableUnibitApiKeyIndex(-1);
			try {
				ret = this.sendGet(url);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Map<String, Object> retMap = new HashMap<>();
		
		try {
			retMap = CommonUtils.convertJsonStrToMap(ret);
		}catch(Exception e) {
		}
		
		return retMap;
	}

	public List<String>[] getSearchedTweetUrls(String queryStr){
		Twitter twitter = new TwitterFactory(appConfig.getTwitterConfig()).getInstance();
		List<String> tweetUrls = new ArrayList<>();
		List<String> sortedTweetUrls = new ArrayList<>();
		List<String>[] ret = new List[2];
		
        try {
            Query query = new Query(queryStr);
        	query.setLang("en");
        	query.setCount(50);
        	query.setResultType(Query.MIXED);
            QueryResult result;
            //do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                List<Status> sortedTweets = new ArrayList<>(tweets);
                //Collections.copy(sortedTweets, tweets);
                
                Comparator<Status> comp = new Comparator<Status>(){
                    @Override
                    public int compare(Status s1, Status s2)
                    {
                        return s1.getText().length() - s2.getText().length();
                    }        
                };
                sortedTweets.sort(comp);
                
                for (Status tweet : tweets) {
                	StringBuilder sb = new StringBuilder();
                	sb.append("https://twitter.com/");
                	sb.append(tweet.getUser().getId());
                	sb.append("/status/");
                	sb.append(tweet.getId());
                	sb.append("?ref_src=");
                	sb.append(appConfig.getTwitterRefSource());
                	tweetUrls.add(sb.toString());
                }
                for (Status tweet : sortedTweets) {
                	StringBuilder sb = new StringBuilder();
                	sb.append("https://twitter.com/");
                	sb.append(tweet.getUser().getId());
                	sb.append("/status/");
                	sb.append(tweet.getId());
                	sb.append("?ref_src=");
                	sb.append(appConfig.getTwitterRefSource());
                	sortedTweetUrls.add(sb.toString());
                }
                ret[0] = tweetUrls;
                ret[1] = sortedTweetUrls;
            //} while ((query = result.nextQuery()) != null);
        } catch (TwitterException te) {
            te.printStackTrace();
            logger.error("Failed to search tweets: " + te.getMessage());
        }
        
        return ret;
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
