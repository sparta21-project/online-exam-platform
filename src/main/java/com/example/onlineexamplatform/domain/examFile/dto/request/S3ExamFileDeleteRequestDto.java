package com.example.onlineexamplatform.domain.examFile.dto.request;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class S3ExamFileDeleteRequestDto {

	private final List<String> imagePaths;

}
