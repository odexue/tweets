package com.odexue.tweets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
public class TweetsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweetsApplication.class, args);
	}

}
