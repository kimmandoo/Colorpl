package com.colorpl.member;

import com.colorpl.global.common.BaseEntity;
import com.colorpl.global.common.exception.CategoryLimitException;
import com.colorpl.member.dto.MemberDTO;
import com.colorpl.reservation.domain.Reservation;
import com.colorpl.schedule.command.domain.Schedule;
import com.colorpl.show.domain.detail.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Member extends BaseEntity {

    @Id
    @Column(name = "MEMBER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String email;

    @Column
    private String nickname;
    private String profile;

    private String password;

    @Enumerated(EnumType.STRING)
    private MemberType type;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Category> categories;

    private static final int MAX_CATEGORIES = 2;

//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)  // 리뷰와 연관된 코드 추가
//    private List<Review> reviews;

    @ManyToMany
    @JoinTable(name = "member_following", joinColumns = @JoinColumn(name = "member_id"), inverseJoinColumns = @JoinColumn(name = "following_id"))
    private Set<Member> followingList;

    @ManyToMany(mappedBy = "followingList")
    private Set<Member> followerList;


    // 연관관계 편의 메서드
    public void addReservation(Reservation reservation) {
        if (reservations.contains(reservation)) {
            throw new IllegalArgumentException("이미 추가된 예매입니다.");
        }
        reservations.add(reservation);
        reservation.updateMember(this);
    }

    public void removeReservation(Reservation reservation) {
        if (!reservations.contains(reservation)) {
            throw new IllegalArgumentException("삭제할 예매가 없습니다.");
        }
        reservations.remove(reservation);
        reservation.updateMember(null);
    }

    public void addSchedule(Schedule schedule) {
        if (schedules.contains(schedule)) {
            throw new IllegalArgumentException("이미 등록된 일정입니다.");
        }
        schedules.add(schedule);
        schedule.updateMember(this);
    }

    public void removeSchedule(Schedule schedule) {
        if (!schedules.contains(schedule)) {
            throw new IllegalArgumentException("삭제할 일정이 없습니다.");
        }
        schedules.remove(schedule);
        schedule.updateMember(null);
    }

    public void addFollowing(Member member) {
        followingList.add(member);
        member.getFollowerList().add(this);
    }

    public void removeFollowing(Member member) {
        followingList.remove(member);
        member.getFollowerList().remove(this);
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfile(String profile) {
        this.profile = profile;
    }

    public void updateCategories(Set<Category> categories) {
        if (categories.size() > MAX_CATEGORIES) {
            throw new CategoryLimitException();
        }
        this.categories = categories;
    }

    public static Member toMember(MemberDTO memberDTO, PasswordEncoder encoder) {
        return Member.builder().email(memberDTO.getEmail())
            .password(encoder.encode(memberDTO.getPassword())).nickname(memberDTO.getNickname())
            .categories(memberDTO.getCategories()).profile(memberDTO.getProfile())
            .type(MemberType.USER)  // 기본 값으로 USER 설정
            .build();
    }

    public void updateMember(Member member, PasswordEncoder encoder) {
        this.email = member.getEmail();
        this.password = encoder.encode(member.getPassword());
        this.nickname = member.getNickname();
        this.profile = member.getProfile();
        this.categories = member.getCategories();
        this.type = member.getType();
    }

    public String getUsername() {
        return email;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + type.name()));
    }
}
