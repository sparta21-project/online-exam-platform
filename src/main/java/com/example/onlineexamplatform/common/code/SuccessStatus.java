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

	// 8000 : 시험답안 작성 성공 코드
	SAVE_EXAM_ANSWER_SUCCESS(HttpStatus.OK, "8001", "관리자용 답안 생성 성공."),
	UPDATE_EXAM_ANSWER_SUCCESS(HttpStatus.OK, "8002", "관리자용 답안 수정 성공"),

	// 9000 : 유저답안 작성 성공 코드
	SAVE_ANSWER_SUCCESS(HttpStatus.CREATED, "9001", "제출용 답안 작성 성공.");
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
