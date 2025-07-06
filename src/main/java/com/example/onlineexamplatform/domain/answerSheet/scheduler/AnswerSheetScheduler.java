package com.example.onlineexamplatform.domain.answerSheet.scheduler;

import com.example.onlineexamplatform.domain.answerSheet.entity.AnswerSheet;
import com.example.onlineexamplatform.domain.answerSheet.enums.AnswerSheetStatus;
import com.example.onlineexamplatform.domain.answerSheet.repository.AnswerSheetRepository;
import com.example.onlineexamplatform.domain.answerSheet.service.AnswerSheetService;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.exam.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnswerSheetScheduler {
    private final AnswerSheetService answerSheetService;
    private final AnswerSheetRepository answerSheetRepository;
    private final ExamRepository examRepository;

    //종료된 시험의 답안지들 채점
<<<<<<< HEAD
    //TODO: 시간 변경
    //@Scheduled(cron = "0 0 * * * *")
    @Scheduled(cron = "0 */5 * * * *")
=======
    @Scheduled(cron = "0 0 * * * *")
>>>>>>> 1adfd0e5fdaef02938281b017f5e673081e04bdd
    public void gradeAnswerSheet() {
        List<Exam> endedExams = examRepository.findByEndTimeBefore(LocalDateTime.now());

        for (Exam exam : endedExams) {
            List<AnswerSheet> answerSheets = answerSheetRepository.findByExamIdAndStatusNot(exam.getId(), AnswerSheetStatus.GRADED);

            for (AnswerSheet answerSheet : answerSheets) {
                Long answerSheetId = answerSheet.getId();

                answerSheetService.gradeAnswerSheet(answerSheetId);
            }
        }
    }

    //종료된 시험의 모든 답안지 상태 변경
    @Scheduled(cron = "0 */10 * * * *")
    public void changeAnswerSheetStatus() {
        List<Exam> endedExams = examRepository.findByEndTimeBefore(LocalDateTime.now());
        List<AnswerSheetStatus> statuses = Arrays.asList(AnswerSheetStatus.GRADED, AnswerSheetStatus.SUBMITTED);

        for (Exam exam : endedExams) {
            List<AnswerSheet> answerSheets = answerSheetRepository.findByExamIdAndStatusNotIn(exam.getId(), statuses);

            for (AnswerSheet answerSheet : answerSheets) {
                Long answerSheetId = answerSheet.getId();

                answerSheetService.changeAnswerSheetStatus(answerSheetId);
            }
        }
    }

}
