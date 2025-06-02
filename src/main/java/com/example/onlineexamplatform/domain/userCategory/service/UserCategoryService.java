package com.example.onlineexamplatform.domain.userCategory.service;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.category.entity.Category;
import com.example.onlineexamplatform.domain.category.repository.CategoryRepository;
import com.example.onlineexamplatform.domain.user.entity.User;
import com.example.onlineexamplatform.domain.user.repository.UserRepository;
import com.example.onlineexamplatform.domain.userCategory.dto.UserCategoryRequest;
import com.example.onlineexamplatform.domain.userCategory.dto.UserCategoryResponse;
import com.example.onlineexamplatform.domain.userCategory.entity.UserCategory;
import com.example.onlineexamplatform.domain.userCategory.repository.UserCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

/**
 * 이 서비스 클래스는 사용자(User)와 카테고리(Category) 간의 관계를 관리합니다.
 * 특정 사용자가 어떤 시험 카테고리에 응시할 수 있는지를 등록, 조회, 삭제하는 기능을 제공합니다.
 * 관리자가 특정 사용자의 응시 권한을 생성 및 삭제하여 관리
 * 사용자가 시험에 응시하기 전 해당 카테고리에 대한 권한이 있는지 확인
 */

public class UserCategoryService {

	private final UserCategoryRepository userCategoryRepository;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;

	@Transactional
	public UserCategoryResponse create(Long userId, UserCategoryRequest request) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		Category category = categoryRepository.findByCategoryType(request.categoryType())
				.orElseThrow(() -> new ApiException(ErrorStatus.CATEGORY_NOT_FOUND));

		if (userCategoryRepository.findByUserIdAndCategory(userId, category).isPresent()) {
			throw new ApiException(ErrorStatus.DUPLICATE_USER_CATEGORY);
		}

		UserCategory saved = userCategoryRepository.save(new UserCategory(user, category));

		return new UserCategoryResponse(saved.getId(), user.getId(), category.getCategoryType());
	}

	@Transactional(readOnly = true)
	public List<UserCategoryResponse> getByUser(Long userId) {
		return userCategoryRepository.findByUserId(userId).stream()
				.map(uc -> new UserCategoryResponse(
						uc.getId(),
						uc.getUser().getId(),
						uc.getCategory().getCategoryType()
				))
				.toList();
	}

	@Transactional
	public void delete(Long userId, Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ApiException(ErrorStatus.CATEGORY_NOT_FOUND));

		UserCategory userCategory = userCategoryRepository.findByUserIdAndCategory(userId, category)
				.orElseThrow(() -> new ApiException(ErrorStatus.USER_CATEGORY_NOT_FOUND));

		userCategoryRepository.delete(userCategory);
	}
}
