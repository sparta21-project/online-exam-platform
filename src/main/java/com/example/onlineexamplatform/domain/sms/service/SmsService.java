package com.example.onlineexamplatform.domain.sms.service;

import org.springframework.stereotype.Service;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.exam.repository.ExamRepository;
import com.example.onlineexamplatform.domain.sms.entity.Sms;
import com.example.onlineexamplatform.domain.sms.repository.SmsRepository;
import com.example.onlineexamplatform.domain.user.entity.User;
import com.example.onlineexamplatform.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SmsService {

	private final SmsRepository smsRepository;
	private final UserRepository userRepository;
	private final ExamRepository examRepository;

	@Transactional
	public void createSms(Long userId, Long examId, int score) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		Exam exam = examRepository.findById(examId)
			.orElseThrow(() -> new ApiException(ErrorStatus.EXAM_NOT_FOUND));

		String phoneNumber = user.getPhoneNumber();
		String message = String.format("[온라인시험] '%s' 시험의 채점이 완료되었습니다. 점수: %d점", exam.getTitle(), score);

		Sms sms = new Sms(phoneNumber, message, user, exam);
		smsRepository.save(sms);

		// 벤더사 연동
	}
}
