package com.colorpl.review.repository;

import com.colorpl.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 사용자 이름으로 리뷰 찾기
//    List<Review> findByUserId(Long reviewId);

//    List<Review> findByScheduleMemberId(Integer memberId);
}
