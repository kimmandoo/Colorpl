package com.colorpl.review.controller;

import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.review.dto.DetailReviewDTO;
import com.colorpl.review.dto.ReviewDTO;
import com.colorpl.review.service.EmpathyService;
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
    private final EmpathyService empathyService;
    private final MemberRepository memberRepository;

    // 무한 스크롤 *일반 리뷰
    @GetMapping("/members/{memberId}/all")
    @Operation(summary = "리뷰 무한 스크롤", description = "무한 스크롤옹 리뷰를 단위별로 가져오는 API")
    public List<ReviewDTO> getReviews(@PathVariable Integer memberId, @RequestParam int page, @RequestParam int size) {
        return reviewService.getReviews(memberId, page, size);
    }

    // 특정 멤버의 모든 리뷰 조회 *일반 리뷰
    @GetMapping("/members/{memberId}")
    @Operation(summary = "특정 멤버의 모든 리뷰 조회", description = "특정 멤버의 모든 리뷰 조회 할 때 사용하는 API(url의 멤버id, 사용)")
    public ResponseEntity<List<ReviewDTO>> findReviewsOfMembers(@PathVariable Integer memberId, @RequestParam int page, @RequestParam int size) {
        List<ReviewDTO> reviews = reviewService.findReviewsOfMember(memberId, page, size);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    // 리뷰 한 개만 *디테일 리뷰
    // findbyid 활용
    @GetMapping("/details/{reviewId}")
    @Operation(summary = "특정 리뷰 조회", description = "특정 리뷰 조회 할 때 사용하는 API(url의 리뷰id 및 (*주의!) param으로 memberId 사용)")
    public ResponseEntity<ReviewDTO> findReviewsOfMembers(@PathVariable Long reviewId, @RequestParam int memberId) {
        ReviewDTO reviews = reviewService.findById(reviewId, memberId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    // 리뷰 생성
    @PostMapping("/members/{memberId}/tickets/{ticketId}")
    @Operation(summary = "새로운 리뷰 작성", description = "리뷰 생성 시 사용하는 API(url의 멤버id, 티켓 id 사용)")
    public ResponseEntity<DetailReviewDTO> createReview(@PathVariable Integer memberId, @PathVariable Long ticketId, @RequestBody DetailReviewDTO detailReviewDTO) {
//        System.out.println(memberId);
        DetailReviewDTO createdReview = reviewService.createReview(memberId, ticketId, detailReviewDTO);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    // 리뷰 업데이트
    @PutMapping("/members/{memberId}/reviews/{reviewId}")
    @Operation(summary = "특정 리뷰의 내용 수정", description = "특정 리뷰의 내용 수정할 때 사용하는 API")
    public ResponseEntity<DetailReviewDTO> updateReview(@PathVariable Integer memberId, @PathVariable Long reviewId, @RequestBody DetailReviewDTO detailReviewDTO) {
        DetailReviewDTO updatedReview = reviewService.updateReview(memberId, reviewId, detailReviewDTO);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    @Operation(summary = "특정 리뷰 삭제", description = "특정 리뷰 삭제할 때 사용하는 API(url의 리뷰id 사용)")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteById(reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/members/{memberId}/empathize/{reviewId}")
    @Operation(summary = "특정 리뷰에 공감 추가", description = "특정 리뷰에 공감할 때 사용하는 API")
    public ResponseEntity<Void> addEmpathy(@PathVariable Long reviewId, @PathVariable Integer memberId) {
        empathyService.addEmpathy(reviewId, memberId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/members/{memberId}/empathize/{reviewId}")
    @Operation(summary = "특정 리뷰에 공감 취소", description = "특정 리뷰에 공감 취소할 때 사용하는 API")
    public ResponseEntity<Void> removeEmpathy(@PathVariable Long reviewId, @PathVariable Integer memberId) {
        empathyService.removeEmpathy(reviewId, memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}