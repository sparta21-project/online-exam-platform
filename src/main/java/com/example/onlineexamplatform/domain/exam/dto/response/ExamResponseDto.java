package com.example.onlineexamplatform.domain.exam.dto.response;

import java.time.LocalDateTime;

import com.example.onlineexamplatform.domain.exam.entity.Exam;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class ExamResponseDto {

	private final Long id;

	private final Long userId;

	private final String title;

	private final String description;

	private final String filePath;

	private final LocalDateTime startTime;

	private final LocalDateTime endTime;

	private final LocalDateTime createdAt;

	private final LocalDateTime updatedAt;

	public static ExamResponseDto from(Exam exam) {
		return ExamResponseDto.builder()
			.id(exam.getId())
			.userId(exam.getUser().getId())
			.title(exam.getTitle())
			.description(exam.getDescription())
			.filePath(exam.getFilePath())
			.startTime(exam.getStartTime())
			.endTime(exam.getEndTime())
			.createdAt(exam.getCreatedAt())
			.updatedAt(exam.getUpdatedAt())
			.build();

	}
}
