package com.example.onlineexamplatform.domain.exam.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.onlineexamplatform.domain.exam.dto.request.CreateExamRequestDto;
import com.example.onlineexamplatform.domain.exam.dto.response.ExamResponseDto;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.exam.repository.ExamRepository;

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
}
