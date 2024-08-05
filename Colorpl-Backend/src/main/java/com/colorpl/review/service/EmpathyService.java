package com.colorpl.review.service;

import com.colorpl.member.Member;
import com.colorpl.review.domain.Empathy;
import com.colorpl.review.domain.Review;
import com.colorpl.review.repository.EmpathyRepository;
import com.colorpl.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmpathyService {
    @Autowired
    private EmpathyRepository empathyRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public void addEmpathy(Long reviewId, Member member) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));

        Optional<Empathy> existingEmpathy = empathyRepository.findByReviewAndMember(review, member);

        if (existingEmpathy.isPresent()) {
            Empathy empathy = existingEmpathy.get();
            if (!empathy.getMyempathy()) {
                empathy.activate();
                empathyRepository.save(empathy);
                review.incrementEmphathy();
                reviewRepository.save(review);
            } else {
                throw new IllegalStateException("Already empathized with this review");
            }
        } else {
            Empathy empathy = Empathy.builder()
                    .review(review)
                    .member(member)
                    .build();
            empathyRepository.save(empathy);
            review.incrementEmphathy();
            reviewRepository.save(review);
        }
    }

    public void removeEmpathy(Long reviewId, Member member) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));

        Empathy empathy = empathyRepository.findByReviewAndMember(review, member)
                .orElseThrow(() -> new IllegalStateException("No empathy found for this review by the member"));

        if (empathy.getMyempathy()) {
            empathy.deactivate();
            empathyRepository.save(empathy);
            review.decrementEmphathy();
            reviewRepository.save(review);
        } else {
            throw new IllegalStateException("Empathy is already removed");
        }
    }
}

