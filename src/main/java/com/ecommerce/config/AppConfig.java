package com.ecommerce.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class AppConfig {

	@Bean
	 SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.
		sessionManagement(i -> i.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests(i -> i.requestMatchers("/api/**").authenticated().anyRequest().permitAll())
		.addFilterBefore(new JwtValidator(), BasicAuthenticationFilter.class)
		.csrf(csrf -> csrf.disable())
		.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
			
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration cfg = new CorsConfiguration();
				cfg.setAllowedOrigins(Arrays.asList(
						"http://localhost:3000",
						"http://localhost:4200",
						"https://shopspot-psi.vercel.app"
						));
				cfg.setAllowedMethods(Collections.singletonList("*"));
				cfg.setAllowCredentials(true);
				cfg.setAllowedHeaders(Collections.singletonList("*"));
				cfg.setExposedHeaders(Arrays.asList(
						"Authorization"
						));
				cfg.setMaxAge(3600L);
				
				return cfg;
			}
		}))
	.httpBasic().and().formLogin();
		
		
		
		return http.build();
	}
	
	@Bean
	 PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	


}
