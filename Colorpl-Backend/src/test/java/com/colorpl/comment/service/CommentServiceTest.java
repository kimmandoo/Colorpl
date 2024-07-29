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
import com.colorpl.review.dto.ReviewDTO;
import com.colorpl.review.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        Integer reviewId = 1;
        List<Comment> comments = new ArrayList<>();
        comments.add(Comment.builder().comment_content("Test Comment").build());

        when(commentRepository.findByReviewId(reviewId)).thenReturn(comments);

        List<CommentDTO> result = commentService.getCommetnsByReviewId(reviewId);

        verify(commentRepository, times(1)).findByReviewId(reviewId);
        assertEquals(1, result.size());
        assertThat(result.get(0).getComment_content()).isEqualTo("Test Comment");
    }

    @Test
    void testGetCommentsByReviewId_ReviewNotFound() {
        Integer reviewId = 1;

        when(commentRepository.findByReviewId(reviewId)).thenReturn(new ArrayList<>());

        assertThrows(ReviewNotFoundException.class, () -> commentService.getCommetnsByReviewId(reviewId));

        verify(commentRepository, times(1)).findByReviewId(reviewId);
    }

    @Test
    void testCreateComment() {
        Integer reviewId = 1;
        Integer memberId = 1;
        CommentDTO commentDTO = CommentDTO.builder()
                .comment_content("New Comment")
                .build();

        Review review = Review.builder().id(reviewId).build();
        Member member = Member.builder().id(memberId).build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CommentDTO createdComment = commentService.createComment(reviewId, memberId, commentDTO);

        verify(reviewRepository, times(1)).findById(reviewId);
        verify(memberRepository, times(1)).findById(memberId);
        verify(commentRepository, times(1)).save(any(Comment.class));
        assertThat(createdComment.getComment_content()).isEqualTo(commentDTO.getComment_content());
    }

    @Test
    void testCreateComment_ReviewNotFound() {
        Integer reviewId = 1;
        Integer memberId = 1;
        CommentDTO commentDTO = CommentDTO.builder().comment_content("New Comment").build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () -> commentService.createComment(reviewId, memberId, commentDTO));

        verify(reviewRepository, times(1)).findById(reviewId);
        verify(memberRepository, never()).findById(memberId);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testCreateComment_MemberNotFound() {
        Integer reviewId = 1;
        Integer memberId = 1;
        CommentDTO commentDTO = CommentDTO.builder().comment_content("New Comment").build();

        Integer scheduleId = 3;
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .content("Updated Review")
                .spoiler(true)
                .emotion(5)
                .build();
        Review review = Review.builder().id(reviewId).build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> commentService.createComment(reviewId, memberId, commentDTO));

        verify(reviewRepository, times(1)).findById(reviewId);
        verify(memberRepository, times(1)).findById(memberId);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testUpdateComment() {
        Long commentId = 1L;
        Integer memberId = 1;
        CommentDTO commentDTO = CommentDTO.builder()
                .comment_content("Updated Comment")
                .build();

        Comment comment = Comment.builder()
                .id(commentId)
                .member(Member.builder().id(memberId).build())
                .build();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CommentDTO updatedComment = commentService.updateComment(commentId, memberId, commentDTO);

        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).save(any(Comment.class));
        assertThat(updatedComment.getComment_content()).isEqualTo(commentDTO.getComment_content());
    }

    @Test
    void testUpdateComment_MemberMismatch() {
        Long commentId = 1L;
        Integer memberId = 1;
        CommentDTO commentDTO = CommentDTO.builder()
                .comment_content("Updated Comment")
                .build();

        Comment comment = Comment.builder()
                .id(commentId)
                .member(Member.builder().id(2).build()) // Different memberId
                .build();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        assertThrows(MemberMismatchException.class, () -> commentService.updateComment(commentId, memberId, commentDTO));

        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testDeleteById_ExistingComment() {
        Long commentId = 1L;

        doNothing().when(commentRepository).deleteById(commentId);

        commentService.deleteById(commentId);

        verify(commentRepository, times(1)).deleteById(commentId);
    }

    @Test
    void testDeleteById_NonexistentComment() {
        Long commentId = 1L;

        doThrow(NoSuchElementException.class).when(commentRepository).deleteById(commentId);

        assertThrows(NoSuchElementException.class, () -> commentService.deleteById(commentId));

        verify(commentRepository, times(1)).deleteById(commentId);
    }
}
