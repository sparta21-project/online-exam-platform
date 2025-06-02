package com.example.onlineexamplatform.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthPasswordRequest {
	@NotBlank(message = "현재 비밀번호를 입력해주세요.")
	private String oldPassword;

	@NotBlank(message = "새 비밀번호는 필수값입니다.")
	@Size(min = 8, message = "새 비밀번호는 최소 8자 이상이어야 합니다.")
	private String newPassword;
}
