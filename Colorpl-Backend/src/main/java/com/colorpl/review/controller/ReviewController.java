package com.colorpl.review.controller;

import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.global.common.exception.ReviewNotFoundException;
import com.colorpl.member.service.MemberService;
import com.colorpl.review.dto.*;
import com.colorpl.review.service.EmpathyService;
import com.colorpl.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import java.net.URI;
import java.util.Collections;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


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
    @Operation(summary = "특정 멤버의 모든 리뷰 조회", description = "특정 멤버의 모든 리뷰 조회 할 때 사용하는 API (예외처리 : 멤버 -1)")
    public ResponseEntity<ReadReviewResponse> findReviewsOfMembers(@PathVariable Integer memberId,
        @RequestParam int page, @RequestParam int size) {
        try {
            ReadReviewResponse reviews = reviewService.findReviewsOfMember(memberId, page, size);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (MemberNotFoundException e) {
            ReadReviewResponse reviews = ReadReviewResponse.builder().items(Collections.emptyList()).totalPage(-1).build();
            return new ResponseEntity<>(reviews, HttpStatus.NOT_FOUND);
        }

    }

    // 특정 멤버의 리뷰 수 구하기
    @GetMapping("/members/{memberId}/numbers")
    @Operation(summary = "특정 멤버의 모든 리뷰 개수 조회", description = "특정 멤버의 모든 리뷰 개수 조회할 때 사용하는 API (예외처리 : 멤버 -1)")
    public ResponseEntity<NumbersReviewResponse> findReviewNumbersOfRespectiveMember(@PathVariable Integer memberId) {
//        Integer memberId = memberService.getCurrentMemberId();
        try {
            NumbersReviewResponse reviews = reviewService.findReviewNumbersOfMember(memberId);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (MemberNotFoundException e) {
            NumbersReviewResponse reviews = NumbersReviewResponse.builder().numbers(-1).build();
            return new ResponseEntity<>(reviews, HttpStatus.NOT_FOUND);
        }
    }

    // 로그인 멤버의 모든 리뷰 조회
    @GetMapping("/myreviews")
    @Operation(summary = "로그인 멤버의 모든 리뷰 조회", description = "로그인 한 멤버의 모든 리뷰 조회 할 때 사용하는 API (예외처리 : 멤버 -1)")
    public ResponseEntity<ReadReviewResponse> findReviewNumbersOfMembers( @RequestParam int page, @RequestParam int size) {
        Integer memberId = memberService.getCurrentMemberId();
        System.out.println(memberId);
        try {
            ReadReviewResponse reviews = reviewService.findReviewsOfMember(memberId, page, size);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (MemberNotFoundException e) {
            ReadReviewResponse reviews = ReadReviewResponse.builder().items(Collections.emptyList()).totalPage(-1).build();
            return new ResponseEntity<>(reviews, HttpStatus.NOT_FOUND);
        }
    }

    // 리뷰 한 개만 *디테일 리뷰
    @GetMapping("/details/{reviewId}")
    @Operation(summary = "특정 리뷰 조회", description = "특정 리뷰 조회 할 때 사용하는 API (예외처리 : id = -1l")
    public ResponseEntity<ReviewDTO> findOneReview(@PathVariable Long reviewId) {
        Integer memberId = memberService.getCurrentMemberId();
        try {
            ReviewDTO reviews = reviewService.findById(reviewId, memberId);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (ReviewNotFoundException e) {
            ReviewDTO reviews = ReviewDTO.builder().id(-1L).commentpagesize(-1).build();
                return new ResponseEntity<>(reviews, HttpStatus.NOT_FOUND);
        }

    }

    // 새 리뷰 작성
    @PostMapping("/create")
    @Operation(summary = "새로운 리뷰 작성", description = "리뷰 생성 시 사용하는 API (예외처리 : 멤버 -1, 스케줄 -2)")
    public ResponseEntity<NonReadReviewResponse> createReview(
        @RequestPart RequestDTO request,
        @RequestPart(required = false) MultipartFile file
    ) {
        try {
            Integer memberId = memberService.getCurrentMemberId();
            Long id = reviewService.createReview(memberId, request, file);
            URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/reviews/{id}")
                    .buildAndExpand(id)
                    .toUri();
            System.out.println(uri);
            NonReadReviewResponse response = NonReadReviewResponse.builder()
                    .reviewId(id)
                    .build();
//        throw new RuntimeException("at least schedule");
            return ResponseEntity.created(uri).body(response);
        } catch (MemberNotFoundException e) {
            NonReadReviewResponse reviews = NonReadReviewResponse.builder().reviewId(-1L).numbers(-1).build();
            return new ResponseEntity<>(reviews, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            NonReadReviewResponse reviews = NonReadReviewResponse.builder().reviewId(-2L).numbers(-2).build();
            return new ResponseEntity<>(reviews, HttpStatus.NOT_FOUND);
        }

    }


    // 리뷰 업데이트
    @PutMapping("/update/{reviewId}")
    @Operation(summary = "특정 리뷰의 내용 수정", description = "특정 리뷰의 내용 수정할 때 사용하는 API(예외처리 : 리뷰 -1)")
    public ResponseEntity<NonReadReviewResponse> updateReview(@PathVariable Long reviewId,
        @RequestBody RequestDTO requestDTO) {
        try {
            Integer memberId = memberService.getCurrentMemberId();
            ReviewDTO updatedReview = reviewService.updateReview(memberId, reviewId, requestDTO);
            NonReadReviewResponse response = NonReadReviewResponse.builder()
                    .reviewId(reviewId)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ReviewNotFoundException e) {
            NonReadReviewResponse reviews = NonReadReviewResponse.builder().reviewId(-1L).numbers(-1).build();
            return new ResponseEntity<>(reviews, HttpStatus.NOT_FOUND);
        }

    }

    // 리뷰 삭제
    @DeleteMapping("/delete/{reviewId}")
    @Operation(summary = "특정 리뷰 삭제", description = "특정 리뷰 삭제할 때 사용하는 API (예외처리 : 리뷰 -1)")
    public ResponseEntity<NonReadReviewResponse> deleteReview(@PathVariable Long reviewId) {
        try {
            reviewService.deleteById(reviewId);
            NonReadReviewResponse response = NonReadReviewResponse.builder()
                    .reviewId(reviewId)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ReviewNotFoundException e) {
            NonReadReviewResponse reviews = NonReadReviewResponse.builder().reviewId(-1L).numbers(-1).build();
            return new ResponseEntity<>(reviews, HttpStatus.NOT_FOUND);
        }
    }

    // 특정 리뷰에 공감 추가
    @PostMapping("/empathize/{reviewId}")
    @Operation(summary = "특정 리뷰에 공감 추가", description = "특정 리뷰에 공감할 때 사용하는 API(예외처리 : 리뷰 -1, 멤버: -2, 공감중복 -3)")
    public ResponseEntity<NonReadReviewResponse> addEmpathy(@PathVariable Long reviewId) {
        Integer memberId = memberService.getCurrentMemberId();
        try {
            empathyService.addEmpathy(reviewId, memberId);
            NonReadReviewResponse response = NonReadReviewResponse.builder()
                    .reviewId(reviewId)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ReviewNotFoundException e) {
            NonReadReviewResponse reviews = NonReadReviewResponse.builder().reviewId(-1L).numbers(-1).build();
            return new ResponseEntity<>(reviews, HttpStatus.NOT_FOUND);
        } catch (MemberNotFoundException e) {
            NonReadReviewResponse reviews = NonReadReviewResponse.builder().reviewId(-2L).numbers(-2).build();
            return new ResponseEntity<>(reviews, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            NonReadReviewResponse reviews = NonReadReviewResponse.builder().reviewId(-3L).numbers(-3).build();
            return new ResponseEntity<>(reviews, HttpStatus.NOT_FOUND);
        }

    }

    // 특정 리뷰에 공감 삭제
    // 수정 필요!
    @DeleteMapping("/empathize/{reviewId}")
    @Operation(summary = "특정 리뷰에 공감 취소", description = "특정 리뷰에 공감 취소할 때 사용하는 API(예외처리 : 리뷰 -1, 멤버: -2, 공감x -3)")
    public ResponseEntity<NonReadReviewResponse> removeEmpathy(@PathVariable Long reviewId) {
        Integer memberId = memberService.getCurrentMemberId();
        try {
            empathyService.removeEmpathy(reviewId, memberId);
            NonReadReviewResponse response = NonReadReviewResponse.builder()
                    .reviewId(reviewId)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ReviewNotFoundException e) {
            NonReadReviewResponse reviews = NonReadReviewResponse.builder().reviewId(-1L).numbers(-1).build();
            return new ResponseEntity<>(reviews, HttpStatus.NOT_FOUND);
        } catch (MemberNotFoundException e) {
            NonReadReviewResponse reviews = NonReadReviewResponse.builder().reviewId(-2L).numbers(-2).build();
            return new ResponseEntity<>(reviews, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            NonReadReviewResponse reviews = NonReadReviewResponse.builder().reviewId(-3L).numbers(-3).build();
            return new ResponseEntity<>(reviews, HttpStatus.NOT_FOUND);
        }

    }

}