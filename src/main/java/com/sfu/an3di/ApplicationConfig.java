package com.sfu.an3di;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import twitter4j.conf.ConfigurationBuilder;

@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfig {
	
	@Value("${app.name:test}")
    private String appName;
	
	@Value("${model.script.path}")
	private String modelScriptPath;
	
	@Value("${unibit.apikey}")
	private String[] unibitApiKeySet;
	
	@Value("${alphavantage.apikey}")
	private String[] alphaApikeySet;
	
	private int availableUnibitApiKeyIndex = 0;
	
	@Value("${quandl.apikey:demo}")
    private String quandlApiKey;
	
	@Value("${newsapi.apikey:demo}")
	private String newsApiKey;
	
	@Value("${oauth.consumerKey:demo}")
    private String twitterConsumerKey;
	
	@Value("${oauth.consumerSecret:demo}")
    private String twitterConsumerSecret;
	
	@Value("${oauth.accessToken:demo}")
    private String twitterAccessToken;
	
	@Value("${oauth.accessTokenSecret:demo}")
    private String twitterAccessTokenSecret;
	
	@Value("${ref_src}")
	private String twitterRefSource;
	
	private twitter4j.conf.Configuration twitterConfig;
	
	public String getAppName() {
    	return this.appName;
    }
	
	public String getModelScriptPath() {
		return this.modelScriptPath;
	}
	
	public String[] getUnibitApiKeySet() {
		return this.unibitApiKeySet;
	}
	
	public void setAvailableUnibitApiKeyIndex(int index) {
		this.availableUnibitApiKeyIndex = index;
	}
	
	public String getUnibitApiKey() {
		if (availableUnibitApiKeyIndex == -1) {
			return "demo";
		}else {
			return this.unibitApiKeySet[availableUnibitApiKeyIndex];
		}
	}
	
	public String getQuandlApiKey() {
		return this.quandlApiKey;
	}
	
	public String getNewsApiKey() {
		return this.newsApiKey;
	}
	
	public String[] getAlphaVantageApiKeySet() {
		return this.alphaApikeySet;
	}
	
	public String getTwitterRefSource() {
		return this.twitterRefSource;
	}
	
	public twitter4j.conf.Configuration getTwitterConfig(){
		if (this.twitterConfig == null) {
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setOAuthConsumerKey(this.twitterConsumerKey);
			cb.setOAuthConsumerSecret(this.twitterConsumerSecret);
			cb.setOAuthAccessToken(this.twitterAccessToken);
			cb.setOAuthAccessTokenSecret(this.twitterAccessTokenSecret);
			
			this.twitterConfig = cb.build();
		}
		
		return this.twitterConfig;
	}
}
