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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CommentService commentService;

    @InjectMocks
    private ReviewService reviewService;

    @InjectMocks
    private MemberService memberService;

    private Comment comment;
    private Member member;
    private Review review;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        member = Member.builder()
                .id(1)
                .email("aaaa@gmail.com")
                .nickname("shelby")
                .password("pwd132@")
                .build();

        review = Review.builder()
                .id(1)
                .content("content")
                .emotion(3)
                .spoiler(false)
                .emphathy((byte) 3)
                .build();

        comment = Comment.builder()
                .id(1L)
                .content("Test Comment")
                .member(member)
                .review(review)
                .build();
    }

//    @Test
//    void findAll() {
//        when(commentRepository.findAll()).thenReturn(Collections.singletonList(comment));
//
//        List<CommentDTO> result = commentService.findAll();
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals("Test Comment", result.get(0).getContent());
//        assertEquals("aaaa@gmail.com", result.get(0).getMember().getEmail());
//        assertEquals("content", result.get(0).getReview().getContent());
//    }

    @Test
    void findById() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        CommentDTO result = commentService.findById(1L);

        assertNotNull(result);
        assertEquals("Test Comment", result.getContent());
        assertEquals("aaaa@gmail.com", result.getMember().getEmail());
        assertEquals("content", result.getReview().getContent());
    }

    @Test
    void findById_NotFound() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        CommentDTO result = commentService.findById(1L);

        assertNull(result);
    }

    @Test
    void save() {
        // Use the service to convert member and review to DTO
        MemberDTO memberDTO = memberService.toDTO(member);
        ReviewDTO reviewDTO = reviewService.toDTO(review);

        CommentDTO commentDTO = CommentDTO.builder()
                .id(1L)
                .content("Test Comment")
                .member(memberDTO)
                .review(reviewDTO)
                .build();
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDTO result = commentService.save(commentDTO);

        assertNotNull(result);
        assertEquals("Test Comment", result.getContent());
        assertEquals("aaaa@gmail.com", result.getMember().getEmail());
        assertEquals("content", result.getReview().getContent());
    }

    @Test
    void deleteById() {
        when(commentRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> commentService.deleteById(1L));

        verify(commentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_NotFound() {
        when(commentRepository.existsById(1L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> commentService.deleteById(1L));
    }
}
