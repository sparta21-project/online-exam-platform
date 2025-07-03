package com.example.onlineexamplatform.domain.user.service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.password.PasswordUtil;
import com.example.onlineexamplatform.config.session.SessionUser;
import com.example.onlineexamplatform.domain.user.dto.AuthLoginRequest;
import com.example.onlineexamplatform.domain.user.dto.AuthLoginResult;
import com.example.onlineexamplatform.domain.user.dto.AuthPasswordRequest;
import com.example.onlineexamplatform.domain.user.dto.AuthSignupRequest;
import com.example.onlineexamplatform.domain.user.dto.AuthSignupResponse;
import com.example.onlineexamplatform.domain.user.dto.UserProfileModifyRequest;
import com.example.onlineexamplatform.domain.user.dto.UserProfileModifyResponse;
import com.example.onlineexamplatform.domain.user.dto.UserProfileResponse;
import com.example.onlineexamplatform.domain.user.entity.Role;
import com.example.onlineexamplatform.domain.user.entity.User;
import com.example.onlineexamplatform.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final RedisTemplate<String, SessionUser> redisTemplate;

	// 유저 회원가입
	public AuthSignupResponse signup(AuthSignupRequest request) {

		// 이메일 중복체크
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new ApiException(ErrorStatus.DUPLICATE_EMAIL);
		}

		String hashedPassword = PasswordUtil.hash(request.getPassword());

		// jpa에 저장할 user 엔티티 객체
		User user = new User(
			request.getEmail(),
			hashedPassword, //평문
			request.getUsername(),
			Role.USER
		);

		// 엔티티에 저장
		User saved = userRepository.save(user);

		// Response dto 반환
		return new AuthSignupResponse(
			saved.getId(),
			saved.getEmail(),
			saved.getUsername(),
			saved.getRole()
		);

	}

	// 관리자 회원가입
	public AuthSignupResponse signupAdmin(AuthSignupRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new ApiException(ErrorStatus.DUPLICATE_EMAIL);
		}

		String hashedPassword = PasswordUtil.hash(request.getPassword());

		User user = new User(
			request.getEmail(),
			hashedPassword,
			request.getUsername(),
			Role.ADMIN
		);

		User saved = userRepository.save(user);

		return new AuthSignupResponse(
			saved.getId(),
			saved.getEmail(),
			saved.getUsername(),
			saved.getRole()
		);
	}

	// 로그인
	public AuthLoginResult login(AuthLoginRequest request) {
		User user = userRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		if (user.isWithdraw()) {
			throw new ApiException(ErrorStatus.USER_DEACTIVATE);
		}

		// BCrypt를 사용한 비밀번호 검증
		if (!PasswordUtil.checkPassword(request.getPassword(), user.getPassword())) {
			throw new ApiException(ErrorStatus.USER_NOT_MATCH);
		}

		SessionUser sessionUser = new SessionUser(user.getId(), user.getEmail(), user.getRole());
		String sessionId = UUID.randomUUID().toString();
		String redisKey = "SESSION:" + sessionId;
		redisTemplate.opsForValue().set(redisKey, sessionUser, Duration.ofHours(24));

		return new AuthLoginResult(
			user.getId(),
			user.getEmail(),
			user.getUsername(),
			user.getRole(),
			sessionId
		);
	}

	// 비밀번호 수정
	public void changePassword(Long userId, AuthPasswordRequest dto) {
		// 조회
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		if (user.isWithdraw()) {
			throw new ApiException(ErrorStatus.USER_DEACTIVATE);
		}

		// 현재 비밀번호 검증
		if (!PasswordUtil.checkPassword(dto.getOldPassword(), user.getPassword())) {
			throw new ApiException(ErrorStatus.INVALID_PASSWORD);
		}

		// 새 비밀번호를 해시화하여 저장
		String hashedNewPassword = PasswordUtil.hash(dto.getNewPassword());
		user.setPassword(hashedNewPassword);
		userRepository.save(user);
	}

	// 프로필 조회
	public UserProfileResponse getProfile(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		if (user.isWithdraw()) {
			throw new ApiException(ErrorStatus.USER_DEACTIVATE);
		}

		return new UserProfileResponse(
			user.getId(),
			user.getEmail(),
			user.getUsername(),
			user.getRole()
		);
	}

	// 프로필 수정
	public UserProfileModifyResponse modifyProfile(Long userId, UserProfileModifyRequest request) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		if (!request.getEmail().equals(user.getEmail())
			&& userRepository.existsByEmail(request.getEmail())) {
			throw new ApiException(ErrorStatus.DUPLICATE_EMAIL);
		}

		user.updateEmail(request.getEmail());
		user.updateUsername(request.getUsername());

		User saved = userRepository.save(user);

		return new UserProfileModifyResponse(
			saved.getId(),
			saved.getEmail(),
			saved.getUsername(),
			saved.getRole()
		);
	}

	// 회원탈퇴
	public void delete(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		if (user.isWithdraw()) {
			throw new ApiException(ErrorStatus.ALREADY_WITHDRAWN);
		}

		user.withdraw();
		userRepository.save(user);
	}

	// 사용자 목록조회 및 검색 (관리자 전용)
	@Transactional(readOnly = true)
	public List<UserProfileResponse> getUsersByFilter(String name, String email) {
		String nameFilter = name != null ? name : "";
		String emailFilter = email != null ? email : "";

		List<User> users = userRepository.findByUsernameContainingAndEmailContaining(nameFilter, emailFilter);

		return users.stream()
			.map(user -> new UserProfileResponse(
				user.getId(),
				user.getEmail(),
				user.getUsername(),
				user.getRole()
			))
			.toList();
	}
}
