package com.example.onlineexamplatform.domain.examFile.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.onlineexamplatform.common.awsS3.S3UploadService;
import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.domain.examFile.service.ExamFileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/s3")
public class ExamFileController {

	private final ExamFileService examFileService;
	private final S3UploadService s3UploadService;

	@PostMapping("/upload")
	public ResponseEntity<ApiResponse<List<String>>> s3Upload(
		@RequestPart(value = "image") List<MultipartFile> multipartFile) {
		List<String> upload = s3UploadService.upload(multipartFile);
		return ApiResponse.onSuccess(SuccessStatus.SUCCESS_FILE_UPLOAD, upload);
	}

}
