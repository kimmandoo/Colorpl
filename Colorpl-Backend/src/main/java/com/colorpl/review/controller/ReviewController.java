package com.colorpl.review.controller;

import com.colorpl.review.dto.ReviewDTO;
import com.colorpl.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 모든 리뷰 조회
    @GetMapping("/all")
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<ReviewDTO> reviews = reviewService.findAll();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    // 무한 스크롤
    @GetMapping
    public List<ReviewDTO> getReviews(@RequestParam int page, @RequestParam int size) {
        return reviewService.getReviews(page, size);
    }

    // 특정 멤버의 모든 리뷰 조회
    @GetMapping("/members/{memberId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByMemberId(@PathVariable Integer memberId) {
        List<ReviewDTO> reviews = reviewService.findMembersAll(memberId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long reviewId) {
        ReviewDTO review = reviewService.findById(reviewId);
        if (review == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    // 리뷰 생성
    @PostMapping("/members/{memberId}")
    public ResponseEntity<ReviewDTO> createReview(@PathVariable Integer memberId, @RequestBody ReviewDTO reviewDTO) {
        System.out.println(memberId);
        ReviewDTO createdReview = reviewService.createReview(memberId, reviewDTO);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    // 리뷰 업데이트
    @PutMapping("/members/{memberId}/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Integer memberId,  @PathVariable Long reviewId, @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updatedReview = reviewService.updateReview(memberId, reviewId, reviewDTO);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteById(reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}