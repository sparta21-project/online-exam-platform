package com.example.onlineexamplatform.common.error;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.dto.ErrorReasonDto;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.common.validator.ValidEnum;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import java.lang.reflect.Field;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	// 커스텀 예외 처리
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<?> handleCustomException(ApiException e) {
		log.error("CustomException: {}", e.getMessage(), e);

		ErrorReasonDto reason = e.getErrorCode().getReasonHttpStatus();
		ApiResponse<ErrorReasonDto> response = new ApiResponse<>(
				false,
				reason.getCode(),
				reason.getMessage(),
				null
		);
		return ResponseEntity.status(reason.getHttpStatus()).body(response);
	}

	// @Valid DTO 필드 검증 실패 (e.g. @NotNull, @NotBlank)
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers,
			HttpStatusCode status,
			WebRequest request) {

		// 커스텀 메시지 우선 추출후 없을 경우 "잘못된 요청입니다" 출력
		String message = ex.getBindingResult().getAllErrors().stream()
				.map(error -> {
					String defaultMessage = error.getDefaultMessage();
					return (defaultMessage != null && !defaultMessage.isBlank())
							? defaultMessage
							: "잘못된 요청입니다.";
				})
				.findFirst()
				.orElse("잘못된 요청입니다.");

		// ErrorReasonDto 생성
		ErrorReasonDto reason = ErrorReasonDto.builder()
				.isSuccess(false)
				.code(ErrorStatus.BAD_REQUEST.getCode())
				.message(message)
				.httpStatus(ErrorStatus.BAD_REQUEST.getHttpStatus())
				.build();

		// ApiResponse 생성 및 반환
		ApiResponse<ErrorReasonDto> response = new ApiResponse<>(
				false,
				reason.getCode(),
				reason.getMessage(),
				null
		);

		return ResponseEntity
				.status(reason.getHttpStatus())
				.body(response);
	}

	// JSON 파싱 실패 (Enum 타입이 잘못된 경우)
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex,
			HttpHeaders headers,
			HttpStatusCode status,
			WebRequest request) {

		log.error("Invalid request body or enum format: {}", ex.getMessage(), ex);

		Throwable cause = ex.getCause();
		if (cause instanceof InvalidFormatException formatEx) {
			if (!formatEx.getPath().isEmpty()) {
				String fieldName = formatEx.getPath().get(0).getFieldName();
				Class<?> targetClass = formatEx.getPath().get(0).getFrom().getClass();

				if (hasValidEnumAnnotation(targetClass, fieldName)) {
					ErrorReasonDto reason = ErrorStatus.INVALID_CATEGORY_TYPE.getReasonHttpStatus();
					ApiResponse<ErrorReasonDto> response = new ApiResponse<>(
							false,
							reason.getCode(),
							reason.getMessage(),
							null
					);
					return ResponseEntity.status(reason.getHttpStatus()).body(response);
				}
			}
		}

		ErrorReasonDto reason = ErrorStatus.BAD_REQUEST.getReasonHttpStatus();
		ApiResponse<ErrorReasonDto> response = new ApiResponse<>(
				false,
				reason.getCode(),
				reason.getMessage(),
				null
		);
		return ResponseEntity.status(reason.getHttpStatus()).body(response);
	}

	//  @RequestParam, @PathVariable 잘못된 값
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
		log.warn("ConstraintViolationException: {}", ex.getMessage());

		ErrorReasonDto reason = ErrorStatus.BAD_REQUEST.getReasonHttpStatus();
		ApiResponse<ErrorReasonDto> response = new ApiResponse<>(
				false,
				reason.getCode(),
				reason.getMessage(),
				null
		);
		return ResponseEntity.status(reason.getHttpStatus()).body(response);
	}

	// 그 외 예상 못한 예외처리
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleUnexpectedException(Exception ex) {
		log.error("Unhandled Exception: {}", ex.getMessage(), ex);

		ErrorReasonDto reason = ErrorStatus.INTERNAL_SERVER_ERROR.getReasonHttpStatus();
		ApiResponse<ErrorReasonDto> response = new ApiResponse<>(
				false,
				reason.getCode(),
				reason.getMessage(),
				null
		);
		return ResponseEntity.status(reason.getHttpStatus()).body(response);
	}

	// @ValidEnum 어노테이션이 붙었는지 검사
	private boolean hasValidEnumAnnotation(Class<?> clazz, String fieldName) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			return field.isAnnotationPresent(ValidEnum.class);
		} catch (NoSuchFieldException e) {
			return false;
		}
	}
}
