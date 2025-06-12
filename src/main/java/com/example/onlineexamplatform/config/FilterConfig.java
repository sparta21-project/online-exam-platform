package com.example.onlineexamplatform.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.example.onlineexamplatform.config.session.SessionFilter;
import com.example.onlineexamplatform.config.session.UserSession;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<SessionFilter> sessionFilter(
		RedisTemplate<String, UserSession> redisTemplate
	) {
		FilterRegistrationBean<SessionFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new SessionFilter(redisTemplate));
		registration.addUrlPatterns("/api/*");  // 인증이 필요한 경로
		registration.setOrder(1);
		return registration;
	}

}
