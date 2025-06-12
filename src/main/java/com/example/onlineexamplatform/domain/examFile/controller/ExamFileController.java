package com.example.onlineexamplatform.domain.examFile.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.common.scheduler.ExamFileScheduler;
import com.example.onlineexamplatform.domain.examFile.dto.response.ExamFileResponseDto;
import com.example.onlineexamplatform.domain.examFile.service.S3UploadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "ExamFile", description = "사용자(Admin)가 시험 생성 시 S3에 파일 업로드 하는 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/s3")
public class ExamFileController {

	private final S3UploadService s3UploadService;
	private final ExamFileScheduler examFileScheduler;

	@Operation(summary = "시험파일 업로드 API", description = "이미지를 MultipartFile로 받아 S3(파일데이터)와 DB(메타데이터)에 업로드 합니다")
	@Parameter(description = "시험파일 바이너리 데이터")
	@PostMapping("/upload")
	public ResponseEntity<ApiResponse<List<ExamFileResponseDto>>> s3Upload(
		@RequestPart(value = "image") List<MultipartFile> multipartFile) {
		List<ExamFileResponseDto> upload = s3UploadService.upload(multipartFile);
		return ApiResponse.onSuccess(SuccessStatus.SUCCESS_FILE_UPLOAD, upload);
	}

	@Operation(summary = "고아 시험파일 삭제 API", description = "스케줄러를 사용하여 exam_id가 Null인 ExamFile객체를 찾아 해당파일을 S3와 DB에서 삭제합니다.")
	@DeleteMapping("/delete/scheduler-test")
	public String deleteExamFile() {
		try {
			examFileScheduler.deleteOrphanExamFile(); // 스케줄러 메서드를 수동 호출
			return "고아 이미지 삭제 작업이 완료되었습니다.";
		} catch (Exception e) {
			return "고아 이미지 삭제 작업에 실패했습니다: " + e.getMessage();
		}
	}

}
