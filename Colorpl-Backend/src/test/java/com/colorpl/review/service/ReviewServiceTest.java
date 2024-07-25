package com.colorpl.review.service;

import com.colorpl.comment.domain.Comment;
import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.review.domain.Review;
import com.colorpl.review.dto.ReviewDTO;
import com.colorpl.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        Comment comment = Comment.builder()
                .id(1L)
                .review(Review.builder().id(1).build())
                .member(null) // Use appropriate Member instance if needed
                .content("Test Comment")
                .build();

        Review review = Review.builder()
                .id(1)
                .content("Test Review")
                .spoiler(false)
                .emotion(5)
                .emphathy((byte) 3)
                .comments(Collections.singletonList(comment))
                .build();

        List<Review> reviews = Collections.singletonList(review);

        when(reviewRepository.findAll()).thenReturn(reviews);

        List<ReviewDTO> result = reviewService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Review", result.get(0).getContent());
    }

    @Test
    void findById() {
        Comment comment = Comment.builder()
                .id(1L)
                .review(Review.builder().id(1).build())
                .member(null)
                .content("Test Comment")
                .build();

        Review review = Review.builder()
                .id(1)
                .content("Test Review")
                .spoiler(false)
                .emotion(5)
                .emphathy((byte) 3)
                .comments(Collections.singletonList(comment))
                .build();

        when(reviewRepository.findById(1)).thenReturn(Optional.of(review));

        ReviewDTO result = reviewService.findById(1);

        assertNotNull(result);
        assertEquals("Test Review", result.getContent());
    }

    @Test
    void findById_NotFound() {
        when(reviewRepository.findById(1)).thenReturn(Optional.empty());

        ReviewDTO result = reviewService.findById(1);

        assertNull(result);
    }

    @Test
    void save() {
        CommentDTO commentDTO = CommentDTO.builder()
                .id(1L)
                .review(ReviewDTO.builder().id(1).build())
                .member(null)
                .content("Test Comment")
                .build();

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .id(1)
                .content("Test Review")
                .spoiler(false)
                .emotion(5)
                .empathy((byte) 3)
                .comments(Collections.singletonList(commentDTO))
                .build();

        Review review = Review.builder()
                .id(1)
                .content("Test Review")
                .spoiler(false)
                .emotion(5)
                .emphathy((byte) 3)
                .comments(Collections.singletonList(Comment.builder().id(1L).content("Test Comment").build()))
                .build();


        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ReviewDTO result = reviewService.save(reviewDTO);

        assertNotNull(result);
        assertEquals("Test Review", result.getContent());
    }

    @Test
    void deleteById() {
        when(reviewRepository.existsById(1)).thenReturn(true);

        assertDoesNotThrow(() -> reviewService.deleteById(1));

        verify(reviewRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteById_NotFound() {
        when(reviewRepository.existsById(1)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> reviewService.deleteById(1));
    }
}
