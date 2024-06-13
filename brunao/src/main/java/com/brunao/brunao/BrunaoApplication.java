package com.brunao.brunao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
public class BrunaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrunaoApplication.class, args);
	}

}
