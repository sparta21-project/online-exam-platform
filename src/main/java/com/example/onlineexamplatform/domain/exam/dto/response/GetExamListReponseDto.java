package com.example.onlineexamplatform.domain.exam.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetExamListReponseDto {

	private final Long id;

	private final String title;

}
