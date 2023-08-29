package com.user.confiig;

import com.user.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import javax.validation.constraints.NotNull;

@Slf4j
@Configuration
@EnableWebSecurity
@PropertySource("secrets.properties")
@AllArgsConstructor
public class SecurityConfig {
	private final UserServiceImpl userService;

	@Bean
	public SecurityFilterChain filterChain(@NotNull @org.jetbrains.annotations.NotNull HttpSecurity http) throws Exception {
		log.warn(" > I am in 'filterChain'");
		http
				.headers()
		.and().authorizeRequests()
				.antMatchers("/api/v1/**").permitAll()
				.antMatchers("/api/v1/account/me").permitAll()
				.anyRequest().permitAll()
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().exceptionHandling()
				.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
		return http.build();

	}

}
