package com.example.onlineexamplatform.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.example.onlineexamplatform.config.session.SessionFilter;
import com.example.onlineexamplatform.config.session.SessionUser;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<SessionFilter> sessionFilter(
		RedisTemplate<String, SessionUser> redisTemplate
	) {
		FilterRegistrationBean<SessionFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new SessionFilter(redisTemplate));
		registration.addUrlPatterns("/api/*");  // 인증이 필요한 경로
		registration.setOrder(0);
		return registration;
	}

}
