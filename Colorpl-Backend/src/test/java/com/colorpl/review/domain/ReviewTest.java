package com.colorpl.review.service;

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
public class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MemberRepository memberRepository;

    // Additional mocks if needed (e.g., CommentRepository)

    @Test
    void testFindAll() {
        List<Review> reviews = new ArrayList<>();
        reviews.add(Review.builder().id(2).content("Test Review").build());

        when(reviewRepository.findAll()).thenReturn(reviews);

        List<ReviewDTO> result = reviewService.findAll();

        verify(reviewRepository, times(1)).findAll();
        assertEquals(1, result.size());
        assertThat(result.get(0).getContent()).isEqualTo("Test Review");
    }

    @Test
    void testCreateReview() {
        Integer memberId = 1;
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .content("New Review")
                .spoiler(false)
                .emotion(3)
                .build();

        Member member = Member.builder().id(memberId).build();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReviewDTO createdReview = reviewService.createReview(memberId, reviewDTO);

        verify(memberRepository, times(1)).findById(memberId);
        verify(reviewRepository, times(1)).save(any(Review.class));
        assertThat(createdReview.getContent()).isEqualTo(reviewDTO.getContent());
        assertThat(createdReview.getSpoiler()).isEqualTo(reviewDTO.getSpoiler());
    }

    @Test
    void testUpdateReview() {
        Integer memberId = 1;
        Integer reviewId = 2;
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .content("Updated Review")
                .spoiler(true)
                .emotion(5)
                .build();

        Review review = Review.builder().id(reviewId).build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReviewDTO updatedReview = reviewService.updateReview(memberId, reviewId, reviewDTO);

        verify(reviewRepository, times(1)).findById(reviewId);
        verify(reviewRepository, times(1)).save(any(Review.class));
        assertThat(updatedReview.getContent()).isEqualTo(reviewDTO.getContent());
        assertThat(updatedReview.getSpoiler()).isEqualTo(reviewDTO.getSpoiler());
    }

    @Test
    void testDeleteById_ExistingReview() {
        Integer reviewId = 2;

        when(reviewRepository.existsById(reviewId)).thenReturn(true);

        reviewService.deleteById(reviewId.intValue()); // Assuming deleteById takes int

        verify(reviewRepository, times(1)).existsById(reviewId);
        verify(reviewRepository, times(1)).deleteById(reviewId);
    }

    @Test
    void testDeleteById_NonexistentReview() {
        Integer reviewId = 2;

        when(reviewRepository.existsById(reviewId)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> reviewService.deleteById(reviewId.intValue())); // Assuming deleteById takes int

        verify(reviewRepository, times(1)).existsById(reviewId);
        verify(reviewRepository, never()).deleteById(reviewId);
    }

    // Add more tests for other methods (e.g., with comments, additional logic)
}
