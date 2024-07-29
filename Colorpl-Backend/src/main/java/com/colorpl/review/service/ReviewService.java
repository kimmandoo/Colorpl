package com.colorpl.review.service;

import com.colorpl.comment.domain.Comment;
import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.global.common.exception.MemberMismatchException;
import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.global.common.exception.ReservationDetailNotFoundException;
import com.colorpl.global.common.exception.ReviewNotFoundException;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.reservation.domain.Reservation;
import com.colorpl.review.domain.Review;
import com.colorpl.review.dto.ReviewDTO;
import com.colorpl.review.dto.ReviewUpdateDTO;
import com.colorpl.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    public List<ReviewDTO> findAll() {
        // 전체 리뷰 받아서 반환
        return reviewRepository.findAll().stream()
                .map(ReviewDTO::toReviewDTO)
                .collect(Collectors.toList());
    }



    @Transactional
    public ReviewDTO createReview(Integer memberId, ReviewDTO reviewDTO) {
        // 멤버 정보 가져오기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        // 리뷰 빌더
        Review review = Review.builder()
                .content(reviewDTO.getContent())
                .spoiler(reviewDTO.getSpoiler())
                .emotion(reviewDTO.getEmotion())
                .comments(reviewDTO.getComments())
                .emphathy(reviewDTO.getEmpathy())
                .build();
        // 리뷰 저장
        Review savedReview = reviewRepository.save(review);
        // DTO로 반환
        return ReviewDTO.toReviewDTO(savedReview);
    }

    @Transactional
    public ReviewDTO updateReview(Integer memberId, Integer reviewId, ReviewDTO reviewDTO) {
        // 리뷰 가져오기
        Review review = reviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);;
        // 리뷰 업데이트
        review.updateReview(reviewDTO.getContent(), reviewDTO.getSpoiler(), reviewDTO.getEmotion());
        Review updatedReview = reviewRepository.save(review);
        return reviewDTO.toReviewDTO(updatedReview);
    }


    @Transactional
    public void deleteById(Integer id) {
        // 리뷰 찾기, 못찾으면 NoSuchElementException
        if (!reviewRepository.existsById(id)) {
            throw new NoSuchElementException("Review with ID " + id + " does not exist.");
        }
        // 삭제
        reviewRepository.deleteById(id);
    }


}