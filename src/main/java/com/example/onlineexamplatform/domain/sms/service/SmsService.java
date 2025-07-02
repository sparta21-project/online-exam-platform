package com.example.onlineexamplatform.domain.sms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.exam.repository.ExamRepository;
import com.example.onlineexamplatform.domain.sms.dto.SmsResponse;
import com.example.onlineexamplatform.domain.sms.entity.Sms;
import com.example.onlineexamplatform.domain.sms.repository.SmsRepository;
import com.example.onlineexamplatform.domain.user.entity.User;
import com.example.onlineexamplatform.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SmsService {

	private final SmsRepository smsRepository;
	private final UserRepository userRepository;
	private final ExamRepository examRepository;

	@Value("${solapi.api-key}")
	private String apiKey;

	@Value("${solapi.api-secret}")
	private String apiSecret;

	@Value("${solapi.domain}")
	private String domain;

	@Value("${solapi.from}")
	private String from;

	@Transactional
	public void createSms(Long userId, Long examId, int score) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		Exam exam = examRepository.findById(examId)
			.orElseThrow(() -> new ApiException(ErrorStatus.EXAM_NOT_FOUND));

		String phoneNumber = user.getPhoneNumber();
		String messageText = "[온라인시험] " + exam.getTitle() + " 시험의 채점이 완료되었습니다. 점수: " + score + "점";

		// DB에 저장
		Sms sms = new Sms(phoneNumber, messageText, user, exam);
		smsRepository.save(sms);

		// 벤더사(Solapi) 연동
		try {
			DefaultMessageService messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, domain);

			Message solapiMessage = new Message();
			solapiMessage.setFrom(from);         // 발신번호
			solapiMessage.setTo(phoneNumber);    // 수신번호
			solapiMessage.setText(messageText);  // 메시지 내용

			SingleMessageSendingRequest request = new SingleMessageSendingRequest(solapiMessage);
			SingleMessageSentResponse response = messageService.sendOne(request);
			// 필요 시 로그 추가 가능
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			throw new ApiException(ErrorStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	public List<SmsResponse> getSmsList(Long userId) {
		List<Sms> smsList = smsRepository.findAllByUserId(userId);

		return smsList.stream()
			.map(sms -> new SmsResponse(
				sms.getExam().getId(),
				sms.getMessage(),
				sms.getCreatedAt()
			))
			.toList();
	}
}
