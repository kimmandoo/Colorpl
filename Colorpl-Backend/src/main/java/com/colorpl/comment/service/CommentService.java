package com.colorpl.comment.service;

import com.colorpl.comment.domain.Comment;
import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.comment.repository.CommentRepository;
import com.colorpl.global.common.exception.MemberMismatchException;
import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.global.common.exception.ReservationNotFoundException;
import com.colorpl.global.common.exception.ReviewNotFoundException;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.reservation.domain.Reservation;
import com.colorpl.reservation.dto.ReservationDTO;
import com.colorpl.review.domain.Review;
import com.colorpl.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    // CRUD

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    public CommentService(CommentRepository commentRepository, ReviewRepository reviewRepository, MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.reviewRepository = reviewRepository;
        this.memberRepository = memberRepository;
    }

    // read comments of review
    @Transactional
    public List<CommentDTO> getCommetnsByReviewId(Integer reviewId) {
        // 특정 리뷰의 댓글들을 리스트로 받아온다
        List<Comment> comments = commentRepository.findByReviewId(reviewId);
        if (comments.isEmpty()) {
            throw new ReviewNotFoundException();
        }
        // 변환하며 반환
        return comments.stream()
                .map(CommentDTO::toCommentDTO)
                .collect(Collectors.toList());
    }

    // create comment in review
    @Transactional
    public CommentDTO createComment(Integer reviewId, Integer memberId, CommentDTO commentDTO) {
        // review와 member 가져온다
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        // 리뷰와 멤버를 가져와 생성
        Comment comment = Comment.builder()
                .member(member)
                .review(review)
                .comment_content(commentDTO.getComment_content())
                .build();
        // 저장
        Comment created_comment = commentRepository.save(comment);
        // DTO 반환
        return CommentDTO.toCommentDTO(created_comment);
    }

    // update comment
    @Transactional
    public CommentDTO updateComment(Long commentId, Integer memberId, CommentDTO commentDTO) {

        Comment comment = commentRepository.findById(commentId)
                .filter(res -> res.getMember().getId().equals(memberId))
                .orElseThrow(MemberMismatchException::new);

        comment.updateComment(commentDTO.getReview(), commentDTO.getMember(),
                commentDTO.getComment_content());
        Comment updated_comment = commentRepository.save(comment);
        return CommentDTO.toCommentDTO(updated_comment);
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
