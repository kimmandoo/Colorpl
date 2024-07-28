package com.colorpl.comment.service;

import com.colorpl.comment.domain.Comment;
import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.comment.repository.CommentRepository;
import com.colorpl.member.Member;
import com.colorpl.member.dto.MemberDTO;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.member.service.MemberService;
import com.colorpl.review.domain.Review;
import com.colorpl.review.dto.ReviewDTO;
import com.colorpl.review.repository.ReviewRepository;
import com.colorpl.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.colorpl.comment.exception.CommentNotFoundException;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ReviewService reviewService;
    private final MemberService memberService;

    @Autowired
    public CommentService(CommentRepository commentRepository,
                          ReviewRepository reviewRepository,
                          MemberRepository memberRepository,
                          ReviewService reviewService,
                          MemberService memberService) {
        this.commentRepository = commentRepository;
        this.reviewRepository = reviewRepository;
        this.memberRepository = memberRepository;
        this.reviewService = reviewService;
        this.memberService = memberService;
    }

    public Optional<List<CommentDTO>> findCommentsByReviewId(Long reviewId) {
        return Optional.ofNullable(commentRepository.findByReviewId(reviewId));
    }





    public CommentDTO save(CommentDTO commentDTO) {
        // Add validation for commentDTO here if needed
        Comment comment = toEntity(commentDTO);
        Comment savedComment = commentRepository.save(comment);
        return toDTO(savedComment);
    }

    public CommentDTO update(Long id, CommentDTO commentDTO) {
        Comment commentToUpdate = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        commentToUpdate.setContent(commentDTO.getContent()); // Update other fields if needed

        Comment updatedComment = commentRepository.save(commentToUpdate);
        return toDTO(updatedComment);
    }

    public void deleteById(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new NoSuchElementException("Comment with ID " + id + " does not exist.");
        }
        commentRepository.deleteById(id);
    }

    private CommentDTO toDTO(Comment comment) {
        MemberDTO memberDTO = memberService.toDTO(comment.getMember());
        ReviewDTO reviewDTO = reviewService.toDTO(comment.getReview());

        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .member(memberDTO)
                .review(reviewDTO)
                .build();
    }

    private Comment toEntity(CommentDTO commentDTO) {
        Integer reviewId = commentDTO.getReview() != null ? commentDTO.getReview().getId() : null;
        Integer memberId = commentDTO.getMember() != null ? commentDTO.getMember().getId() : null;

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException("Review with ID " + reviewId + " not found"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("Member with ID " + memberId + " not found"));

        return Comment.builder()
                .id(commentDTO.getId())
                .content(commentDTO.getContent())
                .review(review)
                .member(member)
                .build();
    }

}