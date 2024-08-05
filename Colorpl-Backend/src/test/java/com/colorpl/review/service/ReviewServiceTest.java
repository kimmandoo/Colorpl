package com.colorpl.review.service;

import com.colorpl.comment.domain.Comment;
import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.comment.repository.CommentRepository;
import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.global.common.exception.ReviewNotFoundException;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.review.domain.Empathy;
import com.colorpl.review.domain.EmpathyId;
import com.colorpl.review.domain.Review;
import com.colorpl.review.dto.DetailReviewDTO;
import com.colorpl.review.dto.ReviewDTO;
import com.colorpl.review.repository.EmpathyRepository;
import com.colorpl.review.repository.ReviewRepository;
import com.colorpl.ticket.domain.Ticket;
import com.colorpl.ticket.domain.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private EmpathyRepository empathyRepository;

    @InjectMocks
    private ReviewService reviewService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getReviews_ShouldReturnReviewDTOList() {
        Integer memberId = 1;
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Review review = Review.builder().id(1L).content("Test content").build();

        when(reviewRepository.findAll(pageable)).thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(review)));

        List<ReviewDTO> result = reviewService.getReviews(memberId, page, size);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reviewRepository, times(1)).findAll(pageable);
    }

    @Test
    void findReviewsOfMember_ShouldReturnReviewDTOList() {
        Integer memberId = 1;
        int page = 0;
        int size = 10;

        Review review = Review.builder().id(1L).content("Test content").build();
        Ticket ticket = Ticket.builder().id(1L).review(review).build();
        Member member = Member.builder().id(memberId).tickets(List.of(ticket)).build();
        ticket = Ticket.builder().id(1L).review(review).member(member).build(); // Ensure the ticket is linked to the member

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        List<ReviewDTO> result = reviewService.findReviewsOfMember(memberId, page, size);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(memberRepository, times(1)).findById(memberId);
    }

    @Test
    void findById_ShouldReturnReviewDTO() {
        Long reviewId = 1L;
        Integer memberId = 1;
        Review review = Review.builder().id(reviewId).content("Test content").build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        ReviewDTO result = reviewService.findById(reviewId, memberId);

        assertNotNull(result);
        assertEquals(reviewId, result.getId());
        verify(reviewRepository, times(1)).findById(reviewId);
    }

    @Test
    void createReview_ShouldReturnDetailReviewDTO() {
        Integer memberId = 1;
        Long ticketId = 1L;
        DetailReviewDTO detailReviewDTO = DetailReviewDTO.builder().content("Test content").build();
        Member member = Member.builder().id(memberId).build();
        Ticket ticket = Ticket.builder().id(ticketId).build();
        Review review = Review.builder().ticket(ticket).content("Test content").build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        DetailReviewDTO result = reviewService.createReview(memberId, ticketId, detailReviewDTO);

        assertNotNull(result);
        assertEquals("Test content", result.getContent());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void updateReview_ShouldReturnUpdatedDetailReviewDTO() {
        Integer memberId = 1;
        Long reviewId = 1L;
        DetailReviewDTO detailReviewDTO = DetailReviewDTO.builder().content("Updated content").build();
        Review review = Review.builder().id(reviewId).content("Original content").build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        DetailReviewDTO result = reviewService.updateReview(memberId, reviewId, detailReviewDTO);

        assertNotNull(result);
        assertEquals("Updated content", result.getContent());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void deleteById_ShouldDeleteReview() {
        Long reviewId = 1L;

        when(reviewRepository.existsById(reviewId)).thenReturn(true);

        reviewService.deleteById(reviewId);

        verify(reviewRepository, times(1)).deleteById(reviewId);
    }

    @Test
    void findByReviewAndMember_ShouldReturnEmpathy() {
        Long reviewId = 1L;
        Integer memberId = 1;
        Empathy empathy = Empathy.builder().id(new EmpathyId(reviewId, memberId)).build();

        when(empathyRepository.findById(new EmpathyId(reviewId, memberId))).thenReturn(Optional.of(empathy));

        Optional<Empathy> result = reviewService.findByReviewAndMember(reviewId, memberId);

        assertTrue(result.isPresent());
        assertEquals(empathy, result.get());
        verify(empathyRepository, times(1)).findById(new EmpathyId(reviewId, memberId));
    }

    @Test
    void toReviewDTO_ShouldReturnReviewDTO() {
        Integer memberId = 1;
        Review review = Review.builder().id(1L).content("Test content").build();

        ReviewDTO result = reviewService.toReviewDTO(memberId, review);

        assertNotNull(result);
        assertEquals(review.getId(), result.getId());
        assertEquals(review.getContent(), result.getContent());
    }
}
