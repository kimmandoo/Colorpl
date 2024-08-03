package com.colorpl.review.controller;

import com.colorpl.review.dto.ReviewDTO;
import com.colorpl.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "모든 리뷰 조회", description = "모든 리뷰를 조회 할 때 사용하는 API")
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<ReviewDTO> reviews = reviewService.findAll();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    // 무한 스크롤
    @GetMapping
    @Operation(summary = "리뷰 무한 스크롤", description = "무한 스크롤옹 리뷰를 단위별로 가져오는 API")
    public List<ReviewDTO> getReviews(@RequestParam int page, @RequestParam int size) {
        return reviewService.getReviews(page, size);
    }

    // 특정 멤버의 모든 리뷰 조회
    @GetMapping("/members/{memberId}")
    @Operation(summary = "특정 멤버의 모든 리뷰 조회", description = "특정 멤버의 모든 리뷰 조회 할 때 사용하는 API(url의 멤버id, 사용)")
    public ResponseEntity<List<ReviewDTO>> findReviewsOfMembers(@PathVariable Integer memberId) {
        List<ReviewDTO> reviews = reviewService.findReviewsOfMembers(memberId);
        if (reviews == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    // 리뷰 생성
    @PostMapping("/members/{memberId}/tickets/{ticketId}")
    @Operation(summary = "새로운 리뷰 작성", description = "리뷰 생성 시 사용하는 API(url의 멤버id, 티켓 id 사용)")
    public ResponseEntity<ReviewDTO> createReview(@PathVariable Integer memberId, @PathVariable Long ticketId, @RequestBody ReviewDTO reviewDTO) {
        System.out.println(memberId);
        ReviewDTO createdReview = reviewService.createReview(memberId, ticketId, reviewDTO);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    // 리뷰 업데이트
    @PutMapping("/members/{memberId}/reviews/{reviewId}")
    @Operation(summary = "특정 멤버의 모든 예매 조회", description = "특정 멤버의 모든 예매 조회 할 때 사용하는 API")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Integer memberId,  @PathVariable Long reviewId, @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updatedReview = reviewService.updateReview(memberId, reviewId, reviewDTO);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    @Operation(summary = "특정 멤버의 모든 예매 조회", description = "특정 멤버의 모든 리뷰 조회 할 때 사용하는 API(url의 멤버id 사용)")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteById(reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}