package com.example.onlineexamplatform.domain.password;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class PasswordController {
	@PostMapping("/encrypt")
	public String encryptPassword(@RequestParam String password) {
		// 실제 암호화
		return PasswordUtil.hashPassword(password);
	}

	@PostMapping("/verify")
	public String verifyPassword(@RequestParam String password, @RequestParam String hashed) {
		boolean result = PasswordUtil.checkPassword(password, hashed);
		return result ? "비밀번호 일치" : "비밀번호 불일치";
	}
}
