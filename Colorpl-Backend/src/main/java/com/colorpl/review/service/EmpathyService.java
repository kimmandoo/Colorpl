package com.colorpl.review.service;

import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.review.domain.Empathy;
import com.colorpl.review.domain.EmpathyId;
import com.colorpl.review.domain.Review;
import com.colorpl.review.repository.EmpathyRepository;
import com.colorpl.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpathyService {

    @Autowired
    private EmpathyRepository empathyRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public void addEmpathy(Long reviewId, Integer memberId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid memberId"));

        EmpathyId empathyId = new EmpathyId(reviewId, memberId);

        if (empathyRepository.findById(empathyId).isPresent()) {
            throw new IllegalStateException("Already empathized with this review");
        }

        Empathy empathy = new Empathy(empathyId, review, member);
        empathyRepository.save(empathy);
        review.incrementEmphathy();
        reviewRepository.save(review);
    }

    @Transactional
    public void removeEmpathy(Long reviewId, Integer memberId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid memberId"));

        EmpathyId empathyId = new EmpathyId(reviewId, memberId);

        Empathy empathy = empathyRepository.findById(empathyId)
                .orElseThrow(() -> new IllegalStateException("No empathy found for this review by the member"));

        empathyRepository.delete(empathy);
        review.decrementEmphathy();
        reviewRepository.save(review);
    }

    public boolean checkEmpathy(Long reviewId, Integer memberId) {
        EmpathyId empathyId = new EmpathyId(reviewId, memberId);

        return empathyRepository.findById(empathyId).isPresent();
    }
}
