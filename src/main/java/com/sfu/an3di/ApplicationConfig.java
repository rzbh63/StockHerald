package com.sfu.an3di;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfig {
	
	@Value("${app.name:test}")
    private String appName;
	
	public String getAppName() {
    	return this.appName;
    }
}
