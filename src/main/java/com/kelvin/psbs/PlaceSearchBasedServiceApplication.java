package com.kelvin.psbs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PlaceSearchBasedServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaceSearchBasedServiceApplication.class, args);
	}

}
