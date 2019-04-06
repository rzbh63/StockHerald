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
	
	@Value("${unibit.apikey1:demo}")
    private String unibitApiKey1;
	
	@Value("${unibit.apikey2:demo}")
    private String unibitApiKey2;
	
	@Value("${unibit.apikey3:demo}")
    private String unibitApiKey3;
	
	@Value("${unibit.apikey1:demo}")
	private String availableUnibitApiKey;
	
	@Value("${quandl.apikey:demo}")
    private String quandlApiKey;
	
	@Value("${alphavantage.apikey1:demo}")
    private String alphaApikey1;
	
	@Value("${alphavantage.apikey2:demo}")
    private String alphaApikey2;
	
	@Value("${alphavantage.apikey3:demo}")
    private String alphaApikey3;
	
	@Value("${alphavantage.apikey4:demo}")
    private String alphaApikey4;
	
	@Value("${alphavantage.apikey5:demo}")
    private String alphaApikey5;

	@Value("${alphavantage.apikey6:demo}")
    private String alphaApikey6;
	
	private String availableAlphaApiKey;
	
	@Value("${oauth.consumerKey:demo}")
    private String twitterConsumerKey;
	
	@Value("${oauth.consumerSecret:demo}")
    private String twitterConsumerSecret;
	
	@Value("${oauth.accessToken:demo}")
    private String twitterAccessToken;
	
	@Value("${oauth.accessTokenSecret:demo}")
    private String twitterAccessTokenSecret;

	private twitter4j.conf.Configuration twitterConfig;
	
	public String getAppName() {
    	return this.appName;
    }
	
	public void changeUnibitApiKey() {
		if (this.availableUnibitApiKey == this.unibitApiKey1) {
			this.availableUnibitApiKey = this.unibitApiKey2;
		} else if (this.availableUnibitApiKey == this.unibitApiKey2) {
			this.availableUnibitApiKey = this.unibitApiKey3;
		} else {
			this.availableUnibitApiKey = "demo";
		}
	}
	
	public String getUnibitApiKey() {
		return this.availableUnibitApiKey;
	}
	
	public String getQuandlApiKey() {
		return this.quandlApiKey;
	}
	
	public String[] getAlphaVantageApiKeys() {
		return new String[]{this.alphaApikey1, this.alphaApikey2, this.alphaApikey3, this.alphaApikey4, this.alphaApikey5, this.alphaApikey6};
	}
	
	public void setAlphaVantageApiKey(String key) {
		this.availableAlphaApiKey = key;
	}

	public String getAlphaVantageApiKey() {
		return this.alphaApikey6;
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
