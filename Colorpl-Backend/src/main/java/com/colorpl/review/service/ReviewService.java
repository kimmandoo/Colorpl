package com.colorpl.review.service;

import com.colorpl.comment.domain.Comment;
import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.global.common.exception.ReviewNotFoundException;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.review.domain.Review;
import com.colorpl.review.dto.ReviewDTO;
import com.colorpl.review.repository.ReviewRepository;
import com.colorpl.ticket.domain.Ticket;
import com.colorpl.ticket.domain.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public List<ReviewDTO> findAll() {
        // 전체 리뷰 받아서 반환
        return reviewRepository.findAll().stream()
                .map(ReviewDTO::toReviewDTO)
                .collect(Collectors.toList());
    }


    public List<ReviewDTO> getReviews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reviewRepository.findAll(pageable).getContent().stream()
                .map(ReviewDTO::toReviewDTO)
                .collect(Collectors.toList());
    }


    // need test here
    public List<ReviewDTO> findReviewsOfMembers(Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        return member.getTickets().stream()
                .map(Ticket::getReview)
                .filter(Objects::nonNull)
                .map(ReviewDTO::toReviewDTO)
                .collect(Collectors.toList());
    }



    @Transactional(readOnly = true)
    public ReviewDTO findById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review != null) {
            return ReviewDTO.toReviewDTO(review);
        }
        return null; // or throw an exception if you prefer
    }


    private List<Comment> convertToComments(List<CommentDTO> commentDTOs, Review review, Member member) {
        return commentDTOs.stream()
                .map(dto -> dto.toComment (member, review))
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDTO createReview(Integer memberId,Long ticketId, ReviewDTO reviewDTO) {
        // 멤버 및 티켓 가져오기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        System.out.println("pass member");

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));


        // Build review entity from DTO
        Review review = Review.builder()
                .ticket(ticket)
                .content(reviewDTO.getContent())
                .spoiler(reviewDTO.getSpoiler())
                .emotion(reviewDTO.getEmotion())
                .emphathy(reviewDTO.getEmpathy())
                .build();

        Review savedReview = reviewRepository.save(review);

        // DTO의 댓글들 엔티티화
        List<Comment> comments = reviewDTO.getComments().stream()
                .map(dto -> dto.toComment(member, savedReview))
                .toList();


        // DTO 반환
        return ReviewDTO.toReviewDTO(savedReview);
    }


    @Transactional
    public ReviewDTO updateReview(Integer memberId, Long reviewId, ReviewDTO reviewDTO) {
        // 리뷰 가져오기
        Review review = reviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        ;
        // 리뷰 업데이트
        review.updateReview(reviewDTO.getContent(), reviewDTO.getSpoiler(), reviewDTO.getEmotion());
        Review updatedReview = reviewRepository.save(review);
        return reviewDTO.toReviewDTO(updatedReview);
    }


    @Transactional
    public void deleteById(Long id) {
        // 리뷰 찾기, 못찾으면 NoSuchElementException
        if (!reviewRepository.existsById(id)) {
            throw new ReviewNotFoundException();
        }
        // 삭제
        reviewRepository.deleteById(id);
    }


    private ReviewDTO convertToDTO(Review review) {
        List<CommentDTO> commentDTOs = Optional.ofNullable(review.getComments())
                .orElse(Collections.emptyList())
                .stream()
                .map(CommentDTO::toCommentDTO) // Convert each Comment to CommentDTO
                .collect(Collectors.toList());

        return ReviewDTO.builder()
                .id(review.getId())
                .content(review.getContent())
                .spoiler(review.getSpoiler())
                .emotion(review.getEmotion())
                .empathy(review.getEmphathy())
                .comments(commentDTOs)
                .build();
    }

}