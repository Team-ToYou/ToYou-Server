//package com.example.toyou.app;
//
//import com.example.toyou.domain.CustomQuestion;
//import com.example.toyou.repository.CustomQuestionRepository;
//import org.springframework.context.annotation.Configuration;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//
//import static com.example.toyou.domain.enums.Emotion.*;
//import static com.example.toyou.domain.enums.QuestionType.*;
//import static com.example.toyou.domain.enums.Status.*;
//
//@Configuration
//public class DataInitializer {
//
//    @Bean
//    CommandLineRunner initDatabase(CustomQuestionRepository repository) {
//        return args -> {
//            // 중고등학생 - 불안
//            repository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(NERVOUS).questionType(LONG_ANSWER).content("시험 전날에는 보통 어떤 생각이 들어?").build());
//            repository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(NERVOUS).questionType(LONG_ANSWER).content("새로운 학기가 시작될 때 불안하거나 걱정되는 점이 있어?").build());
//            repository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(NERVOUS).questionType(LONG_ANSWER).content("발표할 때 가장 불안한 순간은 언제야?").build());
//            repository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(NERVOUS).questionType(LONG_ANSWER).content("큰 결정을 내려야 할 때 불안한 마음을 어떻게 달래?").build());
//            repository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(NERVOUS).questionType(LONG_ANSWER).content("친구들 앞에서 무대에 서야 할 때 어떤 기분이야?").build());
//            // 중고등학생 - 흥분
//            repository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(EXCITED).questionType(LONG_ANSWER).content("친구들과 특별한 이벤트를 계획할 때 가장 흥분되는 순간은 언제야?").build());
//            repository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(EXCITED).questionType(LONG_ANSWER).content("새로운 취미나 활동을 시작할 때 기분이 어때?").build());
//            repository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(EXCITED).questionType(LONG_ANSWER).content("좋아하는 영화 시리즈의 새로운 작품이 개봉할 때 어떤 기분이야?").build());
//            repository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(EXCITED).questionType(LONG_ANSWER).content("좋아하는 가수의 콘서트를 보러 갈 때 가장 흥분되는 순간은 언제야?").build());
//            repository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(EXCITED).questionType(LONG_ANSWER).content("학교에서 특별한 행사가 있을 때 가장 신나는 순간은 언제야?").build());
//            // 중고등학생 - 행복
//            repository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(HAPPY).questionType(LONG_ANSWER).content("가족과 함께 보낸 특별한 순간 중에 기억에 남는 게 뭐야?").build());
//            repository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(HAPPY).questionType(LONG_ANSWER).content("친구들과 재미있는 이야기를 나누며 웃었던 순간은 언제야?").build());
//            repository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(HAPPY).questionType(LONG_ANSWER).content("성취한 것 중에서 가장 큰 자부심을 느낀 때는 언제야?").build());
//            repository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(HAPPY).questionType(LONG_ANSWER).content("좋아하는 음식을 먹을 때 가장 행복한 순간은 언제야?").build());
//            repository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(HAPPY).questionType(LONG_ANSWER).content("꿈꾸던 일이 현실이 되었을 때 어떤 기분이야?").build());
//            // 중고등학생 - 화남
//            repository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(ANGRY).questionType(LONG_ANSWER).content("어떤 상황에서 가장 화가 나?").build());
//            repository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(ANGRY).questionType(LONG_ANSWER).content("진심으로 실망했던 경험이 있어? 어떻게 대처했어?").build());
//            repository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(ANGRY).questionType(LONG_ANSWER).content("가족이나 친구들과의 갈등을 해결하는 데 가장 힘들었던 경험은 뭐야?").build());
//            repository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(ANGRY).questionType(LONG_ANSWER).content("내가 부당한 대우를 받았을 때 어떻게 대처해?").build());
//            repository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(ANGRY).questionType(LONG_ANSWER).content("화가 났을 때 스트레스를 어떻게 해소해?").build());
//
//
//
//
//
//        };
//    }
//}