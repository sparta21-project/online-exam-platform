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
	LOGOUT_SUCCESS(HttpStatus.OK, "1003", "로그아웃 성공"),
	GETMYINFO_SUCCESS(HttpStatus.OK, "1004", "내 정보 불러오기 성공"),
	UPDATE_PROFILE_SUCCESS(HttpStatus.OK, "1005", "프로필 업데이트 성공."),
	DELETE_SUCCESS(HttpStatus.OK, "1006", "회원 탈퇴 성공, 다시 로그인 해주세요."),
	GET_REVIEW_SUCCESS(HttpStatus.OK, "1007", "리뷰 불러오기 성공."),
	GET_RECOMMEND_SUCCESS(HttpStatus.OK, "1008", "추천목록 불러오기 성공."),
	REISSUE_SUCCESS(HttpStatus.OK, "1009", "어세스 토큰 재발급 성공."),
	UPDATE_PASSWORD(HttpStatus.OK, "1010", "비밀번호 변경 성공"),
	USER_GET_ALL_SUCCESS(HttpStatus.OK, "1011", "전체 사용자 목록 조회 성공"),
	USER_SEARCH_SUCCESS(HttpStatus.OK, "1012", "사용자 검색 결과 조회 성공"),
	GET_SMS_LIST_SUCCESS(HttpStatus.OK, "1013", "SMS 알림 조회 성공"),

	// 2000: 시험 성공 코드
	FIND_EXAM(HttpStatus.OK, "2000", "시험 조회 성공."),
	CREATE_EXAM(HttpStatus.CREATED, "2001", "시험 생성 성공."),
	UPDATE_EXAM(HttpStatus.OK, "2002", "시험 수정 성공."),
	DELETE_EXAM(HttpStatus.OK, "2003", "시험 삭제 성공."),

	// 3000 : 유저답안 작성 성공 코드
	SAVE_USER_ANSWER_SUCCESS(HttpStatus.CREATED, "3001", "제출용 답안 작성 성공."),
	GRADE_ANSWER_SHEET_SUCCESS(HttpStatus.OK, "3002", "답안지가 일괄 채점되었습니다."),
	UPDATE_ANSWER_SHEET_STATUS_SUCCESS(HttpStatus.OK, "3003", "답안지의 제출 상태가 일괄 변경되었습니다."),

	// 4000 : S3 성공 코드
	SUCCESS_FILE_UPLOAD(HttpStatus.OK, "4001", "파일 업로드 성공"),
	SUCCESS_FILE_DELETE(HttpStatus.NO_CONTENT, "4002", "파일 삭제 성공"),

	// 4000 : 시험답안 성공 코드
	SAVE_EXAM_ANSWER_SUCCESS(HttpStatus.OK, "4001", "관리자용 답안 생성 성공."),
	UPDATE_EXAM_ANSWER_SUCCESS(HttpStatus.OK, "4002", "관리자용 답안 수정 성공"),
	GET_EXAM_ANSWER_SUCCESS(HttpStatus.OK, "4003", "관리자용 답안 조회 성공"),
	DELETE_EXAM_ANSWER_SUCCESS(HttpStatus.OK, "4004", "관리자용 답안 삭제 성공"),

	// 5000 : 시험 권한 성공 코드
	CREATE_EXAM_CATEGORY(HttpStatus.CREATED, "5001", "시험 권한 생성 성공"),
	GET_EXAM_CATEGORY(HttpStatus.OK, "5002", "시험 권한 조회 성공"),
	DELETE_EXAM_CATEGORY(HttpStatus.OK, "5003", "시험 권한 삭제 성공"),

	// 6000 : 리뷰 성공 코드
	CREATE_REVIEW(HttpStatus.CREATED, "6001", "리뷰 등록이 완료되었습니다."),
	MODIFY_REVIEW_SUCCESS(HttpStatus.OK, "6002", "리뷰 수정이 완료되었습니다."),
	GET_REVIEWS_SUCCESS(HttpStatus.OK, "6003", "리뷰 조회가 완료되었습니다."),
	REVIEW_DELETE_SUCCESS(HttpStatus.OK, "6006", "리뷰 삭제 성공, 다시 로그인 해주세요."),

	// 7000 : 검색 성공 코드
	SEARCH_SUCCESS(HttpStatus.OK, "7001", "검색에 성공했습니다."),
	POPULAR_SEARCH_SUCCESS(HttpStatus.OK, "7002", "인기 검색어 호출에 성공했습니다."),

	// 8000 : 답안지 성공 코드
	CREATE_ANSWER_SHEET_SUCCESS(HttpStatus.CREATED, "8001", "답안지가 생성되었습니다."),
	SAVE_ANSWERS_SUCCESS(HttpStatus.OK, "8002", "답안이 저장되었습니다."),
	GET_ANSWERS_SUCCESS(HttpStatus.OK, "8003", "답안 조회가 완료되었습니다."),
	DELETE_ANSWER_SHEET_SUCCESS(HttpStatus.NOT_FOUND, "8004", "답안지가 삭제되었습니다."),
	SUBMIT_ANSWER_SUCCESS(HttpStatus.OK, "8005", "답안이 제출되었습니다."),
	GET_APPLICANTS_SUCCESS(HttpStatus.OK, "8006", "응시자 조회가 완료되었습니다."),

	// 9000 : 응시 권한 성공 코드
	USERCATEGORY_CREATE_SUCCESS(HttpStatus.CREATED, "9001", "응시 권한 생성에 성공했습니다."),
	USERCATEGORY_GET_SUCCESS(HttpStatus.OK, "9002", "응시 권한 목록 조회에 성공했습니다."),
	USERCATEGORY_DELETE_SUCCESS(HttpStatus.OK, "9003", "응시 권한 삭제에 성공했습니다."),
	USERCATEGORY_GET_USERS_BY_CATEGORY_SUCCESS(HttpStatus.OK, "9004", "응시 권한 보유 사용자 목록 조회 성공"),
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
