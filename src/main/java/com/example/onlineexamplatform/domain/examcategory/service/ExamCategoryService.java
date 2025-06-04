package com.example.onlineexamplatform.domain.examcategory.service;

import com.example.onlineexamplatform.domain.category.entity.Category;
import com.example.onlineexamplatform.domain.category.repository.CategoryRepository;
import com.example.onlineexamplatform.domain.examcategory.dto.ExamCreateRequestDto;
import com.example.onlineexamplatform.domain.examcategory.dto.ExamServiceResponseDto;
import com.example.onlineexamplatform.domain.examcategory.dto.ExamUpdateRequestDto;
import com.example.onlineexamplatform.domain.examcategory.entity.ExamCategory;
import com.example.onlineexamplatform.domain.examcategory.repository.ExamCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamCategoryService {
    private final ExamCategoryRepository examCategoryRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ExamServiceResponseDto create(ExamCreateRequestDto request) {
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        ExamCategory examCategory = examCategoryRepository.save(
                new ExamCategory(
                        request.getExamId(),
                        category
                )
        );

        return new ExamServiceResponseDto(
                examCategory.getId(),
                examCategory.getExamId(),
                examCategory.getCategory().getCategoryType()
        );
    }

    @Transactional(readOnly = true)
    public List<ExamServiceResponseDto> findByExamIdAndCategoryId(Long examId, Long examCategoryId) {
        return examCategoryRepository.findByExamIdAndCategoryId(examId, examCategoryId)
                .stream()
                .map(examCategory -> new ExamServiceResponseDto(
                        examCategory.getId(),
                        examCategory.getExamId(),
                        examCategory.getCategory().getCategoryType()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public ExamServiceResponseDto update(
            Long examCategoryId,
            ExamUpdateRequestDto request) {
        ExamCategory examCategory = examCategoryRepository.findById(examCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
        examCategory.update(
                request.getExamId(),
                category
        );

        return new ExamServiceResponseDto(
                examCategory.getId(),
                examCategory.getExamId(),
                examCategory.getCategory().getCategoryType()
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

