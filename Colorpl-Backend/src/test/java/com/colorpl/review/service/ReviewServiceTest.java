package com.colorpl.review.service;


import com.colorpl.comment.domain.Comment;
import com.colorpl.review.domain.Review;
import com.colorpl.review.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ReviewServiceTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewService reviewService;

    @Test
    @Transactional
    void findAll() {
        Review review1 = Review.builder().build();
        review1.setContent("Great movie!");
        review1.setSpoiler(false);
        review1.setEmotion(5);
        review1.setEmphathy((byte) 10);

        Review review2 = Review.builder().build();
        review2.setContent("Not bad");
        review2.setSpoiler(false);
        review2.setEmotion(3);
        review2.setEmphathy((byte) 5);

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        List<Review> reviews = reviewService.findAll();
        assertThat(reviews).isNotNull();
        assertThat(reviews.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    void findById() {
        Review review = Review.builder().build();
        review.setContent("Great movie!");
        review.setSpoiler(false);
        review.setEmotion(5);
        review.setEmphathy((byte) 10);

        reviewRepository.save(review);
        Review foundReview = reviewService.findById(review.getId());

        assertThat(foundReview).isNotNull();
        assertThat(foundReview.getId()).isEqualTo(review.getId());
        assertThat(foundReview.getContent()).isEqualTo(review.getContent());

        // Checking for a non-existent ID
        Review notFoundReview = reviewService.findById(999);
        assertThat(notFoundReview).isNull();
    }

    @Test
    @Transactional
    void save() {
        Review review = Review.builder().build();
        review.setContent("Amazing film!");
        review.setSpoiler(false);
        review.setEmotion(5);
        review.setEmphathy((byte) 20);

        Review savedReview = reviewService.save(review);

        assertThat(savedReview).isNotNull();
        assertThat(savedReview.getId()).isNotNull();
        assertThat(savedReview.getContent()).isEqualTo(review.getContent());

        // Checking for mismatched content after saving
        assertNotEquals("Different content", savedReview.getContent());
    }

    @Test
    @Transactional
    void deleteById() {
        Review review = Review.builder().build();
        review.setContent("Great movie!");
        review.setSpoiler(false);
        review.setEmotion(5);
        review.setEmphathy((byte) 10);

        reviewRepository.save(review);
        Integer reviewId = review.getId();

        reviewService.deleteById(reviewId);
        Review deletedReview = reviewRepository.findById(reviewId).orElse(null);

        assertThat(deletedReview).isNull();

        // Trying to delete a non-existent review
        assertThrows(Exception.class, () -> {
            reviewService.deleteById(999);
        });
    }

    @Test
    @Transactional
    void updateReview() {
        Review review = Review.builder().build();
        review.setContent("Good movie!");
        review.setSpoiler(false);
        review.setEmotion(4);
        review.setEmphathy((byte) 8);

        Review savedReview = reviewService.save(review);
        savedReview.setContent("Updated content");
        savedReview.setEmotion(5);
        Review updatedReview = reviewService.save(savedReview);

        assertThat(updatedReview).isNotNull();
        assertThat(updatedReview.getId()).isEqualTo(savedReview.getId());
        assertThat(updatedReview.getContent()).isEqualTo("Updated content");
        assertThat(updatedReview.getEmotion()).isEqualTo(5);
    }

    @Test
    @Transactional
    void findByIdMismatch() {
        Review review = Review.builder().build();
        review.setContent("Good movie!");
        review.setSpoiler(false);
        review.setEmotion(4);
        review.setEmphathy((byte) 8);

        reviewRepository.save(review);
        Review foundReview = reviewService.findById(review.getId());

        assertThat(foundReview).isNotNull();
        assertThat(foundReview.getId()).isEqualTo(review.getId());

        // Checking for mismatched content
        assertNotEquals("Bad movie", foundReview.getContent());
    }
}