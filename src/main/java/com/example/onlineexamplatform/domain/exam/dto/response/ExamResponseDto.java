package com.example.onlineexamplatform.domain.exam.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class ExamResponseDto<T> {

	private final Long id;

	private final Long userId;

	private final String title;

	private final String description;

	private final Long totalQuestionsNum;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private final LocalDateTime startTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private final LocalDateTime endTime;

	private final List<T> examFiles;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime createdAt;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime updatedAt;

	public static ExamResponseDto from(Exam exam) {
		return ExamResponseDto.builder()
				.id(exam.getId())
				.userId(exam.getUser().getId())
				.title(exam.getTitle())
				.description(exam.getDescription())
				.startTime(exam.getStartTime())
				.endTime(exam.getEndTime())
				.createdAt(exam.getCreatedAt())
				.updatedAt(exam.getUpdatedAt())
				.build();

	}

	public static <T> ExamResponseDto<T> of(Exam exam, List<T> examFiles) {
		return new ExamResponseDto<>(
			exam.getId(),
			exam.getUser().getId(),
			exam.getTitle(),
			exam.getDescription(),
			exam.getTotalQuestionsNum(),
			exam.getStartTime(),
			exam.getEndTime(),
			examFiles,
			exam.getCreatedAt(),
			exam.getUpdatedAt()
		);
	}

}
