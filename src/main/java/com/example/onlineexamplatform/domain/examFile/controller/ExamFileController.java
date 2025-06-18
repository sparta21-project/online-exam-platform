package com.example.onlineexamplatform.domain.examFile.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.config.session.CheckAuth;
import com.example.onlineexamplatform.domain.examFile.dto.response.ExamFileResponseDto;
import com.example.onlineexamplatform.domain.examFile.scheduler.ExamFileScheduler;
import com.example.onlineexamplatform.domain.examFile.service.S3UploadService;
import com.example.onlineexamplatform.domain.user.entity.Role;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "03-ExamFile", description = "사용자(Admin)가 시험 생성 시 시험 파일을 S3에 업로드하는 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/s3")
public class ExamFileController {

	private final S3UploadService s3UploadService;
	private final ExamFileScheduler examFileScheduler;

	@Operation(summary = "시험파일 S3 업로드 API", description = "시험 생성 전 시험파일을 S3에 업로드합니다.")
	@CheckAuth(Role.ADMIN)
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<List<ExamFileResponseDto>>> s3Upload(
		@Parameter(description = "업로드할 시험파일의 바이너리") @RequestPart(value = "examFile") List<MultipartFile> examFiles) {

		List<ExamFileResponseDto> upload = s3UploadService.upload(examFiles);

		return ApiResponse.onSuccess(SuccessStatus.SUCCESS_FILE_UPLOAD, upload);
	}

}
