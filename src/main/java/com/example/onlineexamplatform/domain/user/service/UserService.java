package com.example.onlineexamplatform.domain.user.service;

import org.springframework.stereotype.Service;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.user.dto.UserLoginRequest;
import com.example.onlineexamplatform.domain.user.dto.UserLoginResponse;
import com.example.onlineexamplatform.domain.user.dto.UserSignupRequest;
import com.example.onlineexamplatform.domain.user.dto.UserSignupResponse;
import com.example.onlineexamplatform.domain.user.entity.Role;
import com.example.onlineexamplatform.domain.user.entity.User;
import com.example.onlineexamplatform.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public UserSignupResponse signup(UserSignupRequest request) {
		// 이메일 중복체크
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new ApiException(ErrorStatus.DUPLICATE_EMAIL);
		}

		// 비밀번호 암호화
		// String encodedPassword = encoder.encode(request.getPassword());

		// jpa에 저장할 user 엔티티 객체
		User user = new User(
			request.getEmail(),
			request.getPassword(),
			request.getUsername(),
			Role.USER
		);

		// 엔티티에 저장
		User saved = userRepository.save(user);

		// Response dto 반환
		return new UserSignupResponse(
			saved.getId(),
			saved.getEmail(),
			saved.getUsername(),
			saved.getRole().name()
		);

	}

	public UserLoginResponse login(UserLoginRequest request) {
		User user = userRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		if (!request.getPassword().equals(user.getPassword())) {
			throw new ApiException(ErrorStatus.USER_NOT_FOUND);
		}

		return new UserLoginResponse(
			user.getId(),
			user.getEmail(),
			user.getUsername(),
			user.getRole().name()
		);
	}

}
