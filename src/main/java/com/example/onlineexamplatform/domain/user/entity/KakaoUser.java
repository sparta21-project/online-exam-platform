package com.example.onlineexamplatform.domain.user.entity;

import com.example.onlineexamplatform.common.entity.BaseEntity;
import com.example.onlineexamplatform.domain.user.dto.KakaoUserInfoResponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "kakao_user")
@Getter
@NoArgsConstructor
public class KakaoUser extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private Long kakaoId;

	@Column(unique = true, nullable = false, length = 255)
	private String email;

	@Column(nullable = false, length = 20)
	private String username;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Builder
	private KakaoUser(Long kakaoId, String email, String username, Role role) {
		this.kakaoId = kakaoId;
		this.email = email;
		this.username = username;
		this.role = role;
	}

	public static KakaoUser fromKakao(KakaoUserInfoResponse kakaoUser) {
		return KakaoUser.builder()
			.kakaoId(kakaoUser.getId())
			.email(kakaoUser.getEmail())
			.username(kakaoUser.getNickname())
			.role(Role.USER)
			.build();
	}

}
