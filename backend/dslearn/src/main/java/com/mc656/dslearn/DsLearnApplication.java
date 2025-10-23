package com.mc656.dslearn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.mc656.dslearn.repositories")
public class DsLearnApplication {

	public static void main(String[] args) {
		SpringApplication.run(DsLearnApplication.class, args);
	}

}
