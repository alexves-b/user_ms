package com.user;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	@Bean
	public SecurityFilterChain filterChain(@NonNull HttpSecurity http) throws Exception {
		http
				.headers().frameOptions().disable()
				.and()
				.csrf().disable()
				.cors().disable()
				.authorizeRequests()
			.requestMatchers(toH2Console()).permitAll()
				.antMatchers("/api/v1/account/**").permitAll()
				.antMatchers("/h2-console/**").permitAll();
		return http.build();
	}
}