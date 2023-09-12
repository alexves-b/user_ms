package com.user;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableAutoConfiguration
@EnableEurekaClient
@EnableScheduling
@EnableFeignClients
public class UserMicroserviceApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserMicroserviceApplication.class, args);
	}
}
