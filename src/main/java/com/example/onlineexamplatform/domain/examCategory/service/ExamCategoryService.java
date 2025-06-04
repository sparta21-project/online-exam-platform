package com.example.onlineexamplatform.domain.examCategory.service;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.category.entity.Category;
import com.example.onlineexamplatform.domain.category.repository.CategoryRepository;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.exam.repository.ExamRepository;
import com.example.onlineexamplatform.domain.examCategory.dto.ExamCategoryCreateDto;
import com.example.onlineexamplatform.domain.examCategory.dto.ExamCategoryResponseDto;
import com.example.onlineexamplatform.domain.examCategory.entity.ExamCategory;
import com.example.onlineexamplatform.domain.examCategory.repository.ExamCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamCategoryService {

    private final ExamRepository examRepository;

    private final CategoryRepository categoryRepository;

    private final ExamCategoryRepository examCategoryRepository;

    @Transactional
    public void create(List<ExamCategoryCreateDto> examCategories) {
        for(ExamCategoryCreateDto dto : examCategories) {
            Exam exam = examRepository.findById(dto.getExamId())
                    .orElseThrow(() -> new ApiException(ErrorStatus.EXAM_NOT_FOUND));

            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ApiException(ErrorStatus.CATEGORY_NOT_FOUND));

            ExamCategory examCategory = new ExamCategory(exam, category);

            examCategoryRepository.save(examCategory);
        }
    }

    @Transactional(readOnly = true)
    public List<ExamCategoryResponseDto> getByExamId(Long examId) {
        examRepository.findById(examId).orElseThrow(() -> new ApiException(ErrorStatus.EXAM_NOT_FOUND));

        return examCategoryRepository.findAllByExamId(examId)
                .stream()
                .map(ExamCategoryResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExamCategoryResponseDto> getByCategoryId(Long categoryId) {
        categoryRepository.findById(categoryId).orElseThrow(() -> new ApiException(ErrorStatus.CATEGORY_NOT_FOUND));

        return examCategoryRepository.findAllByCategoryId(categoryId)
                .stream()
                .map(ExamCategoryResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long examCategoryId) {
        ExamCategory examCategory = examCategoryRepository.findById(examCategoryId)
                .orElseThrow(() -> new ApiException(ErrorStatus.CATEGORY_NOT_FOUND));

        examCategoryRepository.delete(examCategory);
    }
}
