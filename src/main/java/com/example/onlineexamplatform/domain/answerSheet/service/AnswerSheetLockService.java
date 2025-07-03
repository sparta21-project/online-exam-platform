package com.example.onlineexamplatform.domain.answerSheet.service;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerSheetLockService {

    private final RedissonClient redissonClient;
    private final AnswerSheetService answerSheetService;

    public void createAnswerSheetWithLock(Long examId, Long userId) {
        String lockKey = "lock:answersheet:create:" + examId;
        RLock lock = redissonClient.getFairLock(lockKey);

        try {
            boolean acquired = lock.tryLock(3, 10, TimeUnit.SECONDS);
            if (!acquired) {
                throw new ApiException(ErrorStatus.USER_NOT_FOUND);
            }
            log.info("✅ [User {}] 락 획득", userId);
            // 임계 영역, Transactional 수행
            answerSheetService.createAnswerSheet(examId, userId);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiException(ErrorStatus.USER_NOT_FOUND);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("🔓 [User {}] 락 해제", userId);
            }
        }
    }
}
