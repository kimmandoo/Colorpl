package com.colorpl.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MemberRepository memberRepository;

    @Test
    void testGetCommentsByReviewId() {
        Long reviewId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Comment comment = Comment.builder()
                .id(1L)
                .comment_content("Test comment")
                .build();
        Page<Comment> commentPage = new PageImpl<>(List.of(comment));
        when(commentRepository.findByReviewId(reviewId, pageable)).thenReturn(commentPage);

        Page<CommentDTO> result = commentService.getCommentsByReviewId(reviewId, pageable);

        verify(commentRepository, times(1)).findByReviewId(reviewId, pageable);
        assertEquals(1, result.getTotalElements());
        assertThat(result.getContent().get(0).getCommentContent()).isEqualTo(comment.getComment_content());
    }

    @Test
    void testCreateComment() {
        Long reviewId = 1L;
        Integer memberId = 1;
        CommentDTO commentDTO = CommentDTO.builder()
                .commentContent("New comment")
                .build();
        Review review = Review.builder().id(reviewId).build();
        Member member = Member.builder().id(memberId).build();
        Comment comment = Comment.builder()
                .review(review)
                .member(member)
                .comment_content(commentDTO.getCommentContent())
                .build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDTO result = commentService.createComment(reviewId, memberId, commentDTO);

        verify(reviewRepository, times(1)).findById(reviewId);
        verify(memberRepository, times(1)).findById(memberId);
        verify(commentRepository, times(1)).save(any(Comment.class));
        assertThat(result.getCommentContent()).isEqualTo(commentDTO.getCommentContent());
    }

    @Test
    void testUpdateComment() {
        Long commentId = 1L;
        Integer memberId = 1;
        CommentDTO commentDTO = CommentDTO.builder()
                .reviewId(1L)
                .memberId(memberId)
                .commentContent("Updated comment")
                .build();
        Review review = Review.builder().id(commentDTO.getReviewId()).build();
        Member member = Member.builder().id(memberId).build();

        Comment comment = Comment.builder()
                .id(commentId)
                .review(review)
                .member(member)
                .comment_content("Original comment")
                .build();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(reviewRepository.findById(commentDTO.getReviewId())).thenReturn(Optional.of(review));
        when(memberRepository.findById(commentDTO.getMemberId())).thenReturn(Optional.of(member));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDTO result = commentService.updateComment(commentId, memberId, commentDTO);

        verify(commentRepository, times(1)).findById(commentId);
        verify(reviewRepository, times(1)).findById(commentDTO.getReviewId());
        verify(memberRepository, times(1)).findById(commentDTO.getMemberId());
        verify(commentRepository, times(1)).save(any(Comment.class));
        assertThat(result.getCommentContent()).isEqualTo(commentDTO.getCommentContent());
    }

    @Test
    void testDeleteById() {
        Long commentId = 1L;

        when(commentRepository.existsById(commentId)).thenReturn(true);

        commentService.deleteById(commentId);

        verify(commentRepository, times(1)).existsById(commentId);
        verify(commentRepository, times(1)).deleteById(commentId);
    }
}

