package com.example.toyou.app;

import com.example.toyou.domain.CustomQuestion;
import com.example.toyou.repository.CustomQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

import static com.example.toyou.domain.enums.Emotion.*;
import static com.example.toyou.domain.enums.QuestionType.*;
import static com.example.toyou.domain.enums.Status.*;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final CustomQuestionRepository customQuestionRepository;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            // 중고등학생
            // 불안
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(NERVOUS).questionType(LONG_ANSWER).content("시험 전날에는 보통 어떤 생각이 들어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(NERVOUS).questionType(LONG_ANSWER).content("새로운 학기가 시작될 때 불안하거나 걱정되는 점이 있어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(NERVOUS).questionType(LONG_ANSWER).content("발표할 때 가장 불안한 순간은 언제야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(NERVOUS).questionType(LONG_ANSWER).content("큰 결정을 내려야 할 때 불안한 마음을 어떻게 달래?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(NERVOUS).questionType(LONG_ANSWER).content("친구들 앞에서 무대에 서야 할 때 어떤 기분이야?").build());
            // 흥분
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(EXCITED).questionType(LONG_ANSWER).content("친구들과 특별한 이벤트를 계획할 때 가장 흥분되는 순간은 언제야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(EXCITED).questionType(LONG_ANSWER).content("새로운 취미나 활동을 시작할 때 기분이 어때?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(EXCITED).questionType(LONG_ANSWER).content("좋아하는 영화 시리즈의 새로운 작품이 개봉할 때 어떤 기분이야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(EXCITED).questionType(LONG_ANSWER).content("좋아하는 가수의 콘서트를 보러 갈 때 가장 흥분되는 순간은 언제야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(EXCITED).questionType(LONG_ANSWER).content("학교에서 특별한 행사가 있을 때 가장 신나는 순간은 언제야?").build());

            // 행복
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(HAPPY).questionType(LONG_ANSWER).content("가족과 함께 보낸 특별한 순간 중에 기억에 남는 게 뭐야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(HAPPY).questionType(LONG_ANSWER).content("친구들과 재미있는 이야기를 나누며 웃었던 순간은 언제야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(HAPPY).questionType(LONG_ANSWER).content("성취한 것 중에서 가장 큰 자부심을 느낀 때는 언제야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(HAPPY).questionType(LONG_ANSWER).content("좋아하는 음식을 먹을 때 가장 행복한 순간은 언제야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(HAPPY).questionType(LONG_ANSWER).content("꿈꾸던 일이 현실이 되었을 때 어떤 기분이야?").build());
            // 화남
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(ANGRY).questionType(LONG_ANSWER).content("어떤 상황에서 가장 화가 나?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(ANGRY).questionType(LONG_ANSWER).content("진심으로 실망했던 경험이 있어? 어떻게 대처했어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(ANGRY).questionType(LONG_ANSWER).content("가족이나 친구들과의 갈등을 해결하는 데 가장 힘들었던 경험은 뭐야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(ANGRY).questionType(LONG_ANSWER).content("내가 부당한 대우를 받았을 때 어떻게 대처해?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(ANGRY).questionType(LONG_ANSWER).content("화가 났을 때 스트레스를 어떻게 해소해?").build());
            // 평범
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(NORMAL).questionType(LONG_ANSWER).content("혼자 있을 때 주로 어떤 생각과 기분이 들어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(NORMAL).questionType(LONG_ANSWER).content("오늘 하루 어땠어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(NORMAL).questionType(LONG_ANSWER).content("요즘 재밌는 일 있어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(NORMAL).questionType(LONG_ANSWER).content("하루 중 가장 마음이 편안해지는 순간은 언제야? 이유는?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(NORMAL).questionType(LONG_ANSWER).content("아침에 일어날 때 가장 먼저 드는 생각이 뭐야?").build());
            // 단답형(일상)
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(null).questionType(SHORT_ANSWER).content("어떤 직업을 희망해?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(null).questionType(SHORT_ANSWER).content("오늘 급식 메뉴는 뭐였어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(null).questionType(SHORT_ANSWER).content("최애 음식이 뭐야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(null).questionType(SHORT_ANSWER).content("최애 맛집이 어디야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(null).questionType(SHORT_ANSWER).content("너의 MBTI가 뭐야?").build());
            // 선택형(일상)
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(null).questionType(OPTIONAL).content("오늘 휴대폰을 얼마나 사용했어?").answerOptions(Arrays.asList("1시간 이하", "1~2시간", "2~3시간", "3시간 이상")).build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(null).questionType(OPTIONAL).content("오늘 가장 기분 좋았던 시간은 언제야?").answerOptions(Arrays.asList("아침", "점심", "저녁", "밤")).build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(null).questionType(OPTIONAL).content("오늘 몇 끼를 먹었어?").answerOptions(Arrays.asList("1끼", "2끼", "3끼", "4끼 이상")).build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(null).questionType(OPTIONAL).content("오늘 학교에서 가장 재미있었던 수업은 뭐였어?").answerOptions(Arrays.asList("국어", "영어", "수학", "사회", "과학")).build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(SCHOOL).emotion(null).questionType(OPTIONAL).content("오늘 스트레스 정도는 어땠어?").answerOptions(Arrays.asList("1", "2", "3", "4", "5")).build());



            // 대학생
            // 불안
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(NERVOUS).questionType(LONG_ANSWER).content("불안한 감정을 조절하는데 도움 되는 나만의 방법은?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(NERVOUS).questionType(LONG_ANSWER).content("과제나 시험 때문에 스트레스를 많이 받고 있어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(NERVOUS).questionType(LONG_ANSWER).content("가장 큰 스트레스 요인은 무엇인 것 같아?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(NERVOUS).questionType(LONG_ANSWER).content("학교 생활 중 가장 힘든 순간은 언제였어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(NERVOUS).questionType(LONG_ANSWER).content("공부하면서 스트레스 받을 때는 주로 어떻게 푸는 편이야?").build());
            // 흥분
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(EXCITED).questionType(LONG_ANSWER).content("요즘 가장 기대되는 일은 뭐야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(EXCITED).questionType(LONG_ANSWER).content("최근에 가장 신났던 경험은 뭐야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(EXCITED).questionType(LONG_ANSWER).content("이번 주말에 특별한 계획이 있어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(EXCITED).questionType(LONG_ANSWER).content("학교 행사 중에서 가장 기다려지는 건 뭐야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(EXCITED).questionType(LONG_ANSWER).content("가장 좋아하는 취미 활동은 뭐야?").build());
            // 행복
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(HAPPY).questionType(LONG_ANSWER).content("최근에 가장 행복했던 일은 뭐야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(HAPPY).questionType(LONG_ANSWER).content("친구들과 함께했던 재미있는 추억 한 가지를 들려줘!").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(HAPPY).questionType(LONG_ANSWER).content("나를 미소 짓게 하는 작은 일은 뭐야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(HAPPY).questionType(LONG_ANSWER).content("좋아하는 연예인 있어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(HAPPY).questionType(LONG_ANSWER).content("요즘 가장 많이 웃게 해주는 건 뭐야?").build());
            // 화남
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(ANGRY).questionType(LONG_ANSWER).content("너무 화가 나면 어떻게 해? 분노를 조절하는 나만의 방법은?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(ANGRY).questionType(LONG_ANSWER).content("최근에 화가 났던 적이 있어? 그 이유는 뭐야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(ANGRY).questionType(LONG_ANSWER).content("화가 났을 때 진정하기 위해 특별히 하는 루틴이 있어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(ANGRY).questionType(LONG_ANSWER).content("가장 화가 났던 순간을 이야기해줄래?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(ANGRY).questionType(LONG_ANSWER).content("화가 났을 때 듣는 음악이나 보는 영화가 있어?").build());
            // 평범
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(NORMAL).questionType(LONG_ANSWER).content("오늘 하루는 어땠어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(NORMAL).questionType(LONG_ANSWER).content("요즘 주말에는 주로 뭐해?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(NORMAL).questionType(LONG_ANSWER).content("평소에 가장 좋아하는 간식은 뭐야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(NORMAL).questionType(LONG_ANSWER).content("학교 수업 외에 참여하고 있는 활동은 뭐야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(NORMAL).questionType(LONG_ANSWER).content("요즘 가장 자주 가는 장소는 어디야?").build());
            // 단답형(일상)
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(null).questionType(SHORT_ANSWER).content("요즘 가장 열심히 듣는 강의가 뭐야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(null).questionType(SHORT_ANSWER).content("요즘에 푹 빠져있는 취미는?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(null).questionType(SHORT_ANSWER).content("이번 방학에 가고 싶은 여행지 있어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(null).questionType(SHORT_ANSWER).content("내가 가장 좋아하는 학교 주변 맛집은?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(null).questionType(SHORT_ANSWER).content("나를 편안하게 만들어주는 장소를 알려줘.").build());
            // 선택형(일상)
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(null).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("고3으로 돌아가서 수능 다시 보기", "지금 이대로 살기")).build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(null).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("대학원생 되기", "대학교 7년 다니기")).build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(null).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("1교시 수업인데 3연강", "2과목 강의인데 5시간 우주공강")).build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(null).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("이른 아침 수업 듣고 낮잠 자기", "밤샘 공부 후 낮 자유시간")).build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(COLLEGE).emotion(null).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("이번 학기 과탑 확정", "오늘 바로 종강하기")).build());



            // 직장인
            // 불안
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(NERVOUS).questionType(LONG_ANSWER).content("어떤 일 때문에 불안해?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(NERVOUS).questionType(LONG_ANSWER).content("업무를 볼 때 보통 사람 때문에 힘들어? 아니면 업무 난이도 때문에 힘들어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(NERVOUS).questionType(LONG_ANSWER).content("불안감이 개인 생활에까지 영향을 미쳐?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(NERVOUS).questionType(LONG_ANSWER).content("불안을 해소하기 위해 어떤 방법을 사용해?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(NERVOUS).questionType(LONG_ANSWER).content("최근에 불안했던 순간이 언제였어?").build());
            // 흥분
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(EXCITED).questionType(LONG_ANSWER).content("어떤 일이 가장 흥분되게 만들었어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(EXCITED).questionType(LONG_ANSWER).content("어떤 분야가 업무적으로 가장 흥미를 느끼게 해?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(EXCITED).questionType(LONG_ANSWER).content("최근에 흥미로운 프로젝트가 있었어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(EXCITED).questionType(LONG_ANSWER).content("새로운 도전을 할 때 어떤 기분이 들어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(EXCITED).questionType(LONG_ANSWER).content("업무 중에 가장 흥분되는 순간은 언제야?").build());
            // 행복
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(HAPPY).questionType(LONG_ANSWER).content("오늘 어떤 좋은 일이 있었어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(HAPPY).questionType(LONG_ANSWER).content("행복한 기분을 한 마디로 표현하면?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(HAPPY).questionType(LONG_ANSWER).content("회사에서 인정받은 순간이 있었다면 어떤 상황이었어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(HAPPY).questionType(LONG_ANSWER).content("최근에 가장 행복했던 순간이 언제였어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(HAPPY).questionType(LONG_ANSWER).content("어떤 일이 너를 가장 행복하게 만들어?").build());
            // 화남
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(ANGRY).questionType(LONG_ANSWER).content("화를 풀 때 어떤 방식으로 풀어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(ANGRY).questionType(LONG_ANSWER).content("회사 내 의견 충돌 때문에 화가 난 경우가 있었어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(ANGRY).questionType(LONG_ANSWER).content("어떤 일이 너를 가장 화나게 만들어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(ANGRY).questionType(LONG_ANSWER).content("화가 났을 때 어떻게 대처해?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(ANGRY).questionType(LONG_ANSWER).content("화난 상태에서 업무를 처리할 때 어떻게 해?").build());
            // 평범
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(NORMAL).questionType(LONG_ANSWER).content("오늘 일과는 어땠어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(NORMAL).questionType(LONG_ANSWER).content("다사다난한 날보다 평범한 날이 더 좋다고 생각해?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(NORMAL).questionType(LONG_ANSWER).content("업무에서 평범한 일상을 유지하는데 도움이 되는 요소는 뭐야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(NORMAL).questionType(LONG_ANSWER).content("평범한 하루를 보내면서 가장 만족스러웠던 점은 뭐야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(NORMAL).questionType(LONG_ANSWER).content("일상에서 평범함을 유지하는 방법이 있어?").build());
            // 단답형(일상)
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(null).questionType(SHORT_ANSWER).content("오늘 야근했어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(null).questionType(SHORT_ANSWER).content("오늘 점심 뭐 먹었어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(null).questionType(SHORT_ANSWER).content("지금 이직 생각이 있어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(null).questionType(SHORT_ANSWER).content("오늘 출근길은 어땠어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(null).questionType(SHORT_ANSWER).content("현재 직업을 하면서 가장 신기했던 점은 뭐야?").build());
            // 선택형(일상)
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(null).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("모두가 천재인 팀에서 숨쉬듯 자괴감 느끼기", "나만이 유일한 희망인 팀에서 소처럼 일하기")).build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(null).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("불평불만 많지만 마당발인 동료", "조용하고 차분하나 아싸 기질의 동료")).build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(null).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("안주는 김뿐인 절친과의 술자리", "초호화 고급 안주 무한리필 전체 회식 자리")).build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(null).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("일은 잘하지만 인성에 문제 있는 동료", "일을 못하지만 참 착한 동료")).build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(OFFICE).emotion(null).questionType(OPTIONAL).content("5초 안에 대답해 봐! 심심할 땐 밸런스게임").answerOptions(Arrays.asList("처음부터 끝까지 하나하나 터치하는 사수", "방목시키는 사수")).build());



            // 기타
            // 불안
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(NERVOUS).questionType(LONG_ANSWER).content("지금 불안한 이유가 뭐야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(NERVOUS).questionType(LONG_ANSWER).content("불안감을 해소하려고 어떤 걸 시도하고 있어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(NERVOUS).questionType(LONG_ANSWER).content("불안할 때 주로 어떤 행동을 해?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(NERVOUS).questionType(LONG_ANSWER).content("불안을 느낄 때 주변 사람들에게 어떻게 도움을 요청해?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(NERVOUS).questionType(LONG_ANSWER).content("...").build());
            // 흥분
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(EXCITED).questionType(LONG_ANSWER).content("들뜨고 흥분하게 된 이유는 뭐야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(EXCITED).questionType(LONG_ANSWER).content("흥분한 감정이 너의 행동에 어떻게 영향을 미쳤어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(EXCITED).questionType(LONG_ANSWER).content("흥분한 감정을 누구와 함께 공유했어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(EXCITED).questionType(LONG_ANSWER).content("최근에 흥분했던 순간을 하나 이야기해줄 수 있어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(EXCITED).questionType(LONG_ANSWER).content("...").build());
            // 행복
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(HAPPY).questionType(LONG_ANSWER).content("오늘 가장 행복한 순간은 언제였어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(HAPPY).questionType(LONG_ANSWER).content("행복한 감정이 너의 행동에 어떻게 영향을 미쳤어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(HAPPY).questionType(LONG_ANSWER).content("행복한 감정을 누구와 함께 공유했어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(HAPPY).questionType(LONG_ANSWER).content("행복을 느끼게 하는 작은 것들은 뭐가 있어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(HAPPY).questionType(LONG_ANSWER).content("...").build());
            // 화남
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(ANGRY).questionType(LONG_ANSWER).content("오늘 불만족스러웠던 일은 뭐였어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(ANGRY).questionType(LONG_ANSWER).content("분노의 감정이 너의 행동에 어떻게 영향을 미쳤어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(ANGRY).questionType(LONG_ANSWER).content("화를 가라앉히기 위해 어떤 행동을 했어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(ANGRY).questionType(LONG_ANSWER).content("최근에 화가 났던 순간을 하나 이야기해줄 수 있어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(ANGRY).questionType(LONG_ANSWER).content("...").build());
            // 평범
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(NORMAL).questionType(LONG_ANSWER).content("오늘 하루를 한 마디로 표현한다면?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(NORMAL).questionType(LONG_ANSWER).content("하루 루틴 중에서 너한테 가장 중요한 일은 뭐야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(NORMAL).questionType(LONG_ANSWER).content("평범한 하루 속 너만의 소확행(소소하지만 확실한 행복)은 뭐야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(NORMAL).questionType(LONG_ANSWER).content("일상 속에서 자주 하는 습관이나 행동은 뭐야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(NORMAL).questionType(LONG_ANSWER).content("...").build());
            // 단답형(일상)
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(null).questionType(SHORT_ANSWER).content("오늘 어떤 음악을 들었어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(null).questionType(SHORT_ANSWER).content("오늘 본 영상 중 가장 기억에 남는 영상은 뭐였어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(null).questionType(SHORT_ANSWER).content("오늘 아침에 뭐 먹었어?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(null).questionType(SHORT_ANSWER).content("오늘의 일정 중 가장 기억에 남는 순간은 뭐야?").build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(null).questionType(SHORT_ANSWER).content("...").build());
            // 선택형(일상)
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(null).questionType(OPTIONAL).content("오늘 하루 중 얼마나 물을 마셨어?").answerOptions(Arrays.asList("0잔", "1~3잔", "4잔 이상")).build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(null).questionType(OPTIONAL).content("오늘 하루 중 얼마나 커피를 마셨어? ").answerOptions(Arrays.asList("0잔", "1~2잔", "3잔 이상")).build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(null).questionType(OPTIONAL).content("오늘 얼마나 운동을 했어?").answerOptions(Arrays.asList("30분 이하", "30분 ~ 1시간 30분", "1시간 30분 이상")).build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(null).questionType(OPTIONAL).content("오늘 얼마나 휴대폰을 했어?").answerOptions(Arrays.asList("1시간 이하", "1 ~ 3시간", "3시간 이상")).build());
            customQuestionRepository.save(CustomQuestion.builder().userStatus(ETC).emotion(null).questionType(OPTIONAL).content("오늘 받은 스트레스 정도는?").answerOptions(Arrays.asList("1", "2", "3")).build());
        };
    }
}