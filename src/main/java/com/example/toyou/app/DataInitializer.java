//package com.example.toyou.app;
//
//import com.example.toyou.domain.CustomQuestion;
//import com.example.toyou.domain.enums.QuestionType;
//import com.example.toyou.domain.enums.Status;
//import com.example.toyou.repository.CustomQuestionRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//
//import java.util.Arrays;
//
//import static com.example.toyou.domain.enums.QuestionType.*;
//import static com.example.toyou.domain.enums.Status.*;
//
//@Configuration
//@RequiredArgsConstructor
//public class DataInitializer {
//
//    private final CustomQuestionRepository customQuestionRepository;
//
//    @Bean
//    CommandLineRunner initDatabase() {
//        return args -> {
//            // 중고등학생
//            // 단답형
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("오늘 급식으로 뭐 나왔어? 맛있었어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("오늘 아침으로 뭐 먹었어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("요즘 최애 음식이 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("오늘 아침에 일어나자마자 가장 먼저 한 일은?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("요즘 자주 하는 취미 활동은 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("요즘 관심사가 어떻게 돼?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("요즘 즐겨 듣는 음악 장르는?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("가장 좋아하는 연예인은? 왜 좋아?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("최근에 가장 많이 들은 노래는?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("최근에 가장 재밌게 본 영화는?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("요즘 친구들이랑 사이 어때?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("연애해? 아니면 요새 호감있는 사람있어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("최근에 스트레스 생긴 일 있었어? 있다면 어떻게 해소했어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("요즘 가장 많이 하는 운동은?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("가장 최근에 직접 만든 요리는?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("오늘 하루를 돌아봤을 때 가장 기억에 남는 순간은?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("오늘의 TMI는?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("요즘 학교에서 제일 재밌는 과목은?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("요즘 점심시간에 주로 뭐해?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("지금 무슨 동아리하고 있어? 어때?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("최근에 학교에서 가장 웃겼던 일은 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("오늘 학교 끝나고 가장 먼저 한 일은 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("오늘 친구들이랑 무슨 얘기 했어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("오늘 들은 말 중에 가장 기억에 남는 말은?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("오늘 가장 많이 대화한 사람이 누구야? 무슨 얘기했어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("요즘 학교 다음으로 많이 가는 곳이 어디야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("요즘 가장 많이 쓰는 앱은?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("최근에 가족과 재미있게 보낸 시간은 언제였어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("요즘 유튜브로 어떤 영상 자주 봐?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.SHORT_ANSWER).content("교복을 제외하고 즐겨입는 옷 스타일이 어떻게 돼?").build());
//
//            // 선택형
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("오늘 휴대폰 얼마나 사용했어?").answerOptions(Arrays.asList("1시간 이하", "1~3시간", "3시간 이상")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("오늘 몇 끼 먹었어?").answerOptions(Arrays.asList("1끼", "2끼", "3끼 이상")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("오늘의 스트레스 정도는?").answerOptions(Arrays.asList("상", "중", "하")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("오늘 점심시간에 뭐했어?").answerOptions(Arrays.asList("공부", "놀기", "휴식")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("학교 끝나고 뭐했어?").answerOptions(Arrays.asList("공부", "놀기", "휴식")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("오늘 친구랑 얼마나 대화했어?").answerOptions(Arrays.asList("많이 했음", "조금 했음", "거의 안 했음")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("오늘 부모님과 얼마나 대화했어?").answerOptions(Arrays.asList("많이 했음", "조금 했음", "거의 안 했음")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("오늘 급식 어땠어?").answerOptions(Arrays.asList("맛있었음", "보통", "별로였음")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("어제 몇 시간 잤어?").answerOptions(Arrays.asList("6시간 이하", "6~8시간", "8시간 이상")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("오늘 할 일은 다 했어?").answerOptions(Arrays.asList("다 했음", "조금 남았음", "거의 못 했음")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("전교 1등 되기", "반에서 인기 1등 되기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("급식 맛있는 학교", "시설 좋은 학교")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("3년 내내 같은 담임 선생님", "3년 내내 같은 짝꿍")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("체육대회에서 나는 1등하고 우리 반 꼴등하기", "체육대회에서 나는 꼴등하고 우리 반 1등하기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("전교생 앞에서 방구 뀌기", "이성 친구들 앞에서 애교 부리기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("문과", "이과", "예체능")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("수행평가 100%", "지필고사 100%")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("빠른 취업", "대학 진학")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("입학 첫 날로 되돌아가기", "그냥 살기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("우리 학교 급식 맛있다", "우리 학교 급식 맛없다")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("진정한 친구 1명", "친구 100명")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("똥 먹은 적 없는데 똥 먹었다고 소문나기", "똥 먹었는데 아무도 모르기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("1년 동안 폰 없이 살기", "1년 동안 친구 없이 혼자 지내기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("똥 먹기", "10억 받기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("개구리 맛 초콜릿", "초콜릿 맛 개구리")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("팔만대장경 손으로 옮겨쓰기", "대장내시경 팔만번 하기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("평생 안 자도 안 피곤함", "아무리 먹어도 살 안 찜")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("100% 확률로 500만원", "20% 확률로 100억")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("머리 5일 안감기", "세수 5일 안하기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.SCHOOL).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("오이케이크 먹기", "김치케이크 먹기")).build());
//
//
//            // 대학생
//            // 단답형
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("오늘 점심으로 뭐 먹었어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("오늘 아침으로 뭐 먹었어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("요즘 최애 음식이 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("오늘 아침에 일어나자마자 가장 먼저 뭐했어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("요즘 자주 하는 취미 활동은 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("요즘 관심사가 어떻게 돼?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("요즘 즐겨 듣는 음악 장르는?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("가장 좋아하는 연예인은? 왜 좋아?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("최근에 가장 많이 들은 노래는?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("최근에 가장 재밌게 본 영화는?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("요즘 친구들이랑 사이 어때?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("연애해? 아니면 요새 호감있는 사람있어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("최근에 스트레스 생긴 일 있었어? 있다면 어떻게 해소했어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("요즘 가장 많이 하는 운동은?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("가장 최근에 직접 만든 요리는?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("오늘 하루를 돌아봤을 때 가장 기억에 남는 순간은?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("오늘의 TMI는?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("요즘 학교에서 제일 재밌는 과목은?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("요새 동아리나 대외활동하고 있는거 있어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("요즘 공강에 뭐해?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("오늘 들은 말 중에 가장 기억에 남는 말은?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("오늘 가장 많이 대화한 사람이 누구야? 무슨 얘기했어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("요즘 학교 다음으로 많이 가는 곳이 어디야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("요즘 가장 많이 쓰는 앱은?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("최근에 가족과 재미있게 보낸 시간은 언제였어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("요즘 유튜브로 어떤 영상 자주 봐?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("요새 즐겨입는 옷 스타일이 어떻게 돼?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("요즘 가장 신경 쓰는 과제나 프로젝트는 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("요즘 캠퍼스에서 자주 가는 장소는?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.SHORT_ANSWER).content("요즘 자주 가는 카페나 맛집이 있어?").build());
//
//            // 선택형
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("고3으로 돌아가서 수능 다시 보기", "지금 이대로 살기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("대학원생 되기", "대학교 7년 다니기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("1교시 수업인데 3연강", "2과목 강의인데 5시간 우주공강")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("이른 아침 수업 듣고 낮잠 자기", "밤샘 공부 후 낮 자유시간")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("이번 학기 과탑 확정", "오늘 바로 종강하기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("월공강", "수공강", "금공강")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("내가 절친의 전애인과 과CC", "절친이 나의 전애인과 과CC")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("전애인의 현애인과 팀플", "전애인과 시간표 겹치기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("술 마시고 교수님께 전화하기", "술마시고 전 애인한테 전화하기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("도서관에서 공부하기", "카페에서 공부하기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("통학 왕복 3시간", "기숙사 10시 통금")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("하루에 시험 4개", "종강하고 레포트 4개")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("나빼고 전부 새내기랑 팀플", "막학기랑 팀플")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("매주 개인 발표", "매주 팀플")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("관심 있지만 학점따기 어려운 강의", "관심 없지만 학점따기 쉬운 강의")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("모든 걸 혼자하고 발표까지 하는 개인 과제", "연락 잘 안받는 빌런 있는 조별 과제")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("평생 대면 수업만 하기", "평생 비대면 수업만 하기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("5일 내내 1교시", "5일 내내 4시간 우주공강")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("과제는 없지만 시험 난이도 역대급", "시험 없지만 과제 폭탄")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("시험기간 도서관에서 밤새기", "시험기간 집에서 밤새기", "그냥 자기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("진정한 친구 1명", "친구 100명")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("똥 먹은 적 없는데 똥 먹었다고 소문나기", "똥 먹었는데 아무도 모르기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("1년 동안 폰 없이 살기", "1년 동안 친구없이 혼자 지내기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("똥 먹기", "10억 받기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("개구리 맛 초콜릿", "초콜릿 맛 개구리")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("팔만대장경 손으로 옮겨쓰기", "대장내시경 팔만번 하기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("평생 안 자도 안 피곤함", "아무리 먹어도 살 안 찜")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("100% 확률로 500만원", "20% 확률로 100억")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("머리 5일 안감기", "세수 5일 안하기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.COLLEGE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("오이케이크 먹기", "김치케이크 먹기")).build());
//
//
//            // 직장인
//            // 단답형
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("오늘 출근길은 어땠어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("요즘 관심이 가는 새로운 분야나 일이 있어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("최근에 가장 흥미로웠던 직장 내 사건은 뭐였어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("주말에는 보통 뭘 하면서 보내?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("지금 회사에서 아쉬운 점이 있다면 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("요즘 읽고 있는 책이나 보고 있는 콘텐츠가 있어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("주말이나 휴일에 꼭 가고 싶은 곳이 있다면 어디야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("가장 최근에 직접 만든 요리는?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("요즘 이직에 대해 고민해 본 적 있어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("일할 때 어떤 타입의 동료와 함께하면 좋아?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("요즘 관심이 가는 새로운 분야나 일이 있어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("오늘의 TMI는?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("최근에 가장 많이 들은 노래는?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("퇴근 후에 주로 즐기는 취미나 활동이 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("회사 주변 맛집 하나 소개시켜 줘!").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("출퇴근 시간에는 주로 뭐 하면서 보내?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("회사 밖에서 자주 가는 카페나 장소가 있어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("최근에 본 영화나 드라마 중에서 가장 좋았던 건 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("오늘 아침으로 뭐 먹었어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("동료들이 자주 물어보는 질문이 있어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("오늘 하루를 돌아봤을 때 가장 기억에 남는 순간은?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("오늘 하루 중 가장 고마웠던 사람은 누구였어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("일하면서 가장 뿌듯했던 순간이나 성취감을 느낀 적은 언제였어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("좋아하는 운동 있어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("업무 중 가장 어렵게 느껴졌던 건 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("최근에 네가 새롭게 배우거나 시도해 본 일이 있어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("지금 맡고 있는 프로젝트나 업무는 어떤 거야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("친구들과 만나서 자주 하는 이야기가 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("오늘 우연히 본 풍경 중에 가장 기억에 남는 건 뭐였어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.SHORT_ANSWER).content("최근에 알게 된 업무 관련 꿀팁이 있어?").build());
//
//            // 선택형
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("모두가 천재인 팀에서 숨쉬듯 자괴감 느끼기", "나만이 유일한 희망인 팀에서 소처럼 일하기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("불평불만 많지만 마당발인 동료", "조용하고 차분하나 아싸 기질의 동료")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("깔끔한 오피스룩", "편안한 캐주얼룩")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("안주는 김뿐인 절친과의 술자리", "초호화 고급 안주 무한리필 전체 회식 자리")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("일은 잘하지만 인성에 문제 있는 동료", "일을 못하지만 참 착한 동료")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("연인보다 하루 빨리 죽기", "하루 늦게 죽기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("나 같은 자식 낳기", "나 같은 부모에게 자라기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("당장 5억 받기", "50살에 50억 받기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("평생 쌀만 먹기", "평생 밥만 먹기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("짠돌이 애인", "과소비 애인")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("도심 연봉 3천", "깡시골 연봉 6천")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("빙빙 돌려 말하는 애인", "팩폭하는 애인")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("언제 죽는지 알 수 있음", "어떻게 죽는지 알 수 있음")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("팔만대장경 읽기", "대장내시경 8만번 하기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("눈 감고 실제로 최애 만나기", "눈 뜨고 유튜브로 최애 보기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("절대 안 닫히는 방문", "열고 닫는데 30분 걸리는 방문")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("나랑 똑같은 사람과 연애하기", "나랑 정반대인 사람과 연애하기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("A to Z까지 터치하는 사수", "방목시키는 사수")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("월 220 주 5일 근무", "월 300 주 6일 근무")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("돈 많이 주는 하기 싫은 일", "돈 적게 주는 하고 싶은 일")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("월요일 연차", "금요일 연차")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("연봉 만족", "불만족?")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("재택근무 선호", "비선호")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("만원 버스로 30분 출퇴근", "텅 빈 버스로 90분 출퇴근")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("연차 날 쏟아지는 업무 전화", "퇴근 10분 전 업무 받아서 야근하기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("메시지로 대화", "얼굴 보고 대화")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("전 직장 상사가 현 직장 부하", "전 직장 후배가 현 직장 상사")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("20층 사무실 엘리베이터가 고장", "한여름에 사무실 에어컨이 고장")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("연봉 그대로 매일 칼퇴", "연봉 2배 매일 야근")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(Status.OFFICE).questionType(QuestionType.OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("월급 200 받는 백수", "월급 500 받는 직장인")).build());
//
//
//            // 기타
//            // 단답형
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("요즘 즐겨 듣는 음악은 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("최근에 본 영상 중 가장 인상 깊었던 건 뭐였어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("오늘 아침은 뭘 먹었어? 최근 아침에 자주 먹는 메뉴가 있다면?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("오늘 하루 일정 중 가장 기억에 남는 순간은 뭐였어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("자기 전에 요즘 자주 하는 일이나 루틴이 있어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("최근 맡았던 일 중 가장 도전적이었던 과제는 뭐였어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("요즘 네가 가장 몰입해서 하고 있는 일은 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("어제와 비교하면 오늘은 어떤 점이 더 나았어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("최근 가장 큰 성취감을 느꼈던 순간은 언제였어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("내일은 어떤 하루가 되길 바라고 있어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("요즘 자주 느끼는 감정은 어떤 거야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("최근에 새로운 사람을 만났다면, 어떤 대화를 나눴어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("요즘 집중해서 배우거나 익히고 있는 게 있어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("오늘 하루 중 예기치 못하게 벌어진 일이 있다면, 그건 뭐였어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("최근에 조금 아쉬웠던 선택이나 행동이 있다면, 그건 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("요즘 네가 스스로 가장 발전했다고 느끼는 부분은 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("요즘 대화를 많이 나누는 사람은 누구야? 주로 무슨 이야기를 해?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("요즘 특별히 기억에 남는 사건이나 일이 있었다면, 그건 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("오늘 하루를 마치며 감사했던 순간은 뭐였어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("요즘 스스로 부족하다고 느낀 부분이 있다면, 그건 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("오늘 하루를 돌이켜보며, 내일은 어떤 점을 바꿔보고 싶어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("최근 너만 아는 소소한 재미있는 일이 있다면, 그건 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("최근 산책하거나 이동 중에 본 풍경 중 기억에 남는 건 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("최근 웃겼던 일이 있다면?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("요즘 네 기분을 색으로 표현하면, 어떤 색일까?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("최근 가장 듣고 싶었던 말이 있다면, 그건 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("요즘 시간이 있다면 가장 하고 싶은 일이 뭐야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("요즘 가장 즐거운 시간을 보낸 장소는 어디야?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("최근에 너를 가장 설레게 했던 일이나 계획이 있어?").build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(SHORT_ANSWER).content("오늘의 TMI 하나 말해줘!").build());
//
//            // 선택형
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("오늘 물을 얼마나 마셨어?").answerOptions(Arrays.asList("0잔", "1~3잔", "4잔 이상")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("오늘 커피를 얼마나 마셨어?").answerOptions(Arrays.asList("0잔", "1~2잔", "3잔 이상")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("오늘 운동을 얼마나 했어?").answerOptions(Arrays.asList("30분 이하", "30분 ~ 1시간 30분", "1시간 30분 이상")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("오늘 휴대폰을 얼마나 했어?").answerOptions(Arrays.asList("1시간 이하", "1 ~ 3시간", "3시간 이상")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("오늘 받은 스트레스 정도는?").answerOptions(Arrays.asList("1", "2", "3")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("오늘 몇 시간 잤어?").answerOptions(Arrays.asList("6시간 이하", "6~8시간", "8시간 이상")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("오늘 식사는 몇 끼 했어?").answerOptions(Arrays.asList("1끼", "2끼", "3끼 이상")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("오늘 외출한 시간은 얼마나 됐어?").answerOptions(Arrays.asList("30분 이하", "30분 ~ 2시간", "2시간 이상")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("오늘 얼마나 책을 읽었어?").answerOptions(Arrays.asList("읽지 않음", "1시간 이하", "1시간 이상")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("오늘 사람들과 얼마나 대화했어?").answerOptions(Arrays.asList("거의 대화 안 함", "30분 이하", "30분 이상")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("오늘 얼마나 계획대로 됐어?").answerOptions(Arrays.asList("전혀 계획대로 안 됨", "일부 계획대로 됨", "완전히 계획대로 됨")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("오늘 얼마나 바쁜 하루였어?").answerOptions(Arrays.asList("전혀 바쁘지 않았음", "조금 바쁨", "매우 바쁨")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("진정한 친구 1명", "친구 100명")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("똥 먹은 적 없는데 똥 먹었다고 소문나기", "똥 먹었는데 아무도 모르기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("1년 동안 폰 없이 살기", "1년 동안 친구없이 혼자 지내기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("똥먹기", "10억 받기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("개구리 맛 초콜릿", "초콜릿 맛 개구리")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("팔만대장경 손으로 옮겨쓰기", "대장내시경 팔만번 하기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("평생 안 자도 안 피곤함", "아무리 먹어도 살 안 찜")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("100% 확률로 500만원", "20% 확률로 100억")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("머리 5일 안감기", "세수 5일 안하기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("오이케이크 먹기", "김치케이크 먹기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("사생활 노출", "노출로 생활하기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("여름에 히터", "겨울에 에어컨")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("목욕탕에 불나면 얼굴 가리기", "주요 부위 가리기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("3일 굶기", "3일 밤새기")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("잠수 이별", "환승 이별")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("비상계단 키스", "극장 키스")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("모든 사람 생각 읽기", "거짓말하면 죽는 병")).build());
//            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("평생 대중교통 무료로 타기", "3년에 한 번 비행기 무료로 타기")).build());
//        };
//    }
//}