package com.example.onlineexamplatform.testconfig;

import com.example.onlineexamplatform.domain.sms.service.SmsService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MockSmsServiceConfig {

	@Bean
	public SmsService smsService() {
		return Mockito.mock(SmsService.class);
	}
}