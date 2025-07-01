package com.example.onlineexamplatform.domain.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.onlineexamplatform.domain.user.dto.KakaoTokenResponse;
import com.example.onlineexamplatform.domain.user.dto.KakaoUserInfoResponse;
import com.example.onlineexamplatform.domain.user.entity.KakaoUser;
import com.example.onlineexamplatform.domain.user.entity.Role;
import com.example.onlineexamplatform.domain.user.repository.KakaoUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoOauthService {
	private final WebClient webClient;
	private final KakaoUserRepository kakaoUserRepository;

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

	// 카카오 유저 정보 유저테이블에 저장
	public KakaoUser signupByKakao(KakaoUserInfoResponse kakaoUserInfo) {
		return kakaoUserRepository.findByKakaoId(kakaoUserInfo.getId())
			.orElseGet(() -> kakaoUserRepository.save(KakaoUser.builder()
				.kakaoId(kakaoUserInfo.getId())
				.email(kakaoUserInfo.getEmail())
				.username(kakaoUserInfo.getNickname())
				.role(Role.USER)
				.build()));
	}

	public void logoutFromKakao(String accessToken) {
		webClient.post()
			.uri("https://kapi.kakao.com/v1/user/logout")
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.retrieve()
			.bodyToMono(String.class)
			.doOnSuccess(res -> log.info("[KakaoLogout] 카카오 로그아웃 완료"))
			.doOnError(err -> log.warn("[KakaoLogout] 로그아웃 실패: {}", err.getMessage()))
			.subscribe(); // 비동기로 호출 (단순 로그아웃이므로)
	}

}
