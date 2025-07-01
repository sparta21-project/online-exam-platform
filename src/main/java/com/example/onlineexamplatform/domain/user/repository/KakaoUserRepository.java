package com.example.onlineexamplatform.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.onlineexamplatform.domain.user.entity.KakaoUser;

public interface KakaoUserRepository extends JpaRepository<KakaoUser, Long> {

	Optional<KakaoUser> findByKakaoId(Long Id);
}
