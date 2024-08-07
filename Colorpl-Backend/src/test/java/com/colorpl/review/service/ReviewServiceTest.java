package com.colorpl.review.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.colorpl.global.common.storage.StorageService;
import com.colorpl.global.common.storage.UploadFile;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.review.domain.Empathy;
import com.colorpl.review.domain.EmpathyId;
import com.colorpl.review.domain.Review;
import com.colorpl.review.dto.RequestDTO;
import com.colorpl.review.repository.EmpathyRepository;
import com.colorpl.review.repository.ReviewRepository;
import com.colorpl.schedule.domain.CustomSchedule;
import com.colorpl.schedule.domain.Schedule;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

class ReviewServiceTest {

    @Mock

    private StorageService storageService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private EmpathyRepository empathyRepository;

    @InjectMocks
    private ReviewService reviewService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        "yyyy년 MM월 dd일 HH:mm");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createReview_ShouldReturnReviewIdWithFile() {
        // Create search data
        RequestDTO requestDTO = RequestDTO.builder()
            .memberId(1)
            .ticketId(1L)
            .content("Test content")
            .build();
        Member member = Member.builder().id(1).build();
        Schedule schedule = CustomSchedule.builder().id(1L).build();
        Review review = Review.builder().id(1L).content("Test content").schedule(schedule).build();
        MultipartFile file = mock(MultipartFile.class);
        UploadFile uploadFile = UploadFile.builder().storeFilename("storedFileName")
            .uploadFilename("originalFileName").build();

        when(memberRepository.findById(1)).thenReturn(Optional.of(member));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(storageService.storeFile(file)).thenReturn(uploadFile);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Long result = reviewService.createReview(requestDTO, file);

        assertNotNull(result);
        assertEquals(1L, result);
        verify(memberRepository, times(1)).findById(1);
        verify(scheduleRepository, times(1)).findById(1L);
        verify(storageService, times(1)).storeFile(file);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void createReview_ShouldReturnReviewIdWithoutFile() {
        // Create search data
        RequestDTO requestDTO = RequestDTO.builder()
            .memberId(1)
            .ticketId(1L)
            .content("Test content")
            .build();
        Member member = Member.builder().id(1).build();
        Schedule schedule = CustomSchedule.builder().id(1L).build();
        Review review = Review.builder().id(1L).content("Test content").schedule(schedule).build();

        when(memberRepository.findById(1)).thenReturn(Optional.of(member));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Long result = reviewService.createReview(requestDTO, null);

        assertNotNull(result);
        assertEquals(1L, result);

        // Verify that storageService.storeFile was not called
        verify(storageService, never()).storeFile(any(MultipartFile.class));

        // Capture the argument passed to reviewRepository.save
        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository, times(1)).save(reviewCaptor.capture());

        // Check that the filename is correctly set
        Review savedReview = reviewCaptor.getValue();
        assertEquals("noimg", savedReview.getFilename());
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

        when(empathyRepository.findById(new EmpathyId(reviewId, memberId))).thenReturn(
            Optional.of(empathy));

        Optional<Empathy> result = reviewService.findByReviewAndMember(reviewId, memberId);

        assertTrue(result.isPresent());
        assertEquals(empathy, result.get());
        verify(empathyRepository, times(1)).findById(new EmpathyId(reviewId, memberId));
    }

}
