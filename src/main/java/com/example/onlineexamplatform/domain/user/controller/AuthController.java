package com.example.onlineexamplatform.domain.user.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.config.session.CheckAuth;
import com.example.onlineexamplatform.config.session.SessionUser;
import com.example.onlineexamplatform.config.session.UserSession;
import com.example.onlineexamplatform.domain.user.dto.AuthLoginRequest;
import com.example.onlineexamplatform.domain.user.dto.AuthLoginResponse;
import com.example.onlineexamplatform.domain.user.dto.AuthLoginResult;
import com.example.onlineexamplatform.domain.user.dto.AuthPasswordRequest;
import com.example.onlineexamplatform.domain.user.dto.AuthSignupRequest;
import com.example.onlineexamplatform.domain.user.dto.AuthSignupResponse;
import com.example.onlineexamplatform.domain.user.entity.LoginProvider;
import com.example.onlineexamplatform.domain.user.entity.Role;
import com.example.onlineexamplatform.domain.user.service.KakaoOauthService;
import com.example.onlineexamplatform.domain.user.service.RedisService;
import com.example.onlineexamplatform.domain.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "01 Authentication", description = "회원가입,로그인,로그아웃,비밀번호 변경 API")
public class AuthController {

	private static final String SESSION_COOKIE_NAME = "SESSION";
	private final KakaoOauthService kakaoOauthService;
	private final UserService userService;
	private final RedisTemplate<String, SessionUser> redisTemplate;
	private final RedisService redisService;

	// 유저 회원가입
	@Operation(summary = "일반 사용자 회원가입", description = "이메일, 비밀번호, 사용자명을 입력 받아 신규 사용자 계정을 생성합니다.")
	@Parameter(description = "회원가입 요청 정보")
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<AuthSignupResponse>> signup(
		@RequestBody @Valid AuthSignupRequest request,
		HttpServletResponse response
	) {
		AuthSignupResponse dto = userService.signup(request);

		// 세션 객체 생성
		SessionUser session = new SessionUser(
			dto.getId(),
			dto.getUsername(),
			dto.getRole(),
			LoginProvider.LOCAL
		);

		// Redis에 Session 저장 및 쿠키 생성
		redisService.createRedisSession(session, response);

		return ApiResponse.onSuccess(SuccessStatus.SIGNUP_SUCCESS, dto);
	}

	// 관리자 회원가입
	@Operation(summary = "관리자 회원가입", description = "관리자 권한을 가진 계정을 생성합니다.")
	@Parameter(description = "관리자 회원가입 요청 정보")
	@PostMapping("/admin/signup")
	public ResponseEntity<ApiResponse<AuthSignupResponse>> createAdmin(
		@RequestBody @Valid AuthSignupRequest request,
		HttpServletResponse response
	) {
		AuthSignupResponse dto = userService.signupAdmin(request);

		// 세션 객체 생성
		SessionUser session = new SessionUser(
			dto.getId(),
			dto.getUsername(),
			dto.getRole(),
			LoginProvider.LOCAL
		);

		redisService.createRedisSession(session, response);

		return ApiResponse.onSuccess(SuccessStatus.SIGNUP_SUCCESS, dto);
	}

	// 로그인
	@Operation(summary = "로그인", description = "이메일과 비밀번호를 검증하여 세션에 사용자 ID를 저장합니다.")
	@Parameter(description = "로그인 요청 정보")
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<AuthLoginResponse>> login(
		@RequestBody @Valid AuthLoginRequest request,
		HttpServletResponse response
	) {

		// 인증
		AuthLoginResult result = userService.login(request);
		AuthLoginResponse dto = AuthLoginResponse.of(result);

		SessionUser sessionUser = new SessionUser(
			result.getUserId(),
			result.getUsername(),
			result.getRole(),
			result.getLoginProvider()
		);

		// 쿠키 발급
		redisService.createRedisSession(sessionUser, response);

		return ApiResponse.onSuccess(SuccessStatus.LOGIN_SUCCESS, dto);
	}

	// 비밀번호 변경
	@CheckAuth(Role.USER)
	@Operation(summary = "비밀번호 변경", description = "로그인된 사용자의 비밀번호를 변경합니다.")
	@Parameter(description = "비밀번호 변경 요청 정보")
	@PutMapping("/password")
	public ResponseEntity<ApiResponse<Void>> changePassword(
		@RequestBody @Valid AuthPasswordRequest request,
		@UserSession SessionUser sessionUser
	) {
		userService.changePassword(sessionUser.getUserId(), request);
		return ApiResponse.onSuccess(SuccessStatus.UPDATE_PASSWORD);
	}

	// 로그아웃
	@CheckAuth(Role.USER)
	@Operation(summary = "로그아웃", description = "현재 세션을 무효화하여 로그아웃 처리합니다.")
	@DeleteMapping("/logout")
	public ResponseEntity<ApiResponse<Void>> logout(
		HttpServletRequest httpRequest,
		HttpServletResponse httpResponse) {

		// 쿠키에서 세션 id 가져오기
		Cookie cookie = WebUtils.getCookie(httpRequest, SESSION_COOKIE_NAME);
		if (cookie != null) {
			String sessionId = cookie.getValue();

			// Redis에서 세션 삭제
			String redisKey = SESSION_COOKIE_NAME + ":" + sessionId;
			redisTemplate.delete(redisKey);

			// 만료된 쿠키 재전송
			Cookie expired = new Cookie(SESSION_COOKIE_NAME, null);
			expired.setPath("/");
			expired.setHttpOnly(true);
			expired.setMaxAge(0);
			httpResponse.addCookie(expired);
		}

		return ApiResponse.onSuccess(SuccessStatus.LOGOUT_SUCCESS);
	}

	// 카카오 소셜 로그인
	@Operation(summary = "1- 6 카카오 로그인", description = "카카오 로그인을 통해 사용자 ID를 저장합니다.")
	@Parameter(description = "로그인 요청 정보")
	@GetMapping("/login/kakao")
	public Mono<ResponseEntity<ApiResponse<AuthLoginResponse>>> kakaoLogin(
		@RequestParam String code,
		HttpServletResponse response
	) {
		return kakaoOauthService.getKakaoToken(code)
			.flatMap(kakaoToken -> kakaoOauthService.getKakaoUserInfo(kakaoToken.getAccessToken()))
			.map(kakaoUserInfo -> {
				AuthLoginResponse authLoginResponse = kakaoOauthService.loginWithKakao(kakaoUserInfo, response);
				return ApiResponse.onSuccess(SuccessStatus.LOGIN_SUCCESS, authLoginResponse);
			});
	}

}
