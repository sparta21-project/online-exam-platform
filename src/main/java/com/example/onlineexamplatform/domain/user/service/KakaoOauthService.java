package com.example.onlineexamplatform.domain.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.onlineexamplatform.config.session.SessionUser;
import com.example.onlineexamplatform.domain.user.dto.AuthLoginResponse;
import com.example.onlineexamplatform.domain.user.dto.KakaoTokenResponse;
import com.example.onlineexamplatform.domain.user.dto.KakaoUserInfoResponse;
import com.example.onlineexamplatform.domain.user.entity.User;
import com.example.onlineexamplatform.domain.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoOauthService {
	private final WebClient webClient;
	private final UserRepository userRepository;
	private final RedisService redisService;
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
			.orElseGet(() -> userRepository.save(User.fromKakao(kakaoUserInfo)));

		// 세션 객체 생성
		SessionUser session = new SessionUser(
			user.getId(),
			user.getUsername(),
			user.getRole(),
			user.getLoginProvider()
		);

		// Redis에 Session 저장 및 쿠키 생성
		redisService.createRedisSession(session, response);

		// 응답 DTO 생성
		return AuthLoginResponse.of(user);
	}

}
