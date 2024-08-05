package com.colorpl.comment.service;

import com.colorpl.comment.domain.Comment;
import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.comment.repository.CommentRepository;
import com.colorpl.global.common.exception.MemberMismatchException;
import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.global.common.exception.ReviewNotFoundException;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.review.domain.Review;
import com.colorpl.review.repository.ReviewRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    public CommentService(CommentRepository commentRepository, ReviewRepository reviewRepository, MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.reviewRepository = reviewRepository;
        this.memberRepository = memberRepository;
    }

//    public Page<Comment> getCommentsByReview(Review review, Pageable pageable) {
//        return commentRepository.findByReview(review, pageable);
//    }

    // 각 리뷰의 댓글 조회
    @Transactional(readOnly = true)
    public Page<CommentDTO> getCommentsByReviewId(Long reviewId, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findByReviewId(reviewId, pageable);
        return commentPage.map(CommentDTO::fromComment);
    }

    // 리뷰에 댓글 생성
    @Transactional
    public CommentDTO createComment(Long reviewId, Integer memberId, CommentDTO commentDTO) {
        // 리뷰 및 멤버 가져오기
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        // 생성 후 저장
        Comment comment = Comment.builder()
                .member(member)
                .review(review)
                .comment_content(commentDTO.getCommentContent())

                .build();

        Comment createdComment = commentRepository.save(comment);
        // DTO로 반환
        return CommentDTO.fromComment(createdComment);
    }

    // 댓글 수정
    @Transactional
    public CommentDTO updateComment(Long commentId, Integer memberId, CommentDTO commentDTO) {
        // 해당 댓글 찾기
        Comment comment = commentRepository.findById(commentId)
                // 댓글 작성자와 동일한지 확인
                .filter(c -> c.getMember().getId().equals(memberId))
                .orElseThrow(MemberMismatchException::new);

        Review review = reviewRepository.findById(commentDTO.getReviewId())
                .orElseThrow(ReviewNotFoundException::new);
        Member member = memberRepository.findById(commentDTO.getMemberId())
                .orElseThrow(MemberNotFoundException::new);
        // 혹시 멤버나 리뷰 id 없으면 예외처리
        if (commentDTO.getReviewId() == null || commentDTO.getMemberId() == null) {
            throw new IllegalArgumentException("Review ID service must not be null");
        }

        // 댓글 필드 수정
        comment.updateComment(
                review,
                member,
                commentDTO.getCommentContent()
        );

        // 수정된 댓글 저장 및 DTO로 반환
        Comment updatedComment = commentRepository.save(comment);
        return CommentDTO.fromComment(updatedComment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteById(Long commentId) {
        // 존재 확인 후 삭제
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
        }
    }
}
