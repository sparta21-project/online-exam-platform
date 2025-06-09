package com.example.onlineexamplatform.domain.exam.dto.request;

import java.time.LocalDateTime;
import java.util.List;

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
	private final String title;

	@NotBlank(message = "시험 설명은 필수입니다.")
	@Size(max = 1000)
	private final String description;

	@NotNull(message = "전체 문항수는 필수입니다.")
	private final Long totalQuestionsNum;

	@NotNull(message = "시험 시작 시간은 필수입니다.")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private final LocalDateTime startTime;

	@NotNull(message = "시험 시작 시간은 필수입니다.")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private final LocalDateTime endTime;

	private final List<Long> examFileIds;

	public CreateExamRequestDto toCreate() {
		return new CreateExamRequestDto(title, description, totalQuestionsNum, startTime, endTime, examFileIds);
	}

}
