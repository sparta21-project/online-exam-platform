package com.example.onlineexamplatform.domain.userCategory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.category.entity.Category;
import com.example.onlineexamplatform.domain.category.entity.CategoryType;
import com.example.onlineexamplatform.domain.category.repository.CategoryRepository;
import com.example.onlineexamplatform.domain.user.dto.UserProfileResponse;
import com.example.onlineexamplatform.domain.user.entity.User;
import com.example.onlineexamplatform.domain.user.repository.UserRepository;
import com.example.onlineexamplatform.domain.userCategory.dto.UserCategoryRequest;
import com.example.onlineexamplatform.domain.userCategory.dto.UserCategoryResponse;
import com.example.onlineexamplatform.domain.userCategory.entity.UserCategory;
import com.example.onlineexamplatform.domain.userCategory.repository.UserCategoryRepository;

import lombok.RequiredArgsConstructor;

/**
 * 이 서비스 클래스는 사용자(User)와 카테고리(Category) 간의 관계를 관리
 * 특정 사용자가 어떤 카테고리에 응시할 수 있는지를 등록, 조회, 삭제
 * 관리자가 특정 사용자의 응시 권한을 생성 및 삭제하여 관리
 * 사용자가 시험에 응시하기 전 해당 카테고리에 대한 권한이 있는지 확인
 */

@Service
@RequiredArgsConstructor
public class UserCategoryService {

	private final UserCategoryRepository userCategoryRepository;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;

	/**
	 * 관리자 전용
	 * 특정 사용자에게 특정 시험 카테고리 응시 권한을 부여
	 * 중복 권한 존재 시 예외 발생
	 */
	@Transactional
	public UserCategoryResponse create(Long userId, UserCategoryRequest request) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		CategoryType categoryType = CategoryType.valueOf(request.categoryType());
		Category category = categoryRepository.findByCategoryType(categoryType)
			.orElseThrow(() -> new ApiException(ErrorStatus.CATEGORY_NOT_FOUND));

		if (userCategoryRepository.findByUserIdAndCategory(userId, category).isPresent()) {
			throw new ApiException(ErrorStatus.DUPLICATE_USER_CATEGORY);
		}

		UserCategory saved = userCategoryRepository.save(new UserCategory(user, category));
		return new UserCategoryResponse(saved.getId(), user.getId(), category.getCategoryType());
	}

	/**
	 * 사용자 본인 또는 관리자
	 * 특정 사용자의 응시 권한 목록을 조회
	 */
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

	/**
	 * 관리자 전용
	 * 특정 사용자에게 부여된 응시 권한을 삭제
	 * 권한이 없거나 잘못된 ID일 경우 예외 발생
	 */
	@Transactional
	public void delete(Long userCategoryId) {
		UserCategory userCategory = userCategoryRepository.findById(userCategoryId)
			.orElseThrow(() -> new ApiException(ErrorStatus.USER_CATEGORY_NOT_FOUND));

		userCategoryRepository.delete(userCategory);
	}

	/**
	 * 관리자 전용
	 * 특정 응시권한을 보유한 사용자 목록 조회
	 * 권한이 없거나 잘못된 enum값일 경우 예외 발생
	 */
	@Transactional(readOnly = true)
	public List<UserProfileResponse> getUsersByCategory(String categoryTypeStr) {
		CategoryType categoryType;
		try {
			categoryType = CategoryType.valueOf(categoryTypeStr.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new ApiException(ErrorStatus.INVALID_CATEGORY_TYPE);
		}

		Category category = categoryRepository.findByCategoryType(categoryType)
			.orElseThrow(() -> new ApiException(ErrorStatus.CATEGORY_NOT_FOUND));

		List<UserCategory> userCategories = userCategoryRepository.findByCategory(category);

		return userCategories.stream()
			.map(uc -> {
				User user = uc.getUser();
				return new UserProfileResponse(
					user.getId(),
					user.getEmail(),
					user.getUsername(),
					user.getRole(),
					user.getPhoneNumber()
				);
			})
			.toList();
	}
}
