package com.example.onlineexamplatform.domain.exam.repository;

import static com.example.onlineexamplatform.domain.examFile.entity.QExamFile.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.onlineexamplatform.domain.examFile.entity.ExamFile;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ExamFileQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public List<ExamFile> findOldUnlinkedExamFiles(LocalDateTime threshold) {
		return jpaQueryFactory.selectFrom(examFile)
			.where(
				examFile.exam.isNull(),
				examFile.createdAt.before(threshold)
			)
			.fetch();
	}

}
