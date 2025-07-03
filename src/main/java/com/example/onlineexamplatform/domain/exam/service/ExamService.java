package com.example.onlineexamplatform.domain.exam.service;

import static com.example.onlineexamplatform.domain.answerSheet.enums.AnswerSheetStatus.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.answerSheet.entity.AnswerSheet;
import com.example.onlineexamplatform.domain.answerSheet.repository.AnswerSheetRepository;
import com.example.onlineexamplatform.domain.exam.dto.request.CreateExamRequestDto;
import com.example.onlineexamplatform.domain.exam.dto.request.UpdateExamRequestDto;
import com.example.onlineexamplatform.domain.exam.dto.response.ExamDetailResponseDto;
import com.example.onlineexamplatform.domain.exam.dto.response.ExamResponseDto;
import com.example.onlineexamplatform.domain.exam.dto.response.GetExamListResponseDto;
import com.example.onlineexamplatform.domain.exam.dto.response.UpdateExamResponseDto;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.exam.repository.ExamRepository;
import com.example.onlineexamplatform.domain.examCategory.entity.ExamCategory;
import com.example.onlineexamplatform.domain.examCategory.repository.ExamCategoryRepository;
import com.example.onlineexamplatform.domain.examFile.dto.response.ExamFileResponseDto;
import com.example.onlineexamplatform.domain.examFile.dto.response.ExamFileS3PreSignedURLDto;
import com.example.onlineexamplatform.domain.examFile.entity.ExamFile;
import com.example.onlineexamplatform.domain.examFile.repository.ExamFileRepository;
import com.example.onlineexamplatform.domain.examFile.service.S3UploadService;
import com.example.onlineexamplatform.domain.user.entity.User;
import com.example.onlineexamplatform.domain.user.repository.UserRepository;
import com.example.onlineexamplatform.domain.userCategory.entity.UserCategory;
import com.example.onlineexamplatform.domain.userCategory.repository.UserCategoryRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ExamService {

	private final ExamRepository examRepository;
	private final UserRepository userRepository;
	private final S3UploadService s3UploadService;
	private final ExamFileRepository examFileRepository;
	private final UserCategoryRepository userCategoryRepository;
	private final ExamCategoryRepository examCategoryRepository;
	private final AnswerSheetRepository answerSheetRepository;

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
			.remainUsers(requestDto.getRemainUsers())
			.build());

		List<ExamFile> examFiles = Collections.emptyList();

		// ExamFile 매핑 (ExamFile이 존재할 경우에만 처리)
		if (requestDto.getExamFileIds() != null && !requestDto.getExamFileIds().isEmpty()) { // ExamFile 목록이 비어있지 않으면 처리

			examFiles = s3UploadService.findAllByExamFileId(requestDto.getExamFileIds()); // ExamFile 목록 조회
			// 이미 exam이 할당된 파일이 하나라도 있으면 예외 발생
			boolean hasAlreadyAssigned = examFiles.stream()
				.anyMatch(examFile -> examFile.getExam() != null);

			if (hasAlreadyAssigned) {
				throw new ApiException(ErrorStatus.FILE_ALREADY_LINKED); // 예외 처리
			}
			examFiles.forEach(examFile -> examFile.assignExam(exam)); // 해당 ExamFile을 시험에 할당
		}

		return ExamResponseDto.of(
			exam,
			examFiles.stream()
				.map(ExamFileResponseDto::of)
				.toList()
		);
	}

	@Transactional(readOnly = true)
	public Page<GetExamListResponseDto> getExamList(Pageable pageable) {
		return examRepository.findAll(pageable)
			.map(GetExamListResponseDto::toDto);
	}

	@Transactional(readOnly = true)
	public Page<GetExamListResponseDto> searchExamByTitle(Pageable pageable, String examTitle) {
		return examRepository.findByTitle(pageable, examTitle)
			.map(GetExamListResponseDto::toDto);
	}

	@Transactional(readOnly = true)
	public ExamResponseDto<ExamFileResponseDto> findExamById(Long examId) {

		Exam exam = examRepository.findByIdOrElseThrow(examId);

		List<ExamFile> examFiles = examFileRepository.findByExamId(examId);

		List<ExamFileResponseDto> fileResponseDtos = examFiles.stream()
			.map(ExamFileResponseDto::of)
			.toList();

		return ExamResponseDto.of(exam, fileResponseDtos);
	}

	@Transactional(readOnly = true)
	public ExamDetailResponseDto getExamDetail(Long userId, Long examId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));
		Exam exam = examRepository.findByIdOrElseThrow(examId);

		List<UserCategory> userCategories = userCategoryRepository.findByUserId(userId);
		List<ExamCategory> examCategories = examCategoryRepository.findAllByExamId(examId);
		List<ExamFile> examFiles = examFileRepository.findByExamId(examId);

		if (LocalDateTime.now().isBefore(exam.getStartTime())) {
			throw new ApiException(ErrorStatus.EXAM_NOT_STARTED);
		}

		boolean hasCategory = examCategories.isEmpty();
		//카테고리가 없으면 누구든 응시 가능
		for (ExamCategory examCategory : examCategories) {
			for (UserCategory userCategory : userCategories) {
				if (examCategory.getCategory().getId().equals(userCategory.getCategory().getId())) {
					hasCategory = true;
					break;
				}
			}
			if (hasCategory)
				break;
		}

		if (hasCategory) {
			AnswerSheet answerSheet = new AnswerSheet(exam, user, STARTED);
			answerSheetRepository.save(answerSheet);
		} else {
			throw new ApiException(ErrorStatus.CATEGORY_NOT_MATCHED);
		}

		boolean anyMatch = userCategories.stream().anyMatch(userCategory ->
			!userCategory.getUser().getId().equals(userId));
		if (anyMatch) {
			throw new ApiException(ErrorStatus.USER_CATEGORY_NOT_FOUND);
		}

		List<ExamFileS3PreSignedURLDto> examFileS3PreSignedURLs = examFiles.stream()
			.map(examFile -> new ExamFileS3PreSignedURLDto(
				examFile.getFileName(),
				s3UploadService.createPresignedUrl(examFile.getPath(), exam.getEndTime())
			))
			.toList();

		return ExamDetailResponseDto.of(exam, examFileS3PreSignedURLs);

	}

	@Transactional
	public UpdateExamResponseDto updateExamById(Long userId, Long examId, @Valid UpdateExamRequestDto requestDto) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		Exam exam = examRepository.findByIdOrElseThrow(examId);

		if (!user.getId().equals(exam.getUser().getId())) {
			throw new ApiException(ErrorStatus.FORBIDDEN);
		}

		exam.updateExam(requestDto);

		return UpdateExamResponseDto.from(exam);
	}

	@Transactional
	public void deleteExamById(Long userId, Long examId) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		Exam exam = examRepository.findByIdOrElseThrow(examId);

		if (!user.getId().equals(exam.getUser().getId())) {
			throw new ApiException(ErrorStatus.FORBIDDEN);
		}

		//S3 + DB 업로드 파일 데이터 삭제
		s3UploadService.deleteFilesByExam(exam);

		// 시험 삭제
		examRepository.delete(exam);
	}
}
