package com.example.onlineexamplatform.domain.examFile.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExamFileS3PreSignedURLDto {

	private final String fileName;

	private final String presignedUrl;

}
