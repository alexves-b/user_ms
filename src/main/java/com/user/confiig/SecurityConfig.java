package com.user.confiig;

import com.user.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

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
				.headers().frameOptions().disable()
				.and().csrf().disable()
				.cors().configurationSource(corsConfigurationSource())
				.and().authorizeRequests()
				.antMatchers("/api/v1/account/**").permitAll()
				.antMatchers("/api/v1/account/me/**").permitAll()
				.antMatchers("/me/**").permitAll()
				.antMatchers("/account/me/**").permitAll()
				.anyRequest().permitAll()
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().exceptionHandling()
				.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
		return http.build();
	}
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		log.info(" > Configuring CORS policy");
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://5.63.154.191:8098"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH", "HEAD", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("Origin", "Accept", "X-Requested-With", "Content-Type", "Access-Control-Request-Method", "Access-Control-Request-Headers", "Authorization"));
		configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
//		source.registerCorsConfiguration("/**", configuration);
		source.registerCorsConfiguration("/api/v1/account/me/*", configuration);
		source.registerCorsConfiguration("/api/v1/account/*", configuration);
		source.registerCorsConfiguration("/api/v1/*/account/*", configuration);
		return source;
	}


	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public AuthenticationManager authenticationManager(@NotNull @org.jetbrains.annotations.NotNull AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
