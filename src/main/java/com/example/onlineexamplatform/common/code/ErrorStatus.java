package com.example.onlineexamplatform.common.code;

import org.springframework.http.HttpStatus;

import com.example.onlineexamplatform.common.dto.ErrorReasonDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
	// 공통 에러 코드
	BAD_REQUEST(HttpStatus.BAD_REQUEST,"0001", "잘못된 요청입니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "0002", "서버 내부 오류가 발생했습니다."),

	//user 에러 코드
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "1001", "고객 정보가 없습니다."),
	USER_DEACTIVATE(HttpStatus.FORBIDDEN, "1002", "비활성화된 계정입니다."),
	USER_NOT_MATCH(HttpStatus.UNAUTHORIZED, "1003", "로그인 정보가 일치하지 않습니다."),
	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "1004", "이미 사용중인 이메일입니다."),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "1005", "현재 비밀번호가 일치하지 않습니다."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "1006", "로그인이 필요합니다."),
	ALREADY_WITHDRAWN(HttpStatus.BAD_REQUEST, "1007", "이미 탈퇴한 회원입니다."),
	FORBIDDEN(HttpStatus.FORBIDDEN, "1008", "권한이 없습니다."),

	//exam 에러 코드
	EXAM_NOT_FOUND(HttpStatus.NOT_FOUND, "2001", "찾으시는 문제가 없습니다."),

	// user-category 에러 코드
	CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "3001", "해당 카테고리가 DB에 존재하지 않습니다."),
	DUPLICATE_USER_CATEGORY(HttpStatus.CONFLICT, "3002", "이미 등록된 응시 권한입니다."),
	USER_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "3003", "응시 권한이 존재하지 않습니다."),

	INVALID_EXAM_CATEGORY_MAPPING(HttpStatus.BAD_REQUEST, "3007", "시험과 연결된 카테고리가 없습니다."),
	INVALID_CATEGORY_TYPE(HttpStatus.BAD_REQUEST, "3008", "지원하지 않는 카테고리 타입입니다."),

	//exam-file 업로드 에러코드
	IO_EXCEPTION_UPLOAD_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "4001", "파일 업로드 중 IO 예외가 발생했습니다."),
	INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "4002", "허용되지 않은 파일 확장자입니다."),
	NOT_EXIST_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "4003", "파일 확장자가 존재하지 않습니다."),
	NOT_EXIST_FILE(HttpStatus.NOT_FOUND, "4004", "파일이 존재하지 않습니다."),
	IO_EXCEPTION_DELETE_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "4005", "파일 삭제 중 IO 예외가 발생했습니다."),
	INVALID_URL_FORMAT(HttpStatus.BAD_REQUEST, "4006", "잘못된 URL 형식입니다."),
	FILE_ID_MISSING(HttpStatus.BAD_REQUEST, "4007", "이미지 ID가 누락되었습니다."),
	FILE_ALREADY_LINKED(HttpStatus.BAD_REQUEST, "4008", "해당 파일은 이미 다른 시험에 연결되어 있습니다."),

	//answerSheet 에러 코드
	ANSWER_SHEET_NOT_FOUND(HttpStatus.NOT_FOUND, "3001", "답안지 정보가 없습니다."),
	ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "3002", "답안지를 조회할 권한이 없습니다."),
	ANSWER_SUBMITTED(HttpStatus.BAD_REQUEST, "3003", "이미 제출된 답안지입니다."),
	CATEGORY_NOT_MATCHED(HttpStatus.UNAUTHORIZED, "3004", "답안지 생성 권한이 없습니다."),
	USER_SESSION_NOT_FOUND(HttpStatus.UNAUTHORIZED, "3005", "유저 세션 정보가 없습니다."),

	// userAnswer 에러
	DUPLICATE_QUESTION_NUMBER(HttpStatus.CONFLICT, "8001", "중복된 문제 번호가 존재합니다."),
	EXCEED_USER_ANSWER(HttpStatus.BAD_REQUEST, "8002", "제출한 답안의 수가 출제 문제의 수보다 많습니다."),

	// examAnswer 에러
	DUPLICATE_EXAM_ANSWER(HttpStatus.CONFLICT, "9001", "답안이 이미 존재합니다."),
	EXAM_ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "9002", "답안이 없습니다."),
	DUPLICATE_EXAM_CATEGORY(HttpStatus.CONFLICT, "9003", "이미 등록된 응시 권한입니다.")
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
