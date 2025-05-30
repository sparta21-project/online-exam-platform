package com.example.onlineexamplatform.domain.exam.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.onlineexamplatform.domain.exam.dto.request.CreateExamRequestDto;
import com.example.onlineexamplatform.domain.exam.dto.request.UpdateExamRequestDto;
import com.example.onlineexamplatform.domain.exam.dto.response.ExamResponseDto;
import com.example.onlineexamplatform.domain.exam.dto.response.GetExamListReponseDto;
import com.example.onlineexamplatform.domain.exam.dto.response.UpdateExamResponseDto;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.exam.repository.ExamRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ExamService {

	private final ExamRepository examRepository;

	@Transactional
	public ExamResponseDto createExam(CreateExamRequestDto requestDto, Long userId) {

		// TODO 유저 정보 조회 로직 작성

		Exam exam = Exam.builder()
			// .user(user) // 유저 부분 구현 시 활성화
			.title(requestDto.getTitle())
			.description(requestDto.getDescription())
			.filePath(requestDto.getFilePath())
			.startTime(requestDto.getStartTime())
			.endTime(requestDto.getEndTime())
			.build();

		examRepository.save(exam);
		return ExamResponseDto.from(exam);
	}

	// TODO 캐시 적용
	public List<GetExamListReponseDto> getExamList() {
		return examRepository.findAll()
			.stream()
			.map(GetExamListReponseDto::toDto)
			.toList();
	}

	// TODO 레디스 적용 적용
	public List<GetExamListReponseDto> searchExamByTitle(String examTile) {
		return examRepository.findByExamTile(examTile)
			.stream()
			.map(GetExamListReponseDto::toDto)
			.toList();
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
}
