package com.example.onlineexamplatform.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원가입 요청 정보")
public class AuthSignupRequest {
	@Schema(description = "사용자 이메일", example = "user@example.com")
	@Email
	@NotBlank
	private String email;

	@Schema(description = "비밀번호", example = "P@ssw0rd!")
	@NotBlank
	@Size(min = 8, max = 64)
	@Pattern(
		regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+]).+$",
		message = "비밀번호는 대문자·소문자·숫자·특수문자를 모두 포함해야 합니다."
	)
	private String password;

	@Schema(description = "사용자 이름", example = "김민준")
	@NotBlank
	private String username;

	@Schema(description = "전화번호", example = "01012345678")
	@NotBlank
	@Pattern(regexp = "^010\\d{8}$", message = "전화번호는 010으로 시작하는 11자리 숫자여야 합니다.")
	private String phoneNumber;

}
