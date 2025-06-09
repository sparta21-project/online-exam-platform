package com.example.onlineexamplatform.domain.exam.dto.response;

import java.time.LocalDateTime;

import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class UpdateExamResponseDto {

	private final Long id;

	private final Long userId;

	private final String title;

	private final String description;

	private final Long totalQuestionsNum;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private final LocalDateTime currentTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private final LocalDateTime startTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private final LocalDateTime endTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private final LocalDateTime updatedAt;

	public static UpdateExamResponseDto from(Exam exam) {
		return UpdateExamResponseDto.builder()
			.id(exam.getId())
			.userId(exam.getUser().getId())
			.title(exam.getTitle())
			.description(exam.getDescription())
			.totalQuestionsNum(exam.getTotalQuestionsNum())
			.currentTime(exam.getCurrentTime())
			.startTime(exam.getStartTime())
			.endTime(exam.getEndTime())
			.updatedAt(exam.getUpdatedAt())
			.build();

	}
}
