package com.colorpl.member.repository;

import com.colorpl.member.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {

    boolean existsByInvalidRefreshToken(String refreshToken);
}
