package com.colorpl.review.service;

import com.colorpl.comment.domain.Comment;
import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.comment.repository.CommentRepository;
import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.review.domain.Review;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {

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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Review createMockReview(Long id) {
        return Review.builder()
                .id(id)
                .content("Review content")
//                .createDate(LocalDateTime.now())
                .spoiler(false)
                .emotion((byte) 1)
                .emphathy(10)
                .comments(Collections.emptyList())
                .build();
    }

    private Ticket createMockTicket(Long id, Review review) {
        return Ticket.builder()
                .id(id)
                .name("Ticket name")
                .review(review)
                .build();
    }

    private Member createMockMemberWithTickets(Integer memberId, List<Ticket> tickets) {
        return Member.builder()
                .id(memberId)
                .nickname("MockUser")
                .tickets(tickets)
                .build();
    }

    @Test
    void testFindReviewsOfMember() {
        Integer memberId = 1;
        int page = 0;
        int size = 10;
        Review review = createMockReview(1L);
        Ticket ticket = createMockTicket(1L, review);
        Member member = createMockMemberWithTickets(memberId, List.of(ticket));

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(empathyRepository.findByReviewAndMember(review, member)).thenReturn(Optional.empty());

        List<ReviewDTO> result = reviewService.findReviewsOfMember(memberId, page, size);

        assertEquals(1, result.size());
        verify(memberRepository, times(2)).findById(memberId);
        verify(empathyRepository, times(1)).findByReviewAndMember(review, member);
    }

    @Test
    void testFindReviewsOfMember_MemberNotFound() {
        Integer memberId = 1;
        int page = 0;
        int size = 10;

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        try {
            reviewService.findReviewsOfMember(memberId, page, size);
        } catch (MemberNotFoundException e) {
            // expected
        }

        verify(memberRepository, times(1)).findById(memberId);
        verifyNoMoreInteractions(empathyRepository);
    }

    @Test
    void testFindReviewsOfMember_NoReviews() {
        Integer memberId = 1;
        int page = 0;
        int size = 10;
        Member member = createMockMemberWithTickets(memberId, Collections.emptyList());

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        List<ReviewDTO> result = reviewService.findReviewsOfMember(memberId, page, size);

        assertEquals(0, result.size());
        verify(memberRepository, times(1)).findById(memberId);
        verifyNoMoreInteractions(empathyRepository);
    }
}
