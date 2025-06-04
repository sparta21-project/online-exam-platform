package com.example.onlineexamplatform.domain.answerSheet.service;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.answerSheet.dto.request.AnswerSheetRequestDto;
import com.example.onlineexamplatform.domain.answerSheet.dto.request.UserAnswerRequestDto;
import com.example.onlineexamplatform.domain.answerSheet.dto.response.AnswerSheetResponseDto;
import com.example.onlineexamplatform.domain.answerSheet.dto.response.UserAnswerResponseDto;
import com.example.onlineexamplatform.domain.answerSheet.entity.AnswerSheet;
import com.example.onlineexamplatform.domain.answerSheet.enums.AnswerSheetStatus;
import com.example.onlineexamplatform.domain.answerSheet.repository.AnswerSheetRepository;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.exam.repository.ExamRepository;
import com.example.onlineexamplatform.domain.user.entity.User;
import com.example.onlineexamplatform.domain.user.repository.UserRepository;
import com.example.onlineexamplatform.domain.userAnswer.entity.UserAnswer;
import com.example.onlineexamplatform.domain.userAnswer.repository.UserAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

import static com.example.onlineexamplatform.domain.answerSheet.enums.AnswerSheetStatus.STARTED;

@Service
@RequiredArgsConstructor
public class AnswerSheetService {

    private final AnswerSheetRepository answerSheetRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final ExamRepository examRepository;
    private final UserRepository userRepository;

    //빈 답안지 생성 (시험 응시)
    @Transactional
    public void createAnswerSheet(Long examId, Long userId) {
        Exam exam = examRepository.findByIdOrElseThrow(examId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        AnswerSheet answerSheet = new AnswerSheet(exam, user, STARTED);
        AnswerSheet savedAnswerSheet = answerSheetRepository.save(answerSheet);
    }

    //답안지 수정 (임시 저장 포함)
    public AnswerSheetResponseDto.Update updateAnswerSheet(Long examId, AnswerSheetRequestDto.Update requestDto, Long userId) {
        Exam exam = examRepository.findByIdOrElseThrow(examId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        AnswerSheet answerSheet = answerSheetRepository.findByExamAndUser(exam, user)
                .orElseThrow(() -> new ApiException(ErrorStatus.ANSWER_SHEET_NOT_FOUND));

        answerSheet.updateStatus(AnswerSheetStatus.IN_PROGRESS);

        for (UserAnswerRequestDto dto : requestDto.getAnswers()) {
            int questionNumber = dto.getQuestionNumber();
            String answerText = dto.getAnswerText();

            Optional<UserAnswer> optionalUserAnswer = userAnswerRepository.findByAnswerSheetAndQuestionNumber(answerSheet, questionNumber);

            //기존 답이 있으면 수정
            //없으면 생성
            if (optionalUserAnswer.isPresent()) {
                optionalUserAnswer.get().updateAnswer(answerText);
            } else {
                UserAnswer newAnswer = new UserAnswer(answerSheet, questionNumber, answerText);
                userAnswerRepository.save(newAnswer);
            }
        }

        List<UserAnswer> userAnswers = userAnswerRepository.findAllByAnswerSheet(answerSheet);

        List<UserAnswerResponseDto> answerDtos = userAnswers.stream()
                .map(UserAnswerResponseDto::toUserAnswerResponseDto)
                .toList();

        return new AnswerSheetResponseDto.Update(
                exam.getId(),
                user.getId(),
                answerDtos
        );
    }

    public AnswerSheetResponseDto.Get getAnswerSheet() {
        return null;
    }

    public void deleteAnswerSheet() {
    }

    public List<AnswerSheetResponseDto.Applicant> getExamApplicants() {
        return null;
    }
}