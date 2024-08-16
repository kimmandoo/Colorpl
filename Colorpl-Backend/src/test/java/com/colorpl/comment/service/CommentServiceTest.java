package com.colorpl.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.colorpl.comment.domain.Comment;
import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.comment.dto.CommentRequestDTO;
import com.colorpl.comment.repository.CommentRepository;
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
        Integer memberId = 1;
        String commentContent = "New comment";

        Review review = Review.builder().id(reviewId).build();
        Member member = Member.builder().id(memberId).build();
        Comment comment = Comment.builder()
                .id(1L)
                .review(review)
                .member(member)
                .comment_content(commentContent)
                .build();

        Page<Comment> commentPage = new PageImpl<>(List.of(comment));

        // Set up mocks
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(commentRepository.findByReviewId(reviewId, pageable)).thenReturn(commentPage);

        // Call service method
        Page<CommentDTO> result = commentService.getCommentsByReviewId(memberId, reviewId, pageable);

        // Verify interactions
        verify(reviewRepository, times(1)).findById(reviewId);
        verify(commentRepository, times(1)).findByReviewId(reviewId, pageable);

        // Check results
        assertEquals(1, result.getTotalElements());
        CommentDTO resultDTO = result.getContent().get(0);
        assertThat(resultDTO.getCommentContent()).isEqualTo(commentContent);
        assertThat(resultDTO.getMemberId()).isEqualTo(memberId);
    }


    @Test
    void testCreateComment() {
        Long reviewId = 1L;
        Integer memberId = 1;
        CommentRequestDTO commentRequestDTO = CommentRequestDTO.builder()
                .commentContent("New comment")
                .build();

        Review review = Review.builder().id(reviewId).build();
        Member member = Member.builder().id(memberId).build();
        Comment comment = Comment.builder()
                .review(review)
                .member(member)
                .comment_content(commentRequestDTO.getCommentContent())
                .build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDTO result = commentService.createComment(reviewId, memberId, commentRequestDTO);

        verify(reviewRepository, times(1)).findById(reviewId);
        verify(memberRepository, times(1)).findById(memberId);
        verify(commentRepository, times(1)).save(any(Comment.class));

        assertThat(result.getCommentContent()).isEqualTo(commentRequestDTO.getCommentContent());
        assertThat(result.getMemberId()).isEqualTo(memberId);
    }



    @Test
    void testUpdateComment() {
        Long commentId = 1L;
        Integer memberId = 1;
        Long reviewId = 1L;
        CommentRequestDTO commentRequestDTO = CommentRequestDTO.builder()
                .commentContent("Updated comment")
                .build();

        Review review = Review.builder().id(reviewId).build();
        Member member = Member.builder().id(memberId).build();
        Comment existingComment = Comment.builder()
                .id(commentId)
                .review(review)
                .member(member)
                .comment_content("Original comment")
                .build();

        Comment updatedComment = Comment.builder()
                .id(commentId)
                .review(review)
                .member(member)
                .comment_content(commentRequestDTO.getCommentContent())
                .build();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(commentRepository.save(any(Comment.class))).thenReturn(updatedComment);

        CommentDTO result = commentService.updateComment(commentId, reviewId, memberId, commentRequestDTO);

        verify(commentRepository, times(1)).findById(commentId);
        verify(reviewRepository, times(1)).findById(reviewId);
        verify(memberRepository, times(1)).findById(memberId);
        verify(commentRepository, times(1)).save(any(Comment.class));

        assertThat(result.getCommentContent()).isEqualTo(commentRequestDTO.getCommentContent());
        assertThat(result.getMemberId()).isEqualTo(memberId);
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

