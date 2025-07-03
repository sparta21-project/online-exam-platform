package com.example.onlineexamplatform.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoResponse {

	@JsonProperty("id")
	private String id;

	@JsonProperty("kakao_account")
	private KakaoAccount kakaoAccount;

	@JsonProperty("properties")
	private Properties properties;

	@Getter
	@NoArgsConstructor
	public static class Properties {

		@JsonProperty("nickname")
		private String nickName;

	}

	@Getter
	@NoArgsConstructor
	public static class KakaoAccount {

		@JsonProperty("email")
		private String email;

	}

}
