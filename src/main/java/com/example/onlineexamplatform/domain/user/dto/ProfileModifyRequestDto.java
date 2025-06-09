package com.example.onlineexamplatform.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileModifyRequestDto {
	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "유효한 이메일 형식이어야 합니다.")
	private String email;

	@NotBlank(message = "사용자명을 입력해주세요.")
	@Size(min = 2, max = 20, message = "사용자명은 2~20자 사이여야 합니다.")
	private String username;
}
