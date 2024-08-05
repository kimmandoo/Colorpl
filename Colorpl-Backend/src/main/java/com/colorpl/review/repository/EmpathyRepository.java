package com.colorpl.review.repository;

import com.colorpl.member.Member;
import com.colorpl.review.domain.Empathy;
import com.colorpl.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpathyRepository extends JpaRepository<Empathy, Long> {

    Optional<Empathy> findByReviewAndMember(Review review, Member member);
}
