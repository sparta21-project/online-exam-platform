package com.example.onlineexamplatform.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * @ValidEnum 어노테이션의 검증 로직을 수행하는 클래스
 * 문자열 입력값이 지정된 Enum 클래스의 값 중 하나인지 검증
 */
public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

	private Set<String> acceptedValues;

	@Override
	public void initialize(ValidEnum annotation) {
		Class<? extends Enum<?>> enumClass = annotation.enumClass();
		acceptedValues = Arrays.stream(enumClass.getEnumConstants())
				.map(Enum::name)
				.collect(Collectors.toSet());
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value == null || acceptedValues.contains(value);
	}
}
