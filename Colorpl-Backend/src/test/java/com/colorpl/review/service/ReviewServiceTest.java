package com.colorpl.review.service;

import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.global.common.exception.ReviewNotFoundException;
import com.colorpl.global.common.storage.StorageService;
import com.colorpl.global.common.storage.UploadFile;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.review.domain.Empathy;
import com.colorpl.review.domain.EmpathyId;
import com.colorpl.review.domain.Review;
import com.colorpl.review.dto.ReadReviewResponse;
import com.colorpl.review.dto.RequestDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private StorageService storageService;

    @Mock
    private ReviewRepository reviewRepository;

//    @Mock
//    private CommentRepository commentRepository;

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
    void getReviews_ShouldReturnReadReviewResponse() {
        Integer memberId = 1;
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Review review = Review.builder().id(1L).content("Test content").build();

        when(reviewRepository.findAll(pageable)).thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(review)));
        when(reviewRepository.count()).thenReturn(1L);

        ReadReviewResponse result = reviewService.getReviews(memberId, page, size);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(1, result.getTotalPage());
        verify(reviewRepository, times(1)).findAll(pageable);
        verify(reviewRepository, times(1)).count();
    }

    @Test
    void findReviewsOfMember_ShouldReturnReadReviewResponse() {
        Integer memberId = 1;
        int page = 0;
        int size = 10;

        Review review = Review.builder().id(1L).content("Test content").build();
        Ticket ticket = Ticket.builder().id(1L).review(review).build();
        Member member = Member.builder().id(memberId).tickets(List.of(ticket)).build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        ReadReviewResponse result = reviewService.findReviewsOfMember(memberId, page, size);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
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
    void createReview_ShouldReturnReviewId() {
        // Create test data
        RequestDTO requestDTO = RequestDTO.builder()
                .memberId(1)
                .ticketId(1L)
                .content("Test content")
                .build();
        Member member = Member.builder().id(1).build();
        Ticket ticket = Ticket.builder().id(1L).build();
        Review review = Review.builder().id(1L).content("Test content").ticket(ticket).build();
        MultipartFile file = mock(MultipartFile.class);
        UploadFile uploadFile = UploadFile.builder().storeFilename("storedFileName").uploadFilename("originalFileName").build();

        when(memberRepository.findById(1)).thenReturn(Optional.of(member));
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(storageService.storeFile(file)).thenReturn(uploadFile);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Long result = reviewService.createReview(requestDTO, file);

        assertNotNull(result);
        assertEquals(1L, result);
        verify(memberRepository, times(1)).findById(1);
        verify(ticketRepository, times(1)).findById(1L);
        verify(storageService, times(1)).storeFile(file);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void updateReview_ShouldReturnUpdatedReviewDTO() {
        Integer memberId = 1;
        Long reviewId = 1L;
        RequestDTO requestDTO = RequestDTO.builder().spoiler(false).content("Updated content").build();
        Review review = Review.builder().id(reviewId).emotion((byte)3).spoiler(true).content("Original content").build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ReviewDTO result = reviewService.updateReview(memberId, reviewId, requestDTO);

        assertNotNull(result);
        assertEquals("Updated content", result.getContent());
        assertEquals(false, result.getSpoiler());
        assertEquals((byte)3, result.getEmotion());
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

        when(empathyRepository.findById(new EmpathyId(1L, memberId))).thenReturn(Optional.of(new Empathy()));

        ReviewDTO result = reviewService.toReviewDTO(memberId, review);

        assertNotNull(result);
        assertEquals(review.getId(), result.getId());
        assertEquals(review.getContent(), result.getContent());
    }
}
