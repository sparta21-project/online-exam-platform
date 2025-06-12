package com.example.onlineexamplatform.config.session;

import com.example.onlineexamplatform.domain.user.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {
	private Long userid;
	private String username;
	private Role role;
}

