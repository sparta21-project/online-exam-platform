package com.example.onlineexamplatform.domain.sms.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "SMS 알림 응답")
public class SmsResponse {

	@Schema(description = "시험 ID", example = "1")
	private Long examId;

	@Schema(description = "메시지 내용", example = "[온라인시험] 'Spring 초급' 시험의 채점이 완료되었습니다. 점수: 90점")
	private String message;

	@Schema(description = "알림 전송 시각", example = "2025-06-30T13:45:00")
	private LocalDateTime createdAt;
}
