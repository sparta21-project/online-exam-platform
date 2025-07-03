package com.example.onlineexamplatform.domain.user.service;

import java.time.Duration;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.onlineexamplatform.config.session.SessionUser;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {

	private static final String SESSION_COOKIE_NAME = "SESSION";
	private static final Duration SESSION_TTL = Duration.ofMinutes(15);
	private final RedisTemplate<String, SessionUser> redisTemplate;

	public void createRedisSession(SessionUser session, HttpServletResponse response) {

		// Redis 저장
		String sessionId = UUID.randomUUID().toString();
		String redisKey = SESSION_COOKIE_NAME + ":" + sessionId;
		redisTemplate.opsForValue().set(redisKey, session, SESSION_TTL);

		// 쿠키 발급
		Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge((int)SESSION_TTL.getSeconds());
		response.addCookie(cookie);

	}

}
