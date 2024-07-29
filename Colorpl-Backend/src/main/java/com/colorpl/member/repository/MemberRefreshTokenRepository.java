package com.colorpl.member.repository;

import com.colorpl.member.MemberRefreshToken;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRefreshTokenRepository extends JpaRepository<MemberRefreshToken, Integer> {
    Optional<MemberRefreshToken> findByMemberIdAndReissueCountLessThan(Integer memberId,
        long count);

    Optional<MemberRefreshToken> findByMemberId(Integer id);
}