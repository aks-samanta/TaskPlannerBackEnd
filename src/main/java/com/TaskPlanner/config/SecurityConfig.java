package com.TaskPlanner.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableMethodSecurity

public class SecurityConfig {

	private static final String[] AUTH_WHITE_LIST = {};

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.cors().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf()
				.disable().authorizeHttpRequests()
				.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/v2/api-docs/**", "/swagger-resources/**")
				.permitAll().requestMatchers(HttpMethod.POST, "/api/users/register").permitAll().anyRequest()
				.authenticated().and().addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)
				.addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class).httpBasic();

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
		configuration.setExposedHeaders(Arrays.asList("x-auth-token", "authorization"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
