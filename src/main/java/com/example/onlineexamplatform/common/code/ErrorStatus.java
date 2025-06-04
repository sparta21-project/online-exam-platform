package com.example.onlineexamplatform.common.code;

import org.springframework.http.HttpStatus;

import com.example.onlineexamplatform.common.dto.ErrorReasonDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
	//user 에러 코드
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "1001", "고객 정보가 없습니다."),
	USER_DEACTIVATE(HttpStatus.FORBIDDEN, "1002", "비활성화된 계정입니다."),
	USER_NOT_MATCH(HttpStatus.UNAUTHORIZED, "1003", "로그인 정보가 일치하지 않습니다."),
	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "1004", "이미 사용중인 이메일입니다."),

	//exam 에러 코드
	EXAM_NOT_FOUND(HttpStatus.NOT_FOUND, "2001", "찾으시는 문제가 없습니다."),

	//answerSheet 에러 코드
	ANSWER_SHEET_NOT_FOUND(HttpStatus.NOT_FOUND, "3001", "답안지 정보가 없습니다."),
	ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "3002", "답안지를 조회할 권한이 없습니다.");


	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

	public ErrorReasonDto getReason() {
		return ErrorReasonDto.builder()
			.isSuccess(false)
			.code(code)
			.message(message)
			.build();
	}

	public ErrorReasonDto getReasonHttpStatus() {
		return ErrorReasonDto.builder()
			.isSuccess(false)
			.httpStatus(httpStatus)
			.code(code)
			.message(message)
			.build();
	}

}
