package com.sfu.an3di;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfig {
	
	@Value("${app.name:test}")
    private String appName;
	
	@Value("${unibit.apikey:demo}")
    private String unibitApiKey;
	
	@Value("${quandl.apikey:demo}")
    private String quandlApiKey;
	
	public String getAppName() {
    	return this.appName;
    }
	
	public String getUnibitApiKey() {
		return this.unibitApiKey;
	}
	
	public String getQuandlApiKey() {
		return this.quandlApiKey;
	}
}
