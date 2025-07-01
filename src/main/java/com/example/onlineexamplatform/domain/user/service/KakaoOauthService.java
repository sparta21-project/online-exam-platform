package com.example.onlineexamplatform.domain.user.service;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.onlineexamplatform.config.session.SessionUser;
import com.example.onlineexamplatform.domain.user.dto.AuthLoginResponse;
import com.example.onlineexamplatform.domain.user.dto.KakaoTokenResponse;
import com.example.onlineexamplatform.domain.user.dto.KakaoUserInfoResponse;
import com.example.onlineexamplatform.domain.user.entity.LoginProvider;
import com.example.onlineexamplatform.domain.user.entity.Role;
import com.example.onlineexamplatform.domain.user.entity.User;
import com.example.onlineexamplatform.domain.user.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoOauthService {
	private static final String SESSION_COOKIE_NAME = "SESSION";
	private static final Duration SESSION_TTL = Duration.ofMinutes(15);
	private final WebClient webClient;
	private final UserRepository userRepository;
	private final RedisTemplate<String, SessionUser> redisTemplate;
	@Value("${kakao.client_id}")
	private String clientId;

	@Value("${kakao.redirect_uri}")
	private String redirectUri;

	public Mono<KakaoTokenResponse> getKakaoToken(String code) {
		return webClient.post()
			.uri("https://kauth.kakao.com/oauth/token") // 엑세스 토큰 발급 URL
			.body(BodyInserters.fromFormData("grant_type", "authorization_code")
				.with("client_id", clientId)
				.with("redirect_uri", redirectUri)
				.with("code", code))
			.retrieve()
			.onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
				log.warn("4xx 에러 발생: {}", clientResponse.statusCode());
				return clientResponse.createException().flatMap(Mono::error);
			})
			.onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
				log.error("5xx 서버 에러 발생: {}", clientResponse.statusCode());
				return clientResponse.createException().flatMap(Mono::error);
			})
			.bodyToMono(KakaoTokenResponse.class);
	}

	public Mono<KakaoUserInfoResponse> getKakaoUserInfo(String accessToken) {
		return webClient.get()
			.uri("https://kapi.kakao.com/v2/user/me") // 발급된 엑세스 토큰으로 카카오에 유저 정보 조회
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.retrieve()
			.bodyToMono(KakaoUserInfoResponse.class);
	}

	public AuthLoginResponse loginWithKakao(KakaoUserInfoResponse kakaoUserInfo, HttpServletResponse response) {

		// 카카오 유저 정보 유저테이블에 저장
		User user = userRepository.findByVendorId(kakaoUserInfo.getId())
			.orElseGet(() -> userRepository.save(User.builder()
				.vendorId(kakaoUserInfo.getId())
				.email(kakaoUserInfo.getKakaoAccount().getEmail())
				.username(kakaoUserInfo.getProperties().getNickName())
				.role(Role.USER)
				.loginProvider(LoginProvider.KAKAO)
				.build()));

		// 세션 객체 생성
		SessionUser session = new SessionUser(
			user.getId(),
			user.getUsername(),
			user.getRole(),
			user.getLoginProvider()
		);

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

		// 응답 DTO 생성
		return AuthLoginResponse.of(user);
	}

}
