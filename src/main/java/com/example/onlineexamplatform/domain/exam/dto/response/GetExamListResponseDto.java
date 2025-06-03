package com.example.onlineexamplatform.domain.exam.dto.response;

import com.example.onlineexamplatform.domain.exam.entity.Exam;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetExamListResponseDto {

	private final Long id;

	private final Long userId;

	private final String title;

	public static GetExamListResponseDto toDto(Exam exam) {
		return new GetExamListResponseDto(
			exam.getId(),
			exam.getUser().getId(),
			exam.getTitle()
		);
	}

}
