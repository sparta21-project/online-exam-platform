package com.example.onlineexamplatform.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.onlineexamplatform.config.resolver.UserSessionArgumentResolver;
import com.example.onlineexamplatform.config.session.AuthInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

	private final UserSessionArgumentResolver userSessionArgumentResolver;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AuthInterceptor())
			.addPathPatterns("/api/**") // 권한 검사할 URL
			.excludePathPatterns("/api/auth/**"); // 권한 검사 제외할 URL
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(userSessionArgumentResolver);
	}
}
