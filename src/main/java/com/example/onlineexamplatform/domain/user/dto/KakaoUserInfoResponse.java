package com.example.onlineexamplatform.domain.user.dto;

import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoUserInfoResponse {

	private final Long id;

	private final Map<String, Object> properties; // nickname, profile_image 등

	private final Map<String, Object> kakao_account; //email, age_range 등

	public String getEmail() {
		if (kakao_account == null)
			return null;
		Object email = kakao_account.get("email");
		return email != null ? email.toString() : null;
	}

	public String getNickname() {
		if (properties == null)
			return null;
		Object nickName = properties.get("nickname");
		return nickName != null ? nickName.toString() : null;
	}

}
