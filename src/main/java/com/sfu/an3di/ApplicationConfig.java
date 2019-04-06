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
	
	@Value("${unibit.apikey:demo}")
    private String unibitApiKey;
	
	@Value("${quandl.apikey:demo}")
    private String quandlApiKey;
	
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
	
	public String getUnibitApiKey() {
		return this.unibitApiKey;
	}
	
	public String getQuandlApiKey() {
		return this.quandlApiKey;
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
