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
	public ExamResponseDto createExam(CreateExamRequestDto requestDto) {

		Exam exam = Exam.builder()
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
