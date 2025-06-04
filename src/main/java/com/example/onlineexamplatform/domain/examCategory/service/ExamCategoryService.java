package com.example.onlineexamplatform.domain.examCategory.service;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.category.entity.Category;
import com.example.onlineexamplatform.domain.category.repository.CategoryRepository;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.exam.repository.ExamRepository;
import com.example.onlineexamplatform.domain.examCategory.entity.ExamCategory;
import com.example.onlineexamplatform.domain.examCategory.repository.ExamCategoryRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamCategoryService {

    private final ExamRepository examRepository;

    private final CategoryRepository categoryRepository;

    private final ExamCategoryRepository examCategoryRepository;

    public void create(Long examId, Long categoryId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        ExamCategory examCategory = new ExamCategory(exam, category);

        examCategoryRepository.save(examCategory);
    }
}
