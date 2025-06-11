package com.example.onlineexamplatform.config.session;

import java.io.IOException;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SessionFilter extends OncePerRequestFilter {
	// Redis 에서 UserSession에서 객체를 조회하기 위한 RedisTemplate
	private final RedisTemplate<String, UserSession> redis;

	// RedisTemplate 주입
	public SessionFilter(RedisTemplate<String, UserSession> redis) {
		this.redis = redis;
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		// 클라이언트가 보낸 쿠키를 가져온다 (Session)
		Cookie cookie = WebUtils.getCookie(request, "SESSION");
		if (cookie != null) {
			// Redis에 저장된 세션 키를 조합해서 조회
			UserSession session = redis.opsForValue()
				.get("session:" + cookie.getValue());
			// 세션 정보가 유효하면 request attribute에 담아두기 (컨트롤러에서 꺼내 쓸수도 있음)
			if (session != null) {
				request.setAttribute("userSession", session);
			}
		}
		filterChain.doFilter(request, response);
	}
}
