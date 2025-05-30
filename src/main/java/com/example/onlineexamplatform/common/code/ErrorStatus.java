package com.example.onlineexamplatform.common.code;

import com.example.onlineexamplatform.common.dto.ErrorReasonDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
	//user 에러 코드
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "1001", "고객 정보가 없습니다."),
	USER_DEACTIVATE(HttpStatus.FORBIDDEN, "1002", "비활성화된 계정입니다."),
	USER_NOT_MATCH(HttpStatus.UNAUTHORIZED, "1003", "로그인 정보가 일치하지 않습니다."),
	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "1004", "이미 사용중인 이메일입니다."),

	// examAnswer 에러
	DUPLICATE_EXAM_ANSWER(HttpStatus.CONFLICT, "9001", "답안이 이미 존재합니다."),
	;

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
