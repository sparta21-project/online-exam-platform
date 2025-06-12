package com.example.onlineexamplatform.domain.exam.service;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.onlineexamplatform.common.awsS3.S3UploadService;
import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.exam.dto.request.CreateExamRequestDto;
import com.example.onlineexamplatform.domain.exam.dto.request.UpdateExamRequestDto;
import com.example.onlineexamplatform.domain.exam.dto.response.ExamResponseDto;
import com.example.onlineexamplatform.domain.exam.dto.response.GetExamListResponseDto;
import com.example.onlineexamplatform.domain.exam.dto.response.UpdateExamResponseDto;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.exam.repository.ExamRepository;
import com.example.onlineexamplatform.domain.examFile.dto.response.ExamFileResponseDto;
import com.example.onlineexamplatform.domain.examFile.entity.ExamFile;
import com.example.onlineexamplatform.domain.examFile.service.ExamFileService;
import com.example.onlineexamplatform.domain.user.entity.User;
import com.example.onlineexamplatform.domain.user.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ExamService {

	private final ExamRepository examRepository;
	private final UserRepository userRepository;
	private final S3UploadService s3UploadService;
	private final ExamFileService examFileService;

	@Transactional
	public ExamResponseDto<ExamFileResponseDto> createExam(CreateExamRequestDto requestDto, Long userId) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		Exam exam = examRepository.save(Exam.builder()
			.user(user)
			.title(requestDto.getTitle())
			.description(requestDto.getDescription())
			.totalQuestionsNum(requestDto.getTotalQuestionsNum())
			.startTime(requestDto.getStartTime())
			.endTime(requestDto.getEndTime())
			.build());

		List<ExamFile> examFiles = Collections.emptyList();

		// 이미지 매핑 (이미지가 존재할 경우에만 처리)
		if (requestDto.getExamFileIds() != null && !requestDto.getExamFileIds().isEmpty()) { // 이미지 목록이 비어있지 않으면 처리
			examFiles = s3UploadService.findAllByImageId(requestDto.getExamFileIds()); // 이미지 목록 조회
			examFiles.stream()
				.filter(examFile -> examFile.getExam() == null) // 시험과 연결되지 않은 이미지만 필터링
				.forEach(examFile -> examFile.assignExam(exam)); // 해당 이미지를 시험에 할당
		}

		return ExamResponseDto.of(
			exam,
			examFiles.stream()
				.map(ExamFileResponseDto::of)
				.toList()
		);
	}

	// TODO 캐시 적용
	public Page<GetExamListResponseDto> getExamList(Pageable pageable) {
		return examRepository.findAll(pageable)
			.map(GetExamListResponseDto::toDto);
	}

	// TODO 레디스 적용 적용
	public Page<GetExamListResponseDto> searchExamByTitle(Pageable pageable, String examTitle) {
		return examRepository.findByTitle(pageable, examTitle)
			.map(GetExamListResponseDto::toDto);
	}

	// TODO 캐시 적용
	public ExamResponseDto findExamById(Long examId) {

		Exam exam = examRepository.findByIdOrElseThrow(examId);

		return ExamResponseDto.from(exam);
	}

	@Transactional
	public UpdateExamResponseDto updateExamById(Long examId, @Valid UpdateExamRequestDto requestDto) {

		Exam exam = examRepository.findByIdOrElseThrow(examId);

		exam.updateExam(requestDto);

		return UpdateExamResponseDto.from(exam);
	}

	@Transactional
	public void deleteExamById(Long examId) {

		examRepository.findByIdOrElseThrow(examId);

		examRepository.deleteById(examId);
	}
}
