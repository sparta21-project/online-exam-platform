package com.example.onlineexamplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.example.onlineexamplatform.config.session.SessionUser;

@Configuration
public class RedisConfig {
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory("localhost", 6379);
	}

	@Bean
	public RedisTemplate<String, SessionUser> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, SessionUser> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		Jackson2JsonRedisSerializer<SessionUser> serializer =
			new Jackson2JsonRedisSerializer<>(SessionUser.class);
		template.setDefaultSerializer(serializer);
		return template;
	}
}
