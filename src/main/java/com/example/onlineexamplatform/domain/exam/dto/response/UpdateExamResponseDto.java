package com.example.onlineexamplatform.domain.exam.dto.response;

import java.time.LocalDateTime;

import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class UpdateExamResponseDto {

	@Schema(description = "시험ID", example = "1")
	private final Long id;

	@Schema(description = "유저ID", example = "1")
	private final Long userId;

	@Schema(description = "수정된 시험 제목", example = "수정된 시험 제목")
	private final String title;

	@Schema(description = "수정된 시험 설명", example = "수정된 시험 설명")
	private final String description;

	@Schema(description = "수정된 시험 문항수", example = "50")
	private final Long totalQuestionsNum;

	@Schema(description = "수정된 시험 시작 시간", example = "2025-06-12 15:00")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private final LocalDateTime startTime;

	@Schema(description = "시험 종료 시간", example = "2025-06-12 16:00")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private final LocalDateTime endTime;

	@Schema(description = "수정일")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime updatedAt;

	public static UpdateExamResponseDto from(Exam exam) {
		return UpdateExamResponseDto.builder()
			.id(exam.getId())
			.userId(exam.getUser().getId())
			.title(exam.getTitle())
			.description(exam.getDescription())
			.totalQuestionsNum(exam.getTotalQuestionsNum())
			.startTime(exam.getStartTime())
			.endTime(exam.getEndTime())
			.updatedAt(exam.getUpdatedAt())
			.build();

	}
}
