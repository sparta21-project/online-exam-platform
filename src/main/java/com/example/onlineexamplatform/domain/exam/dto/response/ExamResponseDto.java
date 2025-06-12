package com.example.onlineexamplatform.domain.exam.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class ExamResponseDto<T> {

	@Schema(description = "시험ID", example = "1")
	private final Long id;

	@Schema(description = "유저ID", example = "1")
	private final Long userId;

	@Schema(description = "시험 제목", example = "시험 제목")
	private final String title;

	@Schema(description = "시험 설명", example = "시험 설명")
	private final String description;

	@Schema(description = "시험 문항수", example = "100")
	private final Long totalQuestionsNum;

	@Schema(description = "시험 시작 시간", example = "2025-06-12 13:00")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private final LocalDateTime startTime;

	@Schema(description = "시험 종료 시간", example = "2025-06-12 14:00")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private final LocalDateTime endTime;

	@Schema(description = "S3에 업로드한 시험파일의 정보", example = "[1]")
	private final List<T> examFiles;

	@Schema(description = "생성일")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime createdAt;

	@Schema(description = "수정일")
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
