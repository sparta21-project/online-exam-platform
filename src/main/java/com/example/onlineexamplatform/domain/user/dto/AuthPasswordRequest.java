package com.example.onlineexamplatform.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "비밀번호 변경 요청 정보")
public class AuthPasswordRequest {

	@Schema(description = "현재 비밀번호", example = "oldP@ssw0rd")
	@NotBlank(message = "현재 비밀번호를 입력해주세요.")
	private String oldPassword;

	@Schema(description = "새 비밀번호 (최소 8자 이상)", example = "NewP@ssw0rd123")
	@NotBlank(message = "새 비밀번호는 필수값입니다.")
	@Size(min = 8, max = 64)
	@Pattern(
		regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+]).+$",
		message = "비밀번호는 대문자·소문자·숫자·특수문자를 모두 포함해야 합니다."
	)
	private String newPassword;
}
