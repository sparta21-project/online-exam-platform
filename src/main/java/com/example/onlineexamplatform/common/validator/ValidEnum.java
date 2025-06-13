package com.example.onlineexamplatform.common.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;
/**
 * 문자열 타입 필드에 대해 특정 Enum 클래스에 정의된 값만 허용되도록 검증하는 커스텀 어노테이션
 * 예: @ValidEnum(enumClass = CategoryType.class)
 */
@Documented
@Constraint(validatedBy = EnumValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {

	String message() default "유효하지 않은 enum 값입니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	Class<? extends Enum<?>> enumClass();
}
