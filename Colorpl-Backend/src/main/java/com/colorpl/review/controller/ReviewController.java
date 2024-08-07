package com.colorpl.review.controller;

import com.colorpl.member.repository.MemberRepository;
import com.colorpl.member.service.MemberService;
import com.colorpl.review.dto.ReadReviewResponse;
import com.colorpl.review.dto.RequestDTO;
import com.colorpl.review.dto.ReviewDTO;
import com.colorpl.review.dto.NonReadReviewResponse;
import com.colorpl.review.service.EmpathyService;
import com.colorpl.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final EmpathyService empathyService;
    private final MemberService memberService;

    // 무한 스크롤 *일반 리뷰
    @GetMapping("/all")
    @Operation(summary = "리뷰 무한 스크롤", description = "무한 스크롤옹 리뷰를 단위별로 가져오는 API")
    public ReadReviewResponse getReviews(@RequestParam int page, @RequestParam int size) {
        Integer memberId = memberService.getCurrentMemberId();
        return reviewService.getReviews(memberId, page, size);
    }

    // 특정 멤버의 모든 리뷰 조회 *일반 리뷰
    @GetMapping("/members/{memberId}")
    @Operation(summary = "특정 멤버의 모든 리뷰 조회", description = "특정 멤버의 모든 리뷰 조회 할 때 사용하는 API(url의 멤버id, 사용)")
    public ResponseEntity<ReadReviewResponse> findReviewsOfMembers(@PathVariable Integer memberId, @RequestParam int page, @RequestParam int size) {
        ReadReviewResponse reviews = reviewService.findReviewsOfMember(memberId, page, size);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    // 리뷰 한 개만 *디테일 리뷰
    @GetMapping("/details/{reviewId}")
    @Operation(summary = "특정 리뷰 조회", description = "특정 리뷰 조회 할 때 사용하는 API(url의 리뷰id 및 (*주의!) param으로 memberId 사용)")
    public ResponseEntity<ReviewDTO> findReviewsOfMembers(@PathVariable Long reviewId) {
        Integer memberId = memberService.getCurrentMemberId();
        ReviewDTO reviews = reviewService.findById(reviewId, memberId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    // 새 리뷰 작성
    @PostMapping("/tickets/{ticketId}")
    @Operation(summary = "새로운 리뷰 작성", description = "리뷰 생성 시 사용하는 API(url의 멤버id, 티켓 id 사용)")
    public ResponseEntity<NonReadReviewResponse> createReview(
            @RequestPart RequestDTO request,
            @RequestPart(required = false) MultipartFile file
    ) {
        Integer memberId = memberService.getCurrentMemberId();
        Long id = reviewService.createReview(request, file);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/reviews/{id}")
                .buildAndExpand(id)
                .toUri();
        System.out.println(uri);

        NonReadReviewResponse response = NonReadReviewResponse.builder()
                .reviewId(id)
                .build();

        return ResponseEntity.created(uri).body(response);
    }


    // 리뷰 업데이트
    @PutMapping("/reviews/{reviewId}")
    @Operation(summary = "특정 리뷰의 내용 수정", description = "특정 리뷰의 내용 수정할 때 사용하는 API")
    public ResponseEntity<NonReadReviewResponse> updateReview( @PathVariable Long reviewId, @RequestBody RequestDTO requestDTO) {
        Integer memberId = memberService.getCurrentMemberId();
        ReviewDTO updatedReview = reviewService.updateReview(memberId, reviewId, requestDTO);
        NonReadReviewResponse response = NonReadReviewResponse.builder()
                .reviewId(reviewId)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    @Operation(summary = "특정 리뷰 삭제", description = "특정 리뷰 삭제할 때 사용하는 API(url의 리뷰id 사용)")
    public ResponseEntity<NonReadReviewResponse> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteById(reviewId);
        NonReadReviewResponse response = NonReadReviewResponse.builder()
                .reviewId(reviewId)
                .build();

        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    // 특정 리뷰에 공감 추가
    @PostMapping("/empathize/{reviewId}")
    @Operation(summary = "특정 리뷰에 공감 추가", description = "특정 리뷰에 공감할 때 사용하는 API")
    public ResponseEntity<NonReadReviewResponse> addEmpathy(@PathVariable Long reviewId) {
        Integer memberId = memberService.getCurrentMemberId();
        empathyService.addEmpathy(reviewId, memberId);
        NonReadReviewResponse response = NonReadReviewResponse.builder()
                .reviewId(reviewId)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    // 특정 리뷰에 공감 삭제
    // 수정 필요!
    @DeleteMapping("/members/{memberId}/empathize/{reviewId}")
    @Operation(summary = "특정 리뷰에 공감 취소", description = "특정 리뷰에 공감 취소할 때 사용하는 API")
    public ResponseEntity<NonReadReviewResponse> removeEmpathy(@PathVariable Long reviewId) {
        Integer memberId = memberService.getCurrentMemberId();
        empathyService.removeEmpathy(reviewId, memberId);
        NonReadReviewResponse response = NonReadReviewResponse.builder()
                .reviewId(reviewId)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}