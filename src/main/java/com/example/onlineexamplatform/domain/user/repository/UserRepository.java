package com.example.onlineexamplatform.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.onlineexamplatform.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {
	Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);
	// 이름 및 이메일 포함 검색
	List<User> findByUsernameContainingAndEmailContaining(String username, String email);
}
