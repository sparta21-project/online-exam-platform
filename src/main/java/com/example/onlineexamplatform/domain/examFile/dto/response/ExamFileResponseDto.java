package com.example.onlineexamplatform.domain.examFile.dto.response;

import java.time.LocalDateTime;

import com.example.onlineexamplatform.domain.examFile.entity.ExamFile;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExamFileResponseDto {

	private final Long id;

	private final Long examId;

	private final String fileName;

	private final String path;

	private final String fileType;

	private final Long size;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime createdAt;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime updatedAt;

	public static ExamFileResponseDto of(ExamFile examFile) {
		return new ExamFileResponseDto(
			examFile.getId(),
			examFile.getExam() != null ? examFile.getExam().getId() : null,
			examFile.getFileName(),
			examFile.getPath(),
			examFile.getFileType(),
			examFile.getSize(),
			examFile.getCreatedAt(),
			examFile.getUpdatedAt()
		);
	}
}
