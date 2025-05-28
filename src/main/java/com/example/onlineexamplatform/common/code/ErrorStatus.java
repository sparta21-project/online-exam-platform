package com.example.onlineexamplatform.common.code;

import com.example.onlineexamplatform.common.dto.ErrorReasonDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
	//user 에러 코드
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "3001", "고객 정보가 없습니다."),
	USER_DEACTIVATE(HttpStatus.FORBIDDEN, "3002", "비활성화된 계정입니다."),
	USER_NOT_MATCH(HttpStatus.UNAUTHORIZED, "3003", "로그인 정보가 일치하지 않습니다."),
	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "3004", "이미 사용중인 이메일입니다."),

	//book 에러 코드
	BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "2100", "도서 정보를 찾을수 없습니다."),
	BOOK_NOT_APPROVED(HttpStatus.FORBIDDEN, "2103", "승인되지 않은 도서 입니다."),

	//like 에러 코드
	LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "4001", "좋아요 정보를 찾을수 없습니다."),
	LIKE_ALREADY_EXISTS(HttpStatus.CONFLICT, "4002", "이미 좋아요한 항목입니다."),
    TOO_MANY_REQUESTS(HttpStatus.CONFLICT, "4100", "요청이 너무 빠르게 반복되었습니다. 잠시 후 다시 시도해주세요."),

	//admin 에러코드
	BOOK_ADD_REQUEST_ALREADY_ACCEPT(HttpStatus.BAD_REQUEST, "5001", "이미 승인된 요청입니다."),
	REVIEW_ALREADY_PINNED(HttpStatus.BAD_REQUEST, "5002", "이미 고정된 리뷰입니다."),
	REVIEW_ALREADY_UNPINNED(HttpStatus.BAD_REQUEST, "5003", "리뷰가 고정되어 있지 않습니다."),

	//jwt 에러코드
	TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "3101", "토큰이 없습니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "3102", "유효하지 않은 토큰입니다."),
	TOKEN_BLACKLISTED(HttpStatus.UNAUTHORIZED, "3103", "블랙리스트에 등록된 토큰입니다."),

	// 3000 : review 에러코드
	REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "6001", "해당 리뷰를 찾을 수 없습니다"),
	USER_NOT_EQUAL(HttpStatus.BAD_REQUEST, "6002", "해당 리뷰를 작성한 유저가 아닙니다"),
	BOOK_ENROLLMENT_IS_REJECTED(HttpStatus.BAD_REQUEST, "6003", "승인되지 않은 책에는 리뷰 관련 서비스를 이용할 수 없습니다.");

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
