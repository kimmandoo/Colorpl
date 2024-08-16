package com.colorpl.review.repository;

import com.colorpl.review.domain.Empathy;
import com.colorpl.review.domain.EmpathyId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpathyRepository extends JpaRepository<Empathy, EmpathyId> {
    Optional<Empathy> findByReviewIdAndMemberId(Long reviewId, Integer memberId);
}
