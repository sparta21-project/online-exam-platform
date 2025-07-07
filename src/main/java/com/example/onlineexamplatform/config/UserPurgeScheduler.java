package com.example.onlineexamplatform.config;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.onlineexamplatform.domain.user.entity.User;
import com.example.onlineexamplatform.domain.user.repository.UserRepository;

@Component
public class UserPurgeScheduler {

	private final UserRepository userRepository;

	public UserPurgeScheduler(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Scheduled(cron = "0 0 2 * * *")
	public void purgeWithdrawnUsers() {
		LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
		List<User> toDelete = userRepository.findByIsWithdrawTrueAndWithdrawAtBefore(cutoff);
		if (!toDelete.isEmpty()) {
			userRepository.deleteAll(toDelete);
		}
	}
}
