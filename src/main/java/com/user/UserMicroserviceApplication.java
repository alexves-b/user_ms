package com.user;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableAutoConfiguration
@EnableEurekaClient
@EnableScheduling
public class UserMicroserviceApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserMicroserviceApplication.class, args);
	}

}
