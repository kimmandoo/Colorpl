package com.colorpl.review.service;

import com.colorpl.comment.domain.Comment;
import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.review.domain.Review;
import com.colorpl.review.dto.ReviewDTO;
import com.colorpl.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<ReviewDTO> findAll() {
        return reviewRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ReviewDTO findById(Integer id) {
        return reviewRepository.findById(id).map(this::toDTO).orElse(null);
    }

    public ReviewDTO save(ReviewDTO reviewDTO) {
        Review review = toEntity(reviewDTO);
        Review savedReview = reviewRepository.save(review);
        return toDTO(savedReview);
    }

    public void deleteById(Integer id) {
        if (!reviewRepository.existsById(id)) {
            throw new NoSuchElementException("Review with ID " + id + " does not exist.");
        }
        reviewRepository.deleteById(id);
    }

    private ReviewDTO toDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .content(review.getContent())
                .spoiler(review.getSpoiler())
                .emotion(review.getEmotion())
                .empathy(review.getEmphathy())
                .comments(review.getComments().stream().map(this::toCommentDTO).collect(Collectors.toList()))
                .build();
    }

    private Review toEntity(ReviewDTO reviewDTO) {
        return Review.builder()
                .id(reviewDTO.getId())
                .content(reviewDTO.getContent())
                .spoiler(reviewDTO.getSpoiler())
                .emotion(reviewDTO.getEmotion())
                .emphathy(reviewDTO.getEmpathy())
                .comments(reviewDTO.getComments().stream().map(this::toCommentEntity).collect(Collectors.toList()))
                .build();
    }

    private CommentDTO toCommentDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .build();
    }

    private Comment toCommentEntity(CommentDTO commentDTO) {
        return Comment.builder()
                .id(commentDTO.getId())
                .content(commentDTO.getContent())
                .build();
    }
}