package com.example.onlineexamplatform.config.session;

import java.io.IOException;
import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SessionFilter extends OncePerRequestFilter {
	private static final String SESSION_COOKIE_NAME = "SESSION";
	private static final Duration TTL = Duration.ofHours(24);
	// Redis 에서 UserSession에서 객체를 조회하기 위한 RedisTemplate
	private final RedisTemplate<String, SessionUser> redisTemplate;

	// RedisTemplate 주입
	public SessionFilter(RedisTemplate<String, SessionUser> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		// 클라이언트가 보낸 쿠키를 가져온다 (Session)
		Cookie cookie = WebUtils.getCookie(request, SESSION_COOKIE_NAME);
		if (cookie != null) {
			// Redis에 저장된 세션 키를 조합해서 조회
			String redisKey = SESSION_COOKIE_NAME + ":" + cookie.getValue();
			SessionUser session = redisTemplate.opsForValue().get(redisKey);
			if (session != null) {
				// 세션 정보가 유효하면 request attribute에 담아두기 (컨트롤러에서 꺼내 쓸수도 있음)
				request.setAttribute("userSession", session);
				request.getSession().setAttribute("userSession", session);
				redisTemplate.expire(redisKey, TTL); // TTL 갱신
				// System.out.println(session); 세션이 찍히는지 확인하는 콘솔로그
			} else {
				// 세션 만료되면 쿠키도 삭제
				Cookie expired = new Cookie(SESSION_COOKIE_NAME, null);
				expired.setPath("/");
				expired.setMaxAge(0);
				response.addCookie(expired);
			}
		}
		filterChain.doFilter(request, response);
	}
}
