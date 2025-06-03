package com.example.onlineexamplatform.common.code;

import org.springframework.http.HttpStatus;

import com.example.onlineexamplatform.common.dto.ReasonDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessStatus implements BaseCode {
	// 1000: 성공 코드
	SIGNUP_SUCCESS(HttpStatus.CREATED, "1001", "회원가입이 완료되었습니다."),
	LOGIN_SUCCESS(HttpStatus.OK, "1002", "로그인 성공"),

	// 2000: 시험 성공 코드
	FIND_EXAM(HttpStatus.OK, "2000", "시험 조회 성공."),
	CREATE_EXAM(HttpStatus.CREATED, "2001", "시험 생성 성공."),
	UPDATE_EXAM(HttpStatus.OK, "2002", "시험 수정 성공."),
	DELETE_EXAM(HttpStatus.OK, "2003", "시험 삭제 성공."),

	// 3000 : 유저답안 작성 성공 코드
	SAVE_ANSWER_SUCCESS(HttpStatus.CREATED, "3001", "제출용 답안 작성 성공."),

	// 4000 : 시험답안 작성 성공 코드
	SAVE_EXAM_ANSWER_SUCCESS(HttpStatus.OK, "4001", "관리자용 답안 생성 성공."),
	UPDATE_EXAM_ANSWER_SUCCESS(HttpStatus.OK, "4002", "관리자용 답안 수정 성공"),

	// 5000 : 어드민 성공 코드
	BOOK_ADD_ACCEPT_SUCCESS(HttpStatus.OK, "5001", "도서 추가 요청 승인 성공"),
	REVIEW_PIN_SUCCESS(HttpStatus.OK, "5002", "리뷰 상단 고정 성공"),
	REVIEW_UNPIN_SUCCESS(HttpStatus.OK, "5003", "리뷰 상단 고정 해제 성공"),

	// 6000 : 리뷰 성공 코드
	CREATE_REVIEW(HttpStatus.CREATED, "6001", "리뷰 등록이 완료되었습니다."),
	MODIFY_REVIEW_SUCCESS(HttpStatus.OK, "6002", "리뷰 수정이 완료되었습니다."),
	GET_REVIEWS_SUCCESS(HttpStatus.OK, "6003", "리뷰 조회가 완료되었습니다."),
	REVIEW_DELETE_SUCCESS(HttpStatus.OK, "6006", "리뷰 삭제 성공, 다시 로그인 해주세요."),

	// 7000 : 검색 성공 코드
	SEARCH_SUCCESS(HttpStatus.OK, "7001", "검색에 성공했습니다."),
	POPULAR_SEARCH_SUCCESS(HttpStatus.OK, "7002", "인기 검색어 호출에 성공했습니다."),

	// 9000 : 응시 권한 성공 코드
	USERCATEGORY_CREATE_SUCCESS(HttpStatus.CREATED, "9001", "응시 권한 생성에 성공했습니다."),
	USERCATEGORY_GET_SUCCESS(HttpStatus.OK, "9002", "응시 권한 목록 조회에 성공했습니다."),
	USERCATEGORY_DELETE_SUCCESS(HttpStatus.OK, "9003", "응시 권한 삭제에 성공했습니다.")
	;

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

	public ReasonDto getReason() {
		return ReasonDto.builder()
			.isSuccess(true)
			.code(code)
			.message(message)
			.build();
	}

	public ReasonDto getReasonHttpStatus() {
		return ReasonDto.builder()
			.isSuccess(true)
			.httpStatus(httpStatus)
			.code(code)
			.message(message)
			.build();
	}
}
