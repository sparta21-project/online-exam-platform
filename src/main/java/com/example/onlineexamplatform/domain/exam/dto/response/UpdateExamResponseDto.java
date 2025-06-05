package com.example.onlineexamplatform.domain.exam.dto.response;

import java.time.LocalDateTime;

import com.example.onlineexamplatform.domain.exam.entity.Exam;

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

	private final LocalDateTime startTime;

	private final LocalDateTime endTime;

	private final LocalDateTime updatedAt;

	public static UpdateExamResponseDto from(Exam exam) {
		return UpdateExamResponseDto.builder()
			.id(exam.getId())
			.userId(exam.getUser().getId())
			.title(exam.getTitle())
			.description(exam.getDescription())
			.startTime(exam.getStartTime())
			.endTime(exam.getEndTime())
			.updatedAt(exam.getUpdatedAt())
			.build();

	}
}
