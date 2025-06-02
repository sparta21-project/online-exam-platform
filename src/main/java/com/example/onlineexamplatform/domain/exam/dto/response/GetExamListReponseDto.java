package com.example.onlineexamplatform.domain.exam.dto.response;

import com.example.onlineexamplatform.domain.exam.entity.Exam;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetExamListReponseDto {

	private final Long id;

	private final Long userId;

	private final String title;

	public static GetExamListReponseDto toDto(Exam exam) {
		return new GetExamListReponseDto(
			exam.getId(),
			exam.getUser().getId(),
			exam.getExamTitle()
		);
	}

}
