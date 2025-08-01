package com.example.onlineexamplatform.domain.exam.dto.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateExamRequestDto {

	@Schema(description = "수정된 시험 제목", example = "수정된 시험 제목")
	@NotBlank(message = "시험 제목은 필수입니다.")
	@Size(max = 100)
	private final String title;

	@Schema(description = "수정된 시험 설명", example = "수정된 시험 설명")
	@NotBlank(message = "시험 설명은 필수입니다.")
	@Size(max = 1000)
	private final String description;

	@Schema(description = "시험 문항수", example = "50")
	@NotNull(message = "전체 문항수는 필수입니다.")
	private final Long totalQuestionsNum;

	@Schema(description = "수정된 시험 시작 시간", example = "2025-06-12 15:00")
	@NotNull(message = "시험 시작 시간은 필수입니다.")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private final LocalDateTime startTime;

	@Schema(description = "수정된 시험 종료 시간", example = "2025-06-12 16:00")
	@NotNull(message = "시험 종료 시간은 필수입니다.")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private final LocalDateTime endTime;

	@Schema(description = "시험 응시 최대 가능 인원", example = "100")
	@Min(value = 1, message = "1 이상의 숫자를 입력해주세요.")
	private final Integer remainUsers;

}
