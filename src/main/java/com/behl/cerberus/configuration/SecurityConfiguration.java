package com.behl.cerberus.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.behl.cerberus.security.constant.ApiPathExclusion;
import com.behl.cerberus.security.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().exceptionHandling().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeHttpRequests(auth -> {
					auth.requestMatchers(HttpMethod.GET,
							List.of(ApiPathExclusion.GetApiPathExclusion.values()).stream()
									.map(apiPath -> apiPath.getPath()).toArray(String[]::new))
							.permitAll()
							.requestMatchers(HttpMethod.POST,
									List.of(ApiPathExclusion.PostApiPathExclusion.values()).stream()
											.map(apiPath -> apiPath.getPath()).toArray(String[]::new))
							.permitAll()
							.requestMatchers(HttpMethod.PUT,
									List.of(ApiPathExclusion.PutApiPathExclusion.values()).stream()
											.map(apiPath -> apiPath.getPath()).toArray(String[]::new))
							.permitAll().anyRequest().authenticated();
				}).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}