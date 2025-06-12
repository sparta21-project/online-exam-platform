package com.example.onlineexamplatform.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "프로필 수정 요청 정보")
public class UserProfileModifyRequest {

	@Schema(description = "새 사용자 이메일", example = "newuser@example.com")
	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "유효한 이메일 형식이어야 합니다.")
	private String email;

	@Schema(description = "새 사용자명 (2~20자)", example = "newUsername")
	@NotBlank(message = "사용자명을 입력해주세요.")
	@Size(min = 2, max = 20, message = "사용자명은 2~20자 사이여야 합니다.")
	private String username;
}
