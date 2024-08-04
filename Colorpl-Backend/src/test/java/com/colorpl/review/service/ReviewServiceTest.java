package com.colorpl.review.service;

import com.colorpl.comment.domain.Comment;
import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.comment.repository.CommentRepository;
import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.global.common.exception.ReviewNotFoundException;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.review.domain.Review;
import com.colorpl.review.dto.DetailReviewDTO;
import com.colorpl.review.dto.ReviewDTO;
import com.colorpl.review.repository.ReviewRepository;
import com.colorpl.ticket.domain.Ticket;
import com.colorpl.ticket.domain.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Member createMockMemberWithTickets(int memberId, List<Ticket> tickets) {
        Member member = mock(Member.class);
        when(member.getId()).thenReturn(memberId);
        when(member.getTickets()).thenReturn(tickets);
        return member;
    }

    private Review createMockReview(Long reviewId) {
        Review review = mock(Review.class);
        when(review.getId()).thenReturn(reviewId);
        return review;
    }

    private Ticket createMockTicket(Long ticketId, Review review) {
        Ticket ticket = mock(Ticket.class);
        when(ticket.getId()).thenReturn(ticketId);
        when(ticket.getReview()).thenReturn(review);
        return ticket;
    }

    private Page<Review> createMockReviewPage(List<Review> reviews, Pageable pageable) {
        return new PageImpl<>(reviews, pageable, reviews.size());
    }

    @Test
    void testGetReviews() {
        int memberId = 1;
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        List<Review> reviews = new ArrayList<>();
        reviews.add(createMockReview(1L));
        Page<Review> reviewPage = createMockReviewPage(reviews, pageable);

        when(reviewRepository.findAll(pageable)).thenReturn(reviewPage);

        List<ReviewDTO> result = reviewService.getReviews(memberId, page, size);

        assertEquals(1, result.size());
        verify(reviewRepository, times(1)).findAll(pageable);
    }

    @Test
    void testFindReviewsOfMember() {
        int memberId = 1;
        int page = 0;
        int size = 10;
        Review review = createMockReview(1L);
        Ticket ticket = createMockTicket(1L, review);
        Member member = createMockMemberWithTickets(memberId, List.of(ticket));

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        List<ReviewDTO> result = reviewService.findReviewsOfMember(memberId, page, size);

        assertEquals(1, result.size());
        verify(memberRepository, times(1)).findById(memberId);
    }

    @Test
    void testFindById() {
        Long reviewId = 1L;
        int memberId = 1;
        Review review = createMockReview(reviewId);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        DetailReviewDTO result = reviewService.findById(reviewId, memberId);

        assertNotNull(result);
        verify(reviewRepository, times(1)).findById(reviewId);
    }

    @Test
    void testCreateReview() {
        int memberId = 1;
        Long ticketId = 1L;
        DetailReviewDTO detailReviewDTO = DetailReviewDTO.builder()
                .content("Sample Content")
                .spoiler(false)
                .emotion((byte)3)
                .empathy(10)
                .comments(new ArrayList<>())
                .build();
        Member member = createMockMemberWithTickets(memberId, new ArrayList<>());
        Ticket ticket = createMockTicket(ticketId, null);
        Review review = createMockReview(1L);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        DetailReviewDTO result = reviewService.createReview(memberId, ticketId, detailReviewDTO);

        assertNotNull(result);
        verify(memberRepository, times(1)).findById(memberId);
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testUpdateReview() {
        int memberId = 1;
        Long reviewId = 1L;
        DetailReviewDTO detailReviewDTO = DetailReviewDTO.builder()
                .content("Updated Content")
                .spoiler(true)
                .emotion((byte)3)
                .empathy(5)
                .comments(new ArrayList<>())
                .build();
        Review review = createMockReview(reviewId);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        DetailReviewDTO result = reviewService.updateReview(memberId, reviewId, detailReviewDTO);

        assertNotNull(result);
        verify(reviewRepository, times(1)).findById(reviewId);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testDeleteById() {
        Long id = 1L;

        when(reviewRepository.existsById(id)).thenReturn(true);

        reviewService.deleteById(id);

        verify(reviewRepository, times(1)).existsById(id);
        verify(reviewRepository, times(1)).deleteById(id);
    }
}
