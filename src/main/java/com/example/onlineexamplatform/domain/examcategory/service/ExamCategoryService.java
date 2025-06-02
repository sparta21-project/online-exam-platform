package com.example.onlineexamplatform.domain.examcategory.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.onlineexamplatform.domain.examcategory.dto.ExamServiceRequestDto;
import com.example.onlineexamplatform.domain.examcategory.dto.ExamServiceResponseDto;
import com.example.onlineexamplatform.domain.examcategory.entity.ExamCategoryEntity;
import com.example.onlineexamplatform.domain.examcategory.repository.ExamCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExamCategoryService {
	private final ExamCategoryRepository examCategoryRepository;

	@Transactional
	public ExamServiceResponseDto create(ExamServiceRequestDto request) {
		ExamCategoryEntity examCategoryEntity = new ExamCategoryEntity(
			request.getExamId(),
			request.getExamCategory()
		);

		ExamCategoryEntity saved = examCategoryRepository.save(examCategoryEntity);

		return new ExamServiceResponseDto(
			saved.getId(),
			saved.getExamId(),
			saved.getCategoryType()
		);
	}

	@Transactional(readOnly = true)
	public List<ExamServiceResponseDto> findByExamId(Long examId) {
		return examCategoryRepository.findByExamId(examId).stream()
			.map(cat -> new ExamServiceResponseDto(
				cat.getId(),
				cat.getExamId(),
				cat.getCategoryType()
			)).collect(Collectors.toList());
	}

	@Transactional
	public ExamServiceResponseDto update(ExamServiceRequestDto request) {
		ExamCategoryEntity examCategoryEntity = examCategoryRepository.findById(request.getId())
			.orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

		examCategoryEntity.update(
			request.getId(),
			request.getExamId(),
			request.getExamCategory()
		);

		return new ExamServiceResponseDto(
			examCategoryEntity.getId(),
			examCategoryEntity.getExamId(),
			examCategoryEntity.getCategoryType()
		);
	}

	@Transactional
	public void delete(Long id) {
		if (!examCategoryRepository.existsById(id)) {
			throw new IllegalArgumentException("존재하지 않는 ID입니다.");
		}
		examCategoryRepository.deleteById(id);
	}
}

