package com.example.onlineexamplatform.domain.examcategory.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlineexamplatform.domain.examcategory.dto.ExamServiceRequestDto;
import com.example.onlineexamplatform.domain.examcategory.dto.ExamServiceResponseDto;
import com.example.onlineexamplatform.domain.examcategory.service.ExamCategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exam-categories")
public class ExamCategoryController {
	private final ExamCategoryService examCategoryService;

	//  카테고리 생성
	@PostMapping
	public ResponseEntity<ExamServiceResponseDto> create(@RequestBody @Validated ExamServiceRequestDto requestDto) {
		ExamServiceResponseDto response = examCategoryService.create(requestDto);
		return ResponseEntity.ok(response);
	}

	//  특정 시험(examId)의 카테고리 조회
	@GetMapping("/{examId}")
	public ResponseEntity<List<ExamServiceResponseDto>> getByExamId(@PathVariable Long examId) {
		List<ExamServiceResponseDto> categories = examCategoryService.findByExamId(examId);
		return ResponseEntity.ok(categories);
	}

	//  카테고리 수정
	@PutMapping
	public ResponseEntity<ExamServiceResponseDto> update(@RequestBody @Validated ExamServiceRequestDto requestDto) {
		ExamServiceResponseDto response = examCategoryService.update(requestDto);
		return ResponseEntity.ok(response);
	}

	//  카테고리 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		examCategoryService.delete(id);
		return ResponseEntity.noContent().build();
	}
}

