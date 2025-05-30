package com.example.onlineexamplatform.domain.exam.dto.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateExamRequestDto {

	@NotBlank(message = "시험 제목은 필수입니다.")
	@Size(max = 100)
	private String title;

	@NotBlank(message = "시험 설명은 필수입니다.")
	@Size(max = 1000)
	private String description;

	@NotBlank(message = "시험 파일 경로는 필수입니다.")
	@Size(max = 1024)
	private String filePath;

	@NotNull(message = "시험 시작 시간은 필수입니다.")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime startTime;

	@NotNull(message = "시험 시작 시간은 필수입니다.")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime endTime;

}
