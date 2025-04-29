package com.kiron.amtrakTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AmtrakTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmtrakTrackerApplication.class, args);
	}

}
