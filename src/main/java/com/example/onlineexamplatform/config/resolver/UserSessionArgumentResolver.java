package com.example.onlineexamplatform.config.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.onlineexamplatform.config.session.UserSession;
import com.example.onlineexamplatform.config.session.UserSessionArgument;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class UserSessionArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(UserSessionArgument.class)
			&& parameter.getParameterType().equals(UserSession.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		return request.getAttribute("userSession");
	}
}
