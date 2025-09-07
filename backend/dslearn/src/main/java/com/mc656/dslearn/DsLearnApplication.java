package com.mc656.dslearn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class DsLearnApplication {

	public static void main(String[] args) {
		SpringApplication.run(DsLearnApplication.class, args);
	}

}
