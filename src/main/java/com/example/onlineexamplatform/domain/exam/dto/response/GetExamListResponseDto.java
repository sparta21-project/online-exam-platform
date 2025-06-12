package com.example.onlineexamplatform.domain.exam.dto.response;

import com.example.onlineexamplatform.domain.exam.entity.Exam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetExamListResponseDto {

	@Schema(description = "시험ID", example = "1")
	private final Long id;

	@Schema(description = "유저ID", example = "1")
	private final Long userId;

	@Schema(description = "시험 전체 조회 시 보일 시험 제목", example = "시험 제목")
	private final String title;

	public static GetExamListResponseDto toDto(Exam exam) {
		return new GetExamListResponseDto(
			exam.getId(),
			exam.getUser().getId(),
			exam.getTitle()
		);
	}

}
