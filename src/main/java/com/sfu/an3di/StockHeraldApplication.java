package com.sfu.an3di;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class StockHeraldApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockHeraldApplication.class, args);
	}

}
