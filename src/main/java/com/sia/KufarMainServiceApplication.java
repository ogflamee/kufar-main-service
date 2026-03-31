package com.sia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class KufarMainServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KufarMainServiceApplication.class, args);
	}

}
