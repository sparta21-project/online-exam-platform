import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.domain.answerSheet.dto.request.AnswerSheetRequest;
import com.example.onlineexamplatform.domain.answerSheet.dto.response.AnswerSheetResponse;
import com.example.onlineexamplatform.domain.answerSheet.service.AnswerSheetService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.onlineexamplatform.domain.user.controller.UserController.SESSION_USER_KEY;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exam/{examId}")
public class AnswerSheetController {

    private final AnswerSheetService answerSheetService;

    //빈 답안지 생성 (시험 응시)
    @PostMapping("/answersheet")
    public ResponseEntity<ApiResponse<AnswerSheetResponse.Create>> createAnswerSheet(
            @PathVariable Long examId,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getSession().getAttribute(SESSION_USER_KEY);
        AnswerSheetResponse response = answerSheetService.createAnswerSheet(examId, userId);
        return ApiResponse.onSuccess(SuccessStatus.CREATED, response);
    }

    //답안지 수정 (임시 저장 포함)
    @PatchMapping("/answersheet/{answerSheetId}")
    public ResponseEntity<ApiResponse<AnswerSheetResponse.Update>> updateAnswerSheet(
            @PathVariable Long examId,
            @PathVariable Long answerSheetId,
            @RequestBody AnswerSheetRequest.Update requestDto,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getSession().getAttribute(SESSION_USER_KEY);
        AnswerSheetResponse response = answerSheetService.updateAnswerSheet(examId, answerSheetId, requestDto, userId);
        return ApiResponse.onSuccess(SuccessStatus.OK, response);
    }

    //답안지 조회
    @GetMapping("/answersheet/{answerSheetId}")
    public ResponseEntity<ApiResponse<AnswerSheetResponse.Get>> getAnswerSheet(
            @PathVariable Long examId,
            @PathVariable Long answerSheetId,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getSession().getAttribute(SESSION_USER_KEY);
        AnswerSheetResponse response = answerSheetService.getAnswerSheet(examId, answerSheetId, userId);
        return ApiResponse.onSuccess(SuccessStatus.OK, response);
    }

    //답안지 삭제
    @DeleteMapping("/answersheet/{answerSheetId}")
    public ResponseEntity<ApiResponse<Void>> deleteAnswerSheet(
            @PathVariable Long examId,
            @PathVariable Long answerSheetId,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getSession().getAttribute(SESSION_USER_KEY);
        answerSheetService.deleteAnswerSheet(examId, answerSheetId, userId);
        return ApiResponse.onSuccess(SuccessStatus.NO_CONTENT);
    }

    //답안 최종 제출
    @PostMapping("/answersheet/{answerSheetId}")
    public ResponseEntity<ApiResponse<AnswerSheetResponse.Submit>> submitAnswerSheet(
            @PathVariable Long examId,
            @PathVariable Long answerSheetId,
            @RequestBody AnswerSheetRequest.Submit requestDto,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getSession().getAttribute(SESSION_USER_KEY);
        AnswerSheetResponse response = answerSheetService.submitAnswerSheet(examId, answerSheetId, requestDto, userId);
        return ApiResponse.onSuccess(SuccessStatus.OK, response);
    }

    //시험 응시자 조회
    @GetMapping("/applicants")
    public ResponseEntity<ApiResponse<List<AnswerSheetResponse.Applicant>>> getExamApplicants(
            @PathVariable Long examId,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getSession().getAttribute(SESSION_USER_KEY);
        List<AnswerSheetResponse.Applicant> response = answerSheetService.getExamApplicants(examId, userId);
        return ApiResponse.onSuccess(SuccessStatus.OK, response);
    }
}