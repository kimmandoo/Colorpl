package com.colorpl.schedule.query.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.schedule.domain.CustomSchedule;
import com.colorpl.schedule.dto.SearchScheduleCondition;
import com.colorpl.schedule.repository.CustomScheduleRepository;
import com.colorpl.schedule.repository.ScheduleRepository;
import com.colorpl.show.domain.detail.Category;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CustomScheduleRepositoryTest {

    @Autowired
    private CustomScheduleRepository customScheduleRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Test
    void findByMemberAndDateTimeBetween() {

        LocalDateTime now = LocalDateTime.now();

        Member member = Member.builder()
            .email("email")
            .build();
        memberRepository.save(member);

        CustomSchedule createCustomSchedule = CustomSchedule.builder()
            .member(member)
            .seat("seat")
            .dateTime(now)
            .name("name")
            .category(Category.PLAY)
            .location("location")
            .latitude(36.1096007356246)
            .longitude(128.415801893962)
            .build();
        scheduleRepository.save(createCustomSchedule);

        SearchScheduleCondition condition = SearchScheduleCondition.builder()
            .member(member)
            .from(now.minusDays(1))
            .to(now.plusDays(1))
            .build();

        CustomSchedule findCustomSchedule = customScheduleRepository.search(condition).get(0);

        assertThat(findCustomSchedule.getSeat()).isEqualTo("seat");
        assertThat(findCustomSchedule.getDateTime()).isEqualTo(now);
        assertThat(findCustomSchedule.getName()).isEqualTo("name");
        assertThat(findCustomSchedule.getCategory()).isEqualTo(Category.PLAY);
        assertThat(findCustomSchedule.getLocation()).isEqualTo("location");
        assertThat(findCustomSchedule.getLatitude()).isEqualTo(36.1096007356246);
        assertThat(findCustomSchedule.getLongitude()).isEqualTo(128.415801893962);
    }
}