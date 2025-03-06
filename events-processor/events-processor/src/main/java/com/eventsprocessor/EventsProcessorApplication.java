package com.eventsprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EventsProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventsProcessorApplication.class, args);
	}

}
