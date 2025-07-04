//<<<<<<< HEAD
//package com.example.onlineexamplatform.domain.AnswerSheet;
//
//import com.example.onlineexamplatform.domain.answerSheet.repository.AnswerSheetRepository;
//import com.example.onlineexamplatform.domain.answerSheet.service.AnswerSheetLockService;
//import com.example.onlineexamplatform.domain.category.entity.Category;
//import com.example.onlineexamplatform.domain.category.entity.CategoryType;
//import com.example.onlineexamplatform.domain.category.repository.CategoryRepository;
//import com.example.onlineexamplatform.domain.exam.entity.Exam;
//import com.example.onlineexamplatform.domain.exam.repository.ExamRepository;
//import com.example.onlineexamplatform.domain.examCategory.entity.ExamCategory;
//import com.example.onlineexamplatform.domain.examCategory.repository.ExamCategoryRepository;
//import com.example.onlineexamplatform.domain.user.entity.Role;
//import com.example.onlineexamplatform.domain.user.entity.User;
//import com.example.onlineexamplatform.domain.user.repository.UserRepository;
//import com.example.onlineexamplatform.domain.userCategory.entity.UserCategory;
//import com.example.onlineexamplatform.domain.userCategory.repository.UserCategoryRepository;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDateTime;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class AnswerSheetConcurrencyTest {
//
//    @Autowired
//    private AnswerSheetLockService answerSheetLockService;
//
//    @Autowired
//    private ExamRepository examRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private AnswerSheetRepository answerSheetRepository;
//    @Autowired
//    private CategoryRepository categoryRepository;
//    @Autowired
//    private UserCategoryRepository userCategoryRepository;
//    @Autowired
//    private ExamCategoryRepository examCategoryRepository;
//
//    private Long examId;
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @BeforeAll
//    void init() {
//        Category category = categoryRepository.findByCategoryType(CategoryType.MATH)
//                .orElseGet(() -> categoryRepository.save(new Category(CategoryType.MATH)));
//
//        for (long i = 1; i <= 60; i++) {
//            String email = "test" + i + "@test.com";
//            User user = new User(email, "TestPassword123!", "testUser", Role.USER);
//            userRepository.save(user);
//
//            userCategoryRepository.save(new UserCategory(user, category));
//        }
//        User admin = userRepository.save(new User("admin@admin.com", "TestPassword123!", "testAdmin", Role.ADMIN));
//
//        Exam exam = examRepository.save(Exam.builder()
//                .user(admin)
//                .title("테스트 Exam1")
//                .description("테스트용 Exam1")
//                .totalQuestionsNum(50L)
//                .startTime(LocalDateTime.now())
//                .endTime(LocalDateTime.now().plusHours(1))
//                .remainUsers(50)
//                .build());
//
//        examCategoryRepository.save(new ExamCategory(exam, category));
//
//        examId = exam.getId();
//    }
//
//    @Test
//    @DisplayName("병렬로 60번 시험 응시 요청 시 남은 자리는 0이고 성공은 50, 실패는 10번이다.")
//    public void testRedissonLock() throws InterruptedException {
//        int threadCount = 60;
//        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
//        CountDownLatch latch = new CountDownLatch(threadCount);
//
//        for(int i = 1; i <= threadCount; i++) {
//            long userId = i;
//            executor.submit(() -> {
//                try {
//                    answerSheetLockService.createAnswerSheetWithLock(examId, userId);
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//        latch.await();
//
//        Exam updatedExam = examRepository.findById(examId).orElseThrow();
//        long answerSheetCount = answerSheetRepository.count();
//
//        assertEquals(0, updatedExam.getRemainUsers());
//        assertEquals(50, answerSheetCount);
//    }
//}
//=======
////package com.example.onlineexamplatform.domain.AnswerSheet;
////
////import com.example.onlineexamplatform.domain.answerSheet.repository.AnswerSheetRepository;
////import com.example.onlineexamplatform.domain.answerSheet.service.AnswerSheetLockService;
////import com.example.onlineexamplatform.domain.category.entity.Category;
////import com.example.onlineexamplatform.domain.category.entity.CategoryType;
////import com.example.onlineexamplatform.domain.category.repository.CategoryRepository;
////import com.example.onlineexamplatform.domain.exam.entity.Exam;
////import com.example.onlineexamplatform.domain.exam.repository.ExamRepository;
////import com.example.onlineexamplatform.domain.examCategory.entity.ExamCategory;
////import com.example.onlineexamplatform.domain.examCategory.repository.ExamCategoryRepository;
////import com.example.onlineexamplatform.domain.user.entity.Role;
////import com.example.onlineexamplatform.domain.user.entity.User;
////import com.example.onlineexamplatform.domain.user.repository.UserRepository;
////import com.example.onlineexamplatform.domain.userCategory.entity.UserCategory;
////import com.example.onlineexamplatform.domain.userCategory.repository.UserCategoryRepository;
////import jakarta.persistence.EntityManager;
////import jakarta.persistence.PersistenceContext;
////import org.junit.jupiter.api.BeforeAll;
////import org.junit.jupiter.api.DisplayName;
////import org.junit.jupiter.api.Test;
////import org.junit.jupiter.api.TestInstance;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.boot.test.context.SpringBootTest;
////
////import java.time.LocalDateTime;
////import java.util.concurrent.CountDownLatch;
////import java.util.concurrent.ExecutorService;
////import java.util.concurrent.Executors;
////
////import static org.junit.jupiter.api.Assertions.assertEquals;
////
////@SpringBootTest
////@TestInstance(TestInstance.Lifecycle.PER_CLASS)
////public class AnswerSheetConcurrencyTest {
////
////    @Autowired
////    private AnswerSheetLockService answerSheetLockService;
////
////    @Autowired
////    private ExamRepository examRepository;
////    @Autowired
////    private UserRepository userRepository;
////    @Autowired
////    private AnswerSheetRepository answerSheetRepository;
////    @Autowired
////    private CategoryRepository categoryRepository;
////    @Autowired
////    private UserCategoryRepository userCategoryRepository;
////    @Autowired
////    private ExamCategoryRepository examCategoryRepository;
////
////
////    private Long examId;
////
////    @PersistenceContext
////    private EntityManager entityManager;
////
////    @BeforeAll
////    void init() {
////        Category category = categoryRepository.findByCategoryType(CategoryType.MATH)
////                .orElseGet(() -> categoryRepository.save(new Category(CategoryType.MATH)));
////
////        for (long i = 1; i <= 60; i++) {
////            String email = "test" + i + "@test.com";
////            User user = new User(email, "TestPassword123!", "testUser", Role.USER);
////            userRepository.save(user);
////
////            userCategoryRepository.save(new UserCategory(user, category));
////        }
////        User admin = userRepository.save(new User("admin@admin.com", "TestPassword123!", "testAdmin", Role.ADMIN));
////
////        Exam exam = examRepository.save(Exam.builder()
////                .user(admin)
////                .title("테스트 Exam1")
////                .description("테스트용 Exam1")
////                .totalQuestionsNum(50L)
////                .startTime(LocalDateTime.now())
////                .endTime(LocalDateTime.now().plusHours(1))
////                .remainUsers(50)
////                .build());
////
////        examCategoryRepository.save(new ExamCategory(exam, category));
////
////        examId = exam.getId();
////    }
////
////    @Test
////    @DisplayName("병렬로 60번 시험 응시 요청 시 남은 자리는 0이고 성공은 50, 실패는 10번이다.")
////    public void testRedissonLock() throws InterruptedException {
////        int threadCount = 60;
////        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
////        CountDownLatch latch = new CountDownLatch(threadCount);
////
////        for(int i = 1; i <= threadCount; i++) {
////            long userId = i;
////            executor.submit(() -> {
////                try {
////                    answerSheetLockService.createAnswerSheetWithLock(examId, userId);
////                } finally {
////                    latch.countDown();
////                }
////            });
////        }
////        latch.await();
////
////        Exam updatedExam = examRepository.findById(examId).orElseThrow();
////        long answerSheetCount = answerSheetRepository.count();
////
////        assertEquals(0, updatedExam.getRemainUsers());
////        assertEquals(50, answerSheetCount);
////    }
////}
//>>>>>>> 3ea7cd5df4150712d75679e525535f44e162ad40
