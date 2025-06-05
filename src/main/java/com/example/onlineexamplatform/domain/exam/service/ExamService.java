package com.example.onlineexamplatform.domain.exam.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.exam.dto.request.CreateExamRequestDto;
import com.example.onlineexamplatform.domain.exam.dto.request.UpdateExamRequestDto;
import com.example.onlineexamplatform.domain.exam.dto.response.ExamResponseDto;
import com.example.onlineexamplatform.domain.exam.dto.response.GetExamListResponseDto;
import com.example.onlineexamplatform.domain.exam.dto.response.UpdateExamResponseDto;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.exam.repository.ExamRepository;
import com.example.onlineexamplatform.domain.user.entity.User;
import com.example.onlineexamplatform.domain.user.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ExamService {

	private final ExamRepository examRepository;
	private final UserRepository userRepository;

	@Transactional
	public ExamResponseDto createExam(CreateExamRequestDto requestDto, Long userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		Exam exam = Exam.builder()
				.user(user)
				.title(requestDto.getTitle())
				.description(requestDto.getDescription())
				.startTime(requestDto.getStartTime())
				.endTime(requestDto.getEndTime())
				.build();

		examRepository.save(exam);
		return ExamResponseDto.from(exam);
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
