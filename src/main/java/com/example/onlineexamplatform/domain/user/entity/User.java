package com.example.onlineexamplatform.domain.user.entity;

import java.time.LocalDateTime;

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
@Table(name = "user")
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private Long vendorId;

	@Column(unique = true, nullable = false, length = 255)
	private String email;

	@Column(length = 255)
	private String password;

	@Column(nullable = false, length = 20)
	private String username;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private LoginProvider loginProvider;

	@Column(nullable = false)
	private Boolean isWithdraw = false;

	@Column
	private LocalDateTime withdrawAt;

	public User(String email, String password, String username, Role role, LoginProvider loginProvider) {
		this.email = email;
		this.password = password;
		this.username = username;
		this.role = role;
		this.loginProvider = loginProvider;
	}

	@Builder
	private User(Long vendorId, String email, String username, Role role, LoginProvider loginProvider) {
		this.vendorId = vendorId;
		this.email = email;
		this.username = username;
		this.role = role;
		this.loginProvider = loginProvider;
	}

	public static User fromKakao(KakaoUserInfoResponse kakaoUserInfo) {
		return User.builder()
			.vendorId(kakaoUserInfo.getId())
			.email(kakaoUserInfo.getKakaoAccount().getEmail())
			.username(kakaoUserInfo.getProperties().getNickName())
			.role(Role.USER)
			.loginProvider(LoginProvider.KAKAO)
			.build();
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void withdraw() {
		this.isWithdraw = true;
		this.withdrawAt = LocalDateTime.now();
	}

	public boolean isWithdraw() {
		return Boolean.TRUE.equals(this.isWithdraw);
	}

	public void updateEmail(String email) {
		this.email = email;
	}

	public void updateUsername(String username) {
		this.username = username;
	}
}
