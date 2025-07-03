package com.example.onlineexamplatform.domain.exam.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.examFile.dto.response.ExamFileS3PreSignedURLDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExamDetailResponseDto {

	private final Long id;

	private final Long userId;

	private final String title;

	private final String description;

	private final Long totalQuestionsNum;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private final LocalDateTime startTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private final LocalDateTime endTime;

	private final List<ExamFileS3PreSignedURLDto> examFileS3PreSignedURLs;

	@Schema(description = "생성일")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime createdAt;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime updatedAt;

	public static ExamDetailResponseDto of(Exam exam, List<ExamFileS3PreSignedURLDto> examFileS3PreSignedURLs) {
		return new ExamDetailResponseDto(
			exam.getId(),
			exam.getUser().getId(),
			exam.getTitle(),
			exam.getDescription(),
			exam.getTotalQuestionsNum(),
			exam.getStartTime(),
			exam.getEndTime(),
			examFileS3PreSignedURLs,
			exam.getCreatedAt(),
			exam.getUpdatedAt()
		);
	}

}
