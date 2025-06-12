package com.example.onlineexamplatform.domain.examFile.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.onlineexamplatform.common.awsS3.S3UploadService;
import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.domain.examFile.dto.request.S3ExamFileDeleteRequestDto;
import com.example.onlineexamplatform.domain.examFile.dto.response.ExamFileResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/s3")
public class ExamFileController {

	private final S3UploadService s3UploadService;

	@PostMapping("/upload")
	public ResponseEntity<ApiResponse<List<ExamFileResponseDto>>> s3Upload(
		@RequestPart(value = "image") List<MultipartFile> multipartFile) {
		List<ExamFileResponseDto> upload = s3UploadService.upload(multipartFile);
		return ApiResponse.onSuccess(SuccessStatus.SUCCESS_FILE_UPLOAD, upload);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<ApiResponse<String>> s3Delete(@RequestBody S3ExamFileDeleteRequestDto requestDto) {
		s3UploadService.delete(requestDto.getImagePaths());
		return ApiResponse.onSuccess(SuccessStatus.SUCCESS_FILE_DELETE);
	}

}
