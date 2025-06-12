package com.example.onlineexamplatform.common.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.onlineexamplatform.domain.examFile.entity.ExamFile;
import com.example.onlineexamplatform.domain.examFile.service.S3UploadService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExamFileScheduler {

	private final S3UploadService s3UploadService;

	@Transactional
	@Scheduled(cron = "0 0 0 * * *") // 매일 00시 동작
	public void deleteOrphanExamFile() {
		LocalDateTime threshold = LocalDateTime.now().minusMinutes(30); // 30분 이상 지난 시험파일

		List<ExamFile> oldUnlinkedExamFiles = s3UploadService.findOldUnlinkedExamFiles(threshold);

		if (!oldUnlinkedExamFiles.isEmpty()) {
			s3UploadService.deleteExamFile(oldUnlinkedExamFiles);
			log.info("삭제된 고아 시험파일 수 : {}개", oldUnlinkedExamFiles.size());
		} else {
			log.info("고아 시험파일이 없습니다.");
		}
	}

}
