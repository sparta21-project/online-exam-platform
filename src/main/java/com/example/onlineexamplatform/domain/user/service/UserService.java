package com.example.onlineexamplatform.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
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
	private final PasswordEncoder passwordEncoder;

	public UserSignupResponse signup(UserSignupRequest request) {
		// 이메일 중복체크
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new ApiException(ErrorStatus.DUPLICATE_EMAIL);
		}

		// 비밀번호 암호화
		String encoded = passwordEncoder.encode(request.getPassword());

		// jpa에 저장할 user 엔티티 객체
		User user = new User(
			request.getEmail(),
			encoded,
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

}
