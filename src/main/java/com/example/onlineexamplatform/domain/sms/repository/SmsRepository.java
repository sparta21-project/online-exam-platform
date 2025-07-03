package com.example.onlineexamplatform.domain.sms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.onlineexamplatform.domain.sms.entity.Sms;

public interface SmsRepository extends JpaRepository<Sms, Long> {
	List<Sms> findAllByUserId(Long userId);
}
