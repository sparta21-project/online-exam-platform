package com.example.onlineexamplatform.domain.user.entity;

import com.example.onlineexamplatform.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
	@Column(unique = true, nullable = false, length = 255)
	private String email;
	@Column(nullable = false, length = 255)
	private String password;
	@Column(nullable = false, length = 20)
	private String username;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Column(nullable = false)
	private Boolean isWithdraw = false;

	public User(String email, String password, String username, Role role) {
		this.email = email;
		this.password = password;
		this.username = username;
		this.role = role;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void withdraw() {
		this.isWithdraw = true;
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
