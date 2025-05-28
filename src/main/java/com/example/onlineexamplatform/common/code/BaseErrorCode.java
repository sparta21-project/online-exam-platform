package com.example.onlineexamplatform.common.code;

import com.example.onlineexamplatform.common.dto.ErrorReasonDto;

public interface BaseErrorCode {
	ErrorReasonDto getReason();

	ErrorReasonDto getReasonHttpStatus();
}
